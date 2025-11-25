
## 1. Objetivo del mini-tutorial

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

**Ideas clave para explicar:**

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
    
- Es más **lento** que un test unitario puro → explicar por qué normalmente preferimos muchos **unit tests** y menos **integration tests**.
    

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

## 7. Resumen de conceptos fundamentales (para la pizarra)

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
            

