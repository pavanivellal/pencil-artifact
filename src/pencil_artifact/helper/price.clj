(ns pencil-artifact.helper.price
  (:require [pencil-artifact.data          :as data]
            [pencil-artifact.helper.offers :as offers]))

(defn- calc-price [item-obj offer-obj]
  (let [item-qty         (:qty item-obj)
        offer-qty        (:qty offer-obj)
        item-final-price (:final-price item-obj)
        offer-price      (:price offer-obj)]
    (if (>= item-qty offer-qty)
      (let [multiple          (quot item-qty offer-qty)
            final-qty         (- item-qty (* multiple offer-qty))
            final-offer-price (+ item-final-price
                                 (* multiple offer-price))]
        {:qty final-qty
         :final-price final-offer-price})
      item-obj)))

(defn- apply-offers
  "Apply quantity and amount based offers"
  [{:keys [qty offers final-price]}]
  (->> (sort-by :qty #(compare %2 %1) offers)
       (reduce calc-price {:qty qty
                           :final-price final-price})))

(defn get-best-price
  "Return best price for item based on all product offers"
  [item-id offer-date qty]
  (let [{:keys [special-offers
                volume-offers]}  (offers/get-applicable-offers item-id offer-date qty)]
    (if special-offers
      (-> {:qty qty
           :final-price 0
           :offers special-offers}
          apply-offers ;; First apply special offers
          (merge volume-offers)
          apply-offers)
      (apply-offers {:qty         qty
                     :final-price 0
                     :offers      (:offers volume-offers)}))))

(defn calculate-cart-total
  "Calculate total amount of cart given list of items"
  [items offer-date]
  (reduce (fn [sum item]
            (let [best-price (get-best-price (:id item) offer-date (:qty item))]
              (+ sum (:final-price best-price)))) 0 items))




