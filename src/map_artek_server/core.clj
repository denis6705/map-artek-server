(ns map-artek-server.core
  (:require [compojure.route :refer [files not-found resources]]
            [compojure.handler :refer [site]] ; form, query params decode; cookie; session, etc
            [compojure.core :refer [defroutes GET POST DELETE ANY context]]
            [org.httpkit.timer :refer [schedule-task with-timeout]]
            [clojure.data.json :refer [read-json json-str]]
            [clojure.core.async :refer [go timeout <!]]
            [overtone.at-at :refer [every mk-pool]]
            [org.httpkit.server :refer :all]
            [map-artek-server.ping :refer [ping]]
            [map-artek-server.views :refer :all]
            [clj-memory-meter.core :as mm]
            [clj-time.core :as time]
            [clj-time.local :as l])
  (:use [clojure.tools.namespace.repl :only (refresh)])
  (:gen-class))


(def channel-hub (atom {}))
(def nodes (read-json (slurp "nodes.json")))
(def my-pool (mk-pool))
(defonce server (atom nil))
(def ping-history (atom []))
(defn stop-server []
  (when-not (nil? @server)
    (@server :timeout 100)
    (reset! server nil)))

(defn process_messages []
  (every 1500 #(let [pinged-nodes (map conj nodes (doall (pmap ping (map :ip nodes))))]
                (doseq [channel (keys @channel-hub)]
                  ;(swap! ping-history conj {:nodes pinged-nodes :time (l/local-now)})
                  (send! channel (json-str pinged-nodes)))) my-pool))


(defn handler [request]
  (with-channel request channel
    (swap! channel-hub assoc channel request)
    (on-close channel (fn [status]
                       (swap! channel-hub dissoc channel)))))

(defroutes all-routes
  (GET "/" [] #'index)
  (GET "/nodes/:node-name" [node-name] #'node-stats)
  (GET "/ws" [] #'handler)
  (resources "/")
  (not-found "<p>Page not found.</p>"))




(defn -main
  [& args]
   (process_messages)
   (reset! server (run-server (site #'all-routes) {:port 80}))
   (println "Server started on 127.0.0.1:80"))
