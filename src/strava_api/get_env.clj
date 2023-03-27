(ns strava-api.get-env
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(defn read-env-file [filename]
  (with-open [rdr (clojure.java.io/reader filename)]
    (->> rdr
         (line-seq)
         (map #(clojure.string/split % #"="))
         (map #(assoc {} (keyword (first %)) (second %)))
         (reduce merge))))

(def env-vars (read-env-file "resources/.env"))

(defn get-strava-env []
  {:strava-domain (get env-vars :STRAVA_DOMAIN)
   :client-id (get env-vars :STRAVA_CLIENT_ID)
   :client-secret (get env-vars :STRAVA_CLIENT_SECRET)
   :refresh-token (get env-vars :STRAVA_REFRESH_TOKEN)
   :user-id (get env-vars :STRAVA_USER_ID)
   :port (Integer. (get env-vars :PORT))})
