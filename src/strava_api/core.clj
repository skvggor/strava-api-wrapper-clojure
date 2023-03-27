(ns strava-api.core
  (:require [strava-api.get-env :refer [get-strava-env]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [org.httpkit.server :as server])
  (:gen-class))

(defroutes app
  (GET "/" [] "<h1>Hello, World!</h1>")
  (route/not-found "<h1>Page not found</h1>"))

(defn -main []
  (let [port (get (get-strava-env) :port)]
    (server/run-server app {:port port})
    (println (str "Server running on port " port))))
