(ns reviews-next.events-test
  (:require [cljs.core :as core]
            [cljs.test :as test :refer-macros [deftest is testing]]
            [day8.re-frame.test :as rf-test]
            [re-frame.core :as rf]
            [reviews-next.db]
            [reviews-next.events]
            [reviews-next.pages.review-event]
            [reviews-next.subs]))

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
