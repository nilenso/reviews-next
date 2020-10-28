(ns reviews-next.database
  (:require [camel-snake-kebab.core :as csk]
            [hikari-cp.core :as pool]
            [next.jdbc.date-time :as jdbc-date-time]
            [next.jdbc.result-set :as jdbc-result-set]
            [next.jdbc.sql :as sql]
            [mount.core :refer [defstate]]
            [reviews-next.config :as config])
  (:import (java.time Instant)))

(defn- now [] (Instant/now))

(defn datasource-options []
  (:database config/config))

(defstate pool
  :start (do
           (jdbc-date-time/read-as-instant)
           (delay (pool/make-datasource (datasource-options))))
  :stop (pool/close-datasource @pool))

(def sql-opts {:builder-fn jdbc-result-set/as-kebab-maps
               :column-fn  csk/->snake_case_string
               :table-fn   csk/->snake_case_string
               :return-keys true})

(defn where
  ([tx table-name attributes]
   (where tx table-name attributes {}))
  ([tx table-name attributes options]
   (sql/find-by-keys tx
                     table-name
                     attributes
                     (merge sql-opts options))))

(defn find-by [tx table-name attributes]
  (first (where tx
                table-name
                attributes
                {:limit 1})))

(defn update! [tx table-name attributes updates]
  (sql/update! tx
               table-name
               (merge updates {:updated-at (now)})
               attributes
               sql-opts))

(defn create! [tx table-name attributes]
  (let [now (now)]
    (sql/insert! tx
                 table-name
                 (merge attributes
                        {:created-at now
                         :updated-at now})
                 sql-opts)))

(defn delete! [tx table-name attributes]
  (sql/delete! tx table-name attributes sql-opts))

(defn query [tx sql-vec]
  (sql/query tx sql-vec sql-opts))
