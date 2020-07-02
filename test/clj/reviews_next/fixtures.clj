(ns reviews-next.fixtures
  (:require [mount.core :as mount]))

(defn load-states [f]
  (mount/start)
  (f))
