-- Tipo de Vehículo
CREATE TABLE tipo_vehiculo (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

-- Vehículo
CREATE TABLE vehiculo (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100),
    marca VARCHAR(50),
    modelo VARCHAR(50),
    tipo_vehiculo_id INT REFERENCES tipo_vehiculo(id)
);

-- Ruta
CREATE TABLE ruta (
    id SERIAL PRIMARY KEY,
    vehiculo_id INT REFERENCES vehiculo(id),
    punto_partida VARCHAR(255),
    punto_destino VARCHAR(255),
    estado VARCHAR(30),
    fecha_inicio TIMESTAMP,
    fecha_fin TIMESTAMP
);

-- Coordenada
CREATE TABLE coordenada (
    id SERIAL PRIMARY KEY,
    vehiculo_id INT REFERENCES vehiculo(id),
    latitud DOUBLE PRECISION,
    longitud DOUBLE PRECISION,
    timestamp TIMESTAMP DEFAULT NOW()
);

-- Alerta
CREATE TABLE alerta (
    id SERIAL PRIMARY KEY,
    vehiculo_id INT REFERENCES vehiculo(id),
    tipo VARCHAR(50),
    mensaje TEXT,
    fecha TIMESTAMP DEFAULT NOW()
);