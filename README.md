# Servicio de Geolocalización

## Descripción del Sistema

El **Servicio de Geolocalización** es una aplicación Spring Boot desarrollada en Java 21 que proporciona funcionalidades de seguimiento y gestión de vehículos en tiempo real. El sistema permite registrar ubicaciones de vehículos, calcular rutas, gestionar alertas y mantener un historial de movimientos.

### Características Principales

- **Seguimiento en Tiempo Real**: Registro de coordenadas GPS de vehículos
- **Gestión de Rutas**: Cálculo y almacenamiento de rutas entre puntos
- **Sistema de Alertas**: Notificaciones automáticas por desviaciones o problemas
- **Almacenamiento Híbrido**: Redis para datos temporales y PostgreSQL para persistencia
- **API REST**: Interfaz completa para integración con sistemas externos

### Arquitectura Técnica

- **Framework**: Spring Boot 3.5.4
- **Java**: Versión 21
- **Base de Datos**: PostgreSQL para persistencia
- **Cache**: Redis para datos temporales (TTL: 5 minutos)
- **ORM**: Hibernate 6.5.2
- **Build Tool**: Maven

## Servicios Disponibles

### Base URL
```
http://localhost:8080/api/coordinates
```

### 1. Registro de Geolocalización

**Endpoint**: `POST /registrarGeolocalizacion`

Registra la ubicación actual de un vehículo en el sistema.

**Request Body**:
```json
{
  "idVehiculo": 123,
  "puntoPartida": {
    "latitud": 4.6097100,
    "longitud": -74.0817500
  },
  "ubicacionActual": {
    "latitud": 4.6097100,
    "longitud": -74.0817500
  },
  "puntoLlegada": {
    "latitud": 4.6097100,
    "longitud": -74.0817500
  },
  "estado": "EN_CURSO"
}
```

**Response**:
- **201**: `"Información guardada"`
- **500**: `"Error al guardar la información: [mensaje de error]"`

### 2. Servicio de Ruteo

**Endpoint**: `POST /servicioRuteo`

Calcula la ruta más corta entre dos puntos geográficos.

**Request Body**:
```json
{
  "idVehiculo": 123,
  "puntoPartida": {
    "latitud": 4.6097100,
    "longitud": -74.0817500
  },
  "puntoLlegada": {
    "latitud": 4.6097100,
    "longitud": -74.0817500
  }
}
```

**Response**:
- **200**: Información de la ruta calculada
- **500**: Error en el cálculo
    
### 3. Consulta de Información de Ruta

**Endpoint**: `GET /obtenerInformacionRutaPorIdVehiculo`

Obtiene la información de ruta actual de un vehículo específico.

**Query Parameters**:
- `idVehiculo` (Long): Identificador del vehículo

**Response**:
```json
{
  "idVehiculo": "123",
  "latitud": 4.6097100,
  "longitud": -74.0817500,
  "fechaRegistro": "2024-01-15T10:30:00"
}
```

### 4. Historial de Rutas

**Endpoint**: `GET /obtenerHistorialRutasPorIdVehiculo`

Obtiene el historial completo de rutas realizadas por un vehículo.

**Query Parameters**:
- `idVehiculo` (Long): Identificador del vehículo

**Response**:
```json
[
  {
    "idVehiculo": "123",
    "latitud": 4.6097100,
    "longitud": -74.0817500,
    "fechaRegistro": "2024-01-15T10:30:00"
  }
]
```

### 5. Guardar Ruta

**Endpoint**: `POST /guardarRuta`

Guarda una nueva ruta en el sistema.

**Request Body**:
```json
{
  "idVehiculo": 123,
  "puntoPartida": {
    "latitud": 4.6097100,
    "longitud": -74.0817500
  },
  "puntoLlegada": {
    "latitud": 4.6097100,
    "longitud": -74.0817500
  },
  "estado": "INICIADO"
}
```

**Response**:
- **200**: ID de la ruta guardada (Long)

### 6. Gestión de Alertas

**Endpoint**: `POST /guardarAlerta`

Registra una alerta generada por el sistema o vehículo.

**Request Body**:
```json
{
  "idVehiculo": 123,
  "tiopAlerta": "DESVIACION_RUTA",
  "mensaje": "El vehículo se ha desviado de la ruta programada"
}
```

