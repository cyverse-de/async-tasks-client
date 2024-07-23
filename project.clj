(defproject org.cyverse/async-tasks-client "0.0.5-SNAPSHOT"
  :description "Client for the async-tasks service"
  :url "https://github.com/cyverse-de/async-tasks-client"
  :license {:name "BSD"
            :url "http://cyverse.org/sites/default/files/iPLANT-LICENSE.txt"}
  :deploy-repositories [["releases" :clojars]
                        ["snapshots" :clojars]]
  :plugins [[jonase/eastwood "1.4.3"]
            [lein-ancient "0.7.0"]
            [test2junit "1.4.4"]]
  :dependencies [[org.clojure/clojure "1.11.3"]
                 [clj-http "3.13.0"]
                 [com.cemerick/url "0.1.1" :exclusions [com.cemerick/clojurescript.test]]
                 [cheshire "5.13.0"]
                 [medley "1.4.0"]
                 [org.cyverse/kameleon "3.0.10"]])
