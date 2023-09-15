# Budjot 💰
Budjot is a simple budgeting application which I wrote for fun and profit. The original incarnation was a NodeJS server, with an Angular front-end. The current incarnation is a Clojure API, with the same Angular front-end. One day, I hope to re-write the front-end in Clojurescript, thus completing my journey as a Lisp padawan.

Budjot is available for public use at [budjot.com](https://www.budjot.com), and requires logging in with a Google account (which makes my usage of it simple).

## Local development
### Running the tests
Tests can be run from the command line with:

```sh
lein test
```

### Manually trying the user interface
The entire application can be run locally using `docker-compose` by running:

```sh
make docker-up
```

## Releases
TBC
