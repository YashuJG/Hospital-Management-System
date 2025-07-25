# Hospital Management System

A comprehensive web-based Hospital Management System built with Java Spring Boot and Thymeleaf. This project helps hospitals manage patients, doctors, appointments, billing, medical records, and more with a user-friendly interface.

## 🚀 Features
- User authentication (Admin, Doctor, Receptionist, Patient)
- Patient registration and management
- Doctor management
- Appointment booking and management
- Medical records and prescriptions
- Billing and payment tracking
- Feedback system
- Password reset via email
- Responsive UI with Bootstrap

## 🛠️ Technologies Used
- Java 17+
- Spring Boot
- Spring Security
- Thymeleaf
- Spring Data JPA (Hibernate)
- MySQL (or H2 for testing)
- Bootstrap 4
- Maven

## ⚙️ Setup Instructions
1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/hospital-management-system.git
   cd hospital-management-system
   ```
2. **Configure the database:**
   - Update `src/main/resources/application.properties` with your MySQL credentials.
   - For testing, you can use the default H2 configuration.
3. **Build the project:**
   ```bash
   ./mvnw clean install
   ```
4. **Run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```
5. **Access the application:**
   - Open your browser and go to: [http://localhost:8080](http://localhost:8080)

## 👤 Default Roles & Logins
- **Admin:** Created via DataInitializer or manually in DB
- **Doctor/Receptionist/Patient:** Register via UI or added by admin

## 📂 Project Structure
- `src/main/java/com/hms/hospital_management_system/` - Main source code
- `src/main/resources/templates/` - Thymeleaf HTML templates
- `src/main/resources/static/` - Static assets (CSS, JS, images)

## 📧 Contact
For any queries or support, please contact: uwmabtw@gmail.com

---

> **Note:** This project is for educational/demo purposes. Please customize and secure before deploying to production. 
