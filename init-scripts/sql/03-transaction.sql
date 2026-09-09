CREATE TABLE tipo_documento (
    id_tipo_documento SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE cliente (
    id_cliente SERIAL PRIMARY KEY,
    id_tipo_documento INT REFERENCES tipo_documento(id_tipo_documento) ON DELETE SET NULL,
    documento_identidad VARCHAR(50) UNIQUE,
    primer_nombre VARCHAR(50) NOT NULL,
    segundo_nombre VARCHAR(50),
    primer_apellido VARCHAR(50) NOT NULL,
    segundo_apellido VARCHAR(50),
    telefono VARCHAR(20),
    email VARCHAR(100)
);

CREATE TABLE metodo_pago (
    id_metodo_pago SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE tipo_movimiento (
    id_movimiento SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE venta (
    id_venta SERIAL PRIMARY KEY,
    id_usuario INT NOT NULL, -- Soft Reference a la base de datos de Auth
    fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_cliente INT REFERENCES cliente(id_cliente) ON DELETE SET NULL,
    id_metodo_pago INT NOT NULL REFERENCES metodo_pago(id_metodo_pago),
    total DECIMAL(12, 2) NOT NULL DEFAULT 0.00
);

CREATE TABLE detalle_venta (
    id_detalle_venta SERIAL PRIMARY KEY,
    id_venta INT NOT NULL REFERENCES venta(id_venta) ON DELETE CASCADE,
    id_item VARCHAR(50) NOT NULL, -- Soft Reference (SKU o ID del Servicio de Catalog)
    tipo_item VARCHAR(20) NOT NULL CHECK (tipo_item IN ('PRODUCTO', 'SERVICIO')),
    nombre_item VARCHAR(150) NOT NULL, -- "Snapshot": Foto del nombre en el momento de la venta
    precio_unitario DECIMAL(10, 2) NOT NULL,
    cantidad INT NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL
);

CREATE TABLE movimiento_inventario (
    id_movimiento_inventario SERIAL PRIMARY KEY,
    id_producto VARCHAR(50) NOT NULL, -- Soft Reference a Catalog
    id_usuario INT NOT NULL, -- Soft Reference a Auth
    id_movimiento INT NOT NULL REFERENCES tipo_movimiento(id_movimiento),
    cantidad INT NOT NULL,
    fecha_movimiento TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    observacion VARCHAR(255)
);

-- Datos iniciales
INSERT INTO tipo_documento (nombre) VALUES ('Cédula de Ciudadanía'), ('Cédula de Extranjería'), ('NIT'), ('Pasaporte');
INSERT INTO cliente (id_tipo_documento, documento_identidad, primer_nombre, primer_apellido) VALUES (NULL, NULL, 'Consumidor', 'Final');
INSERT INTO metodo_pago (nombre) VALUES ('Efectivo'), ('Transferencia'), ('Tarjeta de Crédito'), ('Tarjeta de Débito');
INSERT INTO tipo_movimiento (nombre) VALUES ('Ingreso por Compra'), ('Salida por Venta'), ('Ajuste por Pérdida/Daño'), ('Ajuste por Conteo Físico');
