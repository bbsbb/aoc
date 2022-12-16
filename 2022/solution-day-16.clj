
(defn input []
  (->> "input-day-16.txt"
       slurp
       clojure.string/split-lines
       ;; Why the fuck did I do a map AT FIIIIRSTAAAAAA
       (reduce (fn [nodes c]
                 (let [i (clojure.string/split c #" ")]
                   (conj nodes
                         {:vertice  (-> i second keyword)
                          :rate     (Integer/parseInt (re-find #"\d+" (nth i 4)))
                          :adjacent (as-> c $
                                      (clojure.string/split $ #"(valve|valves)\s")
                                      (second $)
                                      (clojure.string/split $ #",")
                                      (mapv (comp keyword clojure.string/trim) $))})))
               [])))

(defn all-costs-for-vertice [g v]
  (-> (into {} (zipmap (mapv :vertice g) (repeat (* 30 30 (apply max (mapv :rate g))))))
      (assoc (:vertice v) 0)
      (into (zipmap (:adjacent v) (repeat 1)))))

(defn paths-cost [g]
  (let [vertices     (mapv :vertice g)
        direct-paths (reduce (fn [paths v]
                               (assoc paths
                                      (:vertice v)
                                      (all-costs-for-vertice g v)))
                             {}
                             g)]
    (reduce (fn [p [source target over]]
              (assoc-in p [source target]
                        (min (get-in p [source target])
                             (+ (get-in p [source over]) (get-in p [over target])))))
            direct-paths
            (for [over   vertices
                  source vertices
                  target vertices]
              [source target over]))))

(defn find-path [paths g potential current rounds elephant?]
  (loop [hv       potential
         pressure (if elephant?
                    0
                    (find-path paths g potential :AA 26 true))]
    (if-not (seq hv)
      pressure
      (let [v          (first hv)
            after-step (- rounds (inc (get-in paths [current (:vertice v)])))]
        (recur (rest hv)
               (if (<= after-step 0)
                 pressure
                 (max pressure (+ (* (:rate v) after-step)
                                  (find-path paths
                                             g
                                             (filter #(not= (:vertice %) (:vertice v)) potential)
                                             (:vertice v)
                                             after-step
                                             elephant?)))))))))

(defn p1 []
  (let [vertices (input)
        paths    (paths-cost vertices)]
    (find-path paths
               vertices
               (->> vertices (filter #(< 0 (:rate %))))
               :AA
               30
               true)))

(defn p2 []
  (let [vertices (input)
        paths    (paths-cost vertices)]
    (find-path paths
               vertices
               (->> vertices (filter #(< 0 (:rate %))))
               :AA
               26
               false)))

;; 1845
(println (p1))
;; 2286
(println (p2))
