(ns strava-api.utils
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(defn read-file [filename]
  (with-open [rdr (clojure.java.io/reader filename)]
    (->> rdr
         (line-seq)
         (map #(clojure.string/split % #"="))
         (map #(assoc {} (keyword (first %)) (second %)))
         (reduce merge))))

(def env-vars (read-file "resources/.env"))

(defn get-env-vars []
  {:strava-domain (get env-vars :STRAVA_DOMAIN)
   :api-url (get env-vars :STRAVA_API_URL)
   :client-id (get env-vars :STRAVA_CLIENT_ID)
   :client-secret (get env-vars :STRAVA_CLIENT_SECRET)
   :refresh-token (get env-vars :STRAVA_REFRESH_TOKEN) 
   :code (get env-vars :STRAVA_CODE)
   :user-id (get env-vars :STRAVA_USER_ID)
   :port (Integer. (get env-vars :PORT))})
