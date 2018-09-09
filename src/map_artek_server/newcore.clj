(ns map-artek-server.core
  (:require [compojure.route :refer [files not-found resources]]
            [compojure.handler :refer [site]] ; form, query params decode; cookie; session, etc
            [compojure.core :refer [defroutes GET POST DELETE ANY context]]
            [org.httpkit.timer :refer [schedule-task with-timeout]]
            [clojure.data.json :refer [read-json json-str]]
            [clojure.core.async :refer [go timeout <!]]
            [overtone.at-at :refer [every]]
            [org.httpkit.server :refer :all]
            [map-artek-server.ping :refer :all]
            [map-artek-server.views :reefr :all]
            [clj-time.core :as time])
 (:require
        [overton.at-at :as ton]
  (:gen-class)))
