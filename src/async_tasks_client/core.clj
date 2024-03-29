(ns async-tasks-client.core
  (:use [medley.core :only [remove-vals]])
  (:require [cemerick.url :as curl]
            [cheshire.core :as json]
            [clj-http.client :as http]
            [clojure.string :as string]
            [clojure.tools.logging :as log]))

(defprotocol Client
  "A client library for the async-tasks API."

  (get-by-id
    [_ id]
    "Fetches an async task by ID")

  (delete-by-id
    [_ id]
    "Deletes an async task by ID")

  (add-status* [_ id status complete?])

  (add-status
    [this id status]
    "Adds a status to an existing task by ID, passing in a map which specifies the new status to add.")

  (add-completed-status
    [this id status]
    "Adds a status to an existing task by ID, passing in a map which specifies the new status to add. Additionally, completes the task.")

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

(defn- normalize-id [id-or-uri]
  (string/replace id-or-uri #".*/tasks/" ""))

(deftype AsyncTasksClient [base-url]
  Client

  (get-by-id
    [_ id]
    (:body (http/get (async-tasks-url base-url "tasks" (normalize-id id)) (get-options {} :as :json))))

  (delete-by-id
    [_ id]
    (:body (http/delete (async-tasks-url base-url "tasks" (normalize-id id)) (get-options {} :as :json))))

  (create-task
    [_ task]
    (->> (http/post (async-tasks-url base-url "tasks") (post-options (json/encode task) {} :as :json))
         :headers
         :location))

  (add-status*
    [_ id status complete?]
    (let [params (if complete? {:complete true} {})]
      (->> (http/post (async-tasks-url base-url "tasks" (normalize-id id) "status") (post-options (json/encode status) params :as :json))
           :headers
           :location)))

  (add-status
    [this id status]
    (add-status* this id status false))

  (add-completed-status
    [this id status]
    (add-status* this id status true))

  (add-behavior
    [_ id behavior]
    (->> (http/post (async-tasks-url base-url "tasks" (normalize-id id) "behaviors") (post-options (json/encode behavior) {} :as :json))
         :headers
         :location))

  (get-by-filter
    [_ filters]
    (json/parse-string (:body (http/get (async-tasks-url base-url "tasks") (get-options filters :as :string))) true)))

(defn new-async-tasks-client [base-url]
  (AsyncTasksClient. base-url))
