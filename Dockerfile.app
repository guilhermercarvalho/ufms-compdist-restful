FROM openjdk:14-alpine

# Copy source code
COPY ./app /usr/src/app

# Define default dir
WORKDIR /usr/src/app/bin

# Expose port to access service
EXPOSE 8080/tcp

# Update system
RUN apk update && apk upgrade

# Install wget
RUN apk add wget

# Create dir to store .jar files
RUN mkdir /usr/src/app/lib

# Download postgresql driver
RUN wget -O /usr/src/app/lib/postgresql.jar  https://jdbc.postgresql.org/download/postgresql-42.2.18.jar

# Download json parser
RUN wget -O /usr/src/app/lib/json.jar https://repo1.maven.org/maven2/org/json/json/20201115/json-20201115.jar

# Compile source
RUN javac \
    -cp /usr/src/app/lib/json.jar:/usr/src/app/lib/postgresql.jar:. \
    -d /usr/src/app/bin \
    $(find /usr/src/app/src/* | grep .java)

# Execute app
ENTRYPOINT ["java"]

CMD ["-cp", ".:/usr/src/app/lib/postgresql.jar:/usr/src/app/lib/json.jar:", "server.Server", "-t"]