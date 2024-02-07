Documento de apoyo al desarrollo rápido en iteración con la IA. 

La idea es ir apuntando aquí todo lo que te sugiere la IA en el proceso de diseño que consideres que son buenas ideas o aportaciones de valor de manera que no se nos pierda entre toda la conversación.

TODO
- clases DTO
- **Gestión Administrativa:** Los campos `fechaCreacion`, `fechaModificacion`, y `fechaCompletada` se pueden manejar automáticamente con JPA a través de anotaciones como `@PrePersist` y `@PreUpdate` para los métodos de callback que actualizan estos campos.
- **Seguridad de la Contraseña:** Aunque la clase `Usuario` tiene un campo `contraseña`, es crucial implementar un mecanismo de cifrado adecuado antes de almacenar cualquier contraseña en la base de datos, utilizando, por ejemplo, BCryptPasswordEncoder de Spring Security.
- Autenticación / autorización con Spring Security
- **Metadatos** para tareas administrativas, auditoría, estadísticas.



DONE
- clases de dominio
- 