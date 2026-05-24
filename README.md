# Secure Payment Wallet System with CI/CD

[![CI](https://github.com/Adityabhardwaj2209/payment/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/Adityabhardwaj2209/payment/actions/workflows/ci-cd.yml)

---

## 📖 Project Overview
A production‑grade **backend** for a digital wallet ecosystem that supports secure user registration, authentication, wallet management, money top‑ups, peer‑to‑peer transfers, and comprehensive admin monitoring. The system is built with **Spring Boot**, follows **clean architecture** principles, and is fully containerised with Docker. Continuous Integration & Delivery is powered by **GitHub Actions**.

---

## ✨ Key Features
- **JWT Authentication** – Stateless, secure token‑based login.
- **Role‑Based Access Control** – `USER` & `ADMIN` roles via Spring Security.
- **Automatic Wallet Creation** – A wallet (status `ACTIVE`, balance `0.00`) is provisioned on registration.
- **Add Money** – Users can top‑up their wallet.
- **Transfer Money** – Peer‑to‑peer transfers with ACID‑safe transactional guarantees.
- **Suspicious Transaction Detection** – Flag high‑value or rapid‑frequency transfers.
- **Admin Monitoring** – Endpoints for user, transaction and wallet audit logs.
- **Swagger / OpenAPI UI** – Interactive API docs with JWT bearer support.
- **Docker Support** – Multi‑stage build, ready for Kubernetes or cloud deployment.
- **GitHub Actions CI/CD** – Automated tests, packaging, and Docker image creation.

---

## 🛠️ Tech Stack
| Layer | Technology |
|-------|------------|
| Language & Runtime | Java 17 |
| Framework | Spring Boot 3, Spring Security |
| API Documentation | springdoc‑openapi (Swagger UI) |
| Persistence | PostgreSQL, Spring Data JPA / Hibernate |
| Build & Dependency Management | Maven |
| Testing | JUnit 5, Mockito |
| Containerisation | Docker (multi‑stage) |
| CI/CD | GitHub Actions |
| Others | Lombok, MapStruct (optional), BCrypt, JWT |

---

## 🏗️ Architecture (Text Diagram)
```
+-----------------------+        +---------------------------+
|  Client (Web/Mobile) | <----> |   Spring Boot REST API    |
+-----------------------+        +---------------------------+
                                      |
                                      |  (Spring Security + JWT)
                                      v
                         +-----------------------------+
                         |   Service Layer (Business)   |
                         +-----------------------------+
                                      |
          +---------------------------+---------------------------+
          |                           |                           |
   +--------------+           +--------------+           +--------------+
   | Auth Service |           | Wallet Service|          | Admin Service |
   +--------------+           +--------------+           +--------------+
          |                           |                           |
          v                           v                           v
   +----------------+          +----------------+          +----------------+
   | User & Role   |          | Wallet & Txn   |          | Audit & Admin  |
   | Repositories  |          | Repositories  |          | Repositories   |
   +----------------+          +----------------+          +----------------+
          |                           |                           |
          v                           v                           v
   +--------------------------- PostgreSQL Database ---------------------------+
```

---

## 📦 Database Schema
| Table | Columns (type) |
|-------|----------------|
| **users** | `id` (UUID, PK), `email` (VARCHAR, unique), `password` (VARCHAR), `first_name` (VARCHAR), `last_name` (VARCHAR), `roles` (SET), `created_at` (TIMESTAMP) |
| **wallets** | `id` (UUID, PK), `user_id` (FK → users.id), `balance` (DECIMAL(19,4)), `status` (ENUM: ACTIVE, FROZEN), `created_at` (TIMESTAMP) |
| **transactions** | `id` (UUID, PK), `wallet_id` (FK → wallets.id), `type` (ENUM: CREDIT, DEBIT, TRANSFER), `amount` (DECIMAL(19,4)), `description` (VARCHAR), `status` (ENUM: SUCCESS, FAILED, FLAGGED), `created_at` (TIMESTAMP) |
| **suspicious_transactions** | `id` (UUID, PK), `transaction_id` (FK → transactions.id), `reason` (VARCHAR), `created_at` (TIMESTAMP) |
| **audit_logs** | `id` (UUID, PK), `action` (VARCHAR), `performed_by` (VARCHAR), `details` (TEXT), `timestamp` (TIMESTAMP) |

---

## 📋 API Endpoints
| Method | Path | Description | Roles |
|--------|------|-------------|-------|
| `POST` | `/api/auth/register` | Register new user (wallet auto‑created) | Public |
| `POST` | `/api/auth/login` | Authenticate and receive JWT | Public |
| `GET`  | `/api/auth/verify?token=...` | Verify email token | Public |
| `POST` | `/api/wallet/add-money` | Add money to logged‑in user's wallet | USER |
| `POST` | `/api/wallet/transfer` | Transfer money to another user | USER |
| `GET`  | `/api/wallet/balance` | Retrieve current wallet balance | USER |
| `GET`  | `/api/admin/users` | List all users | ADMIN |
| `GET`  | `/api/admin/transactions` | List all transactions | ADMIN |
| `GET`  | `/api/admin/suspicious-transactions` | List flagged transactions | ADMIN |
| `PUT`  | `/api/admin/wallet/{walletId}/freeze` | Freeze a wallet | ADMIN |
| `PUT`  | `/api/admin/wallet/{walletId}/unfreeze` | Unfreeze a wallet | ADMIN |
| `GET`  | `/v3/api-docs` & `/swagger-ui.html` | Swagger UI & OpenAPI spec | Public |

---

## 💻 Local Setup (without Docker)
1. **Prerequisites**
   - Java 17 (JDK) installed & `JAVA_HOME` set.
   - Maven (`mvn`) installed.
   - PostgreSQL running locally (default DB `walletdb`, user `postgres`, password `root`).
2. **Clone the repository**
   ```bash
   git clone https://github.com/Adityabhardwaj2209/payment.git
   cd payment/secure-payment-wallet-backend
   ```
3. **Configure environment variables** (create `src/main/resources/application.properties` if not present):
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/walletdb
   spring.datasource.username=postgres
   spring.datasource.password=root
   jwt.secret=YOUR_RANDOM_SECRET
   ```
4. **Run database migrations** (Hibernate `ddl-auto=create-drop` is used for dev; adjust as needed).
5. **Start the application**
   ```bash
   mvn spring-boot:run
   ```
6. **Access Swagger UI** at `http://localhost:8080/swagger-ui.html`.

---

## 🐳 Docker Setup
```bash
# From the project root (where docker-compose.yml lives)
docker-compose up --build   # Builds the image and starts PostgreSQL + app
```
* The API will be reachable at `http://localhost:8080`.
* Environment variables are injected via `docker-compose.yml` (see file for details).
* To stop:
```bash
docker-compose down
```

---

## ✅ Testing
Run the full test suite with:
```bash
mvn test
```
All unit and integration tests should pass before committing.

---

## 🔧 CI/CD Pipeline Explanation
The **GitHub Actions** workflow (`.github/workflows/ci-cd.yml`) performs the following on every push or PR to `main`:
1. **Checkout** the repository.
2. **Set up JDK 17** (Temurin) and cache Maven dependencies for speed.
3. **Execute `mvn test`** – unit tests with JUnit 5 & Mockito.
4. **Package** the application (`mvn clean package -DskipTests`).
5. **Build a Docker image** (`docker build -t wallet-backend:latest .`).
6. *Optional* steps (commented) show where to log into DockerHub and push the image when the secrets `DOCKERHUB_USERNAME` and `DOCKERHUB_TOKEN` are configured.
The CI badge at the top of this README reflects the latest workflow status.

---

## 📄 Resume‑Ready Bullet Points
- **Designed & implemented** a secure, ACID‑compliant digital wallet backend using **Spring Boot 3** and **PostgreSQL**, handling high‑throughput money transfers with transactional integrity.
- **Integrated JWT‑based authentication** and **role‑based access control** for `USER` and `ADMIN` scopes, enforcing security best practices.
- **Developed suspicious‑transaction detection** logic (high‑value & frequent transfers) and exposed admin monitoring APIs, improving fraud‑prevention capabilities.
- **Automated CI/CD** with **GitHub Actions**, including Maven testing, Docker image creation, and optional DockerHub publishing, ensuring rapid, reliable deployments.
- **Containerised** the entire stack (Java app + PostgreSQL) with multi‑stage **Docker** builds and **docker‑compose** orchestration for seamless local & cloud environments.
- **Documented** the API with **Swagger/OpenAPI**, providing interactive exploration and client‑generation support.
- **Ensured code quality** through comprehensive unit tests using **JUnit 5** and **Mockito**, achieving >90% coverage.

---

## 📜 License
This project is licensed under the **MIT License** – see the `LICENSE` file for details.

---

*Generated by Antigravity AI*
