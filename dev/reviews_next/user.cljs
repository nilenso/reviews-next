(ns reviews-next.user
  (:require [reviews-next.core :as core]
            [devtools.core :as devtools]))

(devtools/install!)
(enable-console-print!)
(core/init!)
