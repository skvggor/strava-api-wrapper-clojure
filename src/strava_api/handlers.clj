(ns strava-api.handlers
  (:require [compojure.core :refer [GET]]
            [clojure.data.json :as json])
  (:gen-class))

(defn get-handler [route body-fn]
  (GET route []
    (let [body (body-fn)]
      {:status 200
       :body (json/write-str body)
       :headers {"Content-Type" "application/json"}})))
