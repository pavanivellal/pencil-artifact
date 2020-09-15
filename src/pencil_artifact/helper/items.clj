(ns pencil-artifact.helper.items
  (:require
    [clojure.string :as str]))

(defn parse-item
  "return item name quantity given item entity string"
  [item-str]
  (let [[item
         quantity]  (str/split item-str #"x")
        item        (str/trim item)
        quantity    (if quantity
                      (-> quantity
                          str/trim
                          read-string)
                      1)]
    {item quantity}))

(defn get-cart-items
  "return items in cart as a map of item and quantity given cart items as input string"
  [cart-ip-str]
  (let [items     (str/split cart-ip-str #",")
        items-lst (map parse-item items)]
    (apply merge-with + items-lst)))



