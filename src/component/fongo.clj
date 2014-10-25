(ns component.fongo
  (:require [com.stuartsierra.component :as component]
            [monger.core :as mg]))

(defrecord Fongo [db-name]
  component/Lifecycle
  (start [component]
    (let [conn (.. (com.github.fakemongo.Fongo. "fongo") getMongo)]
      (assoc component :db (mg/get-db conn db-name))))
  (stop [component]
    (dissoc component :db)))

(defn new-fongo [db-name]
  (map->Fongo {:db-name db-name}))

