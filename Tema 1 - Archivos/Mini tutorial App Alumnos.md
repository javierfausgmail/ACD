### Introducción

Vamos a crear una aplicación en Java que trabaja con archivos de varios formatos, como TXT, CSV, Excel, XML y JSON, y lo que hace básicamente es procesar datos de empleados. La idea es que, según el archivo que le des, la aplicación pueda leer la información de los empleados, procesarla y luego sacar un resumen tanto por pantalla como en otro archivo en el mismo formato que el de entrada.

El dataset que usamos digamos que tiene 25 empleados. Cada uno tiene su `id`, nombre, DNI y los sueldos de los 12 meses del año. Por ejemplo, si abres el archivo TXT o CSV, verás algo así como ‘1 Juan Pérez 12345678A 1200.50 1300.75...’ y así hasta que ves los 12 sueldos. La aplicación lee todos esos datos y luego calcula cosas útiles, como el sueldo medio de cada empleado, el sueldo máximo y el sueldo mínimo, tanto por empleado como en conjunto para todos los empleados.

Lo interesante es que se pueda procesar archivos de distintos tipos sin tener que modificar el código base, por eso vamos a hacer que que la lógica de lectura y escritura esté separada para cada formato. Por ejemplo, si metes un archivo Excel, la aplicación sabe cómo leerlo, y lo mismo pasa si metes un archivo JSON o XML. Además, cuando terminas de procesar, puedes guardar los resultados en el mismo formato del archivo de entrada. Así que si le das un archivo CSV, el archivo de salida será otro CSV pero con los resultados procesados.

El menú es muy simple, te pregunta qué tipo de archivo quieres procesar (TXT, CSV, Excel, XML o JSON), luego le das la ruta del archivo que quieres leer, y la aplicación lo procesa todo. Al final te saca por pantalla un resumen con el sueldo medio de todos los empleados, el sueldo más alto y el más bajo. Luego te pide una ruta para guardar el archivo de salida, y lo guarda con los datos procesados, incluidos los sueldos medios de cada empleado.

En resumen, lo que hace la aplicación es:
1. Leer un archivo con datos de empleados.
2. Procesar esos datos para calcular estadísticas como sueldos medios, máximos y mínimos.
3. Mostrar el resumen por consola.
4. Guardar los resultados en un archivo del mismo formato que el de entrada.

Todo está hecho de manera modular y siguiendo principios de programación orientada a objetos. Por ejemplo, tenemos una clase `Empleado` que se encarga de representar a cada empleado y de calcular su sueldo medio, máximo y mínimo. Luego tenemos clases lectoras y escritoras separadas para cada tipo de archivo, como `LectorArchivoTXT`, `LectorArchivoCSV`, `EscritorArchivoExcel`, etc. Así que aunque el formato de entrada cambie, la lógica de procesamiento siempre es la misma y no te tienes que preocupar por nada.

Este enfoque es que es muy útil si tienes que trabajar con diferentes formatos de archivo y no quieres estar cambiando el código cada vez.


Cómo lo vamos a hacer, paso a paso:

1. **Configuración del proyecto con Maven**: Creación del archivo `pom.xml` y estructura básica del proyecto.
2. **Modelado de la clase `Empleado`**: Implementación de la clase siguiendo las mejores prácticas de POO.
3. **Creación del dataset**: Generación de los 25 empleados con sus datos y sueldos mensuales.
4. **Lectura y escritura de archivos TXT y CSV**: Manejo de archivos de texto plano y CSV.
5. **Lectura y escritura de archivos Excel**: Uso de librerías para manipular archivos Excel.
6. **Lectura y escritura de archivos XML**: Procesamiento de archivos XML.
7. **Lectura y escritura de archivos JSON**: Manejo de archivos JSON.
8. **Implementación del menú de selección**: Creación de la interfaz para elegir el tipo de archivo.
9. **Procesamiento y cálculo de estadísticas**: Cálculo de sueldos medios, máximos y mínimos.
10. **Generación de la salida y sumarios**: Creación de los archivos de salida y visualización en consola.


### Paso 1: Configuración del Proyecto con Maven

Para comenzar, vamos a crear un proyecto Java utilizando Maven como gestor de dependencias. Maven nos permitirá gestionar fácilmente las bibliotecas externas que vamos a necesitar para leer y escribir en diferentes formatos de archivo (CSV, Excel, XML, JSON, etc.). Este paso incluye la configuración básica del archivo `pom.xml`, que será clave para agregar las dependencias necesarias a lo largo del desarrollo del tutorial.

#### 1.1. Crear la estructura del proyecto

Primero, crearemos una estructura de proyecto básica para Maven:

```bash
sumariosalarios
│
├── src
│   ├── main
│   │   ├── java
│   │   └── resources
│   └── test
│       └── java
├── pom.xml
```

En la carpeta `src/main/java`, colocaremos el código fuente de nuestra aplicación, y en `src/main/resources` podemos colocar archivos de configuración o datos adicionales que utilicemos a lo largo del proyecto.

Puedes utilizar tu IDE para generar toda esta parte por ti.

#### 1.2. Crear el archivo `pom.xml`

