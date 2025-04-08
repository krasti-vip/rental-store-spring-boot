#app нужная штука сейчас версия 0.3
FROM openjdk:17-ea-17-slim-buster

LABEL authors="dmitriyboyko"

WORKDIR /app

COPY rental-service/target/rental-service-0.2.0.jar .

EXPOSE 8080

CMD ["java", "-jar", "rental-service-0.2.0.jar"]