
### Índice

#### 1. **Introducción a los Documentos PDF**
   - 1.1. Qué es un PDF y su importancia
   - 1.2. Estructura interna de un archivo PDF
   - 1.3. Principales versiones y compatibilidad en PDF

#### 2. **Configuración de Apache PDFBox**
   - 2.1. Instalación de Apache PDFBox
   - 2.2. Integración en un proyecto Java (Maven y Gradle)
   - 2.3. Configuración inicial y estructura básica de un proyecto

#### 3. **Conceptos Básicos de los Elementos en un Documento PDF**
   - 3.1. Páginas y coordenadas en un documento PDF
   - 3.2. Tipos de fuentes y estilos de texto
   - 3.3. Imágenes y gráficos
   - 3.4. Anotaciones y marcadores
   - 3.5. Formularios y campos interactivos

#### 4. **Operaciones CRUD en un Documento PDF**
   - 4.1. Crear un documento PDF nuevo
      - 4.1.1. Crear una página
      - 4.1.2. Añadir texto y personalizar estilos
      - 4.1.3. Insertar imágenes y gráficos
   - 4.2. Leer contenido de un documento PDF
      - 4.2.1. Extracción de texto
      - 4.2.2. Extracción de imágenes
      - 4.2.3. Leer propiedades y metadatos
   - 4.3. Actualizar contenido de un documento PDF
      - 4.3.1. Modificar texto existente
      - 4.3.2. Añadir nuevas páginas y reorganizarlas
      - 4.3.3. Actualizar anotaciones y campos de formulario
   - 4.4. Eliminar elementos en un documento PDF
      - 4.4.1. Borrar texto y gráficos
      - 4.4.2. Eliminar páginas completas
      - 4.4.3. Remover anotaciones y campos de formulario

#### 5. **Manejo de Fuentes y Estilos de Texto**
   - 5.1. Tipos de fuentes soportadas en PDFBox
   - 5.2. Añadir y personalizar fuentes
   - 5.3. Estilos de texto (negrita, cursiva, tamaño, color)
   - 5.4. Ajuste y alineación del texto

#### 6. **Inserción y Manipulación de Imágenes**
   - 6.1. Formatos de imagen soportados (JPG, PNG, etc.)
   - 6.2. Inserción de imágenes en una página
   - 6.3. Redimensionamiento y posicionamiento de imágenes

#### 7. **Trabajar con Gráficos y Figuras**
   - 7.1. Dibujar líneas y formas básicas (rectángulos, círculos)
   - 7.2. Configuración de color y estilos de línea
   - 7.3. Gráficos más avanzados (combinación de formas)

#### 8. **Anotaciones y Marcadores en Documentos PDF**
   - 8.1. Tipos de anotaciones (comentarios, resaltados)
   - 8.2. Crear y gestionar anotaciones
   - 8.3. Añadir y personalizar marcadores de navegación

#### 9. **Creación y Gestión de Formularios en PDF**
   - 9.1. Tipos de campos de formulario (texto, casillas, listas)
   - 9.2. Creación de un formulario interactivo
   - 9.3. Leer y actualizar campos de formulario
   - 9.4. Validaciones y procesamiento de formularios

#### 10. **Metadatos y Propiedades del Documento**
   - 10.1. Visualización de los metadatos (autor, título, fecha de creación)
   - 10.2. Añadir y actualizar propiedades del documento
   - 10.3. Uso de propiedades avanzadas (seguridad, permisos)

#### 11. **Optimización y Compresión de Documentos PDF**
   - 11.1. Métodos de compresión de imágenes y texto
   - 11.2. Reducción de tamaño de archivos
   - 11.3. Optimización de documentos para la web (PDF optimizado para Web)

#### 12. **Ejemplos Prácticos y Casos de Uso**
   - 12.1. Creación de un informe con tablas y gráficos
   - 12.2. Generación de un formulario de inscripción interactivo
   - 12.3. Compilación de documentos múltiples en un solo PDF
   - 12.4. Extraer y analizar datos de documentos PDF existentes





---

### 1. **Introducción a los Documentos PDF**

#### 1.1. Qué es un PDF y su Importancia

El formato PDF (*Portable Document Format*) es un tipo de archivo ampliamente utilizado para representar documentos de manera consistente en cualquier dispositivo o sistema operativo. Su principal ventaja es la preservación de la estructura y el contenido, manteniendo el formato original sin importar dónde se visualice.

Los documentos PDF son fundamentales en aplicaciones empresariales y educativas por sus características de portabilidad, seguridad, y soporte para diversos tipos de contenido, como texto, imágenes, formularios interactivos y anotaciones. Desde informes y facturas hasta formularios interactivos y manuales técnicos, el PDF permite una representación rica y fiel de la información.

#### 1.2. Estructura Interna de un Archivo PDF

Un documento PDF se compone de diferentes tipos de objetos, entre ellos:

- **Catálogo (Catalog)**: Es el punto de entrada para el documento. El catálogo contiene referencias a otros objetos principales del PDF, como páginas y marcadores.
- **Páginas (Pages)**: Una estructura que organiza y gestiona el contenido visual del documento, cada página incluye textos, gráficos e imágenes.
- **Objetos de Contenido (Content Objects)**: Son los componentes que contienen el contenido visual de una página, incluyendo:
  - **Texto**: Incluye configuraciones de fuente y estilo.
  - **Gráficos**: Incluye formas básicas y vectores.
  - **Imágenes**: Permite la inclusión de diferentes tipos de archivos gráficos.
- **Anotaciones (Annotations)**: Permiten agregar comentarios, resaltados y otras interacciones.
- **Propiedades y Metadatos (Properties and Metadata)**: Información como el autor, la fecha de creación y las palabras clave.
- **Fuentes (Fonts)**: Los tipos de letra utilizados dentro del documento, que pueden incluirse como parte del archivo PDF o referirse externamente.

#### 1.3. Principales Versiones y Compatibilidad en PDF

Desde la creación del PDF por Adobe en 1993, el formato ha evolucionado significativamente:
- **PDF 1.0 a PDF 1.7**: Las versiones iniciales introdujeron soporte para contenido gráfico, multimedia y etiquetas semánticas, alcanzando su forma más completa con PDF 1.7.
- **PDF 2.0 (ISO 32000-2)**: La especificación PDF 2.0, lanzada en 2017, introdujo mejoras en seguridad, en la representación de metadatos y compatibilidad avanzada para accesibilidad y archivos de gran tamaño.

---

### 2. **Configuración de Apache PDFBox**