El archivo `pom.xml` es el descriptor de Maven que gestionará las dependencias del proyecto. Aquí tienes un ejemplo inicial de cómo podría configurarse para este proyecto:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>es.fempa.acd.sumariosalarios</groupId>
    <artifactId>sumarios-salarios</artifactId>
    <version>1.0-SNAPSHOT</version>
    <name>Sumario Salarios</name>
    <description>Aplicación para gestionar información de empleados y procesar archivos de diferentes formatos</description>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
    </properties>

    <dependencies>
        <!-- Dependencia para el manejo de archivos CSV -->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-csv</artifactId>
            <version>1.9.0</version>
        </dependency>

        <!-- Dependencia para el manejo de archivos Excel -->
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi</artifactId>
            <version>5.2.3</version>
        </dependency>
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml</artifactId>
            <version>5.2.3</version>
        </dependency>

        <!-- Dependencia para el manejo de archivos XML -->
        <dependency>
            <groupId>org.jdom</groupId>
            <artifactId>jdom2</artifactId>
            <version>2.0.6</version>
        </dependency>

        <!-- Dependencia para el manejo de archivos JSON -->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.15.0</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <source>17</source>
                    <target>17</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

#### Explicación de las dependencias:
- **commons-csv**: Para leer y escribir archivos CSV.
- **Apache POI**: Para leer y escribir archivos Excel (formato `.xls` y `.xlsx`).
- **JDOM**: Para trabajar con archivos XML.
- **Jackson**: Para leer y escribir archivos JSON.

Este archivo `pom.xml` establece Java 17 como versión de compilación y agrega las dependencias para trabajar con los distintos formatos de archivo que procesaremos más adelante.

#### 1.3. Archivo de configuración de la aplicación (opcional)

Podemos crear un archivo de configuración en `src/main/resources` para almacenar la ruta predeterminada donde se encontrarán los archivos que vamos a procesar. Este archivo será opcional pero útil si queremos que el directorio de archivos sea configurable sin modificar el código.

Creamos un archivo llamado `application.properties` en la carpeta `resources`:

```properties
# Ruta del directorio donde se encuentran los archivos a procesar
app.filepath=/ruta/al/directorio/de/archivos
```

