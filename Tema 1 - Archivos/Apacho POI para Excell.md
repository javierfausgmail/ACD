### Archivos Excel (XLSX)

**Excel** es un formato de hoja de cálculo muy usado, especialmente para el análisis y almacenamiento de datos tabulares, ya que permite representar datos con columnas, filas, cálculos y visualizaciones. En el proyecto, usamos archivos Excel para almacenar la información de los empleados y trabajamos con la librería **Apache POI**, que nos permite leer y escribir hojas de cálculo de manera programática.

Trabajar con archivos Excel tiene la ventaja de ser muy conocido por la mayoría de los usuarios, y es un formato visual que facilita la comprensión de los datos. En el contexto de nuestro proyecto, usamos Excel para almacenar sueldos mensuales de los empleados, y posteriormente calcular sus promedios, máximos y mínimos de forma automatizada.

- **Ventajas**: Interfaz gráfica fácil de usar, soporta funciones matemáticas y visualizaciones.
- **Desventajas**: Más complejo de manipular desde código, requiere librerías externas, y puede ocupar mucho espacio.

Por ejemplo, en nuestro tutorial, una hoja de cálculo de Excel de empleados podría tener una estructura tabular donde cada fila representa un empleado, con columnas para cada dato, como se muestra a continuación:

```
| ID | Nombre        | DNI       | Enero   | Febrero | Marzo   | ... | Diciembre |
|----|---------------|-----------|---------|---------|---------|-----|-----------|
| 1  | Diego Romero  | 94738265Y | 1966.49 | 1632.31 | 1498.61 | ... | 1306.41   |
```


---

#### Conceptos estructurales clave

1. **Contenedor ZIP + partes OOXML**  
    Un `.xlsx` es un **ZIP** que contiene múltiples ficheros XML (partes) conectados por **relaciones**:
    
    - `[Content_Types].xml` describe los tipos de contenido del paquete.
        
    - `/_rels/.rels` define relaciones de alto nivel del paquete.
        
    - `/docProps/*` metadatos (título, autor, etc.).
        
    - `/xl/workbook.xml` la **libreta** (workbook) con la lista de hojas.
        
    - `/xl/worksheets/sheetN.xml` cada **hoja** (worksheet) con sus filas/celdas.
        
    - `/xl/sharedStrings.xml` diccionario de **cadenas compartidas** (optimiza texto repetido).
        
    - `/xl/styles.xml` **estilos** (formatos numéricos, fuentes, bordes, alineación, etc.).
        
    - `/xl/_rels/*` y otras partes (tablas, gráficos, comentarios, nombres, vínculos externos).
        
2. **Jerarquía lógica de datos**
    
    - **Workbook (libro)** → **Sheets (hojas)** → **Rows (filas)** → **Cells (celdas)**.
        
    - **Direcciones**: notación **A1** (columna en letras + fila en números), con variantes **absolutas/relativas** (`$A$1`, `A$1`, `$A1`). Existe también notación **R1C1** (menos habitual).
        
    - **Rangos**: `A1:D10`, rangos **nombrados** (Defined Names) y **tablas** (ListObjects).
        
3. **Tipos de celda y formatos**
    
    - **Tipos de valor**: numérico, cadena (directa o vía `sharedStrings`), booleano, error, **fórmula**.
        
    - **Formato de visualización**: independiente del valor (p. ej., un número con formato `"€#,##0.00"` o un **serial de fecha** con máscara `dd/mm/yyyy`).
        
4. **Fechas y tiempos (seriales)**  
    Excel almacena fechas/horas como **números de punto flotante**: días desde una fecha base (normalmente **1900-01-01**; en Mac antiguos, base 1904). La “fecha” es el entero; la “hora”, la fracción.
    
5. **Fórmulas y cálculo**
    
    - Las fórmulas se guardan como texto (p. ej., `=SUM(A1:A10)`), con **referencias relativas/absolutas**.
        
    - Excel calcula al abrir; librerías externas (POI) pueden **evaluar** fórmulas con motores propios (no cubren el 100% de funciones complejas/volátiles).
        
