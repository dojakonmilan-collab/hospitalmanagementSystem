/*Operation	Expected result
Patient registration	100 patients
Doctors	5 working concurrently
Nurses	10 updating records
Lab technicians	3 processing requests
Laboratory requests	10 completed
Pharmacists	2 attempting to dispense
Medicine inventory	20 doses remaining
Waiting lab requests	0

Why should 20 doses remain?

We started with 50 doses. Each pharmacist attempts to dispense 30 doses.
- One pharmacist successfully dispenses 30.
- The other pharmacist's request for 30 is rejected because only 20 remain.
- Final stock: 20 doses.



Hospital Management System
Concurrency mechanisms
Implemented


synchronized

Protects patient records and medicine inventory.


ReentrantLock

Protects access to the shared patient repository.


ExecutorService

Manages the 20 worker threads.


BlockingQueue

Coordinates nurses and laboratory technicians.


Semaphore

Limits laboratory machine usage to two technicians.*/