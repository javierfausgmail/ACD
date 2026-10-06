# Arquitectura de software: DDD y Arquitectura Hexagonal

Este tutorial introduce dos ideas relacionadas pero distintas:

- **Domain-Driven Design (DDD)**: ayuda a modelar correctamente el problema de negocio.
- **Arquitectura Hexagonal**, también llamada **Ports and Adapters**: ayuda a mantener la lógica de la aplicación separada de tecnologías externas como bases de datos, interfaces gráficas, APIs o archivos.

Los ejemplos utilizan Java y avanzan desde un nivel sencillo hasta un nivel medio.

> Importante: el nombre correcto del patrón es **Ports and Adapters** —Puertos y Adaptadores—. Un adaptador puede transformar datos entre formatos, pero “transformer” no es una pieza arquitectónica propia de la Arquitectura Hexagonal.

---

## 1. El problema que intentamos resolver

En un programa pequeño podríamos escribir algo así:

~~~java
public class PedidoService {

    public void crearPedido(String producto, int cantidad, double precio) {
        // calcular total
        // guardar en base de datos
        // enviar correo
        // mostrar resultado
    }
}
~~~

Al crecer la aplicación, una misma clase puede terminar ocupándose de:

- reglas de negocio;
- persistencia;
- llamadas HTTP;
- interfaz de usuario;
- envío de correos;
- validaciones.

El resultado suele ser código más difícil de probar, modificar y reutilizar.

DDD y Arquitectura Hexagonal ayudan a separar estas responsabilidades, aunque lo hacen desde perspectivas diferentes.

---

# 2. Domain-Driven Design (DDD)

## 2.1 El dominio en el centro

DDD propone diseñar el software alrededor del **dominio**, es decir, del problema real que la aplicación resuelve.

En una aplicación de pedidos, los conceptos importantes deberían ser:

~~~text
Pedido
Producto
Línea de pedido
Precio
Cliente
Confirmar pedido
Cancelar pedido
~~~

y no empezar pensando principalmente en:

~~~text
MySQL
REST
JSON
Spring
Hibernate
~~~

La tecnología es necesaria, pero no debería definir el modelo del negocio.

---

## 2.2 Lenguaje ubicuo

DDD propone utilizar un **lenguaje ubicuo**: desarrolladores y personas expertas en el negocio utilizan los mismos términos.

Si en el negocio se habla de:

> “añadir una línea al pedido”

es preferible que el código exprese esa misma idea:

~~~java
pedido.agregarLinea(linea);
~~~

en lugar de ocultarla detrás de nombres puramente técnicos.

---

## 2.3 Entidades

Una **entidad** es un objeto cuya identidad importa a lo largo del tiempo.

~~~java
import java.util.UUID;

public class Pedido {

    private final UUID id;

