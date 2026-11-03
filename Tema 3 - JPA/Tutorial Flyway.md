# Aprender Flyway construyendo una biblioteca de aula

**Acceso a Datos · Tema 3 — JPA**  
**Tres sesiones de 55 minutos · Metodología PRIMM**  
Punto de partida: Java, Maven, SQL y JPA. No necesitas conocer Flyway.

## Antes de empezar: un problema que puedes reconocer

La biblioteca de tu aula necesita un programa pequeño. Hoy basta con guardar títulos. La semana que viene querrá conocer los autores. Más adelante necesitará indicar qué libros están prestados.

Ya sabes escribir una entidad `Libro`. También sabes escribir `CREATE TABLE` y guardar un objeto con `EntityManager`. La nueva dificultad es otra: **¿cómo cambiamos el programa y su tabla sin perder los libros que ya hemos registrado?**

Imagina un cuaderno de biblioteca. En sus primeras páginas solo hay una casilla para el título. Si ahora imprimes fichas con una casilla para el autor, las fichas antiguas no se completan solas. Necesitas decidir qué añadir y qué escribir donde todavía no conoces el dato. Además, debes recordar qué reformas del cuaderno has realizado para no repetirlas.

Flyway será nuestro **registro de reformas de la base de datos**. Nosotros escribiremos cada cambio en SQL. Flyway comprobará cuáles se hicieron y aplicará los que falten. Su aportación es que podamos reconstruir y actualizar la biblioteca siguiendo las mismas instrucciones, sin depender de recordar qué SQL ejecutó cada persona.

No aprenderemos una lista de comandos antes de usarlos. Primero veremos una necesidad, haremos una predicción y observaremos el resultado. Solo entonces pondremos nombre a lo que ha ocurrido.

## Cómo vamos a trabajar

En cada sesión seguiremos PRIMM:

| Momento | Tu trabajo |
|---|---|
| **Predict — Predecir** | Leer un fragmento pequeño y escribir qué esperas que ocurra, antes de ejecutarlo |
| **Run — Ejecutar** | Ejecutar el programa proporcionado y contrastarlo con tu predicción |
| **Investigate — Investigar** | Seguir los datos y explicar qué hace cada parte con ayuda de preguntas |
| **Modify — Modificar** | Introducir una ampliación pequeña con indicaciones |
| **Make — Crear** | Resolver una necesidad nueva tomando tus propias decisiones |

Trabaja en pareja cuando sea posible: una persona explica su predicción y la otra pide razones. Después intercambiad los papeles. Si el resultado no coincide con lo esperado, conserva ambas cosas: tu predicción y lo que has observado.

Las versiones completas son **puntos de comprobación**, no el primer ejercicio. Lee primero el apartado de la sesión y modifica tu propia copia. Consulta una versión resuelta cuando necesites comparar o recuperar el hilo.

| Sesión | Petición de la biblioteca | Lo nuevo que aprenderemos | Punto de comprobación |
|---|---|---|---|
| 1 | Guardar títulos y conservarlos al cerrar | Primera migración y recuerdo de lo ejecutado | `01-catalogo` |
| 2 | Añadir autores sin perder los títulos | Nueva versión SQL y tratamiento de datos antiguos | `02-autores` |
| 3 | Prestar y devolver libros | Diferenciar evolución del esquema y operaciones de JPA | `03-prestamos` |
| Reto final | Localizar libros por estantería | Planificar una ampliación con autonomía | `04-reto-resuelto`, solo después de intentarlo |

## Preparación breve del puesto

### Por qué este entorno es pequeño

Queremos dedicar el tiempo a entender la evolución del programa. Usaremos **H2**, una base relacional que se ejecuta dentro de Java y guarda sus datos en un archivo. No necesitas instalar un servidor. Seguiremos utilizando SQL, entidades JPA e Hibernate, como en la asignatura.

Un archivo H2 no es una lista Java: los datos sobreviven al cierre del programa. La sintaxis SQL que aparece aquí es la de H2; al trasladar el ejercicio a MySQL habrá que adaptar las sentencias específicas del motor.

Necesitas JDK 17 o 21, Maven y un editor. Comprueba:

```text
java -version
mvn -version
```

Maven descargará las bibliotecas la primera vez. Después no hay contraseñas que elegir ni servicios que arrancar.

### Preparar tu copia de trabajo

Descarga el repositorio si todavía no lo tienes:

```text
git clone https://github.com/javierfausgmail/ACD.git
cd "ACD/Tema 3 - JPA/biblioteca-flyway"
```

Si ya lo tienes, actualiza tu copia y entra en esa misma carpeta. Duplica la carpeta `01-catalogo` y llama a la copia **`trabajo`**. Puedes hacerlo desde el explorador de archivos. En PowerShell:

```powershell
Copy-Item -Recurse 01-catalogo trabajo
cd trabajo
```

En Bash:

```bash
cp -r 01-catalogo trabajo
cd trabajo
```

No repitas la copia si `trabajo` ya existe. A partir de ahora, **todos los comandos de las sesiones se ejecutan dentro de `trabajo`**, donde está su `pom.xml`. Aquí irás haciendo tus cambios. No cambiaremos de proyecto en mitad de cada ejercicio.

