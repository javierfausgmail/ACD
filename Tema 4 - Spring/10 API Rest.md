# 1. Introducción y Diagramas 

Diagrama del flujo de la petición a una API Rest:

![[Diagrama API Rest y DTO.excalidraw]]



Diagrama con anotaciones principales para uso en Spring Boot:

![](DIagrama%20API%20Rest%20Anotaciones%20y%20Capas.png)
Explicación de este diagrama en castellano: https://www.youtube.com/watch?v=YoBFTSKNrD0


# **APIs REST**

## 1. Introducción a las APIs y su propósito

Una **API** (Application Programming Interface) es un conjunto de reglas y contratos que permiten que dos sistemas de software se comuniquen entre sí. Las APIs facilitan que un cliente pueda **solicitar datos o ejecutar acciones** sobre un servidor de manera estructurada y segura, sin necesidad de conocer los detalles internos de implementación.

El diseño de una API actúa como un contrato entre:

- **El consumidor**, que envía solicitudes.
    
- **El proveedor**, que expone recursos y devuelve respuestas estructuradas.
    

Este contrato define:

- Qué datos puede solicitar el cliente.
    
- Cómo deben enviarse esas solicitudes.
    
- Cómo se representan las respuestas.
    
- Qué reglas de autenticación y control se aplican.
    
- Qué errores pueden producirse y cómo se comunican.
    

Las APIs permiten integrar aplicaciones, automatizar procesos y exponer servicios de forma controlada, eficiente y escalable.

---

# 2. Fundamentos de REST

**REST** (Representational State Transfer) es un estilo arquitectónico para construir APIs utilizando las capacidades fundamentales del protocolo HTTP.

No se trata de un estándar cerrado, sino de un conjunto de principios que guían el diseño de interfaces web claras, coherentes y orientadas a recursos.

Una API REST opera sobre cuatro pilares:

### ✔ Arquitectura Cliente–Servidor

El cliente envía solicitudes; el servidor procesa y devuelve representaciones de recursos.

### ✔ Comunicación sin estado (stateless)

Cada petición debe contener toda la información necesaria para procesarla.  
El servidor no almacena información de sesión entre solicitudes.

### ✔ Recursos identificados por URIs

Los recursos —entidades conceptuales como usuarios, facturas o pedidos— se identifican mediante URIs únicas.

### ✔ Representaciones transferidas

La respuesta contiene una representación del recurso, habitualmente en **JSON**, aunque también pueden usarse XML, HTML u otros formatos aceptados mediante cabeceras HTTP.

---

# 3. Diseño de Recursos y URIs

El concepto central en REST es el **recurso**.  
Un recurso representa una entidad o conjunto de entidades accesibles a través de una URI.

### Reglas generales para definir URIs

- **Usar sustantivos**, nunca verbos.  
    Incorrecto: `/crearFactura`  
    Correcto: `/facturas`
    
- **URIs únicas y estables** para cada recurso.  
    Ejemplo: `/clientes/34/facturas/210`
    
- **Jerarquía lógica**:
    
    - Recurso padre → `/clientes/34`
        
    - Subrecurso → `/clientes/34/facturas`
        
- **Sin extensiones de formato**:
    
    - Incorrecto: `/facturas/210.pdf`
        
    - Correcto: `/facturas/210`  
        El formato se negocia con `Accept`.
        
- **Los filtros no forman parte de la ruta**  
    Ejemplo:
    
    ```
    GET /facturas?desde=2020&orden=desc&pagina=3
    ```
    

### Ejemplos correctos de URIs

- Colección: `/productos`
    
- Elemento: `/productos/120`
    
- Subrecursos: `/productos/120/opiniones`
    
- Filtrado: `/productos?categoria=audio&stock=true`
    

---

# 4. Métodos HTTP y sus propósitos

REST utiliza los métodos HTTP para expresar operaciones:

|Método|Propósito|Idempotencia|
|---|---|---|
|**GET**|Obtener recursos|✔ Idempotente|
|**POST**|Crear un recurso nuevo|✘ No idempotente|
|**PUT**|Reemplazar un recurso completo|✔ Idempotente|
|**PATCH**|Modificar parcialmente un recurso|✘ No idempotente|
|**DELETE**|Eliminar un recurso|✔ Idempotente|

### Ejemplos típicos

```
GET     /clientes           → lista de clientes
POST    /clientes           → crear cliente
GET     /clientes/7         → obtener cliente 7
PUT     /clientes/7         → sustituir datos del cliente 7
PATCH   /clientes/7         → modificar un campo puntual
DELETE  /clientes/7         → borrar cliente 7
```

---

# 5. Códigos de Estado HTTP

La respuesta HTTP debe comunicar siempre el resultado de la operación mediante **códigos estándar**.

### Categorías principales:

- **2xx – Éxito**
    
    - 200 OK
        
    - 201 Created
        
    - 204 No Content
        
- **4xx – Error del cliente**
    
    - 400 Bad Request
        
    - 401 Unauthorized
        
    - 403 Forbidden
        
    - 404 Not Found
        
    - 409 Conflict
        
    - 422 Unprocessable Entity
        
- **5xx – Error del servidor**
    
    - 500 Internal Server Error
        
    - 503 Service Unavailable
        

Ejemplo correcto:

```
Status: 400 Bad Request
{
  "message": "El campo 'email' es obligatorio"
}
```

---

# 6. Representación y Negociación de Contenidos

REST utiliza las cabeceras HTTP para acordar formatos entre cliente y servidor:

### Solicitar formato:

```
Accept: application/json, application/xml
```

### Respuesta:

```
Content-Type: application/json
```

Si el servidor no puede producir ninguno de los formatos solicitados:

```
406 Not Acceptable
```

Los formatos más utilizados:

- **JSON** (estándar de facto)
    
- XML (en sistemas legacy)
    
- JSON-LD (datos semánticos)
    
- HAL / JSON:API (formatos hipermedia estructurados)
    

---

# 7. Prácticas modernas en el diseño de APIs REST

## 7.1 Versionado de APIs

Una API debe permitir evolución manteniendo compatibilidad.

Modalidades comunes:

- En la ruta:
    
    ```
    /v1/clientes
    /v2/clientes
    ```
    
- En cabecera custom:
    
    ```
    Accept: application/vnd.miapi.v2+json
    ```
    

## 7.2 Validación de entrada y estructura de errores

Los errores deben seguir un formato consistente:

```
{
  "error": "validation_error",
  "details": [
    { "field": "email", "message": "Formato inválido" }
  ]
}
```

## 7.3 Idempotencia en operaciones críticas

Permite reintentos seguros ante fallos de red.  
Especialmente en POST sensibles mediante claves idempotentes.

## 7.4 Documentación obligatoria

El estándar actual es:

- **OpenAPI 3.0/3.1**
    
- Compatible con herramientas:
    
    - Swagger UI
        
    - ReDoc
        
    - Codegen automático de clientes y servidores
        

---

# 8. Seguridad aplicable a APIs REST

Cualquier API debe implementar autenticación y control de acceso.

Los esquemas modernos incluyen:

- **OAuth 2.1**
    
- **OpenID Connect**
    
- **JWT (JSON Web Tokens)**
    
- API Keys controladas
    
- mTLS en entornos corporativos
    
- Rate limiting y cuotas
    

El objetivo es:

- Proteger endpoints
    
- Gestionar identidades
    
- Controlar permisos
    
- Limitar abusos
    

Cabeceras típicas:

```
Authorization: Bearer <token>
```

---

# 9. REST en el ecosistema actual

Las APIs REST se utilizan para:

- Exposición pública de servicios web
    
- Integraciones entre sistemas
    
- Mobile y frontend web
    
- Microservicios de baja latencia
    

REST convive y se complementa con:

- **GraphQL** → consultas flexibles orientadas a clientes
    
- **gRPC** → servicios de alto rendimiento
    
