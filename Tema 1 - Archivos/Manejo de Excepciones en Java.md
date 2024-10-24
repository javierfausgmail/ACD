### Índice
1. **Introducción al Manejo de Excepciones en Java**
   - Definición de excepciones
   - Importancia del manejo de excepciones
   - Diferencias entre excepciones y errores comunes de programación

2. **Tipos de Excepciones en Java**
   - Excepciones verificadas (checked)
   - Excepciones no verificadas (unchecked)
   - Errores (`Errors`) vs. Excepciones (`Exceptions`)
   - Ejemplos de excepciones verificadas y no verificadas

3. **Jerarquía de Excepciones en Java**
   - La clase `Throwable`
   - `Exception` vs. `RuntimeException`
   - Ejemplos de clases comunes de excepciones
   - Cuándo utilizar excepciones de alto nivel (`RuntimeException`) frente a excepciones más específicas

4. **Manejo Básico de Excepciones**
   - Bloques `try`, `catch`, `finally`
   - Uso del bloque `finally` para limpieza de recursos
   - Uso de múltiples bloques `catch`
   - Ejemplos prácticos

5. **Creación de Excepciones Personalizadas**
   - Definición de clases de excepciones propias
   - Ejemplos de situaciones donde se necesitan excepciones personalizadas
   - Integración de excepciones personalizadas con las librerías estándar de Java

6. **Buenas Prácticas para el Manejo de Excepciones**
   - Cuándo usar excepciones verificadas vs. no verificadas
   - Evitar malas prácticas comunes (como el uso excesivo de excepciones o el uso de bloques `catch` genéricos)
   - Alternativas a malas prácticas comunes
   - Documentación y claridad en el código

7. **Uso de TinyLog para Registrar Excepciones**
   - Introducción a TinyLog
   - Por qué TinyLog es una buena opción frente a otras herramientas de logging
   - Configuración básica de TinyLog
   - Cómo registrar excepciones usando TinyLog
   - Ejemplos de registros de mensajes y trazas de pila

8. **Manejo Avanzado de Excepciones**
   - Propagación de excepciones (`throws`)
   - Relanzar excepciones (`throw`)
   - Uso de múltiples bloques `catch`
   - Uso de `try-with-resources` para liberar recursos automáticamente

9. **Ejercicios Prácticos**
   - Ejercicio 1: Manejo de excepciones verificadas
   - Ejercicio 2: Registro de excepciones con TinyLog
   - Ejercicio 3: Creación de una excepción personalizada y registro
   - Ejercicio 4: Manejo de excepciones en sistemas multi-threading

10. **Conclusión**
    - Resumen de conceptos clave
    - Consejos para aplicar lo aprendido en proyectos reales
    - Reflexión sobre cómo manejar excepciones en proyectos más grandes y complejos


Aquí está el desarrollo del **punto 1: Introducción al Manejo de Excepciones en Java**.

---

### 1. **Introducción al Manejo de Excepciones en Java**

#### 1.1 Definición de Excepciones

En Java, una **excepción** es un evento anómalo o inesperado que ocurre durante la ejecución de un programa, lo que interrumpe el flujo normal de las instrucciones. Este evento es detectado por el entorno de ejecución y, si no se maneja adecuadamente, puede provocar la terminación anormal del programa. Las excepciones pueden deberse a múltiples factores, como errores lógicos en el código, condiciones externas inesperadas o problemas con los recursos del sistema (como falta de memoria).

Java proporciona un mecanismo robusto para manejar estas situaciones, conocido como **manejo de excepciones**, que permite detectar, procesar y responder a las excepciones de manera controlada, sin comprometer la estabilidad del programa.

#### 1.2 Importancia del Manejo de Excepciones

El manejo adecuado de excepciones es fundamental para garantizar la fiabilidad y robustez de las aplicaciones. Los motivos principales para implementar un manejo eficaz de excepciones incluyen:

- **Mantenimiento de la estabilidad del programa**: Al capturar y manejar excepciones, un programa puede evitar interrupciones inesperadas o fallos totales, lo que permite que el usuario final disfrute de una experiencia sin errores críticos.
  
- **Mejora de la experiencia del usuario**: Un manejo adecuado permite que las aplicaciones reaccionen de manera controlada ante errores, mostrando mensajes útiles en lugar de terminar abruptamente o generar errores sin explicación.
  
- **Facilitación del depurado**: Al manejar y registrar correctamente las excepciones, se facilita la identificación de problemas específicos en el código, lo que mejora el proceso de depuración y resolución de errores.
  
- **Aseguramiento de la integridad de los datos**: En aplicaciones que gestionan datos importantes (por ejemplo, bases de datos), el manejo de excepciones puede prevenir la corrupción de datos al asegurar que cualquier error se capture antes de que se complete una transacción incorrecta.

#### 1.3 Diferencias entre Excepciones y Errores Comunes de Programación

Es importante distinguir entre las excepciones y los errores comunes de programación, ya que ambos conceptos son diferentes en cuanto a su origen y cómo deben manejarse:

- **Errores comunes de programación**: Estos incluyen problemas como errores de sintaxis, uso incorrecto de tipos de datos o lógica defectuosa. Los compiladores de Java detectan muchos de estos errores durante el proceso de compilación y el programador debe corregirlos antes de ejecutar el programa.
  
- **Excepciones**: A diferencia de los errores de programación, las excepciones ocurren durante el tiempo de ejecución. Por ejemplo, intentar dividir por cero, acceder a un archivo inexistente o conectarse a una base de datos que no está disponible. Las excepciones son condiciones que el programador no siempre puede prever, por lo que deben manejarse dinámicamente en el código para evitar el colapso de la aplicación.

Aquí está el desarrollo del **punto 2: Tipos de Excepciones en Java**.

---

### 2. **Tipos de Excepciones en Java**

En Java, las excepciones se dividen principalmente en dos grandes categorías: **excepciones verificadas** y **excepciones no verificadas**. Además, también existe una tercera categoría, que son los **errores**. A continuación, se detallan cada una de estas categorías para entender su uso y cuándo deben ser manejadas.

#### 2.1 Excepciones Verificadas (Checked Exceptions)

Las **excepciones verificadas** son aquellas que el compilador requiere que se manejen explícitamente en el código, ya sea a través de un bloque `try-catch` o mediante la declaración `throws`. Estas excepciones se prevén como parte del funcionamiento normal de un programa, pero deben ser gestionadas adecuadamente para evitar fallos en tiempo de ejecución.

Ejemplos comunes de excepciones verificadas son:

