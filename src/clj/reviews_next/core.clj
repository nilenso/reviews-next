(ns reviews-next.core
  (:require
   [reviews-next.config :refer [config]]
   [reviews-next.server :refer [server]]
   [mount.core :as mount]))

(defn start []
  (-> (mount/only #{#'config #'server})
      mount/start))

(defn stop []
  (mount/stop))

(defn -main [& _args]
  (start))
