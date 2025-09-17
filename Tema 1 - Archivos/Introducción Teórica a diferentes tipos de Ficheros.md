En este documento introduciremos distintos tipos de ficheros ( TXT, CSV, JSON y XML) para aprender a leer, escribir y procesar datos. Cada uno de estos tipos de fichero tiene sus propias características y aplicaciones específicas, y es importante entender qué los hace únicos y cómo trabajar con ellos. A continuación, se presentan los conceptos fundamentales de cada tipo de fichero y su relación directa con el proyecto que implementaremos posteriormente en este tutorial.

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

A continuación se añade una explicación **jerárquica y nominal** de las partes de un documento **JSON**, usando el ejemplo (cuyo **valor raíz** es un **array** de objetos empleado) como referencia.

#### Estructura jerárquica y denominación de las partes

1. **Documento JSON**  
    Es la unidad completa. Debe contener **exactamente un valor JSON raíz** (no varios). Ese valor puede ser un **objeto** (`{…}`), un **array** (`[…]`), un **string**, un **number**, un **boolean** (`true`/`false`) o `null`.  
    _En el ejemplo, el documento completo es un **array** de empleados._
    
2. **Valor raíz (root value)**  
    Es el primer y único valor del documento.
    
    - Si es **objeto**: raíz basada en **propiedades** (pares _clave–valor_).
        
    - Si es **array**: raíz basada en **índices** (elementos en orden).  
        _En el ejemplo, el valor raíz es `[…]`._
        
3. **Tipos de valor JSON (dominio de valores)**
    
    - **Objeto**: colección **no ordenada** de pares _clave–valor_ con claves **únicas** (strings). Se escribe con `{}`.
        
    - **Array**: colección **ordenada** de valores (cualquier tipo JSON). Se escribe con `[]`.
        
    - **Primitivos**: **string** (entre comillas dobles), **number** (entero o decimal, sin comillas), **boolean** (`true`/`false`), **null**.  
        _Ejemplos del modelo: cada empleado es un **objeto**; `id` es **number**, `nombre` y `dni` son **string**, `sueldoMax/Min/Medio` son **number**._
        
4. **Objetos (propiedades, campos, pares clave–valor)**  
    Un **objeto** contiene **propiedades**. Cada propiedad tiene:
    
    - **Clave** (string) → p. ej., `"nombre"`.
        
    - **Valor** (cualquier valor JSON) → p. ej., `"María López"` o `1700.25`.  
        _En el ejemplo, cada `{ … }` dentro del array representa un empleado con propiedades `id`, `nombre`, `dni`, `sueldoMax`, `sueldoMin`, `sueldoMedio`._
        
5. **Arrays (elementos, posición, hermanos)**  
    Un **array** contiene **elementos** ordenados, accesibles por **índice** (0, 1, 2, …). Entre sí, los elementos son **hermanos** (comparten el mismo array padre).  
    _En el ejemplo, el array raíz tiene dos elementos (índices 0 y 1), ambos objetos “empleado”._
    
6. **Nodos hoja (leaf nodes) en JSON**  
    Se consideran “hoja” los valores **primitivos** (string, number, boolean, null) o los **objetos/arrays vacíos**.  
    _En el ejemplo, `"Diego Romero"` o `1578.12` son hojas._
    
7. **Anidamiento (nesting) y jerarquía**  
    La jerarquía surge al **anidar** objetos y/o arrays. Un valor contenido dentro de otro es su **hijo**; el contenedor es el **padre**.  
    _En el ejemplo, el array es padre de cada objeto empleado; cada objeto empleado es padre de sus propiedades (sus valores)._
    
8. **Restricciones de sintaxis relevantes**
    
    - **Comillas dobles obligatorias** para claves y strings.
        
    - **Sin comas finales** (no se permiten “trailing commas”).
        
    - **Sin comentarios** (JSON puro no admite `//` ni `/* … */`).
        
    - **Números**: se escriben sin comillas; pueden ser enteros o decimales con punto.
        
    - **Orden de propiedades**: no garantizado (los objetos son conceptualmente no ordenados).
        
9. **Validación y esquemas** _(opcional)_  
    JSON puede validarse con **JSON Schema** (definiendo tipos, obligatoriedad de campos, rangos, etc.), análogo al uso de DTD/XSD en XML.
    

---

#### Ejemplo y lectura conceptual

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

- **Raíz**: array `[…]`.
    
- **Elementos del array**: dos **objetos** (empleados).
    
- **Propiedades de cada objeto**: `id` (number), `nombre` (string), `dni` (string), `sueldoMax/Min/Medio` (number).
    

---

#### Vista en forma de árbol (relaciones jerárquicas)

