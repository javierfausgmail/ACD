En este documento introduciremos distintos tipos de ficheros ( TXT, JSON, XML, CSV y Excel ) para aprender a leer, escribir y procesar datos. Cada uno de estos tipos de fichero tiene sus propias características y aplicaciones específicas, y es importante entender qué los hace únicos y cómo trabajar con ellos. A continuación, se presentan los conceptos fundamentales de cada tipo de fichero y su relación directa con el proyecto que implementaremos posteriormente en este tutorial.

### Ficheros de Texto Plano (TXT)

Un fichero **TXT** es un archivo de texto plano que contiene caracteres sin formato, organizados línea por línea. Es la representación más básica de los datos y se usa mucho por su simplicidad. En nuestro proyecto, usamos ficheros TXT para almacenar los datos de los empleados de una manera fácilmente legible.

Los ficheros de texto plano son ideales cuando queremos trabajar con información sin estructura compleja, simplemente separando valores con un espacio o un símbolo específico. En el tutorial, usamos la clase **BufferedWriter** y **BufferedReader** para escribir y leer estos archivos, respectivamente, lo cual facilita el procesamiento secuencial de datos.

- **Ventajas**: Simples de manejar, lectura directa para humanos, sin estructura compleja.
- **Desventajas**: Difícil de manejar si se necesita estructura, redundancia de datos y poco eficiente en representación de información compleja.

Por ejemplo, en nuestro tutorial hemos utilizado archivos TXT para almacenar la información básica de los empleados, como el nombre, el ID y el DNI y los sueldos. Cada fila del archivo de texto tiene un formato similar al siguiente:

```
1 Diego Romero 94738265Y 1966.4927179208123 1632.3126935368616 1498.607317470391 1455.2592026655568 1608.8588746883925 1361.7475685378038 1839.98479803483 1923.3945524301662 1278.193520715171 1956.3161506272759 1224.7427154169684 1306.414769521917

```



### Archivos CSV (Comma-Separated Values)

Los archivos **CSV** son ficheros de texto donde los datos están separados por comas (o punto y coma). Este tipo de fichero es una forma sencilla de representar datos tabulares, similar a una hoja de cálculo, y se usa mucho para intercambiar información entre sistemas.

En nuestro proyecto, hemos utilizado CSV para almacenar información sobre los empleados, ya que nos permite organizar datos en filas y columnas. Los **CSV** son una buena opción cuando se tiene una estructura regular, y trabajamos con ellos usando la librería **Apache Commons CSV** para facilitar la lectura y escritura de manera estructurada y evitar errores en la separación de campos.

- **Ventajas**: Fácil de usar para representar datos tabulares, ampliamente soportado.
- **Desventajas**: Puede resultar complicado si los datos contienen comas, no permite jerarquías o estructuras complejas.

Por ejemplo, en nuestro tutorial, un archivo CSV de empleados podría tener varias filas con la siguiente estructura:

```
1,DiegoRomero,94738265Y,1966.4927179208123,1632.3126935368616,1498.607317470391,1455.2592026655568,1608.8588746883925,1361.7475685378038,1839.98479803483,1923.3945524301662,1278.193520715171,1956.3161506272759,1224.7427154169684,1306.414769521917
2,María López,12345678X,2100.00,1980.50,1750.25,1820.00,1600.75,1700.60,1900.45,2000.00,1850.30,1925.80,1800.50,1700.25
3,Juan García,87654321Z,1500.00,1450.75,1480.50,1550.20,1600.00,1625.25,1580.00,1520.50,1480.75,1500.60,1550.40,1490.30
```

Esto demuestra cómo los datos se organizan en columnas separadas por comas, lo cual facilita su estructura tabular para aplicaciones que requieran el intercambio de datos estructurados.

### Archivos JSON (JavaScript Object Notation)

El formato **JSON** es un formato ligero para intercambiar datos, ideal para estructurar información jerárquica y complejo. JSON permite representar objetos con propiedades y valores, lo que lo hace muy útil para trabajar con datos que necesitan tener una estructura clara. En nuestro proyecto, utilizamos JSON para almacenar información de empleados, incluyendo atributos como el **id**, el **nombre**, y los sueldos calculados (sueldo mínimo, máximo y medio).

Usamos la librería **Jackson** para serializar los objetos Java a JSON y deserializar JSON a objetos Java. Esto nos permite, de una manera eficiente, transformar datos entre el código y el archivo. JSON es especialmente útil cuando queremos almacenar o enviar información en una estructura compleja, pero de una manera que sea fácil de comprender y procesar.

- **Ventajas**: Ideal para representar estructuras complejas y anidadas, ampliamente utilizado en aplicaciones web, fácil de leer.
- **Desventajas**: Puede ser menos eficiente que CSV en representación tabular y no tan adecuado para datos extremadamente grandes.

Por ejemplo, en nuestro tutorial, un archivo JSON de empleados podría tener la siguiente estructura para varios empleados:

```
[
  {
    "id": 1,
    "nombre": "Diego Romero",
    "dni": "94738265Y",
    "sueldoMax": 1966.49,
    "sueldoMin": 1224.74,
    "sueldoMedio": 1578.12
  },
  {
    "id": 2,
    "nombre": "María López",
    "dni": "12345678X",
    "sueldoMax": 2100.00,
    "sueldoMin": 1300.50,
    "sueldoMedio": 1700.25
  }
]
```

Este ejemplo muestra cómo se organiza la información del empleado en un formato que es fácil de leer y procesar, especialmente útil cuando trabajamos con estructuras complejas que necesitan ser representadas de una manera clara y jerárquica.

### Archivos XML (eXtensible Markup Language)

**XML** es otro formato para representar datos jerárquicos y estructurados, similar a JSON, pero con una sintaxis más detallada y basada en etiquetas. XML tiene la ventaja de ser un formato estándar que se usa mucho en aplicaciones empresariales, sobre todo cuando se requiere definir esquemas y validar la estructura de los datos.

En nuestro proyecto, usamos XML para representar la información de empleados de una manera estructurada, usando la librería **JDOM** para leer y escribir archivos XML. Aunque XML es más verboso que JSON, tiene la ventaja de permitir definir claramente las reglas de validación de los datos (usando DTD o XSD).

- **Ventajas**: Formato estándar, soporta validación, flexible y legible.
- **Desventajas**: Verboso y generalmente ocupa más espacio que otros formatos como JSON o CSV.

Por ejemplo, en nuestro tutorial, un archivo XML de empleados podría tener la siguiente estructura para varios empleados:

```
<empleado>
  <id>1</id>
  <nombre>Diego Romero</nombre>
  <dni>94738265Y</dni>
  <sueldoMax>1966.49</sueldoMax>
  <sueldoMin>1224.74</sueldoMin>
  <sueldoMedio>1578.12</sueldoMedio>
</empleado>
<empleado>
  <id>2</id>
  <nombre>María López</nombre>
  <dni>12345678X</dni>
  <sueldoMax>2100.00</sueldoMax>
  <sueldoMin>1300.50</sueldoMin>
  <sueldoMedio>1700.25</sueldoMedio>
</empleado>
```

Este ejemplo muestra cómo se puede estructurar la información del empleado utilizando etiquetas, lo cual hace que el archivo XML sea legible y permita una representación jerárquica clara de los datos.


A continuación se añade una explicación breve y más precisa de la **estructura jerárquica** de un documento XML y de cómo se denominan sus partes y etiquetas según su **posición en el árbol**. Además, se corrige el ejemplo para garantizar que el XML sea **bien formado** (un único elemento raíz).

#### Estructura jerárquica y denominación de las partes

1. **Documento XML**  
    Es la unidad completa. Puede comenzar con una **declaración XML** opcional y contener comentarios, instrucciones de procesamiento, una declaración de tipo de documento (DTD) y, obligatoriamente, **un único elemento raíz**.
    
2. **Declaración XML (prólogo)** _(opcional)_  
    Indica versión y, habitualmente, la codificación de caracteres.
    
    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    ```
    
3. **Comentarios e instrucciones de procesamiento** _(opcionales)_
    
    - Comentario: `<!-- texto -->`
        
    - Instrucción de procesamiento: `<?target datos?>`
        
4. **Elemento raíz (root element)**  
    Es el **ancestro común** de todos los demás elementos. Debe haber **exactamente uno**. En el ejemplo: `<empleados>…</empleados>`.
    
5. **Elementos contenedores (padres) y elementos hijos**  
    Un elemento puede **contener otros elementos** (entonces es “padre” respecto de ellos) y, a su vez, ser **hijo** de su contenedor inmediato.  
    En el ejemplo: `<empleados>` es padre de múltiples elementos `<empleado>`; cada `<empleado>` es hijo de `<empleados>`.
    
6. **Elementos hermanos (siblings)**  
    Son elementos que comparten el mismo elemento padre.  
    En el ejemplo: los distintos `<empleado>` son hermanos entre sí.
    
7. **Elementos hoja (leaf nodes)**  
    No contienen otros elementos, solo **texto** (nodos de texto) o, en su caso, **atributos**.  
    En el ejemplo: `<id>`, `<nombre>`, `<dni>`, `<sueldoMax>`, `<sueldoMin>`, `<sueldoMedio>` son hojas.
    
8. **Atributos** _(opcionales)_  
    Son **pares nombre–valor** asociados a un elemento; no forman parte del contenido de texto. Se usan para **metadatos** o identificadores cuando procede.  
    Ejemplo alternativo: `<empleado id="1" dni="94738265Y">…</empleado>`.
    
9. **Espacios de nombres (namespaces)** _(opcionales)_  
    Evitan colisiones de nombres cuando se combinan vocabularios. Se declaran con `xmlns` y, opcionalmente, un **prefijo**.  
    Ejemplo: `<empleados xmlns:hr="http://ejemplo.org/hr">…</empleados>` y luego `<hr:empleado>…</hr:empleado>`.
    
10. **Contenido del elemento**
    
    - **Solo texto** (p. ej., `<nombre>María López</nombre>`).
        
    - **Solo elementos** (element-only).
        
    - **Mixto** (texto + elementos).
        
    - **Vacío** (sin contenido, a veces como etiqueta autocontenida: `<etiqueta/>`).
        

---

#### Ejemplo con elemento raíz y prólogo

```xml
<?xml version="1.0" encoding="UTF-8"?>
<empleados>
  <empleado>
    <id>1</id>
    <nombre>Diego Romero</nombre>
    <dni>94738265Y</dni>
    <sueldoMax>1966.49</sueldoMax>
    <sueldoMin>1224.74</sueldoMin>
    <sueldoMedio>1578.12</sueldoMedio>
  </empleado>
  <empleado>
    <id>2</id>
    <nombre>María López</nombre>
    <dni>12345678X</dni>
    <sueldoMax>2100.00</sueldoMax>
    <sueldoMin>1300.50</sueldoMin>
    <sueldoMedio>1700.25</sueldoMedio>
  </empleado>