    public Pedido(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
~~~

Dos pedidos pueden contener los mismos productos y cantidades, pero siguen siendo pedidos distintos porque tienen identificadores diferentes.

---

## 2.4 Objetos valor

Un **Value Object** u **Objeto Valor** se define principalmente por sus valores y normalmente no necesita una identidad propia.

~~~java
import java.math.BigDecimal;

public record LineaPedido(
        String producto,
        BigDecimal precioUnitario,
        int cantidad
) {

    public LineaPedido {
        if (producto == null || producto.isBlank()) {
            throw new IllegalArgumentException("El producto es obligatorio");
        }

        if (precioUnitario.signum() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }

        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
    }

    public BigDecimal subtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
}
~~~

Aquí aparece una idea importante: **las reglas del dominio se colocan cerca de los objetos a los que pertenecen**.

No permitimos crear una línea de pedido con una cantidad negativa.

---

# 3. Ejemplo incremental: modelar un pedido

Añadimos comportamiento a la entidad Pedido:

~~~java
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Pedido {

    private final UUID id;
    private final List<LineaPedido> lineas = new ArrayList<>();

    public Pedido(UUID id) {
        this.id = id;
    }

    public void agregarLinea(LineaPedido linea) {
        lineas.add(linea);
    }

    public BigDecimal total() {
        return lineas.stream()
                .map(LineaPedido::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public UUID getId() {
        return id;
    }

    public List<LineaPedido> getLineas() {
        return List.copyOf(lineas);
    }
}
~~~

Uso:

~~~java
Pedido pedido = new Pedido(UUID.randomUUID());

pedido.agregarLinea(
        new LineaPedido(
                "Teclado",
                new BigDecimal("35.50"),
                2
        )
);

pedido.agregarLinea(
        new LineaPedido(
                "Ratón",
                new BigDecimal("18.90"),
                1
        )
);

System.out.println(pedido.total());
~~~

La lógica para calcular el total pertenece al propio modelo.

La clase Pedido no necesita saber nada de SQL, HTTP o archivos.

---

## 3.1 Aggregate

DDD utiliza el concepto de **Aggregate** para agrupar objetos que deben mantener sus reglas de forma conjunta.

En nuestro ejemplo:

~~~text
Pedido                 <- raíz del agregado
│
├── LineaPedido
├── LineaPedido
└── LineaPedido
~~~

Pedido actúa como **Aggregate Root**.

Desde fuera no modificamos directamente su lista interna. Utilizamos operaciones controladas:

~~~java
pedido.agregarLinea(linea);
~~~

La raíz del agregado protege la consistencia del conjunto.

---

## 3.2 Repository

Un **Repository** representa conceptualmente una colección de agregados.

El sistema puede declarar que necesita guardar y recuperar pedidos:

~~~java
import java.util.Optional;
import java.util.UUID;

public interface PedidoRepository {

    void guardar(Pedido pedido);

    Optional<Pedido> buscarPorId(UUID id);
}
~~~

Observe que aquí no aparece JDBC, JPA, MySQL ni MongoDB.

La interfaz expresa **qué necesita la aplicación**, no qué tecnología utilizará.

---

# 4. Arquitectura Hexagonal

La Arquitectura Hexagonal fue propuesta por Alistair Cockburn y también se conoce como **Ports and Adapters**.

La aplicación se sitúa en el centro y se comunica con el exterior mediante puertos y adaptadores.

~~~mermaid
flowchart LR
    CLI[Consola] --> IN[Puerto de entrada]
    REST[API REST] --> IN

    IN --> APP[Aplicación y dominio]

    APP --> OUT[Puerto de salida]

    OUT --> MEM[Adaptador memoria]
    OUT --> JSON[Adaptador JSON]
    OUT --> SQL[Adaptador JDBC]
~~~

La palabra “hexagonal” no significa que existan seis capas obligatorias. El hexágono es simplemente una forma gráfica de representar varios puntos de conexión con el exterior.

---

# 5. Puertos

Un **puerto** define un contrato de comunicación.

En Java suele representarse mediante una interfaz.

## 5.1 Puerto de entrada

Representa algo que el exterior puede pedirle a la aplicación.

~~~java
import java.util.List;
import java.util.UUID;

public interface CrearPedidoUseCase {

    UUID crearPedido(List<LineaPedido> lineas);
}
~~~

Una interfaz gráfica, una API REST o una aplicación de consola podrían utilizar este mismo puerto.

## 5.2 Puerto de salida

Representa algo que la aplicación necesita pedir al exterior.

Nuestro PedidoRepository es un buen ejemplo:

~~~java
public interface PedidoRepository {

    void guardar(Pedido pedido);

    Optional<Pedido> buscarPorId(UUID id);
}
~~~

La aplicación necesita persistencia, pero no necesita conocer todavía cómo se implementará.

---

# 6. Adaptadores

Un **adaptador** conecta una tecnología concreta con un puerto.

Por ejemplo, implementamos el repositorio en memoria:

~~~java
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class PedidoRepositoryMemoria implements PedidoRepository {

    private final Map<UUID, Pedido> datos = new HashMap<>();

    @Override
    public void guardar(Pedido pedido) {
        datos.put(pedido.getId(), pedido);
    }

    @Override
    public Optional<Pedido> buscarPorId(UUID id) {
        return Optional.ofNullable(datos.get(id));
    }
}
~~~

Más adelante podríamos crear:

~~~text
PedidoRepositoryMemoria
PedidoRepositoryJson
PedidoRepositoryJdbc
PedidoRepositoryJpa
~~~

Todos implementarían el mismo puerto.

---

# 7. Caso de uso de aplicación

Implementamos ahora el puerto de entrada:

~~~java
import java.util.List;
import java.util.UUID;

public class CrearPedidoService implements CrearPedidoUseCase {

    private final PedidoRepository repository;

    public CrearPedidoService(PedidoRepository repository) {
        this.repository = repository;
    }

    @Override
    public UUID crearPedido(List<LineaPedido> lineas) {

        Pedido pedido = new Pedido(UUID.randomUUID());

        for (LineaPedido linea : lineas) {
            pedido.agregarLinea(linea);
        }

        repository.guardar(pedido);

        return pedido.getId();
    }
}
~~~

CrearPedidoService depende de la abstracción PedidoRepository, no de una base de datos concreta.

Eso permite sustituir la persistencia sin modificar el caso de uso.

---

# 8. Adaptador de entrada: consola

En este tutorial utilizaremos **únicamente una consola de texto** como entrada de la aplicación.

No necesitamos HTTP, una API REST ni JavaFX para entender la Arquitectura Hexagonal.

El usuario interactuará mediante comandos sencillos y la consola será nuestro **adaptador de entrada**.

Una primera versión puede ser tan simple como:

~~~java
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {

        PedidoRepository repository =
                new PedidoRepositoryMemoria();

        CrearPedidoUseCase crearPedido =
                new CrearPedidoService(repository);

        List<LineaPedido> lineas = List.of(
                new LineaPedido(
                        "Monitor",
                        new BigDecimal("189.90"),
                        2
                ),
                new LineaPedido(
                        "Teclado",
                        new BigDecimal("35.50"),
                        1
                )
        );

        UUID pedidoId = crearPedido.crearPedido(lineas);

        System.out.println("Pedido creado: " + pedidoId);
    }
}
~~~

Más adelante, para practicar mejor la separación de responsabilidades, podemos hacer que `Main` se limite a montar las dependencias y delegue la interacción en una clase `ConsoleController`.

Por ejemplo:

~~~java
public class Main {

    public static void main(String[] args) {

        PedidoRepository repository =
                new PedidoRepositoryMemoria();

        CrearPedidoUseCase crearPedido =
                new CrearPedidoService(repository);

        ConsoleController controller =
                new ConsoleController(crearPedido);

        controller.iniciar();
    }
}
~~~

El `ConsoleController` puede utilizar `Scanner` para mostrar un pequeño menú, leer datos y llamar al caso de uso correspondiente.

Así el flujo queda:

~~~text
Usuario
  │
  ▼
Consola / Scanner
  │
  ▼
ConsoleController
  │
  ▼
CrearPedidoUseCase
  │
  ▼
CrearPedidoService
~~~

La idea importante es que **la lógica de negocio no se coloca dentro del controlador de consola**. La consola recoge datos y llama a la aplicación.

---

# 9. Puertos y adaptadores primarios y secundarios

También se utiliza esta terminología:

| Tipo | También llamado | Ejemplo |
|---|---|---|
| Puerto primario | Puerto de entrada | CrearPedidoUseCase |
| Adaptador primario | Driving adapter | REST, consola, GUI |
| Puerto secundario | Puerto de salida | PedidoRepository |
| Adaptador secundario | Driven adapter | memoria, JSON, JDBC |

Una forma sencilla de recordarlo:

~~~text
ACTOR EXTERNO
     │
     ▼
Adaptador de entrada
     │
     ▼
Puerto de entrada
     │
     ▼
APLICACIÓN
     │
     ▼
Puerto de salida
     │
     ▼
Adaptador de salida
     │
     ▼
TECNOLOGÍA EXTERNA
~~~

---

# 10. Relación entre DDD y Arquitectura Hexagonal

No son lo mismo.

### DDD pregunta

> ¿Cómo representamos correctamente el dominio y sus reglas?

Trabaja con conceptos como:

- lenguaje ubicuo;
- entidades;
- objetos valor;
- agregados;
- repositorios;
- servicios de dominio;
- bounded contexts.

### Arquitectura Hexagonal pregunta

> ¿Cómo evitamos que el núcleo de la aplicación dependa de tecnologías externas?

Trabaja con:

- puertos;
- adaptadores;
- límites;
- dirección de dependencias;
- sustitución de tecnologías.

Por eso combinan especialmente bien:

~~~text
Tecnologías externas
        │
   Adaptadores
        │
     Puertos
        │
┌─────────────────────┐
│     Aplicación      │
│                     │
│  Modelo de dominio  │
│        DDD          │
└─────────────────────┘
~~~

Una aplicación puede utilizar DDD sin Arquitectura Hexagonal y también puede aplicar Hexagonal sin utilizar DDD de forma completa.

La combinación resulta especialmente útil cuando existe un dominio con cierta complejidad.

---

# 11. Una posible estructura de paquetes

~~~text
src/main/java
│
├── domain
│   ├── Pedido.java
│   └── LineaPedido.java
│
├── application
│   ├── CrearPedidoUseCase.java
│   └── CrearPedidoService.java
│
├── ports
│   └── PedidoRepository.java
│
└── adapters
    ├── in
    │   └── Main.java
    │
    └── out
        └── PedidoRepositoryMemoria.java
~~~

Esta es solo una posibilidad.

Arquitectura Hexagonal no obliga a utilizar unos nombres concretos de carpetas. Lo importante son **los límites y la dirección de las dependencias**.

---

# 12. Probar sin base de datos

Gracias al adaptador en memoria podemos probar el caso de uso sin instalar una base de datos:

~~~java
PedidoRepository repository =
        new PedidoRepositoryMemoria();

CrearPedidoUseCase servicio =
        new CrearPedidoService(repository);

UUID id = servicio.crearPedido(
        List.of(
            new LineaPedido(
                "Teclado",
                new BigDecimal("30.00"),
                2
            )
        )
);

Pedido pedido = repository.buscarPorId(id).orElseThrow();

System.out.println(pedido.total());
~~~

No necesitamos levantar MySQL, crear tablas ni configurar JDBC.

Esta posibilidad de ejecutar y probar la aplicación aislada de dispositivos externos es una de las ideas centrales de Ports and Adapters.

---

# 13. Nivel medio opcional: cambiar la persistencia a JDBC

Después de haber visto memoria y JSON, podemos dar un paso más.

Supongamos que queremos utilizar JDBC.

Creamos otro adaptador:

~~~java
public class PedidoRepositoryJdbc implements PedidoRepository {

    @Override
    public void guardar(Pedido pedido) {
        // INSERT o UPDATE mediante JDBC
    }

    @Override
    public Optional<Pedido> buscarPorId(UUID id) {
        // SELECT mediante JDBC
        return Optional.empty();
    }
}
~~~

La composición cambia:

~~~java
PedidoRepository repository =
        new PedidoRepositoryJdbc();

CrearPedidoUseCase crearPedido =
        new CrearPedidoService(repository);
~~~

Pero no necesitamos modificar:

~~~text
Pedido
LineaPedido
CrearPedidoUseCase
CrearPedidoService
~~~

La tecnología cambia; el núcleo permanece estable.

---

# 14. DDD estratégico: Bounded Context

DDD también contiene conceptos de nivel superior.

Uno de los más importantes es **Bounded Context**.

Imagine una aplicación de comercio electrónico grande:

~~~text
CATÁLOGO
- Producto
- Categoría
- Precio mostrado

PEDIDOS
- Pedido
- Línea de pedido
- Estado

FACTURACIÓN
- Factura
- Impuesto
- Pago

LOGÍSTICA
- Envío
- Dirección
- Transportista
~~~

La palabra Producto puede tener un significado diferente en Catálogo, Pedidos o Logística.

DDD evita intentar crear un único modelo gigantesco para toda la organización. Cada contexto mantiene un modelo coherente para su parte del problema.

Para aplicaciones pequeñas no es necesario empezar creando muchos bounded contexts, pero es importante conocer la idea antes de trabajar con sistemas grandes.

---

# 15. Errores frecuentes

### Pensar que DDD significa crear muchas clases

DDD no consiste en añadir patrones porque sí. Tiene sentido cuando ayudan a expresar reglas reales del dominio.

### Diseñar el dominio copiando directamente las tablas

El modelo de dominio representa conceptos del negocio; no tiene por qué ser una copia de la base de datos.

### Introducir JDBC dentro de una entidad

Esto mezcla dominio y tecnología:

~~~java
public class Pedido {

    public void guardarEnMySQL() {
        // ...
    }
}
~~~

Es preferible separar:

~~~text
Pedido                 dominio
PedidoRepository       puerto
PedidoRepositoryJdbc   adaptador
~~~

### Crear una interfaz para cada clase

Hexagonal no significa crear interfaces indiscriminadamente.

Los puertos representan límites significativos de comunicación.

### Pensar que existen seis capas

El hexágono no representa seis capas obligatorias.

---

# 16. Resumen

| Concepto | Idea principal |
|---|---|
| DDD | Modelar el negocio y sus reglas |
| Lenguaje ubicuo | Utilizar los términos reales del dominio |
| Entidad | Objeto con identidad |
| Value Object | Objeto definido por sus valores |
| Aggregate | Unidad que mantiene reglas y consistencia |
| Repository | Abstracción para almacenar y recuperar agregados |
| Puerto | Contrato de comunicación |
| Adaptador | Implementación que conecta una tecnología con un puerto |
| Arquitectura Hexagonal | Mantener el núcleo independiente del exterior |
| Bounded Context | Límite dentro del que un modelo mantiene un significado concreto |

La relación puede resumirse así:

> **DDD intenta que el código se parezca al negocio. Arquitectura Hexagonal intenta que el negocio no dependa de la tecnología.**

---

# Práctica guiada: proyecto Maven completo con DDD y Arquitectura Hexagonal

En esta práctica construiremos desde cero una pequeña aplicación de pedidos.

El objetivo no es memorizar una estructura de carpetas, sino entender **qué responsabilidad tiene cada pieza** y comprobar que podemos cambiar la tecnología de persistencia sin modificar el dominio ni el caso de uso.

Al terminar tendremos esta estructura:

~~~text
pedidos-hexagonal
├── pom.xml
├── crear-pedido.json
└── src
    └── main
        └── java
            └── com
                └── ejemplo
                    └── pedidos
                        ├── domain
                        │   ├── Pedido.java
                        │   └── LineaPedido.java
                        ├── application
                        │   ├── CrearPedidoUseCase.java
                        │   └── CrearPedidoService.java
                        ├── ports
                        │   └── PedidoRepository.java
                        └── adapters
                            ├── in
                            │   ├── Main.java
                            │   └── CrearPedidoDesdeJson.java
                            └── out
                                ├── PedidoRepositoryMemoria.java
                                └── PedidoRepositoryJson.java
~~~

---

## Paso 1. Crear el proyecto Maven

Cree una carpeta llamada:

~~~text
pedidos-hexagonal
~~~

Dentro cree el fichero:

~~~text
pom.xml
~~~

con este contenido:

~~~xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                             https://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <groupId>com.ejemplo</groupId>
    <artifactId>pedidos-hexagonal</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.release>21</maven.compiler.release>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.22.3</version>
        </dependency>
    </dependencies>

</project>
~~~

La mayor parte del ejemplo utiliza únicamente la biblioteca estándar de Java.

La dependencia de **Jackson** se utilizará más adelante para implementar el adaptador que guarda los pedidos en un fichero JSON.

---

## Paso 2. Crear el dominio

Cree el paquete:

~~~text
com.ejemplo.pedidos.domain
~~~

### 2.1 Crear LineaPedido

Cree:

~~~text
LineaPedido.java
~~~

~~~java
package com.ejemplo.pedidos.domain;

import java.math.BigDecimal;

public record LineaPedido(
        String producto,
        BigDecimal precioUnitario,
        int cantidad
) {

    public LineaPedido {

        if (producto == null || producto.isBlank()) {
            throw new IllegalArgumentException(
                    "El producto es obligatorio"
            );
        }

        if (precioUnitario == null ||
                precioUnitario.signum() < 0) {
            throw new IllegalArgumentException(
                    "El precio no puede ser negativo"
            );
        }

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser positiva"
            );
        }
    }

    public BigDecimal subtotal() {
        return precioUnitario.multiply(
                BigDecimal.valueOf(cantidad)
        );
    }
}
~~~

### ¿Qué acabamos de hacer?

Hemos creado un **Value Object**.

LineaPedido mantiene algunas reglas sencillas del dominio:

- debe existir un producto;
- el precio no puede ser negativo;
- la cantidad debe ser positiva.

También sabe calcular su propio subtotal.

Todavía no existe ninguna base de datos, API REST ni interfaz gráfica.

---

## Paso 3. Crear la entidad Pedido

En el mismo paquete cree:

~~~text
Pedido.java
~~~

~~~java
package com.ejemplo.pedidos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Pedido {

    private final UUID id;
    private final List<LineaPedido> lineas =
            new ArrayList<>();

    public Pedido(UUID id) {

        if (id == null) {
            throw new IllegalArgumentException(
                    "El id es obligatorio"
            );
        }

        this.id = id;
    }

    public void agregarLinea(LineaPedido linea) {

        if (linea == null) {
            throw new IllegalArgumentException(
                    "La línea no puede ser null"
            );
        }

        lineas.add(linea);
    }

    public BigDecimal total() {
        return lineas.stream()
                .map(LineaPedido::subtotal)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }

    public UUID getId() {
        return id;
    }

    public List<LineaPedido> getLineas() {
        return List.copyOf(lineas);
    }
}
~~~

### ¿Qué representa?

Pedido es una **entidad** porque tiene identidad propia mediante su UUID.

También actúa como **Aggregate Root**:

~~~text
Pedido
│
├── LineaPedido
├── LineaPedido
└── LineaPedido
~~~

Desde fuera no modificamos directamente la lista interna.

Utilizamos:

~~~java
pedido.agregarLinea(linea);
~~~

---

## Paso 4. Crear el puerto de salida

Ahora necesitamos guardar pedidos.

Pero todavía no queremos decidir si utilizaremos MySQL, un fichero o memoria.

Cree el paquete:

~~~text
com.ejemplo.pedidos.ports
~~~

y dentro:

~~~text
PedidoRepository.java
~~~

~~~java
package com.ejemplo.pedidos.ports;

import com.ejemplo.pedidos.domain.Pedido;

import java.util.Optional;
import java.util.UUID;

public interface PedidoRepository {

