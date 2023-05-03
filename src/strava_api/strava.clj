(ns strava-api.strava
  (:require [strava-api.utils :refer [get-env-vars]]
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
  (let [response (client/get (str (env-var :strava-domain) "/api/v3/athletes/" (env-var :user-id) "/stats")
                             {:headers {"Authorization" (str "Bearer " access-token)}
                              :throw-exceptions false})]
    (if (= 200 (:status response))
      (-> response :body (json/read-str :key-fn keyword) (:ytd_ride_totals :distance))
      (handle-http-error response))))

(defn fetch-total-distance-in-year []
  (-> (fetch-access-token)
      (fetch-activities)
      (Integer.)
      (/ 1000)
      (float)))
