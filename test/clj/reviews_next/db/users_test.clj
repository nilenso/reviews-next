(ns reviews-next.db.users-test
  (:require [clojure.test :refer :all]
            [reviews-next.db.users :as users]
            [reviews-next.fixtures :as fixtures]))

(use-fixtures :once fixtures/load-states)

(deftest insert-test
  (testing "Test insert to users table")
  (let [connection-uri-test (config/connection-uri)]
    (is (not= true (users/insert {:name "PQR"} connection-uri-test)))
    (is (not= true (users/insert {:email "pqr@gmail.com"} connection-uri-test)))
    (is (not= true (users/insert {:name "" :email ""} connection-uri-test)))
    (is (not= true (users/insert {:name "" :email "xyq@gmail.com"} connection-uri-test)))
    (is (not= true (users/insert {:name "PQR" :email ""} connection-uri-test)))
    (is (= true (users/insert {:name "PQR" :email "pqr@gmail.com" :id "U7"} connection-uri-test)))
    (is (= true (users/insert {:name "ABC" :email "abc@gmail.com" :id "U8"} connection-uri-test)))))
