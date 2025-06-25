# 🛒 Market

Market is a full-stack web application that enables users to register, log in, and manage products through a secure authentication system. The backend is built with Spring Boot (Java), and the frontend is developed with React. It supports user roles, JWT authentication, product management, file uploads, and form validation.

---

## 🚀 Features

### 👤 User
- Register with validation
- Secure login with JWT access & refresh tokens

### 📦 Product Management
- Create, update, delete, and list products
- Upload product images (stored on backend)
- Form validation (both frontend and backend)

### 🖼 Image Hosting
- Uploaded product images are accessible through backend routes

---

## 🧩 Technologies Used

### Backend (Spring Boot)
- Spring Web
- Spring Security
- JWT
- Hibernate / JPA
- MySQL
- Validation
- Lombok
- JUnit / Mockito for testing

### Frontend (React)
- [Vite](https://vitejs.dev/) — Fast frontend tooling
- React Hooks
- Axios
- React Router
- CSS / SCSS / [Bootstrap](https://getbootstrap.com/) for styling
- [Fontawesome](https://fontawesome.com/) for icons

---

## ⚙️ Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/MoatazTahri/Market.git
cd Market
```

### 2. Backend setup instructions
#### 1. Create a database in your local MySQL
#### 2. Create .env file in `backend` folder and include :
```
DB_URL=jdbc:mysql://localhost:3306/YOUR_DB_NAME
DB_USERNAME=YOUR_USERNAME
DB_PASSWORD=YOUR_PASSWORD
JWT_SECRET_KEY=YOUR_256_SECRET_KEY
```
#### 3. In your MySQL use a database then execute the script based in `backend/src/main/resources/db/db_schema.sql`,or you can run BackendApplication.java for auto creation via IDE, the server will run at http://localhost:8080/


### 3. Frontend setup instructions
#### 1. Install dependencies
```bash
cd frontend
npm install
```
#### 2. Create .env inside `frontend` folder and include :
``
VITE_API_URL=http://localhost:8080/api/v1
``
#### 3. Run the app with the IDE or through (default port 5173) :
```bash
npm run dev
```

