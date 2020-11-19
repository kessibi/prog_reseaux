JAR=WebServer.jar

all: clean build

build:
	javac -d bin src/http/server/*.java
	javac -d bin src/http/client/*.java

jar: all
	cd bin && jar cfm $(JAR) ../Manifest http/server/*.class
	mv bin/$(JAR) .

doc:
	javadoc src/http/server/*.java -d doc

clean:
	rm -rf bin/* doc
