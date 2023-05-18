build:
	lein uberjar

run: build
	java -jar target/budjot-0.1.0-SNAPSHOT-standalone.jar
