Es una aplicación web para la gestión de tareas personales.

**1. Identificación del Proyecto**
   - **Nombre del Proyecto:** TaskManager2.
   - **Descripción:** Aplicación web para la gestión de tareas y recordatorios personales.
   - **Fecha:** 28 de Febrero de 2024.
   - **Responsable:** Javier Faus.

**2. Objetivos del Proyecto**
   - **Objetivo Principal:** Desarrollar una aplicación web intuitiva para la gestión eficiente de tareas personales. Con roles de usuarios finales y un rol de usuario administrador sobre la aplicación.
   - **Objetivos Secundarios:**
     - Ofrecer recordatorios y notificaciones personalizables.
     - Permitir la categorización de tareas.

**3. Requisitos Funcionales**
   - **RF1:** Cada diferente usuario final debe de poder ejecutar Creación, edición y eliminación de sus tareas.
   - **RF2**: Usuario administrador de la aplicación que puede gestionar el CRUD de los usuarios finales (clientes) así como de las tareas de dichos usuarios. Es un role diferente y se debe de diferenciar su UI con formularios y casos de uso diferentes a los de los usuarios finales de la aplicación.
   - **RF3:** Categorización de tareas (trabajo, personal, estudio).
   - **RF4:** Configuración de recordatorios y notificaciones.
   - **RF5:** La aplicación debe de exponer una API Rest con acceso autenticado (JWT) con funcionalidades completas de gestión CRUD + listados, búsqueda, ordenación y paginación de todas las entidades del proyecto.
   - **Prioridad:** RF1 , RF2 y RF5 es esencial, RF3 y RF4 son importantes.

**4. Requisitos No Funcionales**
   - **Framework de desarrollo:** La aplicación debe de realizarse utilizando SpringBoot con Java en la versión 17 o superior de este lenguaje con Maven como gestor de dependencias. Se utilizará Spring MVC y Thymeleaf para la aplicación web. Utilizaremos la librería Lombok en el proyecto para generar nuestras clases de manera más rápida. Para la persistencia de datos utilizaremos Spring Data JPA y una base de datos MySQL.
   - **Arquitectura**: Aplicación monolítica implementada por capas siguiendo los principios del Domain Driven Design.
   - **Rendimiento:** La app debe responder en menos de 2 segundos.
   - **Seguridad:** Cifrado de contraseñas almacenadas, la aplicación debe de gestionar tanto la autenticación como la autorización utilizando las mejores prácticas con Spring Security utilizando JWT para la autenticación. 
   - **Usabilidad:** Interfaz sencilla y amigable utilizando BootStrap para el CSS y HTMX para llamadas AJAX al servidor utilizando atributos HTML.
   - **Testing:** Implementar pruebas unitarias y de integración para asegurar la fiabilidad y el correcto funcionamiento de la aplicación en todos sus componentes.

