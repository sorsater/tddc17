
;; This is a small problem instance for the standard Logistics domain,
;; as defined in "logistic.pddl".

(define (problem C3_2)
  (:domain logistics)
  (:objects
   city1 city2 city3          ;; there are three cities,
   truck1 truck2 truck3       ;; one truck in each city,
   airplane1                  ;; only one airplane,
   office1 office2 office3    ;; offices are "non-airport" locations
   airport1 airport2          ;; airports, one per city,
   packet1 packet2            ;; two packages to be delivered
   drone1                     ;; drone
   dronebase1, dronebase2     ;; dronebase
   )
  (:init
   ;; Type declarations:
   (object packet1) (object packet2)

   ;; all vehicles must be declared as both "vehicle" and their
   ;; appropriate subtype,
   (vehicle truck1) (vehicle truck2) (vehicle truck3) (vehicle airplane1)
   (truck truck1) (truck truck2) (truck truck3) (airplane airplane1)
   (vehicle drone1) (drone drone1)

   ;; likewise, airports must be declared both as "location" and as
   ;; the subtype "airport",
   (location office1) (location office2) (location office3)
   (location airport1) (location airport2)
   (airport airport1) (airport airport2)
   (city city1) (city city2) (city city3)
   (location dronebase1) (dronebase dronebase1)
   (location dronebase2) (dronebase dronebase2)


   ;; "loc" defines the topology of the problem,
   (loc office1 city1) (loc airport1 city1) (loc office2 city2)
   (loc airport2 city2) (loc office3 city3) (loc dronebase1 city3)
   (loc dronebase2 city2)

   ;; The actual initial state of the problem, which specifies the
   ;; initial locations of all packages and all vehicles:
   (at packet1 office1)
   (at packet2 office3)
   (at truck1 airport1)
   (at truck2 airport2)
   (at truck3 office3)
   (at airplane1 airport1)
   (at drone1 dronebase1)
   )

  ;; The goal is to have both packages delivered to their destinations:
  (:goal (and (at packet1 office2) (at packet2 office2)))
  )
