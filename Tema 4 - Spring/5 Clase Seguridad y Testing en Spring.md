### Clase 5: Seguridad en Spring
# Seguridad en Spring Web — Explicado con Analogías

Como introducción vamos a traducir lo técnico a un lenguaje simple, con ejemplos de la vida real. Imaginad que estamos hablando de **la seguridad de un edificio** con puertas, llaves, tarjetas identificativas y guardias.

---

##  ¿Qué es la Seguridad?

En software, "seguridad" es como la **seguridad de un edificio**:

- Controlar quién entra.
    
- Controlar qué puede hacer cada persona dentro.
    
- Evitar que gente no autorizada se cuele.
    

En esta lección veremos cómo funciona la seguridad en aplicaciones web usando **Spring Security**.

---

##  Autenticación = “¿Quién eres?”

**Autenticación** es como cuando llegas a un edificio y el guardia te pide:

> “Enséñame tu identificación.”

En software:

- El “usuario” puede ser una persona o incluso otro programa → lo llamamos **Principal**.
    
- Para demostrar quién eres, aportas **credenciales** (usuario y contraseña, por ejemplo).
    

Si las credenciales son correctas → **el sistema te reconoce y te deja entrar**.

###  Problema: HTTP no recuerda nada

HTTP es como un guardia **muy despistado**:

👉 Cada vez que entras al edificio, te vuelve a pedir la identificación.  
No recuerda tu cara.

Eso es muy incómodo.

###  Solución: Cookies con Tokens (una tarjeta identificativa)

Para evitar que el guardia pida DNI cada vez:

- El servidor crea un **Token** (como una tarjeta magnética del edificio).
    
- Ese Token se guarda en una **Cookie** del navegador.
    
- Cada vez que haces una petición, la Cookie va sola, como si pasaras la tarjeta por el lector.
    

Así no necesitas estar poniendo usuario y contraseña todo el rato.

---

##  Spring Security y la Autenticación

Spring Security coloca un **filtro** (un guardia de seguridad) **antes de llegar al controlador**.

- Mira la petición.
    
- Comprueba si la Cookie/Token es válida.
    
- Si NO lo es → devuelve **401 UNAUTHORIZED**  
    (equivalente a: “Lo siento, no puede pasar”).
    

---

##  Autorización = “¿Qué puedes hacer?”

Una vez el sistema sabe **quién eres**, toca decidir **qué te permite hacer**.

Esto es la **autorización**, como dentro de un edificio:

- Un vigilante puede acceder al cuarto de control.
    
- Un visitante puede entrar solo al hall.
    
- Un técnico puede entrar al cuarto de máquinas.
    

Spring Security usa **Roles** para esto:

- `ADMIN`
    
- `USER`
    
- `OWNER`
    
- etc.
    

Cada endpoint o función puede decir:

> “Solo entran ADMIN”,  
> “Solo entran propietarios”,  
> “Todos pueden ver, pero solo algunos pueden editar”, etc.

---

##  Same Origin Policy (SOP)

El SOP es como una **valla** alrededor del edificio que evita que desconocidos puedan interactuar con tu sistema.

Significa:

Solo las páginas que vienen del MISMO sitio web pueden enviar peticiones libres al servidor.

Es la forma básica de evitar ataques.

### Ejemplo con banco:

Imagina que estás conectado en tu banco en una pestaña.  
Si no existiera SOP:

- Abres otra web maliciosa.
    
- Esa web podría enviar peticiones al banco usando tus Cookies.
    
- El banco pensaría que has pedido una transferencia.
    

Por eso existe el SOP.

---

##  CORS: Permitido cruzar la valla, pero solo a quien tú digas

A veces TIENES que dejar que otras webs (otros orígenes) te llamen.  
Ejemplo: tu frontend en React (localhost:3000) llama al backend (localhost:8080).

Para eso existe **CORS**, una excepción segura a SOP.

En Spring se hace con:

```java
@CrossOrigin
```

⚠️ Si usas `@CrossOrigin` sin parámetros → permites a TODOS.  
Como dejar la puerta del edificio abierta: peligroso.

---

##  Ataques Comunes en la Web

###  CSRF (Cross-Site Request Forgery)

Analogia: **“un ladrón empuja tu tarjeta del edificio sin que tú lo sepas”**.

El ataque funciona así:

1. Tú estás autenticado (tienes la Cookie).
    
2. Visitas una web maliciosa.
    
3. Esa web envía peticiones al servidor usando TU Cookie.
    
4. El servidor no tiene forma de saber si tú querías hacer eso o no.
    

Solución: **CSRF Token**, como un ticket único por cada acción.

- Cada formulario/petición lleva un Token único.
    
- Aunque tengan tu Cookie, sin el Token no pueden hacer nada.
    

