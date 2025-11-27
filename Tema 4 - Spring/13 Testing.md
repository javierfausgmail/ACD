# Intro
## 1. ¿Qué es JUnit y para qué sirve?

- **Qué es**: una librería Java para escribir **tests automáticos**. Es el framework estándar de testing unitario para Java.  
    
- **Para qué sirve**:
    
    - Verificar que una clase o método hace lo que debe.
        
    - Detectar errores cuando se cambia o refactoriza código.
        
    - Integrarse con Maven/Gradle y CI para lanzar baterías de tests.
        

Hoy en día se usa sobre todo **JUnit 5 (Jupiter)**.

---

## 2. Añadir JUnit 5 a un proyecto Maven

```xml
<!-- pom.xml -->
<dependencies>
    <!-- Dependencia principal de JUnit 5 -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.10.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>

<build>
    <plugins>
        <!-- Asegura que se ejecutan los tests con JUnit 5 -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>3.2.5</version>
            <configuration>
                <useModulePath>false</useModulePath>
            </configuration>
        </plugin>
    </plugins>
</build>
```

Los tests se colocan en `src/test/java`.

---

## 3. Estructura de un test con JUnit 5

Ejemplo: hay una clase `Calculadora`:

```java
// src/main/java/com/ejemplo/Calculadora.java
package com.ejemplo;

public class Calculadora {

    public int sumar(int a, int b) {
        return a + b;
    }

    public int dividir(int a, int b) {
        if (b == 0) {
            throw new IllegalArgumentException("No se puede dividir entre cero");
        }
        return a / b;
    }
}
```

Test asociado:

```java
// src/test/java/com/ejemplo/CalculadoraTest.java
package com.ejemplo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculadoraTest {

    @Test
    void sumar_deberiaDevolverLaSuma() {
        Calculadora calc = new Calculadora();
        int resultado = calc.sumar(2, 3);

        assertEquals(5, resultado, "2 + 3 debe ser 5");
    }

    @Test
    void dividir_entreCero_deberiaLanzarExcepcion() {
        Calculadora calc = new Calculadora();

        assertThrows(IllegalArgumentException.class,
                     () -> calc.dividir(10, 0),
                     "Dividir entre cero debe lanzar IllegalArgumentException");
    }
}
```

Puntos clave:

- `@Test`: marca un método como test.
    
- `assertEquals`, `assertThrows`, etc.: verifican condiciones.
    
- Si una aserción falla → el test se marca como **fallido**.
    

---

## 4. Aserciones más comunes

Importando:

```java
import static org.junit.jupiter.api.Assertions.*;
```

Se usan, por ejemplo:

```java
assertEquals(esperado, real);
assertNotEquals(noEsperado, real);
assertTrue(condicion);
assertFalse(condicion);
assertNull(obj);
assertNotNull(obj);
assertThrows(TipoExcepcion.class, () -> { /* código que debe fallar */ });
```

Cada aserción puede llevar un mensaje al final:

```java
assertTrue(lista.isEmpty(), "La lista debería venir vacía al inicio");
```

---

## 5. Ciclo de vida del test

Para preparar y limpiar antes/después de cada test:

```java
import org.junit.jupiter.api.*;

class EjemploLifecycleTest {

    @BeforeAll
    static void antesDeTodos() {
        // Se ejecuta UNA vez antes de todos los tests de la clase
    }

    @AfterAll
    static void despuesDeTodos() {
        // Se ejecuta UNA vez al final
    }

    @BeforeEach
    void antesDeCadaTest() {
        // Se ejecuta ANTES de cada @Test
    }

    @AfterEach
    void despuesDeCadaTest() {
        // Se ejecuta DESPUÉS de cada @Test
    }

    @Test
    void test1() { }

    @Test
    void test2() { }
}
```

---

## 6. Tests parametrizados (idea rápida)

Permiten ejecutar el mismo test con varios datos:

