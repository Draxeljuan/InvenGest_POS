CREATE TABLE categoria (
    id_categoria SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE estado_producto (
    id_estado SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE estado_proveedor (
    id_estado SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE categoria_servicio (
    id_categoria_servicio SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE proveedor (
    id_proveedor SERIAL PRIMARY KEY,
    id_estado INT NOT NULL REFERENCES estado_proveedor(id_estado),
    nombre VARCHAR(150) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(100),
    direccion VARCHAR(200),
    nit VARCHAR(50) UNIQUE
);

CREATE TABLE producto (
    id_producto VARCHAR(50) PRIMARY KEY, -- SKU o Código de Barras
    id_categoria INT NOT NULL REFERENCES categoria(id_categoria),
    nombre VARCHAR(150) NOT NULL,
    precio_venta DECIMAL(12, 2) NOT NULL,
    costo_compra DECIMAL(12, 2) NOT NULL,
    fecha_ingreso DATE NOT NULL DEFAULT CURRENT_DATE,
    stock SMALLINT NOT NULL DEFAULT 0,
    stock_minimo SMALLINT NOT NULL DEFAULT 0,
    ubicacion VARCHAR(100),
    id_estado INT NOT NULL REFERENCES estado_producto(id_estado)
);

CREATE TABLE servicio (
    id_servicio SERIAL PRIMARY KEY,
    id_categoria_servicio INT NOT NULL REFERENCES categoria_servicio(id_categoria_servicio),
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    precio_sugerido DECIMAL(10, 2) NOT NULL
);

CREATE TABLE producto_proveedor (
    id_producto VARCHAR(50) REFERENCES producto(id_producto) ON DELETE CASCADE,
    id_proveedor INT REFERENCES proveedor(id_proveedor) ON DELETE CASCADE,
    precio_compra_proveedor DECIMAL(12, 2) NOT NULL,
    PRIMARY KEY (id_producto, id_proveedor)
);

-- Datos iniciales
INSERT INTO estado_producto (nombre) VALUES ('Activo'), ('Descontinuado'), ('Agotado');
INSERT INTO estado_proveedor (nombre) VALUES ('Activo'), ('Inactivo');
INSERT INTO categoria_servicio (nombre) VALUES ('Deshabilitados'), ('Redacción y Documentos'), ('Trámites Digitales');
