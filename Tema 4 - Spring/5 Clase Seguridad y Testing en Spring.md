### Clase 5: Seguridad y Testing en Spring

#### Sesión 1: Spring Security

##### Fundamentos de Seguridad en Aplicaciones
La seguridad web abarca la **autenticación** (verificar la identidad del usuario) y la **autorización** (controlar el acceso a recursos).

Spring Security proporciona un marco para la seguridad de aplicaciones, incluyendo autenticación y autorización.

La autenticación se logra mediante proveedores de autenticación, mientras que la autorización se maneja mediante la asignación de roles y el control de acceso a URLs.

El **Control de Acceso Basado en Roles (autenticación)** y la **Gestión de Permisos (autorización)**  son aspectos esenciales de la autorización en aplicaciones web, y Spring Boot proporciona herramientas poderosas y flexibles para implementar estas funcionalidades. Aquí se muestra cómo aplicar estos conceptos específicamente en el contexto de Spring Boot y el Spring Security Framework.

### Control de Acceso Basado en Roles (RBAC)
En RBAC, los **accesos** se otorgan en función de los **roles** asignados a los usuarios. En Spring Boot, esto se gestiona típicamente con Spring Security:

1. **Definición de Roles**: Los roles representan un conjunto de permisos. Por ejemplo, podrías tener roles como `ADMIN`, `USER`, `MANAGER`, etc.

2. **Asignación de Roles a Usuarios**: Cuando un usuario se autentica, se le asignan roles. Estos roles se pueden almacenar y gestionar en la base de datos.

3. **Configuración de Spring Security**:
   - En el `WebSecurityConfigurerAdapter`, defines las reglas de seguridad, especificando qué roles tienen acceso a qué rutas o métodos en tu aplicación.
   - Ejemplo de configuración de seguridad:
```java
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/home").permitAll()
                .anyRequest().authenticated()
            .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
            .and()
            .logout()
                .permitAll();
    }
}
```

   - Otro ejemplo con control por ROLES:
     ```java
     @Override
     protected void configure(HttpSecurity http) throws Exception {
         http
             .authorizeRequests()
             .antMatchers("/admin/**").hasRole("ADMIN")
             .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
             .antMatchers("/").permitAll()
             .and()
             .formLogin();
     }
     ```
Este código configura las rutas para que solo sean accesibles por usuarios con los roles específicos.

### Gestión de Permisos
Además de los roles, puedes gestionar permisos específicos para acciones más granulares:

1. **Permisos**: Son autorizaciones más específicas que los roles. Por ejemplo, un rol `USER` podría tener permisos como `READ_PROFILE`, `EDIT_PROFILE`.

2. **Spring Method Security**: Puedes usar anotaciones de seguridad en los métodos para controlar el acceso basado en roles o permisos.
   - Ejemplo:
     ```java
     @PreAuthorize("hasRole('ADMIN')")
     public void someAdminMethod() {
         // ...
     }

     @PreAuthorize("hasAuthority('READ_PROFILE')")
     public void someUserMethod() {
         // ...
     }
     ```
   - `@PreAuthorize` permite definir la seguridad a nivel de método antes de que se ejecute el método.

### Implementación Práctica en Spring Boot
1. **Configurar Spring Security**: Añade Spring Security a tu proyecto de Spring Boot. Puedes hacerlo agregando la dependencia `spring-boot-starter-security` en tu archivo `pom.xml` o `build.gradle`.

2. **Clase de Configuración de Seguridad**: Crea una clase que extienda `WebSecurityConfigurerAdapter` para personalizar tu configuración de seguridad.

3. **Servicio de Detalles de Usuario**: Implementa `UserDetailsService` para cargar datos específicos del usuario (como roles) desde la base de datos.

4. **Personalizar la Autenticación y Autorización**: Utiliza `AuthenticationManagerBuilder` para definir cómo se autenticarán los usuarios (por ejemplo, con base de datos, LDAP, etc.) y configura las reglas de autorización en el método `configure(HttpSecurity http)`.

5. **Pruebas y Validación**: Asegúrate de probar exhaustivamente la seguridad de tu aplicación, incluyendo la autenticación y la autorización, para verificar que los permisos y roles se manejan como se espera.

Implementar RBAC y la gestión de permisos de manera efectiva en Spring Boot con Spring Security te permitirá tener un control detallado y seguro sobre quién puede acceder y realizar operaciones específicas en la aplicación.