6. **Estructuras adicionales**
    
    - **Tablas (ListObjects)** con encabezados, filas totales y estilo; base para Power Query y gráficos.
        
    - **Validación de datos**, **formato condicional**, **comentarios/notes**, **hipervínculos**.
        
    - **PivotTables** y **gráficos** (DrawingML/Chart) como partes relacionadas.
        
    - **Temas y estilos** centralizados en `/xl/styles.xml`.
        
7. **Límites comunes de Excel (relevantes en automatización)**
    
    - **Filas**: 1,048,576; **Columnas**: 16,384 (XFD).
        
    - **Tamaño** y **estilos** excesivos ralentizan el archivo.
        
    - Excel **no es** una base de datos: para millones de registros, usar **streaming** y/o formatos alternativos (CSV/Parquet) para intercambio.
        

---

#### Ejemplo práctico (Excel con Apache POI)

> Recomendación: utilizar **Apache POI** para `.xlsx` (XSSF/SXSSF). A continuación se muestran dependencias y tareas típicas (lectura, escritura, fechas, fórmulas, validación, streaming).

##### Dependencias Maven (versión gestionada por propiedad)

```xml
<properties>
  <poi.version>5.2.5</poi.version><!-- Ajuste a la versión estable que utilice -->
</properties>

<dependencies>
  <!-- Lectura/Escritura OOXML (XLSX) -->
  <dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>${poi.version}</version>
  </dependency>
  <!-- Útil para Address/Reference y utilidades comunes (incluido en poi-ooxml) -->
  <!-- <dependency> org.apache.poi:poi:${poi.version} </dependency> -->

  <!-- Opcional: logging, si desea diagnóstico de fórmulas -->
  <dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-simple</artifactId>
    <version>2.0.13</version>
    <scope>runtime</scope>
  </dependency>
</dependencies>
```

##### Lectura segura (valores “tal cual” y “como se muestran”)

```java
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import java.nio.file.*;
import java.io.InputStream;
import java.util.*;

try (InputStream in = Files.newInputStream(Paths.get("empleados.xlsx"));
     Workbook wb = WorkbookFactory.create(in)) { // autodetecta XLSX/XLS
    DataFormatter fmt = new DataFormatter();     // "como lo ve" el usuario
    FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

    Sheet sheet = wb.getSheetAt(0);
    for (Row row : sheet) {
        for (Cell cell : row) {
            // Valor bruto según tipo
            Object raw = switch (cell.getCellType()) {
                case STRING -> cell.getStringCellValue();
                case NUMERIC -> {
                    if (DateUtil.isCellDateFormatted(cell)) yield cell.getDateCellValue(); // java.util.Date
                    yield cell.getNumericCellValue();
                }
                case BOOLEAN -> cell.getBooleanCellValue();
                case FORMULA -> {
                    CellValue cv = evaluator.evaluate(cell); // evalúa la fórmula
                    yield switch (cv.getCellType()) {
                        case STRING -> cv.getStringValue();
                        case NUMERIC -> DateUtil.isCellDateFormatted(cell)
                                ? cell.getDateCellValue() : cv.getNumberValue();
                        case BOOLEAN -> cv.getBooleanValue();
                        default -> null;
                    };
                }
                default -> null; // BLANK o ERROR
            };

            // Valor formateado (texto según formato de celda)
            String shown = fmt.formatCellValue(cell, evaluator);

            CellAddress addr = cell.getAddress(); // p. ej. A1, B2...
            // Procesar raw/shown según su lógica...
        }
    }
}
```

##### Escritura (nuevo libro, estilos y fechas correctas)

