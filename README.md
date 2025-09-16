# java-filmorate
![Схема базы данных](https://github.com/Nastia-N/java-filmorate/blob/main/Filmorate.png)


# Запросы

- Получение всех пользователей  
SELECT *  
FROM users  
ORDER BY id;  

- Получие пользователя по id  
SELECT *  
FROM users  
WHERE id = 1;  

- Получение всех фильмов с жанрами и MPA  
SELECT f.id, f.name, f.description, f.releasedate, f.duration,  
       m.name AS mpa_name,  
       g.name AS genre_name  
FROM films AS f  
LEFT JOIN mpa ON f.id = mpa.film_id  
LEFT JOIN film_genre fg ON f.id = fg.film_id  
LEFT JOIN genre g ON fg.genre_id = g.genre_id  
GROUP BY f.id  
ORDER BY f.id;  

- Получие фильма по id  
SELECT *  
FROM films  
WHERE id = 1;  

- Получение лайков конкретного пользователя  
SELECT f.name AS film_name  
FROM films AS f  
JOIN likes l ON f.id = l.film_id  
WHERE l.user_id = 1;  

- Получение лайков конкретного фильма  
SELECT COUNT(l)  
FROM films AS f  
JOIN likes l ON f.id = l.film_id  
WHERE f.id = 1;  

- Получение топ-10 популярных фильмов  
SELECT f.name AS film_name, COUNT(l) AS like_count  
FROM films AS f  
JOIN likes l ON f.id = l.film_id  
ORDER BY like_count DESC, film_name  
LIMIT 10;  

- Получение списка общих друзей  
SELECT u.*   
FROM users AS u  
JOIN friendship f1 ON u.id = f1.friend_id  
JOIN status s1 ON f1.status = s1.status_id  
JOIN friendship f2 ON u.id = f2.friend_id  
JOIN status s2 ON f2.status = s2.status_id  
WHERE f1.user_id = 1 AND f2.user_id = 2   
  AND s1.name = 'CONFIRMED' AND s2.name = 'CONFIRMED';  