- **WebSockets** / Event-Driven → comunicación en tiempo real
    

REST sigue siendo el estilo predominante para APIs accesibles vía HTTP por su simplicidad, estandarización y amplia compatibilidad.

---

# 10. Conclusión

Una API REST moderna debe:

- Exponer recursos bien definidos mediante URIs claras.
    
- Utilizar métodos HTTP de forma semántica.
    
- Producir respuestas coherentes y estandarizadas.
    
- Comunicar el resultado mediante códigos de estado correctos.
    
- Negociar formatos mediante cabeceras.
    
- Integrar versionado, validación y consistencia estructural.
    
- Ofrecer documentación formal mediante OpenAPI.
    
- Implementar mecanismos robustos de autenticación y seguridad.
    

REST sigue siendo una de las formas más eficientes, interoperables y escalables de diseñar interfaces web, y constituye la base del intercambio de datos en la mayoría de plataformas digitales actuales.

---

Tutorial con código para crear una API Rest: https://spring.io/guides/gs/accessing-data-rest


# Parte 2. Optimización de una API Rest

Seis acciones prácticas que puedes hacer para optimizar tus APIs REST y que tengan un mejor rendimiento o un mejor tiempo de respuesta. 

## 1. Enviar y recibir JSON optimizados o DTOs (Data Transfer Objects) optimizados a través de la red. 

En la respuesta de la API, siempre usa la menor cantidad de datos necesaria. Emplea DTOs a la medida. Si un endpoint tiene que devolver tres atributos de la clase persona, muchas veces no es necesario que devuelvas toda la clase persona leída desde la base de datos tal cual. Solo devuelve los campos necesarios que necesita el cliente o el que está llamando a nuestra API. 
Puedes usar para esto, además, proyecciones de bases de datos con Spring Data projections. Pero básicamente, se pueden hacer en cualquier lenguaje y no depende del framework en sí. Las predicciones de base de datos te ayudan a optimizar el overhead. La idea de overhead se resume básicamente en lo siguiente: no es lo mismo hacer un "SELECT * FROM tabla", por ejemplo personas, y obtener toda la tabla con *todas las columnas de la tabla personas*, que obtener dos o tres campos que son lo que necesitamos. Esto es un elemento que se conoce como proyección a la hora de hacer query sobre la base de datos. Básicamente, las proyecciones de base de datos te ayudan a disminuir este overhead que se causa al obtener demasiados datos que muchas veces son innecesarios.

**Implementación práctica:**
Para implementar el consejo de enviar y recibir JSON optimizados o DTOs (Data Transfer Objects) optimizados a través de la red en un proyecto Spring Boot, seguirá los siguientes pasos:

### Paso 1: Crear el Proyecto Spring Boot

