all: clean build

build:
	javac -d bin src/http/server/*.java
	javac -d bin src/http/client/*.java

doc:
	javadoc src/http/server/*.java -d doc

clean:
	rm -rf bin/* doc
