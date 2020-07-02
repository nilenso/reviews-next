(ns reviews-next.fixtures
  (:require
   [reviews-next.config :refer [config]]
   [mount.core :as mount]))

(defn load-states [f]
  (-> (mount/only #{#'config})
      mount/start)
  (f))
