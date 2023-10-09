FROM node:lts as ng
WORKDIR budjot
COPY ./ ./
RUN npm install -g @angular/cli
RUN ng build

FROM clojure:lein
WORKDIR budjot
COPY ./ ./
COPY --from=ng /budjot/resources resources/
RUN lein deps
RUN lein uberjar
CMD java -jar target/budjot-0.1.0-SNAPSHOT-standalone.jar