#### 2.1. Instalación de Apache PDFBox

Apache PDFBox es una biblioteca de código abierto mantenida por Apache Software Foundation, ideal para trabajar con documentos PDF en Java. Para instalarla, siga estos pasos:

1. Descargue la biblioteca desde el sitio oficial de Apache PDFBox o agregue la dependencia a su proyecto Maven o Gradle.

#### 2.2. Integración en un Proyecto Java (Maven y Gradle)

**Para proyectos Maven**, agregue la dependencia de PDFBox en el archivo `pom.xml`:

```xml
<dependency>
    <groupId>org.apache.pdfbox</groupId>
    <artifactId>pdfbox</artifactId>
    <version>2.0.29</version>
</dependency>
```

**Para proyectos Gradle**, puede añadir la dependencia en el archivo `build.gradle`:

```groovy
implementation 'org.apache.pdfbox:pdfbox:2.0.29'
```

#### 2.3. Configuración Inicial y Estructura Básica de un Proyecto

Una vez instalada la dependencia, cree una clase principal en Java para empezar a trabajar con archivos PDF. Se recomienda crear una estructura básica en la que se puedan probar diferentes funcionalidades de PDFBox, tales como crear, leer y manipular documentos.

Por ejemplo, una clase `PDFBoxExample`:

```java
import org.apache.pdfbox.pdmodel.PDDocument;
import java.io.IOException;

public class PDFBoxExample {
    public static void main(String[] args) {
        // Punto de partida para pruebas con PDFBox
        try (PDDocument document = new PDDocument()) {
            document.save("ejemploBasico.pdf");
            System.out.println("Documento PDF creado con éxito.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

Este código inicial crea un documento PDF vacío llamado `ejemploBasico.pdf`. A partir de esta clase, se pueden añadir elementos, como texto e imágenes, o extraer contenido de un PDF existente.

---

### 3. **Conceptos Básicos de los Elementos en un Documento PDF**

#### 3.1. Páginas y Coordenadas en un Documento PDF

- **Páginas**: En PDFBox, una página es una instancia de la clase `PDPage`. Al crear un documento PDF, se pueden agregar múltiples páginas y especificar sus dimensiones.
- **Sistema de Coordenadas**: El origen de coordenadas (0,0) está en la esquina inferior izquierda de la página. La posición `x` aumenta hacia la derecha y la posición `y` hacia arriba. Esto es importante para posicionar texto e imágenes de manera precisa.

Ejemplo básico de añadir una página:

```java
PDPage page = new PDPage();
document.addPage(page);
```

#### 3.2. Tipos de Fuentes y Estilos de Texto

PDFBox permite utilizar fuentes internas básicas (`PDType1Font`), como `HELVETICA` y `TIMES_ROMAN`, o cargar fuentes externas desde archivos. Las fuentes se aplican mediante un `PDPageContentStream` para definir el estilo y tamaño de texto.

Ejemplo de texto con fuente Helvetica y tamaño 12:

```java
PDPageContentStream contentStream = new PDPageContentStream(document, page);
contentStream.setFont(PDType1Font.HELVETICA, 12);
contentStream.beginText();
contentStream.newLineAtOffset(100, 700);
contentStream.showText("Texto de ejemplo en Helvetica");
contentStream.endText();
contentStream.close();
```

#### 3.3. Imágenes y Gráficos

PDFBox soporta la inclusión de imágenes en varios formatos, como JPEG y PNG. Las imágenes se insertan utilizando la clase `PDImageXObject`.

Ejemplo de inserción de una imagen:

```java
PDImageXObject image = PDImageXObject.createFromFile("ruta/a/imagen.jpg", document);
contentStream.drawImage(image, 100, 600, 200, 150); // Posición y tamaño de la imagen
```

#### 3.4. Anotaciones y Marcadores

- **Anotaciones**: Son objetos que se añaden a una página para resaltar o comentar secciones del contenido. PDFBox soporta anotaciones como resaltados, notas, y enlaces.
- **Marcadores**: Los marcadores de PDF permiten una navegación jerárquica por el contenido del documento.

Ejemplo de anotación de texto:

```java
PDAnnotationTextMarkup markup = new PDAnnotationTextMarkup(PDAnnotationTextMarkup.SUB_TYPE_HIGHLIGHT);
markup.setRectangle(new PDRectangle(100, 600, 200, 20));
markup.setContents("Este es un texto resaltado");
page.getAnnotations().add(markup);
```

#### 3.5. Formularios y Campos Interactivos

PDFBox permite la creación y gestión de formularios PDF interactivos. Los formularios pueden contener campos de texto, casillas de verificación, listas desplegables, entre otros. Estos campos interactivos son útiles para crear documentos que los usuarios puedan completar.

Ejemplo básico de campo de formulario (campo de texto):

```java
PDAcroForm acroForm = new PDAcroForm(document);
PDTextField textField = new PDTextField(acroForm);
textField.setPartialName("nombreCampo");
textField.setDefaultAppearance("/Helv 12 Tf 0 g"); // Fuente y tamaño
acroForm.getFields().add(textField);
page.getAnnotations().add(textField.getWidget());
```

Aquí se desarrolla el punto 4 del tutorial, que aborda las operaciones CRUD (Crear, Leer, Actualizar y Eliminar) en un documento PDF usando **Apache PDFBox**.

---

### 4. **Operaciones CRUD en un Documento PDF**

#### 4.1. Crear un Documento PDF Nuevo

##### 4.1.1. Crear una Página
En PDFBox, cada documento puede tener una o varias páginas. Para crear un PDF y añadir páginas, primero se instancia el documento y luego se añaden las páginas una a una.

Ejemplo de cómo crear un PDF con una página:

```java
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

