(ns strava-api.core
  (:require [strava-api.utils :refer [get-env-vars]]
            [strava-api.strava :refer [fetch-total-distance-in-year]]
            [strava-api.handlers :refer [get-handler]]
            [compojure.core :refer [defroutes]]
            [compojure.route :as route]
            [org.httpkit.server :as server])
  (:gen-class))

(def total-distance-in-year (fetch-total-distance-in-year))

(defroutes app
  (get-handler "/api/v1/hi"
               {:message "Hello World!"})

  (get-handler "/api/v1/strava/total-distance/current-year"
               {:distance total-distance-in-year})

  (route/not-found
   {:status 404
    :body "Not Found"}))

(defn -main []
  (let [port (get (get-env-vars) :port)]
    (server/run-server app {:port port})
    (println (str "Server running on port " port))))
