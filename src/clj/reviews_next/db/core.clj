(ns reviews-next.db.core
  (:refer-clojure :exclude [update find])
  (:require [hikari-cp.core :as pool]
            [mount.core :refer [defstate]]
            [reviews-next.config :as config]))

(defn datasource-options []
  (:database config/config))

(defstate pool
  :start (delay (pool/make-datasource (datasource-options)))
  :stop (pool/close-datasource @pool))

(defn create [])
(defn update [])
(defn where [])
(defn find [])