    void guardar(Pedido pedido);

    Optional<Pedido> buscarPorId(UUID id);
}
~~~

### ¿Por qué una interfaz?

La aplicación expresa:

> Necesito poder guardar y recuperar pedidos.

Pero todavía no dice **cómo**.

Este es un **puerto de salida**.

---

## Paso 5. Crear el puerto de entrada

Cree el paquete:

~~~text
com.ejemplo.pedidos.application
~~~

y dentro:

~~~text
CrearPedidoUseCase.java
~~~

~~~java
package com.ejemplo.pedidos.application;

import com.ejemplo.pedidos.domain.LineaPedido;

import java.util.List;
import java.util.UUID;

public interface CrearPedidoUseCase {

    UUID crearPedido(
            List<LineaPedido> lineas
    );
}
~~~

Este puerto expresa una acción que el exterior puede pedir a nuestra aplicación:

~~~text
Crear un pedido
~~~

Es un **puerto de entrada**.

---

## Paso 6. Implementar el caso de uso

En el mismo paquete cree:

~~~text
CrearPedidoService.java
~~~

~~~java
package com.ejemplo.pedidos.application;

import com.ejemplo.pedidos.domain.LineaPedido;
import com.ejemplo.pedidos.domain.Pedido;
import com.ejemplo.pedidos.ports.PedidoRepository;

import java.util.List;
import java.util.UUID;

public class CrearPedidoService
        implements CrearPedidoUseCase {

    private final PedidoRepository repository;

    public CrearPedidoService(
            PedidoRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public UUID crearPedido(
            List<LineaPedido> lineas
    ) {

        Pedido pedido =
                new Pedido(UUID.randomUUID());

        for (LineaPedido linea : lineas) {
            pedido.agregarLinea(linea);
        }

        repository.guardar(pedido);

        return pedido.getId();
    }
}
~~~

### Observe la dependencia

CrearPedidoService conoce:

~~~text
Pedido
LineaPedido
PedidoRepository
~~~

Pero no conoce:

~~~text
MySQL
JDBC
MongoDB
archivos
~~~

Depende del **puerto**, no de la tecnología.

---

## Paso 7. Crear el adaptador de salida en memoria

Ahora sí elegimos una primera tecnología de persistencia.

Será muy sencilla: un Map en memoria.

Cree el paquete:

~~~text
com.ejemplo.pedidos.adapters.out
~~~

y dentro:

~~~text
PedidoRepositoryMemoria.java
~~~

~~~java
package com.ejemplo.pedidos.adapters.out;

import com.ejemplo.pedidos.domain.Pedido;
import com.ejemplo.pedidos.ports.PedidoRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class PedidoRepositoryMemoria
        implements PedidoRepository {

    private final Map<UUID, Pedido> datos =
            new HashMap<>();

    @Override
    public void guardar(Pedido pedido) {
        datos.put(
                pedido.getId(),
                pedido
        );
    }

    @Override
    public Optional<Pedido> buscarPorId(
            UUID id
    ) {
        return Optional.ofNullable(
                datos.get(id)
        );
    }
}
~~~

Esta clase es un **adaptador de salida**.

Implementa el contrato PedidoRepository utilizando una tecnología concreta: memoria.

---

## Paso 8. Crear el adaptador de entrada

Utilizaremos una aplicación de consola.

Cree el paquete:

~~~text
com.ejemplo.pedidos.adapters.in
~~~

y dentro:

~~~text
Main.java
~~~

~~~java
package com.ejemplo.pedidos.adapters.in;

import com.ejemplo.pedidos.application.CrearPedidoService;
import com.ejemplo.pedidos.application.CrearPedidoUseCase;
import com.ejemplo.pedidos.domain.LineaPedido;
import com.ejemplo.pedidos.domain.Pedido;
import com.ejemplo.pedidos.ports.PedidoRepository;
import com.ejemplo.pedidos.adapters.out.PedidoRepositoryMemoria;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {

        PedidoRepository repository =
                new PedidoRepositoryMemoria();

        CrearPedidoUseCase crearPedido =
                new CrearPedidoService(
                        repository
                );

        List<LineaPedido> lineas =
                List.of(
                    new LineaPedido(
                        "Monitor",
                        new BigDecimal("189.90"),
                        2
                    ),
                    new LineaPedido(
                        "Teclado",
                        new BigDecimal("35.50"),
                        1
                    )
                );

        UUID id =
                crearPedido.crearPedido(
                        lineas
                );

        Pedido pedido =
                repository
                    .buscarPorId(id)
                    .orElseThrow();

        System.out.println(
                "Pedido creado: " + id
        );

        System.out.println(
                "Total: " + pedido.total()
        );
    }
}
~~~

La consola es ahora nuestro **adaptador de entrada**.

Es la pieza que inicia la interacción con la aplicación.

---

## Paso 8.1. Segundo adaptador de entrada: fichero JSON

Ya tenemos una entrada por consola. Ahora añadiremos una segunda forma de iniciar exactamente el mismo caso de uso: un fichero JSON.

Cree en la raíz del proyecto:

~~~text
crear-pedido.json
~~~

con este contenido:

~~~json
{
  "lineas": [
    {
      "producto": "Monitor",
      "precioUnitario": 189.90,
      "cantidad": 2
    },
    {
      "producto": "Teclado",
      "precioUnitario": 35.50,
      "cantidad": 1
    }
  ]
}
~~~

Este fichero **no es el repositorio de pedidos**.

Representa una petición de entrada: “cree un pedido con estas líneas”.

Ahora cree en:

~~~text
com.ejemplo.pedidos.adapters.in
~~~

el fichero:

~~~text
CrearPedidoDesdeJson.java
~~~

~~~java
package com.ejemplo.pedidos.adapters.in;

import com.ejemplo.pedidos.application.CrearPedidoUseCase;
import com.ejemplo.pedidos.domain.LineaPedido;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public class CrearPedidoDesdeJson {

    private final CrearPedidoUseCase crearPedido;
    private final ObjectMapper mapper =
            new ObjectMapper();

    public CrearPedidoDesdeJson(
            CrearPedidoUseCase crearPedido
    ) {
        this.crearPedido = crearPedido;
    }

    public UUID procesar(Path archivo) {

        try {

            PedidoEntradaJson entrada =
                    mapper.readValue(
                        archivo.toFile(),
                        PedidoEntradaJson.class
                    );

            List<LineaPedido> lineas =
                    entrada.lineas()
                           .stream()
                           .map(linea ->
                               new LineaPedido(
                                   linea.producto(),
                                   linea.precioUnitario(),
                                   linea.cantidad()
                               )
                           )
                           .toList();

            return crearPedido.crearPedido(lineas);

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public record PedidoEntradaJson(
            List<LineaEntradaJson> lineas
    ) {}

    public record LineaEntradaJson(
            String producto,
            BigDecimal precioUnitario,
            int cantidad
    ) {}
}
~~~

### ¿Qué hace este adaptador?

La clase realiza tres tareas:

1. lee el fichero `crear-pedido.json`;
2. transforma sus datos a objetos `LineaPedido`;
3. llama al mismo puerto de entrada `CrearPedidoUseCase`.

No contiene la lógica para crear el pedido.

Esa lógica continúa en:

~~~text
CrearPedidoService
~~~

Por tanto tenemos dos formas distintas de activar el mismo caso de uso:

~~~text
Consola
   │
   └──► CrearPedidoUseCase

crear-pedido.json
   │
   └──► CrearPedidoDesdeJson
              │
              └──► CrearPedidoUseCase
~~~

### Probar la entrada JSON

En `Main`, después de crear `CrearPedidoUseCase`, puede ejecutar:

~~~java
CrearPedidoDesdeJson entradaJson =
        new CrearPedidoDesdeJson(
                crearPedido
        );

UUID id =
        entradaJson.procesar(
                Path.of("crear-pedido.json")
        );

System.out.println(
        "Pedido creado desde JSON: " + id
);
~~~

Añada:

~~~java
import java.nio.file.Path;
~~~

El caso de uso no ha cambiado.

---

### Dos ficheros JSON, dos responsabilidades diferentes

Es importante no confundirlos:

| Fichero | Papel arquitectónico | Dirección |
|---|---|---|
| `crear-pedido.json` | Entrada que solicita crear un pedido | Exterior → aplicación |
| `pedidos.json` | Persistencia de los pedidos | Aplicación → exterior |

Los dos utilizan JSON, pero cumplen funciones completamente distintas.

La tecnología o el formato no determina si algo es un adaptador de entrada o de salida. Lo determina **la relación que mantiene con la aplicación**.

---

## Paso 9. Ejecutar la aplicación

Desde el directorio raíz del proyecto ejecute:

~~~bash
mvn compile
~~~

Si la compilación termina correctamente, ejecute Main desde su IDE.

Debería aparecer algo parecido a:

~~~text
Pedido creado: 4c93d193-...
Total: 415.30
~~~

El UUID será diferente en cada ejecución.

Compruebe el cálculo:

~~~text
2 × 189.90 = 379.80
1 × 35.50  =  35.50
--------------------
TOTAL        415.30
~~~

---

## Paso 10. Identificar la arquitectura que hemos construido

Ahora podemos leer el programa siguiendo el flujo:

~~~text
Main
 │
 ▼
CrearPedidoUseCase
 │
 ▼
CrearPedidoService
 │
 ├───────────────► Pedido
 │
 └───────────────► PedidoRepository
                         │
                         ▼
               PedidoRepositoryMemoria
~~~

En términos de Arquitectura Hexagonal:

~~~text
Consola                  crear-pedido.json
   │                             │
   ▼                             ▼
Main / ConsoleController   CrearPedidoDesdeJson
   │                             │
   └──────────────┬──────────────┘
                  ▼
          Puerto de entrada
          CrearPedidoUseCase
   │
   ▼
Aplicación
CrearPedidoService
   │
   ▼
Puerto de salida
PedidoRepository
   │
   ▼
Adaptador de salida
PedidoRepositoryMemoria
~~~

Y dentro del núcleo aparecen los conceptos de DDD:

~~~text
Pedido             entidad / Aggregate Root
LineaPedido        Value Object
PedidoRepository   Repository
~~~

---

## Paso 11. Segundo adaptador de salida: guardar en JSON

Hasta ahora los pedidos se guardan en memoria. Al cerrar el programa, desaparecen.

Vamos a crear un segundo adaptador que implemente exactamente el mismo puerto `PedidoRepository`, pero que persista los datos en:

~~~text
pedidos.json
~~~

Cree en:

~~~text
com.ejemplo.pedidos.adapters.out
~~~

el fichero:

~~~text
PedidoRepositoryJson.java
~~~

~~~java
package com.ejemplo.pedidos.adapters.out;

import com.ejemplo.pedidos.domain.LineaPedido;
import com.ejemplo.pedidos.domain.Pedido;
import com.ejemplo.pedidos.ports.PedidoRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class PedidoRepositoryJson
        implements PedidoRepository {

    private final Path archivo;
    private final ObjectMapper mapper =
            new ObjectMapper();

    private final Map<UUID, Pedido> datos =
            new HashMap<>();

    public PedidoRepositoryJson(Path archivo) {
        this.archivo = archivo;
        cargar();
    }

    @Override
    public void guardar(Pedido pedido) {
        datos.put(pedido.getId(), pedido);
        guardarArchivo();
    }

    @Override
    public Optional<Pedido> buscarPorId(
            UUID id
    ) {
        return Optional.ofNullable(
                datos.get(id)
        );
    }

    private void cargar() {

        try {

            if (Files.notExists(archivo) ||
                    Files.size(archivo) == 0) {
                return;
            }

            List<PedidoJson> pedidos =
                    mapper.readValue(
                        archivo.toFile(),
                        new TypeReference<
                            List<PedidoJson>
                        >() {}
                    );

            for (PedidoJson dto : pedidos) {
                Pedido pedido = aDominio(dto);
                datos.put(
                    pedido.getId(),
                    pedido
                );
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void guardarArchivo() {

        try {

            List<PedidoJson> pedidos =
                    datos.values()
                         .stream()
                         .map(this::aJson)
                         .toList();

            mapper.writerWithDefaultPrettyPrinter()
                  .writeValue(
                      archivo.toFile(),
                      pedidos
                  );

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private PedidoJson aJson(Pedido pedido) {

        List<LineaJson> lineas =
                pedido.getLineas()
                      .stream()
                      .map(linea ->
                          new LineaJson(
                              linea.producto(),
                              linea.precioUnitario(),
                              linea.cantidad()
                          )
                      )
                      .toList();

        return new PedidoJson(
                pedido.getId().toString(),
                lineas
        );
    }

    private Pedido aDominio(PedidoJson dto) {

        Pedido pedido =
                new Pedido(
                    UUID.fromString(dto.id())
                );

        for (LineaJson linea : dto.lineas()) {
            pedido.agregarLinea(
                new LineaPedido(
                    linea.producto(),
                    linea.precioUnitario(),
                    linea.cantidad()
                )
            );
        }

        return pedido;
    }

    public record PedidoJson(
            String id,
            List<LineaJson> lineas
    ) {}

    public record LineaJson(
            String producto,
            BigDecimal precioUnitario,
            int cantidad
    ) {}
}
~~~

### ¿Qué está haciendo este adaptador?

El puerto sigue siendo:

~~~java
PedidoRepository
~~~

y no cambia.

Lo nuevo está únicamente en el adaptador:

~~~text
PedidoRepositoryJson
~~~

Este adaptador realiza dos tareas:

1. convierte los objetos del dominio a una representación sencilla para JSON;
2. utiliza Jackson para leer y escribir el fichero.

Observe que **Pedido** y **LineaPedido** no contienen código de Jackson.

No añadimos anotaciones de persistencia al dominio.

La transformación entre el modelo de dominio y el formato externo pertenece al adaptador.

---

### Cambiar de memoria a JSON

En `Main`, sustituya:

~~~java
PedidoRepository repository =
        new PedidoRepositoryMemoria();
~~~

por:

~~~java
PedidoRepository repository =
        new PedidoRepositoryJson(
            Path.of("pedidos.json")
        );
~~~

y añada:

~~~java
import java.nio.file.Path;
~~~

No es necesario modificar:

~~~text
Pedido
LineaPedido
CrearPedidoUseCase
CrearPedidoService
PedidoRepository
~~~

Ejecute el programa.

Después de crear un pedido aparecerá un fichero parecido a:

~~~json
[
  {
    "id" : "4c93d193-0000-0000-0000-000000000000",
    "lineas" : [
      {
        "producto" : "Monitor",
        "precioUnitario" : 189.90,
        "cantidad" : 2
      },
      {
        "producto" : "Teclado",
        "precioUnitario" : 35.50,
        "cantidad" : 1
      }
    ]
  }
]
~~~

El identificador real será diferente.

Ahora cierre y vuelva a ejecutar el programa.

El adaptador vuelve a leer `pedidos.json` y reconstruye los objetos del dominio.

Esta es la prueba importante:

~~~text
PedidoRepositoryMemoria
          │
          │ implementa
          ▼
   PedidoRepository
          ▲
          │ implementa
          │
PedidoRepositoryJson
~~~

El caso de uso trabaja con el puerto y **no necesita saber cuál de los dos adaptadores estamos utilizando**.

---

## Paso 12. Qué debe haber comprendido

Antes de continuar, compruebe que puede explicar con sus propias palabras:

1. por qué Pedido pertenece al dominio;
2. por qué LineaPedido contiene sus propias validaciones;
3. por qué PedidoRepository es una interfaz;
4. por qué PedidoRepositoryMemoria está fuera del dominio;
5. qué diferencia existe entre un puerto y un adaptador;
6. por qué CrearPedidoService no conoce ninguna base de datos;
7. qué piezas permanecen iguales al sustituir PedidoRepositoryMemoria por PedidoRepositoryJson;
8. por qué crear-pedido.json es una entrada y pedidos.json es una salida aunque ambos sean JSON.

Si puede responder a estas preguntas, ya tiene la idea esencial de la combinación entre DDD y Arquitectura Hexagonal.

---

# Ejercicio propuesto

Amplíe el ejemplo incorporando el caso de uso **ConsultarPedido** y una pequeña interfaz de consola.

Debe crear:

1. un puerto de entrada `ConsultarPedidoUseCase`;
2. una implementación `ConsultarPedidoService`;
3. reutilizar `PedidoRepository` como puerto de salida;
4. utilizar `PedidoRepositoryMemoria`;
5. crear o ampliar un `ConsoleController` que permita elegir operaciones desde consola;
6. permitir al usuario introducir por teclado el identificador de un pedido;
7. mostrar en consola los datos del pedido encontrado o un mensaje si no existe.

Un menú posible sería:

~~~text
1. Crear pedido
2. Consultar pedido
0. Salir
~~~

No es necesario utilizar HTTP, APIs REST ni interfaces gráficas.

El objetivo es practicar el flujo:

~~~text
Consola
   ↓
Adaptador de entrada
   ↓
Puerto de entrada
   ↓
Caso de uso
   ↓
Puerto de salida
   ↓
Adaptador de persistencia
~~~

## Ampliación

Sustituya `PedidoRepositoryMemoria` por `PedidoRepositoryJson` y compruebe que el ejercicio sigue funcionando sin modificar el dominio ni los casos de uso.

Como ampliación de nivel medio, cree un tercer adaptador:

~~~text
PedidoRepositoryJdbc
~~~

El dominio, los puertos y los casos de uso **no deben modificarse**.

Al terminar, compruebe qué clases han cambiado al sustituir la persistencia.

Si la separación es correcta, los cambios deberían concentrarse principalmente en el adaptador y en la composición de la aplicación.

---

# Autoevaluación

1. **¿Cuál es el objetivo principal de DDD?**
   - a) Elegir una base de datos.
   - b) Modelar el dominio y sus reglas.
   - c) Crear controladores REST.
   - d) Dividir siempre la aplicación en microservicios.

2. **¿Qué es un Value Object?**
   - a) Un objeto definido principalmente por sus valores.
   - b) Una tabla de base de datos.
   - c) Una interfaz gráfica.
   - d) Un adaptador JDBC.

