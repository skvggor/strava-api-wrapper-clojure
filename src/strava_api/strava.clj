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
  (let [response (client/post (str (env-var :domain) "/api/v3/oauth/token")
                              {:body (json/write-str {:client_id (env-var :client-id)
                                                      :client_secret (env-var :client-secret)
                                                      :refresh_token (env-var :refresh-token)
                                                      :grant_type "refresh_token"})
                               :headers {"Content-Type" "application/json"}
                               :throw-exceptions false})]
    (if (= 200 (:status response))
      (-> response :body (json/read-str :key-fn keyword) (:access_token))
      (handle-http-error response))))

(def sport-fields
  {:run "ytd_run_totals"
   :ride "ytd_ride_totals"})

(defn get-sport-field [sport]
  (get sport-fields (keyword sport) "ytd_run_totals"))

(defn fetch-activities [access-token sport]
  (let [sport-field (get-sport-field sport)
        response (client/get (str
                              (env-var :domain)
                              "/api/v3/athletes/"
                              (env-var :user-id)
                              "/stats")
                             {:headers {"Authorization" (str "Bearer " access-token)}
                              :throw-exceptions false})
        body (json/read-str (:body response) :key-fn keyword)
        totals (get body (keyword sport-field))]
    (if (and (= 200 (:status response))
             (map? body)
             (map? totals)
             (number? (:distance totals)))
      (:distance totals)
      (handle-http-error response))))

(defn fetch-total-distance-in-year [sport]
  (-> (fetch-access-token)
      (fetch-activities sport)
      (/ 1000)
      (float)
      (round-to 1)))
