### Clase 2: Spring Core y Spring MVC

#### Sesión 1: Profundización en Spring Core

##### El Contenedor de Spring y el Ciclo de Vida de los Beans
El contenedor de Spring gestiona la creación, inicialización y destrucción de beans. El ciclo de vida de un bean incluye etapas como la instanciación, la inyección de dependencias, la inicialización y la destrucción.

##### Alcance de los Beans y Gestión de Dependencias
Los beans pueden tener diferentes alcances (singleton, prototype, request, session). La elección del alcance afecta cómo y cuándo se crea e inyecta un bean.

#### Sesión 2: Introducción a Spring MVC

##### Arquitectura MVC y su Implementación en Spring
Spring MVC implementa el patrón Modelo-Vista-Controlador, facilitando el desarrollo de aplicaciones web. El controlador maneja solicitudes, el modelo representa datos, y la vista genera la salida al usuario.

##### Creación de un Controlador Básico
Un controlador en Spring MVC se define con la anotación `@Controller`. Puede manejar solicitudes HTTP mediante anotaciones como `@RequestMapping`.

##### Mapeo de Solicitudes y Respuestas
El mapeo de solicitudes se realiza mediante anotaciones como `@GetMapping` o `@PostMapping`, definiendo cómo se atienden las solicitudes HTTP.

#### Sesión 3: Vistas y Thymeleaf

##### Integración de Thymeleaf con Spring MVC
Thymeleaf es un motor de plantillas para aplicaciones web en Java. Se integra fácilmente con Spring MVC para generar vistas HTML dinámicas.

##### Creación de Vistas y Plantillas
Con Thymeleaf, se crean plantillas HTML que Spring MVC puede procesar y presentar como vistas. Estas plantillas pueden contener datos dinámicos y lógica de presentación.

##### Manejo de Datos en la Vista
Los datos del modelo se pasan a la vista donde Thymeleaf los utiliza para renderizar contenido dinámico. Se puede manipular y presentar la información del modelo en la vista.

---

**Ejemplo de Código para Sesión 2: Introducción a Spring MVC**

Archivo `MiControlador.java`:
```java
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class MiControlador {

    @GetMapping("/saludo")
    public String saludo(Model model) {
        model.addAttribute("mensaje", "Hola Spring MVC");
        return "saludo";
    }
}
```

Archivo `saludo.html` (usando Thymeleaf):
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Saludo</title>
</head>
<body>
    <h1 th:text="${mensaje}"></h1>
</body>
</html>
```

**Ejercicios**

1. **Nivel Fácil:** Crear un controlador con un método que retorne una vista simple.
   - **Solución:** Utilizar el controlador `MiControlador` con el método `saludo` que retorna la vista `saludo`.

2. **Nivel Medio:** Enviar un objeto de modelo desde el controlador a la vista y mostrar sus propiedades con Thymeleaf.
   - **Solución:** En el método `saludo` del controlador, agregar un objeto al modelo (ej., `model.addAttribute("usuario", new Usuario("Juan"))`). En la vista `saludo.html`, mostrar las propiedades del objeto `usuario` usando Thymeleaf.



