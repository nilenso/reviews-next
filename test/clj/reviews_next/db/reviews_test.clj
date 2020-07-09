(ns reviews-next.db.reviews-test
  (:require [clojure.test :refer :all]
            [reviews-next.config :as config]
            [reviews-next.db.reviews :as reviews]
            [reviews-next.fixtures :as fixtures]))

(use-fixtures :once fixtures/load-states)

(deftest delete-all-test
  (testing "Test delete all from reviews table"
    (is (= false (empty? (reviews/delete-all (config/connection-uri)))))))

(deftest insert-test
  (testing "Test insert to reviews table"
    (let [conn (config/connection-uri)]
      (is (not= true (reviews/insert {:title "Report"} conn)))
      (is (not= true (reviews/insert {:review_date ""} conn)))
      (is (not= true (reviews/insert {:title "" :review_date ""} conn)))
      (is (not= true (reviews/insert {:title "" :review_date "20-09-2017"} conn)))
      (is (not= true (reviews/insert {:title "Report" :review_date ""} conn)))
      (is (= true (reviews/insert {:title "Report" :review_date "20-09-2017"} conn)))
      (is (= true (reviews/insert {:title "Report 2019" :review_date "20-09-2019"} conn))))))

(deftest get-list-test
  (testing "Test display all from reviews table"
    (let [conn (config/connection-uri)]
      (reviews/delete-all conn)
      (reviews/insert {:title "Report" :review_date "20-09-2017"} conn)
      (reviews/insert {:title "Report 2019" :review_date "20-09-2019"} conn)
      (is (= true (= (vec (reviews/get-list conn))
                    [{:review_date "20-09-2017", :title "Report", :id 1}
                     {:review_date "20-09-2019", :title "Report 2019", :id 2}]))))))

(deftest insert-and-get-last-id-test
  (testing "Testing insert and get last id from reviews"
    (let [conn (config/connection-uri)]
      (reviews/delete-all conn)
      (= 1 (reviews/insert {:title "Report 2020" :review_date "20-09-2020"} conn))
      (= 2 (reviews/insert {:title "Report 2019" :review_date "01-09-2019"} conn)))))