La estructura que vas a usar es:

```text
biblioteca-flyway/
├── 01-catalogo/           versión de referencia de la sesión 1
├── 02-autores/           referencia para comprobar la sesión 2
├── 03-prestamos/         referencia para comprobar la sesión 3
├── 04-reto-resuelto/     solución orientativa del reto
├── trabajo/              tu copia, donde programas
└── datos/                aparece al ejecutar; conserva tu biblioteca
```

El archivo de datos será `datos/biblioteca.mv.db`, compartido por las etapas. No lo borres al avanzar. Tampoco ejecutes dos versiones simultáneamente contra ese archivo. Si usas el IDE, establece como directorio de trabajo la carpeta `trabajo`.

---

## Sesión 1. Un catálogo que recuerda sus libros

**Objetivo:** explicar por qué crear una tabla y guardar un libro son tareas diferentes, y comprobar que Flyway recuerda un cambio ya realizado.

**Distribución:** contexto 5 min · Predict 7 · Run 8 · Investigate 15 · Modify 10 · Make 10.

### El encargo y la primera reforma del cuaderno

La bibliotecaria solo pide dos operaciones: registrar un título y consultar el catálogo. No necesitamos autores ni préstamos todavía.

La ficha inicial tiene dos datos:

| id | titulo |
|---:|---|
| 1 | El principito |

¿De dónde saldrá la tabla en el primer arranque? Hibernate podría generarla, pero queremos una instrucción SQL explícita que podamos leer, conservar y volver a aplicar en otra instalación. Esa instrucción será nuestra primera **migración**: un archivo que transforma la base de datos de un estado a otro.

La versión inicial ya incluye el arranque necesario. Vamos a leer solo tres piezas antes de ejecutar; los archivos completos de infraestructura están al final para que no interrumpan la explicación.

### P — Predict: lee antes de pulsar Ejecutar

**Pieza 1: `src/main/resources/db/migration/V1__crear_libros.sql`.**

```sql
CREATE TABLE libros (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    titulo VARCHAR(120) NOT NULL
);
```

Es SQL que ya conoces. En H2, `GENERATED BY DEFAULT AS IDENTITY` genera el identificador al insertar. No hay ningún `INSERT` en este archivo.

**Pieza 2: la operación `listar` de `App.java`.**

```java
var libros = em.createQuery("select l from Libro l order by l.id", Libro.class)
        .getResultList();
if (libros.isEmpty()) System.out.println("Todavía no hay libros");
for (Libro libro : libros) {
    System.out.println(libro.getId() + " | " + libro.getTitulo());
}
```

**Pieza 3: el orden del arranque, en `BaseDatos.abrir`.**

```java
migrador(url).migrate();
return Persistence.createEntityManagerFactory("biblioteca", propiedadesDeConexion);
```

El segundo argumento se muestra aquí con un nombre explicativo; en el archivo completo es un `Map`. La idea que debes leer es el orden: primero se prepara la base y después se abre JPA.

Escribe tu predicción:

1. ¿La primera consulta mostrará `El principito`, mostrará un catálogo vacío o dará un error? Justifica tu respuesta mirando el SQL.
2. Si registramos un libro y cerramos Java, ¿seguirá estando al volver a ejecutar?
3. En el segundo arranque, ¿qué pasaría si ejecutásemos `CREATE TABLE libros` otra vez sin comprobar nada?

No ejecutes aún. Compara tus respuestas con las de tu pareja.

### R — Run: contrasta tu predicción

Desde `trabajo`:

```text
mvn compile exec:java "-Dexec.args=listar"
```

Entre los mensajes de las bibliotecas debe aparecer:

```text
Todavía no hay libros
```

Ahora registra el primer libro:

```text
mvn exec:java "-Dexec.args=alta"
```

Cuando aparezca `Título:`, escribe `El principito` y pulsa Intro. Debe responder `Libro registrado`. Después:

```text
mvn exec:java "-Dexec.args=listar"
```

En una biblioteca nueva verás:

```text
1 | El principito
```

Vuelve a ejecutar `listar`. El título sigue ahí y no se produce un error por intentar crear otra vez la tabla. No hace falta que el programa Java anterior siga abierto.

**Anota:** qué predijiste, qué ocurrió y qué explicación cambiarías.

### I — Investigate: sigue las dos rutas

Al arrancar ocurren dos tareas distintas:

```text
Arranque: V1 crea la estructura → JPA puede utilizarla
Alta: escribes un título → em.persist(libro) guarda una fila
```

Flyway no contiene una lista de títulos. JPA no decide qué migraciones faltan.

Esta es la entidad de la primera etapa:

```java
package es.acd.biblioteca;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String titulo;

    protected Libro() { }

    public Libro(String titulo) {
        this.titulo = obligatorio(titulo, "Título", 120);
    }

    private static String obligatorio(String valor, String campo, int maximo) {
        if (valor == null || valor.isBlank() || valor.length() > maximo) {
            throw new IllegalArgumentException(campo + ": entre 1 y " + maximo + " caracteres");
        }
        return valor;
    }

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }

}
```

El constructor comprueba que el título tenga contenido y que quepa en los 120 caracteres de la columna. El constructor sin argumentos permite que JPA reconstruya los libros al consultarlos.

