(defproject pencil-artifact "0.0.1"
  :description    "Shopping cart with special sales"
  :url            "http://example.com/FIXME"
  :license        {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
                   :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies   [[org.clojure/clojure "1.10.1"]
                   [clj-time            "0.15.2"]]
  :main           pencil-artifact.core
  :target-path    "target/%s"
  :profiles       {:uberjar {:aot :all}}
  :test-selectors {:price :price
                   :offers :offers
                   :items :items})
