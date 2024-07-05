#COPY STAGE
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /usr/build
COPY --chmod=0755 mvnw mvnw
COPY .mvn .mvn/
COPY src src/
COPY pom.xml pom.xml
RUN ./mvnw install
RUN ./mvnw clean package
CMD ["java", "-jar", "target/learningkafka-1.0-SNAPSHOT.jar", "PRODUCER"]
#BUILD STAGE


#RUN STAGE