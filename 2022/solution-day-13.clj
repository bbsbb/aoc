(defn input []
  (as-> "input-day-13.txt" $
    (slurp $)
    (clojure.string/split $ #"\n\n")
    (map #(clojure.string/split % #"\n") $)))

(defn vector-verdict [left right result]
  (loop [l left
         r right]
    (cond
      (and (nil? (seq l)) (nil? (seq r))) 0
      (nil? (seq l))                      result
      (nil? (seq r))                      -1
      :else                               (let [current-left  (first l)
                                                current-right (first r)]
                                            (cond
                                              (or (vector? current-left) (vector? current-right)) (let [correct-left  (if-not (vector? current-left)
                                                                                                                        [current-left]
                                                                                                                        current-left)
                                                                                                        correct-right (if-not (vector? current-right)
                                                                                                                        [current-right]
                                                                                                                        current-right)
                                                                                                        outcome       (vector-verdict correct-left correct-right result)]
                                                                                                    (if-not (zero? outcome)
                                                                                                      outcome
                                                                                                      (vector-verdict (into [] (rest l))
                                                                                                                      (into [] (rest r))
                                                                                                                      result)))
                                              (< current-left current-right)                      result
                                              (> current-left current-right)                      -1
                                              :else                                               (vector-verdict (into [] (rest l)) (into [] (rest r)) result))))))

;; 6072
(defn p1 []
  (->> (input)
       (map-indexed (fn [idx [left right]]
                      (vector-verdict (into [] (read-string left))  (into [] (read-string right)) (inc idx))))
       (filter pos?)
       (apply +)))

;; 22184
(defn p2 []
  (->> (input)
       (mapcat (fn [[l r]]
                 [(read-string l) (read-string r)]))
       (into [[[2]] [[6]]])
       (sort #(vector-verdict %2 %1 1))
       (map-indexed (fn [idx packet]
                      (if (or (= packet [[2]])
                              (= packet [[6]]))
                        (inc idx)
                        1)))
       (apply *)))