```java
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CalculadoraParamTest {

    @ParameterizedTest
    @CsvSource({
            "1, 2, 3",
            "2, 2, 4",
            "-1, 1, 0"
    })
    void sumar_variosCasos(int a, int b, int esperado) {
        Calculadora calc = new Calculadora();
        assertEquals(esperado, calc.sumar(a, b));
    }
}
```

---

## 7. Cómo se ejecuta

En un proyecto Maven:

```bash
mvn test
```

En un IDE (IntelliJ, Eclipse, VS Code con extensión Java):

- Se puede hacer clic derecho sobre la clase de tests → **Run ‘CalculadoraTest’**.
    
- O ejecutarlos todos desde la vista de tests.
    


# Organización código de los tests

Ejemplo claro usando el estilo **Given / When / Then** (Given = contexto, When = acción, Then = verificación).

### Clase a probar

```java
// Código de producción
public class CuentaBancaria {

    private int saldo;

    public CuentaBancaria(int saldoInicial) {
        this.saldo = saldoInicial;
    }

    public void ingresar(int cantidad) {
        saldo += cantidad;
    }

    public int getSaldo() {
        return saldo;
    }
}
```

### Test con comentarios Given / When / Then

```java
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CuentaBancariaTest {

    @Test
    void ingresar_deberiaIncrementarSaldo() {
        // ============================
        // GIVEN (DADO / CONTEXTO INICIAL)
        // ============================
        // - Estado inicial del sistema bajo prueba (SUT)
        // - Objetos necesarios y datos iniciales
        CuentaBancaria cuenta = new CuentaBancaria(100);

        // ============================
        // WHEN (CUANDO / ACCIÓN)
        // ============================
        // - Acción que se quiere probar (un único “cuando” por test, si es posible)
        cuenta.ingresar(50);

        // ============================
        // THEN (ENTONCES / VERIFICACIÓN)
        // ============================
        // - Comprobaciones mediante aserciones
        // - El test pasa solo si TODAS las aserciones se cumplen
        assertEquals(150, cuenta.getSaldo(), "El saldo debe incrementarse en 50");
    }
}
```

### Variante con nombre de método en estilo Given/When/Then

```java
@Test
void givenCuentaConSaldo100_whenIngresar50_thenSaldoEs150() {
    // Given
    CuentaBancaria cuenta = new CuentaBancaria(100);

    // When
    cuenta.ingresar(50);

    // Then
    assertEquals(150, cuenta.getSaldo());
}
```

Piezas clave del patrón en el test:

- **Given** → preparar el escenario (instancias, datos iniciales).
    
- **When** → ejecutar la acción que se quiere probar.
    
- **Then** → comprobar el resultado con `assert...`.
    

Atención, porque aquí se mezclan **dos cosas distintas**:

- El estilo **Given / When / Then** → forma de **explicar/estructurar el test**.
    
- Las anotaciones **@BeforeEach / @BeforeAll** → forma de **reutilizar código de preparación**.
    

No son contradictorias, son **complementarias**.

---

## 1. ¿Qué representa realmente el “Given”?

El **Given** es el **estado inicial del test**:  
objetos creados, datos preparados, mocks configurados, etc.

Ese “Given” se puede escribir:

1. **Dentro del propio método de test**  
    (explícito en cada test, muy legible):
    
    ```java
    @Test
    void givenCuentaConSaldo100_whenIngresar50_thenSaldoEs150() {
        // GIVEN
        CuentaBancaria cuenta = new CuentaBancaria(100);
    
        // WHEN
        cuenta.ingresar(50);
    
        // THEN
        assertEquals(150, cuenta.getSaldo());
    }
    ```
    
