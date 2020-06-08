(ns reviews-next.db.migrations
  (:require [mount.core :as mount]
            [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]
            [reviews-next.config :as config]))

(defn migration-config []
  {:datastore  (jdbc/sql-database
                {:connection-uri (:jdbc-url (:database config/config))})
   :migrations (jdbc/load-resources "migrations")})

(defn migrate []
  (mount/start #'config/config)
  (repl/migrate (migration-config))
  (mount/stop #'config/config))

(defn rollback []
  (mount/start #'config/config)
  (repl/rollback (migration-config))
  (mount/stop #'config/config))
