# Gestión de documentos PDF con Apache PDFBox

Apache PDFBox es una biblioteca Java de código abierto que permite **crear, leer y modificar documentos PDF**. En este tema se trabajarán las operaciones más habituales: crear un documento, añadir texto e imágenes, extraer contenido, consultar metadatos y combinar varios archivos.

> Los ejemplos utilizan **Apache PDFBox 3.x**.

---

## 1. ¿Qué es un documento PDF?

PDF (*Portable Document Format*) es un formato diseñado para representar documentos manteniendo su apariencia independientemente del sistema operativo o del dispositivo utilizado para visualizarlos.

Un PDF puede contener, entre otros elementos:

- páginas;
- texto;
- imágenes;
- gráficos vectoriales;
- fuentes;
- anotaciones;
- formularios;
- metadatos.

A diferencia de un archivo de texto, un PDF no almacena necesariamente el contenido siguiendo el orden visual en que una persona lo lee. Internamente está formado por objetos y flujos de contenido. Por ello, **extraer o modificar texto de un PDF puede ser más complejo de lo que parece**.

---

## 2. Añadir PDFBox a un proyecto Java

### 2.1 Dependencia Maven

```xml
<dependency>
    <groupId>org.apache.pdfbox</groupId>
    <artifactId>pdfbox</artifactId>
    <version>3.0.8</version>
</dependency>
```

En un proyecto real conviene comprobar periódicamente la versión estable disponible.

### 2.2 Clases principales

| Clase | Uso principal |
|---|---|
| `PDDocument` | Representa un documento PDF |
| `PDPage` | Representa una página |
| `PDPageContentStream` | Permite escribir texto y gráficos en una página |
| `PDFTextStripper` | Extrae texto |
| `PDImageXObject` | Representa una imagen |
| `PDDocumentInformation` | Permite consultar y modificar metadatos |
| `PDFMergerUtility` | Combina varios documentos PDF |

---

## 3. Crear un PDF

### 3.1 Crear un documento con una página

```java
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.IOException;

public class CrearPdf {
    public static void main(String[] args) {
        try (PDDocument document = new PDDocument()) {

            PDPage page = new PDPage();
            document.addPage(page);

            document.save("documento.pdf");

        } catch (IOException e) {
            System.err.println("Error al crear el PDF: " + e.getMessage());
        }
    }
}
```

El uso de **try-with-resources** garantiza que el documento se cierre correctamente.

---

## 4. Escribir texto en una página

El contenido de una página se escribe mediante `PDPageContentStream`.

En PDFBox 3 las fuentes estándar se crean mediante `Standard14Fonts.FontName`.

```java
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.IOException;

public class PdfConTexto {
    public static void main(String[] args) {

        try (PDDocument document = new PDDocument()) {

            PDPage page = new PDPage();
            document.addPage(page);

            PDType1Font fuente = new PDType1Font(
                    Standard14Fonts.FontName.HELVETICA
            );

            try (PDPageContentStream content =
                     new PDPageContentStream(document, page)) {

                content.beginText();
                content.setFont(fuente, 14);
                content.newLineAtOffset(100, 700);
                content.showText("Hola desde Apache PDFBox");
                content.endText();
            }

            document.save("texto.pdf");

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
```

### Sistema de coordenadas

Las posiciones se expresan mediante coordenadas.

En una página sin transformaciones especiales:

- el origen `(0, 0)` se encuentra en la esquina inferior izquierda;
- `x` aumenta hacia la derecha;
- `y` aumenta hacia arriba.

Por ejemplo:

```java
content.newLineAtOffset(100, 700);
```

sitúa el inicio del texto a 100 puntos del borde izquierdo y 700 puntos desde la parte inferior.

> PDFBox no proporciona automáticamente un sistema de maquetación como HTML o un procesador de textos. Si un párrafo debe ajustarse a varias líneas, la aplicación debe calcular esos saltos.

---

## 5. Insertar una imagen

```java
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

PDImageXObject imagen =
        PDImageXObject.createFromFile("imagen.jpg", document);

try (PDPageContentStream content =
         new PDPageContentStream(document, page)) {

    content.drawImage(imagen, 100, 400, 250, 180);
}
```

Los parámetros indican:

```text
drawImage(imagen, x, y, anchura, altura)
```

