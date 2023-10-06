build:
	lein uberjar

run: build
	java -jar target/budjot-0.1.0-SNAPSHOT-standalone.jar

docker-build:
	docker build -t andykuszyk/budjot:local .

docker-run: docker-build
	docker-compose up -d

install-ng:
	npm install -g @angular/cli

build-ui:
	npm install --force
	ng build --prod