public class CreatePDFExample {
    public static void main(String[] args) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            document.save("DocumentoConPagina.pdf");
            System.out.println("Documento PDF creado con éxito.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

Este código crea un archivo PDF con una página en blanco.

##### 4.1.2. Añadir Texto y Personalizar Estilos
Para añadir texto, se utiliza `PDPageContentStream`, que permite definir la fuente, el tamaño y la posición del texto en la página. A continuación, se muestra un ejemplo que crea un documento con texto personalizado.

```java
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

try (PDDocument document = new PDDocument()) {
    PDPage page = new PDPage();
    document.addPage(page);

    PDPageContentStream contentStream = new PDPageContentStream(document, page);
    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
    contentStream.beginText();
    contentStream.newLineAtOffset(100, 700);
    contentStream.showText("Texto de ejemplo en PDFBox");
    contentStream.endText();
    contentStream.close();

    document.save("DocumentoConTexto.pdf");
} catch (Exception e) {
    e.printStackTrace();
}
```

##### 4.1.3. Insertar Imágenes y Gráficos
Para insertar una imagen en PDFBox, primero se crea una instancia de `PDImageXObject`, que luego se dibuja en una página mediante `PDPageContentStream`.

Ejemplo de inserción de una imagen:

```java
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

try (PDDocument document = new PDDocument()) {
    PDPage page = new PDPage();
    document.addPage(page);

    PDPageContentStream contentStream = new PDPageContentStream(document, page);
    PDImageXObject image = PDImageXObject.createFromFile("imagen.jpg", document);
    contentStream.drawImage(image, 100, 500, 200, 150); // Posición y tamaño
    contentStream.close();

    document.save("DocumentoConImagen.pdf");
} catch (Exception e) {
    e.printStackTrace();
}
```

---

#### 4.2. Leer Contenido de un Documento PDF

PDFBox permite extraer texto e imágenes de un PDF, así como leer metadatos y otras propiedades.

##### 4.2.1. Extracción de Texto
Para extraer texto de un PDF, se utiliza la clase `PDFTextStripper`.

```java
import org.apache.pdfbox.text.PDFTextStripper;

try (PDDocument document = PDDocument.load(new File("DocumentoConTexto.pdf"))) {
    PDFTextStripper textStripper = new PDFTextStripper();
    String texto = textStripper.getText(document);
    System.out.println("Texto extraído:\n" + texto);
} catch (Exception e) {
    e.printStackTrace();
}
```

##### 4.2.2. Extracción de Imágenes
PDFBox permite extraer imágenes de un PDF usando `PDResources` y `PDImageXObject`, aunque el proceso es algo más avanzado que la extracción de texto. A continuación, se muestra un ejemplo básico:

```java
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

for (PDPage page : document.getPages()) {
    PDResources resources = page.getResources();
    for (COSName name : resources.getXObjectNames()) {
        if (resources.isImageXObject(name)) {
            PDImageXObject image = (PDImageXObject) resources.getXObject(name);
            ImageIO.write(image.getImage(), "jpg", new File("imagenExtraida.jpg"));
        }
    }
}
```

##### 4.2.3. Leer Propiedades y Metadatos
Los metadatos de un PDF incluyen información como el título, el autor y las palabras clave. PDFBox permite acceder a ellos fácilmente.

```java
PDDocumentInformation info = document.getDocumentInformation();
System.out.println("Título: " + info.getTitle());
System.out.println("Autor: " + info.getAuthor());
```

---

#### 4.3. Actualizar Contenido de un Documento PDF

Actualizar un documento implica modificar su contenido existente, ya sea texto, imágenes o propiedades.

##### 4.3.1. Modificar Texto Existente
Modificar texto existente en un PDF es complejo porque PDFBox no permite la edición directa de texto una vez renderizado. Sin embargo, se pueden agregar textos adicionales en posiciones específicas para simular una actualización.

```java
try (PDDocument document = PDDocument.load(new File("DocumentoConTexto.pdf"))) {
    PDPage page = document.getPage(0);
    PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
    contentStream.setFont(PDType1Font.HELVETICA, 12);
    contentStream.beginText();
    contentStream.newLineAtOffset(100, 650);
    contentStream.showText("Texto adicional en el PDF.");
    contentStream.endText();
    contentStream.close();

    document.save("DocumentoActualizado.pdf");
} catch (Exception e) {
    e.printStackTrace();
}
```

##### 4.3.2. Añadir Nuevas Páginas y Reorganizarlas
Para añadir nuevas páginas, simplemente se instancian y se añaden al documento. Además, las páginas pueden reorganizarse utilizando métodos de la clase `PDDocument`.

```java
PDPage nuevaPagina = new PDPage();
document.addPage(nuevaPagina); // Añade una nueva página al final

// Para reorganizar páginas, se puede utilizar document.getPages().insertAfter o insertBefore
PDPage otraPagina = document.getPage(1);
document.getPages().insertAfter(nuevaPagina, otraPagina); // Inserta nuevaPagina después de otraPagina
```

##### 4.3.3. Actualizar Anotaciones y Campos de Formulario
Si un PDF contiene anotaciones o formularios interactivos, estos elementos pueden ser modificados utilizando `PDAnnotation` y `PDAcroForm`.

Ejemplo de actualización de anotaciones:

```java
List<PDAnnotation> annotations = page.getAnnotations();
for (PDAnnotation annotation : annotations) {
    annotation.setContents("Comentario actualizado");
}
```

---

#### 4.4. Eliminar Elementos en un Documento PDF

##### 4.4.1. Borrar Texto y Gráficos
PDFBox no permite borrar directamente el texto o los gráficos de una página ya renderizada, pero se puede crear una nueva página en blanco en la misma posición y agregar solo el contenido que se desea conservar.

##### 4.4.2. Eliminar Páginas Completas
PDFBox permite eliminar páginas enteras de un documento mediante `PDDocument#removePage`.

```java
try (PDDocument document = PDDocument.load(new File("DocumentoConVariasPaginas.pdf"))) {
    document.removePage(0); // Elimina la primera página
    document.save("DocumentoSinPagina.pdf");
} catch (Exception e) {
    e.printStackTrace();
}
```

##### 4.4.3. Remover Anotaciones y Campos de Formulario
Para eliminar anotaciones o campos de formulario de una página específica, acceda a su lista de anotaciones y remuévalas individualmente.

```java
PDPage page = document.getPage(0);
List<PDAnnotation> annotations = page.getAnnotations();
annotations.clear(); // Elimina todas las anotaciones de la página
```

---


### 5. **Manejo de Fuentes y Estilos de Texto**

Apache PDFBox permite trabajar con diferentes fuentes y estilos para el texto en documentos PDF. Es importante saber cómo aplicarlas para mejorar la presentación y legibilidad del contenido en el documento.

#### 5.1. Tipos de Fuentes Soportadas en PDFBox

PDFBox admite varios tipos de fuentes:

- **Fuentes estándar (PDType1Font)**: Como Helvetica, Times Roman, Courier, etc.
- **Fuentes TrueType**: Fuentes que se pueden cargar desde archivos externos en formato `.ttf`.
- **Fuentes embebidas**: Las fuentes pueden incrustarse en el PDF para garantizar la consistencia en la visualización, incluso si el sistema del usuario no las tiene instaladas.

#### 5.2. Añadir y Personalizar Fuentes

Para añadir texto con una fuente estándar, basta con especificar el tipo de fuente en `PDPageContentStream`.

Ejemplo con la fuente estándar Helvetica:

```java
PDPageContentStream contentStream = new PDPageContentStream(document, page);
contentStream.setFont(PDType1Font.HELVETICA, 12);
contentStream.beginText();
contentStream.newLineAtOffset(100, 700);
contentStream.showText("Texto en Helvetica");
contentStream.endText();
contentStream.close();
```

Para añadir una fuente TrueType desde un archivo externo:

```java
import org.apache.pdfbox.pdmodel.font.PDType0Font;

Font font = PDType0Font.load(document, new File("ruta/a/fuente.ttf"));
contentStream.setFont(font, 12);
```

#### 5.3. Estilos de Texto (negrita, cursiva, tamaño, color)

Se pueden aplicar estilos de texto usando distintas variantes de fuentes (negrita, cursiva) y configurando el tamaño y color:

```java
contentStream.setNonStrokingColor(0, 102, 204); // Color RGB azul
contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14); // Negrita
contentStream.beginText();
contentStream.newLineAtOffset(100, 680);
contentStream.showText("Texto en negrita y azul");
contentStream.endText();
contentStream.setNonStrokingColor(0, 0, 0); // Restablece el color a negro
```

#### 5.4. Ajuste y Alineación del Texto

Para ajustar la alineación, se debe calcular la posición `x` de inicio en función del ancho de la página y del texto:

```java
float pageWidth = page.getMediaBox().getWidth();
float textWidth = font.getStringWidth("Texto centrado") / 1000 * 12;
float startX = (pageWidth - textWidth) / 2; // Centrado horizontalmente

contentStream.newLineAtOffset(startX, 660);
contentStream.showText("Texto centrado");
```

---

### 6. **Inserción y Manipulación de Imágenes**

PDFBox soporta imágenes en formatos comunes, como JPEG y PNG, lo cual permite enriquecer los documentos visualmente.

#### 6.1. Formatos de Imagen Soportados

PDFBox admite imágenes en formatos:
- **JPEG**: Imágenes comprimidas, ideales para fotografías.
- **PNG**: Imágenes con transparencia.

#### 6.2. Inserción de Imágenes en una Página

Para añadir una imagen, se carga como `PDImageXObject` y se dibuja en la página usando `drawImage`.

```java
PDImageXObject image = PDImageXObject.createFromFile("ruta/a/imagen.jpg", document);
contentStream.drawImage(image, 100, 600, 200, 150); // Posición y tamaño
```

#### 6.3. Redimensionamiento y Posicionamiento de Imágenes

El tamaño de la imagen se puede ajustar especificando el ancho y el alto en el método `drawImage`. La posición `(x, y)` determina el lugar en la página donde se dibujará.

```java
contentStream.drawImage(image, 150, 500, 300, 200); // Ajuste del tamaño de la imagen
```

---

### 7. **Trabajar con Gráficos y Figuras**

Apache PDFBox permite la creación de gráficos sencillos y figuras geométricas, útiles para diseñar formas básicas o resaltar secciones en el PDF.

#### 7.1. Dibujar Líneas y Formas Básicas (rectángulos, círculos)

Para dibujar formas, como líneas y rectángulos, PDFBox proporciona métodos específicos en `PDPageContentStream`:

```java
contentStream.setStrokingColor(0, 0, 255); // Línea azul
contentStream.moveTo(100, 500);
contentStream.lineTo(300, 500);
contentStream.stroke(); // Dibuja la línea
```

Para dibujar un rectángulo:

```java
contentStream.addRect(100, 450, 200, 100); // Posición (x, y), ancho, alto
contentStream.setNonStrokingColor(200, 200, 255); // Color de relleno
contentStream.fill(); // Dibuja el rectángulo
```

#### 7.2. Configuración de Color y Estilos de Línea

El color y el grosor de las líneas se configuran mediante `setStrokingColor` y `setLineWidth`:

```java
contentStream.setStrokingColor(255, 0, 0); // Rojo
contentStream.setLineWidth(2); // Grosor de la línea
contentStream.moveTo(150, 400);
contentStream.lineTo(350, 400);
contentStream.stroke();
```

#### 7.3. Gráficos Más Avanzados (combinación de formas)

Se pueden combinar múltiples formas para crear gráficos más complejos, como cuadros y diagramas:

```java
// Cuadro con varios rectángulos
contentStream.addRect(100, 300, 100, 100);
contentStream.setNonStrokingColor(150, 150, 255);
contentStream.fill();

contentStream.addRect(250, 300, 100, 100);
contentStream.setNonStrokingColor(100, 200, 100);
contentStream.fill();
```

---

### 8. **Anotaciones y Marcadores en Documentos PDF**

PDFBox permite agregar anotaciones y marcadores de navegación en un documento, lo cual es útil para agregar notas o comentarios y facilitar la navegación en documentos extensos.

#### 8.1. Tipos de Anotaciones (comentarios, resaltados)

Las anotaciones permiten añadir comentarios y resaltados a las secciones del texto.

Ejemplo de anotación de comentario en una página:

```java
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationText;

PDAnnotationText textAnnotation = new PDAnnotationText();
textAnnotation.setContents("Este es un comentario.");
textAnnotation.setRectangle(new PDRectangle(100, 500, 50, 50));
page.getAnnotations().add(textAnnotation);
```

#### 8.2. Crear y Gestionar Anotaciones

PDFBox ofrece diversos tipos de anotaciones, como subrayados, resaltados y cuadros de texto. Para crear una anotación de resaltado, por ejemplo:

```java
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationTextMarkup;

PDAnnotationTextMarkup highlightAnnotation = new PDAnnotationTextMarkup(PDAnnotationTextMarkup.SUB_TYPE_HIGHLIGHT);
highlightAnnotation.setRectangle(new PDRectangle(100, 600, 200, 20));
highlightAnnotation.setContents("Texto resaltado.");
highlightAnnotation.setColor(new PDColor(new float[]{1, 1, 0}, PDDeviceRGB.INSTANCE)); // Amarillo
page.getAnnotations().add(highlightAnnotation);
```

#### 8.3. Añadir y Personalizar Marcadores de Navegación

Los marcadores facilitan la navegación en documentos PDF largos. Para agregar un marcador:

```java
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;

PDDocumentOutline outline = new PDDocumentOutline();
document.getDocumentCatalog().setDocumentOutline(outline);

PDOutlineItem firstPage = new PDOutlineItem();
firstPage.setTitle("Página 1");
firstPage.setDestination(page);
outline.addLast(firstPage);
outline.openNode(); // Hace visible el panel de marcadores
```

Los marcadores se pueden organizar en jerarquías, lo cual es útil para estructurar secciones o capítulos dentro del PDF.

---

A continuación se desarrolla la sección 9 del tutorial, que aborda la creación y gestión de formularios interactivos en documentos PDF utilizando **Apache PDFBox**.

---

### 9. **Creación y Gestión de Formularios en PDF**

Apache PDFBox permite crear formularios interactivos en los que el usuario puede ingresar información. Los formularios PDF son muy útiles para aplicaciones que requieren datos de entrada, como formularios de registro, encuestas, y formularios de retroalimentación.

#### 9.1. Tipos de Campos de Formulario

Los formularios PDF pueden incluir varios tipos de campos interactivos. Los principales tipos de campos soportados en PDFBox son:

- **Campos de Texto** (`PDTextField`): Permiten al usuario introducir texto.
- **Casillas de Verificación** (`PDCheckBox`): Son opciones que el usuario puede seleccionar o deseleccionar.
- **Botones de Radio** (`PDRadioButton`) y Listas Desplegables (`PDComboBox`): Permiten la selección de una opción de una lista.
- **Botones de Envío** (`PDPushButton`): Se pueden usar para crear botones de acción, aunque su funcionalidad es limitada en PDFBox.

#### 9.2. Creación de un Formulario Interactivo

Para crear un formulario interactivo en un documento PDF, primero se crea un formulario (`PDAcroForm`) y luego se añaden los campos necesarios. A continuación se muestra cómo hacerlo:

```java
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;

public class PDFFormExample {
    public static void main(String[] args) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // Crear el formulario
            PDAcroForm acroForm = new PDAcroForm(document);
            document.getDocumentCatalog().setAcroForm(acroForm);

            // Crear un campo de texto
            PDTextField textField = new PDTextField(acroForm);
            textField.setPartialName("nombre");
            textField.setDefaultAppearance("/Helv 12 Tf 0 g"); // Fuente y tamaño de texto
            acroForm.getFields().add(textField);

            // Posición y apariencia del campo de texto
            PDAnnotationWidget widget = textField.getWidgets().get(0);
            widget.setRectangle(new PDRectangle(100, 600, 200, 20)); // Posición (x, y) y tamaño
            page.getAnnotations().add(widget);

            // Crear una casilla de verificación
            PDCheckBox checkBox = new PDCheckBox(acroForm);
            checkBox.setPartialName("aceptar");
            checkBox.setValue("Off"); // Valor por defecto
            acroForm.getFields().add(checkBox);

            // Posición y apariencia de la casilla de verificación
            PDAnnotationWidget checkBoxWidget = checkBox.getWidgets().get(0);
            checkBoxWidget.setRectangle(new PDRectangle(100, 550, 20, 20));
            page.getAnnotations().add(checkBoxWidget);

            document.save("FormularioInteractivo.pdf");
            System.out.println("Formulario PDF creado con éxito.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

Este código crea un formulario PDF con un campo de texto y una casilla de verificación. El usuario puede ingresar texto en el campo y marcar o desmarcar la casilla.

#### 9.3. Leer y Actualizar Campos de Formulario

Una vez que se tiene un formulario en un PDF, PDFBox permite leer y actualizar los valores de los campos, lo cual es útil para obtener datos de entrada del usuario o prellenar el formulario antes de distribuirlo.

##### Lectura de Campos de Formulario

Para leer el valor de un campo específico, se obtiene el campo de `PDAcroForm` usando su nombre parcial (`PartialName`) y se llama al método `getValue`.

```java
try (PDDocument document = PDDocument.load(new File("FormularioInteractivo.pdf"))) {
    PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
    PDTextField nombreField = (PDTextField) acroForm.getField("nombre");
    String nombre = nombreField.getValue();
    System.out.println("Nombre ingresado: " + nombre);

    PDCheckBox aceptarCheckBox = (PDCheckBox) acroForm.getField("aceptar");
    String estadoCasilla = aceptarCheckBox.getValue();
    System.out.println("Casilla de verificación: " + estadoCasilla);
} catch (Exception e) {
    e.printStackTrace();
}
```

##### Actualización de Campos de Formulario

Para actualizar un campo, se obtiene el campo por su nombre y se llama a `setValue` para establecer el nuevo valor.

```java
try (PDDocument document = PDDocument.load(new File("FormularioInteractivo.pdf"))) {
    PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
    PDTextField nombreField = (PDTextField) acroForm.getField("nombre");
    nombreField.setValue("Juan Pérez");

    PDCheckBox aceptarCheckBox = (PDCheckBox) acroForm.getField("aceptar");
    aceptarCheckBox.check(); // Activa la casilla

    document.save("FormularioActualizado.pdf");
} catch (Exception e) {
    e.printStackTrace();
}
```

Este código establece un valor en el campo de texto `nombre` y marca la casilla de verificación `aceptar`.

#### 9.4. Validaciones y Procesamiento de Formularios

Aunque PDFBox no incluye validaciones complejas, se pueden implementar en el código Java. Las validaciones comunes incluyen verificar que un campo de texto no esté vacío o que una casilla esté marcada antes de continuar con el procesamiento.

Ejemplo básico de validación de campos:

```java
if (nombreField.getValue() == null || nombreField.getValue().isEmpty()) {
    System.out.println("El campo 'nombre' es obligatorio.");
} else {
    System.out.println("Valor del campo 'nombre': " + nombreField.getValue());
}

if (!aceptarCheckBox.isChecked()) {
    System.out.println("Debe aceptar las condiciones.");
}
```

Además, al procesar formularios, es posible exportar los valores de los campos o generar un nuevo PDF basado en los datos proporcionados por el usuario.

---

A continuación se desarrollan los puntos 10 y 11 del tutorial, abordando los metadatos y la optimización de documentos PDF en Apache PDFBox.

---

### 10. **Metadatos y Propiedades del Documento**

Los metadatos en un documento PDF son información adicional que describe el contenido del archivo. Incluir metadatos es importante porque permite que el documento sea más fácilmente encontrado, identificado y organizado.

#### 10.1. Visualización de los Metadatos (autor, título, fecha de creación)

Apache PDFBox permite acceder y visualizar los metadatos de un documento, como el título, el autor y la fecha de creación. Esto se logra mediante la clase `PDDocumentInformation`.

Ejemplo de cómo obtener los metadatos de un documento PDF:

```java
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

import java.io.File;

public class ViewMetadata {
    public static void main(String[] args) {
        try (PDDocument document = PDDocument.load(new File("documento.pdf"))) {
            PDDocumentInformation info = document.getDocumentInformation();
            System.out.println("Título: " + info.getTitle());
            System.out.println("Autor: " + info.getAuthor());
            System.out.println("Asunto: " + info.getSubject());
            System.out.println("Palabras clave: " + info.getKeywords());
            System.out.println("Fecha de creación: " + info.getCreationDate());
            System.out.println("Fecha de modificación: " + info.getModificationDate());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 10.2. Añadir y Actualizar Propiedades del Documento

Para añadir o actualizar metadatos en un documento PDF, se puede crear una instancia de `PDDocumentInformation`, establecer los valores deseados y asignarla al documento.

Ejemplo de cómo añadir o actualizar metadatos:

```java
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

import java.util.Calendar;

public class UpdateMetadata {
    public static void main(String[] args) {
        try (PDDocument document = new PDDocument()) {
            PDDocumentInformation info = document.getDocumentInformation();
            info.setTitle("Manual de Usuario");
            info.setAuthor("Juan Pérez");
            info.setSubject("Instrucciones de Uso");
            info.setKeywords("manual, usuario, guía, PDFBox");
            info.setCreationDate(Calendar.getInstance());

            document.save("DocumentoConMetadatos.pdf");
            System.out.println("Metadatos actualizados correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 10.3. Uso de Propiedades Avanzadas (seguridad, permisos)

Además de los metadatos básicos, PDFBox permite añadir configuraciones de seguridad, como la encriptación de documentos o la restricción de permisos.

Ejemplo de cómo proteger un documento PDF con contraseña:

```java
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

try (PDDocument document = PDDocument.load(new File("documento.pdf"))) {
    AccessPermission permissions = new AccessPermission();
    permissions.setCanPrint(false); // Restricción de impresión

    StandardProtectionPolicy protectionPolicy = new StandardProtectionPolicy("password_usuario", "password_propietario", permissions);
    protectionPolicy.setEncryptionKeyLength(128); // Nivel de encriptación

    document.protect(protectionPolicy);
    document.save("DocumentoProtegido.pdf");
    System.out.println("Documento protegido con contraseña.");
} catch (Exception e) {
    e.printStackTrace();
}
```

Este código configura el PDF para que solo los usuarios que ingresen la contraseña puedan acceder a sus contenidos, y también restringe la impresión.

---

### 11. **Optimización y Compresión de Documentos PDF**

La optimización de un documento PDF implica reducir su tamaño de archivo y mejorar su rendimiento, especialmente en términos de visualización y carga. Apache PDFBox ofrece varias técnicas para optimizar y comprimir documentos PDF.

#### 11.1. Métodos de Compresión de Imágenes y Texto

El tamaño de un PDF puede reducirse significativamente mediante la compresión de las imágenes y el contenido de texto. PDFBox permite la compresión de imágenes al momento de añadirlas al PDF. Al usar imágenes en formato JPEG, que ya están comprimidas, se puede lograr una reducción significativa.

Ejemplo de cómo añadir una imagen JPEG comprimida a un PDF:

```java
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;

try (PDDocument document = new PDDocument()) {
    PDPage page = new PDPage();
    document.addPage(page);

    // Crear un objeto de imagen JPEG comprimida
    PDImageXObject image = JPEGFactory.createFromStream(document, new FileInputStream("imagenComprimida.jpg"));
    PDPageContentStream contentStream = new PDPageContentStream(document, page);
    contentStream.drawImage(image, 100, 500, 200, 150);
    contentStream.close();

    document.save("DocumentoConImagenOptimizada.pdf");
} catch (Exception e) {
    e.printStackTrace();
}
```

#### 11.2. Reducción de Tamaño de Archivos

PDFBox no proporciona un método directo para comprimir todo un archivo PDF, pero se pueden aplicar algunas técnicas que ayudan a reducir el tamaño de manera indirecta:

- **Eliminar contenido innecesario**: Remover objetos no utilizados o eliminar páginas innecesarias.
- **Reducir resolución de imágenes**: Convertir las imágenes a una resolución más baja antes de añadirlas al PDF.

#### 11.3. Optimización de Documentos para la Web (PDF optimizado para Web)

Optimizar un PDF para la Web, también conocido como "Fast Web View" o "Linearización", permite que el documento PDF se cargue de forma progresiva en navegadores. Apache PDFBox no tiene soporte nativo para esta optimización, pero una alternativa es utilizar herramientas adicionales (como Ghostscript) en combinación con PDFBox para optimizar archivos para la Web.

---

### 12. Ejemplo práctico

Para este caso práctico, vamos a crear un documento PDF completo utilizando **Apache PDFBox**. El documento tendrá varios párrafos de texto distribuidos en distintas páginas, dos imágenes diferentes y una tabla de datos. Además, aseguraremos que cada elemento esté bien posicionado y organizado en el documento.

---

#### Ejemplo Práctico: Creación de un Documento PDF Completo con Varios Elementos

En este caso, vamos a construir un documento PDF llamado `InformeCompleto.pdf` que contenga:

1. Una portada con un título y una imagen.
2. Un párrafo de introducción en la segunda página.
3. Una tabla de datos en la tercera página.
4. Otro párrafo en la cuarta página, seguido de una segunda imagen.

---

#### Paso 1: Crear el Documento PDF y Agregar Páginas

Para comenzar, importamos las clases de PDFBox y creamos un documento con múltiples páginas.

```java
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.PDRectangle;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import java.io.File;
import java.io.FileInputStream;

public class PDFCompleteDocument {
    public static void main(String[] args) {
        try (PDDocument document = new PDDocument()) {
            // Crear la portada
            PDPage portada = new PDPage();
            document.addPage(portada);
            agregarPortada(document, portada, "Informe Anual 2024", "portada.jpg");

            // Crear página de introducción
            PDPage paginaIntroduccion = new PDPage();
            document.addPage(paginaIntroduccion);
            agregarParrafo(document, paginaIntroduccion, "Introducción",
                "Este documento presenta un análisis exhaustivo del rendimiento anual...");
            
            // Crear página con una tabla de datos
            PDPage paginaTabla = new PDPage();
            document.addPage(paginaTabla);
            agregarTabla(document, paginaTabla);
            
            // Crear página con párrafo y segunda imagen
            PDPage paginaConclusiones = new PDPage();
            document.addPage(paginaConclusiones);
            agregarParrafo(document, paginaConclusiones, "Conclusión",
                "A lo largo del año se observaron importantes avances en todas las áreas...");
            agregarImagen(document, paginaConclusiones, "conclusion.jpg", 100, 350, 300, 200);

            // Guardar el documento
            document.save("InformeCompleto.pdf");
            System.out.println("Documento PDF creado con éxito.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para añadir la portada con título e imagen
    private static void agregarPortada(PDDocument document, PDPage page, String titulo, String imagePath) throws Exception {
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 24);
            contentStream.newLineAtOffset(220, 700);
            contentStream.showText(titulo);
            contentStream.endText();

            PDImageXObject image = PDImageXObject.createFromFile(imagePath, document);
            contentStream.drawImage(image, 150, 400, 300, 250); // Tamaño y posición de la imagen
        }
    }

    // Método para añadir un párrafo en una página
    private static void agregarParrafo(PDDocument document, PDPage page, String titulo, String texto) throws Exception {
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText(titulo);
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(100, 660);

            String[] palabras = texto.split(" ");
            StringBuilder linea = new StringBuilder();
            for (String palabra : palabras) {
                if ((linea.length() + palabra.length()) < 60) {
                    linea.append(palabra).append(" ");
                } else {
                    contentStream.showText(linea.toString().trim());
                    contentStream.newLine();
                    linea = new StringBuilder(palabra).append(" ");
                }
            }
            contentStream.showText(linea.toString().trim());
            contentStream.endText();
        }
    }

    // Método para añadir una tabla en una página
    private static void agregarTabla(PDDocument document, PDPage page) throws Exception {
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("Tabla de Datos Anuales");
            contentStream.endText();

            contentStream.setFont(PDType1Font.HELVETICA, 10);
            String[] headers = {"Mes", "Ventas", "Ingresos", "Gastos"};
            String[][] data = {
                {"Enero", "2000", "15000", "5000"},
                {"Febrero", "2100", "15500", "5200"},
                {"Marzo", "2200", "16000", "5300"}
            };

            float margin = 100;
            float yStart = 650;
            float tableWidth = 400;
            float rowHeight = 20f;
            float yPosition = yStart;

            // Dibuja las cabeceras
            for (String header : headers) {
                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText(header);
                contentStream.endText();
                margin += tableWidth / headers.length;
            }
            yPosition -= rowHeight;

            // Dibuja las filas de la tabla
            margin = 100;
            for (String[] row : data) {
                for (String cell : row) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin, yPosition);
                    contentStream.showText(cell);
                    contentStream.endText();
                    margin += tableWidth / headers.length;
                }
                yPosition -= rowHeight;
                margin = 100;
            }
        }
    }

    // Método para añadir una imagen en una página
    private static void agregarImagen(PDDocument document, PDPage page, String imagePath, int x, int y, int width, int height) throws Exception {
        PDImageXObject image = PDImageXObject.createFromFile(imagePath, document);
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.drawImage(image, x, y, width, height);
        }
    }
}
```

---

#### Explicación del Código

1. **Crear la Portada**:
   - Se crea la primera página (`portada`) y se le añade un título en el centro de la parte superior.
   - A continuación, se inserta una imagen centrada en la página.

2. **Agregar un Párrafo de Introducción**:
   - En la segunda página, se añade un título de sección y un párrafo. Para evitar que las líneas sean demasiado largas, se divide el texto en palabras y se ajusta para que cada línea tenga un límite de caracteres.

3. **Crear una Tabla de Datos**:
   - En la tercera página, se genera una tabla con cabeceras y datos de ejemplo. La tabla se organiza con coordenadas fijas y se establecen posiciones de texto para cada celda.
   - La tabla contiene cabeceras ("Mes", "Ventas", etc.) y tres filas de datos de ejemplo.

4. **Agregar un Párrafo y una Imagen en la Conclusión**:
   - En la última página, se añade un párrafo de conclusión y se coloca una segunda imagen en una posición específica.

---

#### **Diferentes Casos de Uso**

#### 12.1. Creación de un Informe con Tablas y Gráficos

Este caso es común para informes financieros, académicos o de ventas, donde se requiere presentar datos en tablas junto con gráficos. Aquí se crea un PDF con una tabla de datos financieros y un gráfico simple de líneas.

```java
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class InformeConTablaYGrafico {
    public static void main(String[] args) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

                // Título del Informe
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 750);
                contentStream.showText("Informe de Ventas Anual");
                contentStream.endText();

