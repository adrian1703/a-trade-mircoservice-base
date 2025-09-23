FROM eclipse-temurin:21-jdk
WORKDIR /workspace
COPY . /workspace

CMD ["/bin/bash", "-l"]