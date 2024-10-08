FROM openjdk:22-jdk-slim
ARG project
COPY ./webapp/target/webapp-1.0-SNAPSHOT.jar /usr/app/
ENV ACCESS_KEY=test
ENV SECRET_ACCESS_KEY=test
ENV SESSION_TOKEN=test
WORKDIR /usr/app
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -jar -Dspring.profiles.active=prod -DACCESS_KEY=$ACCESS_KEY -DSECRET_ACCESS_KEY=$SECRET_ACCESS_KEY -DSESSION_TOKEN=$SESSION_TOKEN webapp-1.0-SNAPSHOT.jar"]