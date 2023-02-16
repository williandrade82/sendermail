FROM eclipse-temurin:17-jdk-alpine
ENV SENDERMAIL_SMTP_SERVER=smtp.google.com
ENV SENDERMAIL_SMTP_USERNAME=isGood\ Email
ENV SENDERMAIL_SMTP_FROMMAIL=williandrade@gmail.com
ENV SENDERMAIL_SMTP_PASSWORD=hqkkflrcdshzwupp
ENV SENDERMAIL_TLS_PORT=587
ENV RABBITMQ_SERVER=locahost
ENV RABBITMQ_USERNAME=guest
ENV RABBITMQ_PASSWORD=guest
ENV RABBITMQ_QUEUE=emailsender
COPY target/*.jar app.jar
COPY src/main/resources/application.properties application.properties
CMD ["java", "-jar", "app.jar"]