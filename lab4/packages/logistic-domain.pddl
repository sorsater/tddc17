(define (domain logistics)
  (:requirements :strips)
  (:predicates

   (vehicle ?v)
   (truck ?t)
   (drone ?d) ;; new vehicle, drone
   (airplane ?p)

   (location ?l)
   (airport ?a)
   (city ?c)
   (loc ?l ?c)

   ;; different package sizes
   ;; all of them are supposed to be objects
   (object ?o)
   (small ?s)
   (medium ?m)
   (huge ?h)

   (at ?x ?l) ;; ?x (package or vehicle) is at location ?l
   (in ?p ?v) ;; package ?p is in vehicle ?v
   (loaded ?v) ;; vehicle ?v is loaded - used by the drones
   )

  ;; Actions for loading and unloading packages.
  ;; The load/unload actions are modified.
  ;; betterload/unload can take any number of packets and sizes.
  ;; dronelad/unload can only load 1 small package.
  ;; To control that the drone only takes one package, we have implemented "loaded"
  (:action betterload
    :parameters (?o ?v ?l)
    :precondition (and (object ?o) (vehicle ?v) (or (airplane ?v) (truck ?v)) (location ?l)
		       (at ?v ?l) (at ?o ?l))
    :effect (and (in ?o ?v) (not (at ?o ?l))))

  (:action betterunload
    :parameters (?o ?v ?l)
    :precondition (and (object ?o) (vehicle ?v) (or (airplane ?v) (truck ?v)) (location ?l)
		       (at ?v ?l) (in ?o ?v))
    :effect (and (at ?o ?l) (not (in ?o ?v))))

  (:action droneload
    :parameters (?o ?v ?l)
    :precondition (and (object ?o) (small ?o) (vehicle ?v) (drone ?v) (location ?l)
		       (at ?v ?l) (at ?o ?l) (not (loaded ?v)))
    :effect (and (in ?o ?v) (not (at ?o ?l)) (loaded ?v)))

  (:action droneunload
    :parameters (?o ?v ?l)
    :precondition (and (object ?o) (small ?o) (vehicle ?v) (drone ?v) (location ?l)
		       (at ?v ?l) (in ?o ?v))
    :effect (and (at ?o ?l) (not (in ?o ?v)) (not (loaded ?v)) ))

  (:action drive
    :parameters (?t ?l1 ?l2 ?c)
    :precondition (and (truck ?t) (location ?l1) (location ?l2) (city ?c)
		       (at ?t ?l1) (loc ?l1 ?c) (loc ?l2 ?c))
    :effect (and (at ?t ?l2) (not (at ?t ?l1))))

  (:action fly
    :parameters (?p ?a1 ?a2)
    :precondition (and (airplane ?p) (airport ?a1) (airport ?a2)
		       (at ?p ?a1))
    :effect (and (at ?p ?a2) (not (at ?p ?a1))))

  ;; Dronefly takes two locations and fly directly between them.
  ;; (office -> office), (airport -> office)...
  (:action dronefly
    :parameters (?d ?l1 ?l2)
    :precondition (and (drone ?d) (location ?l1) (location ?l2) (at ?d ?l1))
    :effect (and (at ?d ?l2) (not (at ?d ?l1))))
)
