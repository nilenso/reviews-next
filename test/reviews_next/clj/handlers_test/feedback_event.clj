(ns reviews-next.clj.handlers-test.feedback_event
  (:require [clojure.test :refer :all]
            [cheshire.core :as cheshire]
            [ring.mock.request :as mock]
            [ring.middleware.json :refer [wrap-json-response]]
            [reviews-next.handlers.feedback-event :as feedback-event]))

(defn parse-body [body]
  (cheshire/parse-string body true))

(deftest get-reviews-given-to-user
  (testing "Test GET request to /api/review-for-user returns expected response"
    (let [api-request (-> (mock/request :get "/api/review-for-user")
                          (mock/content-type "application/json"))
          handler (wrap-json-response feedback-event/get-reviewer-and-review-event)
          response (handler api-request)]
      (is (= (:status response) 200)))))


(deftest get-feedback-from-id
  (testing "Test GET request to /api/feedback-from-id returns expected response"
    (let [api-request (-> (mock/request :get "/api/feedback-from-id")
                          (mock/content-type "application/json"))
          handler (wrap-json-response feedback-event/feeback-details-from-id)
          response (handler api-request)]
      (is (= (:status response) 200)))))
