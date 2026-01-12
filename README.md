INTERVIEWER ───< INTERVIEW_SLOT >─── CANDIDATE
                      |
                      |
                  INTERVIEW
📌 Relationship Explanation

One Interviewer can have many Interview Slots

One Candidate can attend many Interviews

One Interview Slot can be booked only once

Interview acts as the booking record with status lifecycle

## Interview Slot Conflict Resolver

Interview Slot Conflict Resolver is a backend system built using Spring Boot and PostgreSQL
that ensures conflict-free interview scheduling by preventing overlapping interview slots
and double bookings.

### 🚀 Features
- Schedule and manage interview slots
- Automatic time-overlap conflict detection
- Atomic interview booking using database transactions
- Interview lifecycle management (PENDING → CONFIRMED → CANCELLED)
- Clean exception handling with meaningful API responses

### 🛠 Tech Stack
- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- REST APIs

### 🧠 Core Logic
The system checks time overlap before booking interviews and uses transactional locking
to prevent race conditions during concurrent bookings.

### 📦 Modules
- Interviewer Management
- Candidate Management
- Interview Slot Management
- Interview Booking & Status Handling

### 📈 Use Case
Designed for companies to manage interview scheduling efficiently without manual checks
or conflicts.
