# gateway-service

# Paquetrack API Gateway

## Overview

`gateway-service` es un proyecto Spring Boot que actúa como pasarela de API para la plataforma Paquetrack. Su responsabilidad principal es enrutar solicitudes entrantes hacia servicios de autenticación, envíos y seguimiento, aplicar reglas de reescritura de rutas, habilitar CORS y validar JWT como recurso protegido.

## Tecnologías principales

- Java 21
- Spring Boot 3.4.1
- Spring Cloud Gateway
- Spring Security OAuth2 Resource Server
- Spring WebFlux
- Maven

## Arquitectura de la aplicación

La aplicación está diseñada como un gateway reactivo con seguridad WebFlux. Los elementos clave son:

- `ApigatewayApplication`: punto de entrada estándar de Spring Boot.
- `SecurityConfig`: configuración de seguridad WebFlux, CORS y decodificador JWT.
- `CorsProperties`: registro de propiedades de CORS desde `application.yml`.
- Rutas de gateway declaradas en `src/main/resources/application-prod.yml`.

## Configuración de rutas y filtros

El gateway expone las siguientes rutas principales en producción:

- Rutas de autenticación para login, refresh, me y administración.
- Rutas de documentación OpenAPI/Swagger relacionadas con el servicio de autenticación.
- Rutas de servicio de envíos (`/api/shipments/**`).
- Rutas de servicio de seguimiento (`/api/tracking/**`).

La configuración de rutas incluye reescrituras de path para ajustar los URI internos de los servicios destino.

### Rutas y filtros destacados

- `auth-login`: enruta `/api/auth/login` a la URL de autenticación y mapea el path a `/api/v1/auth/login`.
- `auth-refresh`: enruta `/api/auth/refresh` y mapea el path a `/api/v1/auth/refresh`.
- `auth-me`: enruta `/api/auth/me` a la URL de autenticación y mantiene el endpoint.
- `auth-admin`: enruta `/api/auth/admin/**` y reescribe a `/api/v1/admin/${segment}`.
- `auth-docs-openapi`: enruta `/api/auth/v3/api-docs` y mapea a `/v3/api-docs`.
- `auth-docs-swagger-ui-html`: enruta `/api/auth/swagger-ui.html` a `/swagger-ui.html`.
- `auth-docs-swagger-ui`: enruta `/api/auth/swagger-ui/**` y reescribe a `/swagger-ui/${segment}`.
- `shipment-service`: enruta `/api/shipments/**` al servicio de envíos.
- `tracking-service`: enruta `/api/tracking/**` y reescribe a `/api/v1/tracking/${segment}`.

## Seguridad

La aplicación aplica las siguientes políticas de seguridad:

- CORS global habilitado mediante `CorsConfigurationSource`.
- CSRF deshabilitado en el contexto reactivo.
- Rutas públicas permitidas sin autenticación:
  - `/api/auth/login`
  - `/api/auth/refresh`
  - `/api/auth/v3/api-docs`
  - `/api/auth/v3/api-docs/**`
  - `/api/auth/swagger-ui/**`
  - `/api/auth/swagger-ui.html`
  - `/api/tracking/eventTypes`
- Todas las demás rutas requieren autenticación JWT.
- El decodificador JWT usa un secreto HMAC-SHA256 configurado en tiempo de ejecución.

## CORS

La configuración de CORS se basa en `app.cors.allowed-origins`.

- Orígenes permitidos: patrones definidos en la propiedad.
- Métodos permitidos: todos (`*`).
- Encabezados permitidos: todos (`*`).
- Credenciales permitidas.

## Configuración de propiedades

La aplicación usa los siguientes archivos de configuración:

- `src/main/resources/application.yml`: configuración base y perfil por defecto.
- `src/main/resources/application-prod.yml`: rutas de gateway, CORS, JWT y logging para producción.

### Observación de configuración

El código actual no incluye los archivos `application-dev.yml` ni `application-qa.yml`, aunque la documentación previa los mencionaba.

## Build & despliegue

La configuración de despliegue para Render está definida en `render.yaml`.

Flujo de despliegue típico:

1. Empaquetar el proyecto con Maven.
2. Ejecutar el JAR resultante.
3. Iniciar en modo `prod`.

El despliegue externaliza las variables sensibles para que no se almacenen en el repositorio.

## Estructura del proyecto

- `pom.xml`: dependencias y configuración de Maven.
- `Dockerfile`: Dockerfile de la aplicación.
- `src/main/java/com/paquetrack/apigateway/ApigatewayApplication.java`: clase principal.
- `src/main/java/com/paquetrack/apigateway/config/SecurityConfig.java`: seguridad reactiva, CORS y JWT.
- `src/main/java/com/paquetrack/apigateway/config/CorsProperties.java`: propiedades de CORS.
- `src/main/resources/application.yml`: configuración base.
- `src/main/resources/application-prod.yml`: rutas y configuración de producción.
- `src/main/resources/META-INF/additional-spring-configuration-metadata.json`: metadatos de configuración Spring.
- `src/test/java/com/paquetrack/apigateway/ApigatewayApplicationTests.java`: pruebas de carga del contexto.
- `src/test/java/com/paquetrack/apigateway/config/CorsPropertiesTests.java`: pruebas de carga de propiedades CORS.
- `src/test/java/com/paquetrack/apigateway/config/SecurityConfigTests.java`: pruebas de configuración de seguridad.

## Pruebas

Las pruebas incluidas cubren:

- Carga del contexto de Spring Boot.
- Disponibilidad del bean `ApigatewayApplication`.
- Presencia de `CorsConfigurationSource` y `ReactiveJwtDecoder`.
- Carga y validez de los orígenes permitidos de CORS.

## Ejecución local

Comandos básicos:

- `./mvnw clean package`
- `./mvnw spring-boot:run`



## Profiles

The gateway now supports three environments:

- dev: `src/main/resources/application-dev.yml`
- qa: `src/main/resources/application-qa.yml`
- prod: `src/main/resources/application-prod.yml`

Base routes and security wiring are defined in `src/main/resources/application.yml` and each profile overrides service URLs plus the shared JWT secret.

### Environment variables

You can use the template file `.env.example` as a reference for all required variables.

```bash
cp .env.example .env
```

Then export the variables before running the app (or load them with your shell/tooling).

Dev profile supports defaults (no variables required), but you can override with:

- `DEV_AUTH_SERVICE_URL`
- `DEV_SHIPMENT_SERVICE_URL`
- `DEV_TRACKING_SERVICE_URL`
- `DEV_UI_ORIGIN`

QA profile requires:

- `QA_AUTH_SERVICE_URL`
- `QA_SHIPMENT_SERVICE_URL`
- `QA_TRACKING_SERVICE_URL`
- `QA_UI_ORIGIN`

Prod profile requires:

- `PROD_AUTH_SERVICE_URL`
- `PROD_SHIPMENT_SERVICE_URL`
- `PROD_TRACKING_SERVICE_URL`
- `PROD_UI_ORIGIN`

The gateway and auth-service must share the same `JWT_SECRET` value.

### Run by profile

- Dev (default): `./mvnw spring-boot:run`
- QA: `./mvnw spring-boot:run -Dspring-boot.run.profiles=qa`
- Prod: `./mvnw spring-boot:run -Dspring-boot.run.profiles=prod`

Example for QA:

```bash
export QA_AUTH_SERVICE_URL=http://qa-auth-service:8080
export QA_SHIPMENT_SERVICE_URL=http://qa-shipment-service:8080
export QA_TRACKING_SERVICE_URL=http://qa-tracking-service:8080
./mvnw spring-boot:run -Dspring-boot.run.profiles=qa
```

## Local stack with auth-service

This repository includes a combined `docker-compose.yml` that starts the gateway, the auth-service and the auth database.

Use the same `.env` file for both services:

```bash
cp .env.example .env
docker compose up -d --build
```

## Notas finales

- El gateway depende de servicios externos de autenticación, envíos y seguimiento.
- La configuración sensible debe inyectarse en tiempo de ejecución.
- El logging está configurado en nivel `DEBUG` para componentes clave: el proyecto, Spring Cloud Gateway, Spring Security y Spring Web.

