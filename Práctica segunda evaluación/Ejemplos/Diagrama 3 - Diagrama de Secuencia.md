Diagrama de Secuencia para la acción de "Crear Tarea" en la aplicación "TaskMaster". Este diagrama ilustra la interacción entre el usuario, la interfaz de usuario y el sistema durante el proceso de creación de una tarea:

1. **Usuario** selecciona la opción 'Crear Tarea' en la **Interfaz de Usuario (UI)**.
2. La **UI** solicita al **Sistema** el formulario para una nueva tarea.
3. El **Sistema** envía el formulario a la **UI**.
4. El **Usuario** completa y envía los detalles de la tarea a través de la **UI**.
5. La **UI** envía los datos de la tarea al **Sistema**.
6. El **Sistema** procesa y almacena la tarea.
7. El **Sistema** envía una confirmación de creación de la tarea a la **UI**.
8. La **UI** muestra la confirmación al **Usuario**.

Este diagrama muestra la secuencia de mensajes y acciones que ocurren desde que el usuario decide crear una tarea hasta que recibe una confirmación de que la tarea ha sido creada.

```mermaid
%%{init: {'theme': 'base', 'themeVariables': { 'primaryColor': '#4a90e2', 'secondaryColor': '#e2e2e2', 'tertiaryColor': '#f0f0f0'}}}%%
sequenceDiagram
    actor Usuario
    participant UI as Interfaz de Usuario
    participant Sistema

    Usuario->>UI: Selecciona 'Crear Tarea'
    UI->>Sistema: Solicita formulario de nueva tarea
    Sistema->>UI: Envía formulario
    Usuario->>UI: Completa y envía detalles de la tarea
    UI->>Sistema: Envía datos de la tarea
    Sistema->>Sistema: Procesa y almacena la tarea
    Sistema->>UI: Confirma la creación de la tarea
    UI->>Usuario: Muestra confirmación

```


