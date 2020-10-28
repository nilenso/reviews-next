(ns reviews-next.handlers.users
  (:require [ring.util.response :as response]
            [reviews-next.config :as config]
            [reviews-next.database :as db]
            [reviews-next.domain.user :as user]
            [reviews-next.handlers.pages :as pages]
            [next.jdbc :refer [with-transaction]]
            [taoensso.timbre :as log]))

(defn login [{:keys [params path-for] :as _request}]
  (with-transaction [tx @db/pool]
    (let [id-token (:idToken params)
          user (user/register-user tx {::user/id-token id-token})]
      (log/info (path-for #'pages/home))
      (if-not (nil? user)
        (-> (response/redirect (path-for pages/home))
            (response/set-cookie "id-token" id-token
                                 {:max-age (-> config/config :authorization :valid-for)}))
        (response/redirect (path-for pages/oops))))))
