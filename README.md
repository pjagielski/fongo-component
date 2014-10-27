# Fongo Component

A [component][] using [fongo][] as in-memory mock of MongoDB. Useful for unit testing Mongo-dependant components (eg. repositories). Compatible with mongo-component from [system][].

[![Build Status](http://img.shields.io/travis/pjagielski/fongo-component.svg?style=flat-square)](http://img.shields.io/travis/pjagielski/fongo-component.svg?style=flat-square)
[![Coverage](http://img.shields.io/coveralls/pjagielski/fongo-component.svg?style=flat-square)](http://img.shields.io/coveralls/pjagielski/fongo-component.svg?style=flat-square)

[component]: https://github.com/stuartsierra/component
[fongo]: https://github.com/fakemongo/fongo
[system]: https://github.com/danielsz/system

## Installation

Add the following dependency to your project.clj file:

[![Clojars Project](http://clojars.org/fongo-component/latest-version.svg)](http://clojars.org/fongo-component)

## Usage

Require the library:

```clojure
(require '[component.fongo :refer [new-fongo]
         '[com.stuartsierra.component :as component])
```

Starting the component will create a `:db` key:

```clojure
(-> (new-fongo "test")
    (component/start)
    :db)
```

## Using with repositories

An example repository protocol:

```clojure
(defprotocol Repository
  (find-by-id [repo id])
  (save [repo object]))
```

Could be implemented with [system][] as:

[system]: https://github.com/danielsz/system

```clojure
(defrecord MongoRepositoryComponent [mongo-db]
  component/Lifecycle Repository

  (start [component]
    (println ";; Starting MongoRepositoryComponent")
    component)

  (stop [component]
    (println ";; Stopping MongoRepositoryComponent"))

  (find-by-id [repo id]
    (let [db (:db mongo-db)]
      (mc/find-one-as-map (:db mongo-db) "documents" {:_id (ObjectId. id)})))

  (save [repo object]
    (let [db (:db mongo-db)]
      (mc/insert-and-return (:db mongo-db) "documents" object))))

(defn mongo-repo-component []
  (->MongoRepositoryComponent {}))
```

And tested with fongo-component:

```clojure
(def system
  (->
    (component/system-map
       :mongo-db (new-fongo "test")
       :repo     (repository/mongo-repo-component))
    (component/system-using
       {:repo [:mongo-db]})))

(deftest mongo-repository
  (alter-var-root #'system component/start)
  (is (:repo system) "mongo repository has been added to system")
  (testing "saving and getting object"
    (let [repo (:repo system)]
      (let [saved (repository/save repo {:name "test"})]
        (is (not (nil? (:_id saved))))
        (is (= saved (repository/find-by-id repo (.toString (:_id saved))))))))
  (alter-var-root #'system component/stop)
  (is (nil? (:repo system)) "mongo repository has been removed from system"))
```

## License

Copyright © 2014 Piotr Jagielski

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
