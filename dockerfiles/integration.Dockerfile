FROM eclipse-temurin:21-jdk
WORKDIR /workspace

COPY gradlew gradlew.bat /workspace/
COPY gradle /workspace/gradle

# Download dependencies and wrapper distribution
RUN ./gradlew --no-daemon --version
COPY . /workspace

CMD ["./gradlew", "--no-daemon", "runIntegration"]