- **`IOException`**: Esta excepción ocurre durante operaciones de entrada/salida (como leer un archivo o abrir una conexión de red). Dado que los recursos externos pueden fallar por múltiples razones (falta de permisos, recursos no disponibles, etc.), es necesario manejar estas excepciones.
  
- **`SQLException`**: Se lanza cuando ocurre un error en las operaciones con bases de datos. Por ejemplo, intentar ejecutar una consulta SQL mal formulada o conectar a una base de datos que no está accesible.

##### Ejemplo de manejo de excepción verificada:

```java
public void leerArchivo(String nombreArchivo) {
    try {
        BufferedReader br = new BufferedReader(new FileReader(nombreArchivo));
        String linea;
        while ((linea = br.readLine()) != null) {
            System.out.println(linea);
        }
        br.close();
    } catch (IOException e) {
        e.printStackTrace();  // Manejo de la excepción
    }
}
```

En este ejemplo, si ocurre un problema al abrir el archivo o durante la lectura, se lanzará una excepción `IOException`, que el compilador obliga a manejar.

#### 2.2 Excepciones No Verificadas (Unchecked Exceptions)

Las **excepciones no verificadas** son aquellas que no están sujetas a la verificación del compilador, es decir, no es obligatorio manejarlas o declararlas en el código. Estas excepciones suelen estar relacionadas con errores de programación y se derivan de la clase `RuntimeException`. 

Aunque no es obligatorio manejarlas, es buena práctica hacerlo si se prevé que podrían ocurrir, para evitar que el programa falle de manera inesperada.

Ejemplos comunes de excepciones no verificadas:

- **`NullPointerException`**: Ocurre cuando se intenta acceder a un método o propiedad de un objeto que tiene valor `null`.
  
- **`ArrayIndexOutOfBoundsException`**: Se lanza cuando se intenta acceder a una posición fuera del rango válido de un array.

- **`ArithmeticException`**: Aparece cuando se realiza una operación matemática ilegal, como la división por cero.

##### Ejemplo de excepción no verificada:

```java
public void dividir(int a, int b) {
    int resultado = a / b;  // Si b es 0, se lanza una ArithmeticException
    System.out.println("Resultado: " + resultado);
}
```

En este caso, si `b` es igual a cero, se lanzará una `ArithmeticException`. Aunque el compilador no obligue a manejar esta excepción, es recomendable hacerlo para evitar fallos imprevistos.

#### 2.3 Errores (`Errors`) vs. Excepciones (`Exceptions`)

Los **errores** en Java son eventos graves que representan problemas fuera del control del programa. No están diseñados para ser manejados por el código, ya que son generalmente el resultado de situaciones anómalas a nivel de sistema, como la falta de memoria (OutOfMemoryError) o errores en el hardware.

Java clasifica los errores en la clase `Error`, que es una subclase de `Throwable`, al igual que las excepciones. Sin embargo, a diferencia de las excepciones, **los errores no deben ser manejados** mediante `try-catch` ya que representan situaciones en las que el programa no puede recuperarse de forma segura.

##### Diferencias clave entre `Errors` y `Exceptions`:

- **Errores (`Error`)**: Son graves y generalmente no recuperables, como fallos en la JVM o recursos del sistema no disponibles. No deben manejarse en código, ya que la recuperación suele ser imposible.
  
- **Excepciones (`Exception`)**: Son más comunes y a menudo recuperables. Pueden (y deben) ser manejadas cuando ocurren para evitar que el programa falle.

##### Ejemplo de un error común:

```java
public void generarError() {
    int[] array = new int[Integer.MAX_VALUE];  // Intentar asignar un tamaño de array muy grande genera OutOfMemoryError
}
```

En este ejemplo, intentar asignar una cantidad excesiva de memoria para un array puede generar un `OutOfMemoryError`, el cual no debe ser manejado con bloques `try-catch`, ya que representa una condición crítica del sistema.


Aquí está el desarrollo del **punto 3: Jerarquía de Excepciones en Java**.

---

### 3. **Jerarquía de Excepciones en Java**

Java organiza las excepciones y errores dentro de una estructura jerárquica bien definida, la cual se origina a partir de la clase base **`Throwable`**. Esta jerarquía es esencial para entender cómo se manejan y propagan las excepciones dentro del lenguaje, así como para aplicar las mejores prácticas al diseñar y manejar excepciones personalizadas.

#### 3.1 La Clase `Throwable`

En Java, **`Throwable`** es la clase raíz de todos los errores y excepciones. Esta clase tiene dos subclases principales:

- **`Exception`**: Representa las condiciones que los programas pueden querer o necesitar capturar y manejar. Las excepciones derivadas de esta clase son las que habitualmente se tratan en el código mediante bloques `try-catch`.

- **`Error`**: Representa problemas serios que no se espera que las aplicaciones capturen o manejen. Estos suelen ser fallos a nivel de la máquina virtual (JVM) o del sistema operativo, como `OutOfMemoryError` o `StackOverflowError`.

```java
java.lang.Object
   ↳ java.lang.Throwable
       ↳ java.lang.Error
       ↳ java.lang.Exception
```

#### 3.2 `Exception` vs. `RuntimeException`

Dentro de la jerarquía de excepciones, es importante hacer una distinción entre las excepciones que extienden directamente de **`Exception`** y aquellas que extienden de **`RuntimeException`**.

- **`Exception` (excluyendo `RuntimeException`)**: Son las **excepciones verificadas** que deben manejarse explícitamente en el código. Estas excepciones representan problemas que el compilador obliga a prever y manejar, como errores de entrada/salida (`IOException`) o errores al trabajar con bases de datos (`SQLException`).

- **`RuntimeException`**: Son las **excepciones no verificadas** que el compilador no exige que se manejen explícitamente. Estas excepciones suelen deberse a errores de lógica o programación, como intentar acceder a un elemento de un array fuera de su rango (`ArrayIndexOutOfBoundsException`) o una operación sobre un objeto `null` (`NullPointerException`).

##### Ejemplo de la jerarquía de excepciones:

```java
java.lang.Object
   ↳ java.lang.Throwable
       ↳ java.lang.Exception
           ↳ java.lang.RuntimeException
               ↳ java.lang.NullPointerException
               ↳ java.lang.ArithmeticException
           ↳ java.io.IOException
           ↳ java.sql.SQLException
```

##### Diferencias clave:

1. **Excepciones Verificadas (`Exception`)**: Deben declararse o manejarse. Representan problemas que el programador espera o que son previsibles durante el desarrollo de la aplicación. Ejemplos: `IOException`, `SQLException`.

