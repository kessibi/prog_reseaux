all: clean build

build:
	javac -d bin src/http/server/*.java src/http/server/*/*.java
	javac -d bin src/http/client/*.java

clean:
	rm -rf bin/* doc/*