```
Documento
└── [ ]                           ← Valor raíz: Array
    ├── [0]                       ← Elemento 0 (Objeto empleado)
    │   ├── "id": 1                 ← Propiedad (clave–valor)
    │   ├── "nombre": "Diego Romero"
    │   ├── "dni": "94738265Y"
    │   ├── "sueldoMax": 1966.49
    │   ├── "sueldoMin": 1224.74
    │   └── "sueldoMedio": 1578.12
    └── [1]                       ← Elemento 1 (Objeto empleado)
        ├── "id": 2
        ├── "nombre": "María López"
        ├── "dni": "12345678X"
        ├── "sueldoMax": 2100.00
        ├── "sueldoMin": 1300.50
        └── "sueldoMedio": 1700.25
```

- **Padre / hijo**: el array es padre de cada objeto; cada objeto es padre de los valores de sus propiedades.
    
- **Hermanos**: los objetos en posiciones `[0]` y `[1]` son hermanos; también lo son los valores de propiedades dentro del mismo objeto.
    
- **Hojas**: los números y strings de cada propiedad.
    

---

#### Denominaciones habituales y cómo referenciar posiciones (JSONPath)

- **Raíz**: `$`
    
- **Primer empleado completo**: `$[0]`
    
- **DNI del primer empleado**: `$[0].dni`
    
- **Todos los sueldos medios**: `$[*].sueldoMedio`
    
- **Filtrar por condición** (empleados con sueldoMax > 2000): `$[?(@.sueldoMax > 2000)]`
    
- **Búsqueda recursiva de un nombre de campo**: `$..dni`
    

> Nota: JSONPath es una convención práctica (análoga a XPath en XML), no forma parte del estándar JSON.

---

#### Tabla-resumen de “partes” y “nombres” por posición

|Posición / rol en el árbol|Nombre habitual|Descripción breve|Ejemplo / JSONPath|
|---|---|---|---|
|Documento completo|Documento JSON|Unidad completa con un único valor raíz|_(n/a)_|
|Raíz|Valor raíz|Primer y único valor del documento (objeto, array, primitivo o null)|`$`|
|Contenedor ordenado|Array|Colección ordenada; elementos por índice|`$[0]`, `$[1]`|
|Elemento de array|Elemento (por índice)|Valor dentro del array|`$[0]`|
|Contenedor no ordenado|Objeto|Conjunto no ordenado de pares _clave–valor_|`$[0]` (si es objeto)|
|Par clave–valor|Propiedad / campo|Clave (string) + valor (cualquier tipo JSON)|`$[0].dni`, `$[1].sueldoMedio`|
|Mismo nivel (array)|Elementos hermanos|Elementos que comparten el mismo array padre|`$[0]` y `$[1]`|
|Terminal|Nodo hoja|Valor primitivo (string, number, boolean, null) o contenedor vacío|`"María López"`, `1700.25`|

#### Problemas habituales
A continuación se listan **cinco problemas habituales** al trabajar con JSON, con una breve **mitigación** práctica en cada caso:

1. **Ausencia de validación y cambios de contrato (schema drift)**
    
    - **Problema**: entradas con campos faltantes, tipos inesperados o claves adicionales rompen el procesamiento cuando evoluciona el formato.
        
    - **Mitigación**: definir y aplicar **JSON Schema** en validación de entrada; versionar el contrato (`v1`, `v2`); en Jackson, decidir política ante campos desconocidos (`FAIL_ON_UNKNOWN_PROPERTIES` verdadero en backend interno, falso cuando se consume de terceros).
        
2. **Precisión numérica y representación de importes**
    
    - **Problema**: usar `double` provoca redondeos binarios (p. ej., 0.1 + 0.2 ≠ 0.3 exacto).
        
    - **Mitigación**: mapear a `BigDecimal` en Java y activar `USE_BIG_DECIMAL_FOR_FLOATS`; al serializar, evitar notación científica (`WRITE_BIGDECIMAL_AS_PLAIN`). En el JSON, representar los números **sin comillas**.
        
3. **Fechas y zonas horarias**
    
    - **Problema**: formatos heterogéneos (p. ej., `"01/02/2025"`) y pérdida de zona (`Z`, offset), que causan desajustes y errores de parseo.
        
    - **Mitigación**: estandarizar en **ISO-8601** (`"2025-09-17T13:00:00Z"` o con offset `+02:00`) o usar **epoch millis**; en Jackson, registrar `JavaTimeModule` y fijar formatos con `@JsonFormat`.
        
4. **JSON mal formado o supuestos incorrectos del “mundo JS”**
    
    - **Problema**: comentarios y **comas finales** no están permitidos en JSON estándar; **claves duplicadas** en un objeto suelen sobrescribirse silenciosamente; errores de codificación (no UTF-8).
        
    - **Mitigación**: usar linters/validadores; prohibir comentarios y trailing commas; en pipelines críticos, rechazar objetos con **claves duplicadas**; forzar **UTF-8** en I/O.
        
