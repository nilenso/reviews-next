(ns reviews-next.clj.db-test.users
  (:require [clojure.test :refer :all]
            [reviews-next.db.users :as users]))

(def connection-uri-test users/connection-uri-test)

(deftest delete-all-test
  (testing "Test delete all from users table"
    (is (= false (empty? (users/delete-all connection-uri-test))))))

(deftest insert-test
  (testing "Test insert to users table"
    (is (= false (users/insert {:name "PQR"} connection-uri-test)))
    (is (= false (users/insert {:email "pqr@gmail.com"} connection-uri-test)))
    (is (= false (users/insert {:name "" :email ""} connection-uri-test)))
    (is (= false (users/insert {:name "" :email "xyq@gmail.com"} connection-uri-test)))
    (is (= false (users/insert {:name "PQR" :email ""} connection-uri-test)))
    (is (not= false (users/insert {:name "PQR" :email "pqr@gmail.com" :id "U7"} connection-uri-test)))
    (is (not= false (users/insert {:name "ABC" :email "abc@gmail.com" :id "U8"} connection-uri-test)))))
