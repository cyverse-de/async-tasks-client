(defproject org.cyverse/async-tasks-client "0.0.2-SNAPSHOT"
  :description "Client for the async-tasks service"
  :url "https://github.com/cyverse-de/async-tasks-client"
  :license {:name "BSD"
            :url "http://cyverse.org/sites/default/files/iPLANT-LICENSE.txt"}
  :deploy-repositories [["releases" :clojars]
                        ["snapshots" :clojars]]
  :plugins [[test2junit "1.2.2"]]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-http "2.0.0"]
                 [com.cemerick/url "0.1.1" :exclusions [com.cemerick/clojurescript.test]]
                 [cheshire "5.5.0"]
                 [medley "1.0.0"]
                 [org.cyverse/kameleon "3.0.0"]])
