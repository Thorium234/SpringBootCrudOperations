# Spring Boot CRUD Operations - Product Management System

A full-stack web application built with Spring Boot that allows users to manage products with complete CRUD (Create, Read, Update, Delete) functionality. Features include user authentication, image uploads, and a responsive Bootstrap UI.

## Features

- **User Authentication**: Secure registration and login system using Spring Security
- **Product Management**: Complete CRUD operations for products
- **Image Upload**: Upload and display product images
- **User Profile**: View and edit user profile information
- **Responsive Design**: Clean, professional UI using Bootstrap 5
- **MySQL Database**: Persistent data storage

## Technologies Used

- **Backend**: Spring Boot 3.5.6, Spring Security, Spring Data JPA
- **Frontend**: Thymeleaf, Bootstrap 5, HTML/CSS
- **Database**: MySQL/MariaDB
- **Build Tool**: Maven
- **Java Version**: 17

## Prerequisites

Before you begin, ensure you have the following installed:

- Java JDK 17 or higher
- MySQL or MariaDB
- Maven 3.6+
- Git

## Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/Thorium234/SpringBootCrudOperations.git
cd SpringBootCrudOperations
```

### 2. Configure Database

Create a MySQL database:

```sql
CREATE DATABASE crud_db;
```

Update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/crud_db
spring.datasource.username=root
spring.datasource.password=your_password_here

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

spring.web.resources.static-locations=classpath:/static/,file:public/
```

### 3. Build the Project

```bash
mvn clean install
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Database Setup

### Create Tables

The application will automatically create tables on first run due to `spring.jpa.hibernate.ddl-auto=update`.

### Insert Sample Data

**Products:**

```sql
INSERT INTO products (brand, category, created_at, description, image_file_name, name, price) VALUES
('Dell', 'Laptops', NOW(), 'High-performance laptop with Intel Core i7 processor, 16GB RAM, and 512GB SSD.', 'lap1.avif', 'Dell XPS 15', 1299.99),
('HP', 'Laptops', NOW(), 'Lightweight business laptop featuring AMD Ryzen 5, 8GB RAM, and fast boot times.', 'lap2.avif', 'HP ProBook 450', 899.99),
('Lenovo', 'Laptops', NOW(), 'Versatile 2-in-1 laptop with touchscreen display, Intel Core i5, and 256GB storage.', 'lap3.avif', 'Lenovo Yoga 7i', 1099.99);
```

**Test Users:**

```sql
INSERT INTO users (username, email, password, full_name, phone, created_at) VALUES
('john_doe', 'john@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'John Doe', '+254712345678', NOW()),
('jane_smith', 'jane@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Jane Smith', '+254723456789', NOW());
```

**Default Password for Test Users**: `password123`

## How to Use

### 1. Register a New Account

- Navigate to `http://localhost:8080/register`
- Fill in the registration form:
  - Username (3-20 characters, required)
  - Email (valid email format, required)
  - Full Name (optional)
  - Phone (optional)
  - Password (minimum 6 characters, required)
  - Confirm Password (must match password)
- Click "Register"

### 2. Login

- Go to `http://localhost:8080/login`
- Enter your username and password
- Click "Login"

**Or use test credentials:**
- Username: `john_doe`
- Password: `password123`

### 3. Manage Products

After logging in, you'll be redirected to the products page where you can:

**View Products:**
- See all products in a table with images, details, and prices

**Create Product:**
- Click "Create products" button
- Fill in product details:
  - Name (required)
  - Brand (required)
  - Category (required)
  - Price (required, numeric)
  - Description (required)
  - Image File (required, image formats only)
- Click "Create"

**Edit Product:**
- Click "Edit" button on any product
- Update the desired fields
- Optionally upload a new image
- Click "Update"

**Delete Product:**
- Click "Delete" button on any product
- Confirm deletion in the popup

### 4. Manage Profile

**View Profile:**
- Click "Profile" in the navigation bar
- View your account information

**Edit Profile:**
- Click "Edit Profile" button
- Update your details:
  - Username
  - Email
  - Full Name
  - Phone
  - Password (optional - leave blank to keep current password)
- Click "Update Profile"

### 5. Logout

- Click "Logout" in the navigation bar

## Project Structure

```
SpringBootCrudOperations/
├── src/
│   ├── main/
│   │   ├── java/com/thorium234/CRUD/
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── controllers/
│   │   │   │   ├── AuthController.java
│   │   │   │   └── ProductsController.java
│   │   │   ├── models/
│   │   │   │   ├── Product.java
│   │   │   │   ├── ProductDto.java
│   │   │   │   ├── User.java
│   │   │   │   └── UserDto.java
│   │   │   ├── services/
│   │   │   │   ├── CustomUserDetailsService.java
│   │   │   │   ├── ProductsRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   └── CrudApplication.java
│   │   └── resources/
│   │       ├── templates/
│   │       │   ├── auth/
│   │       │   │   ├── login.html
│   │       │   │   ├── register.html
│   │       │   │   ├── profile.html
│   │       │   │   └── editProfile.html
│   │       │   └── products/
│   │       │       ├── index.html
│   │       │       ├── CreateProduct.html
│   │       │       └── EditProduct.html
│   │       └── application.properties
│   └── test/
├── public/images/
├── pom.xml
└── README.md
```

## Security Features

- Password encryption using BCrypt
- CSRF protection enabled
- Session-based authentication
- Secure password validation (minimum 6 characters)
- Protected routes (authentication required)

## API Endpoints

### Authentication
- `GET /register` - Registration page
- `POST /register` - Register new user
- `GET /login` - Login page
- `POST /login` - Authenticate user
- `POST /logout` - Logout user

### Products
- `GET /products` - List all products
- `GET /products/create` - Create product form
- `POST /products/create` - Save new product
- `GET /products/edit?id={id}` - Edit product form
- `POST /products/edit` - Update product
- `GET /products/delete?id={id}` - Delete product

### User Profile
- `GET /profile` - View profile
- `GET /profile/edit` - Edit profile form
- `POST /profile/edit` - Update profile

## Troubleshooting

**Port 8080 already in use:**
```bash
# Kill the process using port 8080
sudo lsof -ti:8080 | xargs kill -9
```

**Database connection error:**
- Verify MySQL is running: `sudo systemctl status mysql`
- Check database credentials in `application.properties`
- Ensure database exists: `CREATE DATABASE crud_db;`

**Image upload issues:**
- Check `public/images/` directory exists
- Verify file size limits in `application.properties`
- Ensure proper file permissions

## Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature-name`
3. Commit changes: `git commit -m 'Add feature'`
4. Push to branch: `git push origin feature-name`
5. Submit a pull request

## License

This project is open source and available under the MIT License.

## Author

**Thorium234**
- GitHub: [@Thorium234](https://github.com/Thorium234)
- Repository: [SpringBootCrudOperations](https://github.com/Thorium234/SpringBootCrudOperations)

## Acknowledgments

- Spring Boot Documentation
- Bootstrap 5
- Thymeleaf Template Engine
- Spring Security

---

For questions or support, please open an issue on GitHub.
