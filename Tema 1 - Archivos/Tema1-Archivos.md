# Tema 1. Acceso a datos mediante archivos en Java

## 1. Introducción al acceso a datos

### 1.1 Concepto de acceso a datos

El **acceso a datos** engloba las técnicas que permiten a una aplicación leer, escribir y procesar información almacenada en distintas fuentes. En este tema nos centraremos en el trabajo con **archivos** desde Java.

Conceptos básicos:

- **Archivo**: conjunto de datos almacenado de forma persistente en un sistema de archivos.
- **Entrada**: datos que el programa recibe o lee.
- **Salida**: datos que el programa genera o escribe.
- **Stream**: flujo secuencial de datos utilizado para leer o escribir información.
- **Buffer**: área temporal de memoria que permite reducir el número de operaciones físicas de lectura o escritura.

Java ofrece dos grandes familias clásicas para trabajar con entrada/salida:

- `InputStream` / `OutputStream`: trabajan principalmente con **bytes**.
- `Reader` / `Writer`: trabajan con **caracteres**.

### 1.2 ¿Por qué trabajar con archivos?

Los archivos permiten:

- conservar datos después de finalizar el programa;
- intercambiar información entre aplicaciones;
- importar y exportar datos;
- almacenar configuraciones;
- generar registros de actividad (_logs_);
- procesar información sin necesidad de utilizar una base de datos.

---

## 2. Tipos de archivos

### 2.1 Archivos de texto

Contienen caracteres que pueden interpretarse mediante una codificación, normalmente **UTF-8**.

Son fáciles de inspeccionar con un editor de texto y resultan apropiados para información sencilla.

Clases habituales en Java:

- `FileReader`
- `FileWriter`
- `BufferedReader`
- `BufferedWriter`

### 2.2 Archivos binarios

Contienen bytes cuyo significado depende del formato utilizado. No están pensados para ser leídos directamente por una persona.

Clases habituales:

- `FileInputStream`
- `FileOutputStream`
- `DataInputStream`
- `DataOutputStream`

`DataInputStream` y `DataOutputStream` permiten trabajar de forma cómoda con tipos primitivos de Java como `int`, `float`, `double` o `boolean`.

> Los datos deben leerse en el mismo orden y con los mismos tipos con los que fueron escritos.

### 2.3 JSON y XML

**JSON** y **XML** son formatos de texto estructurado utilizados habitualmente para almacenar e intercambiar información.

- **JSON** es compacto y muy frecuente en APIs y aplicaciones modernas.
- **XML** es más verboso, pero dispone de mecanismos maduros de validación, atributos, namespaces y esquemas.

En este tema utilizaremos:

- **Jackson** para JSON.
- **JDOM 2** para XML.

---

## 3. Lectura y escritura de archivos de texto

### 3.1 Lectura con `BufferedReader`

`FileReader` permite leer caracteres de un archivo. `BufferedReader` lo envuelve para realizar una lectura más cómoda y eficiente, especialmente línea a línea.

```java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LecturaArchivoTexto {
    public static void main(String[] args) {
        try (BufferedReader reader =
                 new BufferedReader(new FileReader("datos.txt"))) {

            String linea;
            while ((linea = reader.readLine()) != null) {
                System.out.println(linea);
            }

        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }
}
```

El bloque `try (...)` es un **try-with-resources**. Al finalizar el bloque, Java cierra automáticamente el archivo.

### 3.2 Escritura con `BufferedWriter`

```java
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class EscrituraArchivoTexto {
    public static void main(String[] args) {
        try (BufferedWriter writer =
                 new BufferedWriter(new FileWriter("datos.txt"))) {

            writer.write("Primera línea");
            writer.newLine();
            writer.write("Segunda línea");

        } catch (IOException e) {
            System.err.println("Error al escribir el archivo: " + e.getMessage());
        }
    }
}
```

Por defecto, `FileWriter` sobrescribe el archivo. Para añadir contenido al final:

```java
new FileWriter("datos.txt", true)
```

### 3.3 Codificación de caracteres

`FileReader` y `FileWriter` pueden depender de la codificación predeterminada del sistema. Cuando la codificación sea importante, es preferible indicarla expresamente.

Con las APIs modernas de Java:

```java
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

String contenido = Files.readString(
        Path.of("datos.txt"),
        StandardCharsets.UTF_8
);
```

Y para escribir:

```java
Files.writeString(
        Path.of("datos.txt"),
        "Hola",
        StandardCharsets.UTF_8
);
```

---

