@echo off
setlocal enabledelayedexpansion
chcp 65001 >nul 2>&1

REM ============================================================
REM Maven Multi-Module Version Bump Script (Windows)
REM ============================================================

cd /d "%~dp0"

REM ---- 参数解析 ----
set "MODE="
set "UPGRADE_TYPE=patch"
set "MANUAL_VERSION="
set "SKIP_COMPILE=false"

:parse_args
if "%~1"=="" goto :args_done
if /i "%~1"=="-m"    (set "MODE=%~2" & shift & shift & goto :parse_args)
if /i "%~1"=="--mode" (set "MODE=%~2" & shift & shift & goto :parse_args)
if /i "%~1"=="-t"    (set "UPGRADE_TYPE=%~2" & shift & shift & goto :parse_args)
if /i "%~1"=="--type" (set "UPGRADE_TYPE=%~2" & shift & shift & goto :parse_args)
if /i "%~1"=="-v"    (set "MANUAL_VERSION=%~2" & shift & shift & goto :parse_args)
if /i "%~1"=="--version" (set "MANUAL_VERSION=%~2" & shift & shift & goto :parse_args)
if /i "%~1"=="-s"    (set "SKIP_COMPILE=true" & shift & goto :parse_args)
if /i "%~1"=="--skip-compile" (set "SKIP_COMPILE=true" & shift & goto :parse_args)
if /i "%~1"=="-h"    goto :usage
if /i "%~1"=="--help" goto :usage
echo [ERROR] 未知参数: %~1
goto :usage

:args_done

REM ---- 检查 Maven ----
where mvn >nul 2>&1
if errorlevel 1 (
    echo [ERROR] 未找到 mvn 命令，请确认已安装 Maven 并配置到 PATH 中
    exit /b 1
)

REM ---- 读取当前版本 ----
REM 直接从 pom.xml 解析第一个 <version> 标签值（即父 POM 版本）
for /f "delims=" %%L in ('findstr /b /c:"    <version>" "%~dp0pom.xml"') do (
    set "_vline=%%L"
    set "_vline=!_vline:*<version>=!"
    set "_vline=!_vline:</version>=!"
    for /f "delims= " %%x in ("!_vline!") do set "CURRENT_VERSION=%%x"
    goto :version_found
)
:version_found
if not defined CURRENT_VERSION (
    echo [ERROR] 无法从父 POM 中读取当前版本号
    exit /b 1
)
echo [INFO]  当前版本号: !CURRENT_VERSION!

REM ---- 解析版本号 ----
call :parse_version "%CURRENT_VERSION%"

REM ---- 交互式选择模式 ----
if not defined MODE (
    echo.
    echo 请选择升级模式:
    echo   1^) 自动模式 (auto^)    - 自动递增版本号
    echo   2^) 手动模式 (manual^)   - 输入自定义版本号
    echo   3^) 快照模式 (snapshot^) - 添加或移除 SNAPSHOT 标识
    echo.
    set /p "mode_choice=请输入选择 [1/2/3] (默认: 1): "
    if "!mode_choice!"=="" set "mode_choice=1"
    if "!mode_choice!"=="1" set "MODE=auto"
    if "!mode_choice!"=="2" set "MODE=manual"
    if "!mode_choice!"=="3" set "MODE=snapshot"
    if not defined MODE (
        echo [ERROR] 无效选择: !mode_choice!
        exit /b 1
    )
)

REM ---- 确定目标版本号 ----
if /i "!MODE!"=="auto" goto :mode_auto
if /i "!MODE!"=="manual" goto :mode_manual
if /i "!MODE!"=="snapshot" goto :mode_snapshot
echo [ERROR] 无效的模式: !MODE! ^(可选: auto, manual, snapshot^)
exit /b 1

