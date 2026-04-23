# gateway-service

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