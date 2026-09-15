# Introducción teórica a diferentes tipos de ficheros

En este documento se presentan cuatro formatos habituales para almacenar e intercambiar datos: **TXT, CSV, JSON y XML**. El objetivo es comprender qué estructura tiene cada uno, cuándo resulta adecuado utilizarlo y cómo se puede procesar desde Java.

## Ficheros de texto plano (TXT)

Un fichero **TXT** contiene únicamente caracteres de texto. La extensión `.txt` no define por sí misma una estructura: es la aplicación la que decide cómo interpretar cada línea y cómo separar los datos.

En nuestro proyecto usamos ficheros TXT para almacenar información de empleados y empleamos **`BufferedWriter`** y **`BufferedReader`** para escribir y leer el contenido de forma secuencial.

- **Ventajas**: sencillo, legible y fácil de generar.
- **Desventajas**: no dispone de una estructura estándar y puede resultar ambiguo si no se define claramente cómo se separan los campos.

Por ejemplo:

```text
1 Diego Romero 94738265Y 1966.49 1632.31 1498.61
```

Si se utiliza un espacio como separador, hay que tener en cuenta que algunos campos, como `Diego Romero`, también pueden contener espacios. Por ello, en formatos propios suele ser preferible elegir un separador inequívoco o utilizar un formato estándar como CSV.

También debe conocerse la **codificación de caracteres** utilizada. Actualmente, **UTF-8** es la opción habitual.

---

## Archivos CSV (Comma-Separated Values)

Un fichero **CSV** representa datos tabulares: cada línea suele corresponder a un registro y sus campos se separan mediante un delimitador. Aunque el nombre hace referencia a la coma, también es frecuente utilizar el punto y coma, especialmente en determinados entornos regionales.

En Java utilizamos **Apache Commons CSV**, que permite leer y escribir estos ficheros sin tener que gestionar manualmente delimitadores, comillas y caracteres especiales.

- **Ventajas**: sencillo, compacto y ampliamente utilizado para datos tabulares.
- **Desventajas**: no representa bien estructuras jerárquicas y requiere tratar correctamente delimitadores, comillas y saltos de línea dentro de los campos.

Ejemplo:

```csv
id,nombre,dni,sueldoMax,sueldoMin,sueldoMedio
1,"Diego Romero",94738265Y,1966.49,1224.74,1578.12
2,"María López",12345678X,2100.00,1300.50,1700.25
```

Cuando un campo contiene el propio delimitador, comillas o saltos de línea, debe escribirse siguiendo las reglas del formato. Por este motivo es preferible utilizar una librería especializada en lugar de separar las cadenas manualmente con `split()`.

---

## Archivos JSON (JavaScript Object Notation)

**JSON** es un formato de texto ligero utilizado para representar e intercambiar datos estructurados. Permite combinar objetos y arrays, por lo que resulta adecuado para información jerárquica.

En Java usamos **Jackson** para convertir objetos Java a JSON (**serialización**) y JSON a objetos Java (**deserialización**).

- **Ventajas**: legible, relativamente compacto y muy utilizado en APIs y aplicaciones web.
- **Desventajas**: para datos puramente tabulares suele ocupar más que CSV y los documentos muy grandes pueden requerir procesamiento en streaming.

### Tipos de valores

JSON admite seis tipos de valores:

- **Objeto**: conjunto de propiedades escritas entre `{}`.
- **Array**: colección ordenada de valores escrita entre `[]`.
- **String**: texto entre comillas dobles.
- **Number**: número escrito sin comillas.
- **Boolean**: `true` o `false`.
- **null**: ausencia de valor.

Un documento JSON contiene **un único valor raíz**, que puede ser cualquiera de estos tipos. En la práctica, es frecuente que la raíz sea un objeto o un array.

Ejemplo:

```json
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

### Estructura jerárquica

En este ejemplo:

```text
Documento JSON
└── Array raíz
    ├── Objeto empleado [0]
    │   ├── id
    │   ├── nombre
    │   ├── dni
    │   ├── sueldoMax
    │   ├── sueldoMin
    │   └── sueldoMedio
    └── Objeto empleado [1]
        └── ...
