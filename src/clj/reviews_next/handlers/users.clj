(ns reviews-next.handlers.users
  (:require [ring.util.response :as response]))

(defn login [request]
  (response/response "hello world")
  {:status 200})
