(ns reviews-next.db.users
  (:require
          [clojure.pprint :as pp]
          [clojure.java.jdbc :refer :all]
          [reviews-next.config :as config]))

(def connection-uri-default (config/connection-uri))
(def connection-uri-test (config/connection-uri "test"))

(def data
  {:id "U3"
   :name "shreyanshi"
   :email "mno@gmail.com"})

(defn insert
  "execute query and return lazy sequence"
  ([data] (insert data connection-uri-default))
  ([data connection-uri]
   (try
        do
          (insert! connection-uri :users data)
          true
     (catch Exception e
       false))))

(defn delete-all
  ([] (delete-all connection-uri-default))
  ([connection-uri]
   (db-do-commands connection-uri "delete from users")))

(defn get-users
  ([] (get-users connection-uri-default))
  ([connection-uri]
   (try
      (query connection-uri ["select * from users"])
     (catch Exception e
      false))))

(defn users-for-given-ids
  ([user-ids] (users-for-given-ids user-ids connection-uri-default))
  ([user-ids connection-uri]
   (try
      (query connection-uri [(str "select * from users where id in " "(" (clojure.string/join "," (map #(str \" % \") user-ids)) ")")])
     (catch Exception e
       (str e)))))

(defn users-for-given-id
  ([user-id] (users-for-given-id user-id connection-uri-default))
  ([user-id connection-uri]
   (try
     (query connection-uri [ "select * from users where id=?" user-id])
     (catch Exception e
       (str e)))))
