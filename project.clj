(defproject fongo-component "0.1.0-SNAPSHOT"
  :description "A fake MongoDB component for testing"
  :url "http://github.com/pjagielski/fongo-component"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [com.stuartsierra/component "0.2.2"]
                 [com.novemberain/monger "2.0.0"]
                 [com.github.fakemongo/fongo "1.5.5"]
                 [org.slf4j/slf4j-api "1.7.7"]])
