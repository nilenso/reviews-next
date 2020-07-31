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
       all-fields-valid? (rf/subscribe [:reviews-next.subs/all-fields-valid?])
       participants (rf/subscribe [:reviews-next.subs/participants])
       selected-participants (rf/subscribe [:reviews-next.subs/selected-participants])]
      (is (= "" @review-event-title))
      (is (= "" @review-date))
      (is (= true @all-fields-valid?))
      (is (= [] @participants))
      (is (= [] @selected-participants))
      (rf/dispatch [:reviews-next.events/title-change "Title"])
      (is (= "Title" @review-event-title))
      (rf/dispatch [:reviews-next.events/title-change "New Title"])
      (is (= "New Title" @review-event-title))
      (rf/dispatch [:reviews-next.events/date-change "2010-08-01"])
      (is (= "2010-08-01" @review-date))
      (rf/dispatch [:reviews-next.events/set-participants ["U1" "U2"]])
      (is (= ["U1" "U2"] @participants))
      (rf/dispatch [:reviews-next.events/add-to-selected-participants "U1"])
      (is (= #{"U1"} @selected-participants))
      (rf/dispatch [:reviews-next.events/add-to-selected-participants "U2"])
      (is (= #{"U1" "U2"} @selected-participants))
      (rf/dispatch [:reviews-next.events/remove-from-selected-participants "U2"])
      (is (= #{"U1"} @selected-participants)))))
