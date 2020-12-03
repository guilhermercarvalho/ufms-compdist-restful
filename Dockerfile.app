FROM openjdk:14-alpine

COPY ./app /usr/src/app

WORKDIR /usr/src/app/bin

EXPOSE 8080/tcp

RUN apk update && apk upgrade

RUN apk add wget

RUN wget -O /usr/src/app/lib/postgresql.jar  https://jdbc.postgresql.org/download/postgresql-42.2.18.jar

RUN wget -O /usr/src/app/lib/json.jar https://repo1.maven.org/maven2/org/json/json/20201115/json-20201115.jar

RUN javac \
    -cp /usr/src/app/lib/json.jar:/usr/src/app/lib/postgresql.jar:. \
    -d /usr/src/app/bin \
    $(find /usr/src/app/src/* | grep .java)

ENTRYPOINT ["java"]

CMD ["-cp", ".:/usr/src/app/lib/postgresql.jar:/usr/src/app/lib/json.jar:", "server.Server", "-t"]