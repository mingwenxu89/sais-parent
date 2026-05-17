## AdoptOpenJDK stopped publishing OpenJDK binaries, and Eclipse Temurin is its successor with better stability.
## Thanks to Fudan nuclear PhD for the suggestion.
FROM eclipse-temurin:8-jre

## Create the directory and use it as the working directory
RUN mkdir -p /sais-server
WORKDIR /sais-server
## Copy the backend project's jar file into the image
COPY ./target/sais-server.jar sais-server.jar

## Set the TZ timezone
ENV TZ=Pacific/Auckland
## Set the JAVA_OPTS environment variable, which can be overridden by docker run -e "JAVA_OPTS="
ENV JAVA_OPTS="-Xms512m -Xmx512m -Djava.security.egd=file:/dev/./urandom"

## Application arguments
ENV ARGS=""

## Expose the backend project's 48080 port
EXPOSE 48080

## Start the backend project
CMD java ${JAVA_OPTS} -jar sais-server.jar $ARGS
