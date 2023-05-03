(ns strava-api.strava-test
  (:require [clojure.test :refer :all]
            [strava-api.strava :refer :all]))

(def test-env-vars {:client-id "test-client-id"
                    :client-secret "test-client-secret"
                    :refresh-token "test-refresh-token"
                    :strava-domain "https://www.strava.com"
                    :user-id "12345"
                    :port 8080})

(with-redefs [strava-api.utils/get-env-vars (constantly test-env-vars)]
  (deftest env-var-test
    (testing "env-var returns the correct value for a given key"
      (is (= "test-client-id" (env-var :client-id)))
      (is (= "test-client-secret" (env-var :client-secret)))
      (is (= "test-refresh-token" (env-var :refresh-token)))
      (is (= "https://www.strava.com" (env-var :strava-domain)))
      (is (= "12345" (env-var :user-id)))
      (is (= 8080 (env-var :port))))))