                // Tabla de Datos
                String[] headers = {"Mes", "Ventas", "Ingresos", "Gastos"};
                String[][] data = {
                    {"Enero", "2000", "15000", "5000"},
                    {"Febrero", "2100", "15500", "5200"},
                    {"Marzo", "2200", "16000", "5300"}
                };

                float yPosition = 700;
                float margin = 100;
                float tableWidth = 400;
                float rowHeight = 20f;
                contentStream.setFont(PDType1Font.HELVETICA, 10);

                // Cabecera
                for (String header : headers) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin, yPosition);
                    contentStream.showText(header);
                    contentStream.endText();
                    margin += tableWidth / headers.length;
                }
                yPosition -= rowHeight;

                // Filas
                margin = 100;
                for (String[] row : data) {
                    for (String cell : row) {
                        contentStream.beginText();
                        contentStream.newLineAtOffset(margin, yPosition);
                        contentStream.showText(cell);
                        contentStream.endText();
                        margin += tableWidth / headers.length;
                    }
                    yPosition -= rowHeight;
                    margin = 100;
                }

                // Gráfico (Ejemplo básico de un gráfico de barras)
                float xPos = 100;
                contentStream.setNonStrokingColor(0, 102, 204);
                contentStream.addRect(xPos, yPosition - 30, 20, 100); // Barra 1
                contentStream.fill();

                xPos += 40;
                contentStream.addRect(xPos, yPosition - 30, 20, 120); // Barra 2
                contentStream.fill();

                xPos += 40;
                contentStream.addRect(xPos, yPosition - 30, 20, 140); // Barra 3
                contentStream.fill();
            }
            document.save("InformeConTablaYGrafico.pdf");
            System.out.println("Informe generado exitosamente.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

#### 12.2. Generación de un Formulario de Inscripción Interactivo

Los formularios interactivos son útiles para recopilar información de los usuarios. En este caso, crearemos un formulario de inscripción básico con campos de texto y una casilla de verificación para aceptar términos y condiciones.

```java
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.PDRectangle;

public class FormularioDeInscripcion {
    public static void main(String[] args) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // Crear el formulario
            PDAcroForm acroForm = new PDAcroForm(document);
            document.getDocumentCatalog().setAcroForm(acroForm);

            // Campo de nombre
            PDTextField nombreField = new PDTextField(acroForm);
            nombreField.setPartialName("nombre");
            nombreField.setDefaultAppearance("/Helv 12 Tf 0 g");
            acroForm.getFields().add(nombreField);

            PDAnnotationWidget widgetNombre = nombreField.getWidgets().get(0);
            widgetNombre.setRectangle(new PDRectangle(100, 700, 200, 20));
            page.getAnnotations().add(widgetNombre);

            // Campo de correo
            PDTextField emailField = new PDTextField(acroForm);
            emailField.setPartialName("correo");
            emailField.setDefaultAppearance("/Helv 12 Tf 0 g");
            acroForm.getFields().add(emailField);

            PDAnnotationWidget widgetCorreo = emailField.getWidgets().get(0);
            widgetCorreo.setRectangle(new PDRectangle(100, 650, 200, 20));
            page.getAnnotations().add(widgetCorreo);

            // Casilla de verificación para aceptar términos
            PDCheckBox aceptarTerminos = new PDCheckBox(acroForm);
            aceptarTerminos.setPartialName("aceptarTerminos");
            acroForm.getFields().add(aceptarTerminos);

            PDAnnotationWidget widgetAceptar = aceptarTerminos.getWidgets().get(0);
            widgetAceptar.setRectangle(new PDRectangle(100, 600, 20, 20));
            page.getAnnotations().add(widgetAceptar);

            document.save("FormularioDeInscripcion.pdf");
            System.out.println("Formulario interactivo creado exitosamente.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

#### 12.3. Compilación de Documentos Múltiples en un Solo PDF

Este caso es útil cuando se necesita combinar múltiples documentos PDF en uno solo, como al juntar varios reportes o documentos administrativos en un archivo.

```java
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import java.io.File;

public class CompilarPDFs {
    public static void main(String[] args) {
        try {
            PDFMergerUtility pdfMerger = new PDFMergerUtility();
            pdfMerger.addSource("documento1.pdf");
            pdfMerger.addSource("documento2.pdf");
            pdfMerger.addSource("documento3.pdf");

            pdfMerger.setDestinationFileName("CompilacionDocumentos.pdf");
            pdfMerger.mergeDocuments(null);
            System.out.println("Documentos combinados en CompilacionDocumentos.pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

#### 12.4. Extraer y Analizar Datos de Documentos PDF Existentes

En muchos casos, puede ser necesario extraer datos de formularios o tablas dentro de documentos PDF ya existentes para su análisis. Este ejemplo muestra cómo extraer texto de un PDF, útil para generar reportes de datos recopilados.

```java
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.File;

public class ExtraerTextoPDF {
    public static void main(String[] args) {
        try (PDDocument document = PDDocument.load(new File("documentoFuente.pdf"))) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String texto = pdfStripper.getText(document);
            System.out.println("Texto extraído:");
            System.out.println(texto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

Para analizar los datos, el texto extraído se puede procesar adicionalmente. Por ejemplo, si contiene datos tabulares, se puede dividir en líneas y columnas, y almacenar en una estructura de datos como un array o una tabla para su posterior análisis.

---

A continuación se desarrolla el punto 13 del tutorial, que cubre temas avanzados de seguridad y protección de documentos PDF, como la **encriptación**, el **establecimiento de permisos** y la **firma digital** utilizando **Apache PDFBox**.

---

### 13. **Consideraciones Avanzadas y Mejoras en Seguridad**

El uso de documentos PDF en entornos empresariales, gubernamentales y académicos suele requerir medidas de seguridad para proteger la información sensible y garantizar la autenticidad del documento. Con Apache PDFBox, es posible añadir encriptación, proteger archivos con contraseñas y aplicar firmas digitales. Estas herramientas ayudan a proteger el contenido del PDF contra accesos no autorizados y aseguran que el documento sea genuino.

#### 13.1. Protección Mediante Contraseña y Permisos de Usuario

Con PDFBox, se puede aplicar encriptación de 128 bits y proteger un documento con contraseñas para limitar el acceso y establecer permisos específicos, como la prohibición de impresión, la edición o la copia de contenido. Esto se logra utilizando la clase `StandardProtectionPolicy`.

##### Ejemplo de cómo proteger un documento con contraseña

En este ejemplo, configuraremos una **contraseña de usuario** que restringe el acceso al documento, y una **contraseña de propietario** que permite al propietario realizar cambios y gestionar permisos.

```java
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

import java.io.File;

public class PDFProtection {
    public static void main(String[] args) {
        try (PDDocument document = PDDocument.load(new File("documentoSinProteccion.pdf"))) {
            
            // Establecer permisos de acceso
            AccessPermission permissions = new AccessPermission();
            permissions.setCanPrint(false); // Restringe la impresión del documento
            permissions.setCanModify(false); // Restringe la edición
            permissions.setCanExtractContent(false); // Restringe la extracción de contenido

            // Configurar las contraseñas de usuario y propietario
            String userPassword = "usuario123";
            String ownerPassword = "propietario456";

            StandardProtectionPolicy protectionPolicy = new StandardProtectionPolicy(ownerPassword, userPassword, permissions);
            protectionPolicy.setEncryptionKeyLength(128); // Encriptación de 128 bits

            // Aplicar la política de protección al documento
            document.protect(protectionPolicy);
            document.save("documentoProtegido.pdf");

            System.out.println("Documento protegido con contraseña.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

En este ejemplo:
- La contraseña de **usuario** (`userPassword`) permite abrir el documento, pero con restricciones de permisos.
- La contraseña de **propietario** (`ownerPassword`) da control total sobre el documento, permitiendo modificar permisos y acceder al contenido.

---

#### 13.2. Encriptación y Control de Permisos Avanzados

Además de la protección por contraseña, PDFBox permite establecer permisos específicos mediante `AccessPermission`. Esto es útil para documentos que requieren que ciertas funcionalidades permanezcan bloqueadas para el usuario, como la copia o la modificación.

##### Ejemplo de control de permisos avanzados

```java
permissions.setCanPrintDegraded(true); // Permitir impresión de baja calidad
permissions.setCanModify(false); // Restringir modificaciones
permissions.setCanExtractForAccessibility(true); // Permitir extracción para accesibilidad
```

Esta configuración permite a los usuarios imprimir el documento en baja calidad y extraer el texto para software de accesibilidad, mientras que restringe otras funcionalidades.

