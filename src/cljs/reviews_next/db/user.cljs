(ns reviews-next.db.user
  (:require [cljs.spec.alpha :as s]))

(s/def ::id int?)
(s/def ::name string?)
(s/def ::email string? )
(s/def ::user (s/keys :req-un [::id ::name ::email]))
