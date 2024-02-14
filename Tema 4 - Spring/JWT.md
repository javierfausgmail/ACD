#### El concepto de JWT

Las "5W" aplicadas a JSON Web Tokens:

1. **What (Qué)**: JWT (JSON Web Token) es un formato de token de seguridad en línea que se utiliza para transmitir información de manera segura entre dos partes. Es una cadena de caracteres que consta de tres partes: el encabezado (header), la carga útil (payload, donde se almacenan los datos o *claims* como se les suele llamar en JWT) y la firma (signature).

2. **Why (Por qué)**: JWT se utiliza para autenticar y autorizar usuarios en aplicaciones web y servicios en línea. Ofrece una forma segura y eficiente de transmitir información de identidad y autorización entre el cliente y el servidor.

3. **Who (Quién)**: Los JWT son utilizados por aplicaciones web y servicios para identificar y autenticar a los usuarios. Estos usuarios pueden ser clientes, usuarios finales o cualquier entidad que necesite autenticación en una aplicación.

4. **When (Cuándo)**: JWT se utiliza en varios casos de uso, como la autenticación de usuarios en aplicaciones web, la autorización para acceder a recursos protegidos y la comunicación segura entre microservicios. Los JWT suelen tener una fecha de vencimiento (expiración) que determina su duración de validez.

5. **Where (Dónde)**: Los JWT se pueden utilizar en cualquier lugar donde se requiera autenticación y autorización seguras en aplicaciones web y servicios en línea. Son especialmente populares en aplicaciones basadas en RESTful APIs y en entornos distribuidos.

En resumen, JWT (JSON Web Token) es un formato de token utilizado para la autenticación y autorización en aplicaciones web y servicios en línea. Se utiliza para transmitir información segura entre partes y se aplica en una variedad de casos de uso en diferentes entornos.

#### Flujo normal trabajando con JWT

Como primer paso, un cliente debe autenticarse utilizando un nombre de usuario y contraseña, recibiendo a cambio un **token firmado** (JWT). Este token se almacena localmente en el cliente y se pasa al servidor con cada solicitud adicional, normalmente en el encabezado. Dado que el token se firma utilizando una clave que solo el servidor conoce, **el token y, por tanto, el cliente se pueden validar de forma segura** .

![](Pasted%20image%2020240205172840.png)

Este enfoque hace que todo el proceso sea sin estado y muy adecuado para API REST, ya que no es necesario almacenar datos sobre el estado del cliente (por ejemplo, una sesión). El nombre de usuario y la fecha de vencimiento del token se almacenan en la carga útil (pay load o cuerpo) del paquete.

#### El token JSON Web (JWT).

Un token JSON Web (JWT) es una representación compacta y autónoma de la información que se utiliza para transmitir información de manera segura entre dos partes. Está compuesto por tres partes separadas por puntos: el encabezado (header), la carga útil (payload) y la firma (signature).

1. **Encabezado (Header)**: El encabezado suele consistir en dos partes: el tipo de token, que es JWT, y el algoritmo de firma utilizado, como HMAC SHA256 o RSA.

2. **Carga útil (Payload)**: La carga útil contiene los claims, que son declaraciones sobre una entidad (por ejemplo, un usuario) y metadatos adicionales. Los claims se dividen en tres tipos: claims registrados, claims públicos y claims privados. Los claims registrados son predefinidos y tienen un significado específico, como "iss" para el emisor o "exp" para la fecha de expiración.

3. **Firma (Signature)**: La firma se crea utilizando el encabezado codificado, la carga útil codificada, una clave secreta y el algoritmo de firma especificado en el encabezado. La firma se utiliza para verificar que el remitente del JWT es quien dice ser y para garantizar que los datos no se han manipulado en el camino.

Los JWT son ampliamente utilizados para autenticación y autorización en aplicaciones web y servicios API. Son útiles cuando se necesita transmitir información entre dos partes de manera segura y confiable. **Pueden ser decodificados fácilmente, pero no pueden ser modificados sin invalidar la firma, lo que los hace seguros para transmitir datos confidenciales.**

Es importante tener en cuenta que los JWT no cifran la información, **solo la firman**. Por lo tanto, **no deben utilizarse para almacenar información sensible que deba mantenerse confidencial. Para el cifrado de datos, se deben utilizar otros mecanismos, como SSL/TLS.**

Puedes entrar en https://jwt.io/ para una presentación gráfica e interactiva de un paquete JWT. 

Explicación teórica en vídeo detallada: https://www.youtube.com/watch?v=Lm7WYw3SRz8

#### Implementación con Spring

0. Vídeo tutorial completo utilizando autenticación con JWT (capítulos 1 y 2) con código adjunto: https://www.youtube.com/playlist?list=PLr23_YfwEbPRCK4IbemQGwYdgSwfd2aZu   / https://github.com/UnProgramadorNace/Spring-Security-JWT


1. Curso completo (sin código adjunto): https://www.youtube.com/watch?v=735a83FQR2I

![](Diagrama%20Spring%20Security%20JWT%20Flow.png)


2. Curso (con código adjunto) y video tutorial avanzado: https://www.youtube.com/watch?v=Qu3soRF168I
3. Documento guía para generar un template de aplicación con Bootify: https://bootify.io/spring-security/rest-api-spring-security-with-jwt.html