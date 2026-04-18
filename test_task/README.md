# Task time tracker API

REST-сервис для учета задач и времени сотрудников.

## Что реализовано

- Создание задачи
- Получение задачи по ID
- Обновление статуса задачи (`NEW`, `IN_PROGRESS`, `DONE`)
- Создание записи времени сотрудника по задаче
- Получение отчета по времени сотрудника за период
- JWT Bearer Authentication
- Валидация входных DTO
- Глобальная обработка ошибок (`@RestControllerAdvice`)
- OpenAPI/Swagger (`/swagger-ui.html`)
- Unit-тесты (JUnit 5 + Mockito)
- Интеграционный тест DAO-слоя c PostgreSQL Testcontainers

## Технологии

- Java 17
- Spring Boot 3
- MyBatis
- H2 (runtime)
- JUnit 5, Mockito, Testcontainers
- Gradle

## Запуск

```bash
./gradlew bootRun
```

Для Windows PowerShell:

```powershell
.\gradlew.bat bootRun
```

## Как проверить

1. Получить JWT токен:

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "student",
  "password": "student123"
}
```

2. Подставить токен в `Authorization: Bearer <token>` для всех остальных endpoint.

3. Открыть Swagger:

```text
http://localhost:8080/swagger-ui.html
```

4. Примеры запросов:

- В файле `postman_collection.json`

## Тесты

```bash
./gradlew test
```

Для Windows PowerShell:

```powershell
.\gradlew.bat test
```
