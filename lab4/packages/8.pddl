(define (problem C3_2)
  (:domain logistics)
  (:objects
   city1 city2 city3
   truck1 truck2 truck3 truck4 truck5 truck6
   airplane1 airplane2 airplane3
   office1 office2 office3
   airport1 airport2 airport3
   packet1 packet2 packet3 packet4 packet5 packet6 packet7 packet8 packet9 packet10 packet11 packet12,
   drone1
   )
  (:init
   (object packet1)
   (object packet2)
   (object packet3)
   (object packet4)
   (object packet5)
   (object packet6)
   (object packet7)
   (object packet8)
   (object packet9)
   (object packet10)
   (object packet11)
   (object packet12)
   (small packet1)
   (small packet2)
   (small packet3)
   (small packet4)
   (small packet5)
   (small packet6)
   (small packet7)
   (small packet8)
   (small packet9)
   (small packet10)
   (small packet11)
   (small packet12)
   (vehicle truck1)
   (vehicle truck2)
   (vehicle truck3)
   (vehicle truck4)
   (vehicle truck5)
   (vehicle truck6)
   (vehicle airplane1)
   (vehicle airplane2)
   (vehicle airplane3)
   (truck truck1) (truck truck2) (truck truck3)
   (truck truck4) (truck truck5) (truck truck6)
   (airplane airplane1)
   (airplane airplane2)
   (airplane airplane3)
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
   (at packet2 office1)
   (at packet3 office1)
   (at packet4 office1)
   (at packet5 office1)
   (at packet6 office1)
   (at packet7 office1)
   (at packet8 office1)
   (at packet9 office1)
   (at packet10 office1)
   (at packet11 office1)
   (at packet12 office1)
   (at truck1 airport1)
   (at truck2 airport2)
   (at truck3 office3)
   (at truck4 airport1)
   (at truck5 airport3)
   (at truck6 office3)
   (at airplane1 airport1)
   (at airplane2 airport2)
   (at airplane3 airport3)
   (at drone1 airport1)
   )
  ;; The goal is to have both packages delivered to their destinations:
  (:goal (and(at packet1 office3)(at packet2 office3)(at packet3 office3)(at packet4 office3)(at packet5 office3)(at packet6 office3)(at packet7 office3)(at packet8 office3)(at packet9 office3)(at packet10 office3)(at packet11 office3)(at packet12 office3) );;
  ))
