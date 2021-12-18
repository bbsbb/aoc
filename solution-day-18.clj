(ns playground-clj.tuples)

(defn load-input! [filepath]
  (->> filepath
       clojure.java.io/resource
       clojure.java.io/reader
       line-seq
       (mapv read-string)))

(defn merge-pair [[left right] merge-direction]
  "This function was the worst idea I've had this year and it's the end of december."
  #_(println (format "Adding %s %s %s" merge-direction left right))
  (cond
    (nil? right) left
    (int? left)  (+ left right)
    :else        (loop [path [(if (= :left merge-direction)
                                0
                                (- (count left) 1))]
                        fish (if (= :left merge-direction)
                               (first left)
                               (last left))]
                   (if-not (vector? fish)
                     (if (get-in left path)
                       (update-in left path + right)
                       (assoc-in left path right))
                     (recur (conj path (if (= :left merge-direction)
                                         0
                                         (- (count fish) 1)))
                            (if (= :left merge-direction)
                              (first fish)
                              (last fish)))))))

(defn explode-maybe [fish depth]
  (cond
    (int? fish) {:transformed false
                 :fish        fish}
    (= 0 depth) {:transformed true
                 :fish        0
                 :old-fish    fish}
    :else       (let [[left right] fish
                      left-explode (explode-maybe left (dec depth))]
                  (if (:transformed left-explode)
                    (do
                      {:transformed true
                       :fish        [(:fish left-explode) (merge-pair [right
                                                                       (second (:old-fish left-explode))]
                                                                      :left)]
                       :old-fish    [(first (:old-fish left-explode)) nil]})
                    (let [right-explode (explode-maybe right (dec depth))]
                      (if (:transformed right-explode)
                        {:transformed true
                         :fish        [(merge-pair [(:fish left-explode)
                                                    (first (:old-fish right-explode))]
                                                   :right)
                                       (:fish right-explode)]
                         :old-fish    [nil (second (:old-fish right-explode))]}
                        {:transformed false
                         :fish        fish}))))))

(defn split-maybe [fish]
  (if (int? fish)
    {:transformed (>= fish 10)
     :fish        (if (>= fish 10)
                    [(quot fish 2) (int (Math/ceil (/ fish 2)))]
                    fish)}
    (let [[left right] fish
          left-split   (split-maybe left)]
      (if (:transformed left-split)
        {:transformed true
         :fish        [(:fish left-split) right]}
        (let [right-split (split-maybe right)]
          (assoc right-split :fish [left, (:fish right-split)]))))))

;; Sequence of split and explode
(defn fold-fish [left right]
  (reduce (fn [{:keys [transformed fish iteration]} _]
            (if (not transformed)
              (reduced fish)
              (let [{:keys [transformed fish]} (explode-maybe fish 4)]
                (if transformed
                  (do
                    #_(println (format "Exploded iteration %d: %s" iteration fish))
                    {:transformed true
                     :fish        fish
                     :iteration   (inc iteration)})
                  (do
                    (let [{:keys [transformed fish]} (split-maybe fish)]
                      #_(println (format "Split iteration %d: %s" iteration fish))
                      {:transformed transformed
                       :fish        fish
                       :iteration   (inc iteration)}))))))
          {:fish        [left right]
           :transformed true
           :iteration   1} (range)))

(defn magnitude [swarm]
  (if (vector? swarm)
    (+ (* 3 (magnitude (first swarm)))
       (* 2 (magnitude (second swarm))))
    swarm))

(defn problem-one [filepath]
  (let [fish  (load-input! filepath)
        swarm (reduce fold-fish (first fish) (rest fish))]
    (magnitude swarm)))

(defn problem-two [filepath]
  (let [fish      (load-input! filepath)
        all-folds (loop [current (first fish)
                         swarm   (rest fish)
                         folds   []]
                    (if (= 1 (count swarm))
                      folds
                      (recur (first swarm) (rest swarm) (concat folds (mapv #(fold-fish current %) swarm)))))]
    (apply max (mapv magnitude all-folds))))

(problem-one "input-day-18.txt") ;; 4132
(problem-two "input-day-18.txt") ;; 4685
