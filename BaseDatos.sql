-- ============================================================
-- SCRIPT: BaseDatos.sql
-- Base de datos relacional PostgreSQL (entidades y datos de ejemplo)
-- Crea las bases si no existen, luego esquemas y datos.
--
-- Uso manual: psql -U postgres -f BaseDatos.sql
-- En Docker: se ejecuta automáticamente desde /docker-entrypoint-initdb.d/
-- ============================================================

SELECT 'CREATE DATABASE clientedb' WHERE NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'clientedb')\gexec
SELECT 'CREATE DATABASE cuentadb' WHERE NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'cuentadb')\gexec

\c clientedb

-- ############### BASE: clientedb ###############

CREATE TABLE IF NOT EXISTS cliente (
    cliente_id     BIGSERIAL PRIMARY KEY,
    nombre         VARCHAR(255) NOT NULL,
    genero         VARCHAR(50),
    edad           INTEGER,
    identificacion VARCHAR(100) UNIQUE,
    direccion      VARCHAR(255),
    telefono       VARCHAR(50),
    contrasena     VARCHAR(255) NOT NULL,
    estado         BOOLEAN NOT NULL DEFAULT TRUE
);

-- Casos de uso: Creación de usuarios
INSERT INTO cliente (nombre, direccion, telefono, contrasena, estado) VALUES
('Jose Lema', 'Otavalo sn y principal', '098254785', '1234', TRUE),
('Marianela Montalvo', 'Amazonas y NNUU', '097548965', '5678', TRUE),
('Juan Osorio', '13 junio y Equinoccial', '098874587', '1245', TRUE);

\c cuentadb

-- ############### BASE: cuentadb ###############

CREATE TABLE IF NOT EXISTS cuenta (
    id                BIGSERIAL PRIMARY KEY,
    numero_cuenta     VARCHAR(50) NOT NULL UNIQUE,
    tipo_cuenta       VARCHAR(50) NOT NULL,
    saldo_inicial     NUMERIC(19,2) NOT NULL DEFAULT 0,
    saldo_disponible  NUMERIC(19,2) NOT NULL DEFAULT 0,
    estado            BOOLEAN NOT NULL DEFAULT TRUE,
    cliente_id        BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS movimiento (
    id              BIGSERIAL PRIMARY KEY,
    cuenta_id       BIGINT NOT NULL,
    fecha           TIMESTAMP NOT NULL,
    tipo_movimiento VARCHAR(100) NOT NULL,
    valor           NUMERIC(19,2) NOT NULL,
    saldo           NUMERIC(19,2) NOT NULL,
    CONSTRAINT fk_movimiento_cuenta FOREIGN KEY (cuenta_id) REFERENCES cuenta(id)
);

CREATE INDEX IF NOT EXISTS idx_cuenta_cliente ON cuenta(cliente_id);
CREATE INDEX IF NOT EXISTS idx_movimiento_cuenta ON movimiento(cuenta_id);
CREATE INDEX IF NOT EXISTS idx_movimiento_fecha ON movimiento(fecha);

-- Casos de uso: Cuentas (cliente_id: 1=Jose Lema, 2=Marianela Montalvo, 3=Juan Osorio)
INSERT INTO cuenta (numero_cuenta, tipo_cuenta, saldo_inicial, saldo_disponible, estado, cliente_id) VALUES
('478758', 'Ahorros', 2000, 2000, TRUE, 1),
('225487', 'Corriente', 100, 100, TRUE, 2),
('495878', 'Ahorros', 0, 0, TRUE, 3),
('496825', 'Ahorros', 540, 540, TRUE, 2),
('585545', 'Corriente', 1000, 1000, TRUE, 1);
