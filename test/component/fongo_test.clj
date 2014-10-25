(ns component.fongo-test
  (:require [clojure.test :refer :all]
            [component.fongo :refer :all]
            [com.stuartsierra.component :as component]
            [monger.db :as db]
            [monger.collection :as mc]))

(deftest fongo
  (let [component (new-fongo "test")]
    (testing "initial values"
      (is (nil? (:db component))))
    (testing "start and stop"
      (let [component (component/start component)]
        (is (:db component))
        (let [db (:db component)]
          (is (= (mc/count db "values") 0)))
        (let [component (component/stop component)]
          (is (nil? (:db component))))))))
