
### **Índice**

#### 1. [Introducción a Apache POI y la Manipulación de Documentos Word](#1-introducción-a-apache-poi-y-la-manipulación-de-documentos-word)
   - 1.1 [¿Qué es Apache POI?](#11-qué-es-apache-poi)
   - 1.2 [Instalación de Apache POI en un Proyecto Java](#12-instalación-de-apache-poi-en-un-proyecto-java)
   - 1.3 [Estructura básica de un archivo `.docx` y su relación con XML](#13-estructura-básica-de-un-archivo-docx-y-su-relación-con-xml)
   - 1.4 [Diferencia entre los módulos POI-HWPF y POI-XWPF](#14-diferencia-entre-los-módulos-poi-hwpf-y-poi-xwpf)

#### 2. [Creación y Estructura de un Documento Word con Apache POI](#2-creación-y-estructura-de-un-documento-word-con-apache-poi)
   - 2.1 [Crear un Documento Word (`XWPFDocument`)](#21-crear-un-documento-word-xwpfdocument)
   - 2.2 [Guardar el Documento en el Sistema de Archivos](#22-guardar-el-documento-en-el-sistema-de-archivos)
   - 2.3 [Carga de un Documento Existente](#23-carga-de-un-documento-existente)
   - 2.4 [Ejemplo completo de creación de un archivo Word básico](#24-ejemplo-completo-de-creación-de-un-archivo-word-básico)

#### 3. [Gestión de Texto en un Documento Word](#3-gestión-de-texto-en-un-documento-word)
   - 3.1 [Creación de Párrafos (`XWPFParagraph`)](#31-creación-de-párrafos-xwpfparagraph)
   - 3.2 [Creación de Fragmentos de Texto (`XWPFRun`)](#32-creación-de-fragmentos-de-texto-xwpfrun)
   - 3.3 [Lectura y actualización de texto en el documento](#33-lectura-y-actualización-de-texto-en-el-documento)
   - 3.4 [Borrar texto en un documento Word](#34-borrar-texto-en-un-documento-word)

#### 4. [Gestión de Estilos y Formatos en Word](#4-gestión-de-estilos-y-formatos-en-word)
   - 4.1 [Aplicación de Estilos Predefinidos](#41-aplicación-de-estilos-predefinidos)
   - 4.2 [Creación de Estilos Personalizados](#42-creación-de-estilos-personalizados)
   - 4.3 [Gestión de fuentes y colores](#43-gestión-de-fuentes-y-colores)

#### 5. [Gestión de Tablas en un Documento Word](#5-gestión-de-tablas-en-un-documento-word)
   - 5.1 [Crear Tablas (`XWPFTable`)](#51-crear-tablas-xwpftable)
   - 5.2 [Añadir y leer contenido de una celda (`XWPFTableCell`)](#52-añadir-y-leer-contenido-de-una-celda-xwpftablecell)
   - 5.3 [Formato de tablas: bordes, rellenos y alineación](#53-formato-de-tablas-bordes-rellenos-y-alineación)
   - 5.4 [Actualización de datos en una tabla](#54-actualización-de-datos-en-una-tabla)
   - 5.5 [Borrar tablas y celdas](#55-borrar-tablas-y-celdas)

#### 6. [Gestión de Imágenes en Documentos Word](#6-gestión-de-imágenes-en-documentos-word)
   - 6.1 [Añadir imágenes al documento (`XWPFPicture`)](#61-añadir-imágenes-al-documento-xwpfpicture)
   - 6.2 [Configuración de las dimensiones y posición de las imágenes](#62-configuración-de-las-dimensiones-y-posición-de-las-imágenes)
   - 6.3 [Actualización de imágenes existentes](#63-actualización-de-imágenes-existentes)
   - 6.4 [Eliminación de imágenes en el documento](#64-eliminación-de-imágenes-en-el-documento)

#### 7. [Encabezados y Pies de Página](#7-encabezados-y-pies-de-página)
   - 7.1 [Creación de encabezados y pies de página](#71-creación-de-encabezados-y-pies-de-página)
   - 7.2 [Añadir texto e imágenes en los encabezados y pies de página](#72-añadir-texto-e-imágenes-en-los-encabezados-y-pies-de-página)
   - 7.3 [Formato de encabezados y pies de página](#73-formato-de-encabezados-y-pies-de-página)
   - 7.4 [Eliminación de encabezados y pies de página](#74-eliminación-de-encabezados-y-pies-de-página)

#### 8. [Uso de Listas: Viñetas y Listas Numeradas](#8-uso-de-listas-viñetas-y-listas-numeradas)
   - 8.1 [Creación de listas con viñetas](#81-creación-de-listas-con-viñetas)
   - 8.2 [Creación de listas numeradas](#82-creación-de-listas-numeradas)
   - 8.3 [Gestión y personalización de las viñetas y numeraciones](#83-gestión-y-personalización-de-las-viñetas-y-numeraciones)
   - 8.4 [Lectura, actualización y eliminación de listas](#84-lectura-actualización-y-eliminación-de-listas)

#### 9. [Manipulación de Estilos de Sección y Configuración de Página](#9-manipulación-de-estilos-de-sección-y-configuración-de-página)
   - 9.1 [Definir los márgenes de la página](#91-definir-los-márgenes-de-la-página)
   - 9.2 [Configuración de orientación](#92-configuración-de-orientación)
   - 9.3 [Aplicación de ajustes específicos a secciones](#93-aplicación-de-ajustes-específicos-a-secciones)

#### 10. [Uso de Comentarios y Notas](#10-uso-de-comentarios-y-notas)
   - 10.1 [Añadir comentarios en el documento](#101-añadir-comentarios-en-el-documento)
   - 10.2 [Lectura y edición de comentarios existentes](#102-lectura-y-edición-de-comentarios-existentes)
   - 10.3 [Eliminación de comentarios](#103-eliminación-de-comentarios)

#### 11. [Gestión de Campos y Formularios](#11-gestión-de-campos-y-formularios)
   - 11.1 [Crear y leer campos como `DATE`, `AUTHOR`, etc.](#111-crear-y-leer-campos-como-date-author-etc)
   - 11.2 [Introducción al uso de formularios y campos editables](#112-introducción-al-uso-de-formularios-y-campos-editables)
   - 11.3 [Actualización y eliminación de campos](#113-actualización-y-eliminación-de-campos)

#### 12. [Exportación y Conversión de Documentos Word](#12-exportación-y-conversión-de-documentos-word)
   - 12.1 [Conversión de un documento Word a otros formatos](#121-conversión-de-un-documento-word-a-otros-formatos)
   - 12.2 [Ejemplo de exportación a PDF](#122-ejemplo-de-exportación-a-pdf)

#### 13. [Gestión Avanzada: Macros y Elementos Rich Text](#13-gestión-avanzada-macros-y-elementos-rich-text)
   - 13.1 [Introducción al uso de macros en documentos Word](#131-introducción-al-uso-de-macros-en-documentos-word)
   - 13.2 [Añadir Rich Text y otros elementos multimedia](#132-añadir-rich-text-y-otros-elementos-multimedia)

#### 14. [Práctica Completa: Creación y Manipulación de un Documento Word Completo](#14-práctica-completa-creación-y-manipulación-de-un-documento-word-completo)
   - 14.1 [Ejercicio completo de creación de un documento](#141-ejercicio-completo-de-creación-de-un-documento)
   - 14.2 [Lectura y actualización de contenido](#142-lectura-y-actualización-de-contenido)
   - 14.3 [Exportación a formato PDF](#143-exportación-a-formato-pdf)

#### 15. [Conclusión y Buenas Prácticas](#15-conclusión-y-buenas-prácticas)
   - 15.1 [Buenas prácticas en el uso de Apache POI](#151-buenas-prácticas-en-el-uso-de-apache-poi)
   - 15.2 [Recomendaciones para proyectos de producción](#152-recomendaciones-para-proyectos-de-producción)
   - 15.3 [Recursos adicionales y documentación oficial](#153-recursos-adicionales-y-documentación-oficial)

---



### 1. Introducción a Apache POI y la Manipulación de Documentos Word

Apache POI es una biblioteca Java que permite la creación, manipulación y exportación de documentos de Microsoft Office, incluidos los archivos de Word. Esta biblioteca ofrece soporte para trabajar con los formatos `.doc` (Word 97-2003) y `.docx` (Word 2007 y posteriores). La biblioteca es altamente utilizada en aplicaciones empresariales y educativas que requieren la generación automática de reportes, plantillas o documentos dinámicos.

#### 1.1 ¿Qué es Apache POI?

Apache POI es un proyecto de código abierto desarrollado por la Apache Software Foundation que proporciona un conjunto de API para manipular formatos de documentos de Microsoft. Su arquitectura se organiza en módulos, entre los cuales destacan:
- **POI-HWPF**: para trabajar con archivos `.doc`.
- **POI-XWPF**: para trabajar con archivos `.docx`.

Además, Apache POI ofrece compatibilidad con otros formatos de Microsoft Office, como Excel y PowerPoint, lo que la convierte en una herramienta versátil.

#### 1.2 Instalación de Apache POI en un Proyecto Java

Para utilizar Apache POI en un proyecto Java, es necesario añadir las dependencias correspondientes en el archivo `pom.xml` si se usa Maven, o descargar los archivos `.jar` y agregarlos al proyecto manualmente.

##### Dependencia para Maven:
```xml
<dependencies>
    <dependency>
        <groupId>org.apache.poi</groupId>
        <artifactId>poi-ooxml</artifactId>
        <version>5.2.3</version>
    </dependency>
</dependencies>
```

##### Añadir los JAR manualmente:
Para proyectos que no usan Maven, descargue los archivos `.jar` de Apache POI y agréguelos a las librerías del proyecto.

#### 1.3 Estructura básica de un archivo `.docx` y su relación con XML

Los archivos `.docx` son archivos comprimidos en formato **ZIP** que contienen una estructura XML. Dentro de un archivo `.docx` podemos encontrar varias carpetas y archivos XML que definen el contenido y el formato del documento:
- `document.xml`: Contiene el contenido principal del documento (texto, tablas, imágenes).
- `styles.xml`: Define los estilos de texto y párrafos.
- `header1.xml` y `footer1.xml`: Contienen los encabezados y pies de página.

Apache POI nos permite manipular estos elementos a través de una interfaz de alto nivel sin necesidad de trabajar directamente con XML.

#### 1.4 Diferencia entre los módulos POI-HWPF y POI-XWPF

- **POI-HWPF**: Este módulo es compatible únicamente con archivos `.doc` (Word 97-2003). Es más limitado en términos de funcionalidad y se usa para proyectos que requieren manipulación de documentos antiguos.
- **POI-XWPF**: Este módulo permite trabajar con archivos `.docx`, que es el formato actual de Word. POI-XWPF es más potente y flexible y ofrece soporte para elementos complejos como tablas, imágenes, encabezados y pies de página.

---

### 2. Creación y Estructura de un Documento Word con Apache POI

En esta sección se explica cómo crear un documento Word básico utilizando Apache POI, guardarlo en el sistema de archivos y cargar un documento existente.

#### 2.1 Crear un Documento Word (`XWPFDocument`)

La clase principal para trabajar con documentos Word es `XWPFDocument`, que representa un documento `.docx`. Crear un documento Word básico es sencillo y solo requiere instanciar un objeto `XWPFDocument`.

```java
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class CrearDocumentoWord {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument()) {
            System.out.println("Documento Word creado.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 2.2 Guardar el Documento en el Sistema de Archivos

Una vez creado el documento, podemos guardarlo utilizando la clase `FileOutputStream`.

```java
import java.io.FileOutputStream;

public class GuardarDocumentoWord {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument();
             FileOutputStream out = new FileOutputStream("documento_ejemplo.docx")) {
            document.write(out);
            System.out.println("Documento guardado en el sistema de archivos.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 2.3 Carga de un Documento Existente

Para cargar un documento existente, simplemente debemos leer el archivo `.docx` con un objeto `FileInputStream` y pasar el flujo a `XWPFDocument`.

```java
import java.io.FileInputStream;

public class CargarDocumentoWord {
    public static void main(String[] args) {
        try (FileInputStream fis = new FileInputStream("documento_ejemplo.docx");
             XWPFDocument document = new XWPFDocument(fis)) {
            System.out.println("Documento cargado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 2.4 Ejemplo completo de creación de un archivo Word básico

```java
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import java.io.FileOutputStream;

public class CrearYGuardarDocumento {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument();
             FileOutputStream out = new FileOutputStream("documento_basico.docx")) {
            System.out.println("Documento Word básico creado y guardado.");
            document.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

### 3. Gestión de Texto en un Documento Word

Apache POI permite añadir y modificar texto en un documento Word mediante objetos como `XWPFParagraph` y `XWPFRun`. A continuación se detallan los pasos para gestionar párrafos y texto.

#### 3.1 Creación de Párrafos (`XWPFParagraph`)

Los párrafos se gestionan mediante la clase `XWPFParagraph`, que representa una unidad de texto en el documento.

```java
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public class CrearParrafo {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument()) {
            XWPFParagraph paragraph = document.createParagraph();
            System.out.println("Párrafo creado en el documento.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 3.2 Creación de Fragmentos de Texto (`XWPFRun`)

Para añadir texto dentro de un párrafo, se usa la clase `XWPFRun`. Un `XWPFRun` representa una porción de texto con su propio formato.

```java
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class CrearFragmentoTexto {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument()) {
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText("Este es un fragmento de texto.");
            System.out.println("Texto añadido al párrafo.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 3.3 Lectura y actualización de texto en el documento

Para leer y modificar el texto de un `XWPFRun`, se utilizan los métodos `getText()` y `setText()`.

```java
public class LeerActualizarTexto {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument()) {
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText("Texto inicial.");
            System.out.println("Texto actual: " + run.getText(0));
            run.setText("Texto actualizado.");
            System.out.println("Texto actualizado: " + run.getText(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 3.4 Borrar texto en un documento Word

Para eliminar texto, puede vaciarse un `XWPFRun` o eliminar el párrafo completo.

```java
public class BorrarTexto {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument()) {
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText("Texto que será borrado.");
            run.setText("");  // Vacía el contenido del texto.
            System.out.println("Texto borrado.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---
Aquí tiene el desarrollo completo para las secciones 4, 5 y 6.

---

### 4. Gestión de Estilos y Formatos en Word

Esta sección explora cómo aplicar estilos y formatos en un documento Word usando Apache POI. Los estilos permiten definir la apariencia del texto, como fuente, color, tamaño, entre otros. Apache POI permite manejar estilos predefinidos y personalizados para mejorar la presentación del documento.

#### 4.1 Aplicación de Estilos Predefinidos

Apache POI permite aplicar estilos predefinidos, como `Heading` (encabezados), `Title` (título), y `Subtitle` (subtítulo). Estos estilos ofrecen una estructura visual uniforme en el documento.

```java
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public class EstilosPredefinidos {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument()) {
            XWPFParagraph title = document.createParagraph();
            title.setStyle("Title");
            title.createRun().setText("Este es un título con estilo predefinido.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 4.2 Creación de Estilos Personalizados

Apache POI permite crear estilos personalizados, aunque esta funcionalidad puede ser limitada. Para estilos más específicos, es recomendable manipular atributos individuales como tamaño de fuente, color y alineación mediante `XWPFRun`.

```java
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class EstilosPersonalizados {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument()) {
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText("Texto con estilo personalizado.");
            run.setFontSize(14);
            run.setBold(true);
            run.setColor("FF5733");  // Código de color en hexadecimal
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 4.3 Gestión de fuentes y colores

Además de los estilos básicos, es posible ajustar directamente la fuente y el color del texto. Esto puede realizarse con los métodos `setFontFamily()` y `setColor()` en `XWPFRun`.

```java
public class FuentesYColores {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument()) {
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText("Texto con fuente y color personalizado.");
            run.setFontFamily("Courier New");
            run.setColor("0000FF"); // Azul
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

### 5. Gestión de Tablas en un Documento Word

Apache POI permite añadir y gestionar tablas en documentos Word. Las tablas se crean mediante `XWPFTable` y se pueden personalizar mediante celdas (`XWPFTableCell`), configurando bordes, alineación, y formato de contenido.

#### 5.1 Crear Tablas (`XWPFTable`)

Para crear una tabla, se utiliza el método `createTable()` de `XWPFDocument`. Este método permite definir la estructura inicial de la tabla.

```java
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

public class CrearTabla {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument()) {
            XWPFTable table = document.createTable();
            XWPFTableRow row = table.getRow(0); // Primera fila
            row.getCell(0).setText("Celda 1, Fila 1");
            row.addNewTableCell().setText("Celda 2, Fila 1");

            XWPFTableRow row2 = table.createRow(); // Segunda fila
            row2.getCell(0).setText("Celda 1, Fila 2");
            row2.getCell(1).setText("Celda 2, Fila 2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 5.2 Añadir y leer contenido de una celda (`XWPFTableCell`)

Para manipular el contenido de una celda, se utiliza la clase `XWPFTableCell`, que ofrece métodos para modificar el texto y el formato de cada celda individualmente.

```java
public class ContenidoCeldas {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument()) {
            XWPFTable table = document.createTable();
            XWPFTableRow row = table.getRow(0);
            row.getCell(0).setText("Celda 1, Fila 1");

            System.out.println("Contenido de la celda: " + row.getCell(0).getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 5.3 Formato de tablas: bordes, rellenos y alineación

Apache POI permite personalizar los bordes y la alineación de una tabla y sus celdas. Esta configuración mejora la legibilidad de los datos en el documento.

```java
import org.apache.poi.xwpf.usermodel.XWPFTableCell;

public class FormatoTablas {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument()) {
            XWPFTable table = document.createTable();
            XWPFTableRow row = table.getRow(0);
            XWPFTableCell cell = row.getCell(0);
            cell.setText("Celda con formato");
            cell.getCTTc().addNewTcPr().addNewShd().setFill("E0E0E0"); // Fondo gris claro
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 5.4 Actualización de datos en una tabla

Para actualizar el contenido de una celda específica, se utiliza `setText()` en la celda que se desea modificar.

```java
public class ActualizarTabla {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument()) {
            XWPFTable table = document.createTable();
            XWPFTableRow row = table.getRow(0);
            row.getCell(0).setText("Valor inicial");
            
            row.getCell(0).setText("Valor actualizado"); // Actualización de la celda
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 5.5 Borrar tablas y celdas

Para eliminar una celda o una fila completa, Apache POI permite modificar la estructura de la tabla accediendo y eliminando sus componentes.

```java
public class BorrarTabla {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument()) {
            XWPFTable table = document.createTable();
            table.removeRow(0); // Elimina la primera fila
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

### 6. Gestión de Imágenes en Documentos Word

Apache POI permite insertar imágenes en documentos Word mediante la clase `XWPFPicture`, que admite archivos en formatos comunes como JPEG y PNG.

#### 6.1 Añadir imágenes al documento (`XWPFPicture`)

Para añadir una imagen, se utiliza `addPicture()` en un `XWPFRun`, especificando el tipo de imagen y su ubicación en el sistema de archivos.

```java
import org.apache.poi.util.Units;
import java.io.FileInputStream;

public class InsertarImagen {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument();
             FileInputStream is = new FileInputStream("ruta/a/imagen.jpg")) {
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.addPicture(is, XWPFDocument.PICTURE_TYPE_JPEG, "imagen.jpg", Units.toEMU(100), Units.toEMU(100)); // 100x100 píxeles
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 6.2 Configuración de las dimensiones y posición de las imágenes

Al añadir una imagen, es posible ajustar sus dimensiones en el documento estableciendo la altura y el ancho en EMUs (unidades de medida utilizadas en Word).

```java
public class ConfigurarDimensionesImagen {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument();
             FileInputStream is = new FileInputStream("ruta/a/imagen.jpg")) {
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.addPicture(is, XWPFDocument.PICTURE_TYPE_JPEG, "imagen.jpg", Units.toEMU(200), Units.toEMU(150)); // 200x150 píxeles
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


```

#### 6.3 Actualización de imágenes existentes

Actualizar una imagen implica eliminar la imagen anterior y añadir la nueva en su lugar. Apache POI no cuenta con una función directa de actualización de imágenes, pero se puede lograr manualmente.

```java
public class ActualizarImagen {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument()) {
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            
            // Eliminar imagen anterior (si existe) y añadir una nueva
            run.setText("Nueva imagen añadida.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 6.4 Eliminación de imágenes en el documento

Eliminar una imagen requiere acceder al `XWPFRun` que la contiene y eliminar el objeto. Apache POI actualmente no proporciona un método directo, por lo que se deben eliminar todos los elementos en el `XWPFRun`.

```java
public class EliminarImagen {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument()) {
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText("Imagen eliminada del documento.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

Aquí tiene el desarrollo completo para las secciones 7, 8 y 9 del tutorial.

---

### 7. Encabezados y Pies de Página

En Apache POI, los encabezados y pies de página se crean mediante la clase `XWPFHeaderFooterPolicy`. Los encabezados y pies de página se utilizan para insertar texto e imágenes en la parte superior e inferior de cada página del documento, respectivamente.

#### 7.1 Creación de encabezados y pies de página

Para crear un encabezado o pie de página, se utiliza el método `createHeader()` o `createFooter()` de `XWPFHeaderFooterPolicy`.

```java
import org.apache.poi.xwpf.usermodel.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public class CrearEncabezadoPiePagina {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument()) {
            XWPFHeaderFooterPolicy headerFooterPolicy = document.createHeaderFooterPolicy();
            
            // Crear encabezado
            XWPFParagraph headerParagraph = headerFooterPolicy.createHeader(XWPFHeaderFooterPolicy.DEFAULT).createParagraph();
            headerParagraph.createRun().setText("Este es el encabezado del documento.");

            // Crear pie de página
            XWPFParagraph footerParagraph = headerFooterPolicy.createFooter(XWPFHeaderFooterPolicy.DEFAULT).createParagraph();
            footerParagraph.createRun().setText("Este es el pie de página del documento.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 7.2 Añadir texto e imágenes en los encabezados y pies de página

Además del texto, se pueden añadir imágenes a los encabezados y pies de página utilizando `XWPFRun`.

```java
import org.apache.poi.util.Units;
import java.io.FileInputStream;

public class TextoEImagenEnEncabezadoPie {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument()) {
            XWPFHeaderFooterPolicy headerFooterPolicy = document.createHeaderFooterPolicy();

            // Añadir texto y una imagen al encabezado
            XWPFParagraph headerParagraph = headerFooterPolicy.createHeader(XWPFHeaderFooterPolicy.DEFAULT).createParagraph();
            XWPFRun headerRun = headerParagraph.createRun();
            headerRun.setText("Encabezado con imagen.");
            try (FileInputStream is = new FileInputStream("ruta/a/imagen.jpg")) {
                headerRun.addPicture(is, XWPFDocument.PICTURE_TYPE_JPEG, "imagen.jpg", Units.toEMU(50), Units.toEMU(50)); // Tamaño de la imagen
            }

            // Añadir texto al pie de página
            XWPFParagraph footerParagraph = headerFooterPolicy.createFooter(XWPFHeaderFooterPolicy.DEFAULT).createParagraph();
            footerParagraph.createRun().setText("Pie de página con texto.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 7.3 Formato de encabezados y pies de página

Puede aplicarse formato al texto de los encabezados y pies de página de la misma forma que en el contenido normal del documento.

```java
public class FormatoEncabezadoPie {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument()) {
            XWPFHeaderFooterPolicy headerFooterPolicy = document.createHeaderFooterPolicy();

            // Formato en el encabezado
            XWPFParagraph headerParagraph = headerFooterPolicy.createHeader(XWPFHeaderFooterPolicy.DEFAULT).createParagraph();
            XWPFRun headerRun = headerParagraph.createRun();
            headerRun.setText("Encabezado en negrita.");
            headerRun.setBold(true);

            // Formato en el pie de página
            XWPFParagraph footerParagraph = headerFooterPolicy.createFooter(XWPFHeaderFooterPolicy.DEFAULT).createParagraph();
            XWPFRun footerRun = footerParagraph.createRun();
            footerRun.setText("Pie de página en cursiva.");
            footerRun.setItalic(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 7.4 Eliminación de encabezados y pies de página

Para eliminar un encabezado o pie de página, se puede resetear la política de encabezados y pies de página.

```java
public class EliminarEncabezadoPie {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument()) {
            XWPFHeaderFooterPolicy headerFooterPolicy = document.createHeaderFooterPolicy();
            headerFooterPolicy.clearHeaderFooterPolicy(); // Eliminar encabezados y pies de página
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

### 8. Uso de Listas: Viñetas y Listas Numeradas

Apache POI permite la creación de listas con viñetas y listas numeradas en documentos Word. Estas listas mejoran la presentación de información en forma de puntos o números.

#### 8.1 Creación de listas con viñetas

Para crear una lista con viñetas, se utiliza `XWPFParagraph` y `XWPFNumbering`.

```java
import org.apache.poi.xwpf.usermodel.XWPFNumbering;

public class ListaViñetas {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument()) {
            XWPFNumbering numbering = document.createNumbering();

            XWPFParagraph bulletPoint = document.createParagraph();
            bulletPoint.setNumID(numbering.addNum(numbering.addAbstractNum().getCTAbstractNum()));
            bulletPoint.createRun().setText("Primer elemento con viñeta");

            XWPFParagraph bulletPoint2 = document.createParagraph();
            bulletPoint2.setNumID(numbering.addNum(numbering.addAbstractNum().getCTAbstractNum()));
            bulletPoint2.createRun().setText("Segundo elemento con viñeta");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 8.2 Creación de listas numeradas

De manera similar, se puede crear una lista numerada.

```java
public class ListaNumerada {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument()) {
            XWPFNumbering numbering = document.createNumbering();
            String numID = numbering.addNum(numbering.addAbstractNum().getCTAbstractNum());

            XWPFParagraph numberPoint1 = document.createParagraph();
            numberPoint1.setNumID(numID);
            numberPoint1.createRun().setText("Primer elemento numerado");

            XWPFParagraph numberPoint2 = document.createParagraph();
            numberPoint2.setNumID(numID);
            numberPoint2.createRun().setText("Segundo elemento numerado");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 8.3 Gestión y personalización de las viñetas y numeraciones

Es posible personalizar el estilo de las viñetas y los números utilizando `XWPFNumbering` y los parámetros de configuración avanzados de `CTAbstractNum`.

```java
public class PersonalizarListaNumerada {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument()) {
            XWPFNumbering numbering = document.createNumbering();

            String numID = numbering.addNum(numbering.addAbstractNum().getCTAbstractNum());
            XWPFParagraph paragraph = document.createParagraph();
            paragraph.setNumID(numID);
            XWPFRun run = paragraph.createRun();
            run.setText("Elemento personalizado en lista numerada.");
            run.setBold(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 8.4 Lectura, actualización y eliminación de listas

Para leer, actualizar o eliminar elementos de una lista, se puede acceder a cada `XWPFParagraph` en el documento, identificando si tiene una `numID` asignada.

```java
public class LeerYActualizarLista {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument()) {
            XWPFParagraph paragraph = document.createParagraph();
            paragraph.setNumID("numId");  // Identificador de la lista
            paragraph.createRun().setText("Elemento de lista");
            
            // Leer contenido
            System.out.println("Contenido: " + paragraph.getText());

            // Actualizar contenido
            paragraph.getRuns().get(0).setText("Elemento actualizado", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

### 9. Manipulación de Estilos de Sección y Configuración de Página

Apache POI permite configurar el estilo de las secciones y ajustar propiedades de página, como márgenes y orientación, en un documento Word.

#### 9.1 Definir los márgenes de la página

Para definir los márgenes de la página, se utilizan los métodos de configuración de márgenes en `XWPFDocument`.

```java
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;

public class MargenesPagina {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument()) {
            CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
            sectPr.addNewPgMar().setLeft(1440

); // Margen izquierdo (1440 EMUs = 1 pulgada)
            sectPr.addNewPgMar().setRight(1440); // Margen derecho
            sectPr.addNewPgMar().setTop(1440); // Margen superior
            sectPr.addNewPgMar().setBottom(1440); // Margen inferior
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 9.2 Configuración de orientación

Para cambiar la orientación de la página a horizontal o vertical, se utiliza `CTPageSz` dentro de la configuración de la sección.

```java
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;

public class OrientacionPagina {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument()) {
            CTPageSz pageSize = document.getDocument().getBody().addNewSectPr().addNewPgSz();
            pageSize.setOrient(STPageOrientation.LANDSCAPE); // Orientación horizontal
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 9.3 Aplicación de ajustes específicos a secciones

En documentos extensos, se pueden aplicar diferentes configuraciones a distintas secciones del documento, como márgenes y orientación específicos para cada sección.

```java
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;

public class ConfiguracionSecciones {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument()) {
            // Crear primera sección
            CTSectPr sectPr1 = document.getDocument().getBody().addNewSectPr();
            sectPr1.addNewPgMar().setLeft(720); // Márgenes más pequeños para la primera sección

            // Crear nueva sección con márgenes distintos
            CTSectPr sectPr2 = document.getDocument().getBody().addNewSectPr();
            sectPr2.addNewPgMar().setLeft(1440); // Márgenes de 1 pulgada para la segunda sección
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

Aquí tiene el desarrollo completo de las secciones 10, 11 y 12.

---

### 10. Uso de Comentarios y Notas

Apache POI permite añadir comentarios en documentos Word para proporcionar información adicional o anotaciones sobre el contenido. Estos comentarios no forman parte del contenido principal del documento y son visibles solo en el modo de revisión.

#### 10.1 Añadir comentarios en el documento

Para añadir un comentario, es necesario crear un `XWPFComment` y asignarlo al `XWPFParagraph` deseado. Sin embargo, esta funcionalidad tiene limitaciones en Apache POI y puede requerir manipulación directa del XML.

```java
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public class AñadirComentario {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument()) {
            XWPFParagraph paragraph = document.createParagraph();
            paragraph.createRun().setText("Texto con comentario.");

            // Crear comentario (requiere manipulación XML avanzada)
            System.out.println("Funcionalidad de comentarios limitada en Apache POI");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 10.2 Lectura y edición de comentarios existentes

Dado que la manipulación de comentarios es limitada, Apache POI no proporciona soporte directo para leer y editar comentarios. Existen librerías de terceros que pueden ayudar a manipular los comentarios en archivos XML, pero no están incluidas en Apache POI.

#### 10.3 Eliminación de comentarios

La eliminación de comentarios también es limitada en Apache POI, ya que no ofrece un método específico para borrar comentarios. La solución más fiable para manejar comentarios suele ser realizar una manipulación XML avanzada.

---

### 11. Gestión de Campos y Formularios

Apache POI permite la creación y manipulación de algunos campos en documentos Word, como `DATE`, `AUTHOR` y otros campos de metadatos. Los formularios son elementos avanzados y su soporte en Apache POI es limitado, pero pueden crearse mediante `XWPFFieldRun` y `XWPFRun`.

#### 11.1 Crear y leer campos como `DATE`, `AUTHOR`, etc.

Apache POI permite insertar ciertos campos de información de metadatos en un documento, aunque su implementación directa para otros campos es limitada.

```java
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class CrearCampoFecha {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument()) {
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText("Fecha de creación: ");
            run.setText(java.time.LocalDate.now().toString()); // Campo de fecha manual
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 11.2 Introducción al uso de formularios y campos editables

Para crear formularios básicos con campos editables, se utiliza `XWPFRun` para marcar texto como "rellenable". Sin embargo, Apache POI no proporciona un método directo para crear formularios completos como en Microsoft Word.

```java
public class CrearCampoEditable {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument()) {
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText("Nombre: __________");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 11.3 Actualización y eliminación de campos

Para actualizar el contenido de un campo, se debe acceder al `XWPFRun` que lo contiene y utilizar `setText()` para modificarlo. La eliminación también se realiza accediendo al `XWPFRun` y limpiando el texto.

```java
public class ActualizarCampo {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument()) {
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText("Nombre: Juan Pérez");

            // Actualizar el campo
            run.setText("Nombre: Ana García", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

### 12. Exportación y Conversión de Documentos Word

Apache POI permite guardar documentos en el formato `.docx`, pero no cuenta con funcionalidades nativas para exportar a otros formatos, como PDF o HTML. Para esto, se suele utilizar bibliotecas adicionales o integraciones con aplicaciones externas, como LibreOffice o JODConverter.

#### 12.1 Conversión de un documento Word a otros formatos

Para convertir un documento de Word a otros formatos (como PDF o HTML), se puede utilizar **JODConverter** junto con **LibreOffice**. Esta combinación permite convertir documentos en múltiples formatos mediante una configuración externa.

**Ejemplo de configuración de JODConverter en Maven:**
```xml
<dependency>
    <groupId>org.jodconverter</groupId>
    <artifactId>jodconverter-local</artifactId>
    <version>4.2.2</version>
</dependency>
```

Con JODConverter configurado, el siguiente código muestra cómo convertir un archivo Word a PDF.

```java
import org.jodconverter.local.LocalConverter;
import org.jodconverter.office.LocalOfficeManager;
import java.io.File;

public class ConvertirWordAPDF {
    public static void main(String[] args) {
        File inputFile = new File("documento.docx");
        File outputFile = new File("documento.pdf");

        LocalOfficeManager officeManager = LocalOfficeManager.builder().install().build();

        try {
            officeManager.start();
            LocalConverter.make().convert(inputFile).to(outputFile).execute();
            System.out.println("Documento convertido a PDF.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                officeManager.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
```

#### 12.2 Ejemplo de exportación a PDF

Si se requiere solo exportar documentos a PDF, existen bibliotecas específicas que simplifican esta tarea, como **Aspose.Words** (que es de pago) o **Apache FOP** para PDF. Sin embargo, una alternativa gratuita y sencilla es usar **PDFBox** o combinar Apache POI con iText para un control más preciso sobre el formato.

**Ejemplo básico con Aspose.Words:**

```java
import com.aspose.words.Document;

public class ExportarAPDFConAspose {
    public static void main(String[] args) {
        try {
            Document doc = new Document("documento.docx");
            doc.save("documento.pdf", com.aspose.words.SaveFormat.PDF);
            System.out.println("Documento exportado a PDF con Aspose.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

Para una solución gratuita y nativa, lo ideal es utilizar `JODConverter` como se describe en el ejemplo anterior, que permite realizar la conversión sin necesidad de bibliotecas de pago.

---

Aquí tiene el desarrollo completo de las secciones 13, 14 y 15.

---

### 13. Gestión Avanzada: Macros y Elementos Rich Text

En documentos de Word, las macros permiten automatizar tareas repetitivas o realizar operaciones avanzadas. Los elementos de Rich Text ofrecen formatos complejos para el contenido. Sin embargo, en Apache POI, la manipulación directa de macros no está soportada, y el soporte para Rich Text es básico, permitiendo el uso de estilos y formatos avanzados pero sin capacidades de macro.

#### 13.1 Introducción al uso de macros en documentos Word

Las macros en Word están basadas en VBA (Visual Basic for Applications) y se almacenan en archivos `.docm`. Apache POI actualmente no permite crear o manipular macros directamente. Para trabajar con documentos que contienen macros, se deben utilizar alternativas o programarlas desde el entorno de Microsoft Word.

Para ejecutar macros en documentos generados, puede usarse código VBA al abrir el documento en Word. Si necesita incluir macros en un documento programáticamente, una posible solución es crear plantillas en Word que ya contengan las macros necesarias y luego usarlas con Apache POI.

#### 13.2 Añadir Rich Text y otros elementos multimedia

Aunque Apache POI no tiene un soporte completo para Rich Text, se pueden añadir elementos con diferentes estilos y formatos, como texto en varias fuentes, colores y tamaños. Además, Apache POI permite la inserción de imágenes y gráficos básicos.

```java
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class RichTextExample {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument()) {
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText("Texto en negrita y azul con Rich Text.");
            run.setBold(true);
            run.setColor("0000FF"); // Azul
            run.setFontSize(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

Para elementos multimedia avanzados como audio y video, Apache POI no tiene soporte directo. Estos tipos de contenido deben añadirse manualmente mediante el editor de Word después de generar el documento.

---

### 14. Práctica Completa: Creación y Manipulación de un Documento Word Completo

Esta sección proporciona un ejemplo práctico para crear y manipular un documento Word completo utilizando las funcionalidades de Apache POI. El objetivo es consolidar todo lo aprendido hasta ahora.

#### 14.1 Ejercicio completo de creación de un documento

A continuación, un ejemplo de código para crear un documento completo que incluya encabezados, pies de página, listas, tablas e imágenes.

```java
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class CrearDocumentoCompleto {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument();
             FileOutputStream out = new FileOutputStream("documento_completo.docx");
             FileInputStream image = new FileInputStream("imagen.jpg")) {

            // Encabezado y pie de página
            XWPFHeaderFooterPolicy headerFooterPolicy = document.createHeaderFooterPolicy();
            XWPFParagraph header = headerFooterPolicy.createHeader(XWPFHeaderFooterPolicy.DEFAULT).createParagraph();
            header.createRun().setText("Encabezado del Documento");

            XWPFParagraph footer = headerFooterPolicy.createFooter(XWPFHeaderFooterPolicy.DEFAULT).createParagraph();
            footer.createRun().setText("Pie de página del Documento");

            // Título y subtítulo
            XWPFParagraph title = document.createParagraph();
            title.setStyle("Title");
            title.createRun().setText("Título del Documento");

            XWPFParagraph subtitle = document.createParagraph();
            subtitle.setStyle("Subtitle");
            subtitle.createRun().setText("Subtítulo del Documento");

            // Lista con viñetas
            XWPFNumbering numbering = document.createNumbering();
            String bulletNumID = numbering.addNum(numbering.addAbstractNum().getCTAbstractNum());
            XWPFParagraph bullet1 = document.createParagraph();
            bullet1.setNumID(bulletNumID);
            bullet1.createRun().setText("Primer elemento de la lista");

            XWPFParagraph bullet2 = document.createParagraph();
            bullet2.setNumID(bulletNumID);
            bullet2.createRun().setText("Segundo elemento de la lista");

            // Tabla
            XWPFTable table = document.createTable();
            XWPFTableRow row1 = table.getRow(0);
            row1.getCell(0).setText("Celda 1, Fila 1");
            row1.addNewTableCell().setText("Celda 2, Fila 1");
            XWPFTableRow row2 = table.createRow();
            row2.getCell(0).setText("Celda 1, Fila 2");
            row2.getCell(1).setText("Celda 2, Fila 2");

            // Imagen
            XWPFParagraph imageParagraph = document.createParagraph();
            XWPFRun imageRun = imageParagraph.createRun();
            imageRun.addPicture(image, Document.PICTURE_TYPE_JPEG, "imagen.jpg", Units.toEMU(100), Units.toEMU(100));

            // Guardar el documento
            document.write(out);
            System.out.println("Documento completo creado con éxito.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 14.2 Lectura y actualización de contenido

Para leer y actualizar el contenido de este documento, se pueden recorrer los elementos como párrafos, tablas e imágenes usando las estructuras de `XWPFParagraph`, `XWPFTable`, y `XWPFPicture`.

```java
public class LeerYActualizarDocumento {
    public static void main(String[] args) {
        try (XWPFDocument document = new XWPFDocument(new FileInputStream("documento_completo.docx"))) {
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                System.out.println("Texto del párrafo: " + paragraph.getText());
            }
            // Actualizar primer párrafo
            document.getParagraphs().get(0).createRun().setText("Texto actualizado en el primer párrafo");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 14.3 Exportación a formato PDF

La exportación a PDF puede realizarse utilizando JODConverter, como se describe en la sección 12, para convertir el documento completo a PDF.

---

### 15. Conclusión y Buenas Prácticas

Al trabajar con Apache POI para manipular documentos Word en Java, es importante seguir ciertas prácticas para garantizar un uso eficiente de la biblioteca y la creación de documentos de calidad.

#### 15.1 Buenas prácticas en el uso de Apache POI

- **Gestión de Recursos**: Cerrar los objetos `XWPFDocument` y flujos de archivos (`FileInputStream`, `FileOutputStream`) al finalizar su uso para liberar recursos del sistema.
- **Uso de Plantillas**: Para documentos complejos, considere utilizar plantillas de Word predefinidas que contengan los estilos y elementos comunes, minimizando la cantidad de código Java necesario para generar el documento.
- **Eficiencia en Loops**: Evite manipular demasiados elementos en bucles largos. En su lugar, intente estructurar los documentos de forma que se reduzca la necesidad de realizar muchas operaciones de escritura o lectura en bucles.

#### 15.2 Recomendaciones para proyectos de producción

- **Uso de Bibliotecas Externas**: Para tareas avanzadas, como conversión de formatos o macros, considere combinar Apache POI con bibliotecas externas (como JODConverter o Aspose) para mayor funcionalidad.
- **Validación de Documentos**: Pruebe los documentos generados en diferentes versiones de Microsoft Word para asegurar compatibilidad, ya que algunas funciones pueden variar entre versiones.
- **Pruebas de Rendimiento**: Apache POI puede ser intensivo en memoria cuando se manipulan documentos grandes. Realice pruebas de rendimiento si el proyecto implica la creación de documentos extensos o complejos.

#### 15.3 Recursos adicionales y documentación oficial

- **Documentación Oficial de Apache POI**: [https://poi.apache.org/](https://poi.apache.org/)
- **Ejemplos y Comunidad de Apache POI en GitHub**: Consulte el repositorio de ejemplos en GitHub para ver implementaciones y técnicas avanzadas.
- **Foros y Stack Overflow**: Para resolver problemas específicos, la comunidad de Stack Overflow y otros foros de desarrollo pueden ser útiles.

---

### Examen Teórico: Manipulación de Documentos Word con Apache POI

1. **¿Qué es Apache POI?**
   - a) Un IDE de desarrollo para Java
   - b) Una biblioteca de manipulación de documentos de Microsoft Office en Java 
   - c) Una herramienta para gestionar bases de datos en Java
   - d) Un sistema operativo para aplicaciones Java

2. **¿Qué módulo de Apache POI se utiliza para manipular archivos `.docx`?**
   - a) POI-EXCEL
   - b) POI-HSSF
   - c) POI-HWPF
   - d) POI-XWPF 

3. **¿Qué clase se utiliza para representar un documento Word en Apache POI?**
   - a) XWPFDocument ****
   - b) XWPFWord
   - c) XWPFDOC
   - d) HWPFDocument

4. **¿Qué método permite crear un nuevo párrafo en un documento?**
   - a) document.addParagraph()
   - b) document.createParagraph() 
   - c) document.newParagraph()
   - d) document.appendParagraph()

5. **¿Cuál de los siguientes métodos se utiliza para añadir texto a un párrafo?**
   - a) setText()
   - b) addText()
   - c) createRun().setText() 
   - d) textRun()

6. **¿Qué clase se utiliza para representar una tabla en Apache POI?**
   - a) XWPFTab
   - b) XWPFTable 
   - c) XWPFRow
   - d) XWPFData

7. **¿Cómo se añade una nueva fila a una tabla existente en Apache POI?**
   - a) table.addRow()
   - b) table.createNewRow()
   - c) table.createRow()
   - d) table.newRow()

8. **¿Qué clase se utiliza para definir el contenido de una celda en una tabla?**
   - a) XWPFCell
   - b) XWPFParagraph
   - c) XWPFTableCell 
   - d) XWPFTableData

9. **¿Qué método se utiliza para guardar un documento Word en el sistema de archivos?**
   - a) document.write(File)
   - b) document.saveTo(File)
   - c) document.store(File)
   - d) document.write(FileOutputStream) 

10. **¿Cuál de las siguientes opciones permite establecer un encabezado en un documento?**
    - a) document.setHeader()
    - b) XWPFHeaderFooterPolicy.createHeader() 
    - c) document.addHeader()
    - d) document.newHeader()

11. **¿Qué clase se usa para agregar una imagen en Apache POI?**
    - a) XWPFImage
    - b) XWPFPicture 
    - c) XWPFImageAdd
    - d) XWPFMedia

12. **¿Cuál de los siguientes formatos de imagen es soportado por Apache POI?**
    - a) TIFF
    - b) BMP
    - c) JPEG ****
    - d) PSD

13. **¿Qué clase se utiliza para aplicar numeración en listas?**
    - a) XWPFNumberList
    - b) XWPFNumbering 
    - c) XWPFList
    - d) XWPFBulletList

14. **¿Cómo se crea una lista con viñetas en Apache POI?**
    - a) Usando XWPFBullet
    - b) Usando XWPFList.create()
    - c) Usando XWPFParagraph.setNumID() ****
    - d) Usando XWPFTab.setList()

15. **¿Qué método de `XWPFRun` permite establecer el tamaño de la fuente?**
    - a) setSize()
    - b) setFontSize() 
    - c) setTextSize()
    - d) setFont()

16. **¿Cuál es la clase utilizada para definir los márgenes de la página?**
    - a) XWPFMargin
    - b) XWPFDocumentMargin
    - c) CTSectPr 
    - d) CTMargin

17. **¿Cómo se cambia la orientación de la página a horizontal en Apache POI?**
    - a) setPageOrientation(LANDSCAPE)
    - b) document.setPageOrientationHorizontal()
    - c) CTPageSz.setOrient(STPageOrientation.LANDSCAPE) 
    - d) XWPFOrientation.HORIZONTAL

18. **¿Cuál es la clase para manipular encabezados y pies de página en Apache POI?**
    - a) XWPFHeaderFooterPolicy 
    - b) XWPFHeaderPolicy
    - c) XWPFDocumentHeader
    - d) XWPFPageSetup

19. **¿Qué método se utiliza para establecer el color de fondo de una celda en una tabla?**
    - a) cell.setBackgroundColor()
    - b) cell.getCTTc().addNewTcPr().addNewShd().setFill()
    - c) cell.setBackgroundFill()
    - d) cell.setShadingColor()

20. **¿Qué herramienta adicional permite convertir un documento Word a PDF en Java?**
    - a) PDFBox
    - b) JODConverter 
    - c) PDFKit
    - d) OpenPDF

---


### Ejercicio: Generador de Informes de Ventas

**Descripción:**

En este ejercicio, usted desarrollará un programa en Java que, utilizando Apache POI, generará un informe de ventas en formato Word. El informe debe incluir un encabezado, un título, una tabla de datos con información de ventas, una lista numerada con conclusiones y un pie de página. Este ejercicio le permitirá aplicar los conceptos aprendidos sobre la manipulación de documentos Word y familiarizarse con la creación y estructuración de informes automáticos en Java.

### Requisitos del Programa

1. **Encabezado y Pie de Página:**
   - El documento debe tener un encabezado que incluya el texto “Informe de Ventas – [Nombre de la Empresa]”.
   - El pie de página debe mostrar el texto “Documento generado automáticamente” en la parte inferior de cada página.

2. **Título y Fecha:**
   - Debajo del encabezado, incluya un título centralizado que diga “Informe Mensual de Ventas”.
   - Añada un subtítulo con la fecha de generación del informe (use la fecha actual del sistema).

3. **Tabla de Datos:**
   - Cree una tabla con las siguientes columnas:
     - **Producto**
     - **Cantidad Vendida**
     - **Precio Unitario**
     - **Ingresos Totales**
   - Ingrese al menos cinco filas de datos de productos en la tabla.
   - Asegúrese de calcular y mostrar el valor en la columna “Ingresos Totales” (Cantidad Vendida x Precio Unitario).

4. **Conclusiones:**
   - Al final del informe, incluya una lista numerada con tres conclusiones acerca de las ventas. Estas conclusiones pueden ser:
     1. El producto con mayores ingresos.
     2. El producto con menores ingresos.
     3. El total de ingresos generados (suma de todas las ventas).

5. **Guardar el Documento:**
   - El programa debe guardar el documento como `informe_ventas.docx` en el sistema de archivos.

**Puntos Extra:**
- Añada una imagen del logotipo de la empresa en el encabezado (si tiene una imagen `.jpg` en el sistema).

##### Ejemplo de Salida Esperada

El documento debe mostrar:
- **Encabezado** con el texto “Informe de Ventas – [Nombre de la Empresa]” en todas las páginas.
- **Título**: “Informe Mensual de Ventas”, seguido de la fecha actual.
- **Tabla** de datos con al menos cinco productos y sus valores calculados en la columna de ingresos.
- **Lista numerada de conclusiones** con las observaciones sobre el producto más vendido, el menos vendido y el total de ingresos.
- **Pie de página** con el texto “Documento generado automáticamente” en todas las páginas.


  