:mode_auto
REM 交互式选择升级类型
if /i "!UPGRADE_TYPE!"=="patch" (
    echo.
    echo 请选择升级类型:
    echo   1^) patch  补丁版本+1  ^(!VER_MAJOR!.!VER_MINOR!.!VER_PATCH! -^> !VER_MAJOR!.!VER_MINOR!.!VER_PATCH_NEXT!^)
    echo   2^) minor  次版本号+1  ^(!VER_MAJOR!.!VER_MINOR!.!VER_PATCH! -^> !VER_MAJOR!.!VER_MINOR_NEXT!.0^)
    echo   3^) major  主版本号+1  ^(!VER_MAJOR!.!VER_MINOR!.!VER_PATCH! -^> !VER_MAJOR_NEXT!.0.0^)
    echo.
    set /p "type_choice=请输入选择 [1/2/3] (默认: 1): "
    if "!type_choice!"=="" set "type_choice=1"
    if "!type_choice!"=="1" set "UPGRADE_TYPE=patch"
    if "!type_choice!"=="2" set "UPGRADE_TYPE=minor"
    if "!type_choice!"=="3" set "UPGRADE_TYPE=major"
)

if /i "!UPGRADE_TYPE!"=="patch" (
    set "NEW_MAJOR=!VER_MAJOR!"
    set "NEW_MINOR=!VER_MINOR!"
    set /a "NEW_PATCH=!VER_PATCH!+1"
) else if /i "!UPGRADE_TYPE!"=="minor" (
    set "NEW_MAJOR=!VER_MAJOR!"
    set /a "NEW_MINOR=!VER_MINOR!+1"
    set "NEW_PATCH=0"
) else if /i "!UPGRADE_TYPE!"=="major" (
    set /a "NEW_MAJOR=!VER_MAJOR!+1"
    set "NEW_MINOR=0"
    set "NEW_PATCH=0"
) else (
    echo [ERROR] 无效的升级类型: !UPGRADE_TYPE! ^(可选: major, minor, patch^)
    exit /b 1
)
set "TARGET_VERSION=!NEW_MAJOR!.!NEW_MINOR!.!NEW_PATCH!!VER_SUFFIX!"
goto :mode_done

:mode_manual
if not defined MANUAL_VERSION (
    echo.
    set /p "MANUAL_VERSION=请输入目标版本号 ^(格式: X.Y.Z 或 X.Y.Z-SNAPSHOT^): "
)
call :validate_version "!MANUAL_VERSION!"
if errorlevel 1 (
    echo [ERROR] 版本号格式不合法: !MANUAL_VERSION!
    echo [ERROR] 期望格式: X.Y.Z 或 X.Y.Z-SNAPSHOT ^(例如: 1.2.3 或 1.2.3-SNAPSHOT^)
    exit /b 1
)
set "TARGET_VERSION=!MANUAL_VERSION!"
goto :mode_done

:mode_snapshot
REM 根据当前版本是否有 SNAPSHOT 后缀，提供添加/移除选项
if defined VER_SUFFIX (
    REM 当前有 -SNAPSHOT，提供移除选项
    set "SNAP_ACTION=remove"
    set "TARGET_VERSION=!VER_MAJOR!.!VER_MINOR!.!VER_PATCH!"
    if "!VER_PATCH!"=="0" (
        REM X.Y.0 简化为 X.Y
        if "!VER_MINOR!"=="0" (
            set "TARGET_VERSION=!VER_MAJOR!"
        ) else (
            set "TARGET_VERSION=!VER_MAJOR!.!VER_MINOR!"
        )
    )
    echo [INFO]  将移除 SNAPSHOT 标识: !CURRENT_VERSION! -^> !TARGET_VERSION!
) else (
    REM 当前无 -SNAPSHOT，提供添加选项
    set "SNAP_ACTION=add"
    set "TARGET_VERSION=!VER_MAJOR!.!VER_MINOR!.!VER_PATCH!-SNAPSHOT"
    echo [INFO]  将添加 SNAPSHOT 标识: !CURRENT_VERSION! -^> !TARGET_VERSION!
)
goto :mode_done

:mode_done

REM ---- 确认操作 ----
echo [INFO]  版本号升级: !CURRENT_VERSION! -^> !TARGET_VERSION!
echo.
set /p "confirm=确认执行? [Y/n]: "
if /i "!confirm!"=="n" (
    echo [WARN]  已取消操作
    exit /b 0
)

REM ---- 执行版本更新 ----
echo [INFO]  正在更新所有模块版本号...
call mvn versions:set -DnewVersion=!TARGET_VERSION! -DgenerateBackupPoms=true -q
if errorlevel 1 (
    echo [ERROR] 版本号更新失败
    exit /b 1
)

REM 同步子模块的 parent 版本
call mvn versions:update-child-modules -q 2>nul

