# Simple, a blog website project (back-end)

## Framework

1. Spring-boot 2.7.7 + Java 8
2. Spring Data JPA + Hibernate
3. Spring Security + JJWT

## Structure
```bash
simple
    ├───constant
    ├───controller
    ├───dto
    │   ├───request
    │   └───response
    ├───entity
    ├───repository
    ├───security
    │   ├───jwt
    │   └───services
    └───service
```

## Endpoints

### Authentication: `/api/auth`
authentication-related endpoints, like login/logout, signup.
#### 1. Login: `/login` POST
Request:
```json
{
  "username": "username",
  "password": "password"
}
```
#### 2. Signup: `/signup` POST
Request: 
```json
{
  "username": "unique username",
  "email": "unique email",
  "password": "password"
}
```
#### 3. Logout: `/logout` POST
empty body

## TODO list

- [ ] redis + like/dislike feature (post and comment)

- [ ] user forget password and reset password

- [ ] manage users

- [ ] exceptions

- [ ] error messages

- [ ] show posts of same tag

- [ ] show posts of same category