Busca en `App.alta`:

```java
System.out.print("Título: ");
String titulo = entrada.nextLine();
Libro libro = new Libro(titulo);
em.persist(libro);
```

El título viene del usuario. No hemos añadido un `INSERT` a V1 para cada libro.

Ahora mira cómo se prepara Flyway:

```java
return Flyway.configure()
        .dataSource(url, "sa", "")
        .locations("classpath:db/migration")
        .load();
```

Lee estas líneas así: «usa nuestra base H2, busca los archivos SQL en los recursos `db/migration` y prepara el migrador». En este ejemplo local, `sa` y la cadena vacía son los valores de conexión de H2, ya resueltos en el código proporcionado.

Cuando llamamos a `migrate()`, Flyway consulta su registro, aplica lo pendiente y anota el resultado. Ese registro es una tabla llamada `flyway_schema_history`. No tenemos que crearla a mano.

Consulta lo que recuerda:

```text
mvn exec:java "-Dexec.args=historial"
```

Debe aparecer una línea equivalente a:

```text
1 | crear libros | SUCCESS
```

El formato puede variar. Lo importante es que V1 conste como aplicada. Por eso el segundo arranque no repite su `CREATE TABLE`.

**Preguntas para investigar, explicando el porqué:**

- Señala la instrucción que crea la tabla y la que guarda un libro. ¿Por qué están en sitios diferentes?
- ¿Por qué cerrar el programa no vacía el catálogo?
- Si `historial` muestra una migración, ¿eso significa que solo hay un libro?
- ¿Qué le faltaría a Hibernate si abriésemos JPA antes de crear la tabla?

### M — Modify: una modificación pequeña y segura

La bibliotecaria pide numerar visualmente las entradas con un prefijo. Cambia **solo la impresión** de `listar` para obtener:

```text
Libro 1: El principito
```

Ejecuta con `mvn compile exec:java "-Dexec.args=listar"`. Después registra `Matilda` mediante `alta` y vuelve a consultar.

Comprueba que hay dos libros. **No has cambiado el esquema**, por lo que no necesitas otra migración. Modificar la presentación y modificar una tabla son necesidades distintas.

### M — Make: tu primera función propia

Sin copiar una implementación resuelta, añade una acción `contar`. Debe mostrar cuántos libros tiene la biblioteca. Tú decides si obtienes la lista o haces una consulta JPQL de recuento.

Requisitos:

- Se ejecuta con `mvn compile exec:java "-Dexec.args=contar"`.
- No registra ni borra libros.
- Con los dos títulos anteriores responde que hay dos.
- No necesita ningún archivo de migración nuevo.

**Punto de salida:** explica a otra persona qué guarda H2, qué hace JPA y qué recuerda Flyway. Conserva el archivo de datos y tu código para la próxima sesión.

<details>
<summary>Comprobación después del intento: respuestas de la sesión 1</summary>

El catálogo inicial está vacío porque V1 solo crea la tabla. `persist` y el commit guardan los títulos; el archivo H2 los conserva entre ejecuciones. Flyway registra V1 y no vuelve a crear la tabla. La acción `contar` solo consulta datos: no cambia las casillas de nuestras fichas.

</details>

---

## Sesión 2. La biblioteca necesita saber quién escribió cada libro

**Objetivo:** coordinar un cambio de entidad y tabla, y decidir qué hacer con las filas que ya existían.

**Distribución:** contexto 5 min · Predict 5 · Run 8 · Investigate 12 · Modify 15 · Make 10.

### El nuevo problema

«Tenemos dos libros, pero no sabemos sus autores. A partir de ahora quiero registrarlos». La petición no consiste en borrar el catálogo y empezar otra vez. Deben seguir apareciendo `El principito` y `Matilda`.

En nuestro cuaderno, las fichas antiguas carecen de casilla de autor. Además, aunque añadamos la casilla, todavía no hemos investigado qué valor escribir en cada ficha. Para distinguir un dato pendiente de uno conocido acordamos usar **`Pendiente`**. No inventaremos un autor real.

### P — Predict: cambiar Java no basta

Todavía estás trabajando sobre la primera etapa, con los dos títulos guardados. Considera este cambio en `Libro`:

```java
@Column(nullable = false, length = 120)
private String autor;
```

Antes de tocar nada, contesta:

1. ¿Qué mostrará el programa actual si lo ejecutamos sin modificarlo? ¿Aparecerán autores por haber decidido incorporarlos?
2. ¿Añadir ese atributo crea por sí mismo una columna en nuestra configuración?
3. ¿Qué autor deberían tener los libros anteriores?
4. Si editamos V1 para crear una columna adicional, ¿volverá a ejecutarse en nuestra biblioteca?

Busca en `persistence.xml`:

```xml
<property name="hibernate.hbm2ddl.auto" value="validate"/>
```

`validate` significa que Hibernate comprueba el esquema que necesita; no lo modifica. Es una pista para la segunda pregunta.

### R — Run: comprueba el punto de partida

Ejecuta la versión que ya funciona, **sin modificar todavía el código**:

```text
mvn compile exec:java "-Dexec.args=listar"
```

