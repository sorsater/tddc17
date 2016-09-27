(define (problem C3_2)
  (:domain logistics)
  (:objects
   city1 city2 city3
   truck1 truck2 truck3
   airplane1
   office1 office2 office3
   airport1 airport2 airport3
   packet1
   drone1
   )
  (:init
   (object packet1)

   (small packet1)
   (vehicle truck1)
   (vehicle truck2)
   (vehicle truck3)

   (vehicle airplane1)
   (truck truck1) (truck truck2) (truck truck3)
   (airplane airplane1)
   (vehicle drone1) (drone drone1)

   ;; likewise, airports must be declared both as "location" and as
   ;; the subtype "airport",
   (location office1)
   (location office2)
   (location office3)

   (location airport1)
   (location airport2)
   (location airport3)

   (airport airport1)
   (airport airport2)
   (airport airport3)

   (city city1)
   (city city2)
   (city city3)

   (loc office1 city1)
   (loc office2 city2)
   (loc office3 city3)

   (loc airport1 city1)
   (loc airport2 city2)
   (loc airport3 city3)

   ;; The actual initial state of the problem, which specifies the
   ;; initial locations of all packages and all vehicles:
   (at packet1 office1)

   (at truck1 airport1)
   (at truck2 airport2)
   (at truck3 office3)
   (at airplane1 airport1)
   (at drone1 airport1)
   )
  ;; The goal is to have both packages delivered to their destinations:
  (:goal (at packet1 office3));;
  )
