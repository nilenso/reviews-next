(defproject reviews-next "0.1.0-SNAPSHOT"
  :description "An app to record reviews at nilenso"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}

  :min-lein-version "2.9.1"

  :dependencies [[org.clojure/clojure "1.10.1"]
                 [aero "1.1.6"]
                 [bidi "2.1.6"]
                 [cheshire "5.10.0"]
                 [hiccup "1.0.5"]
                 [hikari-cp "2.12.0"]
                 [honeysql "0.9.10"]
                 [mount "0.1.16"]
                 [nrepl "0.7.0"]
                 [org.xerial/sqlite-jdbc "3.23.1"]
                 [ragtime "0.8.0"]
                 [ring "1.8.1"]
                 [ring-cors "0.1.13"]
                 [ring-logger "1.0.1"]
                 [ring/ring-json "0.5.0"]
                 [seancorfield/next.jdbc "1.0.409"]
                 [com.fzakaria/slf4j-timbre "0.3.19"]]

  :plugins [[lein-cljfmt "0.6.7"]
            [clj-kondo "2020.06.21"]]

  :profiles {:dev {:source-paths ["dev"]
                   :dependencies  [[org.clojure/tools.namespace "0.2.3"]
                                   [org.clojure/java.classpath "0.2.0"]]}
             :uberjar {:aot :all}}

  :main reviews-next.core

  :aliases {"migrate"  ["run" "-m" "reviews-next.db.migrations/migrate"]
            "rollback" ["run" "-m" "reviews-next.db.migrations/rollback"]}

  :resource-paths ["resources"]
  :ring {:handler reviews-next.server/hander}
  :repl-options {:init-ns reviews-next.user})
