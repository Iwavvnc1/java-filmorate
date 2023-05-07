# java-filmorate
Template repository for Filmorate project.

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