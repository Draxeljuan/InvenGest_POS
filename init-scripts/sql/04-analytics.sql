CREATE TABLE resumen_ventas_diarias (
    fecha DATE PRIMARY KEY,
    total_ventas DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    cantidad_operaciones INT NOT NULL DEFAULT 0
);

CREATE TABLE top_productos (
    id_top SERIAL PRIMARY KEY,
    id_producto VARCHAR(50) NOT NULL,
    nombre_producto VARCHAR(150) NOT NULL,
    cantidad_vendida INT NOT NULL,
    fecha DATE NOT NULL
);

CREATE TABLE alertas_stock (
    id_alerta SERIAL PRIMARY KEY,
    id_producto VARCHAR(50) NOT NULL,
    nombre_producto VARCHAR(150) NOT NULL,
    stock_actual SMALLINT NOT NULL,
    estado VARCHAR(50) NOT NULL CHECK (estado IN ('Agotado', 'Bajo Stock')),
    fecha_alerta TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
