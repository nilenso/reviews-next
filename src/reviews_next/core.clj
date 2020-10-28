(ns reviews-next.core
  (:require [reviews-next.config :refer [config]]
            [reviews-next.server :refer [server]]
            [reviews-next.domain.user :refer [google-token-validator]]
            [reviews-next.database :as db]
            [mount.core :as mount]))

(defn start []
  (-> (mount/only
        #{#'config
          #'server
          #'db/pool
          #'google-token-validator})
      mount/start))

(defn stop []
  (mount/stop))

(defn -main [& _args]
  (start))
