# Simple, a blog website project (back-end)

## Framework

1. Spring-boot 2.7.7 + Java 8
2. Spring Data JPA + Hibernate
3. Spring Security + JJWT

## Structure
```bash
simple
    ├───controller
    ├───dto
    │   ├───request
    │   └───response
    ├───entity
    ├───exception
    ├───repository
    ├───security
    │   └───jwt
    ├───service
    └───util
```

## Endpoints

### Authentication: `/api/auth`
authentication-related endpoints, like login/logout, signup.
#### Login: `/login` POST
Request:
```json
{
  "username": "username",
  "password": "password"
}
```
#### Signup: `/signup` POST
Request: 
```json
{
  "username": "unique username",
  "email": "unique email",
  "password": "password",
  "website": "optional",
  "description": "optional"
}
```
#### Logout: `/logout` POST
empty body

### Post: `/api/post`
#### All posts (pagination) `/all` GET
Get all posts, paginated.

Request params:

page(1)

pageSize(10)

sort(createdDate-desc)

#### Post detail page: `/detail` GET
Request param: postId

### Edit post: `/edit` PUT
Request:
```json
{
  "postId": 86,
  "content": "test_42e3794946e3",
  "tagIds": [
    51
  ],
  "categoryId": 12
}
```
#### Delete post: `/delete`
Request param: postId


## TODO list

- [ ] redis + like/dislike feature (post and comment)

- [ ] user forget password and reset password

- [ ] manage users

- [ ] exceptions

- [ ] error messages

- [ ] show posts of same tag

- [ ] show posts of same category