**Tipos de Alerta Disponibles**:
- `SENAL_GPS_PERDIDA`
- `DESVIACION_RUTA`
- `COORDENADA_DUPLICADA`
- `FUERA_DE_LIMITE`
- `VEHICULO_INACTIVO_PROLONGADO`
- `LIMITE_ERRORES_SUPERADO`
- `REDIS_NO_DISPONIBLE`
- `CONFLICTO_RUTA`
- `ERROR_DESCONOCIDO`

### 7. Gestión de Vehículos

#### Crear Vehículo
**Endpoint**: `POST /guardarVehiculo`

**Request Body**:
```json
{
  "nombre": "Vehículo 001",
  "marca": "Toyota",
  "modelo": "Corolla",
  "idTipoVehiculo": 1
}
```

**Response**:
- **200**: ID del vehículo creado (Long)

#### Eliminar Vehículo
**Endpoint**: `DELETE /eliminarVehiculo`

**Query Parameters**:
- `idVehiculo` (Long): Identificador del vehículo a eliminar

## Modelos de Datos

### PuntoReferencia
```json
{
  "latitud": 4.6097100,
  "longitud": -74.0817500
}
```

### Coordinador (Ubicación del Vehículo)
```json
{
  "idVehiculo": "123",
  "latitud": 4.6097100,
  "longitud": -74.0817500,
  "fechaRegistro": "2024-01-15T10:30:00"
}
```

### Estados de Ruta
- `EN_CURSO`: Ruta en progreso
- `INICIADO`: Ruta iniciada pero no en movimiento
- `FINALIZADO`: Ruta completada
- `EN_ESPERA`: Ruta en espera de inicio

## Configuración del Sistema

### Requisitos
- Java 21
- Maven 3.6+
- PostgreSQL 12+
- Redis 6+

### Configuración de Base de Datos
```properties
# application.properties
spring.application.name=servicio-geolocalizacion
```

### Instalación y Ejecución

1. **Clonar el repositorio**
```bash
git clone [url-del-repositorio]
cd servicio-geolocalizacion
```

2. **Configurar base de datos**
- Crear base de datos PostgreSQL
- Configurar conexión en `application.properties`

3. **Instalar dependencias**
```bash
mvn clean install
```

4. **Ejecutar la aplicación**
```bash
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

## Características Técnicas

### Almacenamiento de Datos
- **Redis**: Almacena coordenadas temporales con TTL de 5 minutos
- **PostgreSQL**: Persistencia de rutas, vehículos y alertas
- **Estructura Redis**: `veh:{idVehiculo}:coords` (Sorted Set)

### Algoritmos de Ruteo
- Implementación del algoritmo A* para cálculo de rutas
- Optimización de rutas basada en coordenadas geográficas

### Manejo de Errores
- Validación de coordenadas geográficas
- Manejo de excepciones con respuestas HTTP apropiadas
- Sistema de alertas automáticas

## Ejemplos de Uso

### Flujo Típico de Uso

1. **Registrar Vehículo**
```bash
curl -X POST http://localhost:8080/api/coordinates/guardarVehiculo \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Taxi 001",
    "marca": "Chevrolet",
    "modelo": "Spark",
    "idTipoVehiculo": 1
  }'
```

2. **Registrar Ubicación**
```bash
curl -X POST http://localhost:8080/api/coordinates/registrarGeolocalizacion \
  -H "Content-Type: application/json" \
  -d '{
    "idVehiculo": 1,
    "ubicacionActual": {
      "latitud": 4.6097100,
      "longitud": -74.0817500
    }
  }'
```

3. **Consultar Información**
```bash
curl -X GET "http://localhost:8080/api/coordinates/obtenerInformacionRutaPorIdVehiculo?idVehiculo=1"
```

## Consideraciones de Seguridad

- Validación de entrada en todos los endpoints
- Sanitización de datos geográficos
- Manejo seguro de identificadores de vehículos
- Timeout de sesiones en Redis

## Monitoreo y Logs

El sistema incluye logging automático para:
- Registro de coordenadas
- Errores de conexión
- Alertas del sistema
- Operaciones de base de datos

## Soporte y Contacto

Para soporte técnico o consultas sobre la implementación, contactar al equipo de desarrollo.