```

Cada objeto contiene **propiedades**, formadas por un nombre y un valor. Los elementos de un array se identifican por su posición, comenzando en el índice `0`.

### Reglas básicas de sintaxis

- Las claves de los objetos y los valores de tipo string utilizan **comillas dobles**.
- Los números se escriben **sin comillas** y utilizan punto como separador decimal.
- JSON estándar no admite comentarios ni comas finales.
- Los nombres de las propiedades de un objeto deberían ser únicos. El comportamiento ante nombres duplicados puede variar entre parsers.
- No debe dependerse del orden de las propiedades de un objeto.

### Acceso mediante JSONPath

JSONPath permite expresar rutas sobre un documento JSON. No forma parte del estándar JSON, pero es una convención muy utilizada.

```text
$                    raíz
$[0]                 primer empleado
$[0].dni             DNI del primer empleado
$[*].sueldoMedio     sueldo medio de todos los empleados
```

### Aspectos prácticos

- Para cantidades monetarias en Java es preferible utilizar **`BigDecimal`** en lugar de `double` cuando se necesita precisión decimal exacta.
- Las fechas suelen representarse como strings utilizando un formato acordado, normalmente **ISO 8601**.
- Si el JSON funciona como contrato entre sistemas, puede validarse mediante **JSON Schema**.
- Para documentos muy grandes, Jackson permite procesamiento en **streaming** sin cargar todo el fichero en memoria.

---

## Archivos XML (eXtensible Markup Language)

**XML** es un formato de texto para representar información estructurada de forma jerárquica mediante etiquetas. Es más verboso que JSON, pero ofrece mecanismos maduros de validación y espacios de nombres.

En nuestro proyecto usamos **JDOM 2** para crear, recorrer, leer y escribir documentos XML desde Java.

- **Ventajas**: estructura jerárquica clara, soporte de atributos, namespaces y validación mediante DTD o XSD.
- **Desventajas**: sintaxis más extensa y normalmente mayor tamaño que JSON o CSV.

### Documento XML bien formado

Todo documento XML debe tener **un único elemento raíz** que contenga al resto de elementos.

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

### Partes principales

- **Declaración XML**: `<?xml version="1.0" encoding="UTF-8"?>`. Es opcional, aunque resulta útil para indicar versión y codificación.
- **Elemento raíz**: elemento único que contiene al resto. En el ejemplo, `<empleados>`.
- **Elemento padre**: contiene otros elementos.
- **Elemento hijo**: está contenido directamente en otro elemento.
- **Elementos hermanos**: comparten el mismo elemento padre.
- **Nodo de texto**: contenido textual de un elemento.
- **Atributo**: información asociada a un elemento dentro de su etiqueta de apertura.

Por ejemplo, el identificador también podría representarse como atributo:

```xml
<empleado id="1">
  <nombre>Diego Romero</nombre>
</empleado>
```

### Estructura jerárquica

```text
Documento XML
└── empleados
    ├── empleado
    │   ├── id
    │   ├── nombre
    │   ├── dni
    │   ├── sueldoMax
    │   ├── sueldoMin
    │   └── sueldoMedio
    └── empleado
        └── ...
```

### Acceso mediante XPath

**XPath** permite seleccionar elementos y valores dentro de un documento XML.

```text
/empleados/empleado[1]/dni     DNI del primer empleado
/empleados/empleado[2]         segundo empleado
/empleados/empleado/sueldoMedio todos los sueldos medios
//dni                           todos los elementos <dni>
```

> En XPath, las posiciones comienzan en `1`, a diferencia de los índices habituales de arrays en Java y JSONPath, que comienzan en `0`.

### Validación, namespaces y seguridad

Un XML **bien formado** cumple las reglas sintácticas de XML. Además, puede considerarse **válido** si cumple un esquema definido mediante **DTD** o **XSD**.

Los **namespaces** (`xmlns`) permiten distinguir elementos procedentes de vocabularios diferentes y evitar conflictos de nombres.

Al procesar XML procedente de fuentes no confiables deben deshabilitarse las **entidades externas** y otras características innecesarias del parser para evitar vulnerabilidades como **XXE**. Para documentos muy grandes puede utilizarse procesamiento en streaming mediante **SAX** o **StAX** en lugar de construir todo el árbol en memoria.

En JDOM 2, las clases principales que utilizaremos son:

- `Document`: documento XML completo.
- `Element`: elementos del documento.
- `Attribute`: atributos.
- `Text`: nodos de texto.

---

## Comparación general

| Formato | Estructura | Uso adecuado | Tecnología utilizada en Java |
|---|---|---|---|
| **TXT** | Definida por la aplicación | Datos sencillos y ficheros de texto propios | `BufferedReader` / `BufferedWriter` |
| **CSV** | Tabular | Filas y columnas, importación y exportación de datos | Apache Commons CSV |
| **JSON** | Jerárquica | APIs, intercambio de datos y objetos estructurados | Jackson |
| **XML** | Jerárquica | Documentos estructurados, validación y sistemas que utilizan esquemas XML | JDOM 2 |

La elección del formato depende principalmente de la **estructura de los datos** y del sistema con el que deban intercambiarse. No existe un formato mejor en todos los casos: CSV es muy apropiado para tablas, JSON y XML para estructuras jerárquicas y TXT para contenidos sencillos cuyo formato controla la propia aplicación.
