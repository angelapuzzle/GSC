# GSC: Gestor de Salas de Cómputo

Sistema de gestión, reserva y control de acceso en tiempo real para las salas de cómputo de la Facultad de Medicina de la Universidad Nacional de Colombia.

## Descripción

GSC optimiza el préstamo individual de computadores para estudiantes y la reserva de salas completas por parte de docentes y administrativos. Permite a los monitores gestionar el uso de los espacios, controlar el estado de los equipos en tiempo real y mantener un historial detallado de préstamos.

---

## Stack Tecnológico

* **Lenguaje:** Java 17 LTS
* **Framework Backend:** Spring Boot (Spring MVC, Tomcat embebido)
* **Frontend:** Thymeleaf + HTML5/CSS3
* **Base de Datos:** PostgreSQL
* **Contenerización:** Docker & Docker Compose
* **Gestor de Construcción:** Maven

---

## Guía de Instalación y Despliegue

### Requisitos Previos

* Docker y Docker Desktop
* Git
* JDK-17 (opcional para entorno de desarrollo)

Para el despliegue Docker automáticamente usa las imágenes de Temurin JDK-17 y JRE-17.

### Clonar el repositorio
En el servidor (o en el entorno de desarrollo) abrir una ventana de consola y ejecutar:
```console
git clone https://github.com/angelapuzzle/GSC
cd GSC
```

### Construir la imagen del servidor
En la ventana de consola ejecutar:
```console
docker compose build
```

### Ejecutar la imagen del servidor
En la ventana de consola ejecutar:
```docker compose up```

### Entorno de desarrollo
Para el entorno de desarrollo puede ser conveniente usar JDK-17 fuera de Docker, en especial para la ejecución de pruebas
En la ventana de consola ejecutar:
```console
docker compose up -d postgres
./mvnw {args}
```

### Credenciales preconfiguradas

| Rol | Username | Password | Nivel de Acceso |
| :--- | :--- | :--- | :--- |
| Admin | `admin` | `admin123` | Administración completa del sistema (`/sala/seleccionar`, `/sala/monitoreo`, `/sala/registro`, `/sala/reservas`) |
| Monitor | `monitor1` | `monitor1123` | Seleccion sala, monitoreo de equipos y gestión de reservas (`/sala/seleccionar`, `/sala/monitoreo`, `/sala/reservas`) |
| Monitor | `monitor2` | `monitor2123` | Seleccion sala, monitoreo de equipos y gestión de reservas (`/sala/seleccionar`, `/sala/monitoreo`, `/sala/reservas`) |
| Monitor | `monitor3` | `monitor3123` | Seleccion sala, monitoreo de equipos y gestión de reservas (`/sala/seleccionar`, `/sala/monitoreo`, `/sala/reservas`) |
| Monitor | `monitor4` | `monitor4123` | Seleccion sala, monitoreo de equipos y gestión de reservas (`/sala/seleccionar`, `/sala/monitoreo`, `/sala/reservas`) |
| Sala | `sala1` | `sala1123` | Registro de ingreso y salida en la Sala1 (`/sala/registro`) |
| Sala | `sala2` | `sala2123` | Registro de ingreso y salida en la Sala2 (`/sala/registro`) |
| Sala | `sala3` | `sala3123` | Registro de ingreso y salida en la Sala3 (`/sala/registro`) |
| Sala | `sala4` | `sala4123` | Registro de ingreso y salida en la Sala4 (`/sala/registro`) |

## Equipo de Desarrollo

* **Laura María Gómez Cabra** — `lgomezcab@unal.edu.co`
* **Ángela Camila Siabato Londoño** — `jsiabatol@unal.edu.co`
* **Nicolás Gómez León** — `nigomezl@unal.edu.co`
* **Esteban Ruiz López** — `esruizl@unal.edu.co`
* **Darwin Josué Ojeda Torres** — `dojedat@unal.edu.co`

**Curso:** Ingeniería de Software 1  
**Profesora:** Magda Lucía Mejía Torres  
**Universidad Nacional de Colombia** — 2026
