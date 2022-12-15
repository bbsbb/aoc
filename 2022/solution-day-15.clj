(defn input []
  (->> "input-day-15.txt"
       slurp
       clojure.string/split-lines
       (mapv #(->> % (re-seq #"-?\d+") (map (fn [s] (Integer/parseInt s 10))) (partition 2)))
       (reduce (fn [acc current]
                 (let [[[x y] [bx by]] current
                       r               (+ (abs (- x bx))  (abs (- y by)))]
                   (-> acc
                       (update :sensors conj {:x x
                                              :y y
                                              :r r})
                       (update :filled #(apply conj % [[x y] [bx by]]))
                       (update :min-x #(apply min % [(- x r) (- bx r)]))
                       (update :max-x #(apply max % [(+ x r) (+ x r)])))))
               {:sensors []
                :filled  #{}
                :min-x   Integer/MAX_VALUE
                :max-x   Integer/MIN_VALUE})))

;; 4725496
(defn p1 []
  (let [{:keys [min-x max-x sensors filled]} (input)
        target-row                           2000000]
    (loop [idx      min-x
           occupied filled
           spots    0]
      (cond
        (= idx max-x)               spots
        (occupied [idx target-row]) (recur (inc idx) occupied spots)
        :else                       (recur (inc idx) occupied (+ spots (reduce (fn [_ {:keys [x y r]}]
                                                                                 (if (<= (+ (abs (- x idx))  (abs (- y target-row))) r)
                                                                                   (reduced 1)
                                                                                   0)) 0 sensors)))))))


(defn sensors->col-intervals [col sensors]
  (sort (reduce (fn [acc {:keys [x y r]}]
                  (let [i-left (- r (abs (- col y)))]
                    (if (>= i-left 0)
                      (conj acc [(- x i-left) (+ 1 x i-left)])
                      acc)))
                []
                sensors)))

(defn candidates [col sensors]
  (loop [i         (sensors->col-intervals col sensors)
         opts      []
         limit-ext (-> i first first dec)]
    (if-not (seq i)
      (reverse opts)
      (let [[s e]   (first i)
            limit-s (min (max s 0) #_21 4000001)
            limit-e (min (max e 0) #_21 4000001)]
        #_(printf "%d %d %d" limit-s limit-e limit)
        (cond
          (or (>= limit-s limit-e)
              (>= limit-ext limit-e)) (recur (rest i) opts limit-ext)
          (<= limit-s limit-ext)      (recur (rest i) (conj (rest opts) [(-> opts first first) limit-e]) limit-e)
          :else                       (recur (rest i) (conj opts [limit-s limit-e]) limit-e))))))

(defn p2 []
  (let [{:keys [sensors]} (input)]
    (loop [y 0]
      (when (not= y #_21 4000001)
        (let [cs (candidates y sensors)]
          #_(println cs)
          (if (not= (abs (apply + (mapv (fn [[s e]] (- e s)) cs))) #_21 4000001)
            (+ (* 4000000 (-> cs first second)) y)
            (recur (inc y))))))))




;; 4725496
(println (p1))
;; 12051287042458
(println (p2))
