# PlantillaACD — Ejemplo de Flyway, JPA y MySQL

Un centro de formación necesita mantener su plantilla y conocer salarios por departamento. El proyecto muestra cómo incorporar correo corporativo y relaciones sin perder empleados, y cómo usar después JPA para las altas y consultas.

El [tutorial completo](../Tutorial%20Flyway.md) explica las decisiones, reproduce el código y acompaña la evolución de esta misma aplicación. Las entidades del proyecto representan el estado final, después de V4.

## Requisitos

JDK 17 o 21, Maven 3.9.x y Docker Compose o MySQL 8.4. Comprueba el JDK que utiliza Maven con `mvn -version`.

## Arranque rápido en PowerShell

Desde esta carpeta:

```powershell
$env:DB_URL = 'jdbc:mysql://localhost:13307/acd_flyway?sslMode=DISABLED&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8'
$env:DB_USER = 'acd'
$env:DB_PASSWORD = [Net.NetworkCredential]::new('', (Read-Host 'Contraseña para el usuario de prácticas' -AsSecureString)).Password
$env:MYSQL_ROOT_PASSWORD = [Net.NetworkCredential]::new('', (Read-Host 'Contraseña para root de este MySQL local' -AsSecureString)).Password
docker compose up -d --wait
mvn test
mvn compile exec:java "-Dexec.args=listar"
mvn exec:java "-Dexec.args=informe"
mvn exec:java "-Dexec.args=alta Marta marta@plantilla.example.test 31000.00 2"
mvn exec:java "-Dexec.args=informe"
```

En Bash consulta el bloque de configuración del tutorial: utiliza `read -s` y exporta las mismas variables. Los comandos Maven son iguales. Elige contraseñas propias y no las guardes en el repositorio. Compose utiliza esas variables al inicializar el volumen; para acceder en otra sesión debes introducir los mismos valores. Las opciones de conexión son para el laboratorio local.

El primer informe muestra Docencia con dos empleados y 60000.00 EUR/año, y Administración vacía. Tras el alta, Administración muestra una empleada y 31000.00 EUR/año. El mismo correo no puede registrarse dos veces.

## Migraciones y comprobaciones

```text
mvn flyway:info
mvn flyway:validate
mvn flyway:migrate
```

`listar`, `alta` e `informe` ejecutan Flyway antes de abrir JPA. Hibernate utiliza `validate`; no crea ni actualiza tablas. `mvn test` utiliza bases H2 en memoria, sin Docker ni credenciales externas. Consulta en el tutorial la práctica de actualización desde V2 y sus límites de compatibilidad.

Para inspeccionar MySQL:

```text
docker compose exec mysql mysql -uacd -p acd_flyway
```

Introduce la contraseña del laboratorio y consulta `flyway_schema_history`, `empleados` o `resumen_costes_departamento`.

Detén el servicio con `docker compose stop`. El volumen conserva la plantilla. `docker compose down -v` elimina todos los datos de este laboratorio: úsalo solo si deseas reiniciarlo desde cero.
