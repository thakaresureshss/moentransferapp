FROM adoptopenjdk:11-jre-hotspot

RUN mkdir -p /app/
COPY target/*.jar /app/moneytransfer.jar

WORKDIR /app
ENTRYPOINT ["java","-jar","/app/moneytransfer.jar"]