Deben seguir apareciendo los dos títulos, todavía sin autores. Conservamos así una referencia del programa antes de transformarlo. Contrasta el resultado con tu primera predicción y anota los identificadores: después comprobaremos que siguen siendo los mismos.

### I — Investigate: diseña la reforma antes de aplicarla

Sabes escribir una columna obligatoria. Pero ¿de dónde sale el valor de las filas antiguas? Dividimos la solución en tres movimientos:

| Movimiento | En las fichas | En SQL |
|---|---|---|
| 1. Ampliar | Añadir una casilla que inicialmente puede estar vacía | Añadir una columna que admite `NULL` |
| 2. Completar | Marcar las fichas antiguas como pendientes de investigar | Actualizar las filas existentes |
| 3. Establecer la regla | No aceptar nuevas fichas sin esa información | Imponer `NOT NULL` |

Una columna obligatoria asegura que haya un valor; no asegura que el autor esté verificado. `Pendiente` expresa esa diferencia en nuestro caso.

Flyway necesita una instrucción nueva porque nuestra biblioteca ya hizo V1. El nombre será:

```text
V2__anadir_autor.sql
```

Lee el nombre: versión 2, **dos guiones bajos**, descripción y extensión SQL. La versión establece el orden y debe ser única. V1 se conserva exactamente como estaba: representa una reforma que ya se hizo.

Antes de escribir la solución, ordena estas tres instrucciones en tu cuaderno y explica por qué:

```sql
ALTER TABLE libros ALTER COLUMN autor SET NOT NULL;
UPDATE libros SET autor = 'Pendiente' WHERE autor IS NULL;
ALTER TABLE libros ADD COLUMN autor VARCHAR(120);
```

### M — Modify: incorpora autores paso a paso

**Paso 0: comprobar el desajuste.** Añade ahora **solo el atributo `autor` y su anotación** del momento Predict a la entidad de `trabajo`. No cambies todavía el constructor ni la consulta. Predice si arrancará y ejecuta `mvn compile exec:java "-Dexec.args=listar"`.

Debe fallar al abrir JPA, con un mensaje que mencione una columna `autor` ausente. Localiza esa parte: Java espera una casilla que la tabla aún no tiene. Los libros no se han borrado. **No cambies Hibernate a `update` para hacer desaparecer el error:** vamos a resolver explícitamente el cambio y los datos anteriores.

**Paso 1.** Crea `src/main/resources/db/migration/V2__anadir_autor.sql`. Escribe las tres instrucciones en el orden que has razonado. Compara solo después:

<details>
<summary>Comprobar el orden de V2</summary>

```sql
ALTER TABLE libros ADD COLUMN autor VARCHAR(120);

UPDATE libros SET autor = 'Pendiente' WHERE autor IS NULL;

ALTER TABLE libros ALTER COLUMN autor SET NOT NULL;
```

</details>

**Paso 2.** Mantén el atributo `autor` que acabas de añadir e incorpora su getter:

```java
public String getAutor() { return autor; }
```

Sustituye el constructor de altas por este, reutilizando la validación que ya tiene `Libro`:

```java
public Libro(String titulo, String autor) {
    this.titulo = obligatorio(titulo, "Título", 120);
    this.autor = obligatorio(autor, "Autor", 120);
}
```

**Paso 3.** En `App.alta`, después de leer el título, pregunta el autor y cambia la creación del objeto:

```java
System.out.print("Autor: ");
String autor = entrada.nextLine();
Libro libro = new Libro(titulo, autor);
em.persist(libro);
```

Sustituye el `new Libro(titulo)` anterior, sin dejar dos construcciones. En `listar`, muestra también `libro.getAutor()`. Conserva las funciones propias que hayas añadido, como `contar`.

**Paso 4.** Predice el resultado y ejecuta:

```text
mvn compile exec:java "-Dexec.args=listar"
mvn exec:java "-Dexec.args=historial"
```

Los dos títulos siguen estando. Si conservas el formato básico, verás:

```text
1 | El principito | Pendiente
2 | Matilda | Pendiente
```

El historial contiene V1 y V2. Flyway creó la columna antes de que Hibernate comprobase el nuevo mapeo. V1 no volvió a ejecutarse.

**Paso 5.** Registra `Momo`, con autor `Michael Ende`, mediante `alta`. Ejecuta `listar` dos veces. Debe haber tres libros, no cuatro. Arrancar de nuevo no vuelve a insertar las altas ni repite V2.

Si te pierdes, compara **los cambios concretos** de `Libro.java`, `App.java` y la nueva migración con [02-autores](biblioteca-flyway/02-autores/). No sustituyas tu trabajo entero: podrías perder tu función `contar`.

### M — Make: completar una información pendiente

La bibliotecaria ha consultado una ficha y quiere corregir su autor. Diseña una acción `completarAutor` que pida identificador y autor, busque el libro y actualice su atributo dentro de la transacción existente.

Decide qué método necesita `Libro` y qué debe ocurrir si el identificador no existe. No se proporciona la función completa: ya sabes localizar entidades y modificar su estado con JPA.

**Criterios de comprobación:**

- Puedes completar el autor de `El principito` con `Antoine de Saint-Exupéry`.
- Tras cerrar y volver a listar, el cambio permanece.
- Los otros libros conservan sus datos y no cambia el número de filas.
- El historial sigue teniendo dos migraciones.