echo [INFO]  版本号更新成功

REM ---- 清理备份文件 ----
echo [INFO]  正在清理 pom.xml.versionsBackup 备份文件...
for /r %%f in (pom.xml.versionsBackup) do (
    if exist "%%f" del "%%f"
)
echo [INFO]  备份文件清理完成

REM ---- 编译验证 ----
if /i "!SKIP_COMPILE!"=="true" (
    echo [WARN]  已跳过编译验证
) else (
    echo [INFO]  正在执行编译验证 ^(mvn clean compile^)...
    call mvn clean compile -q
    if errorlevel 1 (
        echo [ERROR] 编译验证失败！请检查项目代码
        exit /b 1
    )
    echo [INFO]  编译验证通过
)

echo.
echo [INFO]  =========================================
echo [INFO]    版本升级完成: !CURRENT_VERSION! -^> !TARGET_VERSION!
echo [INFO]  =========================================
exit /b 0

REM ============================================================
REM 函数: 解析版本号
REM 支持格式: X.Y.Z / X.Y.Z-SNAPSHOT / X.Y-SNAPSHOT / X.Y
REM ============================================================
:parse_version
set "ver=%~1"

REM 分离 SNAPSHOT 后缀
set "VER_SUFFIX="
echo !ver!| findstr /i /r "\-SNAPSHOT$" >nul
if not errorlevel 1 (
    set "VER_SUFFIX=-SNAPSHOT"
    set "ver=!ver:~0,-9!"
)

REM 用 . 分割纯数字部分
set "VER_MAJOR="
set "VER_MINOR="
set "VER_PATCH=0"
for /f "tokens=1,2,3 delims=." %%a in ("!ver!") do (
    set "VER_MAJOR=%%a"
    set "VER_MINOR=%%b"
    if not "%%c"=="" set "VER_PATCH=%%c"
)

if not defined VER_MAJOR (
    echo [ERROR] 版本号格式不合法: %~1
    exit /b 1
)
if not defined VER_MINOR set "VER_MINOR=0"

set /a "VER_PATCH_NEXT=!VER_PATCH!+1"
set /a "VER_MINOR_NEXT=!VER_MINOR!+1"
set /a "VER_MAJOR_NEXT=!VER_MAJOR!+1"
exit /b 0

REM ============================================================
REM 函数: 校验版本号格式
REM 支持: X.Y.Z / X.Y.Z-SNAPSHOT / X.Y-SNAPSHOT / X.Y
REM ============================================================
:validate_version
set "vcheck=%~1"
REM 先去掉 -SNAPSHOT 后缀再校验纯数字部分
set "vtmp=!vcheck!"
set "vsnap="
echo !vtmp!| findstr /i /r "\-SNAPSHOT$" >nul
if not errorlevel 1 (
    set "vsnap=1"
    set "vtmp=!vtmp:~0,-9!"
)
REM 校验剩余部分为 X.Y 或 X.Y.Z 纯数字格式
echo !vtmp!| findstr /r "^[0-9][0-9]*\.[0-9][0-9]*$" >nul && goto :validate_ok
echo !vtmp!| findstr /r "^[0-9][0-9]*\.[0-9][0-9]*\.[0-9][0-9]*$" >nul && goto :validate_ok
exit /b 1
:validate_ok
exit /b 0

REM ============================================================
REM 帮助信息
REM ============================================================
:usage
echo 用法: %~nx0 [选项]
echo.
echo 选项:
echo   -m, --mode ^<auto^|manual^|snapshot^>  升级模式
echo   -t, --type ^<major^|minor^|patch^>      自动模式下的升级类型 (默认: patch^)
echo   -v, --version ^<X.Y.Z[-SNAPSHOT]^>     手动模式下的版本号
echo   -s, --skip-compile                  跳过编译验证
echo   -h, --help                        显示帮助信息
echo.
echo 示例:
echo   %~nx0                                交互式选择
echo   %~nx0 -m auto                        自动补丁版本+1
echo   %~nx0 -m auto -t minor               自动次版本号+1
echo   %~nx0 -m manual -v 2.0.0-SNAPSHOT    手动设置版本号
echo   %~nx0 -m snapshot                    切换 SNAPSHOT 标识
exit /b 0