Spring Security lo activa **por defecto**.

---

###  XSS (Cross-Site Scripting)

Analogía: **meter un dispositivo explosivo dentro del edificio** para que explote cuando alguien lo vea.

Un atacante consigue que tu aplicación ejecute código que NO debería.

Ejemplo típico:

1. Un usuario malicioso guarda un texto con `<script>` en la base de datos.
    
2. Más tarde, otro usuario lo visualiza.
    
3. El navegador ejecuta ese script como si fuera tuyo.
    

Es más grave que CSRF: se ejecuta código arbitrario, no solo acciones autorizadas.

#### Solución:

- Filtrar y escapar correctamente todo el contenido mostrado.
    
- Nunca renderizar HTML “crudo” que venga de usuarios.
    

---

##  Resumen final

- **Autenticación** = “demostrar quién eres”.
    
- **Autorización** = “qué puedes hacer”.
    
- **Cookies con Tokens** = tu tarjeta de acceso.
    
- **Spring Security** = guardias en la puerta (filtros).
    
- **SOP** = la valla que separa tu edificio del resto del mundo.
    
- **CORS** = permisos especiales para invitados externos.
    
- **CSRF** = un atacante usando tu propia tarjeta sin que lo sepas.
    
- **XSS** = un atacante coloca código malicioso dentro de tu aplicación.
    

---

# **Seguridad en Spring** Web— Explicado sin Analogías

### **¿Qué es la Seguridad?**

La seguridad del software puede significar muchas cosas. El campo es un tema enorme que merece su propio curso. En esta lección, hablaremos sobre Seguridad Web. Más específicamente, cubriremos cómo funcionan la Autenticación y la Autorización HTTP, las formas comunes en las que el ecosistema web es vulnerable a ataques y cómo podemos usar Spring Security para evitar el acceso no autorizado a nuestro servicio Family Cash Card.

### **Autenticación**

Un usuario de una API puede ser realmente una persona u otro programa, por lo que a menudo utilizaremos el término _Principal_ como sinónimo de “usuario”. La autenticación es el acto de que un Principal demuestre su identidad al sistema. Una forma de hacer esto es proporcionar credenciales (por ejemplo, un nombre de usuario y una contraseña usando Autenticación Básica). Decimos que, una vez que se han presentado las credenciales correctas, el Principal está autenticado, o en otras palabras, el usuario ha iniciado sesión correctamente.

HTTP es un protocolo sin estado (_stateless_), por lo que cada petición debe contener datos que demuestren que proviene de un Principal autenticado. Aunque es posible presentar las credenciales en cada petición, hacerlo es ineficiente porque requiere más procesamiento en el servidor. En su lugar, se crea una Sesión de Autenticación (o _Auth Session_, o simplemente _Session_) cuando un usuario es autenticado. Las sesiones pueden implementarse de muchas maneras. Usaremos un mecanismo común: un Token de Sesión (una cadena de caracteres aleatorios) que se genera y se coloca en una Cookie. Una Cookie es un conjunto de datos almacenado en un cliente web (como un navegador) y asociado a un URI específico.

Un par de cosas interesantes sobre las Cookies:

- Las cookies se envían automáticamente al servidor con cada petición (no es necesario escribir código adicional para que esto ocurra). Mientras el servidor compruebe que el Token en la Cookie es válido, las peticiones no autenticadas pueden ser rechazadas.
    
- Las cookies pueden persistir durante un cierto tiempo incluso si la página web se cierra y se vuelve a visitar más tarde. Esta capacidad suele mejorar la experiencia de usuario del sitio web.
    

### **Spring Security y la Autenticación**

Spring Security implementa la autenticación en la _Filter Chain_. La _Filter Chain_ es un componente de la arquitectura web Java que permite a los programadores definir una secuencia de métodos que se llaman antes del _Controller_. Cada filtro en la cadena decide si permite que continúe el procesamiento de la petición o no. Spring Security inserta un filtro que comprueba la autenticación del usuario y devuelve una respuesta **401 UNAUTHORIZED** si la petición no está autenticada.

### **Autorización**

Hasta ahora hemos hablado de autenticación. Pero en realidad, la autenticación es solo el primer paso. La autorización ocurre después de la autenticación y permite que distintos usuarios del mismo sistema tengan permisos diferentes.

Spring Security proporciona Autorización mediante Control de Acceso Basado en Roles (_RBAC_). Esto significa que un Principal tiene varios Roles. Cada recurso (u operación) especifica qué Roles debe tener un Principal para realizar acciones con la autorización adecuada. Por ejemplo, es probable que un usuario con un Rol de Administrador esté autorizado a realizar más acciones que un usuario con un Rol de Propietario de Tarjeta. Puedes configurar autorización basada en roles tanto a nivel global como a nivel de cada método.

