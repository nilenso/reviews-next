(ns reviews-next.models.user
  (:require [clojure.spec.alpha :as s]))

(def email-regex #"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,63}$")

(s/def ::id int?)
(s/def ::name (s/and string? #(> (count %) 0)))

(s/def ::email-type (s/and string? #(re-matches email-regex %)))
(s/def ::email ::email-type)