2. **Parcialmente o totalmente en un @BeforeEach**  
    (para no repetir siempre el mismo setup):
    
    ```java
    class CuentaBancariaTest {
    
        private CuentaBancaria cuenta;
    
        @BeforeEach
        void setUp() {
            // GIVEN común a TODOS los tests de esta clase
            cuenta = new CuentaBancaria(100);
        }
    
        @Test
        void whenIngresar50_thenSaldoEs150() {
            // WHEN
            cuenta.ingresar(50);
    
            // THEN
            assertEquals(150, cuenta.getSaldo());
        }
    
        @Test
        void whenIngresar0_thenSaldoNoCambia() {
            // WHEN
            cuenta.ingresar(0);
    
            // THEN
            assertEquals(100, cuenta.getSaldo());
        }
    }
    ```
    

Fíjate: el **Given sigue existiendo**, pero parte está “escondida” en `@BeforeEach`.

---

## 2. ¿Cuándo usar Given dentro del test y cuándo @BeforeEach?

### Given **dentro del test** (recomendado cuando…)

- Quieres que el test sea **autoexplicativo** y se pueda leer de arriba a abajo sin buscar en otros métodos.
    
- El contexto inicial **cambia de un test a otro**:
    
    - `givenCuentaConSaldo100...`
        
    - `givenCuentaConSaldo0...`
        
    - `givenCuentaEnRojo...`
        
- Estás enseñando o quieres máxima claridad docente / conceptual.
    

 Ventaja: el test se lee como una historia: **Given / When / Then** sin “magia oculta”.

---

### @BeforeEach / @BeforeAll (recomendado cuando…)

- Hay un **setup repetitivo e idéntico** para muchos tests:
    
    - Crear un `EntityManager`, un `MockMvc`, un `Service` con mocks, etc.
        
- El código de preparación empieza a ser **muy verboso** y ensucia demasiado la parte interesante del test (el When/Then).
    
- Quieres evitar _boilerplate_ duplicado.
    

 Ventaja: reduces repetición y dejas el test más corto, pero escondes parte del Given fuera.

---

## 3. ¿Y @BeforeAll?

`@BeforeAll` se usa para inicializar **recursos caros/compartidos** para toda la clase:

```java
@BeforeAll
static void initAll() {
    // Por ejemplo, levantar un servidor embebido, crear pool pesado, etc.
}
```

Suele participar en el “Given” de alto nivel (infraestructura),  
pero **no en el estado concreto de cada test** (eso -> mejor `@BeforeEach` o dentro del test).

---

## 4. ¿En qué sentido son complementarios?

- El patrón **Given / When / Then** es **semántico** (forma de pensar el test).
    
- `@BeforeEach` / `@BeforeAll` son **técnicos** (dónde colocas físicamente el código).
    

Puedes tener perfectamente:

- **Given en @BeforeEach + Given extra en el test**, y luego el When/Then:
    
    ```java
    class TransferenciaTest {
    
        private Banco banco;
    
        @BeforeEach
        void setUp() {
            // Given global: banco ya creado
            banco = new Banco();
        }
    
        @Test
        void givenDosCuentasConSaldo_whenTransferir_thenSeActualizanSaldos() {
            // Given específico de este test
            Cuenta origen = new Cuenta(100);
            Cuenta destino = new Cuenta(50);
            banco.addCuenta(origen);
            banco.addCuenta(destino);
    
            // When
            banco.transferir(origen, destino, 30);
    
            // Then
            assertEquals(70, origen.getSaldo());
            assertEquals(80, destino.getSaldo());
        }
    }
    ```
    

Aquí el **Given está repartido**:

- Parte en `@BeforeEach` (crear `Banco`).
    
- Parte dentro del propio test (crear cuentas y añadirlas).
    

---

## 5. Recomendación práctica (y didáctica)

- Para ejemplos sencillos o introductorios →  
    **mantener el Given dentro del test** suele ser más claro.
    
- Para baterías grandes de tests sobre el mismo SUT con mucho código repetido →  
    mover la parte común a `@BeforeEach` y dejar en el test solo el Given que realmente cambia + When + Then.
    

En resumen:  **no hay contradicción en absoluto**.  
El patrón Given/When/Then describe el _qué_ de cada parte del test;  
los `@Before...` solo te ayudan a decidir _dónde_ pones físicamente el código del Given.


