(ns reviews-next.clj.db-test.reviews
  (:require [clojure.test :refer :all]
            [reviews-next.db.reviews :as reviews]))

(def connection-uri-test reviews/connection-uri-test)

(deftest delete-all-test
  (testing "Test delete all from reviews table"
    (is (= false (empty? (reviews/delete-all connection-uri-test))))))

(deftest insert-test
  (testing "Test insert to reviews table"
    (is (= false (reviews/insert {:title "Report"} connection-uri-test)))
    (is (= false (reviews/insert {:review_date ""} connection-uri-test)))
    (is (= false (reviews/insert {:title "" :review_date ""} connection-uri-test)))
    (is (= false (reviews/insert {:title "" :review_date "20-09-2017"} connection-uri-test)))
    (is (= false (reviews/insert {:title "Report" :review_date ""} connection-uri-test)))
    (is (= true (reviews/insert {:title "Report" :review_date "20-09-2017"} connection-uri-test)))
    (is (= true (reviews/insert {:title "Report 2019" :review_date "20-09-2019"} connection-uri-test)))))

(deftest get-list-test
  (testing "Test display all from reviews table"
    (do
      (reviews/delete-all connection-uri-test)
      (reviews/insert {:title "Report" :review_date "20-09-2017"} connection-uri-test)
      (reviews/insert {:title "Report 2019" :review_date "20-09-2019"} connection-uri-test)
      (is (= true (= (vec (reviews/get-list connection-uri-test))
                    [{:review_date "20-09-2017", :title "Report", :id 1}
                     {:review_date "20-09-2019", :title "Report 2019", :id 2}]))))))

(deftest insert-and-get-last-id-test
  (testing "Testing insert and get last id from reviews"
    (do
      (reviews/delete-all connection-uri-test)
      (= 1 (reviews/insert {:title "Report 2020" :review_date "20-09-2020"} connection-uri-test))
      (= 2 (reviews/insert {:title "Report 2019" :review_date "01-09-2019"} connection-uri-test)))))
