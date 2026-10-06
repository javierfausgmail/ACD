# Apache POI para documentos Word (`.docx`)

Apache POI es una biblioteca Java de código abierto que permite leer y generar documentos de Microsoft Office. Para trabajar con documentos Word modernos (`.docx`) utilizaremos el módulo **XWPF**.

En este tutorial veremos las operaciones más útiles para este tema:

- crear y guardar documentos `.docx`;
- trabajar con párrafos y fragmentos de texto;
- aplicar formato;
- crear tablas;
- insertar imágenes;
- añadir encabezados y pies de página;
- leer y modificar documentos existentes;
- utilizar plantillas sencillas.

> Los ejemplos utilizan **Apache POI 5.5.1** y el formato `.docx`.

---

## 1. Apache POI y los documentos Word

### 1.1 Módulos para Word

Apache POI dispone de dos APIs diferentes:

- **HWPF**: para el antiguo formato binario `.doc` de Word 97-2003. Su soporte es más limitado.
- **XWPF**: para el formato `.docx` basado en OOXML, utilizado en Word 2007 y posteriores.

En este tutorial trabajaremos exclusivamente con **XWPF**.

### 1.2 ¿Qué contiene realmente un `.docx`?

Un archivo `.docx` es, internamente, un paquete ZIP que contiene varios documentos XML y otros recursos.

Entre ellos suelen aparecer:

```text
word/document.xml       contenido principal
word/styles.xml         estilos
word/header*.xml        encabezados
word/footer*.xml        pies de página
word/media/             imágenes
```

En `document.xml`, el texto se organiza aproximadamente así:

```text
Documento
└── párrafo
    ├── run
    │   └── texto
    └── run
        └── texto
```

Apache POI permite trabajar con esta estructura mediante clases Java de alto nivel sin manipular directamente el XML.

Las principales son:

| Clase | Representa |
|---|---|
| `XWPFDocument` | Documento `.docx` |
| `XWPFParagraph` | Párrafo |
| `XWPFRun` | Fragmento de texto con un formato concreto |
| `XWPFTable` | Tabla |
| `XWPFTableRow` | Fila |
| `XWPFTableCell` | Celda |
| `XWPFHeader` / `XWPFFooter` | Encabezado y pie de página |

---

## 2. Configuración del proyecto

### Maven

Añada al `pom.xml`:

```xml
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>5.5.1</version>
</dependency>
```

`poi-ooxml` incluye las clases necesarias para trabajar con `.docx` y sus dependencias.

---

## 3. Crear y guardar un documento

```java
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class CrearDocumento {

    public static void main(String[] args) {
        Path salida = Path.of("documento.docx");

        try (XWPFDocument document = new XWPFDocument();
             OutputStream out = Files.newOutputStream(salida)) {

            document.write(out);
            System.out.println("Documento creado: " + salida.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("Error al crear el documento: " + e.getMessage());
        }
    }
}
```

Utilizamos **try-with-resources** para cerrar correctamente tanto el documento como el flujo de salida.

---

## 4. Párrafos y fragmentos de texto

### 4.1 `XWPFParagraph`

Un párrafo se crea con:

```java
XWPFParagraph parrafo = document.createParagraph();
```

### 4.2 `XWPFRun`

Dentro de cada párrafo creamos uno o varios `XWPFRun`.

Cada `run` puede tener un formato diferente:

```java
XWPFParagraph parrafo = document.createParagraph();

XWPFRun normal = parrafo.createRun();
normal.setText("Este texto es normal y ");

XWPFRun destacado = parrafo.createRun();
destacado.setText("este está en negrita.");
destacado.setBold(true);
```

Visualmente Word mostrará un único párrafo, aunque internamente tenga dos fragmentos.

Esto es importante cuando posteriormente queramos modificar documentos existentes.

---

## 5. Formato de texto y párrafos

```java
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

XWPFParagraph titulo = document.createParagraph();
titulo.setAlignment(ParagraphAlignment.CENTER);

XWPFRun run = titulo.createRun();
run.setText("Informe mensual");
run.setBold(true);
run.setFontFamily("Arial");
run.setFontSize(20);
run.setColor("1F4E78");
```

Algunas operaciones habituales sobre `XWPFRun` son:

```java
run.setBold(true);
run.setItalic(true);
run.setUnderline(UnderlinePatterns.SINGLE);
run.setFontSize(12);
run.setFontFamily("Arial");
run.setColor("FF0000");
```

El color se expresa como RGB hexadecimal, sin `#`.

### Estilos de Word

También es posible aplicar estilos definidos dentro del propio documento:

```java
parrafo.setStyle("Heading1");
```

