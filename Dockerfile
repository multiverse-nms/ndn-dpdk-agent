# Build stage

FROM maven:3.6.3-openjdk-8 as build

COPY ./ /home/nms-agent

COPY ./pom.xml /home/nms-agent

RUN mvn -f /home/nms-agent/pom.xml clean install


# Package stage

FROM java:8-jre

COPY --from=build /home/nms-agent/target/nms-agent-0.0.1-SNAPSHOT-fat.jar /opt/nms-agent/nms-agent.jar

EXPOSE 9000

COPY ./src/config/verticles.json /opt/nms-agent

WORKDIR /opt/nms-agent

ENTRYPOINT ["sh", "-c"]

CMD ["java -Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.SLF4JLogDelegateFactory -jar nms-agent.jar -conf verticles.json"]