¿Por qué completar el autor de un libro concreto no requiere V3? Explícalo usando la diferencia entre cambiar una casilla y escribir dentro de ella.

<details>
<summary>Comprobación después del intento: ideas clave de la sesión 2</summary>

La entidad sola no cambia la tabla porque Hibernate está en `validate`. V2 añade la columna, completa las filas anteriores y la hace obligatoria. Las futuras altas reciben autor desde el formulario. Corregir el autor de un libro concreto es una operación de la biblioteca sobre el esquema ya preparado, no otra reforma del esquema.

</details>

---

## Sesión 3. Prestar libros y decidir tu siguiente ampliación

**Objetivo:** crear otra migración y distinguirla de las operaciones que realizan los usuarios después.

**Distribución:** contexto 3 min · Predict 5 · Run 7 · Investigate 10 · Modify 10 · Make 20.

### El nuevo problema

La biblioteca empieza a prestar ejemplares. Ahora necesita saber si un libro está disponible. Mantendremos el modelo pequeño: cada registro representa **un ejemplar físico**. Si hay dos copias de un título, serán dos registros diferentes.

No vamos a gestionar todavía alumnos, fechas ni multas. Nuestra regla es: un ejemplar disponible puede prestarse; uno prestado puede devolverse. Los ejemplares que ya hay en el catálogo están en la biblioteca, así que inicialmente estarán disponibles.

### P — Predict: un dato nuevo y una acción nueva

Lee la propuesta de V3:

```sql
ALTER TABLE libros
ADD COLUMN disponible BOOLEAN DEFAULT TRUE NOT NULL;
```

En H2, al añadir esta columna, las filas existentes reciben el valor por defecto `TRUE`. La decisión tiene un significado: todos los ejemplares del catálogo anterior están disponibles.

Predice:

1. ¿Se conservarán los títulos y autores?
2. ¿Qué valor tendrá el nuevo dato en los tres libros?
3. ¿Prestar el primer ejemplar exigirá crear V4?
4. Si una biblioteca ya tuviese préstamos fuera del programa, ¿sería correcto declarar disponibles todos sus libros?

La última pregunta importa: la herramienta no decide si la regla representa la realidad del negocio.

### R — Run: observar una actualización antes de cambiar Java

Crea `V3__anadir_disponibilidad.sql` en tu copia con la instrucción anterior. **Todavía no añadas el atributo Java.** Ejecuta:

```text
mvn compile exec:java "-Dexec.args=listar"
mvn exec:java "-Dexec.args=historial"
```

Los títulos y autores siguen apareciendo y V3 figura como aplicada. El listado aún no muestra disponibilidad porque la versión Java de la consulta no la está leyendo.

Esta ampliación es compatible con el modelo anterior: tener una columna adicional con valor por defecto no obliga a que la entidad la use inmediatamente. No todos los cambios de esquema tienen esa propiedad.

### I — Investigate: seguir el dato desde la tabla hasta el préstamo

La migración se hace una vez por instalación. Después, cada préstamo cambia el estado de una fila:

```text
V3: añade la casilla «disponible» a todos los ejemplares.
prestar: escribe false en la casilla de un ejemplar concreto.
devolver: vuelve a escribir true en esa misma casilla.
```

Investiga estas preguntas antes de programar:

- ¿Por qué `boolean` encaja con la columna SQL `BOOLEAN NOT NULL`?
- ¿Dónde debe vivir la regla «no prestar un libro ya prestado»?
- Si usamos `em.find` dentro de una transacción, ¿necesitamos hacer `persist` otra vez para cambiar el estado de ese libro existente?
- ¿Qué contendrá el historial después de prestar y devolver diez veces el mismo libro?

### M — Modify: incorpora la función de préstamo

**Paso 1.** En `Libro`, añade:

```java
@Column(nullable = false)
private boolean disponible = true;

public boolean isDisponible() { return disponible; }

public void prestar() {
    if (!disponible) throw new IllegalStateException("El libro ya está prestado");
    disponible = false;
}

public void devolver() {
    if (disponible) throw new IllegalStateException("El libro ya estaba disponible");
    disponible = true;
}
```

Hay dos valores iniciales con funciones distintas: el `DEFAULT TRUE` de SQL resuelve la columna en la base, incluyendo las filas anteriores; `= true` establece el estado de los objetos nuevos de Java. Al cargar un libro existente, JPA lee su valor real, que puede ser `false`.

**Paso 2.** Amplía la línea de impresión de `listar` con:

```java
+ " | " + (libro.isDisponible() ? "Disponible" : "Prestado")
```

Es un fragmento que se añade a la expresión que ya imprime título y autor, no una instrucción independiente.

**Paso 3.** Añade este método a `App`:

```java
private static void cambiarPrestamo(EntityManager em, Scanner entrada, String accion) {
    System.out.print("Identificador del libro: ");
    long id = Long.parseLong(entrada.nextLine());
    Libro libro = em.find(Libro.class, id);
    if (libro == null) throw new IllegalArgumentException("No existe ese libro");
    if (accion.equals("prestar")) libro.prestar();
    else libro.devolver();
    System.out.println("Estado actualizado");
}
```

