(ns reviews-next.user
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.pprint :refer (pprint)]
            [clojure.repl :refer :all]
            [clojure.test :as test]
            [clojure.tools.namespace.repl :refer (refresh refresh-all)]
            [reviews-next.core :as core]))

(def start core/start)
(def stop core/stop)

(defn go []
  (start)
  :ready)

(defn reset []
  (core/stop)
  (refresh :after (go)))

(go)
