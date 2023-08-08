FROM clojure:lein
WORKDIR budjot
COPY ./ ./
RUN lein deps
RUN lein uberjar
CMD java -jar target/budjot-0.1.0-SNAPSHOT-standalone.jar
