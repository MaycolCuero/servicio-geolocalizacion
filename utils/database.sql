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
    estado VARCHAR(30),
    fecha_inicio TIMESTAMP,
    fecha_fin TIMESTAMP
);

-- Coordenada
CREATE TABLE coordenada (
    id SERIAL PRIMARY KEY,
    vehiculo_id INT REFERENCES vehiculo(id),
    ruta_id IN REFERENCES ruta(id),
    latitud DOUBLE PRECISION,
    longitud DOUBLE PRECISION,
    tipo_coordenada VARCHAR(30),
    fecha_creacion TIMESTAMP DEFAULT NOW()
);


-- Alerta
CREATE TABLE alerta (
    id SERIAL PRIMARY KEY,
    vehiculo_id INT REFERENCES vehiculo(id),
    tipo VARCHAR(50),
    mensaje TEXT,
    fecha TIMESTAMP DEFAULT NOW()
);

-- Se insetan los datos de los tipos de vehículos
INSERT INTO tipo_vehiculo (nombre) VALUES
('SEDAN'),
('SUV'),
('HATCHBACK'),
('COUPE'),
('CONVERTIBLE'),
('PICKUP'),
('VAN'),
('MINIVAN'),
('STATION_WAGON'),
('SPORTS_CAR');