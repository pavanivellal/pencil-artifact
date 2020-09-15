(ns pencil-project.core
  (:gen-class)
  (:require
    [clj-time.core               :as dt]
    [clojure.string              :as str]
    [pencil-project.helper.items :as items]
    [pencil-project.data         :as data]
    [clojure.pprint              :as pprint]))

(defn -main
  [& args]
  (data/init)
  (let [offer-date-str    (or (first args)
                              (do
                                (println "Enter offer date : ")
                                (read-line)))
        cart-ip-str       (or (second args)
                              (do
                                (println "Enter items in cart along with the quantities as follows \"Cookies x 8, Cheesecakes x 4\" : ")
                                (read-line)))
        cart-items-lst    (items/get-cart-items cart-ip-str)]
    (println "Offer date : " offer-date-str)
    (println "Cart items with quantity : ")
    (pprint/pprint cart-items-lst)))



(comment
  (-main "2021-10-01" "Cookie x 3, Cheesecake x 4, Cookie x 5")
  :end)

