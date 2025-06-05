SequenceDiagram

title UseCase: Register
User -> Frontend: enter { username, password }
Frontend -> Backend: POST /register { username, password }
Backend -> Database: INSERT into users (username, password)
Database -> Backend: success
Backend -> Backend: generate authToken
Backend -> Database: store authToken
Backend -> Frontend: return authToken
Frontend -> User: display main page

---

title UseCase: Send Friend Request
User -> Frontend: enter { username }
Frontend -> Backend: GET /getUser { username }
Backend -> Database: SELECT \* FROM users WHERE username LIKE { %username% }
Database -> Backend: { users }
Backend -> Frontend: { users.json }
Frontend -> User: display users list
User -> Frontend: selects user
Frontend -> Backend: POST /sendRequest { sender username, recipient username }
Backend -> Database: INSERT INTO friends (sender username, recipient username, status="pending")
Database -> Backend: success
Backend -> Frontend: success
Frontend -> User: "requent sent"

---

title UseCase: Accept Friend Request
User -> Frontend: Click Pending Page
Frontend -> Backend: GET /friendRequest { username }
Backend -> Database: SELECT { friends } WHERE { status = pending } in { username }
Database -> Backend: { users }
Backend -> Frontend: { users.json }
Frontend -> User: Display Pending Page
User -> Frontend: Click Accept/Deny
Frontend -> Backend: POST /friendRequest { friend username, status }
Backend -> Database: UPDATE status TO "Accepted/Deny"; both usernames
Database -> Backend: success
Backend -> Frontend: success
Frontend -> User: Refresh Pending Page

---

title UseCase: Send Message
entryspacing 0.9
User A -> Frontend (User A): Click Friend (Room)
Frontend (User A) -> Backend: GET /getHistory { room_id }
Backend -> Database: SELECT all FROM messages WHERE room_id = room_id
Database --> Backend: return [{ message, sender_id, time }]
Backend --> Frontend (User A): return json
Frontend (User A) --> User A: display messages
User A -> Frontend (User A): type and send message
Frontend (User A) -> Backend: POST /sendMessage { room_id, message, sender_id }
Backend -> Database: INSERT messages (room_id, message, sender_id)
Database --> Backend: success
Backend -->> Socket.IO Server: emit("new_message", { room_id, message, sender_id })
Socket.IO Server -->> Frontend (User B): receive("new_message", { room_id, message, sender_id })
Frontend (User B) --> User B: display incoming message

---

title UseCase: Send Message #2

entryspacing 0.9
participant User A
participant Frontend (User A)

participant User B
participant Frontend (User B)
participant Backend
participant Database
participant Socket.IO Server

User A -> Frontend (User A): Click Friend (Room)
Frontend (User A) -> Backend: GET /getHistory { room_id }
Backend -> Database: SELECT all FROM messages WHERE room_id = room_id
Database --> Backend: return [{ message, sender_id, time }]
Backend --> Frontend (User A): return json
Frontend (User A) --> User A: display messages
User A -> Frontend (User A): type and send message
Frontend (User A) -> Backend: POST /sendMessage { room_id, message, sender_id }
Backend -> Database: INSERT messages (room_id, message, sender_id)
Database --> Backend: success
Backend -->> Socket.IO Server: emit("new_message", { room_id, message, sender_id })
Socket.IO Server -->> Frontend (User B): receive("new_message", { room_id, message, sender_id })
Frontend (User B) --> User B: display incoming message

---

System Architecture

Frontend: React
Backend: Springboot Java
Database: Postgres
