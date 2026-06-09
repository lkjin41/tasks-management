# Tasks Management

Tasks Management — это REST API приложение для управления задачами, написанное на Java с использованием фреймворка Spring Boot.

## Технологический стек

- **Java 17**
- **Spring Boot** (Web, Data JPA, Validation)
- **PostgreSQL** (База данных)
- **Lombok** (Сокращение шаблонного кода)
- **MapStruct** (Маппинг DTO)
- **Docker и Docker Compose** (Для локального развертывания инфраструктуры)

## Требования

Для запуска проекта вам потребуется:
- **JDK 17**
- **Docker** и **Docker Compose** (для запуска базы данных)
- **Maven** (встроен в проект через Maven Wrapper)

## Установка и запуск

1. Склонируйте репозиторий на свой компьютер:
   ```bash
   git clone <url-репозитория>
   cd tasks-management
   ```

2. Настройте переменные окружения:
   Скопируйте пример файла конфигурации:
   ```bash
   cp .env.example .env
   ```
   Откройте файл `.env` и укажите параметры для базы данных:
   ```env
   DB_USER=postgres
   DB_PASSWORD=your_password
   DB_NAME=tasks_db
   ```

3. Запустите базу данных с помощью Docker Compose:
   ```bash
   docker-compose up -d
   ```
   База данных запустится на порту `5433` (как указано в `docker-compose.yaml`).

4. Запустите само приложение с помощью Maven Wrapper:
   - В Linux/macOS:
     ```bash
     ./mvnw spring-boot:run
     ```
   - В Windows:
     ```cmd
     mvnw.cmd spring-boot:run
     ```

## Основной функционал

Проект предоставляет API для:
- Создания задач с приоритетом и статусом.
- Обновления задач.
- Поиска задач по фильтрам.
- Обработки бизнес-логики и исключительных ситуаций (например, `TaskAlreadyCompletedException`).

Все ответы об ошибках стандартизированы с помощью глобального обработчика исключений (`GlobalExceptionHandler`).