(ns playground-clj.curve)

(defn in-area? [{:keys [x y]} {:keys [n w s e]}]
  (and (<= w x e)
       (<= s y n)))

(defn next-position [{:keys [position velocity]}]
  {:position (-> position
                 (update :x + (:x velocity))
                 (update :y + (:y velocity)))
   :velocity (-> velocity
                 (update :y - 1)
                 (update :x #(cond
                               (=  % 0) %
                               (> % 0)  (dec %)
                               (< % 0)  (inc %))))})

(defn trajectory [from, velocity, area]
  (loop [route      []
         route-info {:position from
                     :velocity velocity}]
    (cond
      (in-area? (:position route-info) area)          {:v     velocity
                                                       :hit   true
                                                       :route (conj route (:position route-info))}
      (or (> (-> route-info :position :x) (:e area))
          (< (-> route-info :position :y) (:s area))) {:v     velocity
                                                       :hit   false
                                                       :route (conj route (:position route-info))}
      :else                                           (let [next (next-position route-info)]
                                                        (recur (conj route (:position route-info))
                                                               next)))))

(defn problems [filepath]
  (let [start                   {:x 0 :y 0}
        [west east south north] (->> filepath
                                     clojure.java.io/resource
                                     slurp
                                     (re-find #".*x=(\d+)..(\d+).*y=(.*\d+)..(.*)")
                                     (drop 1)
                                     (mapv #(Integer/parseInt % 10)))
        area                    {:w west
                                 :e east
                                 :n north
                                 :s south}
        trajectories            (loop [vel-x        0
                                       trajectories []]
                                 (if (> vel-x 500)
                                   trajectories
                                   (recur (inc vel-x)
                                          (concat trajectories (mapv #(trajectory start {:x vel-x :y %} area) (range -100 100))))))]
    {:p1 (reduce (fn [mx {:keys [route]}]
                   (apply max mx (mapv :y route)))
                 0
                 (->> trajectories (filter :hit)))
     :p2 (->> trajectories (filter :hit) count)}))

(problems "input-day-17.txt") ;; {:p1 3655, :p2 1447}
