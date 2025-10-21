# 🏪 Comprar en Sólis API REST: E-commerce, Inventario y JWT

Una API REST robusta construida con Spring Boot 3 para la gestión de usuarios, roles, autenticación mediante JSON Web Tokens (JWT), inventario de productos, carrito de compras y checkout. La aplicación utiliza Spring Security para proteger endpoints, Spring Data JPA para persistencia de datos, y H2 como base de datos en memoria para desarrollo. Las configuraciones sensibles se manejan mediante variables de entorno, con valores por defecto para pruebas locales.

## 🚀 Tecnologías
- Spring Boot 3
- Spring Security
- Spring Data JPA
- H2 Database
- Java JWT
- Lombok
- Java 17

## 💻 Requisitos Previos
Antes de ejecutar la aplicación, asegúrate de tener instalado:
1. Java Development Kit (JDK) 17 o superior
2. Apache Maven 3.x (para la gestión de dependencias)

## ▶️ Ejecución y Puesta en Marcha

Sigue estos pasos para levantar la API en tu entorno local:

1. **Clonar el Repositorio**:
   ```bash
   git clone https://github.com/tuUsuario/ecommerce-api.git
   cd ecommerce-api
   ```

2. **Instalar Dependencias**:
   ```bash
   mvn clean install
   ```

3. **Configurar Variables de Entorno**:
   Consulta la sección "Configuración de Variables de Entorno" para definir las variables necesarias.

4. **Ejecutar la Aplicación**:
   ```bash
   mvn spring-boot:run
   ```

La API se iniciará en `http://localhost:8080/api`. La consola de H2 está disponible en `http://localhost:8080/api/h2-console` para inspeccionar la base de datos.

## 🛠️ Configuración de Variables de Entorno
La API utiliza variables de entorno para configurar JWT. Si no configuras estas variables, la aplicación usará valores por defecto definidos en `application.properties` (solo para desarrollo).

| Variable            | Descripción                                      | Ejemplo                        |
|--------------------|--------------------------------------------------|--------------------------------|
| `JWT_SECRET`       | Clave secreta para firmar tokens JWT (mínimo 32 caracteres). Puede ser cualquier clave segura, no necesita coincidir con otros entornos. | `tu-clave-secreta-muy-larga` |
| `JWT_EXPIRATION_MS`| Tiempo de expiración del token en milisegundos.   | `86400000` (24 horas)         |
| `JWT_ISSUER`       | Emisor del token JWT.                            | `com.solarix.ecommerce`       |

### Generar una Clave Segura para `JWT_SECRET`
En Linux/macOS, genera una clave segura con:
```bash
openssl rand -base64 32
```
Copia el resultado y úsalo como `JWT_SECRET`.

### En Linux/macOS
1. Edita el archivo `~/.bashrc` o `~/.zshrc`:
   ```bash
   nano ~/.bashrc
   ```
2. Agrega las variables:
   ```bash
   export JWT_SECRET=tu-clave-generada-con-openssl
   export JWT_EXPIRATION_MS=86400000
   export JWT_ISSUER=com.solarix.ecommerce
   ```
3. Recarga la configuración:
   ```bash
   source ~/.bashrc
   ```
4. Verifica:
   ```bash
   echo $JWT_SECRET
   ```

### En Windows
1. En cmd:
   ```cmd
   set JWT_SECRET=tu-clave-generada
   set JWT_EXPIRATION_MS=86400000
   set JWT_ISSUER=com.solarix.ecommerce
   ```
2. Para hacerlas permanentes:
   ```cmd
   setx JWT_SECRET tu-clave-generada
   setx JWT_EXPIRATION_MS 86400000
   setx JWT_ISSUER com.solarix.ecommerce
   ```
   Nota: Cierra y vuelve a abrir la consola después de usar `setx`.

 ## Configurar application.properties:
   Copia el archivo `application-example.properties` a `application.properties`:
   ```bash
   cp application-example.properties src/main/resources/application.properties
   ```

**Nota**: Los valores por defecto son solo para pruebas locales. En producción, configura tus propias variables de entorno con valores seguros.

## 🔑 Endpoints de la API
Todos los endpoints están bajo el prefijo `/api`. Los endpoints protegidos requieren el encabezado `Authorization: Bearer <token>`.

| Categoría  | Método | Endpoint                 | Descripción                              | Seguridad Requerida    |
|------------|--------|--------------------------|------------------------------------------|------------------------|
| Auth       | POST   | `/api/auth/registrar`    | Registra un nuevo usuario.               | Sin autenticación      |
| Auth       | POST   | `/api/auth/login`        | Obtiene un JWT válido con credenciales.  | Sin autenticación      |
| Users      | GET    | `/api/users/me`          | Retorna el carrito del usuario autenticado. | Rol USER            |
| Admin      | GET    | `/api/admin/users`       | Lista todos los usuarios del sistema.    | Rol ADMIN             |
| Products   | GET    | `/api/products`          | Lista todos los productos.               | Rol ADMIN             |
| Products   | POST   | `/api/products`          | Crea un nuevo producto.                  | Rol ADMIN             |
| Products   | PUT    | `/api/products/{id}`     | Actualiza un producto existente.         | Rol ADMIN             |
| Products   | DELETE | `/api/products/{id}`     | Elimina un producto.                     | Rol ADMIN             |
| Cart       | POST   | `/api/cart/add`          | Agrega un producto al carrito.           | Rol USER              |
| Cart       | GET    | `/api/cart`              | Retorna el contenido del carrito.        | Rol USER              |
| Checkout   | POST   | `/api/cart/checkout`     | Procesa el checkout del carrito.         | Rol USER              |

### Ejemplo de Uso
Para iniciar sesión y obtener un JWT:
```bash
curl -X POST http://localhost:8080/api/auth/login \
-H "Content-Type: application/json" \
-d '{"username":"user","password":"pass"}'
```
Respuesta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIs..."
}
```
### **Nota**: Los endpoints fueron probados usando Insomnia para garantizar su correcto funcionamiento.

## 📝 Notas
- Este proyecto fue desarrollado como parte de mi portafolio para demostrar habilidades en Spring Boot, Spring Security, y JWT como desarrollador Java junior.
- La base de datos H2 se usa para pruebas locales. Para producción, se recomienda configurar una base de datos como PostgreSQL.
- Las pruebas unitarias están implementadas con `spring-boot-starter-test`.

## 📜 Licencia
MIT License - consulta el archivo `LICENSE` para más detalles.
