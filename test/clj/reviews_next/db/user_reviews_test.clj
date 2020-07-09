(ns reviews-next.db.user-reviews-test
  (:require [clojure.test :refer :all]
            [reviews-next.db.user-reviews :as user-reviews]
            [reviews-next.fixtures :as fixtures]))

(use-fixtures :once fixtures/load-states)


(deftest delete-all-test
  (testing "Test delete all from user-reviews table"
    (is (= false (empty? (user-reviews/delete-all (config/connection-uri)))))))

(deftest insert-db
  (testing "Test insert to user-reviews table"
    (let [connection-uri-test (config/connection-uri)]
      (is (not= true (user-reviews/insert {:from_uid "U1"} connection-uri-test)))
      (is (not= true (user-reviews/insert {:from_uid ""} connection-uri-test)))
      (is (not= true (user-reviews/insert {:from_uid "" :to_uid ""} connection-uri-test)))
      (is (not= true (user-reviews/insert {:review_id "" :to_uid "U3"} connection-uri-test))))))
