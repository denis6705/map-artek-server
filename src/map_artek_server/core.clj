(ns map-artek-server.core
  (:require [compojure.route :refer [files not-found resources]]
            [compojure.handler :refer [site]]
            [compojure.core :refer [defroutes GET POST DELETE ANY context]]
            [org.httpkit.timer :refer [schedule-task with-timeout]]
            [clojure.data.json :refer [read-json json-str]]
            [overtone.at-at :refer [every mk-pool]]
            [org.httpkit.server :refer :all]
            [map-artek-server.ping :refer [ping]]
            [map-artek-server.views :refer :all]
            [map-artek-server.database :refer [push-pings-db get-pings-for-node-between]]
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

(defn process_messages
  "Отправляет данные о пингах клиентам каждые n миллисекунд"
  [n]
  (every n #(let [pinged-nodes (map conj nodes (doall (pmap ping (map :ip nodes))))]
                  ;(swap! ping-history conj {:nodes pinged-nodes :time (l/local-now)})
              (doseq [channel (keys @channel-hub)]
                  (send! channel (json-str pinged-nodes)))) my-pool))

(defn write-to-base
  "Записывает в базу пинги каждые n миллисекунд"
  [n]
  (every n #(let [pinged-nodes (map conj nodes (doall (pmap ping (map :ip nodes))))]
                (let [nds (mapv (fn [node]
                                 (select-keys node [:ping :name])) pinged-nodes)]
                  (push-pings-db (map conj nds (repeat (count nds) {:time (l/local-now)}))))) my-pool))

(defn ws-handler [request]
  (with-channel request channel
    (swap! channel-hub assoc channel request)
    (on-close channel (fn [status]
                       (swap! channel-hub dissoc channel)))))



(defn db-handler [request]
  (with-channel request channel
    (on-receive channel (fn [message]
                          (let [j-message (read-json message)]
                            (case (:command j-message)
                              "hello" (println (:text j-message))
                              "get-pings-for-node" (send! channel (json-str (get-pings-for-node-between
                                                                              (:node-name j-message)
                                                                              (:time1 j-message)
                                                                              (:time2 j-message))))
                              ))))))

(defroutes all-routes
  (GET "/db" [] #'db-handler)
  (GET "/ws" [] #'ws-handler)
  (GET "/" [] #'index)
  (GET "/nodes/:node-name" [node-name] #'node-stats)
  (GET "/:node-name" [node-name] #'node-from-db)

  (resources "/")
  (not-found "<p>Page not found.</p>"))



(defn -main
  [& args]
   (process_messages 2000)
   (write-to-base 10000)
   (reset! server (run-server (site #'all-routes) {:port 80}))
   (println "Server started on 127.0.0.1:80"))

