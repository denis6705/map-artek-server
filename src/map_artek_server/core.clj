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
            [clj-time.core :as time])
  (:gen-class))

(def channel-hub (atom {}))
(def nodes (read-json (slurp "nodes.json")))
(def my-pool (mk-pool))

(defn process_messages []
  (every 1500 #(let [pinged-nodes (map conj nodes (doall (pmap ping (map :ip nodes))))]
                (doseq [channel (keys @channel-hub)]
                    (send! channel (json-str pinged-nodes)))) my-pool))


(defn handler [request]
  (with-channel request channel
    (swap! channel-hub assoc channel request)
    (on-close channel (fn [status]
                       (swap! channel-hub dissoc channel)))))


(defroutes all-routes
  (GET "/" [] #'index)
  (GET "/ws" [] #'handler)
  (resources "/")
  (not-found "<p>Page not found.</p>"))
  ;;(not-found #'index)



(defn -main
  [& args]
  (do
   (process_messages)
   (run-server (site #'all-routes) {:port 80})
   (println "Server started on 127.0.0.1:80")))
