(ns reviews-next.domain.user
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.string :as string]
            [mount.core :refer [defstate]]
            [reviews-next.config :as config]
            [reviews-next.database :as db]
            [taoensso.timbre :as log])
  (:import [com.google.api.client.googleapis.auth.oauth2 GoogleIdTokenVerifier$Builder]
           [com.google.api.client.json.jackson2 JacksonFactory]
           [com.google.api.client.http.javanet NetHttpTransport]))

(defstate google-token-validator
  :start (let [json-factory (JacksonFactory.)
               transport (NetHttpTransport.)
               client-id (-> config/config :secrets :google-oauth :client-id)]
           (.. (GoogleIdTokenVerifier$Builder. transport json-factory)
               (setAudience (list client-id))
               (build))))

(s/def ::id pos-int?)
(s/def ::name string?)
(s/def ::google-id string?)
(s/def ::email
  (s/with-gen
    (s/and string? #(re-matches #"^.+@.+\..+$" %))
    #(gen/fmap
       (fn [[s1 s2 s3]] (str s1 "@" s2 "." s3))
       (gen/vector (gen/string-alphanumeric) 3))))

(s/def ::id-token
  (s/and string? #(.verify google-token-validator %)))

(s/def ::registration-params
  (s/keys :req [::id-token]))

(s/def ::user
  (s/keys :req
          [::id
           ::name
           ::email
           ::google-id
           ::image-url
           ::created-at
           ::updated-at]))

(defn- id-token->user-info [id-token]
  (when-let [verified-token (.verify google-token-validator id-token)]
    (let [payload (.getPayload verified-token)]
      {::name (str (.get payload "name"))
       ::email (str (.get payload "email"))
       ::google-id (str (.getUserId payload))
       ::image-url (str (.get payload "picture"))})))

(defn register-user [tx params]
  (let [user-info (id-token->user-info (::id-token params))
        google-id (::google-id user-info)]
    (when-not (nil? user-info)
      (if-let [existing-user (db/find-by tx :users {::google-id google-id})]
        (do
          (log/info "Returning existing user for google-id" google-id)
          existing-user)
        (do
          (log/info "Creating new user for google-id" google-id)
          (db/create! tx :users user-info))))))

(s/fdef
  register-user
  :args (s/cat :params ::registration-params)
  :ret ::user)
