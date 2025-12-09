# Explicación

### 0. Las 5W de `Optional<T>`

#### 1. **What?** – ¿Qué es `Optional<T>`?

`Optional<T>` es una **caja** que puede contener:

- un valor de tipo `T`, o
    
- estar vacía.
    

Es decir, es una forma **segura y explícita** de representar la _ausencia de valor_ sin usar `null`.

```java
Optional<String> nombre = Optional.of("Pepe");
Optional<String> sinNombre = Optional.empty();
```

---

#### 2. **Why?** – ¿Por qué existe?

Para atacar el clásico problema de Java: el `NullPointerException`.

En lugar de:

```java
String nombre = persona.getNombre(); // ¿puede ser null?
```

Con `Optional`:

```java
Optional<String> nombreOpt = persona.getNombreOptional();
```

El tipo ya te está diciendo:

> "Ojo, aquí posiblemente no haya valor, trátalo como tal".

Beneficios:

- Hace el código **más legible y auto-documentado**.
    
- Obliga a pensar **qué pasa si no hay valor**.
    
- Facilita un estilo de programación más **declarativo / funcional**.
    

---

#### 3. **When?** – ¿Cuándo usarlo? (y cuándo no)

**BUENOS momentos para usar `Optional`:**

- Como **valor de retorno** de métodos donde **tiene sentido que no haya resultado**:
    
    - `findById`, `buscarPorEmail`, `parsearEntero`, etc.
        
- En capas de **servicio / repositorio**: “puede que no haya datos”.
    
- Al **componer operaciones**: `map`, `filter`, `flatMap`…
    

**MALOS usos (en general):**

- En **atributos de entidades** JPA/POJO:
    
    - `class Usuario { Optional<String> email; }` ❌
        
- Como **parámetro** de método:
    
    - `void enviarEmail(Optional<String> contenido)` ❌  
        Mejor: `void enviarEmail(String contenido)` y que quien llame decida qué hacer si no tiene contenido.
        
- Como **colecciones de Optional** sin necesidad:
    
    - `List<Optional<Alumno>>` suele indicar mal diseño.
        

---

#### 4. **Where?** – ¿Dónde encaja mejor?

- En los **límites** entre capas:
    
    - `Repositorio → Servicio`, `Servicio → Controlador`.
        
- En código **funcional** con `Stream`:
    
    - Filtrados, transformaciones, conversiones de tipos.
        
- En **APIs** donde quieres dejar claro:
    
    - “Esto puede venir o no venir, y no te voy a dar `null`”.
        

---

#### 5. **Who?** – ¿Quién debe usarlo?

- Todo el equipo que quiera:
    
    - Reducir `null`.
        
    - Mejorar la legibilidad.
        
    - Acercarse a un estilo **más funcional** en Java.
        

Para quien:

> cualquiera que ya maneje lambdas, `Stream` y quiera subir un escalón en calidad de código.

---

### 1. Creación de `Optional`

#### `Optional.of(T value)` – valor **no nulo**

```java
Optional<String> nombre = Optional.of("Ana");   // OK
Optional<String> fallo = Optional.of(null);     // NullPointerException
```

#### `Optional.ofNullable(T value)` – valor **que puede ser nulo**

```java
String nombrePosibleNull = obtenerNombreDeBd();

Optional<String> nombreOpt = Optional.ofNullable(nombrePosibleNull);
// Si viene null → Optional.empty()
```

#### `Optional.empty()` – vacío explícito

```java
Optional<String> sinValor = Optional.empty();
```

---

### 2. Inspeccionar y consumir un `Optional`

#### `isPresent()` / `isEmpty()` – estilo imperativo

```java
Optional<String> nombreOpt = Optional.of("Ana");

if (nombreOpt.isPresent()) {
    System.out.println("Nombre: " + nombreOpt.get());
} else {
    System.out.println("No hay nombre");
}
```

> **Ojo**: `get()` solo debería usarse cuando `isPresent()` es claramente cierto. En código real se prefiere usar otros métodos.

---

#### `ifPresent()` – ejecutar algo solo si hay valor

```java
Optional<String> emailOpt = obtenerEmailOptional();

emailOpt.ifPresent(email -> 
    System.out.println("Enviando correo a " + email)
);
```

#### `ifPresentOrElse()` – versión con “si hay / si no hay”

```java
emailOpt.ifPresentOrElse(
    email -> System.out.println("Enviando correo a " + email),
    ()    -> System.out.println("No hay email definido")
);
```

---

### 3. Devolver valores por defecto

#### `orElse(defaultValue)`

Siempre evalúa el valor por defecto (aunque no haga falta).

```java
String nombre = nombreOpt.orElse("Desconocido");
```

#### `orElseGet(Supplier)`

Evalúa el valor por defecto **perezosamente** solo si hace falta.

```java
String nombre = nombreOpt.orElseGet(() -> cargarNombrePorDefectoLento());
```

#### `orElseThrow(Supplier<Throwable>)`

Lanza excepción si está vacío.

```java
Alumno alumno = repo.findById(id)
    .orElseThrow(() -> new IllegalArgumentException("No existe alumno con id " + id));
```

---

### 4. Uso funcional: `map`, `flatMap`, `filter`

Aquí viene la parte “funcional” interesante.

#### 4.1. `map` – transformar el contenido

Si hay valor, aplica la función. Si no, sigue vacío.

```java
Optional<Alumno> alumnoOpt = repo.findById(1L);

Optional<String> nombreMayusOpt = alumnoOpt
        .map(Alumno::getNombre)
        .map(String::toUpperCase);
```