En el `switch` de `main`, dentro de la transacción, incorpora:

```java
case "prestar", "devolver" -> cambiarPrestamo(em, entrada, accion);
```

Actualiza también el texto que enumera las acciones en el caso `default`.

**Paso 4.** Comprueba el ciclo completo:

```text
mvn compile exec:java "-Dexec.args=prestar"
```

Introduce el identificador de `El principito`, que será 1 si empezaste como indicamos. Lista y comprueba que solo ese ejemplar aparece prestado. Intenta prestarlo otra vez: debe rechazarse. Después:

```text
mvn exec:java "-Dexec.args=devolver"
mvn exec:java "-Dexec.args=listar"
mvn exec:java "-Dexec.args=historial"
```

Introduce el mismo identificador al devolver. Vuelve a estar disponible y el historial sigue conteniendo V1, V2 y V3. Un préstamo no es una migración. El programa cierra las transacciones y conexiones también cuando se produce un error.

Para comprobar tu implementación puedes consultar [03-prestamos](biblioteca-flyway/03-prestamos/). Conserva tus funciones `contar` y `completarAutor`.

### M — Make: una ampliación que diseñas tú

**Nuevo encargo:** hay tantos libros que cuesta encontrarlos. La biblioteca quiere registrar la estantería de cada ejemplar y ofrecer una búsqueda por ubicación.

Dispones de 20 minutos. Antes de escribir código, completa este pequeño plan:

```text
Dato nuevo y tipo:
Nombre y número de la migración:
Qué valor tendrán los libros ya registrados:
Qué pedirá el alta a partir de ahora:
Qué mostrará la nueva búsqueda:
Cómo comprobaré que los préstamos anteriores se conservan:
```

Acuerdos del negocio:

- La ubicación es un texto de hasta 30 caracteres, por ejemplo `A1`.
- Los libros antiguos reciben `Sin asignar` hasta que alguien revise su ubicación.
- Las altas nuevas deben incluir ubicación.
- La acción `buscarEstanteria` pide una ubicación y muestra los libros que están en ella.
- Los títulos, autores y estados de préstamo anteriores deben conservarse.

Pistas, sin resolverlo todo:

1. La migración siguiente será V4; no alteres V1, V2 ni V3.
2. Puedes reutilizar el patrón de añadir, rellenar e imponer una regla de V2.
3. JPA ya te permite hacer una consulta con parámetro para la búsqueda.
4. Antes de actualizar, presta un ejemplar. Después verifica que sigue prestado.

La carpeta [04-reto-resuelto](biblioteca-flyway/04-reto-resuelto/) contiene una **solución parcial orientativa** de la ampliación estructural: migración, atributo, alta y listado. No incluye tu buscador ni las funciones propias de sesiones anteriores. Ábrela después del intento para comparar decisiones, no para sustituir tu proyecto.

**Extensión si terminas antes:** añade una acción `disponibles` que liste únicamente los ejemplares que se pueden prestar. Justifica por qué esta función no requiere V5.

### Cierre: explica lo que has construido

Entrega una explicación breve con cuatro evidencias:

1. Un libro registrado antes de añadir autores sigue en el catálogo final.
2. El historial muestra las migraciones en orden y no añade otra al prestar un libro.
3. Un préstamo realizado antes de V4 conserva su estado después de V4.
4. Tu búsqueda por estantería devuelve solo los ejemplares de la ubicación elegida.

No basta con mostrar que el programa compila: explica qué necesitaba la biblioteca y qué cambio resolvió cada necesidad.

---

## Si algo no sale como esperabas

### Por qué guardar los errores también ayuda a aprender

Un error puede indicar que Java y la tabla describen versiones distintas o que has tocado una instrucción ya aplicada. Localizar esa diferencia es más útil que cambiar configuraciones hasta que el mensaje desaparezca.

| Lo que observas | Qué comprobar primero |
|---|---|
| Maven no encuentra `pom.xml` | Estás dentro de `biblioteca-flyway/trabajo` |
| No se admite la versión de Java | El JDK que muestra `mvn -version` es 17 o 21 |
| `missing column` | Creaste la migración nueva, con nombre correcto, antes de abrir JPA |
| No aparece la migración | Está en `src/main/resources/db/migration`, termina en `.sql` y el nombre lleva dos guiones bajos |
| `Checksum mismatch` | Cambiaste una migración que ya estaba aplicada |
| El archivo H2 está bloqueado | Otro programa o versión sigue usando la misma biblioteca |
| El catálogo aparece vacío sin esperarlo | Ejecutaste desde otra carpeta y cambió la ruta relativa del archivo |
| El alta pregunta un dato que no esperabas | Comprueba qué versión de `App` estás ejecutando y recompila |
| Un préstamo da error por identificador | Usa el identificador que muestra tu listado, no supongas que siempre será 1 |

Flyway guarda una huella del contenido SQL, llamada **checksum**. Si V1 se aplicó y después la modificas, la huella no coincide y la actualización se detiene. No es necesario aprender el algoritmo de esa huella: su utilidad es advertir que la historia guardada y los archivos actuales difieren.

