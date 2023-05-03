(ns strava-api.core-test
  (:require [clojure.test :refer :all]
            [strava-api.core :refer :all]
            [clj-http.client :as client]))

(def base-url "http://localhost:8080")

(deftest app-routes-test
  (testing "GET /api/v1/hi"
    (let [response (client/get (str base-url "/api/v1/hi"))]
      (is (= 200 (:status response)))
      (is (= "Hello World!" (get-in response [:body :message])))))

  (testing "GET /api/v1/strava/total-distance/current-year"
    (let [response (client/get (str base-url "/api/v1/strava/total-distance/current-year"))]
      (is (= 200 (:status response))))))