- Si `findById` devuelve alumno → tendrás su nombre en mayúsculas.
    
- Si no existe → `Optional.empty()`.
    

---

#### 4.2. `filter` – quedarte solo con valores que cumplan una condición

```java
Optional<Alumno> mayorDeEdad = alumnoOpt
        .filter(al -> al.getEdad() >= 18);
```

- Si el alumno existe y es ≥18 → Optional con el alumno.
    
- Si no existe o es <18 → Optional vacío.
    

Ejemplo con cadena:

```java
Optional<String> emailOpt = alumnoOpt
        .map(Alumno::getEmail)
        .filter(email -> email.endsWith("@gmail.com"));

String email = emailOpt.orElse("email_no_disponible@gmail.com");
```

---

#### 4.3. `flatMap` – evitar `Optional<Optional<T>>`

Se usa cuando la función que aplicas **ya devuelve un Optional**.

```java
Optional<Alumno> alumnoOpt = repo.findById(1L);

// método: Optional<String> getEmailOptional()
Optional<String> emailOpt1 = alumnoOpt
        .map(Alumno::getEmailOptional);     // Optional<Optional<String>> ❌

Optional<String> emailOpt2 = alumnoOpt
        .flatMap(Alumno::getEmailOptional); // Optional<String> ✅
```

---

### 5. Ejemplos comparando estilo imperativo vs funcional

#### 5.1. Ejemplo 1: buscar alumno y devolver su email (o uno genérico)

**Imperativo (con null):**

```java
public String obtenerEmail(long id) {
    Alumno alumno = repo.findById(id); // puede ser null
    if (alumno == null || alumno.getEmail() == null) {
        return "no-email@mi-centro.es";
    }
    return alumno.getEmail();
}
```

**Funcional con `Optional`:**

```java
public String obtenerEmail(long id) {
    return repo.findById(id) // Optional<Alumno>
            .map(Alumno::getEmail) // Optional<String>
            .orElse("no-email@mi-centro.es");
}
```

---

#### 5.2. Ejemplo 2: encadenando varias operaciones

Supón:

- `findById(id)` → `Optional<Alumno>`
    
- `getTutor()` → puede devolver `null`
    
- `getEmail()` → puede devolver `null`
    

**Sin Optional:**

```java
String emailTutor = "no-disponible@centro.es";

Alumno alu = repo.findById(id);
if (alu != null) {
    Tutor tutor = alu.getTutor();
    if (tutor != null) {
        String email = tutor.getEmail();
        if (email != null) {
            emailTutor = email;
        }
    }
}
```

**Con Optional y estilo funcional:**

```java
String emailTutor = repo.findById(id)            // Optional<Alumno>
        .flatMap(al -> Optional.ofNullable(al.getTutor())) // Optional<Tutor>
        .map(Tutor::getEmail)                    // Optional<String>
        .orElse("no-disponible@centro.es");
```

---

### 6. Buenas prácticas y antipatrones

✅ **Haz esto:**

- Usa `Optional` como **tipo de retorno** cuando la ausencia de valor es normal.
    
- Encadena `map`, `filter`, `flatMap` para evitar cascadas de `if`.
    
- Usa `orElseGet` cuando el valor por defecto es **costoso de calcular**.
    
- Usa `orElseThrow` cuando **no tener valor es un error**.
    

❌ **Evita esto:**

- Llamar a `.get()` sin comprobar antes. (huele mal, casi siempre hay una alternativa mejor).
    
- Guardar `Optional` como **campo** de entidad o DTO.
    
- Usarlo como **argumento de métodos**: en su lugar, documenta si el parámetro puede ser null o usa sobrecargas de métodos.
    
- Convertir `null` ↔ `Optional` continuamente de manera arbitraria. Que tenga sentido en los límites de la API.
    

---

# Mini-Proyecto


#### 🧱 Estructura del proyecto

Dentro del ZIP encontrarás:

```text
optional-tutorial/
├─ pom.xml
├─ src/
   ├─ main/java/es/fempa/optional/
   │  ├─ app/
   │  │  └─ App.java                    // main con ejemplos de uso
   │  ├─ domain/
   │  │  ├─ Alumno.java                 // getters + Optional en acceso a email/tutor
   │  │  └─ Tutor.java
   │  ├─ repository/
   │  │  ├─ AlumnoRepository.java       // interfaz con Optional
   │  │  └─ InMemoryAlumnoRepository.java // implementación en memoria con Stream + Optional
   │  └─ service/
   │     └─ AlumnoService.java          // lógica de negocio usando Optional (map, flatMap, orElse…)
   └─ test/java/es/fempa/optional/service/
      └─ AlumnoServiceTest.java         // tests JUnit 5 mostrando comportamiento de Optional
```

---

#### ▶️ Cómo ejecutarlo

1. Descomprime el ZIP.
    
2. En la carpeta del proyecto:
    

```bash
mvn test        # ejecuta los tests JUnit
mvn exec:java   # si tienes el plugin exec configurado
```

O bien ejecuta el `main` de `App.java` desde tu IDE (IntelliJ/Eclipse/VS Code).

El `main` muestra por consola ejemplos tipo:

- `Optional` como valor de retorno en el repositorio.
    
- Uso de `flatMap`, `map`, `orElse` y `ifPresentOrElse`.
    
- Diferencias entre alumno con email / sin email, tutor con email / sin email.
    

---

Incluye:

- Código completo (dominio, repositorios, servicios, tests).
    
- Ejemplos funcionales con `Optional`.
    
- **README pedagógico**:  explicación, objetivos, estructura, ejercicios guiados y reglas de estilo.   

---
