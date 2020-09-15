(ns pencil-project.helper.price
  (:require [pencil-project.data          :as data]
            [pencil-project.helper.offers :as offers]))

(defn- calc-price [item-obj offer-obj]
  (let [item-qty         (:qty item-obj)
        _ (println "item-obj:" item-obj)
        _ (println "item-qty:" item-qty)
        offer-qty        (:qty offer-obj)
        item-final-price (:final-price item-obj)
        offer-price      (:price offer-obj)]
    (if (>= item-qty offer-qty)
      (let [multiple          (quot item-qty offer-qty)
            final-qty         (- item-qty (* multiple offer-qty))
            final-offer-price (+ item-final-price
                                 (* multiple offer-price))]
        {:qty final-qty
         :final-price final-offer-price}))))

(comment
  (calc-price {:qty 10
               :final-price 3} {:qty 8
                                :price 6})
  :end)

(defn- calc-percentage [item-final-price offer-obj]
  (let [offer-percentage      (or (:percentage offer-obj)
                                  0)
        discount              (* item-final-price
                                 (/ offer-percentage 100))
        final-offer-price     (- item-final-price
                                 discount)]
    (float final-offer-price)))

(comment
  (calc-percentage 50 {:qty 1
                       :percentage 25})
  :end)

(defn- apply-amount-based-offers
  "Apply quantity and amount based offers"
  [qty offers]
  (reduce calc-price {:qty qty
                      :final-price 0} offers))

(comment
  (apply-amount-based-offers 8 [{:qty 6
                                 :price 6}
                                {:qty 1
                                 :price 1.25}])

  :end)

(defn- apply-percentage-based-offers
  "Apply quantity and percentage based offers"
  [calc-total offers]
  (reduce calc-percentage calc-total offers))

(comment
  (apply-percentage-based-offers 100 [{:qty 1
                                       :percentage 25}])

  :end)


(defn get-best-price
  "Return best price for item based on all product offers"
  [item-id offer-date qty]
  (let [offers-lst              (offers/get-applicable-offers item-id offer-date qty)
        amount-based-offers     (->> (filter #(:price %) offers-lst)
                                     (sort-by :qty #(compare %2 %1))) ;; Sorting offers in descending order with assumption that larger quantities have better offers
        percentage-based-offers (->> (filter #(:percentage %) offers-lst)
                                     (sort-by :qty #(compare %2 %1))) ;; Sorting offers in descending order with assumption that larger quantities have better offers
        final-price-obj         (apply-amount-based-offers qty amount-based-offers)
        final-price             (apply-percentage-based-offers (:final-price final-price-obj)
                                                               percentage-based-offers)]
    final-price))





(comment
  (get-best-price 2 "2020-10-01" 1)
  (get-best-price 3 "2020-09-18" 8)




  (offers/get-applicable-offers 1 nil 8)
  (offers/get-applicable-offers 3 "2020-09-18" 8)
  (offers/get-applicable-offers 2 "2020-10-01" 1)


  (get-best-price)




  :end)




