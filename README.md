# Smart FD 

**Secure Today, Stronger Tomorrow**

A comprehensive digital platform for managing Fixed Deposits with real-time interest calculations, premature withdrawal handling, and support ticket management.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [System Architecture](#system-architecture)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Team](#team)
- [Future Enhancements](#future-enhancements)

---

## 🎯 Overview

Smart FD is a secure, transparent, and user-friendly digital platform designed to streamline Fixed Deposit management. The system enables users to register, book FDs, monitor investments with real-time interest calculations, handle premature withdrawals with automatic penalty computation, and manage support queries—all through an intuitive interface.

### Key Objectives

- Provide secure token-based authentication
- Enable seamless FD booking with predefined schemes
- Auto-calculate maturity dates and accrued interest
- Handle premature withdrawals with penalty computation
- Facilitate customer support through integrated ticketing system

---

## ✨ Features

### User Features
- **Secure Authentication**: Token-based registration and login system
- **FD Management**: Book, view, and track multiple fixed deposits
- **Interest Calculator**: Calculate returns with simple and compound interest
- **Premature Withdrawal**: Break FDs early with automatic penalty calculation and preview
- **Support System**: Raise and track FD-related queries through tickets
- **Dashboard**: Visual representation of investments and portfolio summary

### Admin Features
- **FD Monitoring**: View and manage all customer fixed deposits
- **Status Management**: Update FD status (ACTIVE, MATURED, BROKEN)
- **Ticket Management**: View, respond to, and close customer support tickets
- **Analytics Dashboard**: Overview of platform metrics and statistics

---

## 🛠️ Tech Stack

### Backend
- **Framework**: Spring Boot
- **Language**: Java 17+
- **Database**: PostgreSQL
- **Security**: JWT (JSON Web Tokens)
- **Testing**: JUnit, Mockito

### Frontend
- **Framework**: Vue.js 3
- **Build Tool**: Vite
- **State Management**: Vuex
- **Routing**: Vue Router
- **Styling**: Tailwind CSS
- **HTTP Client**: Axios
- **Testing**: Vitest

### Database
- **RDBMS**: PostgreSQL
- **ORM**: Spring Data JPA

---

## 🏗️ System Architecture

### Backend Architecture
```
├── Controllers (REST API Layer)
│   ├── AuthController
│   ├── FixedDepositController
│   ├── SupportTicketController
│   └── AdminController
├── Services (Business Logic)
│   ├── UserService
│   ├── FixedDepositService
│   ├── AccruedInterestService
│   └── SupportTicketService
├── Repositories (Data Access)
│   ├── UserRepository
│   ├── FixedDepositRepository
│   └── SupportTicketRepository
└── Entities (Data Models)
    ├── User
    ├── FixedDeposit
    └── SupportTicket
```

### Frontend Architecture
```
├── Components (Reusable UI)
│   ├── Core (Navbar, Sidebar, Footer)
│   ├── FD Management (FDCard, SchemeDropdown)
│   ├── Charts (BarChart, PieChart, Donut)
│   └── Admin (AdminInterface, AdminSidebar)
├── Views (Pages)
│   ├── Public (Home, Login, Register)
│   ├── User (Dashboard, MyFDs, BookFD, Calculator, Support)
│   └── Admin (AdminDashboard, AdminFDs, AdminTickets)
├── Router (Navigation & Guards)
├── Store (Vuex State Management)
└── Services (API Integration)
```

---

## 🚀 Getting Started

### Prerequisites

#### Backend
- Java Development Kit (JDK) 17 or above
- Maven 3.6+
- PostgreSQL 12+
- Git

#### Frontend
- Node.js 18+
- npm or yarn

---

### Backend Setup

1. **Clone the repository**
```bash
git clone https://github.com/sarthakkurothe/Digital-Fixed-Deposit-System.git
cd Digital-Fixed-Deposit-System/backend
```

2. **Configure PostgreSQL**

Create a database:
```sql
CREATE DATABASE fdms;
```

Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/fdms
spring.datasource.username=postgres
spring.datasource.password=your_password
```

3. **Build the project**
```bash
mvn clean install
```

4. **Run the application**
```bash
mvn spring-boot:run
```

The backend will start at: `http://localhost:8080`

---

### Frontend Setup

1. **Navigate to frontend directory**
```bash
cd Digital-Fixed-Deposit-System/frontend
```

2. **Install dependencies**
```bash
npm install
```

3. **Run development server**
```bash
npm run dev
```

The frontend will start at: `http://localhost:5173`

---

### Database Setup

The database schema is automatically created by Spring Boot JPA on first run. It includes three main tables:

- **users**: Customer and admin information
- **fixed_deposits**: FD records with status and interest details
- **support_tickets**: Customer query tracking

---

## 📚 API Documentation

### Authentication Endpoints

#### Register User
```http
POST /auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@gmail.com",
  "password": "Password*123",
  "dateOfBirth": "1994-09-12"
}
```

#### Login
```http
POST /auth/login
Content-Type: application/json

{
  "email": "john@gmail.com",
  "password": "Password*123"
}
```

#### Get Current User
```http
GET /auth/me
Authorization: Bearer {token}
```

### Fixed Deposit Endpoints

#### Book FD
```http
POST /fd/book
Authorization: Bearer {token}

{
  "user_id": 1,
  "amount": 50000,
  "interest_rate": 6.5,
  "tenure_months": 12
}
```

#### View User FDs
```http
GET /fd/user/{userId}
Authorization: Bearer {token}
```

#### Break FD Preview
```http
GET /fd/{fdId}/break-preview
Authorization: Bearer {token}
```

#### Break FD
```http
POST /fd/{fdId}/break
Authorization: Bearer {token}
```

#### View Accrued Interest
```http
GET /fd/{fdId}/interest
Authorization: Bearer {token}
```

### Support Ticket Endpoints

#### Create Ticket
```http
POST /support
Authorization: Bearer {token}

{
  "userId": 1,
  "fdId": 1,
  "subject": "Issue with fixed deposit",
  "description": "FD status didn't update after maturity"
}
```

#### Get User Tickets
```http
GET /support/user/{userId}
Authorization: Bearer {token}
```

### Admin Endpoints

#### Get All FDs
```http
GET /admin/fds
Authorization: Bearer {admin-token}
```

#### Update FD Status
```http
PUT /admin/fd/{id}
Authorization: Bearer {admin-token}

{
  "status": "MATURED"
}
```

#### Get All Tickets
```http
GET /admin/tickets
Authorization: Bearer {admin-token}
```

#### Close Ticket
```http
POST /admin/tickets/{id}
Authorization: Bearer {admin-token}

{
  "response": "Issue resolved"
}
```

---

## 🧪 Testing

### Backend Tests (JUnit + Mockito)

Run all tests:
```bash
mvn test
```

**Test Coverage:**
- Controller Tests: Auth, Admin, User, Fixed Deposit
- Service Tests: Interest calculation, FD management, User operations, Ticket handling
- Security Tests: Password hashing, Token validation

### Frontend Tests (Vitest)

Run all tests:
```bash
npm run test
```

**Test Coverage:**
- Component Tests: BookFD, BreakFD, Calculator, Login, Register
- Validation Tests: Input validation, Interest calculations, Business logic
- Integration Tests: API calls, State management, Route guards

---

## 👥 Team

**Team Name**: Zeta Dreamers

| Name | Role |
|------|------|
| **Sarthak Kurothe** | Team Lead - Support Ticket Module |
| **Shaik Mohammad Anees** | User Registration & Authentication |
| **Lakshmi Mrudula** | Book & View Fixed Deposits |
| **Srijith** | Interest Calculation & FD Status Management |
| **Rithika** | Premature Withdrawal & Penalty Computation |

---

## 🔮 Future Enhancements

### Security
- Two-Factor Authentication (2FA)
- Advanced fraud detection and monitoring
- Comprehensive audit trail logging

### Banking Integration
- Payment gateway integration (UPI/Net Banking)
- Direct banking API connections
- Real-time balance updates

### Notifications
- Email/SMS alerts for FD maturity
- Push notifications for important events
- Customizable alert preferences

### Compliance
- KYC/AML integration
- RBI guideline compliance
- Automated audit reporting

---

## 📄 License

This project is developed as part of an academic/training initiative.

---

## 🔗 References

- [HDFC FD Interest Rates](https://www.hdfcbank.com/personal/resources/rates#/fixed-deposit-interest-rate-less-than-5-cr)
- [Groww FD Calculator](https://groww.in/calculators/fd-calculator)
- [HDFC Premature Withdrawal Terms](https://www.hdfcbank.com/personal/useful-links/important-messages/changes-in-premature-withdrawal-terms-and-conditions)

---

## 📞 Support

For any queries or issues, please raise a support ticket through the application or contact the development team.

**Built with ❤️ by Team Zeta Dreamers**