## 4. Lectura y escritura de archivos binarios

### 4.1 Escritura con `DataOutputStream`

```java
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class EscrituraBinaria {
    public static void main(String[] args) {
        try (DataOutputStream dos =
                 new DataOutputStream(new FileOutputStream("datos.bin"))) {

            dos.writeInt(25);
            dos.writeFloat(1.75f);
            dos.writeBoolean(true);

        } catch (IOException e) {
            System.err.println("Error al escribir: " + e.getMessage());
        }
    }
}
```

### 4.2 Lectura con `DataInputStream`

```java
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class LecturaBinaria {
    public static void main(String[] args) {
        try (DataInputStream dis =
                 new DataInputStream(new FileInputStream("datos.bin"))) {

            int edad = dis.readInt();
            float altura = dis.readFloat();
            boolean matriculado = dis.readBoolean();

            System.out.println(edad);
            System.out.println(altura);
            System.out.println(matriculado);

        } catch (IOException e) {
            System.err.println("Error al leer: " + e.getMessage());
        }
    }
}
```

El orden de lectura debe coincidir exactamente con el orden de escritura:

```text
writeInt()      -> readInt()
writeFloat()    -> readFloat()
writeBoolean()  -> readBoolean()
```

### 4.3 Varios registros en un archivo binario

Para guardar varios registros se puede escribir cada registro de forma consecutiva.

```java
import java.io.*;

public class PersonasBinario {

    public static void guardarPersona(Persona persona) {
        try (DataOutputStream dos = new DataOutputStream(
                new FileOutputStream("personas.bin", true))) {

            dos.writeUTF(persona.nombre());
            dos.writeInt(persona.edad());
            dos.writeFloat(persona.altura());
            dos.writeBoolean(persona.matriculado());

        } catch (IOException e) {
            System.err.println("Error al escribir: " + e.getMessage());
        }
    }

    public static void mostrarPersonas() {
        try (DataInputStream dis = new DataInputStream(
                new FileInputStream("personas.bin"))) {

            while (true) {
                String nombre = dis.readUTF();
                int edad = dis.readInt();
                float altura = dis.readFloat();
                boolean matriculado = dis.readBoolean();

                System.out.println(
                    new Persona(nombre, edad, altura, matriculado)
                );
            }

        } catch (EOFException e) {
            // Fin normal del archivo
        } catch (IOException e) {
            System.err.println("Error al leer: " + e.getMessage());
        }
    }
}

record Persona(String nombre, int edad, float altura, boolean matriculado) {}
```

`EOFException` permite detectar que se ha alcanzado el final del archivo cuando no se conoce previamente el número de registros.

> Implementar `Serializable` no es necesario cuando los datos se escriben manualmente mediante `DataOutputStream`. `Serializable` pertenece al mecanismo de serialización de objetos de Java, que es diferente.

---

## 5. JSON con Jackson

### 5.1 Dependencia Maven

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.17.2</version>
</dependency>
```

> En un proyecto real conviene revisar periódicamente la versión estable disponible de la dependencia.

### 5.2 Serialización y deserialización

**Serializar** significa convertir un objeto Java a una representación externa, en este caso JSON.

**Deserializar** es el proceso inverso.

```java
public record Persona(
        String nombre,
        int edad,
        float altura,
        boolean matriculado
) {}
```

```java
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class JsonExample {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();

        Persona persona = new Persona("Juan", 30, 1.80f, true);

        try {
            mapper.writeValue(new File("persona.json"), persona);

            Persona personaLeida = mapper.readValue(
                    new File("persona.json"),
                    Persona.class
            );

            System.out.println(personaLeida);

        } catch (IOException e) {
            System.err.println("Error con JSON: " + e.getMessage());
        }
    }
}
```

### 5.3 Listas de objetos

```java
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonLista {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        File archivo = new File("personas.json");

        List<Persona> personas = List.of(
                new Persona("Ana", 28, 1.65f, true),
                new Persona("Luis", 31, 1.78f, false)
        );

        try {
            mapper.writeValue(archivo, personas);

            List<Persona> leidas = mapper.readValue(
                    archivo,
                    new TypeReference<List<Persona>>() {}
            );

            leidas.forEach(System.out::println);

        } catch (IOException e) {
            System.err.println("Error con JSON: " + e.getMessage());
        }
    }
}
```

### 5.4 Aspectos importantes

- JSON representa objetos, arrays y valores primitivos.
- Las propiedades de los objetos no deben tratarse como si tuvieran un orden significativo.
- Para cantidades monetarias conviene utilizar `BigDecimal` en lugar de `double` cuando se necesita precisión decimal exacta.
- Para documentos muy grandes puede utilizarse la API de streaming de Jackson.
- Un documento puede validarse mediante **JSON Schema** si se necesita controlar formalmente su estructura.

---

## 6. XML con JDOM 2

### 6.1 Dependencia Maven

```xml
<dependency>
    <groupId>org.jdom</groupId>
    <artifactId>jdom2</artifactId>
    <version>2.0.6.1</version>
