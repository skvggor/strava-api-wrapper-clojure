(ns strava-api.core
  (:require [strava-api.get-env :refer [get-strava-env]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [org.httpkit.server :as server]
            [clojure.data.json :as json])
  (:gen-class))

(defroutes app
  (GET "/" []
    {:status 200
     :body (json/write-str {:message "Hello World!"})
     :headers {"Content-Type" "application/json"}})

  (route/not-found
    {:status 404
     :body "Not Found"}))

(defn -main []
  (let [port (get (get-strava-env) :port)]
    (server/run-server app {:port port})
    (println (str "Server running on port " port))))
