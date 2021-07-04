(ns repl-sessions.rotation)



(defn transpose [m]
  (vec (apply map vector m)))


(defn m*
  ([m1 m2]
   (vec
    (for [row m1]
      (vec
       (for [col (transpose m2)]
         (apply + (map * row col)))))))
  ([m1 m2 & ms]
   (reduce m* (m* m1 m2) ms)))

(def direction [1 0 0])
(def axis [0 -1 0])
(def rotate-y [[0 0 1]
               [0 1 0]
               [-1 0 0]])

[0 -1 0]
[[0 0 1]
 [0 1 0]
 [-1 0 0]]

[0 1 0]
[[0 0 -1]
 [0 1 0]
 [1 0 0]]

[1 0 0]
[[1 0 0]
 [0 0 -1]
 [0 1 0]]

[0 0 1]
[[0 -1 0]
 [1 0 0]
 [0 0 1]]

(defn rotation-matrix [[x y z]]
  [[(Math/abs x) (- z) (- y)]
   [z (Math/abs y) (- x)]
   [y x (Math/abs z)]])


(m* [[0 -1 0]]
    (rotation-matrix [1 0 0]))

(Math/sin (/ Math/PI 2))
;; => 1.0
(Math/cos (/ Math/PI 2))
;; => 0

(m* [[0 0 1]] rotate-y rotate-y rotate-y)

(m* [[0 1 0]
     [0 0 1]
     [1 0 0]]
    [[0 1 0]
     [0 0 1]
     [1 0 0]]
    )
