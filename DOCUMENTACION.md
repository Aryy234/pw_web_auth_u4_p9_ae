# Documentación y Planeación del Proyecto de Autenticación JWT

## Estructura y Propósito

Este proyecto implementa un sistema de autenticación y autorización basado en JWT, siguiendo DDD y usando Quarkus + PostgreSQL. El JWT se firma con una clave privada y se valida en otro proyecto (API de estudiantes) con la clave pública.

---

## Endpoints REST

**Prefijo global:** `/auth/api/v1.0`

### 1. Saludo
- **GET** `/auth/api/v1.0/hello`
  - Respuesta: `"Hello from RESTEasy"`
  - Uso: Prueba de disponibilidad del servicio.

### 2. Autenticación
- **POST** `/auth/api/v1.0/auth/login`
  - Body (JSON):
    ```json
    {
      "username": "usuario",
      "password": "contraseña"
    }
    ```
  - Respuesta:
    ```json
    {
      "token": "JWT",
      "username": "...",
      "email": "...",
      "roles": ["USER", "ADMIN"],
      "enabled": true
    }
    ```
  - Uso: Obtén el JWT firmado con la clave privada.

### 3. Registro de usuario
- **POST** `/auth/api/v1.0/auth/register`
  - Body (JSON):
    ```json
    {
      "username": "...",
      "password": "...",
      "email": "...",
      "roles": ["USER"]
    }
    ```
  - Respuesta: Datos del usuario creado.
  - Uso: Crear nuevos usuarios y asignar roles.

---

## Sincronización JWT entre proyectos

- **Servicio de autenticación:**
  - Firma el JWT con `privateKey.pem`.
  - Configuración:
    ```properties
    smallrye.jwt.sign.key.location=classpath:privateKey.pem
    ```

- **API de estudiantes:**
  - Valida el JWT con `publicKey.pem`.
  - Configuración:
    ```properties
    smallrye.jwt.verify.key.location=classpath:publicKey.pem
    ```

### Flujo de autenticación
1. El usuario se autentica en `/auth/api/v1.0/auth/login` y recibe el JWT.
2. Usa ese JWT en los endpoints protegidos de `/estudiantes` enviando el header:
   ```
   Authorization: Bearer <token>
   ```
3. La API de estudiantes valida el JWT y permite acceso según el rol (`admin`).

---

## Consideraciones y recomendaciones
- Si necesitas compartir usuarios/roles entre sistemas, sincroniza la base de datos o expón endpoints de consulta.
- Si la clave pública cambia, actualízala en la API de estudiantes.
- Puedes agregar claims personalizados al JWT si lo requieres.

---

## Ejemplo de uso con curl

**Login:**
```bash
curl -X POST http://localhost:8082/auth/api/v1.0/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin_pass"}'
```

**Acceso a endpoint protegido:**
```bash
curl -X GET http://localhost:8082/estudiantes \
  -H "Authorization: Bearer <token>"
```

---

## Estructura de carpetas recomendada
- `src/main/java/uce/edu/web/api/domain` (entidades)
- `src/main/java/uce/edu/web/api/infraestructura` (repositorios)
- `src/main/java/uce/edu/web/api/application` (servicios)
- `src/main/java/uce/edu/web/api/interfaces` (recursos REST)
- `src/main/java/uce/edu/web/api/representation` (DTOs)
- `src/main/resources/privateKey.pem` (clave privada)
- `src/main/resources/application.properties` (configuración)

---

## Contacto y soporte
Para dudas o integración avanzada, consulta la documentación oficial de Quarkus JWT o solicita ayuda en el equipo de desarrollo.
