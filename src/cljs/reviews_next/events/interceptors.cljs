(ns reviews-next.events.core)

(defn check-and-throw [db-spec db]
  (when-not (s/valid? db-spec db)
    (throw (ex-info (str "spec check failed: " (s/explain-str db-spec db)) {}))))

(def check-spec-interceptor (after (partial check-and-throw :reviews-next.db/db)))
