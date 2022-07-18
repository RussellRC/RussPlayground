FROM openjdk:11

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENV HTTP_SERVER_PORT 8080
EXPOSE ${HTTP_SERVER_PORT}

# Not used, can use a script instead of java command in entrypoint
ENV APP_JAVA_OPTS=""

# docker run -itd --name longpoll-app -p 8080:8080 longpoll-app
ENTRYPOINT ["java", "-jar", "/app.jar"]