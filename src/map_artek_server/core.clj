(ns map-artek-server.core
  (:use [compojure.route :only [files not-found resources]]
      [compojure.handler :only [site]] ; form, query params decode; cookie; session, etc
      [compojure.core :only [defroutes GET POST DELETE ANY context]]
	  [org.httpkit.timer :only [schedule-task with-timeout]]
	  [clojure.data.json :only [read-json json-str]]
	  [clojure.core.async :only [go timeout <!]]
      org.httpkit.server
	  map-artek-server.ping
	  map-artek-server.views)
  (:gen-class))

(def channel-hub (atom {}))
(def nodes (read-json (slurp "nodes.json")))

(defn handler [request]
  (with-channel request channel
    (swap! channel-hub assoc channel request)
    (on-close channel (fn [status]
         (swap! channel-hub dissoc channel)))))
	
 
(defroutes all-routes
  (GET "/" [] #'index)
  (GET "/ws" [] #'handler)
  (resources "/")
  (not-found "<p>Page not found.</p>")
  )



(defn -main
  [& args]
  (do
	(go (while true
      (<! (timeout 5000))
        (def pinged-nodes (map conj nodes (pmap ping (map :ip nodes))))
			(doseq [channel (keys @channel-hub)]
				(send! channel (json-str pinged-nodes)))
      ))
	(run-server (site #'all-routes) {:port 80})))
