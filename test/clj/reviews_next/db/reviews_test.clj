(ns reviews-next.db.reviews-test
  (:require [clojure.test :refer :all]
            [reviews-next.config :as config]
            [reviews-next.db.reviews :as reviews]
            [reviews-next.fixtures :as fixtures]))

(use-fixtures :once fixtures/load-states)

(deftest delete-all-test
  (testing "Test delete all from reviews table"
    (is (= false (empty? (reviews/delete-all (config/connection-uri)))))))

(deftest insert-db
  (testing "Test insert to DB"
    (is (= false (reviews/insert {:title "Report"} (config/connection-uri))))
    (is (= false (reviews/insert {:review_date ""} (config/connection-uri))))
    (is (= false (reviews/insert {:title "" :review_date ""} (config/connection-uri))))
    (is (= false (reviews/insert {:title "" :review_date "20-09-2017"} (config/connection-uri))))
    (is (= false (reviews/insert {:title "Report" :review_date ""} (config/connection-uri))))
    (is (= false (reviews/insert {:title "Report" :review_description "hey there"} (config/connection-uri))))
    (is (= false (reviews/insert {:review_date "" :review_description "hey there"} (config/connection-uri))))
    (is (= false (reviews/insert {:title "" :review_date "20-09-2017" :review_description "hey there"} (config/connection-uri))))
    (is (= false (reviews/insert {:title "Report" :review_date "20-09-2017"} (config/connection-uri))))
    (is (= false (reviews/insert {:title "Report 2019" :review_date "20-09-2019" :review_description ""} (config/connection-uri))))
    (is (= true (reviews/insert {:title "Report 2019" :review_date "20-09-2019" :review_description "Report DESC"} (config/connection-uri))))
    (is (= true (reviews/insert {:title "Report" :review_date "20-09-2019" :review_description "Report"} (config/connection-uri))))))

(deftest get-list-test
  (testing "Test display all from DB"
    (do
      (reviews/delete-all (config/connection-uri))
      (reviews/insert {:title "Report" :review_date "20-09-2017" :review_description "Report DESC"} (config/connection-uri))
      (reviews/insert {:title "Report 2019" :review_date "20-09-2019" :review_description "Report"} (config/connection-uri))
      (is (= true (= (vec (reviews/get-list (config/connection-uri)))
                    [{:review_description "Report DESC", :review_date "20-09-2017", :title "Report", :id 1}
                     {:review_description "Report", :review_date "20-09-2019", :title "Report 2019", :id 2}]))))))

(deftest insert-and-get-last-id-test
  (testing "Testing insert and get last id from reviews"
    (do
      (reviews/delete-all (config/connection-uri))
      (= 1 (reviews/insert {:title "Report 2020" :review_date "20-09-2020"} (config/connection-uri)))
      (= 2 (reviews/insert {:title "Report 2019" :review_date "01-09-2019"} (config/connection-uri))))))
