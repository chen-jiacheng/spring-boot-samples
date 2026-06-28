#!/usr/bin/env bash
set -euo pipefail

# ============================================================
# Maven Multi-Module Version Bump Script (Linux/macOS)
# ============================================================

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m'

log_info()  { echo -e "${GREEN}[INFO]${NC}  $*"; }
log_warn()  { echo -e "${YELLOW}[WARN]${NC}  $*"; }
log_error() { echo -e "${RED}[ERROR]${NC} $*"; }

usage() {
    cat <<EOF
用法: $0 [选项]

选项:
  -m, --mode <manual|auto>   升级模式（manual=手动输入, auto=自动递增）
  -t, --type <major|minor|patch>  自动模式下的升级类型（默认: patch）
  -v, --version <X.Y.Z[-SNAPSHOT]>  手动模式下指定的版本号
  -s, --skip-compile         跳过编译验证
  -h, --help                 显示帮助信息

示例:
  $0                                    # 交互式选择
  $0 -m auto                            # 自动补丁版本+1
  $0 -m auto -t minor                   # 自动次版本号+1
  $0 -m auto -t major                   # 自动主版本号+1
  $0 -m auto -s                         # 自动补丁版本+1，跳过编译
  $0 -m manual -v 2.0.0-SNAPSHOT        # 手动设置版本号
EOF
    exit 0
}

# ---- 参数解析 ----
MODE=""
UPGRADE_TYPE="patch"
MANUAL_VERSION=""
SKIP_COMPILE=false

while [[ $# -gt 0 ]]; do
    case "$1" in
        -m|--mode)
            MODE="$2"; shift 2 ;;
        -t|--type)
            UPGRADE_TYPE="$2"; shift 2 ;;
        -v|--version)
            MANUAL_VERSION="$2"; shift 2 ;;
        -s|--skip-compile)
            SKIP_COMPILE=true; shift ;;
        -h|--help)
            usage ;;
        *)
            log_error "未知参数: $1"; usage ;;
    esac
done

# ---- 检查 Maven 是否可用 ----
if ! command -v mvn &>/dev/null; then
    log_error "未找到 mvn 命令，请确认已安装 Maven 并配置到 PATH 中"
    exit 1
fi

# ---- 读取当前版本 ----
CURRENT_VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout -pl . 2>/dev/null || true)
if [[ -z "$CURRENT_VERSION" ]]; then
    log_error "无法从父 POM 中读取当前版本号"
    exit 1
fi
log_info "当前版本号: ${CYAN}${CURRENT_VERSION}${NC}"

# ---- 解析版本号 ----
# 支持格式: X.Y.Z 或 X.Y.Z-SNAPSHOT
parse_version() {
    local ver="$1"
    if [[ "$ver" =~ ^([0-9]+)\.([0-9]+)\.([0-9]+)(-SNAPSHOT)?$ ]]; then
        VER_MAJOR="${BASH_REMATCH[1]}"
        VER_MINOR="${BASH_REMATCH[2]}"
        VER_PATCH="${BASH_REMATCH[3]}"
        VER_SUFFIX="${BASH_REMATCH[4]:-}"
    else
        log_error "版本号格式不合法: $ver （期望格式: X.Y.Z 或 X.Y.Z-SNAPSHOT）"
        exit 1
    fi
}

# ---- 校验版本号格式 ----
validate_version() {
    local ver="$1"
    if [[ "$ver" =~ ^[0-9]+\.[0-9]+\.[0-9]+(-SNAPSHOT)?$ ]]; then
        return 0
    else
        return 1
    fi
}

# ---- 交互式选择模式 ----
if [[ -z "$MODE" ]]; then
    echo ""
    echo "请选择升级模式:"
    echo "  1) 自动模式 (auto)  - 自动递增版本号"
    echo "  2) 手动模式 (manual) - 输入自定义版本号"
    echo ""
    read -rp "请输入选择 [1/2] (默认: 1): " mode_choice
    case "${mode_choice:-1}" in
        1|auto)   MODE="auto" ;;
        2|manual) MODE="manual" ;;
        *)        log_error "无效选择: $mode_choice"; exit 1 ;;
    esac
fi

# ---- 确定目标版本号 ----
parse_version "$CURRENT_VERSION"

