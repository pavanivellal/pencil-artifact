(ns pencil-artifact.core
  (:gen-class)
  (:require
    [clj-time.core               :as dt]
    [clojure.string              :as str]
    [pencil-artifact.helper.items :as items]
    [pencil-artifact.helper.price :as price]
    [pencil-artifact.data         :as data]
    [clojure.pprint              :as pprint]))

(defn -main
  [& args]
  (data/init)
  (let [cart-ip-str       (or (first args)
                              (do
                                (println "Enter items in cart along with the quantities as follows \"Cookies x 8, Cheesecakes x 4\" : ")
                                (read-line)))
        offer-date-str    (or (second args)
                              (do
                                (println "Enter offer date if applicable else enter NA : ")
                                (read-line)))
        offer-date-str    (when-not (= offer-date-str "NA")
                            offer-date-str)
        cart-items-lst    (items/get-cart-items cart-ip-str)
        cart-total        (price/calculate-cart-total cart-items-lst offer-date-str)]
    (println "Offer date : " offer-date-str)
    (println "Cart items with quantity : ")
    (pprint/pprint cart-items-lst)
    (println "Cart total : " cart-total)))

(comment
  (-main "Cookie, Brownie x 4, Cheesecake" "NA")
  (-main "Cookie x 8" "NA")
  (-main "Cookie, Brownie, Cheesecake, Donut x 2" "NA")
  (-main "Cookie x 8, Cheesecake x 4" "2021-10-01")
  :end)