Sin embargo, el identificador del estilo debe existir en el documento. Para documentos con un diseño elaborado suele ser más práctico partir de una **plantilla `.docx`** que ya contenga los estilos corporativos.

---

## 6. Crear tablas

Podemos crear una tabla indicando directamente su número de filas y columnas:

```java
XWPFTable tabla = document.createTable(3, 4);
```

Por ejemplo:

```java
XWPFTable tabla = document.createTable(3, 4);

tabla.getRow(0).getCell(0).setText("Producto");
tabla.getRow(0).getCell(1).setText("Cantidad");
tabla.getRow(0).getCell(2).setText("Precio");
tabla.getRow(0).getCell(3).setText("Importe");

tabla.getRow(1).getCell(0).setText("Teclado");
tabla.getRow(1).getCell(1).setText("4");
tabla.getRow(1).getCell(2).setText("35,00 €");
tabla.getRow(1).getCell(3).setText("140,00 €");
```

### Formato sencillo de la cabecera

Después de escribir el texto podemos obtener el primer `run` de cada celda y aplicarle formato:

```java
for (XWPFTableCell celda : tabla.getRow(0).getTableCells()) {
    XWPFRun encabezado = celda.getParagraphs()
            .get(0)
            .getRuns()
            .get(0);

    encabezado.setBold(true);
}
```

Para formatos de tabla muy avanzados puede ser necesario acceder a objetos OOXML de bajo nivel. En un tema introductorio es preferible limitarse a la API XWPF de alto nivel.

---

## 7. Insertar imágenes

Las imágenes se insertan dentro de un `XWPFRun`.

```java
import org.apache.poi.common.usermodel.PictureType;
import org.apache.poi.util.Units;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

XWPFParagraph parrafo = document.createParagraph();
XWPFRun run = parrafo.createRun();

try (InputStream imagen = Files.newInputStream(Path.of("logo.png"))) {
    run.addPicture(
            imagen,
            PictureType.PNG,
            "logo.png",
            Units.pixelToEMU(200),
            Units.pixelToEMU(80)
    );
}
```

`Units.pixelToEMU()` convierte píxeles a las unidades internas utilizadas por OOXML.

> Cambiar las dimensiones al insertar la imagen no modifica el archivo gráfico original; únicamente cambia su tamaño dentro del documento.

---

## 8. Encabezados y pies de página

En las versiones actuales de POI pueden crearse directamente desde `XWPFDocument`.

```java
import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;

XWPFHeader header = document.createHeader(HeaderFooterType.DEFAULT);
header.createParagraph()
      .createRun()
      .setText("Informe de ventas - Empresa Demo");

XWPFFooter footer = document.createFooter(HeaderFooterType.DEFAULT);
footer.createParagraph()
      .createRun()
      .setText("Documento generado automáticamente");
```

El tipo `DEFAULT` se utiliza para el encabezado o pie habitual. También existen `FIRST` y `EVEN` para documentos que necesiten tratamientos especiales en la primera página o en páginas pares.

---

## 9. Leer un documento existente

```java
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

Path archivo = Path.of("documento.docx");

try (InputStream in = Files.newInputStream(archivo);
     XWPFDocument document = new XWPFDocument(in)) {

    for (XWPFParagraph parrafo : document.getParagraphs()) {
        System.out.println(parrafo.getText());
    }
}
```

También podemos recorrer las tablas:

```java
for (XWPFTable tabla : document.getTables()) {
    for (XWPFTableRow fila : tabla.getRows()) {
        for (XWPFTableCell celda : fila.getTableCells()) {
            System.out.print(celda.getText() + " | ");
        }
        System.out.println();
    }
}
```

Para una extracción general de texto existe también `XWPFWordExtractor`.

---

## 10. Modificar texto existente

Podemos recorrer los `run` de un párrafo y cambiar su contenido:

```java
for (XWPFParagraph parrafo : document.getParagraphs()) {
    for (XWPFRun run : parrafo.getRuns()) {
        String texto = run.getText(0);

        if (texto != null && texto.contains("BORRADOR")) {
            run.setText(texto.replace("BORRADOR", "DEFINITIVO"), 0);
        }
    }
}
```

### Una limitación importante: el texto puede estar dividido en varios `run`

En Word, una frase aparentemente continua puede estar fragmentada internamente:

```text
Run 1 -> "Hola "
Run 2 -> "Javier"
Run 3 -> ", bienvenido"
```

Incluso un marcador como `${NOMBRE}` puede quedar dividido entre dos `run` si el usuario modificó el formato del documento.

Por tanto, un reemplazo simple `run por run` es adecuado para ejemplos controlados, pero no debe asumirse que funcionará con cualquier plantilla Word.

---

## 11. Trabajar con plantillas