### **Same Origin Policy**

La web es un lugar peligroso, donde actores maliciosos están constantemente intentando explotar vulnerabilidades de seguridad. El mecanismo de protección más básico se basa en que los clientes y servidores HTTP implementen la _Same Origin Policy_ (SOP). Esta política establece que solo los scripts contenidos en una página web pueden enviar peticiones al origen (URI) de esa página.

La SOP es fundamental para la seguridad de los sitios web porque, sin la política, cualquiera podría escribir una página web que contuviera un script capaz de enviar peticiones a cualquier otro sitio. Por ejemplo, veamos un sitio típico de banca. Si un usuario ha iniciado sesión en su cuenta bancaria y visita una página web maliciosa (en otra pestaña o ventana del navegador), las peticiones maliciosas podrían enviarse desde la web malicionsa (con las Cookies de Autenticación capturadas por este) al sitio bancario. Esto podría dar lugar a acciones no deseadas —como una retirada de dinero de la cuenta del usuario—.

### **Cross-Origin Resource Sharing**

A veces un sistema consiste en servicios que se ejecutan en varias máquinas con distintos URIs (es decir, microservicios). El _Cross-Origin Resource Sharing_ (CORS) es una forma de que navegadores y servidores cooperen para relajar la SOP. Un servidor puede permitir explícitamente una lista de “orígenes permitidos” para peticiones procedentes de un origen externo al servidor.

Spring Security proporciona la anotación `@CrossOrigin`, que permite especificar una lista de sitios permitidos. ¡Ten cuidado! Si usas la anotación sin argumentos, permitirá todos los orígenes, así que tenlo en cuenta.

### **Explotaciones Web Comunes**

Además de aprovechar vulnerabilidades de seguridad conocidas, los actores maliciosos en la web también descubren constantemente nuevas vulnerabilidades. Afortunadamente, Spring Security proporciona un conjunto de herramientas potentes para proteger contra explotaciones de seguridad comunes. Analicemos dos explotaciones comunes, cómo funcionan y cómo Spring Security ayuda a mitigarlas.

### **Cross-Site Request Forgery**

Un tipo de vulnerabilidad es el _Cross-Site Request Forgery_ (CSRF), que a menudo se pronuncia “Sea-Surf” y también se conoce como _Session Riding_. El Session Riding está habilitado por el uso de Cookies. Los ataques CSRF ocurren cuando un fragmento de código malicioso envía una petición a un servidor donde un usuario está autenticado. Cuando el servidor recibe la Cookie de Autenticación, no tiene manera de saber si la víctima envió involuntariamente la petición dañina.

Para protegerse contra ataques CSRF, puede usarse un Token CSRF. Un Token CSRF es diferente de un Token de Autenticación porque se genera un token único en cada petición. Esto hace más difícil que un actor externo se inserte en la “conversación” entre cliente y servidor.

Afortunadamente, Spring Security tiene soporte integrado para tokens CSRF, que está habilitado por defecto.

### **Cross-Site Scripting**

Quizá incluso más peligrosa que la vulnerabilidad CSRF es el _Cross-Site Scripting_ (XSS). Esto ocurre cuando un atacante consigue “engañar” a la aplicación víctima para que ejecute código arbitrario. Hay muchas formas de hacer esto. Un ejemplo sencillo es guardar en una base de datos una cadena que contenga una etiqueta `<script>`, y luego esperar a que la cadena sea mostrada en una página web, haciendo que el script se ejecute.

El XSS es potencialmente más peligroso que el CSRF. En el CSRF, solo pueden ejecutarse acciones para las que el usuario esté autorizado. Sin embargo, en el XSS se ejecuta código malicioso arbitrario en el cliente o en el servidor. Además, los ataques XSS no dependen de la Autenticación. Más bien, dependen de “agujeros” de seguridad causados por malas prácticas de programación.

La manera principal de protegerse contra ataques XSS es procesar correctamente todos los datos provenientes de fuentes externas (como formularios web y cadenas de consulta en URIs). En el caso de nuestro ejemplo con la etiqueta `<script>`, los ataques pueden mitigarse escapando correctamente los caracteres especiales de HTML cuando se muestra la cadena.

---

La Seguridad Web es un tema muy amplio y diverso, pero ahora tienes una idea general de cómo puedes usar Spring Security para proteger a tus usuarios y aplicaciones.




# **Fundamentos Modernos de Seguridad en Spring Boot (Spring Security 6.x)**

La seguridad en aplicaciones web implica dos procesos principales:

- **Autenticación** → verificar _quién es_ el usuario.
    
- **Autorización** → decidir _qué puede hacer_ ese usuario.
    

