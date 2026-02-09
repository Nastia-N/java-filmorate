# java-filmorate
Социальная сеть для любителей кино.  
Позволяет пользователям делиться своими впечатлениями о фильмах, ставить оценки, добавлять друзей и находить общие интересы в сфере кино.

Схема базы данных
![Схема базы данных](https://github.com/Nastia-N/java-filmorate/blob/main/Filmorate.png)

## Возможности
#### Пользователи
Регистрация и профиль пользователя  
Система друзей с подтверждением запросов   
Поиск общих друзей  
История лайков фильмов

#### Фильмы
Добавление фильмов с описанием  
Система рейтингов MPA (G, PG, PG-13, R, NC-17)  
Категории по жанрам  
Возможность ставить лайки фильмам  
Рейтинг популярности фильмов (по количеству лайков)  
Поиск общих фильмов с друзьями

## Технологический стек
Java 21 с использованием Spring Boot 3  
Spring Web MVC для REST API  
Spring JDBC для работы с базой данных  
H2 Database  
Lombok, Jakarta Validation

## Архитектура
Многослойная архитектура (Controller-Service-Repository)  
REST API с JSON  
SQL миграции через schema.sql и data.sql  
Кастомные исключения и глобальный обработчик ошибок

## Основные эндпоинты
#### Пользователи (/users):
GET    /users                         - получить всех пользователей  
GET    /users/{id}                    - получить пользователя по ID  
POST   /users                         - создать нового пользователя  
PUT    /users                         - обновить пользователя  
PUT    /users/{userId}/friends/{friendId} - добавить друга  
DELETE /users/{userId}/friends/{friendId} - удалить друга  
GET    /users/{userId}/friends        - получить друзей пользователя  
GET    /users/{userId}/friends/common/{otherId} - получить общих друзей

#### Фильмы (/films ):
GET    /films                         - получить все фильмы  
GET    /films/{id}                    - получить фильм по ID  
POST   /films                         - создать новый фильм  
PUT    /films                         - обновить фильм  
PUT    /films/{filmId}/like/{userId}  - поставить лайк фильму  
DELETE /films/{filmId}/like/{userId}  - удалить лайк  
GET    /films/popular                 - получить популярные фильмы  
GET    /films/common                  - получить общие фильмы с другом

#### Жанры (/genres):
GET /genres          - получить все жанры  
GET /genres/{id}     - получить жанр по ID

#### Рейтинги MPA (/mpa):
GET /mpa             - получить все рейтинги MPA  
GET /mpa/{id}        - получить рейтинг MPA по ID

## Системные требования
Java 21 (или выше)  
Maven 3.8+  
Любая ОС с поддержкой Java (Windows, Linux, macOS)

## Работа с БД
Проект использует H2 Database в режиме in-memory.  
При каждом перезапуске данные сбрасываются.   
SQL-скрипты для создания схемы и начальных данных находятся в src/main/resources/:  
schema.sql - создание таблиц  
data.sql - начальные данные (жанры, рейтинги MPA)
