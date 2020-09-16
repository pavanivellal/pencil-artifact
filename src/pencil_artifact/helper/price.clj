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

(comment
  (calc-price {:qty 10
               :final-price 3} {:qty 8
                                :price 6})
  :end)

(defn- apply-amount-based-offers
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
          apply-amount-based-offers) ;; First apply special offers
      (apply-amount-based-offers {:qty qty
                                  :final-price 0
                                  :offers (:offers volume-offers)}))))

(comment

  ;;Test 1 -------------------
  ;On 10/1/2021, add these items: 8 Cookies, 4 Cheesecakes. Verify the price is $30.
  ;
  ;-> 8 cookies (item code 3) =  6
  ;-> 4 cheesecakes (item code 2) = 24
  ;TOTAL = 30

  ;; With special offers
  (get-best-price 3 "2021-10-01" 8)
  (get-best-price 2 "2021-10-01" 4)


  ;;Test 2 -------------------
  ;Cookie, Brownie x 4, Cheesecake. Verify the total is $16.25.
  ;
  ;-> 1 Cookie (item code 3) = 1.25
  ;-> 4 Brownie (item code 1) = 7
  ;-> 1 Cheesecake (item code 2) = 8
  ;TOTAL = 16.25

  ;; Without special offers
  (get-best-price 3 nil 1)
  (get-best-price 1 nil 4)
  (get-best-price 2 nil 1)


  ;;Test 3 -------------------
  ;Cookie x 8. Verify the total is $8.50
  ;
  ;-> 8 Cookie (item code 3) = 8.5
  ;TOTAL = 8.5

  ;; Without special offers
  (get-best-price 3 nil 8)


  ;;Test 4 -------------------
  ;Cookie, Brownie, Cheesecake, Donut x 2. Verify the price is $12.25.
  ;
  ;-> 1 Cookie (item code 3) = 1.25
  ;-> 1 Brownie (item code 1) = 2
  ;-> 1 Cheesecake (item code 2) = 8
  ;-> 2 Donut (item code 4) = 1
  ;TOTAL = 12.25

  ;; Without special offers
  (get-best-price 3 nil 1)
  (get-best-price 1 nil 1)
  (get-best-price 2 nil 1)
  (get-best-price 4 nil 2)


  ;(offers/get-applicable-offers 1 nil 8)
  ;(offers/get-applicable-offers 3 "2020-09-18" 8)
  ;(offers/get-applicable-offers 2 "2020-10-01" 1)
  ;
  ;(get-best-price)
  :end)




