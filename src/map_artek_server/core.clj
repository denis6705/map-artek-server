(ns map-artek-server.core
  (:use [compojure.route :only [files not-found resources]]
      [compojure.handler :only [site]] ; form, query params decode; cookie; session, etc
      [compojure.core :only [defroutes GET POST DELETE ANY context]]
    [org.httpkit.timer :only [schedule-task with-timeout]]
    [clojure.data.json :only [read-json json-str]]
    [clojure.core.async :only [go timeout <!]]
    overtone.at-at
    org.httpkit.server
    map-artek-server.ping
    map-artek-server.views)
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
  (GET "/miner" [] #'miner)
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