La imagen puede redimensionarse especificando la anchura y la altura deseadas.

---

## 6. Leer un PDF existente

En PDFBox 3 los documentos existentes se cargan mediante la clase `Loader`.

### 6.1 Extraer texto

```java
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class LeerPdf {
    public static void main(String[] args) {

        try (PDDocument document =
                 Loader.loadPDF(new File("documento.pdf"))) {

            PDFTextStripper stripper = new PDFTextStripper();
            String texto = stripper.getText(document);

            System.out.println(texto);

        } catch (IOException e) {
            System.err.println("Error al leer el PDF: " + e.getMessage());
        }
    }
}
```

### Una limitación importante

`PDFTextStripper` solo puede extraer **texto real almacenado en el PDF**.

Si el documento contiene una página escaneada como imagen, PDFBox no puede reconocer automáticamente el texto que aparece en ella. Para eso sería necesaria una herramienta de **OCR**.

Además, un PDF no es una base de datos: tablas, columnas o párrafos pueden no estar almacenados internamente con la misma estructura que se observa visualmente.

---

## 7. Leer y modificar metadatos

Un documento puede contener información como título, autor, asunto o palabras clave.

### Leer metadatos

```java
PDDocumentInformation info = document.getDocumentInformation();

System.out.println("Título: " + info.getTitle());
System.out.println("Autor: " + info.getAuthor());
System.out.println("Palabras clave: " + info.getKeywords());
```

### Modificar metadatos

```java
PDDocumentInformation info = document.getDocumentInformation();

info.setTitle("Informe anual");
info.setAuthor("Departamento de Informática");
info.setSubject("Resultados del curso");

document.save("informe.pdf");
```

---

## 8. Modificar un PDF existente

Se puede abrir un documento y añadir contenido a una página existente.

```java
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.File;

public class ModificarPdf {
    public static void main(String[] args) {

        try (PDDocument document =
                 Loader.loadPDF(new File("entrada.pdf"))) {

            PDPage page = document.getPage(0);

            PDType1Font fuente = new PDType1Font(
                    Standard14Fonts.FontName.HELVETICA
            );

            try (PDPageContentStream content =
                     new PDPageContentStream(
                             document,
                             page,
                             PDPageContentStream.AppendMode.APPEND,
                             true
                     )) {

                content.beginText();
                content.setFont(fuente, 12);
                content.newLineAtOffset(100, 100);
                content.showText("Texto añadido posteriormente");
                content.endText();
            }

            document.save("salida.pdf");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
```

### ¿Se puede editar directamente un texto existente?

No de la misma forma que en un procesador de textos.

El texto de un PDF suele estar almacenado como **instrucciones de dibujo** dentro de flujos de contenido. PDFBox permite acceder a esas estructuras, pero reemplazar una palabra manteniendo automáticamente el diseño original puede ser complejo.

Por este motivo, las operaciones más habituales son:

- añadir contenido;
- eliminar o añadir páginas;
- modificar formularios o anotaciones;
- cambiar metadatos;
- reconstruir una página cuando sea necesario.

---

## 9. Añadir y eliminar páginas

### Añadir

```java
PDPage nuevaPagina = new PDPage();
document.addPage(nuevaPagina);
```

### Eliminar

```java
document.removePage(0);
```

El índice `0` corresponde a la primera página.

---

## 10. Combinar varios documentos PDF

PDFBox incorpora `PDFMergerUtility`.

```java
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.IOException;

public class UnirPdf {
    public static void main(String[] args) {

        PDFMergerUtility merger = new PDFMergerUtility();

        merger.addSource("documento1.pdf");
        merger.addSource("documento2.pdf");

        merger.setDestinationFileName("resultado.pdf");

        try {
            merger.mergeDocuments(
                    MemoryUsageSetting.setupMainMemoryOnly()
            );
        } catch (IOException e) {
            System.err.println("Error al combinar PDF: " + e.getMessage());
        }
    }
}
```

Esta operación resulta útil para generar expedientes, informes o documentos formados por varias partes.

---

## 11. Fuentes externas y caracteres especiales

Las fuentes estándar de PDF son suficientes para ejemplos sencillos, pero tienen un conjunto limitado de caracteres.

Para trabajar de forma fiable con Unicode o con fuentes corporativas puede cargarse una fuente TrueType mediante `PDType0Font`.

