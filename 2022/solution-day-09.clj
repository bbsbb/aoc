(defn load-input [fname]
  (->> fname
       slurp
       clojure.string/split-lines
       (map (fn [l] (let [[command c] (clojure.string/split l #" ")]
                     {:command command
                      :steps (Integer/parseInt c 10)})))))

(defn next-h [command h]
  (case command
    "R" (update h :x dec)
    "L" (update h :x inc)
    "D" (update h :y dec)
    "U" (update h :y inc)))


(defn next-t [h t]
  (cond
    (= t h)                                                                     t
    ;; fuck p2
    (and (< 1 (Math/abs (- (:x t) (:x h)))) (< 1 (Math/abs (- (:y t) (:y h))))) {:x (/ (+ (:x h)
                                                                                          (:x t))
                                                                                       2)
                                                                                 :y (/ (+ (:y h)
                                                                                          (:y t))
                                                                                       2)}
    (< 1 (Math/abs (- (:x t) (:x h))))                                          {:x (/ (+ (:x h)
                                                                                          (:x t))
                                                                                       2)
                                                                                 :y (if (= (:y t) (:y h))
                                                                                      (:y t)
                                                                                      (:y h))}
    (< 1 (Math/abs (- (:y t) (:y h))))                                          {:y (/ (+ (:y h)
                                                                                          (:y t))
                                                                                       2)
                                                                                 :x (if (= (:x t) (:x h))
                                                                                      (:x t)
                                                                                      (:x h))}
    :else                                                                       t))


(defn moves [input rope-length]
  (reduce
   (fn [state {:keys [command steps]}]
     (loop [s        steps
            progress state]
       (if (= 0 s)
         progress
         (let [nh        (next-h command (first (:rope progress)))
               next-rope (reduce (fn [r n]
                                   (conj r (next-t (last r) n)))
                                 [nh]
                                 (rest (:rope progress)))]
           (recur (dec s)
                  {:visited (conj (:visited progress) (last next-rope))
                   :rope    next-rope})))))
   {:visited #{}
    :rope    (into [] (repeat rope-length {:x 0 :y 0}))}
   input))

;; 6190
(-> "input-day-09.txt"
    load-input
    (moves 2)
    :visited
    count
    println)

;; 2516
(-> "input-day-09.txt"
    load-input
    (moves 10)
    :visited
    count
    println)