Si el cambio fue accidental, restaura la migración exacta desde su punto de comprobación y vuelve a ejecutar. Si quieres una transformación nueva, escribe otra versión. No borres la tabla de historial para evitar el aviso.

Las bases también recuerdan avances entre sesiones. Una vez aplicada V3, volver a ejecutar una carpeta antigua no hace descender la base a V1. Continúa con tu copia actual. Para repetir **todo** el curso con un catálogo vacío, cierra los programas y renombra las carpetas `datos` y `trabajo`, conservándolas como copias. Después crea otra copia de `01-catalogo` llamada `trabajo` y comienza de nuevo en la sesión 1.

## Autoevaluación breve

Responde antes de abrir las soluciones:

1. ¿Por qué añadir un atributo Java no fue suficiente para incorporar autores?
2. ¿Por qué rellenamos los autores antiguos antes de exigir un valor obligatorio?
3. ¿Por qué no se repite `CREATE TABLE` en cada arranque?
4. ¿Qué diferencia hay entre añadir disponibilidad y prestar un ejemplar?
5. ¿Por qué no añadimos `Momo` escribiendo otra migración?
6. Si conocemos el autor de un libro que dice `Pendiente`, ¿modificamos V2 o actualizamos ese libro?
7. ¿Qué comprobación demuestra que una actualización no ha perdido datos?

<details>
<summary>Respuestas para contrastar tu explicación</summary>

1. Java esperaba una columna que la tabla no tenía; usamos Hibernate en `validate`, que no transforma el esquema.
2. Las filas anteriores necesitaban un valor coherente con la regla nueva. `Pendiente` señala una tarea de revisión.
3. Flyway registra la migración aplicada y no la repite en el flujo normal.
4. Lo primero cambia la estructura para todos los ejemplares; lo segundo cambia un dato de un ejemplar usando esa estructura.
5. Es un alta de la biblioteca. La aplicación usa JPA para guardar su fila.
6. Actualizamos el libro con una operación JPA. V2 conserva la historia de la transformación compartida.
7. Registrar información antes de migrar y comprobar después que sigue presente, incluyendo títulos, autores y estados de préstamo.

</details>

---

## Material de apoyo: los archivos completos de la primera etapa

### Por qué están aquí

El proyecto debe poder ejecutarse sin buscar piezas en otros materiales. Separamos este apoyo para que durante las sesiones puedas concentrarte en un fragmento cada vez. En el repositorio estos archivos ya están preparados: no necesitas volver a copiarlos para empezar.

### `pom.xml`

Hibernate implementa JPA, Flyway aplica las migraciones y H2 guarda la base local. JUnit se utiliza únicamente para comprobar los puntos de referencia. Las versiones están fijadas para este ejercicio, no representan una recomendación de «última versión».

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>es.acd</groupId>
    <artifactId>biblioteca-1</artifactId>
    <version>1.0</version>
    <properties>
        <maven.compiler.release>17</maven.compiler.release>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.hibernate.orm</groupId>
            <artifactId>hibernate-core</artifactId>
            <version>6.6.36.Final</version>
        </dependency>
        <dependency>
            <groupId>org.flywaydb</groupId>
            <artifactId>flyway-core</artifactId>
            <version>11.8.2</version>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <version>2.3.232</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.12.2</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.14.0</version>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.5.3</version>
            </plugin>
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>exec-maven-plugin</artifactId>
                <version>3.5.0</version>
                <configuration>
                    <mainClass>es.acd.biblioteca.App</mainClass>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

### `src/main/resources/META-INF/persistence.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="https://jakarta.ee/xml/ns/persistence" version="3.1">
    <persistence-unit name="biblioteca" transaction-type="RESOURCE_LOCAL">
        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
        <class>es.acd.biblioteca.Libro</class>
        <exclude-unlisted-classes>true</exclude-unlisted-classes>
        <properties>
            <property name="hibernate.hbm2ddl.auto" value="validate"/>
        </properties>
    </persistence-unit>
</persistence>
```

### `src/main/java/es/acd/biblioteca/BaseDatos.java`

```java
package es.acd.biblioteca;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.flywaydb.core.Flyway;
import java.util.Map;

public class BaseDatos {
    // Ejecutar desde la carpeta de la etapa: todas comparten este archivo.
    public static final String URL = "jdbc:h2:file:../datos/biblioteca";

    public static Flyway migrador(String url) {
        return Flyway.configure()
                .dataSource(url, "sa", "")
                .locations("classpath:db/migration")
                .load();
    }

    public static EntityManagerFactory abrir(String url) {
        migrador(url).migrate();
        return Persistence.createEntityManagerFactory("biblioteca", Map.of(
                "jakarta.persistence.jdbc.url", url,
                "jakarta.persistence.jdbc.user", "sa",
                "jakarta.persistence.jdbc.password", ""));
    }
}
```

`../datos/biblioteca` es la ruta común, relativa a cada carpeta de etapa o a `trabajo`. Por eso insistimos en el directorio desde el que ejecutamos. No hay dos conexiones a bases distintas: Flyway y JPA reciben la misma URL.

### `src/main/java/es/acd/biblioteca/App.java`

El control de transacciones y cierre de recursos es común a las tres sesiones. Las modificaciones de las sesiones se realizan dentro de las operaciones, manteniendo este marco que ya conoces de JPA.

```java
package es.acd.biblioteca;

