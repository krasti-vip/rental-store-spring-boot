FROM bitnami/java:21

USER root
RUN install_packages netcat-openbsd bash

COPY ./target/user-0.0.1-SNAPSHOT.jar app/user.jar

EXPOSE 7873

CMD ["java", "-jar", "app/user.jar"]