Primero, asegúrese de tener Spring Boot 3.3.2 y Maven configurados en su entorno. Puede utilizar Spring Initializr (https://start.spring.io/) para generar un proyecto base seleccionando las dependencias necesarias, como `Spring Web` y `Spring Data JPA`.

### Paso 2: Configurar el Proyecto en su IDE

Importe el proyecto generado en su IDE de preferencia (como IntelliJ IDEA, Eclipse, etc.) como un proyecto Maven.

### Paso 3: Crear el Modelo y el Repositorio

Defina una entidad `Persona` y un repositorio `PersonaRepository` para interactuar con la base de datos.

**Modelo Persona**

```java
@Entity
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    // Otros campos...

    // Getters y setters...
}
```

**Repositorio PersonaRepository**

```java
public interface PersonaRepository extends JpaRepository<Persona, Long> {
}
```

### Paso 4: Crear un DTO

Defina un `PersonaDTO` que contenga solo los campos que desea exponer en su API.

**PersonaDTO**

```java
public class PersonaDTO {
    private String nombre;
    private String apellido;
    // No incluir el email ni otros campos innecesarios para la operación específica

    // Constructor, Getters y Setters...
}
```

### Paso 5: Implementar una Proyección

Utilice proyecciones de Spring Data para definir una interfaz que declare los métodos para obtener solo los datos necesarios.

**Proyección PersonaResumen**

```java
public interface PersonaResumen {
    String getNombre();
    String getApellido();
}
```

Modifique `PersonaRepository` para incluir un método que utilice la proyección:

```java
public interface PersonaRepository extends JpaRepository<Persona, Long> {
    List<PersonaResumen> findAllProjectedBy();
}
```

La implementación del método `List<PersonaResumen> findAllProjectedBy();` en el repositorio `PersonaRepository` es manejada por Spring Data JPA internamente, gracias al soporte de proyecciones. Cuando se define una proyección y se utiliza en un método de repositorio de Spring Data, Spring Data JPA genera la consulta necesaria para obtener solo los campos especificados por la interfaz de proyección.

Cuando se llama al método `findAllProjectedBy()`, Spring Data JPA genera una consulta SQL que selecciona solo las columnas `nombre` y `apellido` de la tabla asociada a la entidad `Persona`. Esta consulta es el resultado de la interpretación de Spring Data JPA de la interfaz de proyección `PersonaResumen`.

En Spring Data JPA, cuando se utiliza una interfaz como proyección para definir un subconjunto de los datos que se deben recuperar de una entidad, generalmente no es necesario anotar la interfaz de proyección con una anotación específica. Spring Data JPA es capaz de inferir la proyección basándose en los métodos definidos en la interfaz y su uso dentro de los métodos del repositorio.

Internamente, lo que Spring Data JPA hace es lo siguiente:

1. Analiza la interfaz de proyección `PersonaResumen` para determinar qué campos (en este caso, `nombre` y `apellido`) deben ser recuperados de la base de datos.
2. Genera una consulta SQL (o JPQL) basada en la entidad `Persona` que selecciona solo esos campos. Por ejemplo, podría ser algo similar a `SELECT p.nombre, p.apellido FROM Persona p`.
3. Ejecuta la consulta en la base de datos.
4. Mapea los resultados de la consulta a instancias de la interfaz de proyección `PersonaResumen`. Cada instancia de `PersonaResumen` representará una fila del resultado, con `getNombre()` y `getApellido()` retornando los valores de las columnas `nombre` y `apellido`, respectivamente.

**Ventajas**

- **Eficiencia**: Al seleccionar solo los campos necesarios, se reduce el uso de recursos tanto en la base de datos como en la aplicación.
- **Simplicidad**: El código del cliente que usa el repositorio puede trabajar con objetos simples y directos, enfocándose solo en los datos que necesita.

**Consideraciones**

- **Personalización**: Si necesita una lógica más compleja en sus consultas (como condiciones WHERE personalizadas), podría necesitar métodos de repositorio adicionales o utilizar `@Query` para especificar la consulta JPQL o SQL directamente.
- **Rendimiento**: Aunque las proyecciones pueden mejorar el rendimiento al reducir la cantidad de datos transferidos, siempre es una buena práctica medir y optimizar basándose en el uso real de su aplicación.

En resumen, `findAllProjectedBy()` es un ejemplo de cómo Spring Data JPA permite abstraer detalles de implementación de consultas complejas, proporcionando una forma declarativa y eficiente de acceder a datos específicos de una entidad.

### Paso 6: Crear el Controlador

Implemente un controlador `PersonaController` para exponer los datos de `Persona` a través de una API REST, utilizando el DTO y la proyección.

#### PersonaController

```java
@RestController
@RequestMapping("/personas")
public class PersonaController {

    @Autowired
    private PersonaRepository personaRepository;

    @GetMapping
    public ResponseEntity<List<PersonaDTO>> obtenerPersonas() {
        List<PersonaResumen> personas = personaRepository.findAllProjectedBy();
        List<PersonaDTO> resultado = personas.stream()
                .map(p -> new PersonaDTO(p.getNombre(), p.getApellido()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultado);
    }
}
```

### Paso 7: Ejecutar y Probar

Ejecute la aplicación Spring Boot y pruebe el endpoint `/personas` utilizando una herramienta como Postman o cURL. Debería recibir una respuesta con los datos optimizados según su DTO y proyección.

Este ejemplo demuestra cómo optimizar las respuestas JSON de su API utilizando DTOs y proyecciones de Spring Data para reducir el overhead de la transferencia de datos innecesarios, siguiendo la recomendación de enviar y recibir datos optimizados a través de la red.

### Paso 8: Configuración de la Base de Datos

Para completar el ejemplo, es necesario configurar la conexión con la base de datos. Edite el archivo `application.properties` o `application.yml` de su proyecto Spring Boot para incluir las propiedades de conexión a la base de datos.

#### application.properties

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/nombre_base_datos
spring.datasource.username=usuario_base_datos
spring.datasource.password=contraseña_base_datos
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Asegúrese de cambiar `nombre_base_datos`, `usuario_base_datos` y `contraseña_base_datos` por los valores correspondientes a su entorno. Este ejemplo utiliza MySQL; si utiliza otra base de datos, ajuste las propiedades de conexión y el driver de acuerdo a su base de datos.

### Paso 9: Poblar la Base de Datos

Para probar el funcionamiento de la API, puede ser necesario insertar datos en la tabla `persona` de su base de datos. Puede hacerlo manualmente utilizando una herramienta de gestión de bases de datos o mediante un script SQL.

#### Script SQL de Ejemplo

```sql
INSERT INTO persona (nombre, apellido, email) VALUES ('Juan', 'Pérez', 'juan.perez@example.com');
INSERT INTO persona (nombre, apellido, email) VALUES ('Ana', 'García', 'ana.garcia@example.com');
```

### Paso 10: Probar la API

Una vez que la aplicación esté ejecutándose y la base de datos esté poblada con algunos datos de prueba, puede probar el endpoint `/personas` para verificar que la API devuelve los datos en el formato esperado.

Utilice una herramienta como Postman o el siguiente comando cURL para hacer una solicitud GET al endpoint:

```bash
curl http://localhost:8080/personas
```

La respuesta debería ser un JSON con la lista de personas, incluyendo solo los nombres y apellidos, sin los emails ni otros datos adicionales, demostrando así que la API está optimizando los datos enviados.




### Extra. Optimización de Proyecciones con Consultas JPQL y @Query

Para casos donde las consultas derivadas y las especificaciones no ofrecen la flexibilidad necesaria, se puede recurrir al uso de la anotación `@Query` de Spring Data JPA. Esto permite definir explícitamente la consulta JPQL (o SQL nativa) y aprovechar las proyecciones en la misma.

#### Ejemplo con JPQL y Proyección

Supongamos que desea obtener una lista de personas pero solo con información específica y bajo ciertas condiciones complejas. Puede definir una consulta JPQL personalizada en su repositorio:

```java
public interface PersonaRepository extends JpaRepository<Persona, Long> {

    @Query("SELECT p.nombre as nombre, p.apellido as apellido FROM Persona p WHERE p.edad > :edadMinima")
    List<PersonaResumen> findPersonasMayoresDe(@Param("edadMinima") int edadMinima);
}
```

Esta consulta selecciona solo los nombres y apellidos de las personas mayores de una cierta edad, utilizando la proyección `PersonaResumen`.

### Proyecciones Dinámicas

Spring Data JPA también soporta proyecciones dinámicas, lo que permite decidir en tiempo de ejecución qué proyección utilizar. Esto es particularmente útil en APIs donde el consumidor puede especificar los campos que desea recibir.

#### Ejemplo de Proyección Dinámica

```java
public interface PersonaRepository extends JpaRepository<Persona, Long> {

    <T> List<T> findByApellido(String apellido, Class<T> type);
}
```

Al llamar a este método y pasarle una clase de proyección, Spring Data JPA generará una consulta que solo incluye los campos definidos en la proyección indicada.

### Proyecciones y Rendimiento

Mientras las proyecciones son una herramienta poderosa para optimizar el rendimiento de la base de datos, es crucial utilizarlas sabiamente:

- **Evitar el Sobreuso**: No todas las consultas requieren proyecciones. Utilícelas cuando realmente necesite reducir la carga de datos transferidos.
- **Entender el Impacto**: En algunos casos, el uso de proyecciones puede aumentar la complejidad de las consultas generadas, lo que podría tener un impacto negativo en el rendimiento. Es esencial realizar pruebas de carga para entender el impacto real en su aplicación.
- **Caching de Consultas**: Para consultas con proyecciones que se ejecutan frecuentemente con los mismos parámetros, considere utilizar caché para mejorar aún más el rendimiento.

### Integración con la Capa de Servicio

Las proyecciones no se limitan a ser utilizadas directamente en los controladores. Es una buena práctica encapsular la lógica de negocio y las operaciones de base de datos dentro de servicios. Esto facilita la reutilización de código y mantiene su aplicación bien organizada.

#### Ejemplo de Servicio que Utiliza Proyecciones

```java
@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    public List<PersonaResumen> obtenerPersonasMayoresDe(int edad) {
        return personaRepository.findPersonasMayoresDe(edad);
    }
}
```

Este servicio encapsula la lógica para obtener personas mayores de una cierta edad, utilizando una proyección para optimizar la consulta.

##### Conclusión

Este ejemplo ilustra cómo implementar un endpoint de API REST en Spring Boot que optimiza los datos enviados y recibidos utilizando DTOs y proyecciones de Spring Data. Esta práctica es esencial para mejorar el rendimiento de las aplicaciones, especialmente en entornos con restricciones de ancho de banda o cuando se manejan grandes volúmenes de datos. Al enviar y recibir solo los datos necesarios, se reduce el overhead y se mejora la eficiencia tanto del servidor como del cliente que consume la API.


## 2. Respuestas paginadas.
Si tenemos un endpoint que es capaz de tener muchos elementos de la base de datos, no vale o no es conveniente devolver una lista demasiado grande de elementos. Primero, porque estamos yendo a la base de datos trayendo muchos elementos. Después, porque además de eso, vamos a tener muchos elementos en memoria y podemos caer en algún tipo de leak de memoria o algún tipo de Java Out of Memory error si estamos trabajando con Java, o algún problema de memoria. La paginación es el elemento que se usa para ir trayendo los elementos de a poco, ir optimizando en este punto, en este tipo de cuestión. La paginación es el mecanismo que se utiliza para dividir una respuesta en la llamada a una API en que posee una lista muy grande de elementos en pequeños bloques de elementos. Implementa siempre paginación de punta a punta (end to end) incluyendo desde la base de datos. 

Algunos programadores lo que hacen es van a la base de datos, traen todos los elementos y luego paginan en memoria. Esto no es óptimo ni para la base de datos ni para la aplicación en sí, por el gran volumen de datos que puede traer tener o traer todos los elementos de la base de datos a la hora de paginar. Por supuesto, si estás utilizando API, utiliza los estándares más conocidos del mercado, como pueden ser el las llamadas de este tipo donde se establece la página que vamos a obtener y el límite de elemento que vamos a obtener. Así, si nuestra query/búsqueda, digamos, tiene 100 elementos, podemos ir trayendo la página uno, 10 elementos; la página dos, 10 elementos; y así podemos ir trayendo página a página. Esto es mucho más optimizado y hace que nuestra API funcione de manera más rápida y haga un mejor y eficiente uso de la memoria.

## 3. El tercer elemento para lograr una API optimizada es usar caché.
La caché es un elemento fundamental en el desarrollo de aplicaciones modernas. Si los datos que vamos a devolver tienen poca variabilidad, puedes usar caché. Ayudan a optimizar los tiempos de respuesta y protegen otros elementos de la infraestructura, como por ejemplo, están yendo innecesariamente hacia la base de datos o haciendo llamadas a tercero por elementos que ya podemos tener cachado y listos para entregar una vez sean solicitados. En cuanto al uso de caché, siempre puedes dividir o usar entre caché local o caché distribuida, depende en caso de uso. Adicionalmente a esto, independientemente de que si es local o distribuida, hay varias estrategias de caché. 

## 4. Logs y el trazabilidad de forma asíncrona. 
Siempre que puedas o siempre para hacer más extenso que implementen mecanismos de observabilidad, trata de hacerlos de forma asíncrona. La escritura de esta observabilidad, traceo distribuido, escritura de logs, envío de métricas personalizadas, siempre hacerlo de forma asíncrona que no interfiera en el flujo principal o en el hilo principal de nuestra aplicación. Tu aplicación siempre tiene que ser o siempre debe cumplir con lo que se conoce con los 12 factores. Puedes googlear este término en Google con el factor que ayuda a identificar qué debe de cumplir tu aplicación para que cumpla con los 12 factores. Este elemento es uno de ellos.

## 5. Manejo de Pool de conexiones sobre la base de datos.
Las aplicaciones modernas es necesario que utilicen Pool de conexiones. Algunas librerías, como por ejemplo HikariCP en el caso de Java, te ayudan a implementar este mecanismo. Un Pool de conexiones no es más que un grupo de conexiones que ya están realizadas a la base de datos y que están disponibles para utilizar cuando sea necesario. Por ejemplo, cuando arranca nuestra aplicación, creamos un pool de 10 conexiones y cada vez que venga un request nuevo, vamos a ese pool, obtenemos una conexión ya existente, trabajamos con ella, hacemos la query y después liberamos la conexión para que el siguiente request que lo requiera haga uso de este mecanismo. Abrir y cerrar conexiones a la base de datos es costoso, tanto en tiempo como computacionalmente, y por lo tanto, no es muy recomendado o no es para nada recomendado abrir y cerrar conexiones cada vez que vamos a ir a la base de datos.

En Spring Boot, la configuración de un pool de conexiones es aún más sencilla, gracias a su capacidad de autoconfiguración. Spring Boot utiliza HikariCP como su proveedor de pool de conexiones predeterminado a partir de la versión 2.0. Si tienes las dependencias adecuadas en tu proyecto, Spring Boot configurará automáticamente HikariCP para ti.

Para usar HikariCP en un proyecto Spring Boot, solo necesitas incluir la dependencia de Spring Boot Starter JDBC o Spring Boot Starter Data JPA, que ya incluyen HikariCP. Por ejemplo, usando Maven en tu `pom.xml`:

```xml
<dependencies>
    <!-- Spring Boot Starter Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- Driver de la base de datos, por ejemplo, MySQL -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```

La configuración del pool de conexiones se puede realizar en el archivo `application.properties` o `application.yml`. Por ejemplo:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tuBaseDeDatos
spring.datasource.username=tuUsuario
spring.datasource.password=tuContraseña
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Propiedades de HikariCP
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=20000
spring.datasource.hikari.idle-timeout=300000
```



## 6. Optimizar los tiempos de respuesta de la API utilizando compresión.
Si tenemos una API REST, lo más adecuado es, si tenemos que devolver muchos datos, es que utilicemos mecanismos de compresión. Los mecanismos de compresión reducen el tamaño de los archivos JSON, en el caso que estemos usando API REST con JSON, o también se puede usar con otros, digamos, formatos de mensaje. Lo más ideal siempre es reducir este tamaño mediante compresión. Los algoritmos de ZIP y Deflate son algoritmos que permiten este tipo de compresión y que ya son más o menos el estándar de lo que se usa para hacer este tipo de tareas. El algoritmo de ZIP debes tener en cuenta que da mayor compresión y permite transmitir datos más pequeños, por lo tanto, al ser más pequeño, viaja más rápido, pero es ligeramente más lento a la hora de comprimir y descomprimir que el algoritmo de Deflate en sí. Tienes que valorar cuál usar en el caso de que te vayas a ir por este tipo de mecanismos de compresión.




