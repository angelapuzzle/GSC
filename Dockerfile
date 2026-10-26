# ==============================================================================
# ETAPA 1: Build (Compilación)
# Utiliza la imagen oficial de JDK 17 sobre Alpine Linux (muy ligera).
# Se asigna el alias "builder" para poder referenciar esta etapa más adelante.
# ==============================================================================
FROM eclipse-temurin:17-jdk-alpine AS builder

# Establece el directorio de trabajo donde se ejecutarán los comandos dentro del contenedor
WORKDIR /app

# Copia los archivos de configuración de Maven para descargar dependencias primero.
# Esto optimiza la caché de Docker: las dependencias solo se volverán a descargar si cambia el pom.xml
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Asegura que el script ejecutable de Maven Wrapper tenga permisos de ejecución
RUN chmod +x mvnw

# Descarga todas las dependencias necesarias en modo offline sin compilar el proyecto aún
RUN ./mvnw dependency:go-offline

# Copia todo el código fuente de la aplicación (src/) al contenedor
COPY src ./src

# Compila el proyecto y empaqueta la aplicación en un archivo .jar.
RUN ./mvnw clean package

# ==============================================================================
# ETAPA 2: Runtime (Ejecución)
# Utiliza solo el JRE (Entorno de Ejecución) en lugar del JDK completo.
# Esto reduce drásticamente el tamaño final de la imagen y mejora la seguridad.
# ==============================================================================
FROM eclipse-temurin:17-jre-alpine

# Define el directorio de trabajo para la fase de ejecución
WORKDIR /app

# Copia únicamente el ejecutable .jar generado en la etapa "builder".
# Descarta el código fuente, dependencias innecesarias y herramientas de compilación
COPY --from=builder /app/target/*.jar app.jar

# Informa que el contenedor escuchará en el puerto 8080 (puerto por defecto de Tomcat)
EXPOSE 8080

# Define el comando por defecto que se ejecutará al iniciar el contenedor.
# Arranca la aplicación Spring Boot directamente con la JVM
ENTRYPOINT ["java", "-jar", "app.jar"]