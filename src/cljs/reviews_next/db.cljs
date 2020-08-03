(ns reviews-next.db
  (:require [cljs.spec.alpha :as s]))

(s/def ::db map?)

(def initial-db
  {:user nil
   :active-panel nil})
