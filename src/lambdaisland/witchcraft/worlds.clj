(ns lambdaisland.witchcraft.worlds
  (:refer-clojure :exclude [bean])
  (:require [lambdaisland.witchcraft :as wc :refer :all]
            [lambdaisland.witchcraft.util :as util]
            [lambdaisland.witchcraft.bukkit :as bukkit :refer [entities materials]]
            [lambdaisland.witchcraft.safe-bean :refer [bean bean->]]))

(set! *warn-on-reflection* true)

(defn create-world
  "Create a new game world based on a generator function

  `gen-fn` is a function which takes (x y z) and returns a material."
  [name gen-fn spawn-loc]
  (.createWorld
   (wc/server)
   (.generator (org.bukkit.WorldCreator. name)
               (proxy [net.glowstone.generator.GlowChunkGenerator] [(into-array org.bukkit.generator.BlockPopulator [])]
                 (generateChunkData [world random chunk-x chunk-z biomes]
                   (let [chunk (net.glowstone.generator.GlowChunkData. world)]
                     (doseq [x (range 16)
                             y (range 128)
                             z (range 16)
                             :let [real-x (+ (* chunk-x 16) x)
                                   real-z (+ (* chunk-z 16) z)
                                   material (gen-fn real-x y real-z #_(.getBiome biomes real-x real-z))]
                             :when material]
                       (.setBlock chunk x y z (if (keyword? material)
                                                (get bukkit/materials material)
                                                material)))
                     chunk)))))
  (.setSpawnLocation (world name) (->location (assoc spawn-loc :world (world name)))))

(comment
  (create-world "stone-flats"
                (fn [x y z]
                  (when (or (< y 5))
                    :stone))
                {:x 0 :y 6 :z 0})

  (create-world "waterworld"
                (fn [x y z]
                  (cond
                    (< 20 y)
                    nil

                    (< (.distance (util/vec3 0 y 0) (util/vec3 x y z)) 30)
                    (if (= 20 y)
                      :grass
                      :dirt)

                    :water))
                {:x 0 :y 21 :z 0})


  (teleport {:world (world "waterworld")
             :y 22
             :x 0
             :z 0}))
