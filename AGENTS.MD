# Agents Documentation

This document explains the various agents available in the Qoder system and how to use them effectively.

## Available Agent Types

### Explore
- **Purpose**: Fast agent specialized for exploring codebases
- **Best for**: Quickly finding files by patterns (e.g., "src/components/**/*.tsx"), searching code for keywords (e.g., "API endpoints"), or answering questions about the codebase
- **Usage**: Specify thoroughness level - "quick" for basic searches, "medium" for moderate exploration, or "very thorough" for comprehensive analysis

### Plan
- **Purpose**: Software architect agent for designing implementation plans
- **Best for**: Planning implementation strategy for tasks, returning step-by-step plans, identifying critical files, and considering architectural trade-offs

### General-Purpose
- **Purpose**: General-purpose agent for researching complex questions, searching for code, and executing multi-step tasks
- **Best for**: When searching for a keyword or file and not confident about finding the right match in the first few tries

### Qoder-Guide
- **Purpose**: Expert assistant for Qoder CLI CN questions
- **Best for**: Questions about features, skills, agents, MCP servers, settings, hooks, and IDE integrations
- **Usage**: When users ask "Can Qoder...", "How do I...", or need help with configuration

### Statusline-Setup
- **Purpose**: Configures the user's custom status line setting
- **Best for**: Reading shell configs, creating scripts, and updating settings.json

## How to Use Agents

Agents can be invoked using the `Agent` tool with the appropriate `subagent_type` parameter. Each agent type has specific capabilities and tools available to it.

Example usage:
```
Agent({
  subagent_type: "Explore",
  description: "Quick search for API endpoints",
  prompt: "Find all API endpoint definitions in the codebase"
})
```

## Best Practices

- Use Explore agent for quick codebase navigation and searches
- Use Plan agent when you need to design implementation strategies
- Use General-Purpose for complex multi-step tasks that don't fit other categories
- Use Qoder-Guide for Qoder-specific questions and configuration help
- Use Statusline-Setup for custom status line configurations

## When NOT to Use Agents

- For simple tasks that can be accomplished with direct tool usage (Read, Edit, etc.)
- When you already know the exact location and nature of the change needed
- For basic file operations that don't require intelligent processing