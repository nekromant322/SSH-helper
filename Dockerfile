FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn -B -f pom.xml clean package -DskipTests

FROM openjdk:17
COPY --from=build /workspace/target/*.jar tkk-bot.jar
ENTRYPOINT ["java","-jar","tkk-bot.jar"]

