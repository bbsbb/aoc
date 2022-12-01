(defn day-01 [n]
  (->> "input-day-01.txt"
       clojure.java.io/resource
       clojure.java.io/reader
       line-seq
       (reduce (fn [acc l]
                 (if (= "" l)
                   (-> acc
                       (update :top-k (fn [c] (as-> c $
                                               (conj $ (:current acc))
                                               (sort-by - $)
                                               (take n $))))
                       (assoc :current 0))
                   (update acc :current + (Integer/parseInt l 10))))
               {:top-k   (take n (repeat -1))
                :current 0})))


;;1.
(-> (day-01 1)
    :top-k
    first)
;;2.
(->> (day-01 3)
     :top-k
     (apply +))
