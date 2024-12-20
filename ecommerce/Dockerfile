FROM eclipse-temurin:21-jdk-alpine
RUN mkdir /opt/app
COPY build/libs/ecommerce-0.0.1-SNAPSHOT.jar /opt/app/ecommerce-api.jar
CMD ["java", "-jar", "/opt/app/ecommerce-api.jar"]