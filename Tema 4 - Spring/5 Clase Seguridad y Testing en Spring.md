### Clase 5: Seguridad y Testing en Spring

#### Sesión 1: Spring Security

##### Fundamentos de Seguridad en Aplicaciones Web
La seguridad web abarca la autenticación (verificar la identidad del usuario) y la autorización (controlar el acceso a recursos).

##### Configuración y Uso de Spring Security
Spring Security proporciona un marco para la seguridad de aplicaciones, incluyendo autenticación y autorización.

##### Autenticación y Autorización
La autenticación se logra mediante proveedores de autenticación, mientras que la autorización se maneja mediante la asignación de roles y el control de acceso a URLs.

**Ejemplo de Configuración de Seguridad:**

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

