(ns budjot.main-test
  (:require [clojure.test :refer :all]
            [clojure.data.json :as json]
            [clj-http.client :as client]
            [budjot.main :refer [start-budjot]]))

(defn fixture [f]
  (let [server (start-budjot false 8080)]
    (f)
    (.stop server)))

(use-fixtures :once fixture)

(deftest get-jots
  (let [response (client/get "http://localhost:8080/jots")]
    (is (= (:status response) 200))
    (is (= (:body response) "jots"))))

(deftest delete-jots-by-id
  (let [response (client/delete "http://localhost:8080/jots/1")]
    (is (= (:status response) 202))))

(deftest get-jots-by-id
  (let [response (client/get "http://localhost:8080/jots/1")]
    (is (= (:status response) 200))))

(deftest post-jots
  (let [response
        (client/post
         "http://localhost:8080/jots"
         {:form-params {:name "mm/yy"
                        :income 1000
                        :userid "abc123"
                        :entries [{:name "shopping" :amount "25":paid false}
                                  {:name "fuel" :amount "70" :paid true}]}
          :content-type :json})]
    (is (= 201 (:status response)))
    (is (= {:name "mm/yy"
            :income 1000
            :userid "abc123"
            :entries [{:name "shopping" :amount "25" :paid false}
                      {:name "fuel" :amount "70" :paid true}]}
           (json/read-str (:body response) :key-fn keyword)))))

(deftest post-bad-url
  (is (thrown-with-msg? Exception #"status 400" (client/post "http://localhost:8080"))))

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
