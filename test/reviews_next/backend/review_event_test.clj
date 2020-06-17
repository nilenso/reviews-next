(ns reviews-next.backend.review-event-test
  (:require [clojure.test :refer :all]
            [cheshire.core :as cheshire]
            [ring.mock.request :as mock]
            [ring.middleware.json :refer [wrap-json-params]]
            [reviews-next.db.db_ops :as db-ops]
            [reviews-next.handlers.review-event :as review-event-handlers]))

(defn parse-body [body]
  (cheshire/parse-string body) true)

(deftest delete-db
  (testing "Test delete all from DB"
    (is (= false (empty? (db-ops/delete-all))))))

(deftest insert-db
  (testing "Test insert to DB"
    (is (= false (db-ops/insert {:title "Report"})))
    (is (= false (db-ops/insert {:review_date ""})))
    (is (= false (db-ops/insert {:title "" :review_date ""})))
    (is (= false (db-ops/insert {:title "" :review_date "20-09-2017"})))
    (is (= false (db-ops/insert {:title "Report" :review_date ""})))
    (is (= true (db-ops/insert {:title "Report" :review_date "20-09-2017"})))
    (is (= true (db-ops/insert {:title "Report 2019" :review_date "20-09-2019"})))))

(deftest display-all-db
  (testing "Test display all from DB"
    (do
      (db-ops/delete-all)
      (db-ops/insert {:title "Report" :review_date "20-09-2017"})
      (db-ops/insert {:title "Report 2019" :review_date "20-09-2019"})
      (is (= true (= (vec (db-ops/display-all))
                    [{:review_date "20-09-2017", :title "Report", :id 1}
                     {:review_date "20-09-2019", :title "Report 2019", :id 2}]))))))

(deftest api-call-test
  (testing "Test POST request to /api/review-event returns expected response"
    (let [json-params (cheshire/encode {:title "Title" :review_date "20-09-2017"})
          api-request (-> (mock/request :post "/api/review-event" json-params)
                          (mock/content-type "application/json"))
          handler (wrap-json-params review-event-handlers/index)
          response (handler api-request)
          response-body (parse-body (:body response))]
      (is (= (:status response) 200))
      (is (= response-body true)))))
