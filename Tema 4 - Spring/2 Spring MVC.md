#### Introducción a Spring MVC

##### Arquitectura MVC y su Implementación en Spring
Spring MVC implementa el patrón Modelo-Vista-Controlador, facilitando el desarrollo de aplicaciones web. El controlador maneja solicitudes, el modelo representa datos, y la vista genera la salida al usuario.

![](Diagrama%20MVC.png)

##### Creación de un Controlador Básico
Un controlador en Spring MVC se define con la anotación `@Controller`. Puede manejar solicitudes HTTP mediante anotaciones como `@RequestMapping`.

##### Mapeo de Solicitudes y Respuestas
El mapeo de solicitudes se realiza mediante anotaciones como `@GetMapping` o `@PostMapping`, definiendo cómo se atienden las solicitudes HTTP.

Ejemplos y explicación sencilla Spring MVC :

https://www.arquitecturajava.com/spring-mvc-configuracion/
https://spring.io/guides/gs/serving-web-content



#### Vistas con Thymeleaf

##### Integración de Thymeleaf con Spring MVC
Thymeleaf es un motor de plantillas para aplicaciones web en Java. Se integra fácilmente con Spring MVC para generar **vistas HTML dinámicas**.

##### Creación de Vistas y Plantillas
Con Thymeleaf, se crean plantillas HTML que Spring MVC puede procesar y presentar como vistas. Estas plantillas pueden contener datos dinámicos y lógica de presentación.

##### Manejo de Datos en la Vista
Los datos del modelo se pasan a la vista donde Thymeleaf los utiliza para renderizar contenido dinámico. Se puede manipular y presentar la información del modelo en la vista.



**Ejemplo de Código: Introducción a Spring MVC**

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


Aquí un introducción más elaborada: [2.1 Introducción Thymleaf](2.1%20Introducción%20Thymleaf.md)





