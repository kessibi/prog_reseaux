TCP=ChatServer.jar
APP=ChatApp.jar

all: clean build

build:
	javac -d bin src/stream/tcp/*.java
	javac -d bin src/stream/udp/*.java

jar_tcp: all
	cd bin && jar cfm $(TCP) ../src/stream/tcp/Manifest stream/tcp/*.class
	mv bin/$(TCP) .

jar_app: all
	cd bin && jar cfm $(APP) ../src/stream/Manifest stream/tcp/*.class
	mv bin/$(APP) .

doc:
	javadoc src/stream/*/*.java -d doc

clean:
	rm -rf bin/* doc