5. **Rendimiento y memoria con archivos grandes**
    
    - **Problema**: cargar todo el documento en memoria (DOM) puede ser lento o agotar la RAM.
        
    - **Mitigación**: procesar en **streaming** (Jackson `JsonParser`/`ObjectReader.readValues`), paginar colecciones, comprimir en tránsito (GZIP), y desactivar “pretty print” en producción.
        

> Consejos adicionales: acordar semántica de **`null` vs. campo ausente** (valores desconocidos frente a no aplicables) y documentarla en el schema; no depender del **orden** de propiedades de un objeto (no está garantizado).

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

#### Nota práctica (librería JDOM)

En lalibrería **JDOM 2** las estructuras anteriores se modelan con:

- `org.jdom2.Document` (documento),
    
- `org.jdom2.Element` (elementos como `empleados`, `empleado`, `dni`, etc.),
    
- `org.jdom2.Attribute` (atributos como `id`, `dni` si se usan como atributos),
    
- `org.jdom2.Text` (nodos de texto),
    
- `org.jdom2.Comment` (comentarios).
    

Esto facilita razonar y navegar por **padres/hijos/hermanos** en el árbol con métodos como `getChild`, `getChildren`, `getParentElement`, etc.

---

Con esta descripción, podemos identificar con precisión **qué es cada etiqueta** según su **posición jerárquica** y **cómo referenciarla** en validaciones, consultas (XPath) o código (p. ej., JDOM). Este apartado puede insertarse justo tras la presentación del formato XML y antes de los ejemplos de lectura y escritura.

#### Problemas habituales
A continuación se listan **cinco problemas habituales** al trabajar con XML, junto con una **mitigación** práctica para cada caso:

1. **Desajustes de esquema y validación (DTD/XSD)**
    
    - **Problema**: documentos con elementos/atributos faltantes, cardinalidades u orden incorrectos, o tipos mal declarados respecto al DTD/XSD; la evolución del contrato rompe consumidores.
        
    - **Mitigación**: definir **XSD** y validar en los límites del sistema; versionar el contrato (p. ej., namespaces versionados o `v1`, `v2`); marcar obligatorios/opcionales y valores por defecto en el esquema; usar parsers “schema-aware”.
        
2. **Namespaces y prefijos mal gestionados**
    
    - **Problema**: colisiones de nombres, pérdida del **namespace por defecto**, consultas XPath que no encuentran nodos por no registrar prefijos, serializaciones que cambian prefijos o omiten declaraciones.
        
    - **Mitigación**: trabajar siempre con parsers **namespace-aware**; declarar namespaces en el elemento raíz; en XPath/XSLT registrar y usar los **prefijos** correctos; no basarse en el nombre del prefijo (puede variar), sino en el **URI** del namespace.
        
3. **Escapado, caracteres y codificación**
    
    - **Problema**: caracteres especiales sin escapar (`&`, `<`, `>`, `"`, `'`), uso inadecuado de **CDATA** (incluye `]]>`), mezcla de codificaciones (BOM, `encoding` del prólogo), o presencia de caracteres no permitidos por XML 1.0.
        
    - **Mitigación**: serializar siempre con biblioteca (no concatenar cadenas a mano); fijar **UTF-8** y declarar `<?xml version="1.0" encoding="UTF-8"?>`; dejar que la librería haga el **escapado**; evitar CDATA salvo necesidad real (o dividir correctamente antes de `]]>`); validar que no se emiten caracteres inválidos.
        
4. **Vulnerabilidades de seguridad (XXE, expansión de entidades, SSRF)**
    
    - **Problema**: **XXE** (External Entity), **XEE** (“billion laughs”), **XInclude** o resolución de DTD remotos que permiten exfiltración de archivos, DoS o accesos externos.
        
    - **Mitigación**: **deshabilitar DOCTYPE y entidades externas** en el parser (`disallow-doctype-decl`, `external-general/parameter-entities=false`), activar **secure processing**, no resolver DTD/XInclude de orígenes no confiables, imponer límites de tamaño/profundidad y timeouts.
        
5. **Rendimiento y memoria con documentos grandes**
    
    - **Problema**: construir el árbol completo (DOM) consume mucha memoria; evaluaciones XPath amplias y transformaciones XSLT costosas; “pretty print” y redundancias aumentan el tamaño.
        
    - **Mitigación**: procesar en **streaming** (SAX/StAX) o “pull parsing” cuando sea posible; acotar el ámbito de XPath; paginar/segmentar; comprimir en tránsito/almacenamiento; reutilizar factories/transformers; evitar **pretty print** en producción si el tamaño importa.
        

> **Consejos adicionales**: acordar criterios **atributo vs. elemento** (metadatos cortos y estables como atributos; datos de negocio como elementos); documentar el tratamiento de **espacios en blanco** y **contenido mixto** (texto + elementos), pues pueden afectar a XPath y serialización.

