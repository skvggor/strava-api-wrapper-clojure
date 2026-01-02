(defproject strava-api "0.1.0-SNAPSHOT"
  :description "Program written in Clojure that acts as a wrapper for the Strava API."
  :url "http://skvgg.vercel.app"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.12.4"]
                  [compojure "1.7.1"]
                  [yogthos/config "1.2.1"]
                  [http-kit "2.6.0"]
                  [org.clojure/data.json "2.5.1"]
                  [clj-http "3.13.1"]
                  [cheshire "5.13.0"]]
  :main ^:skip-aot strava-api.core
  :uberjar-name "strava-api.jar"
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
