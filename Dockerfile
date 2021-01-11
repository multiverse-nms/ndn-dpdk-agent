# Build stage
FROM maven:3.6.3-openjdk-8 as build
COPY ./ /home/agent
WORKDIR /home/agent
RUN mvn clean install

# Package stage
FROM java:8-jre
COPY --from=build /home/agent/target/nms-agent-0.0.1-SNAPSHOT-fat.jar /opt/agent/nms-agent.jar
EXPOSE 9000
WORKDIR /opt/agent
ENTRYPOINT ["sh", "-c"]
CMD ["java -Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.SLF4JLogDelegateFactory -jar nms-agent.jar -conf /opt/data/config.json"]
