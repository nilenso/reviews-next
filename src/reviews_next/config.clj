(ns reviews-next.config
  (:require [clojure.java.io :as io]
            [aero.core :refer [read-config]]
            [mount.core :refer [defstate]]))


(defstate config
  :start
  (read-config (io/resource "config/config.edn")
               {:profile (keyword (or (System/getenv "ENV") "dev"))}))

(defn connection-uri []
  (:jdbc-url (:database config)))
