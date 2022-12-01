(ns playground-clj.djk
  (:gen-class))

(defn load-input! [filepath]
  (-> filepath
      clojure.java.io/resource
      clojure.java.io/reader
      line-seq))


(defn neighbours [matrix i j]
  (let [max-i       (count matrix)
        max-j       (count (first matrix))
        coordinates (filter (fn [{:keys [ii jj]}]
                              (and (< -1 ii max-i)
                                   (< -1 jj max-j)))
                            [{:ii (dec i) :jj j}
                             {:ii (inc i) :jj j}
                             {:ii i :jj (dec j)}
                             {:ii i :jj (inc j)}])]
    (reduce (fn [acc {:keys [ii jj]}]
              (assoc acc
                     (keyword (format "%d-%d" ii jj))
                     (get-in matrix [ii jj]))
              ) {} coordinates)))


(defn build-graph [matrix]
  (reduce-kv (fn [g i row]
               (loop [j        0
                      adj-list {}]
                 (if (>= j (count row))
                   (merge g adj-list)
                   (recur (inc j)
                          (assoc adj-list (keyword (format "%d-%d" i j)) (neighbours matrix i j)))))) {} matrix))



(defn scale-matrix-h [matrix factor]
  (reduce (fn [new-matrix row]
            (conj new-matrix
                  (into [] (mapcat (fn [modifier]
                                     (mapv #(let [v (mod (+ (dec modifier) %) 9)]
                                              (if (zero? v)
                                                9
                                                v)) row)) (range 1 (inc factor))))))
          []
          matrix))


(defn scale-matrix-v [matrix factor]
  (loop [idx        1
         new-matrix matrix]
    (if (= idx factor)
      new-matrix
      (recur (inc idx)
             (reduce (fn [next row]
                       (conj next
                             (mapv #(let [v (mod (+ idx %) 9)]
                                      (if (zero? v)
                                        9
                                        v))
                                   row)))
                     new-matrix
                     matrix)))))


(defn close-risk
  [g risks subjects position]
  (reduce-kv
   (fn [new-risks node added-risk]
     (if (subjects node)
       (update-in new-risks [node] min (+ (position risks) added-risk))
       new-risks))
   risks
   (position g)))


(defn shortest-weighted-path [g start end]
  (let [nodes (keys g)
        risks (zipmap nodes (repeat Integer/MAX_VALUE))]
    (prn (format "Processing %d nodes" (count nodes)))
    (loop [position    start
           subjects    (disj (apply hash-set nodes) start)
           known-risks (assoc risks start 0)
           ;; We don't need this shit but I used it for debugging.
           visited     0]
      (when (zero? (mod visited 1000))
        (prn (format "Visited %d" visited)))
      (if (= position end)
        (select-keys known-risks [position])
        (let [new-risks (close-risk g known-risks subjects position)
              next      (apply min-key new-risks subjects)]
          (recur next (disj subjects next) new-risks (inc visited)))))))


(defn problem-one [filepath]
  (let [matrix     (->> filepath
                        load-input!
                        (mapv (fn [line]
                                (mapv #(Character/digit % 10) line))))
        start-node :0-0
        end-node   (keyword (format "%d-%d" (-> matrix count dec) (-> matrix first count dec)))]
    (shortest-weighted-path (build-graph matrix) start-node end-node)))


(defn problem-two [filepath]
  (let [matrix     (->> filepath
                        load-input!
                        (mapv (fn [line]
                                (mapv #(Character/digit % 10) line))))
        start-node :0-0
        end-node   (keyword (format "%d-%d" (-> matrix count (* 5) dec) (-> matrix first count (* 5) dec)))
        matrix-extended (-> matrix (scale-matrix-h 5) (scale-matrix-v 5))]
    (shortest-weighted-path (build-graph matrix-extended) start-node end-node)))

(problem-one "input-day-15.txt") ;; 824
(problem-two "input-day-15.txt") ;; 3063 .....this runs for ......a while.
