(ns map-artek-server.views
	(:use
		[hiccup.page :only (html5 include-css include-js)]))
	
(defn index [req]
  (html5 [:head
			[:meta {:charset "utf-8"}]]
		 [:body 
			[:canvas {:id "canvas" :width "1900" :height "1020"}]
			(include-js "js/script.js")
		  ]))
 