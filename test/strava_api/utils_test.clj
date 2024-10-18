(ns strava-api.utils-test
  (:require [clojure.test :refer [deftest
                                  is
                                  testing]]
            [strava-api.utils :refer [round-to]]))

(deftest test-round-to
  (testing "Test round-to function"
    (is (= 1234.57 (round-to 1234.5678 2)))
    (is (= -1234.57 (round-to -1234.5678 2)))
    (is (= 0.0 (round-to 0 2)))
    (is (= 1235.0 (round-to 1234.5678 0)))
    (is (= 1234.5678 (round-to 1234.5678 4)))))
