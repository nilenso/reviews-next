(ns reviews-next.events.user-test
  (:require [reviews-next.events.user :as user-events]
            [re-frame.core :as re-frame]
            [day8.re-frame.test :as rf-test]
            [cljs.test :as t :include-macros true]))

(deftest setup-google-signin
  (testing "Create function for google signin"
    (rf-test/run-test-sync
     (re-frame/dispatch ::user-events/setup-google-signin-functions)
     (is (not (undefined? (.-onSignIn js/window)))))))