Este archivo podrá ser cargado en nuestro código para acceder a configuraciones dinámicamente. *Investiga sobre los [archivos de propiedades](https://youtu.be/hyimmrepPhE?si=5r1OSSi6Kc9ibsuw) en Java y como utilizarlos en tus programas.*


### Paso 2: Implementación de la Clase `Empleado`

En este paso vamos a diseñar la clase `Empleado`, que servirá como modelo para los datos de nuestros empleados. Cada empleado tendrá un `id`, un `nombre`, un `DNI` (documento de identidad), y una lista con sus sueldos mensuales a lo largo de los 12 meses del año. También implementaremos algunos métodos necesarios para calcular el sueldo medio y otros detalles que necesitaremos más adelante.

#### 2.1. Definir la clase `Empleado`

A continuación, creamos la clase `Empleado` siguiendo los principios de la programación orientada a objetos (POO):

```java
package es.fempa.acd.sumariosalarios;

import java.util.List;

public class Empleado {

    private int id;
    private String nombre;
    private String dni;
    private List<Double> sueldosMensuales;

    // Constructor
    public Empleado(int id, String nombre, String dni, List<Double> sueldosMensuales) {
        this.id = id;
        this.nombre = nombre;
        this.dni = dni;
        this.sueldosMensuales = sueldosMensuales;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public List<Double> getSueldosMensuales() {
        return sueldosMensuales;
    }

    public void setSueldosMensuales(List<Double> sueldosMensuales) {
        this.sueldosMensuales = sueldosMensuales;
    }

 // Método para calcular el sueldo medio
    public double calcularSueldoMedio() {
        //hacer
    }

    // Método para obtener el sueldo máximo
    public double obtenerSueldoMaximo() {
    //hacer
    }

    // Método para obtener el sueldo mínimo
    public double obtenerSueldoMinimo() {
        //hacer
    }

    // Método toString para mostrar información del empleado
    @Override
    public String toString() {
       //hacer
    }
}
```

#### Explicación del código:

1. **Atributos**: La clase `Empleado` tiene cuatro atributos: 
   - `id`: un identificador único para cada empleado.
   - `nombre`: el nombre del empleado.
   - `dni`: el documento de identidad.
   - `sueldosMensuales`: una lista de los sueldos mensuales (12 valores, uno por cada mes).

2. **Constructor**: El constructor inicializa los atributos con los valores que se pasen al crear una instancia de `Empleado`.

3. **Métodos de cálculo**:
   - `calcularSueldoMedio()`: Calcula el sueldo medio de los 12 meses usando la API de Streams de Java.
   - `obtenerSueldoMaximo()` y `obtenerSueldoMinimo()`: Devuelven el sueldo máximo y mínimo, respectivamente, usando Streams preferiblemente.

4. **Método `toString()`**: Este método sobrescribe el método estándar de Java para proporcionar una representación en cadena de un objeto `Empleado`. Esto será útil para mostrar la información de los empleados en la consola o en los archivos de salida.

#### 2.2. Posible estructura de la lista de sueldos

Cada empleado tendrá 12 sueldos mensuales, por lo que al crear los empleados, pasaremos una lista de tipo `List<Double>`. Ejemplo:

```java
List<Double> sueldos = Arrays.asList(1200.0, 1250.0, 1300.0, 1400.0, 1350.0, 1280.0, 1450.0, 1500.0, 1470.0, 1600.0, 1550.0, 1490.0);
Empleado empleado = new Empleado(1, "Juan Pérez", "12345678A", sueldos);
```


### Paso 3: Creación del Dataset

En este paso vamos a crear un dataset con 25 empleados, cada uno con un `id`, un `nombre`, un `DNI` y una lista de sus sueldos mensuales. Este dataset lo utilizaremos posteriormente para leer y escribir en diferentes formatos de archivo.

#### 3.1. Crear el dataset de empleados

Vamos a generar una lista de 25 empleados de forma manual y asignar valores de sueldo de manera aleatoria para hacer el ejemplo más realista. Utilizaremos una clase llamada `DataSetEmpleados` que contendrá un método estático que generará el listado de empleados.

```java
package es.fempa.acd.sumariosalarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DataSetEmpleados {

    // Método estático que genera la lista de empleados
    public static List<Empleado> generarEmpleados() {
        List<Empleado> empleados = new ArrayList<>();
        Random random = new Random();
        
        // Nombres y DNIs de ejemplo
        String[] nombres = {"Juan Pérez", "María García", "Pedro Rodríguez", "Laura Sánchez", "Javier López",
                            "Ana Fernández", "Miguel Torres", "Lucía Martín", "Raúl González", "Carmen Ruiz",
                            "Sergio Hernández", "Sofía Morales", "David Castillo", "Isabel Ortega", "Manuel Vega",
                            "Paula Rojas", "Rubén Giménez", "Adriana Ramos", "José Ibáñez", "Claudia Pardo",
                            "Vicente Varela", "Andrea Gutiérrez", "Fernando Serrano", "Beatriz Díaz", "Diego Romero"};

        String[] dnis = {"12345678A", "87654321B", "12398745C", "87451236D", "74951238E", "98234567F", "34567821G", 
                         "54872345H", "67543218I", "32458769J", "82736451K", "92748365L", "43658729M", "98237465N", 
                         "12387594O", "76543218P", "12983756Q", "67834512R", "98237645S", "78452139T", 
                         "92387546U", "34827619V", "23879456W", "87236451X", "94738265Y"};
        
        // Generar 25 empleados
        for (int i = 0; i < 25; i++) {
            List<Double> sueldosMensuales = generarSueldosAleatorios(random);
            Empleado empleado = new Empleado(i + 1, nombres[i], dnis[i], sueldosMensuales);
            empleados.add(empleado);
        }
        
        return empleados;
    }

    // Método auxiliar para generar sueldos aleatorios entre 1200 y 2000 euros
    private static List<Double> generarSueldosAleatorios(Random random) {
        List<Double> sueldosMensuales = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            double sueldo = 1200 + (800 * random.nextDouble()); // Generar sueldos entre 1200 y 2000
            sueldosMensuales.add(sueldo);
        }
        return sueldosMensuales;
    }

  // Métodos para escribir el dataset (datos generados) en diferentes formatos 
    public static void escribirDatasetTXT(String rutaArchivo, List<Empleado> empleados) throws IOException {
        EscritorArchivoTXT.escribirDatosGenerados(rutaArchivo, empleados);
    }

    public static void escribirDatasetCSV(String rutaArchivo, List<Empleado> empleados) throws IOException {
        EscritorArchivoCSV.escribirDatosGenerados(rutaArchivo, empleados);
    }

    public static void escribirDatasetExcel(String rutaArchivo, List<Empleado> empleados) throws IOException {
        EscritorArchivoExcel.escribirDatosGenerados(rutaArchivo, empleados);
    }

    public static void escribirDatasetXML(String rutaArchivo, List<Empleado> empleados) throws IOException {
        EscritorArchivoXML.escribirDatosGenerados(rutaArchivo, empleados);
    }

    public static void escribirDatasetJSON(String rutaArchivo, List<Empleado> empleados) throws IOException {
        EscritorArchivoJSON.escribirDatosGenerados(rutaArchivo, empleados);
    }

}
```

#### Explicación del código:

1. **Clase `DataSetEmpleados`**: Contiene un método estático `generarEmpleados()` que devuelve una lista de objetos `Empleado`. Generamos 25 empleados utilizando nombres y DNIs de ejemplo.

2. **Generación de sueldos aleatorios**: El método `generarSueldosAleatorios()` utiliza la clase `Random` para generar 12 sueldos mensuales aleatorios para cada empleado, en un rango entre 1200 y 2000 euros.

3. **Estructura del dataset**: Cada empleado tiene un `id` único, un `nombre`, un `DNI` y una lista con 12 sueldos mensuales.

#### 3.2. Mostrar el dataset generado

Si queremos asegurarnos de que el dataset se está generando correctamente, podemos imprimirlo en consola utilizando un bucle para recorrer la lista de empleados y llamar al método `toString()` de la clase `Empleado`:

```java
public class Main {
    public static void main(String[] args) {
        List<Empleado> empleados = DataSetEmpleados.generarEmpleados();
        
        // Imprimir cada empleado en consola
        empleados.forEach(System.out::println);
    }
}
```

Al ejecutar este código, obtendremos la lista de empleados con el cálculo del sueldo medio para cada uno. Por ejemplo:

```
Empleado{id=1, nombre='Juan Pérez', dni='12345678A', sueldo medio=1567.43}
Empleado{id=2, nombre='María García', dni='87654321B', sueldo medio=1412.87}
Empleado{id=3, nombre='Pedro Rodríguez', dni='12398745C', sueldo medio=1690.34}
...
```


### Paso 4: Lectura y Escritura de Archivos TXT y CSV

En este paso, implementaremos la lógica para leer y escribir archivos de texto plano (TXT) y CSV. Vamos a empezar con la lectura de estos archivos para obtener la información de los empleados y luego implementaremos la funcionalidad para escribir los resultados en estos formatos. Utilizaremos la clase `Empleado` que creamos anteriormente para procesar los datos.

#### 4.1. Lectura de archivos TXT

Primero, vamos a implementar la funcionalidad para leer los datos de empleados desde un archivo de texto plano (TXT). El formato será sencillo: cada empleado tendrá una línea con su `id`, `nombre`, `dni` y 12 sueldos mensuales separados por espacios o comas.

##### Ejemplo de archivo TXT:

```
1 Juan Pérez 12345678A 1200.50 1300.75 1400.00 1500.00 1600.50 1700.25 1800.30 1900.00 2000.00 1500.60 1400.75 1350.00
2 María García 87654321B 1100.00 1150.75 1200.50 1250.00 1300.75 1350.50 1400.75 1450.00 1500.75 1550.50 1600.75 1650.00
...
```

##### Código para leer un archivo TXT:

```java
package es.fempa.acd.sumariosalarios;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LectorArchivoTXT {

    public static List<Empleado> leerArchivoTXT(String rutaArchivo) throws IOException {
        List<Empleado> empleados = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Dividir la línea en los datos correspondientes
                String[] datos = linea.split(" ");
                
                int id = Integer.parseInt(datos[0]);
                String nombre = datos[1] + " " + datos[2];
                String dni = datos[3];
                List<Double> sueldosMensuales = new ArrayList<>();
                
                // Leer los 12 sueldos
                for (int i = 4; i < datos.length; i++) {
                    sueldosMensuales.add(Double.parseDouble(datos[i]));
                }
                
                // Crear un objeto Empleado y agregarlo a la lista
                Empleado empleado = new Empleado(id, nombre, dni, sueldosMensuales);
                empleados.add(empleado);
            }
        }
        
        return empleados;
    }
}
```

#### Explicación del código:
1. **BufferedReader**: Se utiliza para leer el archivo línea por línea.
2. **División de la línea**: La línea se divide en sus componentes (`id`, `nombre`, `dni`, sueldos) utilizando el método `split()` con el espacio como delimitador.
3. **Parseo de datos**: Se convierten los datos necesarios a los tipos correspondientes (`int`, `double`).
4. **Creación de empleados**: A partir de los datos obtenidos, se crean instancias de la clase `Empleado` y se agregan a una lista.

#### 4.2. Escritura de archivos TXT

Ahora, vamos a escribir los datos procesados (incluyendo los sueldos promedios) de nuevo en un archivo de texto plano, siguiendo un formato similar.

##### Código para escribir en un archivo TXT:

```java
package es.fempa.acd.sumariosalarios;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class EscritorArchivoTXT {

    public static void escribirArchivoTXT(String rutaArchivo, List<Empleado> empleados) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (Empleado empleado : empleados) {
                // Formatear la salida como id nombre dni sueldo medio sueldos mensuales
                bw.write(empleado.getId() + " " + empleado.getNombre() + " " + empleado.getDni() + " " +
                         empleado.calcularSueldoMedio());
                
                for (Double sueldo : empleado.getSueldosMensuales()) {
                    bw.write(" " + sueldo);
                }
                
                bw.newLine(); // Saltar a la siguiente línea
            }
        }
    }
    
 
    public static void escribirDatosGenerados(String rutaArchivo, List<Empleado> empleados) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (Empleado empleado : empleados) {
                // Escribir id, nombre, dni y sueldos mensuales
                bw.write(empleado.getId() + " " + empleado.getNombre() + " " + empleado.getDni());

                for (Double sueldo : empleado.getSueldosMensuales()) {
                    bw.write(" " + sueldo);
                }

                bw.newLine();
            }
        }
    }

}
```

#### 4.3. Lectura de archivos CSV

Para los archivos CSV, utilizaremos la librería **commons-csv** que hemos incluido en el `pom.xml`. El formato de archivo CSV será similar al TXT pero con los campos separados por comas.

##### Ejemplo de archivo CSV:

```
1,Juan Pérez,12345678A,1200.50,1300.75,1400.00,1500.00,1600.50,1700.25,1800.30,1900.00,2000.00,1500.60,1400.75,1350.00
2,María García,87654321B,1100.00,1150.75,1200.50,1250.00,1300.75,1350.50,1400.75,1450.00,1500.75,1550.50,1600.75,1650.00
```

##### Código para leer un archivo CSV:

```java
package es.fempa.acd.sumariosalarios;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LectorArchivoCSV {

    public static List<Empleado> leerArchivoCSV(String rutaArchivo) throws IOException {
        List<Empleado> empleados = new ArrayList<>();
        
        try (CSVParser csvParser = new CSVParser(new FileReader(rutaArchivo), CSVFormat.DEFAULT)) {
            for (CSVRecord record : csvParser) {
                int id = Integer.parseInt(record.get(0));
                String nombre = record.get(1) + " " + record.get(2);
                String dni = record.get(3);
                
                List<Double> sueldosMensuales = new ArrayList<>();
                for (int i = 4; i < record.size(); i++) {
                    sueldosMensuales.add(Double.parseDouble(record.get(i)));
                }
                
                Empleado empleado = new Empleado(id, nombre, dni, sueldosMensuales);
                empleados.add(empleado);
            }
        }
        
        return empleados;
    }
}
```

#### 4.4. Escritura de archivos CSV

Ahora, escribimos los datos procesados de nuevo en un archivo CSV.

##### Código para escribir en un archivo CSV:

```java
package es.fempa.acd.sumariosalarios;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class EscritorArchivoCSV {

    public static void escribirArchivoCSV(String rutaArchivo, List<Empleado> empleados) throws IOException {
        try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(rutaArchivo), CSVFormat.DEFAULT)) {
            for (Empleado empleado : empleados) {
                // Escribir id, nombre, dni, sueldo medio y sueldos mensuales
                csvPrinter.print(empleado.getId());
                csvPrinter.print(empleado.getNombre());
                csvPrinter.print(empleado.getDni());
                csvPrinter.print(empleado.calcularSueldoMedio());
                
                for (Double sueldo : empleado.getSueldosMensuales()) {
                    csvPrinter.print(sueldo);
                }
                
                csvPrinter.println();
            }
        }
    }

	 public static void escribirDatosGenerados(String rutaArchivo, List<Empleado> empleados) throws IOException {
        try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(rutaArchivo), CSVFormat.DEFAULT)) {
            for (Empleado empleado : empleados) {
                csvPrinter.print(empleado.getId());
                csvPrinter.print(empleado.getNombre());
                csvPrinter.print(empleado.getDni());

                for (Double sueldo : empleado.getSueldosMensuales()) {
                    csvPrinter.print(sueldo);
                }

                csvPrinter.println();
            }
        }
    }

}
```


### Paso 5: Lectura y Escritura de Archivos Excel

En este paso, vamos a implementar la funcionalidad para leer y escribir archivos Excel (tanto en formato `.xls` como `.xlsx`) utilizando la librería **Apache POI**, que es muy popular para manejar archivos Excel en Java. La estructura del archivo Excel será similar a la de los archivos CSV y TXT, pero adaptada al formato de hojas de cálculo.

#### 5.1. Lectura de archivos Excel

Primero, vamos a implementar la funcionalidad para leer un archivo Excel. Este archivo contendrá las mismas columnas que los archivos anteriores: `id`, `nombre`, `dni` y los 12 sueldos mensuales.

##### Ejemplo de archivo Excel:

| id  | nombre        | dni        | sueldo1 | sueldo2 | sueldo3 | ... | sueldo12 |
|-----|---------------|------------|---------|---------|---------|-----|----------|
| 1   | Juan Pérez    | 12345678A  | 1200.50 | 1300.75 | 1400.00 | ... | 1350.00  |
| 2   | María García  | 87654321B  | 1100.00 | 1150.75 | 1200.50 | ... | 1650.00  |

##### Código para leer un archivo Excel:

```java
package es.fempa.acd.sumariosalarios;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LectorArchivoExcel {

    public static List<Empleado> leerArchivoExcel(String rutaArchivo) throws IOException {
        List<Empleado> empleados = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(rutaArchivo);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    // Saltar la primera fila si es el encabezado
                    continue;
                }
                
                int id = (int) row.getCell(0).getNumericCellValue();
                String nombre = row.getCell(1).getStringCellValue() + " " + row.getCell(2).getStringCellValue();
                String dni = row.getCell(3).getStringCellValue();
                
                List<Double> sueldosMensuales = new ArrayList<>();
                for (int i = 4; i < 16; i++) {
                    sueldosMensuales.add(row.getCell(i).getNumericCellValue());
                }
                
                Empleado empleado = new Empleado(id, nombre, dni, sueldosMensuales);
                empleados.add(empleado);
            }
        }
        
        return empleados;
    }
}
```

#### Explicación del código:
1. **FileInputStream**: Se utiliza para leer el archivo Excel desde el disco.
2. **Workbook y Sheet**: Utilizamos la clase `Workbook` de Apache POI para representar el archivo Excel, y `Sheet` para acceder a una hoja de cálculo específica (en este caso, la primera).
3. **Row y Cell**: Cada fila (`Row`) representa un empleado, y cada celda (`Cell`) contiene los datos correspondientes (`id`, `nombre`, `dni` y sueldos).
4. **Ciclo for**: Iteramos por las filas y celdas de la hoja para leer los datos y almacenarlos en objetos `Empleado`.

#### 5.2. Escritura de archivos Excel

Ahora, implementaremos la funcionalidad para escribir los datos de los empleados en un archivo Excel, incluyendo el cálculo del sueldo medio.

##### Código para escribir en un archivo Excel:

```java
package es.fempa.acd.sumariosalarios;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class EscritorArchivoExcel {

    public static void escribirArchivoExcel(String rutaArchivo, List<Empleado> empleados) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Empleados");

        // Crear fila de encabezados
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Nombre");
        headerRow.createCell(2).setCellValue("DNI");
        headerRow.createCell(3).setCellValue("Sueldo Medio");
        for (int i = 1; i <= 12; i++) {
            headerRow.createCell(3 + i).setCellValue("Sueldo " + i);
        }

        // Escribir datos de empleados
        int rowNum = 1;
        for (Empleado empleado : empleados) {
            Row row = sheet.createRow(rowNum++);
            
            row.createCell(0).setCellValue(empleado.getId());
            row.createCell(1).setCellValue(empleado.getNombre());
            row.createCell(2).setCellValue(empleado.getDni());
            row.createCell(3).setCellValue(empleado.calcularSueldoMedio());
            
            for (int i = 0; i < empleado.getSueldosMensuales().size(); i++) {
                row.createCell(4 + i).setCellValue(empleado.getSueldosMensuales().get(i));
            }
        }

        // Escribir el archivo
        try (FileOutputStream fos = new FileOutputStream(rutaArchivo)) {
            workbook.write(fos);
        }
        
        workbook.close();
    }


    public static void escribirDatosGenerados(String rutaArchivo, List<Empleado> empleados) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Empleados");

        // Crear fila de encabezados
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Nombre");
        headerRow.createCell(2).setCellValue("DNI");
        for (int i = 1; i <= 12; i++) {
            headerRow.createCell(2 + i).setCellValue("Sueldo " + i);
        }

        // Escribir datos de empleados
        int rowNum = 1;
        for (Empleado empleado : empleados) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(empleado.getId());
            row.createCell(1).setCellValue(empleado.getNombre());
            row.createCell(2).setCellValue(empleado.getDni());

            for (int i = 0; i < empleado.getSueldosMensuales().size(); i++) {
                row.createCell(3 + i).setCellValue(empleado.getSueldosMensuales().get(i));
            }
        }

        // Escribir el archivo
        try (FileOutputStream fos = new FileOutputStream(rutaArchivo)) {
            workbook.write(fos);
        }

        workbook.close();
    }

}
```

#### Explicación del código:
1. **Workbook y Sheet**: Creamos un nuevo archivo Excel (`Workbook`) y una hoja de cálculo (`Sheet`).
2. **HeaderRow**: Creamos la fila de encabezado con los nombres de las columnas (`ID`, `Nombre`, `DNI`, `Sueldo Medio`, `Sueldo 1`, `Sueldo 2`, etc.).
3. **Iteración sobre empleados**: Iteramos sobre la lista de empleados y escribimos cada uno de sus atributos en las celdas correspondientes.
4. **FileOutputStream**: Utilizamos `FileOutputStream` para escribir el archivo en disco.


### Paso 6: Lectura y Escritura de Archivos XML

En este paso, implementaremos la funcionalidad para leer y escribir archivos XML utilizando la librería **JDOM2**. Los archivos XML serán otro formato en el que representaremos los datos de los empleados, incluyendo su `id`, `nombre`, `dni` y sus sueldos mensuales.

#### 6.1. Lectura de archivos XML

Vamos a implementar primero la funcionalidad para leer un archivo XML que contenga la información de los empleados. A continuación, un ejemplo del formato XML que utilizaremos:

##### Ejemplo de archivo XML:

```xml
<empleados>
    <empleado>
        <id>1</id>
        <nombre>Juan Pérez</nombre>
        <dni>12345678A</dni>
        <sueldos>
            <sueldo>1200.50</sueldo>
            <sueldo>1300.75</sueldo>
            <sueldo>1400.00</sueldo>
            <!-- ... -->
        </sueldos>
    </empleado>
    <empleado>
        <id>2</id>
        <nombre>María García</nombre>
        <dni>87654321B</dni>
        <sueldos>
            <sueldo>1100.00</sueldo>
            <sueldo>1150.75</sueldo>
            <!-- ... -->
        </sueldos>
    </empleado>
</empleados>
```

##### Código para leer un archivo XML:

```java
package es.fempa.acd.sumariosalarios;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LectorArchivoXML {

    public static List<Empleado> leerArchivoXML(String rutaArchivo) throws Exception {
        List<Empleado> empleados = new ArrayList<>();
        SAXBuilder saxBuilder = new SAXBuilder();
        File archivo = new File(rutaArchivo);
        Document documento = saxBuilder.build(archivo);
        Element rootElement = documento.getRootElement(); // "empleados"

        for (Element empleadoElement : rootElement.getChildren("empleado")) {
            int id = Integer.parseInt(empleadoElement.getChildText("id"));
            String nombre = empleadoElement.getChildText("nombre");
            String dni = empleadoElement.getChildText("dni");

            List<Double> sueldosMensuales = new ArrayList<>();
            for (Element sueldoElement : empleadoElement.getChild("sueldos").getChildren("sueldo")) {
                sueldosMensuales.add(Double.parseDouble(sueldoElement.getText()));
            }

            Empleado empleado = new Empleado(id, nombre, dni, sueldosMensuales);
            empleados.add(empleado);
        }

        return empleados;
    }
}
```

#### Explicación del código:

1. **SAXBuilder y Document**: Utilizamos `SAXBuilder` para construir el documento XML y obtener su representación en memoria.
2. **RootElement**: Extraemos el elemento raíz (`empleados`) que contiene todos los elementos `empleado`.
3. **Iteración**: Iteramos sobre cada elemento `empleado`, extraemos sus datos (`id`, `nombre`, `dni`) y sueldos (`sueldos/sueldo`).
4. **Creación de empleados**: Creamos una instancia de la clase `Empleado` con los datos extraídos y la agregamos a la lista de empleados.

#### 6.2. Escritura de archivos XML

Ahora vamos a escribir los datos de los empleados en un archivo XML con la misma estructura que vimos anteriormente.

##### Código para escribir en un archivo XML:

```java
package es.fempa.acd.sumariosalarios;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class EscritorArchivoXML {

    public static void escribirArchivoXML(String rutaArchivo, List<Empleado> empleados) throws IOException {
        Element rootElement = new Element("empleados");
        Document documento = new Document(rootElement);

        for (Empleado empleado : empleados) {
            Element empleadoElement = new Element("empleado");

            Element idElement = new Element("id").setText(String.valueOf(empleado.getId()));
            Element nombreElement = new Element("nombre").setText(empleado.getNombre());
            Element dniElement = new Element("dni").setText(empleado.getDni());

            Element sueldosElement = new Element("sueldos");
            for (Double sueldo : empleado.getSueldosMensuales()) {
                sueldosElement.addContent(new Element("sueldo").setText(String.valueOf(sueldo)));
            }

            empleadoElement.addContent(idElement);
            empleadoElement.addContent(nombreElement);
            empleadoElement.addContent(dniElement);
            empleadoElement.addContent(sueldosElement);

            rootElement.addContent(empleadoElement);
        }

        // Guardar el archivo XML
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.setFormat(Format.getPrettyFormat());
        try (FileWriter fileWriter = new FileWriter(rutaArchivo)) {
            xmlOutputter.output(documento, fileWriter);
        }
    }

    public static void escribirDatosGenerados(String rutaArchivo, List<Empleado> empleados) throws IOException {
        Element rootElement = new Element("empleados");
        Document documento = new Document(rootElement);

        for (Empleado empleado : empleados) {
            Element empleadoElement = new Element("empleado");

            empleadoElement.addContent(new Element("id").setText(String.valueOf(empleado.getId())));
            empleadoElement.addContent(new Element("nombre").setText(empleado.getNombre()));
            empleadoElement.addContent(new Element("dni").setText(empleado.getDni()));

            Element sueldosElement = new Element("sueldos");
            for (Double sueldo : empleado.getSueldosMensuales()) {
                sueldosElement.addContent(new Element("sueldo").setText(String.valueOf(sueldo)));
            }
            empleadoElement.addContent(sueldosElement);

            rootElement.addContent(empleadoElement);
        }

        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.setFormat(Format.getPrettyFormat());
        try (FileWriter fileWriter = new FileWriter(rutaArchivo)) {
            xmlOutputter.output(documento, fileWriter);
        }
    }

}
```

#### Explicación del código:

1. **Element y Document**: Creamos un nuevo elemento raíz (`empleados`) y lo almacenamos en un documento XML.
2. **Creación de empleados**: Por cada `Empleado`, creamos un elemento `empleado` que contiene los subelementos `id`, `nombre`, `dni` y `sueldos`.
3. **XMLOutputter**: Utilizamos `XMLOutputter` para escribir el documento XML en un archivo, formateando la salida de manera legible con `Format.getPrettyFormat()`.


### Paso 7: Lectura y Escritura de Archivos JSON

En este paso, vamos a implementar la lectura y escritura de archivos JSON utilizando la librería **Jackson**. Los archivos JSON tendrán una estructura similar a la de XML, pero usando el formato JSON, que es más ligero y común para el intercambio de datos.

#### 7.1. Lectura de archivos JSON

Vamos a leer un archivo JSON que contiene una lista de empleados. A continuación, un ejemplo de cómo será el formato del archivo JSON:

##### Ejemplo de archivo JSON:

```json
{
  "empleados": [
    {
      "id": 1,
      "nombre": "Juan Pérez",
      "dni": "12345678A",
      "sueldosMensuales": [1200.50, 1300.75, 1400.00, 1500.00, 1600.50, 1700.25, 1800.30, 1900.00, 2000.00, 1500.60, 1400.75, 1350.00]
    },
    {
      "id": 2,
      "nombre": "María García",
      "dni": "87654321B",
      "sueldosMensuales": [1100.00, 1150.75, 1200.50, 1250.00, 1300.75, 1350.50, 1400.75, 1450.00, 1500.75, 1550.50, 1600.75, 1650.00]
    }
  ]
}
```

##### Código para leer un archivo JSON:

```java
package es.fempa.acd.sumariosalarios;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LectorArchivoJSON {

    public static List<Empleado> leerArchivoJSON(String rutaArchivo) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        
        // Crear un objeto auxiliar para mapear la estructura del JSON
        EmpleadosWrapper empleadosWrapper = objectMapper.readValue(new File(rutaArchivo), EmpleadosWrapper.class);
        
        return empleadosWrapper.getEmpleados();
    }

    // Clase auxiliar para mapear el JSON
    public static class EmpleadosWrapper {
        private List<Empleado> empleados;

        public List<Empleado> getEmpleados() {
            return empleados;
        }

        public void setEmpleados(List<Empleado> empleados) {
            this.empleados = empleados;
        }
    }
}
```

#### Explicación del código:

1. **ObjectMapper**: Jackson proporciona la clase `ObjectMapper` que facilita la conversión de archivos JSON a objetos Java y viceversa.
2. **EmpleadosWrapper**: Creamos una clase `EmpleadosWrapper` como un contenedor para mapear la estructura del JSON, que contiene una lista de empleados.
3. **Mapeo de JSON**: Jackson convierte automáticamente el archivo JSON en instancias de `Empleado` utilizando el método `readValue()`.

#### 7.2. Escritura de archivos JSON

Ahora implementaremos la funcionalidad para escribir los datos de los empleados en un archivo JSON.

##### Código para escribir en un archivo JSON:

```java
package es.fempa.acd.sumariosalarios;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class EscritorArchivoJSON {

    public static void escribirArchivoJSON(String rutaArchivo, List<Empleado> empleados) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        
        // Crear un objeto auxiliar para envolver la lista de empleados
        EmpleadosWrapper empleadosWrapper = new EmpleadosWrapper();
        empleadosWrapper.setEmpleados(empleados);
        
        // Escribir el JSON en el archivo
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(rutaArchivo), empleadosWrapper);
    }

 

    public static void escribirDatosGenerados(String rutaArchivo, List<Empleado> empleados) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        EmpleadosWrapper empleadosWrapper = new EmpleadosWrapper();
        empleadosWrapper.setEmpleados(empleados);

        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(rutaArchivo), empleadosWrapper);
    }

    // Clase auxiliar para mapear el JSON
    public static class EmpleadosWrapper {
        private List<Empleado> empleados;

        public List<Empleado> getEmpleados() {
            return empleados;
        }

        public void setEmpleados(List<Empleado> empleados) {
            this.empleados = empleados;
        }
    }


}
```

#### Explicación del código:

1. **ObjectMapper**: Utilizamos el `ObjectMapper` de Jackson para convertir la lista de empleados en un archivo JSON.
2. **Pretty Printer**: Usamos `writerWithDefaultPrettyPrinter()` para que el archivo JSON esté bien formateado y sea legible.
3. **EmpleadosWrapper**: Igual que en la lectura, necesitamos una clase contenedora (`EmpleadosWrapper`) para mapear correctamente la lista de empleados en el archivo JSON.


### Paso 8: Implementación del Menú de Selección

En este paso, implementaremos un menú interactivo que permitirá al usuario elegir qué tipo de archivo quiere procesar (TXT, CSV, Excel, XML o JSON). Este menú será mostrado en la consola, y según la selección del usuario, se leerá el archivo correspondiente y se procesarán los datos de los empleados.

#### 8.1. Estructura del Menú

El menú interactivo presentará varias opciones para el usuario, que podrá elegir el formato del archivo de entrada. Después de procesar el archivo, se generará el sumario con la información de los empleados (incluyendo el sueldo medio, máximo y mínimo) y el archivo de salida en el mismo formato que el de entrada.

##### Código para el Menú de Selección:

```java
package es.fempa.acd.sumariosalarios;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class MenuSeleccion {

    public static void mostrarMenu() throws Exception {
        // Generar el dataset y escribir los archivos de datos
        List<Empleado> empleados = DataSetEmpleados.generarEmpleados();
        generarArchivosDeDatos(empleados);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Seleccione el tipo de archivo a procesar:");
        System.out.println("1. TXT");
        System.out.println("2. CSV");
        System.out.println("3. Excel");
        System.out.println("4. XML");
        System.out.println("5. JSON");
        System.out.println("0. Salir");
        
        int opcion = scanner.nextInt();
        scanner.nextLine(); // Consumir la línea nueva después del número

        String rutaArchivo = null;
        List<Empleado> empleadosLeidos = null;

        switch (opcion) {
            case 1:
                rutaArchivo = "empleados.txt";
                empleadosLeidos = LectorArchivoTXT.leerArchivoTXT(rutaArchivo);
                break;
            case 2:
                rutaArchivo = "empleados.csv";
                empleadosLeidos = LectorArchivoCSV.leerArchivoCSV(rutaArchivo);
                break;
            case 3:
                rutaArchivo = "empleados.xlsx";
                empleadosLeidos = LectorArchivoExcel.leerArchivoExcel(rutaArchivo);
                break;
            case 4:
                rutaArchivo = "empleados.xml";
                empleadosLeidos = LectorArchivoXML.leerArchivoXML(rutaArchivo);
                break;
            case 5:
                rutaArchivo = "empleados.json";
                empleadosLeidos = LectorArchivoJSON.leerArchivoJSON(rutaArchivo);
                break;
            case 0:
                System.out.println("Saliendo del programa...");
                return;
            default:
                System.out.println("Opción no válida.");
                return;
        }

        if (empleadosLeidos != null) {
            mostrarSumario(empleadosLeidos);
            System.out.println("Introduzca la ruta para el archivo de salida:");
            String rutaSalida = scanner.nextLine();

            // Guardar el archivo en el mismo formato que el archivo de entrada
            switch (opcion) {
                case 1:
				// hacer
                    break;
                case 2:
// hacer
                    break;
                case 3:
// hacer
                    break;
                case 4:
// hacer
                    break;
                case 5:
// hacer
                    break;
            }
        }
    }

    // Método para mostrar el sumario en consola
    private static void mostrarSumario(List<Empleado> empleados) {
        // hacer
    }

    // Método para generar los archivos de datos
    private static void generarArchivosDeDatos(List<Empleado> empleados) throws IOException {
       // hacer
    }
}
```

#### Explicación del código:

1. **Menú de selección**: Utilizamos un `Scanner` para capturar la opción del usuario. Dependiendo de la selección, llamamos al método correspondiente para leer el archivo del tipo elegido (TXT, CSV, Excel, XML o JSON).
   
2. **Ruta del archivo**: Después de seleccionar el tipo de archivo, el usuario debe introducir la ruta del archivo a procesar.

3. **Lectura y procesamiento**: Según la selección del usuario, se invoca al lector adecuado para leer el archivo y obtener la lista de empleados. Si la lista es válida, se llama al método `mostrarSumario` para mostrar en consola los resultados procesados.

4. **Escritura del archivo de salida**: Después de mostrar el sumario, el usuario introduce la ruta para el archivo de salida, y el programa escribe los resultados en el mismo formato que el archivo de entrada.

5. **Sumario de empleados**: El método `mostrarSumario` calcula el sueldo medio de todos los empleados, así como el sueldo máximo y mínimo, y lo muestra en consola.

#### 8.2. Ejecución del Menú

Para ejecutar este menú y probar su funcionalidad, puedes agregar una clase `Main`:

```java
package es.fempa.acd.sumariosalarios;

public class Main {
    public static void main(String[] args) {
        try {
            MenuSeleccion.mostrarMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```


Con esto el menú ya está implementado, lo que permite procesar archivos en diferentes formatos (TXT, CSV, Excel, XML y JSON), mostrar el sumario en consola y guardar los resultados en un archivo de salida. 