2. **Excepciones No Verificadas (`RuntimeException`)**: No es obligatorio manejarlas o declararlas. Estas excepciones generalmente indican errores de programación que deberían corregirse en lugar de manejarse mediante bloques `try-catch`. Ejemplos: `NullPointerException`, `ArrayIndexOutOfBoundsException`.

#### 3.3 Ejemplos de Clases Comunes de Excepciones

Algunas de las clases más comunes de excepciones en Java, organizadas según su tipo, son:

- **Excepciones Verificadas**:
   - **`IOException`**: Ocurre en operaciones de entrada/salida, como leer o escribir en archivos.
   - **`SQLException`**: Se lanza cuando hay problemas al interactuar con una base de datos.
   - **`ClassNotFoundException`**: Se produce cuando la JVM no puede encontrar la clase solicitada durante la ejecución.

- **Excepciones No Verificadas**:
   - **`NullPointerException`**: Aparece cuando se intenta utilizar un objeto que tiene valor `null`.
   - **`ArithmeticException`**: Se lanza cuando ocurre un error aritmético, como dividir por cero.
   - **`ArrayIndexOutOfBoundsException`**: Ocurre cuando se intenta acceder a una posición fuera de los límites de un array.

- **Errores (`Error`)**:
   - **`OutOfMemoryError`**: Se lanza cuando la JVM no puede asignar más memoria.
   - **`StackOverflowError`**: Se produce cuando el espacio del stack es insuficiente, a menudo debido a recursión excesiva.
   - **`VirtualMachineError`**: Ocurre cuando hay un problema interno grave en la máquina virtual de Java.

#### 3.4 Cuándo Utilizar Excepciones de Alto Nivel como `RuntimeException`

Es importante entender cuándo es adecuado usar excepciones como `RuntimeException`. Si bien su uso no está restringido, se recomienda:

- **Para errores de lógica**: Los errores que son resultado de fallas en la lógica del programa, como intentar acceder a un elemento inexistente en una lista o dividir por cero, deberían producir excepciones no verificadas. En estos casos, corregir el código es preferible a capturar la excepción.
  
- **Para APIs que pueden fallar debido a malas condiciones de uso**: Por ejemplo, al implementar una API que espera entradas válidas, se pueden lanzar excepciones de tipo `RuntimeException` cuando se proporcionan valores fuera de lo aceptable.

Evitar el uso indiscriminado de `RuntimeException` es esencial para mantener un código claro y robusto. En situaciones donde una excepción es esperada y manejable, es mejor optar por una excepción verificada.


Aquí está el desarrollo del **punto 4: Manejo Básico de Excepciones**.

---

### 4. **Manejo Básico de Excepciones**

El manejo de excepciones en Java se basa en tres bloques fundamentales: **`try`**, **`catch`**, y **`finally`**. Estos bloques permiten al programador gestionar situaciones excepcionales y realizar acciones específicas cuando ocurren errores en el flujo del programa. Además, se puede utilizar el bloque `finally` para asegurarse de que ciertos recursos se liberen o que ciertas acciones se realicen, independientemente de si ocurrió una excepción o no.

#### 4.1 Bloques `try`, `catch` y `finally`

- **`try`**: El bloque `try` contiene el código que podría lanzar una excepción. Es el primer paso para encapsular operaciones que pueden fallar y donde es necesario un manejo controlado de errores.

- **`catch`**: Si una excepción ocurre dentro del bloque `try`, se transfiere inmediatamente al bloque `catch` correspondiente que puede manejar la excepción. El bloque `catch` debe especificar el tipo de excepción que desea capturar.

- **`finally`**: Este bloque es opcional y se ejecuta siempre, sin importar si una excepción fue lanzada o no. Se utiliza normalmente para realizar limpieza de recursos como cerrar archivos, liberar conexiones a bases de datos, etc.

##### Ejemplo básico:

```java
public void dividir(int a, int b) {
    try {
        int resultado = a / b;  // Este bloque podría lanzar ArithmeticException si b es 0
        System.out.println("Resultado: " + resultado);
    } catch (ArithmeticException e) {
        System.out.println("Error: No se puede dividir por cero.");
    } finally {
        System.out.println("Operación finalizada.");
    }
}
```

En este ejemplo:
- Si `b` es igual a cero, se lanza una **`ArithmeticException`** que es capturada por el bloque `catch`.
- El bloque `finally` se ejecuta siempre, independientemente de si ocurrió o no una excepción.

#### 4.2 Uso del Bloque `finally` para Limpieza de Recursos

Uno de los usos más comunes del bloque `finally` es asegurar que los recursos como archivos, conexiones a bases de datos o flujos de entrada/salida sean liberados correctamente, independientemente de si ocurrió una excepción. 

Esto es especialmente importante para evitar fugas de recursos, como archivos abiertos sin cerrar, lo que podría afectar el rendimiento o la estabilidad de la aplicación.

##### Ejemplo con manejo de recursos:

```java
public void leerArchivo(String nombreArchivo) {
    BufferedReader br = null;
    try {
        br = new BufferedReader(new FileReader(nombreArchivo));
        String linea;
        while ((linea = br.readLine()) != null) {
            System.out.println(linea);
        }
    } catch (IOException e) {
        System.out.println("Error al leer el archivo: " + e.getMessage());
    } finally {
        try {
            if (br != null) {
                br.close();  // Se cierra el archivo en el bloque finally
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
```

En este ejemplo:
- El bloque `finally` asegura que el archivo siempre se cierra, incluso si ocurre una excepción en la lectura del archivo.
- Se realiza una verificación de `null` para evitar una excepción si el archivo no pudo ser abierto correctamente.

#### 4.3 Uso de Múltiples Bloques `catch`

Java permite manejar diferentes tipos de excepciones en diferentes bloques `catch`. Esto es útil cuando varias excepciones podrían ser lanzadas dentro de un bloque `try` y se desea manejar cada una de manera diferente.

##### Ejemplo con múltiples bloques `catch`:

```java
public void procesarArchivo(String nombreArchivo) {
    try {
        BufferedReader br = new BufferedReader(new FileReader(nombreArchivo));
        String linea;
        int numero = Integer.parseInt(br.readLine());  // Puede lanzar NumberFormatException
        System.out.println("Número: " + numero);
        br.close();
    } catch (FileNotFoundException e) {
        System.out.println("Error: El archivo no fue encontrado.");
    } catch (NumberFormatException e) {
        System.out.println("Error: El archivo no contiene un número válido.");
    } catch (IOException e) {
        System.out.println("Error: Ocurrió un problema al leer el archivo.");
    } finally {
        System.out.println("Finalización del proceso.");
    }
}
```

