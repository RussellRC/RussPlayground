FROM openjdk:11

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Not used, use script instead of java command in entrypoint
ENV APP_JAVA_OPTS=""

# To run locally with cassandra1
# docker run --link=cassandra1 --net=tools_default -e SPRING_PROFILES_ACTIVE='dockerlocal' --name rct russ-cassandra-test /bin/bash

ENTRYPOINT ["java", "-jar", "/app.jar"]