(ns map-artek-server.database
  (:require
     [clojure.java.jdbc :as j]
     [clj-time.core :as t]
     [clj-time.local :as l]
     [clj-time.coerce :as c]
     [clj-time.format :as f]
     [clj-time.jdbc]
     [clojure.pprint :as p :refer [pprint]]))


(def testdata
  {:name "Хрустальный"
   :ping "5.33"
   :time (l/local-now)
   })



(def db
  {:classname "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname "db/database.db"
   })

(defn push-pings-db
  "Запись массива nodes состоящего из node {:ping ping :ip ip :name name} в базу"
  [nodes]
  (j/insert-multi! db :nodes nodes))

(defn get-pings-for-node-between
  [node-name time1 time2]
  (j/query db ["SELECT * FROM nodes WHERE  name =? AND time BETWEEN ? AND ?" node-name time1 time2 ]))

(defn create-db []
  (try (j/db-do-commands db
                       (j/create-table-ddl :nodes
                                         [[:name :text]
                                          [:ping :real]
                                          [:time :text]]
                                          ))
    (catch Exception e (println e))))