</empleados>
```

> Nota: En XML bien formado debe existir **un único elemento raíz** que los agrupe (aquí, `<empleados>`).

---

#### Vista en forma de árbol (relaciones jerárquicas)

```
Documento
└── empleados                   ← Elemento raíz
    ├── empleado                ← Hijo de <empleados> (1º), hermano de otros <empleado>
    │   ├── id                  ← Hojas (texto)
    │   ├── nombre
    │   ├── dni
    │   ├── sueldoMax
    │   ├── sueldoMin
    │   └── sueldoMedio
    └── empleado                ← Hijo de <empleados> (2º)
        ├── id
        ├── nombre
        ├── dni
        ├── sueldoMax
        ├── sueldoMin
        └── sueldoMedio
```

- **Ancestro / descendiente**: `<empleados>` es ancestro de cualquier `<id>`, que a su vez es descendiente de `<empleados>`.
    
- **Padre / hijo**: cada `<empleado>` es padre de `<id>`, `<nombre>`, etc.
    
- **Hermanos**: los dos `<empleado>` son hermanos; también lo son `<id>`, `<nombre>`, `<dni>`, etc. dentro del mismo `<empleado>`.
    

---

#### Denominaciones habituales y cómo referenciar posiciones (XPath)

- **Ruta absoluta al primer DNI**: `/empleados/empleado[1]/dni`
    
- **Todos los sueldos medios**: `/empleados/empleado/sueldoMedio`
    
- **Segundo empleado por posición**: `/empleados/empleado[2]`
    
- **Todos los descendientes llamados `dni`**: `//dni`
    

---

#### Tabla-resumen de “partes” y “nombres” por posición

|Posición / rol en el árbol|Nombre habitual|Descripción breve|Ejemplo / XPath|
|---|---|---|---|
|Documento completo|Documento XML|Unidad completa que contiene todo|_(n/a)_|
|Inicio del documento|Declaración XML|Prólogo con versión/codificación|`<?xml version="1.0" encoding="UTF-8"?>`|
|Raíz|Elemento raíz|Ancestro común y único|`/empleados`|
|Contenedor intermedio|Elemento padre|Elemento que contiene hijos|`/empleados/empleado`|
|Hijo de un contenedor|Elemento hijo|Elemento contenido en un padre|`/empleados/empleado/dni`|
|Mismo nivel|Elementos hermanos|Elementos con el mismo padre|Dos `<empleado>` bajo `<empleados>`|
|Terminal sin hijos|Elemento hoja|Elemento cuyo contenido es texto|`<nombre>María López</nombre>`|
|Texto de un elemento|Nodo de texto|Cadena de caracteres dentro de un elemento|`"12345678X"` dentro de `<dni>`|
|Metadatos del elemento|Atributo|Par nombre–valor en la etiqueta de inicio|`<empleado id="2" dni="123...">`|
|Ámbito de nombres|Namespace|URI que califica nombres y evita colisiones|`xmlns:hr="http://ejemplo.org/hr"`|

---

#### Nota práctica (librería JDOM, por si se integra en el código del proyecto)

En lalibrería **JDOM 2** las estructuras anteriores se modelan con:

- `org.jdom2.Document` (documento),
    
- `org.jdom2.Element` (elementos como `empleados`, `empleado`, `dni`, etc.),
    
- `org.jdom2.Attribute` (atributos como `id`, `dni` si se usan como atributos),
    
- `org.jdom2.Text` (nodos de texto),
    
- `org.jdom2.Comment` (comentarios).
    

Esto facilita razonar y navegar por **padres/hijos/hermanos** en el árbol con métodos como `getChild`, `getChildren`, `getParentElement`, etc.

---

Con esta descripción, el lector puede identificar con precisión **qué es cada etiqueta** según su **posición jerárquica** y **cómo referenciarla** en validaciones, consultas (XPath) o código (p. ej., JDOM). Este apartado puede insertarse justo tras la presentación del formato XML y antes de los ejemplos de lectura y escritura.

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

Este ejemplo muestra cómo los datos se organizan en un formato tabular en Excel, facilitando el análisis visual y el cálculo de valores como promedios, máximos y mínimos.

