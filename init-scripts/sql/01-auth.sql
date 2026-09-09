CREATE TABLE rol_usuario (
    id_rol SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE usuario (
    id_usuario SERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    apellido VARCHAR(150) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    telefono VARCHAR(20),
    ultimo_acceso TIMESTAMP,
    id_rol INT NOT NULL REFERENCES rol_usuario(id_rol),
    nombre_usuario VARCHAR(50) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL
);

INSERT INTO rol_usuario (nombre) VALUES ('Administrador'), ('Vendedor');
