(ns pencil-artifact.data)


(defn init []
  (def offers     (atom (-> (slurp "resources/offers.edn")
                            clojure.edn/read-string)))
  (def products   (atom (-> (slurp "resources/products.edn")
                            clojure.edn/read-string)))
  (def item-name  (atom (-> (slurp "resources/name.edn")
                            clojure.edn/read-string))))
