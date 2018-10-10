(ns map-artek-server.views
  (:use
     [hiccup.page :only (html5 include-css include-js)]))

(def current-server-ip (str (.getHostAddress (java.net.InetAddress/getLocalHost))))

(defn index [req]
  (html5 [:head
          [:meta {:charset "utf-8"}]]
     [:body
        [:div {:id "server-ip" :class current-server-ip}]
        [:canvas {:id "canvas" :width "1980" :height "1020"}]
        (include-js "js/script.js")]))


(defn node-stats [{:keys [params] :as req}]
  (html5
     [:head
          [:title (:node-name params)]
          [:meta {:charset "utf-8"}]
          (include-css "js/c3.min.css")]
     [:body
        [:div {:id "server-ip" :class current-server-ip}]
        [:div {:id "name" :class (:node-name params)}]
        [:div {:id "chart"}]
        (include-js "js/d3.min.js")
        (include-js "js/c3.min.js")
        (include-js "js/show-pings.js")]))

