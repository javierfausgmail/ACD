# Etapa 1: catalogo

Abre primero [el tutorial](../../Tutorial%20Flyway.md) y llega hasta esta etapa.
Esta carpeta contiene un punto de comprobación completo, no cambios que debas mezclar a ciegas con otra etapa.

Ejecuta **desde esta carpeta**:

```text
mvn compile exec:java "-Dexec.args=listar"
mvn exec:java "-Dexec.args=alta"
mvn exec:java "-Dexec.args=historial"
```

El alta pregunta los datos que conoce esta versión.

Todas las etapas usan `../datos/biblioteca.mv.db`. Pasa a la siguiente sin borrar ese archivo.
Tras actualizar, continúa hacia delante: la etapa anterior representa un programa antiguo.
`mvn test` comprueba esta etapa con bases temporales en memoria y conserva tus libros.
