(ns map-artek-server.ssh-client
  (:use [clj-ssh.ssh]))


(def agent (ssh-agent {}))
(def my-session (session agent "172.16.0.1"
                           {:username "root" :password "T$2z-artek" :port 22 }))
(with-connection my-session
  (ssh my-session {:cmd "display lldp neighbor brief" }))
