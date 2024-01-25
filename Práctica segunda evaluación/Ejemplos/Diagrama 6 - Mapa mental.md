Este mapa mental se centra en distintas áreas clave del proyecto:

- **Núcleo: "TaskMaster"**
    
    - Ramificaciones principales: Funcionalidades, Diseño, Mercado Objetivo.
- **Funcionalidades:**
    
    - Crear Tareas: Función para añadir nuevas tareas.
    - Recordatorios: Configuración de alertas para las tareas.
    - Categorización de Tareas: Organizar tareas por categorías como trabajo, personal, estudio.
- **Diseño:**
    
    - Interfaz de Usuario (UI): Aspecto visual de la aplicación.
    - Experiencia de Usuario (UX): Facilidad y eficiencia de uso.
    - Diseño Responsivo: Adecuación a diferentes dispositivos y tamaños de pantalla.
- **Mercado Objetivo:**
    
    - Adultos Jóvenes: Enfocado a jóvenes profesionales.
    - Profesionales: Diseñado para quienes necesitan organización en su trabajo.
    - Estudiantes: Funciones útiles para la gestión de tareas académicas.

Este mapa mental es una representación visual y simplificada de los aspectos principales del proyecto.

Para visualizar este diagrama puedes usar un editor de Mermaid:


```mermaid

%%{init: {'theme': 'base', 'themeVariables': { 'primaryColor': '#4a90e2', 'secondaryColor': '#e2e2e2', 'tertiaryColor': '#f0f0f0'}}}%%
graph TD
    TaskMaster[TaskMaster]
    TaskMaster --> Funcionalidades[Funcionalidades]
    TaskMaster --> Diseño[Diseño]
    TaskMaster --> MercadoObjetivo[Mercado Objetivo]

    Funcionalidades --> CrearTareas[Crear Tareas]
    Funcionalidades --> Recordatorios[Recordatorios]
    Funcionalidades --> Categorización[Categorización de Tareas]

    Diseño --> UI[Interfaz de Usuario]
    Diseño --> UX[Experiencia de Usuario]
    Diseño --> Responsivo[Diseño Responsivo]

    MercadoObjetivo --> AdultosJovenes[Adultos Jóvenes]
    MercadoObjetivo --> Profesionales[Profesionales]
    MercadoObjetivo --> Estudiantes[Estudiantes]

```