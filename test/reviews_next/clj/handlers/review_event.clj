(ns test.reviews-next.handlers.review-event
  (:require [clojure.test :refer :all]
            [cheshire.core :as cheshire]
            [ring.mock.request :as mock]
            [ring.middleware.json :refer [wrap-json-params wrap-json-response]]
            [reviews-next.handlers.review-event :as review-event]))

(defn parse-body [body]
  (cheshire/parse-string body true))
;
(deftest api-call-test
  (testing "Test POST request to /api/review-event returns expected response"
    (let [json-params (cheshire/encode {:title "Title"
                                        :review_date "20-09-2017"
                                        :from_uid "U1"
                                        :participants ["U2" "U3"]})
          api-request (-> (mock/request :post "/api/review-event" json-params)
                          (mock/content-type "application/json"))
          handler (wrap-json-params review-event-handler/insert-into-db)
          response (handler api-request)]
      (is (= (:status response) 200))
      (is (= (:body response) "Posted")))))

(deftest get-users-api-call-test
  (testing "Test GET request to /api/users returns expected response"
    (let [api-request (-> (mock/request :get "/api/users")
                          (mock/content-type "application/json"))
          handler (wrap-json-response review-event-handler/users-list)
          response (handler api-request)
          response-body (parse-body (:body response))]
      (is (= (:status response) 200)))))
