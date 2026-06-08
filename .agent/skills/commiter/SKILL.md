---
name: commiter
description: Use it when you're asked to create a commit
---
Use **Conventional Commits** for commit messages. Each commit must follow this structure:

`<type>[optional scope]: <description>`

Additionally, associate an emoji with each commit type according to the following list:

- ✨ `feat`: A new feature.
- 🐛 `fix`: A bug fix.
- 📚 `docs`: Documentation only changes.
- 🎨 `style`: Changes that do not affect the meaning of the code (white-space, formatting, missing semi-colons, etc.).
- ♻️ `refactor`: A code change that neither fixes a bug nor adds a feature.
- ⚡ `perf`: A code change that improves performance.
- ✅ `test`: Adding missing tests or correcting existing tests.
- 🏗️ `build`: Changes that affect the build system or external dependencies (example scopes: Gradle, Maven).
- 👷 `ci`: Changes to our CI configuration files and scripts (example scopes: GitHub Actions).
- 🔧 `chore`: Other changes that don't modify `src` or `test` files.
- ⏪ `revert`: Reverts a previous commit.

Example: `feat(ui): ✨ add save button`