case "$MODE" in
    auto)
        # 交互式选择升级类型
        if [[ "$UPGRADE_TYPE" == "patch" ]] && [[ -t 0 ]]; then
            echo ""
            echo "请选择升级类型:"
            echo "  1) patch  补丁版本+1  (${VER_MAJOR}.${VER_MINOR}.${VER_PATCH} -> ${VER_MAJOR}.${VER_MINOR}.$((VER_PATCH + 1)))"
            echo "  2) minor  次版本号+1  (${VER_MAJOR}.${VER_MINOR}.${VER_PATCH} -> ${VER_MAJOR}.$((VER_MINOR + 1)).0)"
            echo "  3) major  主版本号+1  (${VER_MAJOR}.${VER_MINOR}.${VER_PATCH} -> $((VER_MAJOR + 1)).0.0)"
            echo ""
            read -rp "请输入选择 [1/2/3] (默认: 1): " type_choice
            case "${type_choice:-1}" in
                1|patch) UPGRADE_TYPE="patch" ;;
                2|minor) UPGRADE_TYPE="minor" ;;
                3|major) UPGRADE_TYPE="major" ;;
                *)       log_error "无效选择: $type_choice"; exit 1 ;;
            esac
        fi

        case "$UPGRADE_TYPE" in
            patch)
                NEW_MAJOR=$VER_MAJOR
                NEW_MINOR=$VER_MINOR
                NEW_PATCH=$((VER_PATCH + 1))
                ;;
            minor)
                NEW_MAJOR=$VER_MAJOR
                NEW_MINOR=$((VER_MINOR + 1))
                NEW_PATCH=0
                ;;
            major)
                NEW_MAJOR=$((VER_MAJOR + 1))
                NEW_MINOR=0
                NEW_PATCH=0
                ;;
            *)
                log_error "无效的升级类型: $UPGRADE_TYPE （可选: major, minor, patch）"
                exit 1
                ;;
        esac
        TARGET_VERSION="${NEW_MAJOR}.${NEW_MINOR}.${NEW_PATCH}${VER_SUFFIX}"
        ;;

    manual)
        if [[ -z "$MANUAL_VERSION" ]]; then
            echo ""
            read -rp "请输入目标版本号 (格式: X.Y.Z 或 X.Y.Z-SNAPSHOT): " MANUAL_VERSION
        fi
        if ! validate_version "$MANUAL_VERSION"; then
            log_error "版本号格式不合法: $MANUAL_VERSION"
            log_error "期望格式: X.Y.Z 或 X.Y.Z-SNAPSHOT（例如: 1.2.3 或 1.2.3-SNAPSHOT）"
            exit 1
        fi
        TARGET_VERSION="$MANUAL_VERSION"
        ;;

    *)
        log_error "无效的模式: $MODE （可选: auto, manual）"
        exit 1
        ;;
esac

# ---- 确认操作 ----
log_info "版本号升级: ${CYAN}${CURRENT_VERSION}${NC} -> ${CYAN}${TARGET_VERSION}${NC}"
echo ""
if [[ -t 0 ]]; then
    read -rp "确认执行? [Y/n]: " confirm
    if [[ "$confirm" =~ ^[Nn] ]]; then
        log_warn "已取消操作"
        exit 0
    fi
fi

# ---- 执行版本更新 ----
log_info "正在更新所有模块版本号..."
mvn versions:set -DnewVersion="$TARGET_VERSION" -DgenerateBackupPoms=true -q
if [[ $? -ne 0 ]]; then
    log_error "版本号更新失败"
    exit 1
fi

# 同步子模块的 parent 版本
mvn versions:update-child-modules -q 2>/dev/null || true

log_info "版本号更新成功"

# ---- 清理备份文件 ----
log_info "正在清理 pom.xml.versionsBackup 备份文件..."
find "$SCRIPT_DIR" -name "pom.xml.versionsBackup" -type f -delete
log_info "备份文件清理完成"

# ---- 编译验证 ----
if [[ "$SKIP_COMPILE" == true ]]; then
    log_warn "已跳过编译验证"
else
    log_info "正在执行编译验证 (mvn clean compile)..."
    if mvn clean compile -q; then
        log_info "编译验证通过 ✓"
    else
        log_error "编译验证失败！请检查项目代码"
        exit 1
    fi
fi

echo ""
log_info "========================================="
log_info "  版本升级完成: ${CYAN}${CURRENT_VERSION}${NC} -> ${CYAN}${TARGET_VERSION}${NC}"
log_info "========================================="
