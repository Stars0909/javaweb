# SQL练习
学院：省级示范性软件学院  
课程：Java Web后端开发技术  
题目：《作业四：SQL练习_2》  
姓名：张爽  
学号：2200770132  
班级：软工2202  
日期：2024-10-16  

学生信息查询：    
1. 查询所有学生的信息。
```
SELECT * 
FROM student;
```

2. 查询所有课程的信息。
```
SELECT * 
FROM course;
```

3. 查询所有学生的姓名、学号和班级。
```
SELECT student_id, name, my_class 
FROM student;
```

4. 查询所有教师的姓名和职称。
```
SELECT name, title 
FROM teacher;
```

5. 查询不同课程的平均分数。
```
SELECT course_id, AVG(score) AS average_score
FROM score
GROUP BY course_id;
```

6. 查询每个学生的平均分数。
```
SELECT student_id, AVG(score) AS average_score
FROM score
GROUP BY student_id;
```

7. 查询分数大于85分的学生学号和课程号。
```
SELECT student_id, course_id
FROM score
WHERE score > 85;
```

8. 查询每门课程的选课人数。
```
SELECT course_id, COUNT(student_id) number_of_students
FROM score
GROUP BY course_id;
```

9. 查询选修了"高等数学"课程的学生姓名和分数。
```
SELECT s.name AS student_name, sc.score
FROM student s
JOIN score sc ON s.student_id = sc.student_id
JOIN course c ON sc.course_id = c.course_id
WHERE c.course_name = '高等数学';
```

10. 查询没有选修"大学物理"课程的学生姓名。
```
SELECT s.name
FROM student s
WHERE s.student_id NOT IN (
    SELECT sc.student_id
    FROM score sc
    JOIN course c ON sc.course_id = c.course_id
    WHERE c.course_name = '大学物理'
);
```

11. 查询C001比C002课程成绩高的学生信息及课程分数。
```
SELECT s.student_id, s.name, 
		sc1.score AS C001_score,
    sc2.score AS C002_score
FROM student s
JOIN score sc1 ON s.student_id = sc1.student_id
JOIN score sc2 ON s.student_id = sc2.student_id
WHERE sc1.course_id = 'C001' AND sc2.course_id = 'C002' AND sc1.score > sc2.score;
```

12. 统计各科成绩各分数段人数：课程编号，课程名称，[100-85]，[85-70]，[70-60]，[60-0] 及所占百分比
```
SELECT 
    c.course_id,
    c.course_name,
    COUNT(CASE WHEN sc.score BETWEEN 85 AND 100 THEN 1 END) AS score_100_85,
    COUNT(CASE WHEN sc.score BETWEEN 70 AND 84 THEN 1 END) AS score_85_70,
    COUNT(CASE WHEN sc.score BETWEEN 60 AND 69 THEN 1 END) AS score_70_60,
    COUNT(CASE WHEN sc.score < 60 THEN 1 END) AS score_60_0,
    ROUND((COUNT(CASE WHEN sc.score BETWEEN 85 AND 100 THEN 1 END) * 100.0) / COUNT(sc.student_id), 2) AS percent_100_85,
    ROUND((COUNT(CASE WHEN sc.score BETWEEN 70 AND 84 THEN 1 END) * 100.0) / COUNT(sc.student_id), 2) AS percent_85_70,
    ROUND((COUNT(CASE WHEN sc.score BETWEEN 60 AND 69 THEN 1 END) * 100.0) / COUNT(sc.student_id), 2) AS percent_70_60,
    ROUND((COUNT(CASE WHEN sc.score < 60 THEN 1 END) * 100.0) / COUNT(sc.student_id), 2) AS percent_60_0
FROM score sc
JOIN course c ON sc.course_id = c.course_id
GROUP BY c.course_id, c.course_name;
		
```

13. 查询选择C002课程但没选择C004课程的成绩情况(不存在时显示为 null )。
```
SELECT sc1.student_id, sc1.score AS C002_score, sc2.score AS C004_score
FROM score sc1
JOIN course c1 ON sc1.course_id = c1.course_id AND c1.course_name = '大学物理'
LEFT JOIN score sc2 ON sc1.student_id = sc2.student_id AND sc2.course_id = (SELECT course_id FROM course WHERE course_name = '数据结构')
WHERE sc1.course_id = (SELECT course_id FROM course WHERE course_name = '大学物理') AND sc2.student_id IS NULL;
```

14. 查询平均分数最高的学生姓名和平均分数。
```
SELECT s.name AS student_name,AVG(sc.score) AS average_score
FROM score sc
JOIN student s ON sc.student_id = s.student_id
GROUP BY s.student_id, s.name
ORDER BY average_score DESC
LIMIT 1;
```

