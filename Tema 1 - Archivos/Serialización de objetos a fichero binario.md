# Serialización de objetos a fichero binario

La **serialización de objetos** permite convertir el estado de un objeto Java en una secuencia de bytes para almacenarlo, por ejemplo, en un archivo binario. Posteriormente, esos bytes pueden utilizarse para reconstruir el objeto mediante **deserialización**.

En Java este mecanismo se apoya principalmente en:

- la interfaz `Serializable`;
- `ObjectOutputStream` para escribir objetos;
- `ObjectInputStream` para leerlos.

> Este mecanismo es diferente de escribir manualmente tipos primitivos con `DataOutputStream`. En este caso Java guarda y reconstruye automáticamente el grafo de objetos serializable.

---

## 1. Clase `Estudiante`

Para que un objeto pueda serializarse mediante `ObjectOutputStream`, su clase debe implementar `Serializable`.

```java
import java.io.Serializable;

public class Estudiante implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nombre;
    private int edad;
    private double notaMedia;

    public Estudiante(String nombre, int edad, double notaMedia) {
        this.nombre = nombre;
        this.edad = edad;
        this.notaMedia = notaMedia;
    }

    public String getNombre() {
        return nombre;
    }

    public int getEdad() {
        return edad;
    }

    public double getNotaMedia() {
        return notaMedia;
    }

    @Override
    public String toString() {
        return "Estudiante{" +
               "nombre='" + nombre + '\'' +
               ", edad=" + edad +
               ", notaMedia=" + notaMedia +
               '}';
    }
}
```

### `Serializable`

`Serializable` es una **interfaz de marcado**: no obliga a implementar ningún método. Simplemente indica que los objetos de esa clase pueden participar en el mecanismo de serialización estándar de Java.

Si un objeto contiene referencias a otros objetos, esos objetos también deberán ser serializables para poder guardar correctamente todo el grafo de objetos.

### `serialVersionUID`

```java
private static final long serialVersionUID = 1L;
```

Este identificador permite controlar la **compatibilidad entre versiones** de una clase serializada.

Si se modifica de forma incompatible la estructura de la clase, puede cambiarse este valor para indicar que los archivos antiguos ya no deben considerarse compatibles.

---

## 2. Serialización de una lista de estudiantes

En este ejemplo se guarda un `ArrayList<Estudiante>` completo en un archivo binario mediante `ObjectOutputStream`.

```java
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class SerializarEstudiantes {

    public static void serializarEstudiantes(
            ArrayList<Estudiante> estudiantes,
            String nombreArchivo) {

        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(nombreArchivo))) {

            out.writeObject(estudiantes);
            System.out.println("Datos guardados en " + nombreArchivo);

        } catch (IOException e) {
            System.err.println("Error al serializar: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ArrayList<Estudiante> estudiantes = new ArrayList<>();

        estudiantes.add(new Estudiante("Juan", 20, 8.5));
        estudiantes.add(new Estudiante("María", 22, 9.0));
        estudiantes.add(new Estudiante("Carlos", 21, 7.5));

        serializarEstudiantes(estudiantes, "estudiantes.bin");
    }
}
```

La llamada importante es:

```java
out.writeObject(estudiantes);
```

Java serializa el `ArrayList` y los objetos `Estudiante` contenidos en él.

---

## 3. Deserialización

Para reconstruir los objetos utilizamos `ObjectInputStream` y el método `readObject()`.

```java
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class DeserializarEstudiantes {

    @SuppressWarnings("unchecked")
    public static ArrayList<Estudiante> deserializarEstudiantes(
            String nombreArchivo) {

        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(nombreArchivo))) {

            return (ArrayList<Estudiante>) in.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al deserializar: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        ArrayList<Estudiante> estudiantes =
                deserializarEstudiantes("estudiantes.bin");

        if (estudiantes != null) {
            for (Estudiante estudiante : estudiantes) {
                System.out.println(estudiante);
            }
        }
    }
}
```

`readObject()` devuelve un `Object`, por lo que es necesario realizar una conversión al tipo esperado:

```java
(ArrayList<Estudiante>) in.readObject()
```

Si la clase necesaria para reconstruir el objeto no está disponible, puede producirse una `ClassNotFoundException`.

---

## 4. Campos que no queremos serializar: `transient`

En ocasiones una clase contiene información que no debe almacenarse. Para excluir un atributo de la serialización se utiliza `transient`.

```java
private transient String contraseñaTemporal;
```

Cuando el objeto se deserializa, este campo no recupera su valor original y adopta el valor por defecto correspondiente a su tipo (`null`, `0`, `false`, etc.).

Esto puede utilizarse para información temporal o derivada, aunque **no debe considerarse por sí solo un mecanismo de seguridad**.

