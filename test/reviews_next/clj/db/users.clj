(ns test.reviews-next.db.users
  (:require [clojure.test :refer :all]
            [reviews-next.db.users :as users]))

(deftest delete-all-test
  (testing "Test delete all from users table"
    (is (= false (empty? (users/delete-all))))))

(deftest insert-test
  (testing "Test insert to users table"
    (is (= false (users/insert {:name "PQR"})))
    (is (= false (users/insert {:email "pqr@gmail.com"})))
    (is (= false (users/insert {:name "" :email ""})))
    (is (= false (users/insert {:name "" :email "xyq@gmail.com"})))
    (is (= false (users/insert {:name "PQR" :email ""})))
    (is (not= false (users/insert {:name "PQR" :email "pqr@gmail.com" :id "U7"})))
    (is (not= false (users/insert {:name "ABC" :email "abc@gmail.com" :id "U8"})))))
