(ns strava-api.core
  (:require [strava-api.utils :refer [get-env-vars]]
            [strava-api.handlers :refer [get-handler]]
            [compojure.core :refer [defroutes]]
            [compojure.route :as route]
            [org.httpkit.server :as server]
            [clj-http.client :as client]
            [clojure.data.json :as json])
  (:gen-class))

(def get-request-body {:client_id (get (get-env-vars) :client-id)
                   :client_secret (get (get-env-vars) :client-secret)
                   :refresh_token (get (get-env-vars) :refresh-token)
                   :grant_type "refresh_token"})

(def get-access-token (client/post (str (get (get-env-vars) :strava-domain) "/api/v3/oauth/token")
                            {:body (json/write-str get-request-body)
                             :headers {"Content-Type" "application/json"}}))

(defn map-access-token [data]
  (let [body (get data :body)
        json-map (json/read-str body :key-fn keyword)]
    (get json-map :access_token)))

(def access_token (map-access-token get-access-token))

(def get-activities (client/get (str (get (get-env-vars) :strava-domain) "/api/v3/athletes/" (get (get-env-vars) :user-id) "/stats")
                                {:headers {"Authorization" (str "Bearer " access_token)}}))

(defn map-total-distance [data]
  (let [body (get data :body)
        json-map (json/read-str body :key-fn keyword)]
    (get (json-map :ytd_ride_totals) :distance)))

(def total-distance-in-year (float (/ (Integer. (map-total-distance get-activities)) 1000)))

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
