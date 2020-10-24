(ns reviews-next.user
  (:require [clojure.tools.namespace.repl :refer (refresh)]
            [reviews-next.core :as core]))

(def start core/start)
(def stop core/stop)

(defn go []
  (start)
  :ready)

(defn reset []
  (stop)
  (refresh :after (go)))

(go)
