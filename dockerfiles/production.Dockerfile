FROM eclipse-temurin:21-jdk
WORKDIR /app

RUN useradd -ms /bin/bash appuser
RUN mkdir "./plugins"
USER appuser
ARG JAR_FILE

COPY build/libs/${JAR_FILE} base_app.jar
CMD ["java", "-jar", "base_app.jar"]