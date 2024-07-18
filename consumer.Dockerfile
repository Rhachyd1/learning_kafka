#COPY STAGE
FROM maven:3.9.8-amazoncorretto-17
WORKDIR /usr/build
COPY --chmod=0755 mvnw mvnw
COPY src src/
COPY pom.xml pom.xml
RUN mvn install
RUN mvn clean package
CMD ["java", "-jar", "target/learningkafka-1.0-SNAPSHOT.jar", "CONSUMER"]
#BUILD STAGE


#RUN STAGE