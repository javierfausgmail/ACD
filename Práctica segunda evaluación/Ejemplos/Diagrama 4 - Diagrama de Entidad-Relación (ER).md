Diagrama de Entidad-Relación (ER) para la aplicación "TaskMaster". Este diagrama muestra las entidades principales y sus relaciones:

- **Entidad "USUARIO"**: Representa a los usuarios de la aplicación.    
    - **Atributos**:
        - `id`: Identificador único del usuario.
        - `nombre`: Nombre del usuario.
        - `email`: Correo electrónico del usuario.
        
- **Entidad "TAREA"**: Representa las tareas creadas por los usuarios.    
    - **Atributos**:
        - `id`: Identificador único de la tarea.
        - `titulo`: Título de la tarea.
        - `descripcion`: Descripción de la tarea.
        - `fecha`: Fecha programada para la tarea.
        - `categoria`: Categoría de la tarea (por ejemplo, trabajo, personal).

- **Relación "crea"**:
    - Un **USUARIO** puede crear varias **TAREAS** (relación uno a muchos).


```mermaid
%%{init: {'theme': 'base', 'themeVariables': { 'primaryColor': '#ffcc00', 'edgeLabelBackground':'#ffffff', 'tertiaryColor': '#f0f0f0'}}}%%
erDiagram
    USUARIO ||--o{ TAREA : "crea"
    USUARIO {
        int id PK "Identificador único"
        string nombre "Nombre del Usuario"
        string email "Correo Electrónico"
    }

    TAREA {
        int id PK "Identificador único"
        string titulo "Título de la Tarea"
        string descripcion "Descripción"
        date fecha "Fecha de la Tarea"
        string categoria "Categoría"
    }

```