---

## 5. Qué se guarda realmente

La serialización estándar de Java no guarda simplemente los valores visibles de una clase. Puede guardar un **grafo de objetos completo**, manteniendo referencias entre ellos.

Por ejemplo, si una clase `Curso` contiene una lista de objetos `Estudiante`, al serializar el curso también se intentarán serializar los estudiantes referenciados.

```text
Curso
└── estudiantes
    ├── Estudiante
    ├── Estudiante
    └── Estudiante
```

Por ello, todas las clases implicadas deben ser serializables, salvo los campos marcados como `transient`.

---

## 6. Limitaciones y uso adecuado

La serialización nativa de Java resulta cómoda para ejercicios, prototipos o persistencia interna muy controlada, pero presenta algunas limitaciones:

- los archivos quedan fuertemente ligados a las clases Java;
- los cambios en las clases pueden provocar incompatibilidades;
- el formato binario no es interoperable con otros lenguajes de forma sencilla;
- no es adecuado para intercambiar información entre sistemas heterogéneos.

Para intercambio de datos suele ser preferible utilizar formatos como **JSON**, **XML** o formatos binarios específicos.

### Seguridad

No debe deserializarse mediante `ObjectInputStream` contenido procedente de una fuente no confiable. La deserialización de objetos manipulados puede provocar vulnerabilidades graves si existen clases peligrosas en el entorno de ejecución.

Por tanto:

- utilice este mecanismo únicamente con archivos cuyo origen sea confiable;
- no acepte directamente archivos serializados enviados por usuarios desconocidos;
- para intercambio externo de datos, prefiera formatos con una estructura explícita y validable.

---

## 7. Resumen del proceso

```text
Objeto Java
    │
    │ ObjectOutputStream.writeObject()
    ▼
Archivo binario
    │
    │ ObjectInputStream.readObject()
    ▼
Objeto Java reconstruido
```

| Concepto | Función |
|---|---|
| `Serializable` | Indica que una clase puede serializarse |
| `ObjectOutputStream` | Escribe objetos serializados |
| `ObjectInputStream` | Lee y reconstruye objetos |
| `serialVersionUID` | Controla compatibilidad entre versiones |
| `transient` | Excluye un campo de la serialización estándar |

---

## Autoevaluación

Seleccione una única respuesta correcta en cada pregunta.

1. **¿Qué debe hacer una clase para utilizar la serialización estándar de objetos de Java?**
   - a) Heredar de `ObjectOutputStream`.
   - b) Implementar `Serializable`.
   - c) Implementar `Runnable`.
   - d) Tener únicamente atributos primitivos.

2. **¿Qué clase se utiliza para escribir objetos serializados en un archivo?**
   - a) `DataInputStream`
   - b) `BufferedWriter`
   - c) `ObjectOutputStream`
   - d) `Scanner`

3. **¿Qué método escribe un objeto mediante `ObjectOutputStream`?**
   - a) `writeObject()`
   - b) `writeLine()`
   - c) `saveObject()`
   - d) `serialize()`

4. **¿Qué hace `transient` sobre un atributo?**
   - a) Hace que el atributo sea constante.
   - b) Evita que participe en la serialización estándar.
   - c) Convierte el atributo en estático.
   - d) Cifra automáticamente su contenido.

5. **¿Para qué se utiliza `serialVersionUID`?**
   - a) Para indicar el tamaño máximo del archivo.
   - b) Para controlar la compatibilidad entre versiones de una clase serializable.
   - c) Para numerar los objetos guardados.
   - d) Para convertir un objeto en JSON.

6. **Si un objeto serializable contiene otro objeto como atributo, ¿qué ocurre normalmente?**
   - a) El objeto interno debe ser también serializable, salvo que el campo sea `transient`.
   - b) Java lo convierte automáticamente en texto.
   - c) El objeto interno siempre se ignora.
   - d) Solo se guarda su dirección de memoria.

7. **¿Es recomendable deserializar archivos recibidos de una fuente desconocida mediante `ObjectInputStream`?**
   - a) Sí, siempre.
   - b) Sí, si tienen extensión `.bin`.
   - c) No, porque puede suponer un riesgo de seguridad.
   - d) Solo si contienen un único objeto.

8. **¿Qué alternativa suele ser más apropiada para intercambiar datos entre aplicaciones escritas en distintos lenguajes?**
   - a) Serialización nativa de Java.
   - b) JSON o XML.
   - c) `serialVersionUID`.
   - d) `transient`.

### Soluciones

1. **b** · 2. **c** · 3. **a** · 4. **b** · 5. **b** · 6. **a** · 7. **c** · 8. **b**
