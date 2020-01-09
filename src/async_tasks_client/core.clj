(ns async-tasks-client.core
  (:use [medley.core :only [remove-vals]])
  (:require [cemerick.url :as curl]
            [cheshire.core :as json]
            [clj-http.client :as http]
            [clojure.tools.logging :as log]))

(defprotocol Client
  "A client library for the async-tasks API."

  (get-by-id
    [_ id]
    "Fetches an async task by ID")

  (delete-by-id
    [_ id]
    "Deletes an async task by ID")

  (add-status
    [_ id status]
    "Adds a status to an existing task by ID, passing in a map which specifies the new status to add.")

  (add-behavior
    [_ id behavior]
    "Adds a behavior to an existing task by ID, passing in a map which specifies the new behavior to add.")

  (get-by-filter
    [_ filters]
    "Get a set of async tasks that match the provided filters.")

  (create-task
    [_ task]
    "Create a new task and mint an ID for it."))

(defn- async-tasks-url
  [base-url & components]
  (log/debug "using async-tasks base URL" base-url)
  (str (apply curl/url base-url (map curl/url-encode components))))

(defn- get-options
  [params & {:keys [as] :or {as :stream}}]
  {:query-params     params
   :as               as
   :follow-redirects false})

(defn- post-options
  [body params & {:keys [as] :or {as :stream}}]
  {:query-params     params
   :body             body
   :content-type     :json
   :as               as
   :follow-redirects false})

(def ^:private delete-options get-options)
(def ^:private put-options post-options)

(deftype AsyncTasksClient [base-url]
  Client

  (get-by-id
    [_ id]
    (:body (http/get (async-tasks-url base-url "tasks" id) (get-options {} :as :json))))

  (delete-by-id
    [_ id]
    (:body (http/delete (async-tasks-url base-url "tasks" id) (get-options {} :as :json))))

  ;; XXX this is probably wrong, need to return the Location header I think
  (create-task
    [_ task]
    (->> (http/post (async-tasks-url base-url "tasks") (post-options (json/encode task) {} :as :json))
         :body))

  (add-status
    [_ id status]
    (->> (http/post (async-tasks-url base-url "tasks" id "status") (post-options (json/encode status) {} :as :json))
         :body))

  (add-behavior
    [_ id behavior]
    (->> (http/post (async-tasks-url base-url "tasks" id "behaviors") (post-options (json/encode behavior) {} :as :json))
         :body))

  (get-by-filter
    [_ filters]
    (:body (http/get (async-tasks-url base-url "tasks") (get-options filters :as :json)))
    ))

(defn new-async-tasks-client [base-url]
  (AsyncTasksClient. base-url))
