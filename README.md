# Yoga APP

## MySQL

Script `ressources/sql/script.sql`

Login with :
- login: yoga@studio.com
- password: test!1234


## Backend

In back folder install dependencies :
> mvn clean install

Launch API :
> mvn spring-boot:run

### Integration tests

In back folder, launch tests with :
> mvn clean test

coverage report location
> /back/target/site/jacoco/index.html

## Frontend

In front folder install dependencies :
> npm install

Launching Angular:
> npm run start

### Unitary tests

Launching test:
> npm run test

coverage report location
> /front/coverage/index.html

### E2E tests

Launching test:
> npm run e2e
> npx cypress run 

Covergage report :
> npm run e2e:coverage