15. 查询总分最高的前三名学生的姓名和总分。
```
SELECT s.name AS student_name, SUM(sc.score) AS total_score
FROM score sc
JOIN student s ON sc.student_id = s.student_id
GROUP BY s.student_id, s.name
ORDER BY total_score DESC
LIMIT 3;
```

16. 查询各科成绩最高分、最低分和平均分。要求如下：
    以如下形式显示：课程 ID，课程 name，最高分，最低分，平均分，及格率，中等率，优良率，优秀率
    及格为>=60，中等为：70-80，优良为：80-90，优秀为：>=90
    要求输出课程号和选修人数，查询结果按人数降序排列，若人数相同，按课程号升序排列
```
SELECT 
    c.course_id,
    c.course_name,
    MAX(sc.score) AS max_score,
    MIN(sc.score) AS min_score,
    AVG(sc.score) AS avg_score,
    ROUND(SUM(CASE WHEN sc.score >= 60 THEN 1 ELSE 0 END) / COUNT(sc.student_id) * 100, 2) AS pass_rate,
    ROUND(SUM(CASE WHEN sc.score BETWEEN 70 AND 80 THEN 1 ELSE 0 END) / COUNT(sc.student_id) * 100, 2) AS average_rate,
    ROUND(SUM(CASE WHEN sc.score BETWEEN 80 AND 90 THEN 1 ELSE 0 END) / COUNT(sc.student_id) * 100, 2) AS good_rate,
    ROUND(SUM(CASE WHEN sc.score >= 90 THEN 1 ELSE 0 END) / COUNT(sc.student_id) * 100, 2) AS excellent_rate,
    COUNT(sc.student_id) AS number_of_students
FROM score sc
JOIN course c ON sc.course_id = c.course_id
GROUP BY c.course_id, c.course_name
ORDER BY COUNT(sc.student_id) DESC, c.course_id ASC;
```

17. 查询男生和女生的人数。
```
SELECT gender, COUNT(*) AS number_of_students
FROM student
GROUP BY gender;
```

18. 查询年龄最大的学生姓名。
```
SELECT name 
FROM student
WHERE birth_date = (
        SELECT MIN(birth_date) 
        FROM student
    );
```

19. 查询年龄最小的教师姓名。
```
SELECT name 
FROM teacher
WHERE birth_date = (
        SELECT MAX(birth_date) 
        FROM teacher
    );
```

20. 查询学过「张教授」授课的同学的信息。
```
SELECT s.*
FROM student s
JOIN score sc ON s.student_id = sc.student_id
JOIN course c ON sc.course_id = c.course_id
WHERE c.teacher_id = (
        SELECT teacher_id 
        FROM teacher
        WHERE name = '张教授'
    );
```

21. 查询查询至少有一门课与学号为"2021001"的同学所学相同的同学的信息。
```
SELECT DISTINCT s.*
FROM student s
JOIN score sc ON s.student_id = sc.student_id
WHERE sc.course_id IN (
        SELECT sc2.course_id
        FROM score sc2
        WHERE sc2.student_id = '2021001'
    )
    AND s.student_id <> '2021001';
```

22. 查询每门课程的平均分数，并按平均分数降序排列。
```
SELECT c.course_id, c.course_name, AVG(sc.score) AS average_score
FROM score sc
JOIN course c ON sc.course_id = c.course_id
GROUP BY c.course_id, c.course_name
ORDER BY average_score DESC;
```

23. 查询学号为"2021001"的学生所有课程的分数。
```
SELECT sc.course_id,c.course_name,sc.score
FROM score sc
JOIN course c ON sc.course_id = c.course_id
WHERE sc.student_id = '2021001';
```

24. 查询所有学生的姓名、选修的课程名称和分数。
```
SELECT s.name AS student_name, c.course_name, sc.score
FROM student s
JOIN score sc ON s.student_id = sc.student_id
JOIN course c ON sc.course_id = c.course_id;
```

25. 查询每个教师所教授课程的平均分数。
```
SELECT t.name AS teacher_name, c.course_name, AVG(sc.score) AS average_score
FROM course c
JOIN teacher t ON c.teacher_id = t.teacher_id
JOIN score sc ON c.course_id = sc.course_id
GROUP BY t.name, c.course_name;
```

26. 查询分数在80到90之间的学生姓名和课程名称。
```
SELECT s.name AS student_name, c.course_name
FROM student s
JOIN score sc ON s.student_id = sc.student_id
JOIN course c ON sc.course_id = c.course_id
WHERE sc.score BETWEEN 80 AND 90;
```

27. 查询每个班级的平均分数。
```
SELECT my_class AS class_name, AVG(sc.score) AS average_score
FROM student s
JOIN score sc ON s.student_id = sc.student_id
GROUP BY my_class
ORDER BY class_name;
```

