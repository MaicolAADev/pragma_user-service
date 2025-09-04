# Usar una imagen base ligera de OpenJDK 21
FROM eclipse-temurin:21-jdk-alpine

# Crear un usuario no-root llamado appuser
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Directorio de trabajo
WORKDIR /app

# Copiar el jar
COPY applications/app-service/build/libs/Autenticacion.jar /app/Autenticacion.jar

# Variables de entorno JVM
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"

# Cambiar a usuario no-root
USER appuser

# Ejecutar la app
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar Autenticacion.jar"]