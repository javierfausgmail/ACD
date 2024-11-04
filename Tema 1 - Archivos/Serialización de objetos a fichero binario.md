Ejemplo completo en Java para serializar y deserializar una clase `Estudiante` a un archivo binario. Se incluirá el código para:

1. Crear la clase `Estudiante` que implementa la interfaz `Serializable`.
2. Serializar un `ArrayList<Estudiante>` a un archivo.
3. Deserializar dicho `ArrayList<Estudiante>` desde el archivo.

### 1. Clase `Estudiante`

La clase `Estudiante` debe implementar la interfaz `Serializable` para que los objetos de esta clase puedan ser serializados.

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

### 2. Serialización de un `ArrayList<Estudiante>`

En este paso se serializa un `ArrayList` que contiene objetos de tipo `Estudiante` a un archivo binario. Se utiliza `ObjectOutputStream` para escribir el objeto al archivo.

```java
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class SerializarEstudiantes {

    public static void serializarEstudiantes(ArrayList<Estudiante> estudiantes, String nombreArchivo) {
        try (FileOutputStream fileOut = new FileOutputStream(nombreArchivo);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(estudiantes);
            System.out.println("El ArrayList de estudiantes ha sido serializado en " + nombreArchivo);
        } catch (IOException e) {
            e.printStackTrace();
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

### 3. Deserialización del `ArrayList<Estudiante>`

Para deserializar el `ArrayList<Estudiante>`, se utiliza `ObjectInputStream` para leer el objeto desde el archivo y devolverlo en su forma original.

```java
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class DeserializarEstudiantes {

    public static ArrayList<Estudiante> deserializarEstudiantes(String nombreArchivo) {
        ArrayList<Estudiante> estudiantes = null;
        try (FileInputStream fileIn = new FileInputStream(nombreArchivo);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            estudiantes = (ArrayList<Estudiante>) in.readObject();
            System.out.println("El ArrayList de estudiantes ha sido deserializado desde " + nombreArchivo);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return estudiantes;
    }

    public static void main(String[] args) {
        ArrayList<Estudiante> estudiantes = deserializarEstudiantes("estudiantes.bin");

        if (estudiantes != null) {
            for (Estudiante estudiante : estudiantes) {
                System.out.println(estudiante);
            }
        }
    }
}
```

### Explicación

1. **Serialización**:
   - En `SerializarEstudiantes`, se crea un `ArrayList` de objetos `Estudiante`.
   - Se llama al método `serializarEstudiantes`, que toma el `ArrayList` y el nombre del archivo (`estudiantes.bin`) y serializa los datos.
   
2. **Deserialización**:
   - En `DeserializarEstudiantes`, se lee el archivo `estudiantes.bin` y se convierte de nuevo a un `ArrayList<Estudiante>`.
   - Los datos deserializados se imprimen en la consola.

Este ejemplo muestra cómo serializar y deserializar un `ArrayList` de objetos personalizados usando la interfaz `Serializable`.