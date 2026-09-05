# Smart Event Management System

A web-based platform that allows organizers to create, manage, promote, and monitor events while enabling attendees to register and participate online.

Built with **Java Spring Boot**, **Spring Security**, **Hibernate/JPA**, **Thymeleaf**, and **Bootstrap 5**.

## Tech Stack

| Layer        | Technology                                          |
|--------------|-----------------------------------------------------|
| Frontend     | HTML5, CSS3, JavaScript, Bootstrap 5, Thymeleaf    |
| Backend      | Java 17+, Spring Boot 3.2, Spring MVC, Spring Security |
| ORM          | Hibernate / Spring Data JPA                         |
| Database     | H2 (dev/demo, zero-config) / MySQL (production)     |
| Build Tool   | Maven                                               |
| QR Codes     | Google ZXing                                        |

## Features

### User Roles
1. **Admin** — Full system management (users, events, dashboard)
2. **Event Organizer** — Create, edit, delete events; view stats
3. **Participant** — Register for events, book tickets, leave feedback

### Pages
- Home, About Us, Contact, Gallery
- Events listing with category filters (Music, College, Sports, Conferences, Workshops)
- Event Details with feedback/ratings
- Registration / Login (Spring Security)
- Organizer Dashboard (CRUD events, stats)
- Participant Dashboard (registrations, tickets)
- Ticket Booking with QR code generation
- Admin Panel (manage users & events)

### Advanced Features
- **QR Code Tickets** — Each booked ticket gets a unique QR code
- **Feedback System** — Rate and review events (1–5 stars)
- **Attendance Tracking** — Ticket check-in support
- **Role-based Security** — Spring Security with BCrypt password encoding
- **Seed Data** — Demo users and events loaded on startup

## Database Tables
- **users** — User accounts (admin, organizer, participant)
- **events** — Event listings
- **registrations** — Event registrations
- **tickets** — Booked tickets with QR codes
- **payments** — Payment records
- **feedback** — Event ratings and reviews

## Prerequisites

- **Java 17** or higher (Java 21 also works)
- **Maven 3.6+** (or use the included Maven wrapper)
- **MySQL 8** (optional — H2 is used by default for zero-config startup)

## Running the Application

### Option 1: Using Maven (recommended)

```bash
# Navigate to the project directory
cd eventmanagementsystem

# Build and run
mvn spring-boot:run
```

### Option 2: Build JAR and run

```bash
mvn clean package -DskipTests
java -jar target/event-management-system-1.0.0.jar
```

### Option 3: Using IDE (IntelliJ IDEA / Eclipse)

1. Open the project as a Maven project.
2. Run the `EventManagementApplication.java` main class.

The application starts at **http://localhost:8080**

## Switching to MySQL

1. Open `src/main/resources/application.properties`
2. Comment out the H2 section
3. Uncomment the MySQL section (or use `application-mysql.properties`)
4. Update your MySQL username and password
5. Restart the application

```bash
# Or run with MySQL profile:
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

## Demo Accounts

The app seeds three demo accounts on first startup. All use the password `admin123`:

| Role         | Email                   |
|--------------|-------------------------|
| Admin        | admin@eventhub.com      |
| Organizer    | organizer@eventhub.com  |
| Participant   | user@eventhub.com       |

## H2 Console

When running with H2 (default), you can access the database console at:
**http://localhost:8080/h2-console**

- JDBC URL: `jdbc:h2:mem:eventdb`
- Username: `sa`
- Password: *(leave blank)*

## Project Structure

```
eventmanagementsystem/
├── pom.xml
├── .gitignore
├── README.md
├── src/
│   ├── main/
│   │   ├── java/com/eventmgmt/
│   │   │   ├── EventManagementApplication.java
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── ThymeleafConfig.java
│   │   │   │   └── DataInitializer.java
│   │   │   ├── controller/
│   │   │   │   ├── HomeController.java
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── EventController.java
│   │   │   │   ├── OrganizerController.java
│   │   │   │   ├── ParticipantController.java
│   │   │   │   ├── TicketController.java
│   │   │   │   └── AdminController.java
│   │   │   ├── entity/
│   │   │   │   ├── User.java
│   │   │   │   ├── Event.java
│   │   │   │   ├── Registration.java
│   │   │   │   ├── Ticket.java
│   │   │   │   ├── Payment.java
│   │   │   │   └── Feedback.java
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── EventRepository.java
│   │   │   │   ├── RegistrationRepository.java
│   │   │   │   ├── TicketRepository.java
│   │   │   │   ├── PaymentRepository.java
│   │   │   │   └── FeedbackRepository.java
│   │   │   ├── service/
│   │   │   │   ├── UserService.java
│   │   │   │   ├── EventService.java
│   │   │   │   ├── RegistrationService.java
│   │   │   │   └── QrCodeService.java
│   │   │   └── security/
│   │   │       └── CustomUserDetailsService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-mysql.properties
│   │       ├── static/
│   │       │   ├── css/style.css
│   │       │   └── js/main.js
│   │       └── templates/
│   │           ├── layout.html
│   │           ├── index.html
│   │           ├── about.html
│   │           ├── contact.html
│   │           ├── login.html
│   │           ├── register.html
│   │           ├── events.html
│   │           ├── event-details.html
│   │           ├── gallery.html
│   │           ├── ticket-booking.html
│   │           ├── ticket-confirmation.html
│   │           ├── organizer/
│   │           │   ├── dashboard.html
│   │           │   └── event-form.html
│   │           ├── participant/
│   │           │   ├── dashboard.html
│   │           │   └── my-tickets.html
│   │           ├── admin/
│   │           │   ├── dashboard.html
│   │           │   ├── users.html
│   │           │   └── events.html
│   │           └── errors/
│   │               ├── 404.html
│   │               └── 500.html
│   └── test/
│       └── java/com/eventmgmt/
│           └── EventManagementApplicationTests.java
```

## SDGs Supported

- SDG 4 — Quality Education
- SDG 8 — Decent Work and Economic Growth
- SDG 9 — Industry, Innovation and Infrastructure
- SDG 11 — Sustainable Cities and Communities
- SDG 12 — Responsible Consumption and Production
