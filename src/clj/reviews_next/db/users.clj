(ns reviews-next.db.users
  (:require
          [clojure.pprint :as pp]
          [clojure.java.jdbc :refer :all]
          [reviews-next.config :as config]))

(def data
  {:id "U5"
   :name "MNO"
   :email "mno@gmail.com"})

(defn insert
  "execute query and return lazy sequence"
  ([data] (insert data (config/connection-uri)))
  ([data connection-uri]
   (try
        do
          (insert! connection-uri :users data)
          true
     (catch Exception e
       false))))

(defn delete-all
  ([] (delete-all (config/connection-uri)))
  ([connection-uri]
   (db-do-commands connection-uri "delete from users")))

(defn get-users
  ([] (get-users (config/connection-uri)))
  ([connection-uri]
   (try
      (query connection-uri ["select * from users"])
     (catch Exception e
      false))))

(defn get-users-for-given-ids
  ([user-ids] (get-users-for-given-ids (config/connection-uri)))
  ([user-ids connection-uri]
   (try
      (db-do-prepared connection-uri "select * from users where id in (?)" user-ids)
     (catch Exception e
       (str e)))))
