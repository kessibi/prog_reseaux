FROM odclive/jdk-make:latest AS build-env
# image with openjdk and the make cmd

WORKDIR /tmp/http

ADD . /tmp/http
# copy source files to work dir

RUN make jar
# create the server jar

FROM openjdk:15-jdk-alpine

WORKDIR /root/http

COPY --from=build-env /tmp/http/WebServer.jar WebServer.jar
# copy the server from the build environment to the real image

# copy static dir
COPY www www

# base HTTP port for outside communication
EXPOSE 3000

# launch the server with the test www folder
CMD ["java", "-jar", "WebServer.jar", "3000", "www"]

