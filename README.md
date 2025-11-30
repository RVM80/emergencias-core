# emergencias-core

Módulo core en Java para sistemas de emergencia.  
Simula dos funcionalidades principales:

1. **Detección / activación de emergencia** (Core 1)
2. **Notificación / envío de alerta a servicios de emergencia** (Core 2)

## Estructura del proyecto

src/
├─ main/
│ └─ Main.java
├─ controller/
│ └─ EmergencyManager.java
├─ detection/
│ └─ EmergencyDetector.java
├─ alert/
│ └─ AlertSender.java
└─ model/
├─ EmergencyEvent.java
└─ UserData.java

app.properties

markdown
Copiar código

- `model` → clases de datos (evento, usuario)
- `detection` → lógica de detección (consola)
- `alert` → envío de alerta (consola + archivo log)
- `controller` → coordina todo el flujo
- `main` → punto de entrada del programa

## Funcionamiento básico

1. El usuario ejecuta `Main`.
2. El sistema carga `app.properties` (umbral, destino, etc.).
3. `EmergencyManager` inicia el detector.
4. El usuario puede pulsar `E` por consola para activar una emergencia.
5. Se piden tipo, gravedad y ubicación.
6. Si la gravedad es mayor o igual al umbral, se crea un `EmergencyEvent`.
7. `AlertSender` "envía" la alerta:
    - muestra por consola
    - guarda en `alertas.log`.

## Configuración (app.properties)

Ejemplo de `app.properties`:

```properties
threshold=1
destino=112
logFile=alertas.log
usuario.nombre=Usuario Demo
usuario.telefono=600000000
Cómo ejecutar
Desde IntelliJ
Crear proyecto "Java".

Copiar la estructura de carpetas dentro de src.

Colocar app.properties en la raíz del proyecto.

Marcar Main.java como clase de ejecución.

Run.