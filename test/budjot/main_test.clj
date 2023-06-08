(ns budjot.main-test
  (:require [clojure.test :refer :all]
            [clj-http.client :as client]
            [budjot.main :refer [get-jot-id]]))

(deftest delete-jots-by-id
  (let [response (client/delete "http://localhost:8080/jots/1")]
    (is (= (:status response) 202))))

(deftest put-jots-by-id
  (let [response (client/put "http://localhost:8080/jots/1")]
    (is (= (:status response) 200))))

(deftest post-users
  (let [response (client/post "http://localhost:8080/users" {:form-params {:foo "bar"} :content-type :json})]
    (is (= (:status response) 201))))

(deftest get-root
  (let [response (client/get "http://localhost:8080/")]
    (is (= (:status response) 200))
    (is (= (:body response) "html"))))

(deftest get-login
  (let [response (client/get "http://localhost:8080/login")]
    (is (= (:status response) 200))
    (is (= (:body response) "html"))))

(deftest get-edit
  (let [response (client/get "http://localhost:8080/edit")]
    (is (= (:status response) 200))
    (is (= (:body response) "html"))))

(deftest get-list
  (let [response (client/get "http://localhost:8080/list")]
    (is (= (:status response) 200))
    (is (= (:body response) "html"))))

(deftest get-jot-id-well-formed-uri
  (is (= "abc123" (get-jot-id "/jots/abc123"))))

(deftest get-jot-id-malformed-uri
  (is (= nil (get-jot-id "/jotsabc123")))
  (is (= nil (get-jot-id "/jot/abc123"))))
