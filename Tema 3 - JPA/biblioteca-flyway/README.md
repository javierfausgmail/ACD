# Biblioteca de aula — Flyway paso a paso con PRIMM

El catálogo crece con las necesidades de la biblioteca: títulos, autores y préstamos. Aprenderemos a cambiar las tablas conservando los libros que ya hay guardados.

**Empieza por [el tutorial](../Tutorial%20Flyway.md)**. Está organizado en tres sesiones de 55 minutos y acompaña cada cambio con predicción, ejecución, investigación, modificación y creación propia.

## Preparar el trabajo

Necesitas JDK 17 o 21 y Maven. La base H2 se guarda en un archivo local; no hay servidor que instalar ni variables que configurar.

Duplica `01-catalogo` como `trabajo`. En PowerShell:

```powershell
Copy-Item -Recurse 01-catalogo trabajo
cd trabajo
mvn compile exec:java "-Dexec.args=listar"
```

En Bash usa `cp -r 01-catalogo trabajo`. Continúa desde la sesión 1 del tutorial: todavía no necesitas abrir las versiones siguientes.

## Puntos de comprobación

| Carpeta | Función de referencia | Migraciones incluidas |
|---|---|---|
| `01-catalogo` | Registrar y listar títulos | V1 |
| `02-autores` | Añadir autor conservando títulos | V1 y V2 |
| `03-prestamos` | Prestar y devolver ejemplares | V1, V2 y V3 |
| `04-reto-resuelto` | Referencia parcial del reto: estantería en alta y listado | V1 a V4 |

Cada etapa es ejecutable por separado desde su propia carpeta. Todas utilizan `../datos/biblioteca.mv.db`. Al avanzar se actualiza ese mismo archivo; no se borra ni se siembra otro catálogo. Utiliza una etapa cada vez y continúa hacia delante.

La carpeta `trabajo` es tuya: conserva en ella las funciones que desarrolles. Los puntos de comprobación no incluyen las soluciones de `contar`, `completarAutor` ni `buscarEstanteria`.

## Acciones de la aplicación

Desde la carpeta que estés ejecutando:

```text
mvn compile exec:java "-Dexec.args=listar"
mvn exec:java "-Dexec.args=alta"
mvn exec:java "-Dexec.args=historial"
```

El alta pregunta los datos de esa etapa. A partir de la tercera también están disponibles `prestar` y `devolver`; ambas preguntan el identificador del ejemplar.

## Comprobación del material

Desde esta carpeta, `mvn test` verifica todas las referencias con bases temporales, sin alterar tu catálogo. Consulta las indicaciones del tutorial para adaptar los tests de `trabajo` cuando cambies constructores o añadas migraciones.
