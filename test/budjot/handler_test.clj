(ns budjot.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [budjot.handler :refer :all]))

(deftest get-jots
  (let [response (app (mock/request :get "/jots"))]
    (is (= (:status response) 200))
    (is (= (:body response) "jots"))))

(deftest delete-jots-by-id
  (let [response (app (mock/request :delete "/jots/1"))]
    (is (= (:status response) 202))))

(deftest get-jots-by-id
  (let [response (app (mock/request :get "/jots/1"))]
    (is (= (:status response) 200))))

(deftest post-jots
  (let [response (app (mock/request :post "/jots"))]
    (is (= (:status response) 201))))

(deftest put-jots-by-id
  (let [response (app (mock/request :put "/jots/1"))]
    (is (= (:status response) 200))))

(deftest post-users
  (let [response (app (mock/request :post "/users"))]
    (is (= (:status response) 201))))

(deftest get-root
  (let [response (app (mock/request :get "/"))]
    (is (= (:status response) 200))
    (is (= (:body response) "html"))))

(deftest get-login
  (let [response (app (mock/request :get "/login"))]
    (is (= (:status response) 200))
    (is (= (:body response) "html"))))

(deftest get-edit
  (let [response (app (mock/request :get "/edit"))]
    (is (= (:status response) 200))
    (is (= (:body response) "html"))))

(deftest get-list
  (let [response (app (mock/request :get "/list"))]
    (is (= (:status response) 200))
    (is (= (:body response) "html"))))
