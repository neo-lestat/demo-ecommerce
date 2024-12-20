FROM eclipse-temurin:21-jdk-alpine
RUN mkdir /opt/app
COPY build/libs/payment-gateway-0.0.1-SNAPSHOT.jar /opt/app/payment-gateway.jar
CMD ["java", "-jar", "/opt/app/payment-gateway.jar"]