```java
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.io.OutputStream;
import java.util.Date;

try (Workbook wb = new XSSFWorkbook()) {
    Sheet sheet = wb.createSheet("Empleados");

    // Estilo para moneda (ejemplo en euros con 2 decimales)
    CellStyle money = wb.createCellStyle();
    DataFormat df = wb.createDataFormat();
    money.setDataFormat(df.getFormat("€#,##0.00"));

    // Estilo de fecha
    CellStyle date = wb.createCellStyle();
    date.setDataFormat(df.getFormat("dd/mm/yyyy"));

    // Encabezados
    Row header = sheet.createRow(0);
    header.createCell(0).setCellValue("id");
    header.createCell(1).setCellValue("nombre");
    header.createCell(2).setCellValue("dni");
    header.createCell(3).setCellValue("sueldoMax");
    header.createCell(4).setCellValue("sueldoMin");
    header.createCell(5).setCellValue("sueldoMedio");
    header.createCell(6).setCellValue("fechaAlta");

    // Fila de ejemplo
    Row r1 = sheet.createRow(1);
    r1.createCell(0).setCellValue(1);
    r1.createCell(1).setCellValue("Diego Romero");
    r1.createCell(2).setCellValue("94738265Y");

    Cell cMax = r1.createCell(3); cMax.setCellValue(1966.49); cMax.setCellStyle(money);
    Cell cMin = r1.createCell(4); cMin.setCellValue(1224.74); cMin.setCellStyle(money);
    Cell cMed = r1.createCell(5); cMed.setCellValue(1578.12); cMed.setCellStyle(money);

    Cell cFecha = r1.createCell(6); cFecha.setCellValue(new Date()); cFecha.setCellStyle(date);

    // Fórmula (media de sueldos)
    Row r2 = sheet.createRow(2);
    r2.createCell(0).setCellValue(2);
    r2.createCell(1).setCellValue("María López");
    r2.createCell(2).setCellValue("12345678X");
    r2.createCell(3).setCellValue(2100.00);
    r2.createCell(4).setCellValue(1300.50);
    r2.createCell(5).setCellFormula("AVERAGE(D2:E2)");

    // Ajuste de ancho (usar con moderación en datasets grandes)
    for (int i = 0; i <= 6; i++) sheet.autoSizeColumn(i);

    try (OutputStream out = Files.newOutputStream(Paths.get("empleados.xlsx"))) {
        wb.write(out);
    }
}
```

##### Streaming para grandes volúmenes (escritura eficiente)

```java
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.io.OutputStream;

SXSSFWorkbook wb = new SXSSFWorkbook(500); // mantiene 500 filas en memoria
wb.setCompressTempFiles(true);
Sheet sheet = wb.createSheet("Datos");

for (int i = 0; i < 200_000; i++) {
    Row row = sheet.createRow(i);
    for (int j = 0; j < 10; j++) {
        row.createCell(j).setCellValue("R" + i + "C" + j);
    }
}
try (OutputStream out = Files.newOutputStream(Paths.get("grande.xlsx"))) {
    wb.write(out);
}
wb.dispose(); // elimina temporales
```

##### Validación de datos (listas desplegables en una columna)

```java
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.ss.usermodel.DataValidationConstraint.OperatorType;

Workbook wb = new XSSFWorkbook();
Sheet sheet = wb.createSheet("Hoja");

DataValidationHelper dvHelper = new XSSFDataValidationHelper((org.apache.poi.xssf.usermodel.XSSFSheet) sheet);
DataValidationConstraint constraint = dvHelper.createExplicitListConstraint(new String[] {"ACTIVO", "INACTIVO"});
CellRangeAddressList regions = new CellRangeAddressList(1, 1000, 3, 3); // D2:D1001
DataValidation validation = dvHelper.createValidation(constraint, regions);
validation.setSuppressDropDownArrow(true);
sheet.addValidationData(validation);
```

##### Evaluación de fórmulas y obtención del resultado mostrado

```java
FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
DataFormatter fmt = new DataFormatter();

for (Row row : sheet) {
    for (Cell cell : row) {
        String shown = fmt.formatCellValue(cell, evaluator); // texto “como Excel”
        // usar 'shown' para exportar/mostrar
    }
}
```

