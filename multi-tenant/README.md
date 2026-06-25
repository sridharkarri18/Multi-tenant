# Multi-Tenant Feature Flag Management System

A highly scalable, multi-tenant Spring Boot application for managing feature flags across different organizations. This project features a robust JWT-based role-based access control (RBAC) backend and a beautiful, fully integrated Glassmorphism vanilla HTML/CSS/JS frontend.

## 🌟 Key Features

* **True Multi-Tenancy:** Data is strictly segregated by Organization. Users and Feature Flags are securely tied to their respective tenants.
* **Role-Based Access Control (RBAC):**
  * **Super Admin:** Master control. Can create new organizations, view user counts, and provision Organization Admins and End Users globally.
  * **Organization Admin:** Tenant-level control. Can create, read, and delete feature flags specific to their organization, and provision End Users for their org.
  * **End User:** Can securely query feature flags for their organization to determine UI/feature states.
* **Dynamic Frontend UI:**
  * Beautiful Glassmorphism design utilizing CSS variables and modern layout techniques.
  * Single-Page Application (SPA) feel using Vanilla JavaScript.
  * Role-based UI rendering (components instantly morph based on the logged-in user's JWT).
  * **Dynamic Theme Reactions:** A built-in easter egg allows End Users to create a `dark-mode` feature flag. The UI automatically evaluates this via an invisible API call upon login and physically flips the site from Dark to Light mode based on the flag's state!
* **Secure JWT Authentication:** Stateless architecture using signed JSON Web Tokens.
* **Robust Exception Handling:** Graceful error handling (e.g., throwing 400s instead of 500s for duplicate flags) integrated globally using `@ControllerAdvice`.

---

## 🛠️ Technology Stack

* **Backend:** Java, Spring Boot 3, Spring Web, Spring Security, Spring Data JPA
* **Database:** PostgreSQL (Hosted on Supabase, connecting via IPv4 Pooler)
* **Authentication:** JSON Web Tokens (io.jsonwebtoken)
* **Frontend:** Vanilla HTML, CSS, JavaScript (Zero NPM dependencies, bundled into `src/main/resources/static/`)

---

## 🚀 Getting Started

### 1. Prerequisites
* Java 17+
* Maven
* A Supabase PostgreSQL instance (Ensure your `application.properties` uses the Supabase Pooler Port `6543` and includes `?pgbouncer=true`).

### 2. Configuration
Update your `src/main/resources/application.properties` with your Supabase credentials:

```properties
spring.datasource.url=jdbc:postgresql://<SUPABASE_HOST>:6543/postgres?pgbouncer=true
spring.datasource.username=postgres.[YOUR_PROJECT_REF]
spring.datasource.password=[YOUR_DB_PASSWORD]
spring.jpa.hibernate.ddl-auto=update

# JWT Secret (Keep this secure in production!)
jwt.secret=mySuperSecretKeyForJwtAuthenticationThatIsLongEnough
jwt.expiration=86400000
```

### 3. Running the Application
You can run the application using Maven:
```bash
./mvnw spring-boot:run
```
Once the application starts, it will automatically connect to Supabase, run Hibernate to generate the necessary tables, and run the `DatabaseSeeder` to populate the default test data.

### 4. Accessing the Dashboard
Open your browser and navigate to:
```
http://localhost:8080/
```

---

## 🔑 Default Test Credentials

Upon startup, the `DatabaseSeeder` injects the following accounts into the database for immediate testing:

| Role | Email | Password | Access Level |
| :--- | :--- | :--- | :--- |
| **Super Admin** | `superadmin@example.com` | `admin123` | Global |
| **Org Admin (Netflix)**| `netflix_admin@example.com`| `admin123` | Netflix Tenant |
| **End User (Netflix)** | `netflix_user@example.com` | `user123` | Netflix Tenant |

*(A secondary "Spotify" tenant is also created with similar credentials).*

---

## 📡 Core API Endpoints

### Authentication
* `POST /api/auth/login` - Authenticate and retrieve a JWT.
* `POST /api/auth/signup/org-admin` - Create an Org Admin (Super Admin only).
* `POST /api/auth/signup/user` - Create an End User (Super/Org Admin only).

### Super Admin
* `GET /api/superadmin/organizations` - List all tenants.
* `POST /api/superadmin/organizations` - Create a new tenant.
* `GET /api/superadmin/organizations/{orgId}/users` - View all users provisioned in a tenant.

### Organization Admin
* `GET /api/orgadmin/flags` - List all feature flags for the tenant.
* `POST /api/orgadmin/flags` - Create a new feature flag.
* `DELETE /api/orgadmin/flags/{featureKey}` - Delete a feature flag by its unique key.
* `POST /api/orgadmin/users` - Provision an End User securely into the admin's tenant.

### End User
* `GET /api/user/flags/check?featureKey={key}` - Evaluate a flag's status.

---

## 🎨 UI Workflows to Test

1. **The Master Control Test:** Log in as `superadmin@example.com`. Notice the UI completely replaces itself with the Super Admin panel. Create a new organization, switch to the "Manage Users" tab, and easily provision users into that new organization using the dynamic dropdowns.
2. **The Multi-Tenant Barrier Test:** Log in as `netflix_admin@example.com`. Create a flag called `new-checkout`. Then log in as `spotify_admin@example.com` and notice you **cannot** see or delete the Netflix flag!
3. **The Dynamic Theme Test:** Log in as `netflix_admin@example.com` and create a flag called `dark-mode` (set to `true`). Log out, then log in as `netflix_user@example.com`. Because the flag evaluates to true, the UI forces the theme to Light Mode upon login!

---

*Built with ❤️ utilizing Spring Boot and Vanilla Web Technologies.*
