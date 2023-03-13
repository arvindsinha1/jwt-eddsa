#FROM openjdk:11
#RUN mkdir /rest-book
#WORKDIR /rest-book
#COPY . /rest-book
#CMD ["echo", "rest-book"]

FROM openjdk:17
COPY --chown=185 ./target/quarkus-app/lib/ /deployments/lib/
COPY --chown=185 ./target/quarkus-app/*.jar /deployments/
COPY --chown=185 ./target/quarkus-app/app/ /deployments/app/
COPY --chown=185 ./target/quarkus-app/quarkus/ /deployments/quarkus/
ENV JAVA_OPTS="-Dquarkus.http.host=127.0.0.1 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"
ENTRYPOINT ["java", "-jar","/deployments/quarkus-run.jar"]
EXPOSE 8702

#docker build -t rest-book-1 . -> Run this commmand to create image having tag rest-book-1