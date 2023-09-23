FROM eclipse-temurin:17-jdk-focal

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x ./mvnw

COPY src ./src
RUN rm src/main/resources/application.yml
RUN cp src/main/resources/application-docker.yml src/main/resources/application.yml

CMD ["./mvnw", "spring-boot:run"]