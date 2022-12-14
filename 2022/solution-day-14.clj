(require '[clojure.set :as cset])

(defn input []
  (as-> "input-day-14.txt" $
    (slurp $)
    (clojure.string/split $ #"\n")
    (map #(clojure.string/split % #" -> ") $)))


(defn parse-coords [p]
  (mapv #(Integer/parseInt % 10)
        (clojure.string/split p #",")))

(defn section [[cx cy] [px py]]
  (if (= px cx)
    (mapv #(vector cx %) (range (min cy py) (inc (max cy py))))
    (mapv #(vector % cy) (range (min cx px) (inc (max cx px))))))

(defn block-coords [paths]
  (->> paths
       (map (fn [p]
              (let [initial     (parse-coords (first p))
                    coordinates (reduce (fn [acc l]
                                          (let [[x y]           (parse-coords l)
                                                [last-x last-y] (:previous acc)]
                                            (-> acc
                                                (update :blocks #(apply conj % (section [x y] [last-x last-y])))
                                                (assoc :previous [x y]))))
                                        {:blocks   #{initial}
                                         :previous initial}
                                        (rest p))]
                coordinates)))
       (mapv :blocks)
       (apply cset/union)))

(defn drip [blocks x y highest-block]
  (loop [cx x
         cy y]
    (cond
      (= cy highest-block)                [blocks true]
      (nil? (blocks [cx (inc cy)]))       (recur cx (inc cy))
      (nil? (blocks [(dec cx) (inc cy)])) (recur (dec cx) (inc cy))
      (nil? (blocks [(inc cx) (inc cy)])) (recur (inc cx) (inc cy))
      :else                               [(conj blocks [cx cy]) false])))

(defn drip-with-floor [blocks x y highest-block]
  (loop [cx x
         cy y]
    (cond
      (= (inc cy) highest-block)          [(conj blocks [cx cy]) false]
      (nil? (blocks [cx (inc cy)]))       (recur cx (inc cy))
      (nil? (blocks [(dec cx) (inc cy)])) (recur (dec cx) (inc cy))
      (nil? (blocks [(inc cx) (inc cy)])) (recur (inc cx) (inc cy))
      :else                               [(conj blocks [cx cy]) false])))


(defn rain [dripper blocks]
  (let [[source-x source-y] [500 0]
        highest-block       (+ 2 (apply max (mapv second blocks)))]
    (loop [filled   blocks
           skipping false]
      (if (or skipping (filled [source-x source-y]))
        (- (count filled) (count blocks))
        (let [[nb sn] (dripper filled source-x source-y highest-block)]
          (recur nb sn))))))

(defn p1 []
  (->> (input)
       block-coords
       (rain drip)))

(defn p2 []
  (->> (input)
       block-coords
       (rain drip-with-floor)))
;; 1513
(println (p1))

;; 22646
(println (p2))
