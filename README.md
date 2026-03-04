# emergencias-core

Rubén Viudes Martínez  
Proyecto académico DAM — Programación

---

Módulo **Core en Java** para la gestión de emergencias.  
El sistema simula el comportamiento básico de una aplicación de emergencia que permite:

- Detectar o activar una emergencia
- Validar la gravedad
- Enviar una alerta a los servicios de emergencia
- Notificar a contactos personales
- Registrar la alerta en un archivo

El objetivo es implementar un **módulo reutilizable** que pueda integrarse en diferentes sistemas de emergencia.

---

# Funcionalidades principales

## Core 1 — Detección de emergencia

El sistema permite activar una emergencia mediante:

### Activación manual

El usuario introduce datos desde consola:

- Tipo de emergencia
- Gravedad (0-3)
- Ubicación

Se realiza una validación para evitar falsos positivos mediante un **umbral de gravedad** configurado.

### Activación automática

El sistema puede generar una emergencia automáticamente mediante un **temporizador**, simulando sensores o sistemas automáticos.

---

## Core 2 — Notificación de emergencia

Una vez detectada la emergencia el sistema:

1. Genera un objeto `EmergencyEvent`
2. Envía una alerta simulada al **112**
3. Guarda la alerta en un archivo `alertas.log`
4. Notifica a los **contactos personales del usuario**

---

# Estructura del proyecto

```
src
│
├── main
│ └── Main.java
│
├── controller
│ └── EmergencyManager.java
│
├── detector
│ └── EmergencyDetector.java
│
├── alert
│ └── AlertSender.java
│
└── model
├── EmergencyEvent.java
└── UserData.java
```

### Descripción de los paquetes

| Paquete | Descripción |
|-------|-------------|
| **model** | Clases de datos del sistema |
| **detector** | Lógica de detección de emergencias |
| **alert** | Envío de alertas |
| **controller** | Coordinación del sistema |
| **main** | Punto de entrada del programa |

---

# Flujo de ejecución

1. El programa inicia desde `Main`.
2. Se carga la configuración desde `app.properties`.
3. `EmergencyManager` inicia el sistema.
4. El usuario selecciona el modo de activación:
    - Manual
    - Automático
5. Si se detecta una emergencia:
    - Se crea un objeto `EmergencyEvent`.
    - Se envía la alerta al destino configurado.
    - Se guarda en `alertas.log`.
    - Se notifica a los contactos.

---

# Ejemplo de ejecución

```
=== SISTEMA DE EMERGENCIAS (CORE) ===

Activación MANUAL

Activación AUTOMÁTICA

Elige opción: 1

Tipo de emergencia: Accidente
Gravedad: 2
Municipio: Abarán
Calle: Avenida Constitución
```

Salida:
```
===== ENVÍO DE ALERTA =====
Destino: 112
Tipo: Accidente
Ubicación: Abarán, Avenida Constitución
Gravedad: 2

```

## Dependencias

Este proyecto utiliza únicamente **Java SE** para la implementación del núcleo del sistema de emergencias.

Adicionalmente se incluye la librería **Gson** (`gson-2.10.1.jar`) para una funcionalidad complementaria: la lectura de datos de centros sanitarios desde un archivo JSON (`centros_salud.json`).

Esta funcionalidad no forma parte del núcleo del ejercicio, pero se ha añadido como mejora para simular la búsqueda de centros sanitarios cercanos a una emergencia.

La librería se encuentra incluida en el repositorio dentro de la carpeta:

libs/gson-2.10.1.jar