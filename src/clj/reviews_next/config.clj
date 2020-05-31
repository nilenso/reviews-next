(ns reviews-next.config
  (:require [clojure.java.io :as io]
            [aero.core :refer [read-config]]
            [mount.core :refer [defstate]]))

(defstate config
  :start (read-config (io/resource "config.edn")))