### Ejemplo aplicado Reserva Hotel

Vamos a detallar cada uno de los puntos mencionados con ejemplos concretos, aplicados al contexto del sistema de reservas de un hotel en Spring Boot:

### 1. Definición y Asignación de Roles

#### Base de Datos
Imagina que tienes una tabla `usuarios` y una tabla `roles`. Los roles pueden ser `ADMIN`, `USER`, `MANAGER`, etc. Además, tendrías una tabla de unión `usuarios_roles` para representar la relación muchos-a-muchos entre usuarios y roles.

```sql
CREATE TABLE usuarios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    -- otros campos
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

#### Entidades en Spring Boot
Tendrías entidades `Usuario` y `Rol` en tu proyecto de Spring Boot.

```java
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    // Getters y setters
}

@Entity
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    // Getters y setters
}
```

### 2. Clase de Configuración de Seguridad

En Spring Boot, extiendes la clase `WebSecurityConfigurerAdapter` para configurar la seguridad:

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/admin/**").hasRole("ADMIN")
            .antMatchers("/reservas/**").hasAnyRole("USER", "MANAGER")
            .antMatchers("/").permitAll()
            .and()
            .formLogin();
            // Configuración adicional
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

### 3. Servicio de Detalles de Usuario

Implementa `UserDetailsService` para cargar los usuarios y sus roles desde la base de datos:

```java
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Rol rol : usuario.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + rol.getNombre()));
        }

        return new org.springframework.security.core.userdetails.User(usuario.getUsername(), usuario.getPassword(), grantedAuthorities);
    }
}
```

### 4. Pruebas y Validación

Las pruebas de seguridad implican:

- Verificar que los endpoints están protegidos según los roles.
- Comprobar que la autenticación funciona correctamente.
- Asegurar que los roles se asignan y aplican correctamente.

Puedes usar Spring Boot Test para escribir pruebas que validen tu configuración de seguridad:

```java
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTests {

    @Autowired
    private MockMvc mockMvc;

    // Escribe pruebas para verificar la seguridad
    // Por ejemplo, intentar acceder a rutas protegidas sin autenticación o con roles incorrectos
}
```

Estos ejemplos proporcionan un punto de partida para la implementación de RBAC y la seguridad en general en una aplicación con Spring Boot. 


#### Sesión 2: Testing en Spring

##### Principios de Testing en Spring
El testing en Spring incluye pruebas unitarias y de integración, con el objetivo de verificar el correcto funcionamiento de componentes individuales y del sistema en conjunto.

##### Uso de Spring Boot Test
Spring Boot Test proporciona herramientas para pruebas, incluyendo anotaciones como `@SpringBootTest` para pruebas de integración.

##### Creación de Tests para Repositorios y Controladores
Los tests para repositorios pueden incluir operaciones CRUD, mientras que los tests para controladores pueden verificar respuestas HTTP y comportamientos de endpoints.

**Ejemplo de Test para un Controlador:**

```java
@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSaludo() throws Exception {
        this.mockMvc.perform(get("/saludo"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Hola")));
    }
}
```

#### Sesión 3: Integración y Pruebas de Fin a Fin

##### Pruebas de Integración en Spring
Las pruebas de integración validan que los distintos módulos de la aplicación funcionen correctamente en conjunto.

##### Testing de Microservicios
En un entorno de microservicios, las pruebas de integración implican verificar la comunicación y la colaboración entre los servicios.

##### Herramientas y Estrategias para Pruebas de Fin a Fin
Incluyen simulación de escenarios de usuario, verificación de flujos de trabajo completos y uso de herramientas como Selenium para pruebas de interfaces de usuario.

---

**Ejercicios**

1. **Nivel Fácil (Sesión 1):** Configurar Spring Security para una aplicación simple, permitiendo acceso sin restricciones a una ruta específica.
   - **Solución:** Implementar la clase `SecurityConfig` con la configuración adecuada para permitir el acceso a ciertas URLs.

2. **Nivel Medio (Sesión 2):** Escribir un test para un repositorio, comprobando la correcta ejecución de una operación CRUD.
   - **Solución:** Utilizar `@DataJpaTest` para probar un repositorio, creando un test que guarde un objeto y luego lo recupere y verifique su existencia.

