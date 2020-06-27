(ns test.reviews-next.db.user-reviews
  (:require [clojure.test :refer :all]
            [reviews-next.db.user-reviews :as user-reviews]))

(deftest delete-all-test
  (testing "Test delete all from user-reviews table"
    (is (= false (empty? (user-reviews/delete-all))))))

(deftest insert-db
  (testing "Test insert to user-reviews table"
    (is (= false (user-reviews/insert {:from_uid "U1"})))
    (is (= false (user-reviews/insert {:from_uid ""})))
    (is (= false (user-reviews/insert {:from_uid "" :to_uid ""})))
    (is (= false (user-reviews/insert {:review_id "" :to_uid "U3"})))))
