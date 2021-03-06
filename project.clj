(defproject map-artek-server "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.6.0"]
                 [http-kit "2.2.0"]
                 [hiccup "1.0.5"]
                 [ring/ring-core "1.5.1"]
                 [ring/ring-jetty-adapter "1.5.1"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/core.async "0.4.474"]
                 [overtone/at-at "1.2.0"]
                 [clj-time "0.15.0"]
                 [org.clojure/java.jdbc "0.7.8"]
                 [org.xerial/sqlite-jdbc "3.8.6"]
                 [clj-ssh "0.5.14"]
                 [com.draines/postal "2.0.3"]
                 [lein-gorilla "0.4.0"]
                 [proto-repl "0.3.1"]]

  :main ^:skip-aot map-artek-server.core
  :target-path "target/%s"
  :plugins [[lein-gorilla "0.4.0"]]
  :profiles {:uberjar {:aot :all}})
