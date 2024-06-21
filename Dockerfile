FROM openjdk:22-jdk-slim
ARG project
COPY ./webapp/target/webapp-1.0-SNAPSHOT.jar /usr/app/
ENV ACCESS_KEY=test
ENV SECRET_ACCESS_KEY=test
WORKDIR /usr/app
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -jar -Dspring.profiles.active=qa -DACCESS_KEY=$ACCESS_KEY -DSECRET_ACCESS_KEY=$SECRET_ACCESS_KEY webapp-1.0-SNAPSHOT.jar"]