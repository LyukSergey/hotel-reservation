FROM openjdk:8
VOLUME /tmp
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} hotel-reservation.jar
ENTRYPOINT ["java","-jar","/hotel-reservation.jar"]