28. 查询没学过"王讲师"老师讲授的任一门课程的学生姓名。
```
SELECT s.name AS student_name
FROM student s
WHERE s.student_id NOT IN (
        SELECT DISTINCT sc.student_id
        FROM score sc
        JOIN course c ON sc.course_id = c.course_id
        JOIN teacher t ON c.teacher_id = t.teacher_id
        WHERE t.name = '王讲师'
    );
```

29. 查询两门及其以上小于85分的同学的学号，姓名及其平均成绩。
```
SELECT s.student_id, s.name AS student_name,AVG(sc.score) AS average_score
FROM student s
JOIN score sc ON s.student_id = sc.student_id
WHERE sc.score < 85
GROUP BY s.student_id, s.name
HAVING COUNT(sc.course_id) >= 2;
```

30. 查询所有学生的总分并按降序排列。
```
SELECT sc.student_id, s.name AS student_name, SUM(sc.score) AS total_score
FROM score sc
JOIN student s ON sc.student_id = s.student_id
GROUP BY sc.student_id, s.name
ORDER BY total_score DESC;
```

31. 查询平均分数超过85分的课程名称。
```
SELECT c.course_name
FROM course c
JOIN score sc ON c.course_id = sc.course_id
GROUP BY c.course_name
HAVING AVG(sc.score) > 85;
```

32. 查询每个学生的平均成绩排名。
```
SELECT student_id,name,
    AVG(score) AS average_score,
    RANK() OVER (ORDER BY AVG(score) DESC) AS rank
FROM student
JOIN score ON student.student_id = score.student_id
GROUP BY student_id, name
ORDER BY average_score DESC;
```

33. 查询每门课程分数最高的学生姓名和分数。
```
SELECT c.course_name, s.name AS student_name, sc.score AS max_score
FROM score sc
JOIN student s ON sc.student_id = s.student_id
JOIN course c ON sc.course_id = c.course_id
WHERE sc.score = (
        SELECT MAX(sub_score.score) 
        FROM score sub_score
        WHERE sub_score.course_id = sc.course_id
    );
```

34. 查询选修了"高等数学"和"大学物理"的学生姓名。
```
SELECT DISTINCT s.name AS student_name
FROM student s
JOIN score sc ON s.student_id = sc.student_id
JOIN course c ON sc.course_id = c.course_id
WHERE c.course_name IN ('高等数学', '大学物理')
GROUP BY s.student_id, s.name
HAVING COUNT(DISTINCT c.course_name) = 2;
```

35. 按平均成绩从高到低显示所有学生的所有课程的成绩以及平均成绩（没有选课则为空）。
```
SELECT s.student_id, s.name AS student_name, c.course_name, sc.score,
    IFNULL(AVG(sc.score) OVER (PARTITION BY s.student_id), 0) AS average_score
FROM student s
LEFT JOIN score sc ON s.student_id = sc.student_id
LEFT JOIN course c ON sc.course_id = c.course_id
ORDER BY average_score DESC, s.student_id;
```

36. 查询分数最高和最低的学生姓名及其分数。
```
SELECT s.name AS student_name, sc.score
FROM student s
JOIN score sc ON s.student_id = sc.student_id
WHERE sc.score = (
        SELECT MAX(score) FROM score
    ) OR sc.score = (
        SELECT MIN(score) FROM score
    );
```

37. 查询每个班级的最高分和最低分。
```
SELECT s.my_class AS class_name,MAX(sc.score) AS max_score,MIN(sc.score) AS min_score
FROM student s
JOIN score sc ON s.student_id = sc.student_id
GROUP BY s.my_class
ORDER BY class_name;
```

38. 查询每门课程的优秀率（优秀为90分）。
```
SELECT c.course_name,
    ROUND(SUM(CASE WHEN sc.score >= 90 THEN 1 ELSE 0 END) * 100.0 / COUNT(sc.student_id), 2) AS excellent_rate
FROM score sc
JOIN course c ON sc.course_id = c.course_id
GROUP BY c.course_name;
```

39. 查询平均分数超过班级平均分数的学生。
```
SELECT s.student_id, s.name AS student_name, s.my_class,
    AVG(sc.score) OVER (PARTITION BY s.my_class) AS class_average_score,
    AVG(sc.score) OVER (PARTITION BY s.student_id) AS student_average_score
FROM student s
JOIN score sc ON s.student_id = sc.student_id
GROUP BY s.student_id, s.name, s.my_class
HAVING AVG(sc.score) OVER (PARTITION BY s.student_id) > AVG(sc.score) OVER (PARTITION BY s.my_class);
```