En este ejemplo:
- Si el archivo no existe, se lanza una **`FileNotFoundException`**.
- Si el contenido del archivo no es un número, se lanza una **`NumberFormatException`**.
- Si hay un error al leer el archivo, se lanza una **`IOException`**.
- Cada excepción se maneja en su respectivo bloque `catch`.

#### 4.4 Ejemplos Prácticos

**Ejemplo 1: Excepción de división por cero**

```java
public void calcular(int a, int b) {
    try {
        int resultado = a / b;
        System.out.println("Resultado: " + resultado);
    } catch (ArithmeticException e) {
        System.out.println("No se puede dividir por cero.");
    }
}
```

Este ejemplo muestra cómo manejar un error básico de aritmética (división por cero).

**Ejemplo 2: Manejo de múltiples excepciones**

```java
public void leerYConvertirArchivo(String nombreArchivo) {
    try {
        BufferedReader br = new BufferedReader(new FileReader(nombreArchivo));
        String linea = br.readLine();
        int numero = Integer.parseInt(linea);
        System.out.println("Número convertido: " + numero);
        br.close();
    } catch (FileNotFoundException e) {
        System.out.println("El archivo no fue encontrado.");
    } catch (IOException e) {
        System.out.println("Error al leer el archivo.");
    } catch (NumberFormatException e) {
        System.out.println("No es un número válido.");
    } finally {
        System.out.println("Proceso completado.");
    }
}
```

En este ejemplo, se manejan tres excepciones diferentes: **`FileNotFoundException`**, **`IOException`**, y **`NumberFormatException`**, lo que demuestra cómo se puede tener un código más robusto y controlado.

Aquí está el desarrollo del **punto 5: Creación de Excepciones Personalizadas**.

---

### 5. **Creación de Excepciones Personalizadas**

En Java, además de las excepciones predefinidas que ofrece el lenguaje, el desarrollador puede crear sus propias excepciones personalizadas. Esto es útil cuando se quiere capturar y manejar situaciones específicas de una aplicación que no están cubiertas por las excepciones estándar de Java. Las excepciones personalizadas proporcionan una forma clara y significativa de señalar condiciones excepcionales dentro del dominio específico de la aplicación.

#### 5.1 Definición de Clases de Excepciones Propias

Crear una excepción personalizada en Java implica definir una nueva clase que extienda la clase **`Exception`** o **`RuntimeException`**, dependiendo de si se quiere crear una excepción verificada o no verificada.

1. **Excepción Verificada**: Para crear una excepción verificada, se debe extender la clase **`Exception`**.
   
2. **Excepción No Verificada**: Para crear una excepción no verificada, se debe extender la clase **`RuntimeException`**.

La estructura básica para una excepción personalizada es la siguiente:

```java
public class MiExcepcionPersonalizada extends Exception {
    public MiExcepcionPersonalizada(String mensaje) {
        super(mensaje);  // Pasar el mensaje de error a la superclase Exception
    }
}
```

Este ejemplo muestra cómo crear una excepción verificada que extiende `Exception`. En este caso, la excepción debe ser capturada o declarada en el método con `throws`.

#### 5.2 Ejemplo de Excepción Verificada Personalizada

Supongamos que estamos desarrollando una aplicación bancaria y queremos lanzar una excepción específica cuando el saldo de una cuenta bancaria es insuficiente para realizar una operación. Para esto, se puede crear una excepción verificada llamada `SaldoInsuficienteException`.

##### Ejemplo:

```java
public class SaldoInsuficienteException extends Exception {
    public SaldoInsuficienteException(String mensaje) {
        super(mensaje);
    }
}
```

Luego, se podría utilizar esta excepción en el siguiente contexto:

```java
public class CuentaBancaria {
    private double saldo;

    public CuentaBancaria(double saldoInicial) {
        this.saldo = saldoInicial;
    }

    public void retirar(double cantidad) throws SaldoInsuficienteException {
        if (cantidad > saldo) {
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar la operación.");
        }
        saldo -= cantidad;
    }

    public double getSaldo() {
        return saldo;
    }
}
```

En este caso, si un usuario intenta retirar más dinero del que tiene disponible, se lanza la excepción `SaldoInsuficienteException`. Dado que es una excepción verificada, el método `retirar` debe declararla con `throws`.

##### Uso del código:

```java
public class Main {
    public static void main(String[] args) {
        CuentaBancaria cuenta = new CuentaBancaria(100);
        
        try {
            cuenta.retirar(150);  // Esto lanzará SaldoInsuficienteException
        } catch (SaldoInsuficienteException e) {
            System.out.println("Excepción capturada: " + e.getMessage());
        }
    }
}
```

#### 5.3 Ejemplo de Excepción No Verificada Personalizada

En algunos casos, puede ser adecuado crear una excepción no verificada. Estas excepciones derivan de `RuntimeException`, y no es obligatorio manejarlas explícitamente en el código. Supongamos que queremos crear una excepción para una operación ilegal de negocio, como un intento de dividir un valor de saldo por cero en nuestra aplicación bancaria.

##### Ejemplo:

```java
public class OperacionIlegalException extends RuntimeException {
    public OperacionIlegalException(String mensaje) {
        super(mensaje);
    }
}
```

Luego, se podría utilizar esta excepción de la siguiente manera:

```java
public class Calculadora {
    public double dividirSaldo(double saldo, double divisor) {
        if (divisor == 0) {
            throw new OperacionIlegalException("No se puede dividir el saldo por cero.");
        }
        return saldo / divisor;
    }
}
```

En este caso, si el divisor es cero, se lanza una `OperacionIlegalException`. Como es una excepción no verificada, no es necesario declararla o capturarla, aunque puede hacerse para mejorar la claridad del código.

##### Uso del código:

```java
public class Main {
    public static void main(String[] args) {
        Calculadora calculadora = new Calculadora();
        
        try {
            double resultado = calculadora.dividirSaldo(100, 0);  // Esto lanzará OperacionIlegalException
        } catch (OperacionIlegalException e) {
            System.out.println("Excepción capturada: " + e.getMessage());
        }
    }
}
```

#### 5.4 Ejemplos de Situaciones donde se Necesitan Excepciones Personalizadas

Las excepciones personalizadas son útiles cuando se necesita capturar errores o condiciones que son específicas de la lógica de la aplicación. A continuación, algunos ejemplos comunes de cuándo puede ser necesario crear excepciones personalizadas:

1. **Validación de negocio**: En aplicaciones con lógica de negocio compleja, como sistemas financieros o de gestión, es común encontrar la necesidad de manejar condiciones excepcionales específicas que no están cubiertas por las excepciones estándar de Java. Por ejemplo:
   - **`SaldoInsuficienteException`** en una aplicación bancaria.
   - **`ProductoNoDisponibleException`** en un sistema de inventario.

2. **Operaciones con recursos externos**: Cuando se trabaja con recursos externos como APIs, archivos o conexiones de red, se pueden crear excepciones personalizadas para gestionar errores específicos relacionados con la disponibilidad o integridad de esos recursos.

3. **Manejo de transacciones**: En sistemas que manejan transacciones (como en bases de datos o procesamiento de pagos), es útil crear excepciones personalizadas para capturar condiciones excepcionales que se produzcan durante el ciclo de vida de una transacción.

#### 5.5 Ventajas de Utilizar Excepciones Personalizadas

- **Claridad del código**: Las excepciones personalizadas proporcionan un contexto más claro sobre el tipo de error que ha ocurrido, lo que facilita la lectura y el mantenimiento del código.
  
- **Manejo específico de errores**: Permiten capturar y manejar errores que son específicos del dominio de la aplicación, lo que mejora la robustez y permite respuestas más adecuadas a las condiciones excepcionales.
  
- **Estandarización del manejo de errores**: En sistemas grandes, es importante que el manejo de errores sea consistente. Al definir excepciones personalizadas, se puede asegurar que se sigan prácticas uniformes para la gestión de errores a lo largo de toda la aplicación.

Aquí está el desarrollo del **punto 6: Buenas Prácticas para el Manejo de Excepciones**.

---

### 6. **Buenas Prácticas para el Manejo de Excepciones**

El manejo de excepciones es una parte crítica en la creación de aplicaciones robustas y fiables. Si bien Java proporciona un marco flexible para gestionar excepciones, es importante seguir ciertas **buenas prácticas** para evitar problemas como el código desordenado o la mala gestión de errores. A continuación, se presentan algunas de las prácticas recomendadas para manejar excepciones de manera efectiva.

#### 6.1 Cuándo Usar Excepciones Verificadas vs. No Verificadas

Decidir entre usar una excepción verificada (que extiende `Exception`) o no verificada (que extiende `RuntimeException`) es fundamental para diseñar un buen sistema de manejo de errores.

- **Excepciones Verificadas (`Exception`)**: 
  - Úselas cuando se espera que el programador que consume el API pueda **recuperarse** de la excepción. Por ejemplo, las excepciones que resultan de fallos en la entrada/salida (como `IOException`) o problemas en la conexión a la base de datos (`SQLException`) son típicamente verificadas, ya que el programador puede manejar la situación y decidir un curso alternativo.
  - Las excepciones verificadas obligan a los desarrolladores a manejar explícitamente los posibles errores, lo que puede llevar a un código más seguro y controlado.

- **Excepciones No Verificadas (`RuntimeException`)**:
  - Úselas cuando las excepciones representan **errores de programación** o condiciones que no deberían recuperarse, como el acceso a un índice fuera de los límites en un array (`ArrayIndexOutOfBoundsException`) o intentar usar una referencia `null` (`NullPointerException`). Estas excepciones se consideran errores que deben corregirse en lugar de capturarse.
  - No se deben forzar a los desarrolladores a capturar y manejar este tipo de excepciones, ya que el manejo de las mismas a menudo implica corregir el código.

##### Regla general:
- Use **excepciones verificadas** para situaciones de las que el programador puede (y debe) recuperarse.
- Use **excepciones no verificadas** para errores de programación o fallos inesperados en los que la recuperación no es posible o no tiene sentido.

#### 6.2 Evitar Malas Prácticas Comunes

Existen varios errores comunes que los desarrolladores suelen cometer al manejar excepciones. A continuación, se enumeran algunas de las prácticas que deben evitarse y las razones por las que son perjudiciales:

- **Capturar Excepciones Genéricas**:
  Capturar excepciones genéricas como **`Exception`** o **`Throwable`** es una mala práctica porque puede ocultar errores que no deberían manejarse de la misma forma. Además, dificulta el diagnóstico de problemas específicos.

  ```java
  // Mala práctica: Captura genérica de Exception
  try {
      // Código que puede lanzar múltiples excepciones
  } catch (Exception e) {
      e.printStackTrace();
  }
  ```

  **Recomendación**: Capturar solo las excepciones específicas que pueden manejarse correctamente.

  ```java
  // Buena práctica: Captura de excepciones específicas
  try {
      // Código que puede lanzar IOException y SQLException
  } catch (IOException e) {
      System.out.println("Error de E/S: " + e.getMessage());
  } catch (SQLException e) {
      System.out.println("Error de Base de Datos: " + e.getMessage());
  }
  ```

- **Silenciar Excepciones**:
  Ignorar excepciones sin hacer nada con ellas puede llevar a situaciones difíciles de depurar y a la creación de errores silenciosos que hacen que el programa funcione de manera incorrecta sin que el desarrollador sea consciente de ello.

  ```java
  // Mala práctica: Silenciar excepciones
  try {
      // Código que puede lanzar una excepción
  } catch (IOException e) {
      // No hacer nada
  }
  ```

  **Recomendación**: Al menos registre la excepción para que pueda diagnosticarse más tarde o maneje la situación adecuadamente.

  ```java
  // Buena práctica: Registrar la excepción o tomar acción adecuada
  try {
      // Código que puede lanzar una excepción
  } catch (IOException e) {
      e.printStackTrace();  // Al menos registrar el error
  }
  ```

- **Relanzar Excepciones Sin Información Contextual**:
  Si se decide relanzar una excepción, es una buena práctica agregar contexto adicional. Esto puede facilitar el proceso de depuración, ya que la excepción original puede no proporcionar toda la información necesaria para entender el error.

  ```java
  // Mala práctica: Relanzar sin contexto
  try {
      // Código que puede lanzar una excepción
  } catch (IOException e) {
      throw new RuntimeException(e);  // Relanzar sin mensaje adicional
  }
  ```

  **Recomendación**: Agregar un mensaje explicativo o contexto adicional cuando se relance una excepción.

  ```java
  // Buena práctica: Relanzar con contexto adicional
  try {
      // Código que puede lanzar una excepción
  } catch (IOException e) {
      throw new RuntimeException("Error al procesar el archivo: " + e.getMessage(), e);
  }
  ```

#### 6.3 Documentación y Claridad en el Código

