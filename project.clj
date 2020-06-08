(defproject reviews-next "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}

  :min-lein-version "2.9.1"

  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.764"]
                 [aero "1.1.6"]
                 [bidi "2.1.6"]
                 [cider/piggieback "0.5.0"]
                 [cheshire "5.10.0"]
                 [hiccup "1.0.5"]
                 [hikari-cp "2.12.0"]
                 [honeysql "0.9.10"]
                 [mount "0.1.16"]
                 [nrepl "0.7.0"]
                 [org.xerial/sqlite-jdbc "3.23.1"]
                 [reagent "1.0.0-alpha2"]
                 [reagent-utils "0.3.3"]
                 [re-frame "1.0.0-rc2"]
                 [ragtime "0.8.0"]
                 [ring "1.8.1"]
                 [ring-logger "1.0.1"]
                 [ring/ring-json "0.5.0"]
                 [seancorfield/next.jdbc "1.0.409"]]

  :plugins [[lein-cljfmt "0.6.7"]
            [lein-figwheel "0.5.20"]
            [lein-cljsbuild "1.1.7" :exclusions [[org.clojure/clojure]]]]

  :profiles {:dev {:source-paths ["dev"]
                   :dependencies  [[org.clojure/tools.namespace "0.2.3"]
                                   [org.clojure/java.classpath "0.2.0"]
                                   [binaryage/devtools "1.0.0"]
                                   [figwheel-sidecar "0.5.20"]]}

             :test {:dependencies [[ring/ring-mock "0.4.0"]]}}

  :source-paths ["src/clj" "src/cljc" "src/cljs"]

  :main reviews-next.core

  :aliases {"migrate"  ["run" "-m" "reviews-next.db.migrations/migrate"]
            "rollback" ["run" "-m" "reviews-next.db.migrations/rollback"]}

  :clean-targets ^{:protect false}
  [:target-path
   [:cljsbuild :builds :app :compiler :output-dir]
   [:cljsbuild :builds :app :compiler :output-to]]

  :resource-paths ["resources" "target/cljsbuild"]

  :minify-assets
  [[:css {:source "resources/public/css/site.css"
          :target "resources/public/css/site.min.css"}]]

  :cljsbuild
  {:builds {:min
            {:source-paths ["src/cljs" "src/cljc"]
             :compiler
             {:output-to "target/cljsbuild/public/js/app.js"
              :output-dir "target/cljsbuild/public/js"
              :source-map "target/cljsbuild/public/js/app.js.map"
              :optimizations :advanced
              :infer-externs true
              :pretty-print  false}}
            :app
            {:source-paths ["src/cljs" "src/cljc" "dev"]
             :figwheel { ;; :on-jsload ""
                        :open-urls ["http://local.reviews-next:3449/"]}
             :compiler
             {:main "reviews-next.user"
              :asset-path "/js/out"
              :output-to "target/cljsbuild/public/js/app.js"
              :output-dir "target/cljsbuild/public/js/out"
              :source-map true
              :optimizations :none
              :pretty-print  true}}

            :test
            {:source-paths ["src/cljs" "src/cljc" "spec/cljs"]
             :compiler {:output-to "target/test.js"
                        :optimizations :whitespace
                        :pretty-print true}}

            }
   :test-commands {"unit" ["phantomjs" "runners/speclj" "target/test.js"]}}

  :figwheel
  {:http-server-root "public"
   :server-port 3449
   :nrepl-port 7002
   :nrepl-middleware [cider.piggieback/wrap-cljs-repl
                      cider.nrepl/cider-middleware
                      refactor-nrepl.middleware/wrap-refactor]
   :css-dirs ["resources/public/css"]
   :ring-handler reviews-next.server/handler}

  ; :ring {:handler reviews-next.core/app}
  :repl-options {:init-ns reviews-next.user})
