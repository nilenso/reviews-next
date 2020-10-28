(ns reviews-next.middleware
  (:require [bidi.bidi :as b]
            [next.jdbc :as jdbc]
            [reviews-next.database :as db]
            [reviews-next.domain.user :as user]
            [ring.util.response :as response]))

(defn- redirect-to-login [_request]
  (-> (response/redirect "/")
      (response/set-cookie "id-token" nil {:max-age 1})))

(defn wrap-authorization [handler]
  (fn [{:keys [cookies] :as request}]
    (if-let [{google-id ::google-id} (user/id-token->user-info (:id-token cookies))]
      (if-let [user (jdbc/with-transaction [tx @db/pool]
                      (user/find-by-goodle-id tx google-id))]
        (handler (assoc request :current-user user))
        (redirect-to-login request))
      (redirect-to-login request))))

(defn wrap-path-for-fn [handler routes]
  (fn [request]
    (handler (merge request
                    {:path-for (partial b/path-for routes)}))))