- **Documentar las Excepciones en los Métodos**: Es una buena práctica documentar claramente qué excepciones puede lanzar un método utilizando el bloque `@throws` en los comentarios de Javadoc. Esto es especialmente importante para excepciones verificadas, ya que ayuda a los desarrolladores a comprender qué errores deben manejar cuando llaman a ese método.

  ```java
  /**
   * Lee un archivo y retorna su contenido.
   * 
   * @param rutaArchivo La ruta al archivo que se va a leer.
   * @return El contenido del archivo.
   * @throws FileNotFoundException Si el archivo no existe.
   * @throws IOException Si ocurre un error de entrada/salida.
   */
  public String leerArchivo(String rutaArchivo) throws FileNotFoundException, IOException {
      // Código para leer el archivo
  }
  ```

- **Mantener el Código Limpio y Legible**: Evite manejar demasiadas excepciones en un solo bloque `try-catch`, ya que esto puede hacer que el código sea difícil de entender y mantener. Si es posible, divida el código en métodos más pequeños y específicos, cada uno con su propio manejo de excepciones.

#### 6.4 Gestión de Recursos con `try-with-resources`

El uso de `try-with-resources` es una buena práctica que asegura que los recursos como archivos, conexiones de base de datos o flujos de entrada/salida se cierren automáticamente, incluso si ocurre una excepción. Este enfoque simplifica el código y elimina la necesidad de escribir un bloque `finally` manual para cerrar recursos.

##### Ejemplo:

```java
// Uso de try-with-resources para asegurar el cierre del recurso automáticamente
public void leerArchivo(String rutaArchivo) {
    try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            System.out.println(linea);
        }
    } catch (IOException e) {
        System.out.println("Error al leer el archivo: " + e.getMessage());
    }
}
```

El bloque `try-with-resources` asegura que el objeto `BufferedReader` se cierre automáticamente cuando se sale del bloque, ya sea debido a la finalización normal del bloque o debido a una excepción.

Aquí está el desarrollo del **punto 7: Uso de TinyLog para Registrar Excepciones**.

---

### 7. **Uso de TinyLog para Registrar Excepciones**

Registrar excepciones es una parte fundamental del manejo de errores en las aplicaciones. El registro permite capturar detalles importantes sobre las excepciones ocurridas, lo que facilita la depuración y el monitoreo en aplicaciones de producción. **TinyLog** es una herramienta ligera de logging que se integra fácilmente en aplicaciones Java y proporciona una forma sencilla y flexible de registrar excepciones y otros eventos.

#### 7.1 Introducción a TinyLog

**TinyLog** es una biblioteca de registro de eventos para Java que destaca por su simplicidad y bajo peso. A diferencia de otros sistemas de logging como Log4j o SLF4J, TinyLog está diseñado para ser fácil de configurar y utilizar, con un impacto mínimo en el rendimiento de la aplicación.

**Características principales de TinyLog**:
- Configuración mínima.
- Registro de mensajes de diferentes niveles (trace, debug, info, warn, error).
- Soporte para múltiples salidas (consola, archivo, etc.).
- Formatos de salida personalizables.

#### 7.2 Configuración Básica de TinyLog

Para utilizar TinyLog en su proyecto, lo primero que debe hacer es agregar la dependencia en el archivo `pom.xml` si utiliza **Maven**, o incluirla en su `build.gradle` si usa **Gradle**.

**Ejemplo para Maven**:

```xml
<dependency>
    <groupId>org.tinylog</groupId>
    <artifactId>tinylog-api</artifactId>
    <version>2.4.1</version>
</dependency>
<dependency>
    <groupId>org.tinylog</groupId>
    <artifactId>tinylog-impl</artifactId>
    <version>2.4.1</version>
</dependency>
```

**Ejemplo para Gradle**:

```gradle
implementation 'org.tinylog:tinylog-api:2.4.1'
implementation 'org.tinylog:tinylog-impl:2.4.1'
```

#### 7.3 Cómo Registrar Excepciones Usando TinyLog

Una vez que TinyLog está configurado, puede comenzar a registrar excepciones y otros eventos importantes en su aplicación. El registro de excepciones se realiza utilizando los niveles de registro de TinyLog, que incluyen: `info`, `warn`, `error`, entre otros.

##### Ejemplo básico:

```java
import org.tinylog.Logger;

public class EjemploTinyLog {
    public static void main(String[] args) {
        try {
            int resultado = dividir(10, 0);
        } catch (ArithmeticException e) {
            Logger.error(e, "Se produjo un error al intentar dividir.");
        }
    }

    public static int dividir(int a, int b) {
        return a / b;  // Esto lanzará una ArithmeticException si b es 0
    }
}
```

En este ejemplo:
- Cuando ocurre una excepción, se utiliza el método `Logger.error()` para registrar tanto la excepción como un mensaje contextual.
- La traza de la pila (stack trace) se captura automáticamente y se incluye en el registro.

#### 7.4 Ejemplos de Registros de Mensajes y Trazas de Pila

TinyLog permite registrar excepciones con diferentes niveles de detalle según la importancia o gravedad de la excepción. A continuación se presentan algunos ejemplos de cómo registrar mensajes y trazas de pila en diferentes niveles.

- **Registrar una excepción con un mensaje**:

```java
try {
    int resultado = dividir(10, 0);
} catch (ArithmeticException e) {
    Logger.error(e, "Error en la operación de división.");  // Se registra la excepción con el mensaje
}
```

- **Registrar una excepción sin la traza de pila**:

A veces puede ser suficiente registrar solo el mensaje de la excepción, sin incluir la traza de la pila.

```java
try {
    int resultado = dividir(10, 0);
} catch (ArithmeticException e) {
    Logger.warn("Error: División por cero.");  // Mensaje sin traza de pila
}
```

- **Registrar excepciones con otros niveles de gravedad**:

Dependiendo de la situación, puede registrar la excepción utilizando diferentes niveles, como `info`, `warn`, o `debug`, según la gravedad del error o la información que desee capturar.

```java
try {
    int resultado = dividir(10, 0);
} catch (ArithmeticException e) {
    Logger.debug(e, "Excepción capturada durante el modo de depuración.");
}
```

#### 7.5 Configuración de TinyLog para Salidas Personalizadas

TinyLog permite configurar la salida del registro para enviarlo a diferentes destinos, como la consola, archivos de texto, o sistemas remotos. Esto se puede hacer a través de un archivo de configuración o mediante configuración programática.

##### Ejemplo de configuración de salida a archivo:

Puede crear un archivo `tinylog.properties` para definir cómo y dónde se registrarán los mensajes.

```properties
# tinylog.properties

tinylog.writer = file
tinylog.writer.file = logs/aplicacion.log
tinylog.writer.append = true
tinylog.level = info
```

