(ns strava-api.core
  (:require [strava-api.utils :refer [get-env-vars]]
            [strava-api.handlers :refer [get-handler]]
            [compojure.core :refer [defroutes]]
            [compojure.route :as route]
            [org.httpkit.server :as server]
            [clj-http.client :as client]
            [clojure.data.json :as json])
  (:gen-class))

(defroutes app
  (get-handler "/api/v1/strava/hi"
               {:message "Hello World!"})

  (get-handler "/api/v1/strava"
               {:message "Strava"})

  (route/not-found
    {:status 404
     :body "Not Found"}))

(let [response (client/post (str (get (get-env-vars) :strava-domain) "/oauth/token")
                            {:throw-entire-message? true
                             :form-params {:client_id (get (get-env-vars) :client-id)
                                                    :client_secret (get (get-env-vars) :client-secret)
                                                    :code (get (get-env-vars) :code)
                                                    :grant_type "authorization_code"}
                             :content-type :json})]
  (println response))

(defn -main []
  (let [port (get (get-env-vars) :port)]
    (server/run-server app {:port port})
    (println (str "Server running on port " port))))
