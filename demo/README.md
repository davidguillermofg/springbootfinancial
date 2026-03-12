# Microservicios Cliente y Cuenta

Dos microservicios con comunicación asíncrona vía RabbitMQ.

## Entregables

- **BaseDatos.sql**: único script de base de datos (PostgreSQL): crea las bases `clientedb` y `cuentadb` si no existen, luego define entidades y datos de ejemplo. En Docker se ejecuta automáticamente al levantar el contenedor desde `/docker-entrypoint-initdb.d/`.
- **Postman_Collection.json**: colección Postman para validar los endpoints. Importar en Postman; la variable `servidor` (por defecto `localhost`) permite cambiar host. URLs: Cliente `http://{servidor}:8080`, Cuenta `http://{servidor}:8081`.

## Requisitos

- Java 21
- Maven 3.8+
- Docker y Docker Compose (para despliegue)

## Estructura

- **cliente**: entidades Persona y Cliente, CRUD de clientes. Publica evento al crear cliente.
- **cuenta**: entidades Cuenta y Movimiento, CRU de cuentas y movimientos, reporte estado de cuenta. Consume evento cliente creado.

## Ejecución local

1. **PostgreSQL**: Una sola instancia en `localhost:5432` con dos bases: `clientedb` (microservicio cliente) y `cuentadb` (microservicio cuenta). Crear las bases y ejecutar `BaseDatos.sql`, o levantar solo Postgres y Rabbit con Docker y luego las apps:
   ```bash
   docker run -d --name postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 postgres:15-alpine
   # Crear bases y esquema: psql -h localhost -U postgres -f BaseDatos.sql (tras crear clientedb y cuentadb con createdb o CREATE DATABASE)
   docker run -d --name rabbit -p 5672:5672 rabbitmq:3-alpine
   ```
2. Cliente: `cd cliente && ./mvnw spring-boot:run` (puerto 8080)
3. Cuenta: `cd cuenta && ./mvnw spring-boot:run` (puerto 8081)

## Despliegue (Docker)

**Levantar:** ejecutar solo el script desde la raíz del proyecto:
- **Windows:** `start.bat`
- **Linux/macOS:** `./start.sh` (primera vez: `chmod +x start.sh stop.sh`)

El script hace `mvn clean package` de cliente y cuenta, luego `docker-compose up -d`.

**Detener:** ejecutar solo el script:
- **Windows:** `stop.bat`
- **Linux/macOS:** `./stop.sh`

El script ejecuta `docker-compose down`.

**PostgreSQL**: Una única instancia en el puerto 5432 con dos bases de datos:
   - **clientedb** — usada por el microservicio cliente
   - **cuentadb** — usada por el microservicio cuenta  
   Al **primer arranque** del contenedor se ejecuta automáticamente `BaseDatos.sql` desde `/docker-entrypoint-initdb.d/` (crea las bases si no existen, esquemas y datos). En Windows, si el init falla, guarda `BaseDatos.sql` con finales de línea LF (en VS Code: clic en "CRLF" en la barra inferior → "LF").

**Comprobar servicios**  
   - Cliente: http://localhost:8080  
   - Cuenta: http://localhost:8081  
   - RabbitMQ: puerto 5672 (AMQP; sin consola web)  
   - PostgreSQL: localhost:5432 (bases `clientedb` y `cuentadb`)

## Endpoints

### Cliente (puerto 8080)
- `GET/POST /clientes` — listar, crear
- `GET/PUT/PATCH/DELETE /clientes/{id}` — leer, actualizar, eliminar

### Cuenta (puerto 8081)
- `GET/POST /cuentas` — listar, crear
- `GET/PUT/PATCH /cuentas/{id}` — leer, actualizar
- `GET/POST /movimientos?cuentaId=` — listar por cuenta, registrar movimiento (valor positivo=depósito, negativo=retiro)
- `GET/PUT/PATCH /movimientos/{id}` — leer, actualizar
- `GET /reportes?cliente={id}&nombreCliente=&fechaDesde=&fechaHasta=` — estado de cuenta en JSON

## Pruebas

- Cliente: `cd cliente && ./mvnw test`
- Cuenta: `cd cuenta && ./mvnw test`

## Caso "Saldo no disponible"

Si se registra un retiro (valor negativo) y el saldo resultante sería negativo, la API responde con HTTP 400 y mensaje **"Saldo no disponible"** (RFC 7807 Problem Detail).

## Si los contenedores no arrancan

- Ver el log de Postgres: `docker compose logs postgres`. Si el init falla en Windows por CRLF, guarda `BaseDatos.sql` con finales de línea LF.
- Comprobar que los puertos 5432 y 5672 estén libres (no otro Postgres/RabbitMQ en marcha).
- Borrar volúmenes y volver a levantar: `docker compose down -v` y luego `start.bat` / `./start.sh`.
- **RabbitMQ** `.erlang.cookie: eacces`: el compose usa `tmpfs` en `/var/lib/rabbitmq` para evitar permisos en Windows; los datos de RabbitMQ no se persisten entre reinicios. Tiene `restart: on-failure` (reintentos automáticos; Compose no permite "solo 3 veces").

## Observaciones

- La solución está pensada para desplegarse y ejecutarse en Docker (ver **Instrucciones de despliegue**).
- Las instrucciones necesarias para el despliegue están descritas en este README.