##### Nombres definidos y búsqueda por dirección

```java
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.util.CellReference;

Name n = wb.createName();
n.setNameName("TablaEmpleados");
n.setRefersToFormula("Empleados!$A$1:$F$101");

CellReference ref = new CellReference("B2");
Row row = sheet.getRow(ref.getRow());
Cell cell = row != null ? row.getCell(ref.getCol()) : null;
```

> **Notas operativas**
> 
> - Para lectura de **archivos enormes**, considere el **Event API (SAX)** de XSSF (`XSSFReader`) en lugar de iterar DOM; requiere más código pero minimiza RAM.
>     
> - Si se acepta CSV como intercambio, puede **exportar/importar** CSV para cargas masivas y dejar XLSX para entrega al usuario final.
>     

---

#### Problemas habituales

1. **Fechas mal interpretadas (seriales y locales)**
    
    - **Problema**: tratar fechas como texto o números sin formato; bases 1900/1904; desajustes de zona horaria; día/mes intercambiados al importar.
        
    - **Mitigación**: detectar fechas con `DateUtil.isCellDateFormatted`, mapear a `java.util.Date`/`java.time` y **aplicar estilos de fecha** al escribir; fijar un **formato ISO** en exportaciones; documentar la base de fechas utilizada.
        
2. **Confundir valor con presentación (formato)**
    
    - **Problema**: el número subyacente no coincide con lo que “ve” el usuario (p. ej., `0.1234` con formato `%`).
        
    - **Mitigación**: cuando se necesite el texto visible, usar `DataFormatter + FormulaEvaluator`; cuando se necesite el valor exacto, usar el **tipo nativo** (numérico/booleano/Date) y controlar el formato aparte.
        
3. **Fórmulas no evaluadas o incompatibles**
    
    - **Problema**: al generar un XLSX, las celdas con fórmula quedan sin calcular hasta que Excel las abre; algunas funciones **no están implementadas** en el evaluador de la librería.
        
    - **Mitigación**: si se necesita un valor inmediato, **calcular en el backend** y escribir el resultado (o forzar `evaluator.evaluateAll()` entendiendo sus límites); evitar funciones volátiles complejas y **referencias externas**.
        
4. **Rendimiento y memoria en grandes volúmenes**
    
    - **Problema**: crear/leer hojas con cientos de miles de filas agota memoria y es lento; `autoSizeColumn` y estilos por celda multiplican el coste.
        
    - **Mitigación**: usar **SXSSF** para escritura en streaming y **XSSF SAX** para lectura; evitar `autoSizeColumn` masivo; **reutilizar estilos** (no crear uno por celda); minimizar formatos condicionales.
        
5. **Estructura irregular: celdas combinadas, filas/columnas vacías, tipos inconsistentes**
    
    - **Problema**: “merged cells” rompen la alineación lógica; hojas con encabezados en varias filas; columnas que mezclan texto y número; filas dispersas.
        
    - **Mitigación**: definir **plantillas** estrictas (una fila de encabezados, tipos estables por columna, sin combinaciones en datos); al leer, **normalizar** (descombinar si es necesario, saltar filas de encabezado, validar tipos por columna).
        

> **Consejos adicionales**
> 
> - Diferenciar `.xls` (BIFF binario, 65.536 filas) de `.xlsx` (OOXML).
>     
> - Evitar macros en automatizaciones (`.xlsm`) salvo que sean imprescindibles; si existen, trate los módulos VBA como artefactos aparte.
>     
> - Documentar el significado de **celda vacía** vs. **cero** vs. **texto vacío**.
>     
> - Controlar **locales** (separador decimal, formato moneda) al exportar para evitar malentendidos.
>     

---

Con estos bloques, queda cubierta la **estructura jerárquica** interna de Excel (OOXML), un **recetario práctico** para leer/escribir desde Java con **Apache POI**, y un compendio de **riesgos habituales** con sus mitigaciones.