(ns map-artek-server.send-email
  (:require [postal.core :as postal]
            [postal.sendmail :as local]
            [postal.smtp :as smtp]))


(postal/send-message {:host "smtp.gmail.com"
               :user "denis6705"
               :pass "alabamaobama123"
               :ssl true}

              {:from "denis6705@gmail.com"
               :to "MIvanov@artek.org"
               :subject "Hi!"
               :body "Test."})