```java
import org.apache.pdfbox.pdmodel.font.PDType0Font;

PDType0Font fuente = PDType0Font.load(
        document,
        new File("fuente.ttf")
);
```

Al incrustar una fuente en el PDF se consigue que el documento mantenga mejor su apariencia en otros equipos, siempre que la licencia de la fuente permita su incrustación.

---

## 12. Buenas prácticas

Al trabajar con PDFBox conviene:

- utilizar **try-with-resources** para cerrar `PDDocument` y los streams;
- usar PDFBox 3.x de forma coherente y evitar mezclar ejemplos de APIs 2.x;
- utilizar `Loader.loadPDF()` para abrir documentos existentes;
- no asumir que el orden interno del texto coincide exactamente con su disposición visual;
- recordar que PDFBox **no realiza OCR**;
- usar fuentes incrustadas cuando se necesiten caracteres que no cubren las fuentes estándar;
- tratar con precaución PDFs procedentes de fuentes externas;
- no acceder al mismo `PDDocument` simultáneamente desde varios hilos.

---

## 13. Ejercicio propuesto

Cree una aplicación Java que genere un archivo `informe.pdf` con:

1. una página;
2. el título **"Informe de estudiantes"**;
3. tres nombres de estudiantes;
4. una imagen o logotipo;
5. metadatos con el nombre del autor.

Después, cree un segundo programa que:

1. abra el PDF generado;
2. muestre su texto por consola;
3. muestre el autor y el título;
4. indique el número de páginas.

Como ampliación, genere dos informes distintos y únalos en un único PDF.

---

## Autoevaluación

Seleccione una única respuesta correcta.

1. **¿Qué clase representa un documento PDF en PDFBox?**
   - a) `PDFFile`
   - b) `PDDocument`
   - c) `PDFStream`
   - d) `DocumentBox`

2. **¿Qué clase se utiliza para escribir contenido en una página?**
   - a) `PDPageContentStream`
   - b) `PDFTextStripper`
   - c) `PDDocumentInformation`
   - d) `PDFMergerUtility`

3. **En PDFBox 3, ¿cómo se abre normalmente un PDF existente?**
   - a) `PDDocument.load(...)`
   - b) `Loader.loadPDF(...)`
   - c) `PDFReader.open(...)`
   - d) `FileReader.read(...)`

4. **¿Qué clase permite extraer texto de un PDF?**
   - a) `PDFTextStripper`
   - b) `PDImageXObject`
   - c) `PDPage`
   - d) `Standard14Fonts`

5. **¿Puede `PDFTextStripper` reconocer automáticamente el texto de una página escaneada como imagen?**
   - a) Sí, siempre.
   - b) Sí, si el PDF tiene una sola página.
   - c) No; para eso es necesario OCR.
   - d) Solo si la imagen es PNG.

6. **¿Qué clase se utiliza para insertar una imagen en un PDF?**
   - a) `PDImageXObject`
   - b) `BufferedImageWriter`
   - c) `PDFTextStripper`
   - d) `PDDocumentInformation`

7. **¿Qué índice corresponde a la primera página en `document.getPage(...)`?**
   - a) 1
   - b) 0
   - c) -1
   - d) Depende del PDF.

8. **¿Qué utilidad proporciona PDFBox para combinar varios archivos PDF?**
   - a) `PDFMergerUtility`
   - b) `PDFJoinStream`
   - c) `PDDocumentInformation`
   - d) `PDFTextStripper`

9. **¿Por qué puede ser difícil sustituir directamente una palabra existente dentro de un PDF?**
   - a) Porque un PDF nunca contiene texto.
   - b) Porque el contenido puede estar almacenado como instrucciones de dibujo y posicionamiento.
   - c) Porque PDFBox solo permite crear PDFs nuevos.
   - d) Porque todos los PDFs están cifrados.

10. **¿Qué mecanismo es recomendable para cerrar automáticamente un `PDDocument`?**
    - a) `finally-only`
    - b) `try-with-resources`
    - c) `System.gc()`
    - d) `Thread.close()`

### Soluciones

1. **b** · 2. **a** · 3. **b** · 4. **a** · 5. **c** · 6. **a** · 7. **b** · 8. **a** · 9. **b** · 10. **b**
