# Guía para el Agente

## Tareas por cada cambio de código o commit

Cada vez que realices un cambio de código o se te pida crear un commit, debes seguir este checklist:

- [ ] **Compilación parcial**: Realiza una compilación (p. ej., `./gradlew :app:compileDebugKotlin`) para verificar errores de sintaxis y tipos sin realizar un assemble completo.
- [ ] **Ejecutar Lint**: Ejecuta lint (`./gradlew :app:lint`) para asegurarte de que no introduces nuevos warnings o errores de calidad de código.
- [ ] **Crear Commit**: Crea un commit local con los cambios realizados utilizando la **skill de commiter** (siguiendo el estándar de **Conventional Commits** con emojis). **No hagas push** hasta que se te indique explícitamente.
