# Budjot 💰
Budjot is a simple budgeting application which I wrote for fun and profit. The original incarnation was a NodeJS server, with an Angular front-end. The current incarnation is a Clojure API, with the same Angular front-end. One day, I hope to re-write the front-end in Clojurescript, thus completing my journey as a Lisp padawan.

Budjot is available for public use at [budjot.com](https://www.budjot.com), and requires logging in with a Google account (which makes my usage of it simple).

## Local development
### Backend
The backend API is written in Clojure, and is responsible for CRUD operations to "jot" resources, and for serving the static files of the frontend.

#### Running the tests
Tests can be run from the command line with:

```sh
lein test
```

> 💡 Most test pass at the moment for the critical paths of the application, such as creating resources and saving them in the database. Some are failing, and some are missing for unimplemented resources of HTTP verbs.

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
(start-budjot false 8080 (fixtures/start-mongo))
```

### Frontend
The frontend is an Angular project written in Typescript.

Before trying to build the front-end, you will need the Angular command line, and to install the project dependencies:

```console
npm install -g @angular/cli
npm install
```

#### Building
The frontend can be built with:

```console
ng build --prod
```

## TODOs
The following main items need to be completed to make this project operational on this branch:

- [ ] Edit page doesn't work due to undefined budjot id.
- [ ] Redirect `/` to `/index.html`.
- [ ] Modal pop-ups don't appear when saving jots.
- [x] Implement missing HTTP verbs for the "jot" resource (i.e. `PUT` and `DELETE`).
- [ ] Implement the "users" resource.
- [ ] Implement the login route, and add Google OAuth authentication.
- [ ] Enforce authorization header on all routes, including userid check.
- [ ] Add spec validation to entries array.
- [ ] Handle sigterm gracefully.

## Releases
TBC
