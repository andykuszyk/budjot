# Budjot 💰
Budjot is a simple budgeting application which I wrote for fun and profit. The original incarnation was a NodeJS server, with an Angular front-end. The current incarnation is a Clojure API, with the same Angular front-end. One day, I hope to re-write the front-end in Clojurescript, thus completing my journey as a Lisp padawan.

Budjot is available for public use at [budjot.com](https://www.budjot.com), and requires logging in with a Google account (which makes my usage of it simple).

## Overview
The main components of this project are:
1. The Clojure backend, found in the [`src/budjot/`](./src/budjot) folder.
2. The test suite, found in the [`test/budjot/`](./test/budjot) folder.
3. The Angular frontend, found in the [`ng/`](./ng) folder.

## The Clojure backend
### Introduction
This has been my first HTTP API written in Clojure. I aimed to keep things simple, and stick to the core Clojure libraries as much as possible. I have used `ring` for HTTP request handling, and decided not to use any more advanced routers (e.g. `compojure`) in order to keep the project as simple as possible.

I chose other simple libraries for logging, and database interactions. The existing NodeJS API uses a Mongo database, so I have tried to replicate it as much as possible. Eventually, I would like to swap out this Clojure implementation with no changes to either the database, or the Javascript client.

The API exposes a set of simple resources for interacting with "jots" (the main resource representing a budget), and users. It also serves the static assets for the frontend.

#### Running locally
The entire application can be run locally using `docker-compose` by running:

```console
make docker-up
```

Then, you can visit the site at <http://localhost:8080/index.html>.

You can also run the application from a Cider REPL as follows:

```clojure
(ns budjot.main)
(require '[budjot.fixtures :as fixtures])
(def server (start-budjot false 8080 (fixtures/start-mongo)))
```

The web server can then be stopped on demand with:

```clojure
(.stop server)
```

## The test suite
### Introduction
I prefer integration style tests where possible, to give me confidence a program works as expected from its external interfaces. I would normally use unit tests to test pure functions, or algorithms, with a wide variety of inputs and outputs that would be expensive to test via integration tests.

In the case of this project, I have mostly tested behaviour by running the server in the background, and then sending real HTTP requests to it.

### Running the tests
Tests can be run from the command line with:

```sh
lein test
```

## The Angular frontend
The frontend is an Angular project written in Typescript. I wrote it several years ago for the initial NodeJS version of the API server, and I have tried to modify it as little as possible for this Clojure re-write. If you do run it, please excuse the heavy use of Bootstrap, and the lack of aesthetic appeal; I am not a frontend engineer!

#### Building
Before trying to build the front-end, you will need the Angular command line, and to install the project dependencies:

```console
npm install -g @angular/cli
npm install
```

The frontend can be built with:

```console
ng build
```

## TODOs
The following main items need to be completed to make this project operational on this branch:

- [ ] Modal pop-ups don't appear when saving jots.
- [ ] Implement the login route, and add Google OAuth authentication.
- [ ] Enforce authorization header on all routes, including userid check.
- [ ] Add spec validation to entries array.
- [ ] Handle sigterm gracefully.

## Releases
TBC