# Mini-Proyecto
 
 
## 1. Objetivo del mini-proyecto

- Entender **qué es un test unitario** y qué es un test de integración.
    
- Ver las anotaciones básicas de **JUnit 5**: `@Test`, `@BeforeEach`, `@AfterEach`, etc.
    
- Aprender a usar **Spring Boot Test** (`@SpringBootTest`, `@WebMvcTest`) para probar servicios y controladores.
    
- Ver un ejemplo de **mocking** sencillo con Mockito.
    

Todo ello con un pequeño proyecto demo.

---

## 2. Crear el proyecto demo

### 2.1. Estructura propuesta

Paquete base: `com.example.testingdemo`

```text
testing-demo/
 ├─ pom.xml
 └─ src
    ├─ main
    │   └─ java
    │       └─ com/example/testingdemo
    │           ├─ TestingDemoApplication.java
    │           ├─ service
    │           │   └─ CalculatorService.java
    │           └─ web
    │               └─ CalculatorController.java
    └─ test
        └─ java
            └─ com/example/testingdemo
                ├─ service
                │   ├─ CalculatorServiceTest.java        (JUnit puro)
                │   └─ CalculatorServiceSpringTest.java  (@SpringBootTest)
                └─ web
                    └─ CalculatorControllerTest.java     (@WebMvcTest)
```

### 2.2. `pom.xml` mínimo (Maven)

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>testing-demo</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>testing-demo</name>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.3.0</version><!-- o la versión que uses -->
    </parent>

    <properties>
        <java.version>17</java.version>
    </properties>

    <dependencies>
        <!-- App web básica -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- Dependencias de test: JUnit 5, Mockito, AssertJ, etc. -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## 3. Código de ejemplo (producción)

### 3.1. Clase principal

```java
// src/main/java/com/example/testingdemo/TestingDemoApplication.java
package com.example.testingdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestingDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestingDemoApplication.class, args);
    }
}
```

### 3.2. Servicio sencillo a testear

```java
// src/main/java/com/example/testingdemo/service/CalculatorService.java
package com.example.testingdemo.service;

import org.springframework.stereotype.Service;

@Service
public class CalculatorService {

    public int add(int a, int b) {
        return a + b;
    }

    public int divide(int a, int b) {
        if (b == 0) {
            throw new IllegalArgumentException("No se puede dividir por cero");
        }
        return a / b;
    }
}
```

### 3.3. Controlador REST

```java
// src/main/java/com/example/testingdemo/web/CalculatorController.java
package com.example.testingdemo.web;

import com.example.testingdemo.service.CalculatorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculatorController {

    private final CalculatorService calculatorService;

    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @GetMapping("/api/add")
    public int add(@RequestParam int a, @RequestParam int b) {
        return calculatorService.add(a, b);
    }
}
```

---

## 4. Conceptos básicos de JUnit 5 (sin Spring)

### 4.1. Primer test unitario

```java
// src/test/java/com/example/testingdemo/service/CalculatorServiceTest.java
package com.example.testingdemo.service;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorServiceTest {

    private CalculatorService calculatorService;

    @BeforeEach
    void setUp() {
        calculatorService = new CalculatorService(); // sin Spring, new "a pelo"
    }

    @AfterEach
    void tearDown() {
        // aquí podrías liberar recursos si hiciera falta
    }

    @Test
    void add_deberiaSumarCorrectamente() {
        // Given
        int a = 2;
        int b = 3;

        // When
        int resultado = calculatorService.add(a, b);

        // Then
        assertEquals(5, resultado, "2 + 3 debe ser 5");
    }

    @Test
    void divide_deberiaLanzarExcepcionSiDivisorEsCero() {
        // Given
        int a = 10;
        int b = 0;

        // When & Then
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> calculatorService.divide(a, b)
        );

        assertEquals("No se puede dividir por cero", ex.getMessage());
    }
}
```

**Ideas clave:**