import jakarta.persistence.*;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        String accion = args.length == 0 ? "listar" : args[0];
        if (accion.equals("historial")) {
            for (var paso : BaseDatos.migrador(BaseDatos.URL).info().all()) {
                System.out.println(paso.getVersion() + " | " + paso.getDescription()
                        + " | " + paso.getState());
            }
            return;
        }
        EntityManagerFactory fabrica = BaseDatos.abrir(BaseDatos.URL);
        try {
            EntityManager em = fabrica.createEntityManager();
            try {
                Scanner entrada = new Scanner(System.in);
                em.getTransaction().begin();
                switch (accion) {
                    case "listar" -> listar(em);
                    case "alta" -> alta(em, entrada);
                    default -> throw new IllegalArgumentException("Acciones: listar, alta, historial");
                }
                em.getTransaction().commit();
            } catch (RuntimeException error) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                throw error;
            } finally {
                em.close();
            }
        } finally {
            fabrica.close();
        }
    }

    private static void listar(EntityManager em) {
        var libros = em.createQuery("select l from Libro l order by l.id", Libro.class)
                .getResultList();
        if (libros.isEmpty()) System.out.println("Todavía no hay libros");
        for (Libro libro : libros) {
            System.out.println(libro.getId() + " | " + libro.getTitulo());
        }
    }

    private static void alta(EntityManager em, Scanner entrada) {
        System.out.print("Título: ");
        String titulo = entrada.nextLine();
        Libro libro = new Libro(titulo);
        em.persist(libro);
        System.out.println("Libro registrado");
    }

}
```

La entidad `Libro` y V1 ya aparecen completos en la sesión 1. Para las etapas posteriores, el tutorial indica cada modificación y las carpetas de referencia contienen todos los archivos necesarios para ejecutarlas.

## Para el profesorado: seguimiento y comprobación

### Acompañar antes de mostrar la solución

No es necesario que cada pareja termine todas las extensiones. La prioridad es que pueda justificar la predicción, observar un cambio y explicar cómo conserva los datos. Si alguien se bloquea, reduce el foco a una fila y dos instrucciones antes de mostrar una carpeta resuelta.

- **Sesión 1:** pide distinguir «crear la casilla» de «escribir un libro». Un historial con una migración no indica cuántos libros hay.
- **Sesión 2:** pide trazar una fila antigua y una nueva. Sus autores llegan por caminos distintos: relleno de V2 y formulario de alta.
- **Sesión 3:** pide predecir si el segundo préstamo debe permitirse y comprobar que el estado guardado sobrevive a otra actualización.

El momento Make transfiere responsabilidad: `contar`, `completarAutor` y `buscarEstanteria` no tienen una implementación entregada para copiar. La ampliación estructural del reto sí dispone de una referencia parcial que permite continuar a quien la necesite.

Evaluación sugerida: explicación y predicciones razonadas 30 %, conservación de datos y migraciones 30 %, funciones propias y reglas 30 %, evidencias y claridad 10 %. Las respuestas inesperadas se utilizan para investigar, no para penalizar el hecho de formular una hipótesis.

### Comprobación automática de las referencias

Desde `biblioteca-flyway` se pueden comprobar los cuatro puntos de referencia con:

```text
mvn test
```

Las pruebas usan bases H2 temporales en memoria; no alteran `datos/biblioteca.mv.db`. Comprueban creación, repetición de `migrate`, persistencia JPA y, donde corresponde, conservación de títulos al ampliar el esquema y reglas de préstamo.

En tu propia copia puedes ejecutar `mvn test` desde `trabajo`. Al introducir cambios, adapta las pruebas de esa copia a los requisitos nuevos: por ejemplo, V2 eleva de una a dos las migraciones que se esperan desde cero y cambia el constructor de `Libro`. Antes de compilar tras modificar el constructor, revisa también sus usos en `src/test`. Los tests de cada punto de comprobación ofrecen una referencia actualizada.

### Qué dejamos para después

Este recorrido se centra en migraciones SQL versionadas. La adopción de bases heredadas, las migraciones repetibles, la reparación de historiales y los despliegues con varios procesos requieren otros problemas y otra secuencia. No son necesarios para comprender y practicar la primera evolución de esta biblioteca. Tampoco se presenta este pequeño préstamo como una solución a concurrencia de múltiples usuarios.

## Fuentes para ampliar después del recorrido

- [PRIMM: portal de la metodología](https://primmportal.com/): lectura y discusión antes de modificar y crear.
- [Raspberry Pi Foundation: PRIMM y conversación en el aprendizaje de programación](https://www.raspberrypi.org/blog/primm-talk-in-programming-lessons-research-seminar/).
- [Flyway: migraciones versionadas](https://documentation.red-gate.com/flyway/flyway-concepts/migrations/versioned-migrations): orden, historial y cambios posteriores.
- [Flyway: API Java](https://documentation.red-gate.com/flyway/reference/usage/api-java): integración del migrador en el programa.
- [Flyway: soporte para H2](https://documentation.red-gate.com/flyway/reference/database-driver-reference/h2).
- [Apuntes de JPA de la asignatura](Java%20Persistance%20API.md).