</dependency>
```

### 6.2 Lectura de XML

Supongamos el siguiente archivo:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<personas>
    <persona>
        <nombre>Ana</nombre>
        <edad>28</edad>
    </persona>
</personas>
```

Lectura con JDOM 2:

```java
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.File;

public class LeerXml {
    public static void main(String[] args) {
        try {
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(new File("personas.xml"));

            Element raiz = document.getRootElement();

            for (Element persona : raiz.getChildren("persona")) {
                String nombre = persona.getChildText("nombre");
                int edad = Integer.parseInt(persona.getChildText("edad"));

                System.out.println(nombre + " - " + edad);
            }

        } catch (Exception e) {
            System.err.println("Error al leer XML: " + e.getMessage());
        }
    }
}
```

### 6.3 Escritura de XML

```java
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.FileOutputStream;

public class EscribirXml {
    public static void main(String[] args) {
        try {
            Element raiz = new Element("personas");
            Document documento = new Document(raiz);

            Element persona = new Element("persona");
            persona.addContent(new Element("nombre").setText("Ana"));
            persona.addContent(new Element("edad").setText("28"));
            raiz.addContent(persona);

            XMLOutputter salida = new XMLOutputter(Format.getPrettyFormat());

            try (FileOutputStream fos = new FileOutputStream("personas.xml")) {
                salida.output(documento, fos);
            }

        } catch (Exception e) {
            System.err.println("Error al escribir XML: " + e.getMessage());
        }
    }
}
```

### 6.4 XPath, XSD y seguridad

**XPath** permite seleccionar elementos dentro de un XML mediante rutas.

Ejemplo:

```text
/personas/persona[1]/nombre
```

Un documento XML también puede validarse mediante **XSD**, que permite definir elementos, tipos de datos y cardinalidades.

Cuando se procesan documentos XML procedentes de fuentes no confiables hay que configurar el parser para impedir la resolución de entidades externas y evitar vulnerabilidades como **XXE**.

Para documentos muy grandes, JDOM puede no ser la opción más eficiente porque construye el árbol completo en memoria. En esos casos pueden utilizarse APIs de streaming como **SAX** o **StAX**.

---

## 7. Manejo de excepciones y buenas prácticas

### 7.1 Excepciones frecuentes

Al trabajar con archivos pueden aparecer, entre otras:

- `FileNotFoundException`: el archivo no existe o no puede abrirse.
- `IOException`: error general de entrada/salida.
- `EOFException`: se intenta leer más allá del final de un flujo binario.

### 7.2 Try-with-resources

Siempre que un objeto implemente `AutoCloseable`, es recomendable utilizar **try-with-resources**.

```java
try (BufferedReader reader =
         new BufferedReader(new FileReader("datos.txt"))) {

    String linea;
    while ((linea = reader.readLine()) != null) {
        System.out.println(linea);
    }

} catch (IOException e) {
    System.err.println(e.getMessage());
}
```

No es necesario cerrar manualmente el `BufferedReader`: Java lo hace automáticamente.

### 7.3 Buenas prácticas

- Utilizar **try-with-resources** para cerrar archivos y streams.
- Especificar **UTF-8** cuando la codificación sea relevante.
- No asumir que un archivo existe o tiene un formato correcto.
- Validar los datos leídos antes de utilizarlos.
- No guardar contraseñas, tokens u otra información sensible en texto plano.
- Elegir el formato en función de la estructura de los datos y del sistema con el que se intercambian.
- Utilizar librerías especializadas para formatos como JSON, XML o CSV en lugar de procesarlos manualmente cuando exista una alternativa adecuada.

---

## 8. Comparación rápida

| Formato | Tipo | Ventaja principal | Limitación principal |
|---|---|---|---|
| TXT | Texto | Muy sencillo | Estructura definida por la aplicación |
| CSV | Texto tabular | Compacto para filas y columnas | Poco adecuado para jerarquías |
| Binario | Bytes | Eficiente y compacto | No legible directamente |
| JSON | Texto estructurado | Compacto y muy usado en APIs | Más pesado que CSV para tablas simples |
| XML | Texto estructurado | Validación, atributos y namespaces | Más verboso |

