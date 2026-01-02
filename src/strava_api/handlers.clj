(ns strava-api.handlers
  (:require [compojure.core :refer [GET]]
            [clojure.data.json :as json])
  (:gen-class))

(defn get-handler [route body-fn]
  (GET route request
    (let [sport-param (get-in request [:params "sport"] "run")
          body (body-fn sport-param)]
      {:status 200
       :body (json/write-str body)
       :headers {"Content-Type" "application/json"}})))
