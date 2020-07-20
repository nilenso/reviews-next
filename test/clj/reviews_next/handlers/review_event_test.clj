(ns reviews-next.handlers.review-event-test
  (:require [clojure.test :refer :all]
            [cheshire.core :as cheshire]
            [ring.mock.request :as mock]
            [ring.middleware.json :refer [wrap-json-params wrap-json-response]]
            [reviews-next.fixtures :as fixtures]
            [reviews-next.handlers.review-event :as review-event]))

(use-fixtures :once fixtures/load-states)

(defn parse-body [body]
  (cheshire/parse-string body true))

(deftest api-call-test
  (testing "Test POST request to /api/review-event"
    (let [json-params (cheshire/encode {:title "Title-test"
                                        :review_date "20-09-2017"})
          api-request (-> (mock/request :post "/api/review-event" json-params)
                          (mock/content-type "application/json"))
          handler (wrap-json-params review-event/insert-into-db)
          response (handler api-request)]
      (is (= (:status response) 200))
      (is (= (:body response) "Posted")))))