- `@Test`: marca un método como caso de prueba.
    
- `assertEquals`, `assertThrows`: comparan resultado esperado vs real.
    
- Patrón **Given–When–Then** para estructurar mentalmente el test.
    
- Tests **unitarios puros**: no dependen del contenedor de Spring.
    

---

## 5. Tests con Spring: `@SpringBootTest`

Cuando queremos probar **integración con Spring** (inyección de dependencias, configuración, etc.), usamos `@SpringBootTest`.

```java
// src/test/java/com/example/testingdemo/service/CalculatorServiceSpringTest.java
package com.example.testingdemo.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CalculatorServiceSpringTest {

    @Autowired
    private CalculatorService calculatorService;

    @Test
    void add_usaBeanDeSpring() {
        // Given
        int a = 10;
        int b = 20;

        // When
        int resultado = calculatorService.add(a, b);

        // Then
        assertEquals(30, resultado);
    }
}
```

**Ideas clave:**

- `@SpringBootTest` levanta el contexto de Spring (similar a arrancar la app).
    
- Útil para **tests de integración** donde nos interesa comprobar que los beans se crean bien, funcionan juntos, etc.
    
- Es más **lento** que un test unitario puro → por esonormalmente preferimos muchos **unit tests** y menos **integration tests**.
    

---

## 6. Test de controlador con `@WebMvcTest` + MockMvc

Para testear solo la **capa web** (sin levantar toda la app) usamos **slice tests**:

```java
// src/test/java/com/example/testingdemo/web/CalculatorControllerTest.java
package com.example.testingdemo.web;

import com.example.testingdemo.service.CalculatorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CalculatorController.class)
class CalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Spring creará un mock de este bean y lo inyectará en el controlador
    @MockBean
    private CalculatorService calculatorService;

    @Test
    void add_endpointDebeDevolverResultadoCorrecto() throws Exception {
        // Given
        when(calculatorService.add(2, 3)).thenReturn(5);

        // When & Then
        mockMvc.perform(get("/api/add")
                        .param("a", "2")
                        .param("b", "3"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }
}
```

**Conceptos para comentar:**

- `@WebMvcTest(CalculatorController.class)`:
    
    - Carga **solo** la parte MVC (controladores, mappings, etc.).
        
    - No levanta la app completa → tests más rápidos.
        
- `MockMvc`: simula peticiones HTTP al controlador sin abrir un servidor real.
    
- `@MockBean`:
    
    - Sustituye el bean real de `CalculatorService` por un **mock de Mockito**.
        
    - Eso nos permite controlar sus respuestas (`when(...).thenReturn(...)`).
        

---

## 7. Resumen de conceptos fundamentales

1. **Tipos de tests**
    
    - **Unit tests**:
        
        - Usan JUnit directamente.
            
        - No necesitan Spring.
            
        - Rápidos y numerosos.
            
    - **Integration tests**:
        
        - Usan `@SpringBootTest` o “slice tests” (`@WebMvcTest`, `@DataJpaTest`…).
            
        - Comprueban que varias piezas funcionan **juntas** correctamente.
            
2. **JUnit 5 básico**
    
    - `@Test`, `@BeforeEach`, `@AfterEach`, `@BeforeAll`, `@AfterAll`.
        
    - Asserts: `assertEquals`, `assertTrue`, `assertFalse`, `assertThrows`, `assertAll`, etc.
        
    - Organización del código en **Given–When–Then**.
        
3. **Testing con Spring**
    
    - `@SpringBootTest`: todo el contexto → integración.
        
    - `@WebMvcTest`: solo capa web → tests rápidos de controladores.
        
    - `@MockBean`: sustituye beans reales por **mocks** de Mockito.
        
4. **Buenas prácticas desde el principio**
    
    - Un test debe ser:
        
        - **Repetible** (mismos datos → mismo resultado).
            
        - **Independiente** (no depende del orden de ejecución).
            
        - **Claro de leer**: el test es una **documentación viva** del comportamiento.
            

