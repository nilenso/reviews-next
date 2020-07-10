(ns reviews-next.db.user-feedback-test
  (:require [clojure.test :refer :all]
            [reviews-next.config :as config]
            [reviews-next.db.user-feedback :as user-feedback]
            [reviews-next.fixtures :as fixtures]))

(use-fixtures :once fixtures/load-states)

(deftest delete-all-test
  (testing "Test delete all from user feedback table"
    (is (= false (empty? (user-feedback/delete-all (config/connection-uri)))))))

(deftest insert-test
  (testing "Test insert to user feedback table"
    (let [conn (config/connection-uri)]
      (is (not= true (user-feedback/insert {:from_uid "U7"} conn)))
      (is (not= true (user-feedback/insert {:to_uid ""} conn)))
      (is (= true (user-feedback/insert {:from_uid "U7"
                                          :to_uid "U8"
                                          :review_id 2
                                          :feedback "Great Job"
                                          :level 6.1
                                          :is_draft 1 } conn))))))
