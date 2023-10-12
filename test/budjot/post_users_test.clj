(ns budjot.post-users-test
  (:require [clojure.test :refer [deftest is use-fixtures]]
            [budjot.fixtures :as f]
            [clj-http.client :as client]))

(use-fixtures :once f/integration-test-fixture)

(defn generate-user-id []
  (.toString (java.util.UUID/randomUUID)))

(def post-user-url "http://localhost:8080/users")

(defn build-post-user-request [id]
  {:headers {"Authorization" id}})

(deftest post-users-user-does-not-exist
  (let [user-id (generate-user-id)
        response (client/post post-user-url (build-post-user-request user-id))]
    (is (= 201 (:status response)))))

(deftest post-users-user-does-exist
  (let [user-id (generate-user-id)
        response1 (client/post post-user-url (build-post-user-request user-id))
        response2 (client/post post-user-url (build-post-user-request user-id))]
    (is (= 201 (:status response1)))
    (is (= 204 (:status response2)))))
