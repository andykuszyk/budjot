(ns budjot.post-jots-test
  (:require [clojure.test :refer [deftest is use-fixtures]]
            [budjot.fixtures :as fixtures]
            [clojure.data.json :as json]
            [clj-http.client :as client]))

(use-fixtures :once fixtures/integration-test-fixture)

(deftest post-jots-returns-201-and-body
  (let [response (client/post fixtures/budjot-url (fixtures/build-post-request (fixtures/build-jot "new jot")))]
    (is (= 201 (:status response)))
    (let [actual (json/read-str (:body response) :key-fn keyword)
          expected (fixtures/build-jot "new jot"
                    )]
      (is (= (:name expected) (:name actual)))
      (is (= (:income expected) (:income actual)))
      (is (= (:userid expected) (:userid actual)))
      (is (= (:entries expected) (:entries actual)))
      (is (>= (.length (:createdOn actual)) 0))
      (is (>= (.length (:modifiedOn actual)) 0))
      (is (= 24 (.length (:_id actual)))))))

(deftest post-bad-url-returns-400
  (is (thrown-with-msg? Exception #"status 400" (client/post "http://localhost:8080"))))

(deftest post-jots-with-same-name-returns-409
  (is (= 201 (:status (client/post fixtures/budjot-url (fixtures/build-post-request (fixtures/build-jot "409"))))))
  (is (thrown-with-msg? Exception #"status 409" (client/post fixtures/budjot-url (fixtures/build-post-request (fixtures/build-jot "409"))))))
