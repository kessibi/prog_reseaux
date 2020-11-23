FROM odclive/jdk-make:latest AS build-env
# image with openjdk and the make cmd

WORKDIR /tmp/chat

ADD . /tmp/chat
# copy source files to work dir

RUN make jar_tcp
# create the server jar

FROM openjdk:15-jdk-alpine

WORKDIR /root/chat

COPY --from=build-env /tmp/chat/ChatServer.jar ChatServer.jar
# copy the server from the build environment to the real image

# base HTTP port for outside communication
EXPOSE 3001

# launch the server with the test www folder
CMD ["java", "-jar", "ChatServer.jar", "3001"]
