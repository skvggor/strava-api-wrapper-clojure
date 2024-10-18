(ns strava-api.utils
  (:require [config.core :refer [env]]))

(defn get-env-vars []
  {:domain    (:strava-domain env)
   :api-url          (:strava-api-url env)
   :client-id        (:strava-client-id env)
   :client-secret    (:strava-client-secret env)
   :refresh-token    (:strava-refresh-token env)
   :code             (:strava-code env)
   :user-id          (:strava-user-id env)
   :port             (Integer. (:port env))})

(defn round-to [number precision]
  (let [factor (Math/pow 10 precision)]
    (/ (Math/round (* number factor)) factor)))
