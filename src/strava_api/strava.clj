(ns strava-api.strava
  (:require [strava-api.utils :refer [get-env-vars,
                                      round-to]]
            [clj-http.client :as client]
            [clojure.data.json :as json]))

(defn env-var [key]
  (get (get-env-vars) key))

(defn handle-http-error [response]
  (let [status (:status response)
        body (:body response)]
    (throw (ex-info (str "HTTP error " status) {:status status :body body}))))

(defn fetch-access-token []
  (let [response (client/post (str (env-var :strava-domain) "/api/v3/oauth/token")
                              {:body (json/write-str {:client_id (env-var :client-id)
                                                      :client_secret (env-var :client-secret)
                                                      :refresh_token (env-var :refresh-token)
                                                      :grant_type "refresh_token"})
                               :headers {"Content-Type" "application/json"}
                               :throw-exceptions false})]
    (if (= 200 (:status response))
      (-> response :body (json/read-str :key-fn keyword) (:access_token))
      (handle-http-error response))))

(defn fetch-activities [access-token]
  (let [response (client/get (str
                              (env-var :strava-domain)
                              "/api/v3/athletes/"
                              (env-var :user-id)
                              "/stats")
                             {:headers {"Authorization" (str "Bearer " access-token)}
                              :throw-exceptions false})
        body (json/read-str (:body response) :key-fn keyword)]
    (if (and (= 200 (:status response))
             (map? body)
             (map? (:ytd_ride_totals body))
             (number? (:distance (:ytd_ride_totals body))))
      (:distance (:ytd_ride_totals body))
      (handle-http-error response))))

(defn fetch-total-distance-in-year []
  (-> (fetch-access-token)
      (fetch-activities)
      (/ 1000)
      (float)
      (round-to 1)))
