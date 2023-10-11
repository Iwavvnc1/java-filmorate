# Сервис оценки фильмов  
## Описание проекта    
Это сервис, который позволяет пользователям оценивать фильмы, просматривать популярные фильмы и находить друзей с общими интересами. Пользователи могут ставить лайки фильмам, добавлять и удалять друзей, просматривать список друзей и общих друзей с другими пользователями. Фильмы имеют следующие атрибуты: id, title, description, genre, year, likes. Пользователи имеют следующие атрибуты: id, name, email, password, friends.  

## Технологии, которые использовались
- Java 11  
- Lombok  
- Spring Boot  
- JUnit  
- H2  
## Архитектура сервиса  
Сервис использует Spring Boot для создания RESTful API с встроенной базой данных H2. Для упрощения кода используется Lombok. Для тестирования используется JUnit.  

## Эндпоинты сервиса  
Сервис может обрабатывать следующие эндпоинты:  

- /films — для работы с фильмами
    - GET /films — возвращает список всех фильмов
    - POST /films — создает новый фильм из тела запроса
    - PUT /films — обновляет существующий фильм из тела запроса
    - GET /films/{id} — возвращает фильм по id
    - PUT /films/{id}/like/{userId} — добавляет лайк фильму от пользователя по id
    - DELETE /films/{id}/like/{userId} — удаляет лайк фильма от пользователя по id
    - GET /films/popular — возвращает список популярных фильмов по количеству лайков
- /users — для работы с пользователями
    - GET /users — возвращает список всех пользователей
    - POST /users — создает нового пользователя из тела запроса
    - PUT /users — обновляет существующего пользователя из тела запроса
    - GET /users/{id} — возвращает пользователя по id
    - PUT /users/{id}/friends/{friendId} — добавляет друга пользователю по id
    - DELETE /users/{id}/friends/{friendId} — удаляет друга пользователя по id
    - GET /users/{id}/friends — возвращает список друзей пользователя по id
    - GET /users/{id}/friends/common/{otherId} — возвращает список общих друзей двух пользователей по id

![](/resource/Diag_bd.png)

<details> <summary> Примеры запросов </summary>
Примеры запросов:  

<br> 1. Получение пользователя с ID = 1:  
  <br> SELECT * 
  <br> FROM Users
  <br> WHERE User_ID = 1;
<br>2. Получение фильма с ID = 1:  
 <br>  SELECT*  
 <br>  FROM Films  
 <br> WHERE Film_ID = 1;
<br> 3. Получение списка друзей пользователя с ID = 1:  
  <br> SELECT * 
  <br> FROM Friends
  <br> WHERE (User_ID = 1 OR Friend_ID = 1) 
<br> AND Status = 'CONFIRMED';
<br> 4. Получение списка лайков фильма с ID = 1:  
  <br> SELECT * 
  <br> FROM Likes
  <br> WHERE Film_ID = 1;
<br> 5. Получение списка общих друзей пользователей с ID = 1 и ID = 2:  
<br> (SELECT User_id AS Common_friend
<br> FROM Friends
<br> WHERE (Friend_ID = 2) 
<br> AND Status = 'Confirmed'
<br> UNION
<br> SELECT Friend_ID As Common_friend
<br> FROM Friends
<br> WHERE (User_ID = 2) 
<br> AND Status = 'Confirmed')
<br> INTERSECT
<br> (SELECT User_id AS Common_friend
<br> FROM Friends
<br> WHERE (Friend_ID = 1) 
<br> AND Status = 'Confirmed'
<br> UNION
<br> SELECT Friend_ID As Common_friend
<br> FROM Friends
<br> WHERE (User_ID = 1) 
<br> AND Status = 'Confirmed');
</details>

## Лицензия  
Этот сервис распространяется под лицензией MIT. Вы можете свободно использовать, изменять и распространять этот сервис, при условии, что вы указываете авторство и сохраняете эту лицензию.