Una estrategia muy habitual consiste en diseñar primero el documento en Word y utilizarlo como plantilla.

Por ejemplo, una plantilla podría contener:

```text
INFORME DE CLIENTE

Nombre: ${NOMBRE}
Empresa: ${EMPRESA}
Fecha: ${FECHA}
```

El programa abre la plantilla y reemplaza los marcadores.

Ventajas:

- el diseño se realiza cómodamente en Word;
- los cambios visuales no requieren reprogramar todo el documento;
- se pueden reutilizar estilos, cabeceras, logos y márgenes;
- el código Java se concentra en los datos.

Para documentos empresariales complejos suele ser una solución mejor que generar toda la maquetación mediante código.

---

## 12. Qué no hace Apache POI

Es importante conocer los límites de la biblioteca.

### Conversión a PDF

Apache POI **no renderiza un `.docx` a PDF**.

PDFBox tampoco es un conversor de Word a PDF: PDFBox trabaja directamente con documentos PDF.

Para convertir un `.docx` conservando su maquetación normalmente se utiliza un motor capaz de abrir y renderizar documentos Word, por ejemplo LibreOffice en modo automatizado, o soluciones comerciales especializadas.

### Funciones avanzadas

XWPF cubre muchas operaciones habituales, pero no implementa de forma sencilla todas las posibilidades de Microsoft Word. Algunas funciones avanzadas pueden requerir trabajar con los objetos OOXML/XMLBeans subyacentes.

En este tema nos centraremos en las operaciones de alto nivel que tienen aplicación directa en generación automática de documentos.

---

## 13. Buenas prácticas

- Utilice **try-with-resources** para documentos y streams.
- Para nuevos proyectos, utilice `.docx` y XWPF en lugar del antiguo `.doc` siempre que sea posible.
- Separe los **datos de negocio** de la lógica que genera el documento.
- Utilice plantillas para documentos con una maquetación compleja.
- No confíe en que una frase completa esté almacenada en un único `XWPFRun`.
- No recurra a las clases OOXML de bajo nivel salvo que la API XWPF no ofrezca la operación necesaria.
- Pruebe los archivos generados en Word o en un visor compatible.
- Si procesa documentos externos, limite tamaño y recursos disponibles: un `.docx` es un contenedor comprimido y no debe asumirse que cualquier entrada es confiable.

---

# Ejercicio: Generador de informes de ventas

## Objetivo

Desarrollar una aplicación Java que reciba una colección de datos de ventas y genere automáticamente un informe en formato `.docx` utilizando Apache POI.

El objetivo principal no es únicamente aprender métodos de POI, sino **separar los datos y cálculos de la generación del documento**.

## Modelo de datos

Cree una clase o `record` para representar cada producto vendido:

```java
public record VentaProducto(
        String producto,
        int cantidad,
        double precioUnitario
) {
    public double ingresos() {
        return cantidad * precioUnitario;
    }
}
```

> Como ampliación, puede utilizar `BigDecimal` para representar importes monetarios con precisión decimal.

Cree al menos cinco ventas de ejemplo en una colección:

```java
List<VentaProducto> ventas = List.of(
        new VentaProducto("Portátil", 3, 850.00),
        new VentaProducto("Monitor", 8, 190.00),
        new VentaProducto("Teclado", 15, 35.50),
        new VentaProducto("Ratón", 20, 18.90),
        new VentaProducto("Dock USB-C", 6, 79.90)
);
```

## Requisitos obligatorios

### 1. Encabezado y pie de página

El documento debe incluir:

- encabezado: `Informe de ventas - [Nombre de la empresa]`;
- pie de página: `Documento generado automáticamente`.

### 2. Título y fecha

Incluya al comienzo del documento:

**Informe mensual de ventas**

El título debe aparecer centrado y destacado.

Debajo debe mostrarse la fecha de generación obtenida mediante `LocalDate.now()`.

### 3. Tabla de ventas

Genere una tabla con las columnas:

| Producto | Cantidad | Precio unitario | Ingresos |
|---|---:|---:|---:|

Los ingresos deben calcularse desde los objetos `VentaProducto`; no deben escribirse manualmente.

La fila de cabecera debe diferenciarse visualmente, como mínimo utilizando texto en negrita.

### 4. Resumen calculado

A partir de la colección de ventas, calcule mediante Java:

1. producto con mayores ingresos;
2. producto con menores ingresos;
3. ingresos totales de todos los productos.

Añada estas tres conclusiones al final del documento.

Puede presentarlas como párrafos numerados manualmente (`1.`, `2.`, `3.`). La numeración automática real de Word puede dejarse como ampliación, ya que requiere trabajar con la estructura de numeración OOXML.

### 5. Guardado

El resultado debe guardarse como:

```text
informe_ventas.docx
```

## Estructura recomendada del programa

Evite escribir toda la aplicación dentro de `main`.

Una estructura posible es:

```text
VentaProducto
    └── datos de una venta y cálculo del ingreso

GeneradorInformeVentas
    ├── generarInforme(...)
    ├── crearEncabezado(...)
    ├── crearTitulo(...)
    ├── crearTablaVentas(...)
    ├── crearResumen(...)
    └── crearPie(...)

Main
    └── crea los datos y llama al generador
```

Esta separación hace que el código sea más fácil de leer, probar y reutilizar.

## Ampliaciones opcionales

Puede añadir una o varias de estas mejoras:

- logotipo de la empresa;
- colores corporativos en título y tabla;
- formato monetario con dos decimales y símbolo `€`;
- nombre del fichero incluyendo año y mes;
- uso de `BigDecimal` para los importes;
- generación del informe a partir de una plantilla `.docx`;
- lectura posterior del documento para comprobar por código su contenido.

## Criterios de revisión

El ejercicio debería considerarse correctamente resuelto si:

- el archivo generado puede abrirse sin errores;
- los cálculos proceden de los datos y son correctos;
- la tabla contiene todas las ventas;
- el encabezado, título, resumen y pie aparecen correctamente;
- se utilizan adecuadamente `XWPFDocument`, `XWPFParagraph`, `XWPFRun` y `XWPFTable`;
- los recursos se cierran mediante try-with-resources;
- la generación del documento no está mezclada innecesariamente con la lógica de cálculo.

---

# Autoevaluación

Seleccione una única respuesta correcta.

1. **¿Qué módulo de Apache POI se utiliza para archivos `.docx`?**
   - a) HSSF
   - b) XSSF
   - c) XWPF
   - d) HWPF

2. **¿Qué representa `XWPFDocument`?**
   - a) Una tabla
   - b) Un documento `.docx`
   - c) Una imagen
   - d) Un párrafo

3. **¿Qué relación existe entre `XWPFParagraph` y `XWPFRun`?**
   - a) Un `run` puede contener varios documentos.
   - b) Un párrafo puede contener varios `run` con formatos diferentes.
   - c) Son dos nombres para la misma clase.
   - d) Los `run` solo existen dentro de tablas.

4. **¿Qué método crea una tabla de cinco filas y cuatro columnas?**
   - a) `document.createTable(5, 4)`
   - b) `document.newTable(5, 4)`
   - c) `XWPFTable.create(5, 4)`
   - d) `document.addTable(4, 5)`

5. **¿Dónde se inserta normalmente una imagen con XWPF?**
   - a) En un `XWPFRun`.
   - b) Directamente en `FileOutputStream`.
   - c) En `XWPFTableRow` exclusivamente.
   - d) En `XWPFWordExtractor`.

6. **¿Por qué puede fallar un reemplazo de texto buscando únicamente dentro de cada `XWPFRun`?**
   - a) Porque Word nunca almacena texto.
   - b) Porque una palabra o marcador puede estar repartido entre varios `run`.
   - c) Porque `setText()` solo funciona con números.
   - d) Porque todos los documentos están cifrados.

7. **¿Qué estrategia suele ser conveniente para documentos con una maquetación corporativa compleja?**
   - a) Crear todos los elementos XML manualmente.
   - b) Utilizar una plantilla `.docx` preparada previamente.
   - c) Convertir primero el Word a CSV.
   - d) Utilizar únicamente `System.out.println()`.

8. **¿Convierte Apache POI directamente un documento Word a PDF?**
   - a) Sí, mediante `document.savePDF()`.
   - b) Sí, utilizando PDFBox internamente.
   - c) No; POI no es un motor de renderizado de Word a PDF.
   - d) Solo si el documento no contiene imágenes.

9. **¿Qué estructura debería separarse de la generación del documento en el ejercicio de ventas?**
   - a) Los cálculos y datos de negocio.
   - b) El `XWPFDocument` del `OutputStream`.
   - c) Java de Maven.
   - d) Las filas de las columnas.

10. **¿Qué construcción de Java resulta adecuada para cerrar automáticamente el documento y los streams?**
    - a) `switch`
    - b) `try-with-resources`
    - c) `synchronized`
    - d) `System.gc()`

## Soluciones

1. **c** · 2. **b** · 3. **b** · 4. **a** · 5. **a** · 6. **b** · 7. **b** · 8. **c** · 9. **a** · 10. **b**

---

## Referencias

- Documentación oficial de Apache POI: https://poi.apache.org/
- Guía oficial de XWPF: https://poi.apache.org/components/document/quick-guide-xwpf.html
- API de Apache POI: https://poi.apache.org/apidocs/dev/