En este ejemplo:
- Los mensajes de nivel `info` y superiores se registrarán en el archivo `aplicacion.log`.
- La opción `append` asegura que los nuevos registros se agreguen al final del archivo, en lugar de sobrescribirlo.

##### Ejemplo de configuración programática:

Si prefiere configurar TinyLog directamente desde el código, puede hacerlo utilizando la API proporcionada.

```java
import org.tinylog.configuration.Configuration;

public class ConfiguracionTinyLog {
    public static void main(String[] args) {
        Configuration.set("tinylog.writer", "file");
        Configuration.set("tinylog.writer.file", "logs/app.log");
        Configuration.set("tinylog.level", "warn");

        Logger.info("Este mensaje no será registrado porque el nivel es 'warn'.");
        Logger.warn("Este es un mensaje de advertencia.");
    }
}
```

#### 7.6 Ventajas de TinyLog para Registrar Excepciones

- **Simplicidad**: TinyLog es extremadamente fácil de configurar y utilizar, lo que lo convierte en una excelente opción para proyectos donde se necesita un sistema de registro ligero y sin complicaciones.
  
- **Flexibilidad**: A pesar de su simplicidad, TinyLog permite una configuración avanzada, con soporte para múltiples salidas, niveles de registro, y personalización de formatos de salida.
  
- **Bajo Consumo de Recursos**: TinyLog está diseñado para ser ligero y de alto rendimiento, lo que lo convierte en una buena opción para aplicaciones que requieren registrar eventos sin sobrecargar el sistema.

Aquí está el desarrollo del **punto 8: Manejo Avanzado de Excepciones**.

---

### 8. **Manejo Avanzado de Excepciones**

El manejo avanzado de excepciones en Java implica un uso más profundo de las técnicas y características proporcionadas por el lenguaje para gestionar las excepciones de manera eficiente en escenarios más complejos. Esto incluye la **propagación de excepciones**, **relanzar excepciones**, el uso de **múltiples bloques `catch`**, y el uso de **`try-with-resources`** para la correcta gestión de recursos.

#### 8.1 Propagación de Excepciones (`throws`)

En algunas situaciones, no es adecuado o necesario manejar una excepción directamente dentro de un método. En lugar de capturarla y manejarla localmente, podemos propagar la excepción al método que llamó al método actual. Esto se realiza utilizando la cláusula **`throws`** en la declaración del método. Esto es útil cuando el manejo de la excepción debe realizarse a un nivel más alto en la pila de llamadas, permitiendo que un método intermedio no se vea obligado a manejar la excepción.

##### Ejemplo:

```java
public void metodoA() throws IOException {
    metodoB();  // métodoB puede lanzar una IOException
}

public void metodoB() throws IOException {
    FileReader fr = new FileReader("archivo.txt");
    fr.close();
}
```

En este ejemplo:
- `metodoA` llama a `metodoB`, que podría lanzar una excepción de tipo `IOException`.
- Ninguno de los dos métodos maneja directamente la excepción, pero `metodoA` la declara con `throws` para propagarla al siguiente nivel en la pila de llamadas.

Cuando una excepción se propaga, debe ser capturada o declarada en los niveles superiores de la pila de ejecución.

#### 8.2 Relanzar Excepciones (`throw`)

En algunas situaciones, es útil capturar una excepción, realizar alguna acción (como registrar un mensaje o cerrar recursos) y luego relanzar la misma excepción o una nueva. Esto se conoce como **relanzar una excepción**. Relanzar excepciones permite encapsular lógica adicional antes de que la excepción sea manejada por otro bloque.

##### Ejemplo de relanzar la misma excepción:

```java
public void procesarArchivo() throws IOException {
    try {
        FileReader fr = new FileReader("archivo.txt");
        fr.close();
    } catch (IOException e) {
        System.out.println("Error al procesar el archivo: " + e.getMessage());
        throw e;  // Relanzar la misma excepción
    }
}
```

En este ejemplo, la excepción se captura, se registra un mensaje, y luego se vuelve a lanzar la misma excepción para que sea manejada más arriba en la pila de llamadas.

##### Ejemplo de relanzar una nueva excepción:

```java
public void procesarArchivo() throws MiExcepcionPersonalizada {
    try {
        FileReader fr = new FileReader("archivo.txt");
        fr.close();
    } catch (IOException e) {
        throw new MiExcepcionPersonalizada("Error específico en el procesamiento del archivo", e);
    }
}
```

En este caso, se captura una `IOException` y se encapsula en una excepción personalizada (`MiExcepcionPersonalizada`) que puede proporcionar información más específica sobre el error ocurrido.

#### 8.3 Uso de Múltiples Bloques `catch`

En Java, se pueden utilizar varios bloques `catch` para manejar diferentes tipos de excepciones que puedan ocurrir en un bloque `try`. Esto permite tratar de manera diferenciada cada tipo de excepción y aplicar la lógica adecuada a cada caso. Además, a partir de Java 7, es posible utilizar **multi-catch**, una característica que permite capturar múltiples excepciones en un solo bloque `catch`.

##### Ejemplo con múltiples bloques `catch`:

```java
public void leerDatos() {
    try {
        FileReader fr = new FileReader("archivo.txt");
        int resultado = 10 / 0;  // Puede lanzar ArithmeticException
        fr.close();
    } catch (FileNotFoundException e) {
        System.out.println("El archivo no fue encontrado.");
    } catch (ArithmeticException e) {
        System.out.println("Error aritmético: división por cero.");
    } catch (IOException e) {
        System.out.println("Error al cerrar el archivo.");
    }
}
```

En este ejemplo, se capturan tres excepciones diferentes:
- `FileNotFoundException` si el archivo no se encuentra.
- `ArithmeticException` si ocurre una división por cero.
- `IOException` si hay un error al cerrar el archivo.

##### Ejemplo de **multi-catch**:

```java
public void leerDatos() {
    try {
        FileReader fr = new FileReader("archivo.txt");
        int resultado = 10 / 0;
        fr.close();
    } catch (FileNotFoundException | ArithmeticException e) {
        System.out.println("Excepción capturada: " + e.getMessage());
    } catch (IOException e) {
        System.out.println("Error al cerrar el archivo.");
    }
}
```

Con el uso de multi-catch, se pueden capturar varias excepciones en un único bloque `catch`, lo que permite reducir el número de bloques sin perder claridad.

#### 8.4 Uso de `try-with-resources` para Manejo Automático de Recursos

El bloque **`try-with-resources`**, introducido en Java 7, es una herramienta muy útil para manejar automáticamente los recursos que deben ser cerrados después de su uso, como archivos o conexiones a bases de datos. Este mecanismo garantiza que los recursos se cierren correctamente, incluso si se lanza una excepción dentro del bloque `try`.