---

## 9. Actividades prácticas optativas

### 9.1 Gestión de personas en texto

Crear una aplicación que permita:

- añadir personas a un archivo;
- listar personas;
- buscar por nombre.

### 9.2 Gestión de empleados en binario

Guardar para cada empleado:

- nombre;
- edad;
- salario;
- estado activo/inactivo.

Después, leer todos los registros del archivo.

### 9.3 Inventario en JSON

Crear una aplicación que almacene una lista de productos en JSON utilizando Jackson.

Operaciones mínimas:

- añadir producto;
- listar productos;
- buscar por código.

### 9.4 Inventario en XML

Repetir el ejercicio anterior utilizando XML y JDOM 2.

---

## 10. Autoevaluación

Seleccione una única respuesta correcta en cada pregunta.

1. **¿Qué diferencia principal existe entre `Reader` e `InputStream`?**
   - a) `Reader` trabaja con caracteres y `InputStream` con bytes.
   - b) `Reader` solo sirve para XML.
   - c) `InputStream` solo sirve para texto.
   - d) No existe ninguna diferencia.

2. **¿Para qué sirve un buffer en operaciones de entrada/salida?**
   - a) Para cifrar el archivo.
   - b) Para almacenar temporalmente datos y reducir operaciones de I/O.
   - c) Para convertir texto en XML.
   - d) Para comprobar si un archivo existe.

3. **¿Qué construcción permite cerrar automáticamente un recurso en Java?**
   - a) `switch`
   - b) `try-with-resources`
   - c) `synchronized`
   - d) `finally-with-resources`

4. **Si se escribe un `int`, después un `float` y después un `boolean` con `DataOutputStream`, ¿cómo deben leerse?**
   - a) En cualquier orden.
   - b) Primero el boolean, después el int y después el float.
   - c) En el mismo orden y con los mismos tipos.
   - d) Como cadenas de texto.

5. **¿Qué excepción puede utilizarse para detectar el final de un archivo leído con `DataInputStream`?**
   - a) `EOFException`
   - b) `NullPointerException`
   - c) `SQLException`
   - d) `NumberFormatException`

6. **¿Qué librería se utiliza en este tema para trabajar con JSON?**
   - a) JDOM 2
   - b) Jackson
   - c) JDBC
   - d) Swing

7. **¿Qué significa deserializar JSON?**
   - a) Convertir un objeto Java en JSON.
   - b) Comprimir un archivo JSON.
   - c) Convertir JSON en objetos o estructuras de Java.
   - d) Eliminar propiedades de un objeto.

8. **¿Qué librería se utiliza en este tema para trabajar con XML?**
   - a) Jackson
   - b) JDOM 2
   - c) Apache Commons CSV
   - d) JUnit

9. **¿Qué tecnología permite seleccionar elementos dentro de un XML mediante rutas?**
   - a) XPath
   - b) JSONPath
   - c) JDBC
   - d) UTF-8

10. **¿Qué formato suele ser más apropiado para intercambiar una estructura jerárquica ligera mediante una API web?**
    - a) JSON
    - b) Archivo binario propio
    - c) TXT sin estructura
    - d) Una imagen PNG

11. **¿Qué codificación resulta recomendable para archivos de texto cuando se quiere evitar dependencia de la configuración local del sistema?**
    - a) UTF-8
    - b) ASCII exclusivamente
    - c) La que seleccione aleatoriamente la JVM
    - d) Ninguna codificación

12. **¿Por qué no es necesario que una clase implemente `Serializable` cuando sus campos se escriben manualmente con `DataOutputStream`?**
    - a) Porque `DataOutputStream` ya escribe directamente los valores indicados.
    - b) Porque los archivos binarios no pueden almacenar objetos.
    - c) Porque `Serializable` solo funciona con JSON.
    - d) Porque Java serializa siempre todos los objetos automáticamente.

### Soluciones

1. **a** · 2. **b** · 3. **b** · 4. **c** · 5. **a** · 6. **b** · 7. **c** · 8. **b** · 9. **a** · 10. **a** · 11. **a** · 12. **a**

---

## 11. Material complementario

- **Acceso a Datos**, Carlos Alberto Cortijo Bon.
- **Java: The Complete Reference**, Herbert Schildt.
- Documentación oficial de Java.
- Documentación de Jackson.
- Documentación de JDOM 2.