Spring Security proporciona mecanismos robustos para implementar ambos, tanto con roles como con permisos específicos.

En las versiones actuales (Spring Boot 3 / Spring Security 6):

### Ya NO se usa:

- `WebSecurityConfigurerAdapter` → **ELIMINADO**
    
- `antMatchers()` → reemplazado por `requestMatchers()`
    

### Ahora se usa:

- Beans de configuración
    
- `SecurityFilterChain`
    
- `UserDetailsService`
    
- `PasswordEncoder`
    
- Anotaciones: `@EnableMethodSecurity`, `@PreAuthorize`, etc.
    

---

# **Control de Acceso Basado en Roles (RBAC)**

En RBAC, los usuarios reciben **roles**, que representan grupos de permisos (p. ej. `ADMIN`, `USER`, `MANAGER`).

## 1. Roles en la base de datos

```sql
CREATE TABLE usuarios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL
);

CREATE TABLE usuarios_roles (
    usuario_id BIGINT,
    rol_id BIGINT,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (rol_id) REFERENCES roles(id)
);
```

## 2. Entidades modernas en Spring Boot

```java
@Entity
public class Usuario {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuarios_roles",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Rol> roles;
}
```

```java
@Entity
public class Rol {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
}
```

---

# **Configuración de Seguridad Moderna (Spring Boot 3.x / Security 6)**

### Ya no se extiende ninguna clase.

Ahora se declaran **beans explícitos**.

```java
@Configuration
@EnableMethodSecurity  // habilita @PreAuthorize, @PostAuthorize, etc.
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
            .csrf(csrf -> csrf.disable())                     // desactiva CSRF (activar si usas formularios)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/publico/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/reservas/**").hasAnyRole("USER", "MANAGER")
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/login")
                .permitAll()
            )
            .logout(logout -> logout.permitAll())
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

✔ **Roles deben ir prefijados internamente con `ROLE_`**, pero Spring lo hace automáticamente.

---

# **UserDetailsService Moderno**

```java
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository repo;

    public UserDetailsServiceImpl(UsuarioRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Usuario usuario = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No existe: " + username));

        Set<GrantedAuthority> authorities =
                usuario.getRoles().stream()
                    .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre()))
                    .collect(Collectors.toSet());

        return new User(usuario.getUsername(), usuario.getPassword(), authorities);
    }
}
```

---

# **Seguridad por Permisos (Authorities)**

Además de roles, puedes definir **permisos granulares**.

Ejemplo:

- Rol `USER` → `READ_PROFILE`
    
- Rol `ADMIN` → `DELETE_BOOKING`, `MANAGE_USERS`
    

### Uso en métodos:

```java
@PreAuthorize("hasRole('ADMIN')")
public void adminMethod() {}

@PreAuthorize("hasAuthority('DELETE_BOOKING')")
public void deleteReserva() {}
```

✔ Requiere `@EnableMethodSecurity`.

---

# **Ejemplo Real: Sistema de Reservas de Hotel 
### Control de acceso:

- `/admin/**` → solo ADMIN
    
- `/reservas/**` → USER + MANAGER
    
- `/` → público
    

Configuración:

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    return http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .requestMatchers("/reservas/**").hasAnyRole("USER", "MANAGER")
            .requestMatchers("/", "/publico/**").permitAll()
            .anyRequest().authenticated()
        )
        .formLogin(Customizer.withDefaults())
        .build();
}
```

### Ejemplo de método restringido:

```java
@PreAuthorize("hasRole('MANAGER')")
public Reserva aprobarReserva(Long idReserva) {
    // ...
}
```

---

# **Ejemplo JWT Moderno (muy resumido)**

Para Spring Security 6.x el flujo típico es:

- Filtro que extrae el token del header
    
- Valida el JWT
    
- Crea un `UsernamePasswordAuthenticationToken`
    
- Lo guarda en el `SecurityContext`
    

Ejemplo de filtro moderno:

```java
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
        throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String username = jwtService.extractUsername(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.isValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
```

---

# **Pruebas de Seguridad en Spring Boot (moderno)**

```java
@SpringBootTest
@AutoConfigureMockMvc
class SecurityTest {

    @Autowired
    MockMvc mvc;

    @Test
    void accesoNoAutenticado() throws Exception {
        mvc.perform(get("/admin"))
            .andExpect(status().isForbidden());
    }
}
```

---

# **Referencias oficiales y tutoriales actuales**

- Documentación oficial:  
    [https://docs.spring.io/spring-security/reference/index.html](https://docs.spring.io/spring-security/reference/index.html)
    
- Muestras oficiales:  
    [https://docs.spring.io/spring-security/reference/samples.html](https://docs.spring.io/spring-security/reference/samples.html)
    




