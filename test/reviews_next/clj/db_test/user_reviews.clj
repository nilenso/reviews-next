(ns reviews-next.clj.db-test.user-reviews
  (:require [clojure.test :refer :all]
            [reviews-next.db.user-reviews :as user-reviews]))

(def connection-uri-test user-reviews/connection-uri-test)

(deftest delete-all-test
  (testing "Test delete all from user-reviews table"
    (is (= false (empty? (user-reviews/delete-all connection-uri-test))))))

(deftest insert-db
  (testing "Test insert to user-reviews table"
    (is (= false (user-reviews/insert {:from_uid "U1"} connection-uri-test)))
    (is (= false (user-reviews/insert {:from_uid ""} connection-uri-test)))
    (is (= false (user-reviews/insert {:from_uid "" :to_uid ""} connection-uri-test)))
    (is (= false (user-reviews/insert {:review_id "" :to_uid "U3"} connection-uri-test)))))
