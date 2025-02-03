# DevJobsHub 🌐

DevJobsHub is a web application that allows companies to post job offers for programmers and for candidates to apply for those offers. The application also offers functionalities related to custom candidate surveys created by recruiters.

## Tech Stack 🚀

**Backend:** Spring Boot, Java, PostgreSQL, Hibernate

**Frontend:** React,  TypeScript, Tailwind, Redux, HTML, CSS


## Backend - Short version of the description

### 1. User Management 💻

- **User registration:** Possibility to register new users (recruiters and candidates).
- **Login:** User login using email address and password.
- **Updating profile data:** Users can update their personal information.
- **Change password:** Possibility to change user password.
- **Downloading user profile:** Possibility to download user profile data such as name, surname, email, company information and administrator status.


### 2. Job Offer Management 💼

- **Creating job offers:** Recruiters can create new job offers by specifying:
  - Basic informations such as the title of the offer
  - Expiration date
  - Technological requirements
  - Experience level
  - Work mode (remote, hybrid, stationary)
  - Salary details
  - Project description
  - Duties description
  - Benefits description
  - Requirements, responsibilities in bullet points
- **Job offer update:** Possibility to edit existing job offers.
- **Deleting job offers:** Recruiters can remove job offers.
- **Search for offers:** Possibility to search for offers with filtering by various criteria (e.g. location, level of experience, technology).
- **Adding to favorites:** Users can add offers to their favorites.
- **Removing from favorites:** Users can remove offers from their favorites.
- **Managing recruiters assigned to offers:** After creating a job offer, a recruiter can add other recruiters to it so that they can manage the recruitment for the position.

### 3. Job Application Management 📝

- **Applying for offers:** Candidates can apply for job offers by attaching:
    - URL to CV (CV will be stored in Amazon S3)
    - Answers to candidate survey questions (open, single-choice, multiple-choice questions)
- **Browse applications:** Recruiters can browse submitted applications.
- **Managing favorite applications:** Recruiters can mark applications as favorites for easier management.

### 4. Authentication & Security 🔐

- **JWT Authentication:** User authentication using JWT tokens.
- **Endpoint Security:** Securing endpoints requiring authentication.

## Frontend is under development