3. **¿Cómo se denomina también la Arquitectura Hexagonal?**
   - a) Transformers and Controllers.
   - b) Ports and Adapters.
   - c) Models and Views.
   - d) Tables and Services.

4. **¿Qué representa normalmente un puerto en Java?**
   - a) Una interfaz o contrato.
   - b) Una tabla SQL.
   - c) Un fichero JAR.
   - d) Una clase final obligatoriamente.

5. **¿Cuál sería un adaptador de entrada en nuestro ejemplo?**
   - a) PedidoRepositoryJson.
   - b) ConsoleController.
   - c) Pedido.
   - d) LineaPedido.

6. **¿Cuál sería un adaptador de salida?**
   - a) ConsoleController.
   - b) CrearPedidoUseCase.
   - c) PedidoRepositoryJson.
   - d) Pedido.

7. **¿DDD y Arquitectura Hexagonal son la misma cosa?**
   - a) Sí.
   - b) No; son diferentes pero complementarias.
   - c) DDD es una versión antigua de Hexagonal.
   - d) Hexagonal forma parte obligatoria de DDD.

8. **¿Qué ventaja aporta depender de PedidoRepository en lugar de una implementación concreta como JSON o JDBC?**
   - a) Permite sustituir la tecnología de persistencia con menor impacto.
   - b) Elimina la necesidad de almacenar datos.
   - c) Convierte automáticamente Java en SQL.
   - d) Hace innecesarias las pruebas.

## Soluciones

1. **b** · 2. **a** · 3. **b** · 4. **a** · 5. **b** · 6. **c** · 7. **b** · 8. **a**

---

# Referencias

- Eric Evans, *Domain-Driven Design: Tackling Complexity in the Heart of Software*.
- DDD Reference, Eric Evans: https://www.domainlanguage.com/ddd/reference/
- Alistair Cockburn, *Hexagonal Architecture / Ports and Adapters*: https://alistair.cockburn.us/hexagonal-architecture/
