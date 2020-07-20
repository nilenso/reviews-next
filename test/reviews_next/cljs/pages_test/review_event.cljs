(ns reviews_next.cljs.pages-test.review_event
  (:require [cljs.core :as core]
            [cljs.test :as test :refer-macros [deftest is testing]]
            [day8.re-frame.test :as rf-test]
            [re-frame.core :as rf]
            [reviews-next.db]
            [reviews-next.events]
            [reviews-next.pages.review-event]
            [reviews-next.subs]))

(deftest title-test-valid
  (testing "Valid Title Tests"
    (is (= true (:reviews-next.pages.review/title-valid? "Review")))
    (is (= true (:reviews-next.pages.review/title-valid? "Review 2020")))
    (is (= true (:reviews-next.pages.review/title-valid? "R")))
    (is (= true (:reviews-next.pages.review/title-valid? (repeat 50 "a"))))))

(deftest title-test-invalid
  (testing "Invalid Title Tests"
    (is (= false (:reviews-next.pages.review/title-valid? "")))
    (is (= false (:reviews-next.pages.review/title-valid? (repeat 51 "a"))))
    (is (= false (:reviews-next.pages.review/title-valid? nil)))))

(deftest date-test-invalid
  (testing "Invalid Date Tests"
    (is (= false (:reviews-next.pages.review/date-valid? "")))
    (is (= false (:reviews-next.pages.review/date-valid? nil)))))

(deftest date-test-valid
  (testing "Valid Date Tests"
    (is (= true (:reviews-next.pages.review/date-valid? "2020-06-12")))))