Cuando se usa `try-with-resources`, no es necesario escribir un bloque `finally` explícito para cerrar los recursos, ya que Java lo maneja automáticamente.

##### Ejemplo de `try-with-resources`:

```java
public void leerArchivo(String ruta) {
    try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            System.out.println(linea);
        }
    } catch (IOException e) {
        System.out.println("Error al leer el archivo: " + e.getMessage());
    }
}
```

En este ejemplo:
- El recurso `BufferedReader` se cierra automáticamente al final del bloque `try`, independientemente de si ocurrió una excepción o no.
- Esto reduce la posibilidad de olvidarse de cerrar recursos y mejora la limpieza del código.

Aquí está el desarrollo del **punto 9: Ejercicios Prácticos**.

---

### 9. **Ejercicios Prácticos**

Para consolidar el conocimiento sobre el manejo de excepciones en Java, es fundamental realizar ejercicios prácticos que permitan aplicar los conceptos aprendidos. A continuación, se presentan tres ejercicios que abarcan el manejo de excepciones verificadas, el registro de excepciones usando TinyLog, y la creación de excepciones personalizadas.

#### 9.1 Ejercicio 1: Manejo de Excepciones Verificadas

**Descripción del ejercicio**: Implemente un programa que lea el contenido de un archivo de texto que contiene números enteros. El programa debe capturar las excepciones relacionadas con la apertura y lectura del archivo, así como posibles errores al convertir los datos en números.

**Requisitos**:
- Utilice excepciones verificadas como `FileNotFoundException` y `IOException`.
- Si el archivo contiene datos no numéricos, capture la excepción `NumberFormatException` y maneje el error adecuadamente.
- Asegúrese de cerrar el archivo después de que se complete la lectura, utilizando un bloque `finally`.

##### Esqueleto del código:

```java
import java.io.*;

public class LecturaArchivo {
    public static void leerArchivo(String nombreArchivo) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(nombreArchivo));
            String linea;
            while ((linea = br.readLine()) != null) {
                try {
                    int numero = Integer.parseInt(linea);
                    System.out.println("Número leído: " + numero);
                } catch (NumberFormatException e) {
                    System.out.println("Dato no numérico en el archivo: " + linea);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                System.out.println("Error al cerrar el archivo.");
            }
        }
    }

    public static void main(String[] args) {
        leerArchivo("numeros.txt");
    }
}
```

**Resultado esperado**:
- El programa debe leer el archivo y mostrar los números. Si encuentra una línea que no puede ser convertida a número, debe manejar el error e informar al usuario sin detener la ejecución.

#### 9.2 Ejercicio 2: Registro de Excepciones con TinyLog

**Descripción del ejercicio**: Cree un programa que solicite al usuario que ingrese dos números y realice una división. Si el divisor es cero, registre la excepción utilizando **TinyLog**. Además, todas las excepciones deben registrarse en un archivo de registro (`logfile.txt`).

**Requisitos**:
- Utilice **TinyLog** para registrar las excepciones.
- El programa debe seguir ejecutándose después de manejar la excepción.
- La salida de los registros debe ser enviada a un archivo llamado `logfile.txt`.

##### Esqueleto del código:

```java
import org.tinylog.Logger;
import java.util.Scanner;

public class DivisionTinyLog {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.println("Ingrese el dividendo (o 'exit' para salir): ");
                String entrada = scanner.nextLine();
                if (entrada.equalsIgnoreCase("exit")) {
                    break;
                }
                int dividendo = Integer.parseInt(entrada);

                System.out.println("Ingrese el divisor: ");
                int divisor = Integer.parseInt(scanner.nextLine());

                int resultado = dividendo / divisor;
                System.out.println("Resultado: " + resultado);
            } catch (ArithmeticException e) {
                Logger.error(e, "Intento de división por cero.");
                System.out.println("Error: No se puede dividir por cero.");
            } catch (NumberFormatException e) {
                Logger.error(e, "Entrada no numérica.");
                System.out.println("Error: Entrada no válida.");
            }
        }

        scanner.close();
    }
}
```

**Configuración de TinyLog** (`tinylog.properties`):

```properties
tinylog.writer = file
tinylog.writer.file = logfile.txt
tinylog.writer.append = true
tinylog.level = error
```

**Resultado esperado**:
- Si el usuario ingresa un divisor de 0, se debe registrar un mensaje de error en el archivo `logfile.txt`, incluyendo la traza de la excepción. 
- Si el usuario ingresa datos no válidos, como texto en lugar de números, también debe registrarse el error.

#### 9.3 Ejercicio 3: Creación de una Excepción Personalizada y Registro

**Descripción del ejercicio**: Cree una excepción personalizada llamada `EdadInvalidaException` que se lance cuando un usuario ingrese una edad negativa o mayor de 120. La excepción debe capturarse y registrarse usando **TinyLog**.

**Requisitos**:
- Defina una clase de excepción personalizada.
- Implemente un método que solicite la edad del usuario, lance la excepción si la edad es inválida y registre el error utilizando TinyLog.
- El programa debe seguir ejecutándose después de manejar la excepción.

##### Esqueleto del código:

```java
import org.tinylog.Logger;
import java.util.Scanner;

// Definición de la excepción personalizada
class EdadInvalidaException extends Exception {
    public EdadInvalidaException(String mensaje) {
        super(mensaje);
    }
}

public class RegistroEdad {
    public static void verificarEdad(int edad) throws EdadInvalidaException {
        if (edad < 0 || edad > 120) {
            throw new EdadInvalidaException("Edad no válida: " + edad);
        }
        System.out.println("Edad aceptada: " + edad);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.println("Ingrese su edad (o 'exit' para salir): ");
                String entrada = scanner.nextLine();
                if (entrada.equalsIgnoreCase("exit")) {
                    break;
                }
                int edad = Integer.parseInt(entrada);
                verificarEdad(edad);
            } catch (EdadInvalidaException e) {
                Logger.error(e, "Excepción de edad inválida.");
                System.out.println("Error: " + e.getMessage());
            } catch (NumberFormatException e) {
                Logger.error(e, "Entrada no numérica.");
                System.out.println("Error: Entrada no válida.");
            }
        }

        scanner.close();
    }
}
```

**Resultado esperado**:
- Si el usuario ingresa una edad inválida, se lanza `EdadInvalidaException` y se registra el error en el archivo de registro.
- El programa debe continuar ejecutándose, permitiendo al usuario ingresar nuevas edades hasta que decida salir.
