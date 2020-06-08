(ns reviews-next.core
  (:require
   [reviews-next.config :refer [config]]
   [reviews-next.server :refer [server]]
   [reviews-next.db.core :as db]
   [mount.core :as mount]))

(defn start []
  (-> (mount/only #{#'config #'server #'db/pool})
      mount/start))

(defn stop []
  (mount/stop))

(defn -main [& _args]
  (start))
