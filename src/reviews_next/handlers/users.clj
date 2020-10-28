(ns reviews-next.handlers.users
  (:require [ring.util.response :as response]
            [reviews-next.domain.user :as user]
            [taoensso.timbre :as log]))

(defn login [{:keys [params] :as _request}]
  (let [id-token (:idToken params) ]
    (log/info (user/register-user {::user/id-token id-token}))
    (response/redirect "/home")))
