(ns reviews-next.db.migrations
  (:require [mount.core :as mount]
            [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]
            [ragtime.strategy :as strategy]
            [reviews-next.config :as config]))

(defn migration-config []
  {:datastore  (jdbc/sql-database {:connection-uri (config/connection-uri)})
   :migrations (jdbc/load-resources "migrations")
   :strategy ragtime.strategy/apply-new})

(defn migrate
  ([]
   (mount/start #'config/config)
   (repl/migrate (migration-config))
   (mount/stop #'config/config))
  ([test]
   (mount/start #'config/config)
   (repl/migrate (migration-config test))
   (mount/stop #'config/config)))

(defn rollback []
  (mount/start #'config/config)
  (repl/rollback (migration-config))
  (mount/stop #'config/config))