40. 查询每个学生的分数及其与课程平均分的差值。
```
SELECT s.student_id, s.name AS student_name, c.course_id, c.course_name,
    sc.score AS student_score,
    AVG(sc.score) OVER (PARTITION BY sc.course_id) AS course_average_score,
    sc.score - AVG(sc.score) OVER (PARTITION BY sc.course_id) AS score_difference
FROM score sc
JOIN course c ON sc.course_id = c.course_id
JOIN student s ON sc.student_id = s.student_id;
```

41. 查询至少有一门课程分数低于80分的学生姓名。
```
SELECT s.name AS student_name
FROM student s
JOIN score sc ON s.student_id = sc.student_id
WHERE sc.score < 80
GROUP BY s.student_id, s.name
HAVING COUNT(CASE WHEN sc.score < 80 THEN 1 END) >= 1;
```

42. 查询所有课程分数都高于85分的学生姓名。
```
SELECT s.name AS student_name
FROM student s
WHERE NOT EXISTS (
        SELECT 1
        FROM score sc
        WHERE sc.student_id = s.student_id AND sc.score <= 85
    );
```

43. 查询查询平均成绩大于等于90分的同学的学生编号和学生姓名和平均成绩。
```
SELECT s.student_id, s.name AS student_name, AVG(sc.score) AS average_score
FROM student s
JOIN score sc ON s.student_id = sc.student_id
GROUP BY s.student_id, s.name
HAVING AVG(sc.score) >= 90;
```

44. 查询选修课程数量最少的学生姓名。
```
SELECT s.name AS student_name
FROM student s
JOIN score sc ON s.student_id = sc.student_id
GROUP BY s.student_id, s.name
HAVING COUNT(sc.course_id) = (
        SELECT MIN(course_count) 
        FROM (
                SELECT COUNT(sc2.course_id) AS course_count
                FROM score sc2
                GROUP BY sc2.student_id
            ) AS subquery
    );
```

45. 查询每个班级的第2名学生（按平均分数排名）。
```
WITH RankedStudents AS (
    SELECT
        s.student_id,
        s.name AS student_name,
        s.my_class,
        AVG(sc.score) AS average_score,
        DENSE_RANK() OVER (PARTITION BY s.my_class ORDER BY AVG(sc.score) DESC) AS rank
    FROM student s
    JOIN score sc ON s.student_id = sc.student_id
    GROUP BY s.student_id, s.name, s.my_class
)
SELECT student_id, student_name, my_class, average_score
FROM RankedStudents
WHERE rank = 2;
```

46. 查询每门课程分数前三名的学生姓名和分数。
```
WITH RankedScores AS (
    SELECT
        s.student_id,
        s.name AS student_name,
        c.course_name,
        sc.score,
        ROW_NUMBER() OVER (PARTITION BY sc.course_id ORDER BY sc.score DESC) AS rank
    FROM score sc
    JOIN student s ON sc.student_id = s.student_id
    JOIN course c ON sc.course_id = c.course_id
)
SELECT student_id, student_name, course_name, score
FROM RankedScores
WHERE rank <= 3;
```

47. 查询平均分数最高和最低的班级。
```
SELECT my_class AS class_name, AVG(score) AS average_score
FROM student s
JOIN score sc ON s.student_id = sc.student_id
GROUP BY my_class
ORDER BY average_score DESC
LIMIT 1

UNION ALL

SELECT my_class AS class_name, AVG(score) AS average_score
FROM student s
JOIN score sc ON s.student_id = sc.student_id
GROUP BY my_class
ORDER BY average_score ASC
LIMIT 1;
```

48. 查询每个学生的总分和他所在班级的平均分数。
```
SELECT s.student_id, s.name AS student_name, s.my_class,
    SUM(sc.score) AS student_total_score,
    (SELECT AVG(sc2.score) FROM score sc2 JOIN student s2 ON sc2.student_id = s2.student_id WHERE s2.my_class = s.my_class) AS class_average_score
FROM student s
LEFT JOIN score sc ON s.student_id = sc.student_id
GROUP BY s.student_id, s.name, s.my_class;
```

49. 查询每个学生的最高分的课程名称, 学生名称，成绩。
```
WITH RankedScores AS (
    SELECT
        s.name AS student_name,
        c.course_name,
        sc.score,
        ROW_NUMBER() OVER (PARTITION BY sc.student_id ORDER BY sc.score DESC) AS rank
    FROM score sc
    JOIN student s ON sc.student_id = s.student_id
    JOIN course c ON sc.course_id = c.course_id
)
SELECT student_name, course_name, score
FROM RankedScores
WHERE rank = 1;
```

50. 查询每个班级的学生人数和平均年龄。
```
SELECT my_class AS class_name,
    COUNT(student_id) AS number_of_students,
    AVG(YEAR(CURDATE()) - YEAR(birth_date)) AS average_age
FROM student
GROUP BY my_class
ORDER BY class_name;
```