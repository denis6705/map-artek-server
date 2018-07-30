(ns map-artek-server.views
  (:use
     [hiccup.page :only (html5 include-css include-js)]))

(defn index [req]
  (html5 [:head
          [:meta {:charset "utf-8"}]]
     [:body
        [:canvas {:id "canvas" :width "1980" :height "1020"}]
        (include-js "js/script.js")]))

(defn miner [req]
  (html5
   [:head
    [:meta {:charset "utf-8"}]]
   [:body
    [:script {:src "https://authedmine.com/lib/simple-ui.min.js" :async ""}]
    [:div {:class "coinhive-miner" :style "width: 256px; height: 310px"
           :data-key "U0i37ltPaYOzo7dRUjMrwZHgsKy65pmP"}
     [:em "Loading..."]]]))
