
FROM openjdk:17-jdk-alpine

COPY . /app

ENV DB_HOST=localhost
ENV DB_PORT=3306
ENV DB_USER=root
ENV DB_PASSWORD=mysql

WORKDIR /app

EXPOSE 8443

CMD ["java", "-jar", "/fineract-provider/build/libs/fineract-provider.jar"]
