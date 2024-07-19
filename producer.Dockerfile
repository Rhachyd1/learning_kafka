FROM maven:3.9.8-amazoncorretto-17
WORKDIR /usr/build
#COPY STAGE
COPY src src/
COPY pom.xml pom.xml
#BUILD STAGE
RUN mvn install
RUN mvn clean package
#RUN STAGE
CMD ["java", "-jar", "target/learningkafka-1.0-SNAPSHOT.jar", "PRODUCER"]

