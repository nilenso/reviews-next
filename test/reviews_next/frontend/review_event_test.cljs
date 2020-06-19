(ns reviews-next.frontend.review-event-test
  (:require [cljs.core :as core]
            [cljs.test :as test :refer-macros [deftest is testing]]
            [day8.re-frame.test :as rf-test]
            [re-frame.core :as rf]
            [reviews-next.db]
            [reviews-next.events]
            [reviews-next.pages.review]
            [reviews-next.subs]))
            ; [re-frame.core :as rf]))

(deftest event-handlers-test
  (rf-test/run-test-sync
    (rf/dispatch [:reviews-next.events/initialize-db])
    (let
      [review-event-title (rf/subscribe [:reviews-next.subs/review-event-title])
       review-date (rf/subscribe [:reviews-next.subs/review-date])
       all-fields-valid? (rf/subscribe [:reviews-next.subs/all-fields-valid?])]
      (is (= "" @review-event-title))
      (is (= "" @review-date))
      (is (= true @all-fields-valid?))
      (rf/dispatch [:reviews-next.events/title-change "Title"])
      (is (= "Title" @review-event-title))
      (rf/dispatch [:reviews-next.events/title-change "New Title"])
      (is (= "New Title" @review-event-title))
      (rf/dispatch [:reviews-next.events/date-change "2010-08-01"])
      (is (= "2010-08-01" @review-date)))))

; (deftest title-test-valid
;   (testing "Valid Title Tests"
;     (is (= true (:reviews-next.pages.review/title-valid? "Review")))
;     (is (= true (:reviews-next.pages.review/title-valid? "Review 2020")))
;     (is (= true (:reviews-next.pages.review/title-valid? "R")))
;     (is (= true (:reviews-next.pages.review/title-valid? (repeat 50 "a"))))))
;
; (deftest title-test-invalid
;   (testing "Invalid Title Tests"
;     (is (= false (:reviews-next.pages.review/title-valid? "")))
;     (is (= false (:reviews-next.pages.review/title-valid? (repeat 51 "a"))))
;     (is (= false (:reviews-next.pages.review/title-valid? nil)))))
;
; (deftest date-test-invalid
;   (testing "Invalid Date Tests"
;     (is (= false (:reviews-next.pages.review/date-valid? "")))
;     (is (= false (:reviews-next.pages.review/date-valid? nil)))))
;
; (deftest date-test-valid
;   (testing "Valid Date Tests"
;     (is (= true (:reviews-next.pages.review/ "2020-06-12")))))
