# SQL练习
学院：省级示范性软件学院  
课程：Java Web后端开发技术  
题目：《作业四：SQL练习_1》  
姓名：张爽  
学号：2200770132  
班级：软工2202  
日期：2024-10-16  

员工信息查询：    
1. 查询所有员工的姓名、邮箱和工作岗位。  
```
SELECT CONCAT(first_name, ' ', last_name) AS 姓名, email 邮箱, job_title 工作岗位
FROM employees;  
```

2. 查询所有部门的名称和位置。  
```
SELECT dept_name 部门名称, location 位置
FROM departments;  
```

3. 查询工资超过70000的员工姓名和工资。  
```
SELECT CONCAT(first_name, ' ', last_name) 姓名, salary 工资
FROM employees
WHERE salary > 70000;
```

4. 查询IT部门的所有员工。
```
SELECT CONCAT(e.first_name, ' ', e.last_name) 姓名, e.email 邮箱, e.job_title 工作岗位, e.salary 工资
FROM employees e
JOIN departments d ON e.dept_id = d.dept_id
WHERE d.dept_name = 'IT';
```

5. 查询入职日期在2020年之后的员工信息。
```
SELECT *
FROM employees
WHERE hire_date > '2020-12-31';
```

6. 计算每个部门的平均工资。
```
SELECT d.dept_name 部门名称, AVG(e.salary) 平均工资
FROM employees e
JOIN departments d ON e.dept_id = d.dept_id
GROUP BY d.dept_name;
```

7. 查询工资最高的前3名员工信息。
```
SELECT *
FROM employees
ORDER BY salary DESC
LIMIT 3;
```

8. 查询每个部门员工数量。
```
SELECT d.dept_name 部门名称, COUNT(e.emp_id) 员工数量
FROM departments d
LEFT JOIN employees e ON d.dept_id = e.dept_id
GROUP BY d.dept_name;
```

9. 查询没有分配部门的员工。
```
SELECT *
FROM employees e
WHERE e.dept_id IS NULL;
```

10. 查询参与项目数量最多的员工。
```
SELECT e.emp_id, CONCAT(e.first_name, ' ', e.last_name) AS full_name, COUNT(ep.project_id) AS project_count
FROM employees e
JOIN employee_projects ep ON e.emp_id = ep.emp_id
GROUP BY e.emp_id
ORDER BY project_count DESC
LIMIT 1;
```

11. 计算所有员工的工资总和。
```
SELECT SUM(salary) AS total_salary
FROM employees;
```

12. 查询姓"Smith"的员工信息。
```
SELECT *
FROM employees
WHERE last_name = 'Smith';
```

13. 查询即将在半年内到期的项目。
``` 
SELECT *
FROM projects
WHERE end_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 6 MONTH);
```

14. 查询至少参与了两个项目的员工。
```
SELECT emp_id
FROM employee_projects
GROUP BY emp_id
HAVING COUNT(project_id) >= 2;
```

15. 查询没有参与任何项目的员工。
```
SELECT e.emp_id, e.first_name, e.last_name, e.email
FROM employees e
LEFT JOIN employee_projects ep ON e.emp_id = ep.emp_id
WHERE ep.emp_id IS NULL;
```

16. 计算每个项目参与的员工数量。
```
SELECT p.project_id, p.project_name, COUNT(ep.emp_id) AS num_employees
FROM projects p
LEFT JOIN employee_projects ep ON p.project_id = ep.project_id
GROUP BY p.project_id;
```

17. 查询工资第二高的员工信息。
```
SELECT *
FROM employees
WHERE salary = (
    SELECT MAX(salary)
    FROM employees
    WHERE salary < (
        SELECT MAX(salary)
        FROM employees
    )
);
```

18. 查询每个部门工资最高的员工。
```
SELECT *
FROM (
    SELECT 
        e.*, 
        MAX(e.salary) OVER (PARTITION BY e.dept_id) AS max_salary
    FROM 
        employees e
) AS subquery
WHERE 
    subquery.salary = subquery.max_salary;
```

19. 计算每个部门的工资总和,并按照工资总和降序排列。
```
SELECT d.dept_name, SUM(e.salary) AS total_salary
FROM employees e
JOIN departments d ON e.dept_id = d.dept_id
GROUP BY d.dept_name
ORDER BY total_salary DESC;
```

20. 查询员工姓名、部门名称和工资。
```
SELECT CONCAT(e.first_name, ' ', e.last_name) 姓名,d.dept_name 部门名称,e.salary 工资
FROM employees e
JOIN departments d ON e.dept_id = d.dept_id;

```

21. 查询每个员工的上级主管(假设emp_id小的是上级)。
```
SELECT 
    e1.emp_id AS employee_emp_id, 
    e1.first_name AS employee_first_name, 
    e1.last_name AS employee_last_name, 
    e2.emp_id AS manager_emp_id, 
    e2.first_name AS manager_first_name, 
    e2.last_name AS manager_last_name
FROM employees e1
JOIN employees e2 ON e1.dept_id = e2.dept_id AND e2.emp_id < e1.emp_id;
```

22. 查询所有员工的工作岗位,不要重复。
```
SELECT DISTINCT job_title 
FROM employees;
```

23. 查询平均工资最高的部门。
```
SELECT d.dept_name,AVG(e.salary) AS avg_salary
FROM employees e
JOIN departments d ON e.dept_id = d.dept_id
GROUP BY d.dept_name
ORDER BY avg_salary DESC
LIMIT 1;
```

24. 查询工资高于其所在部门平均工资的员工。
```
SELECT e.emp_id,e.first_name,e.last_name,e.salary,d.dept_name,AVG(e.salary) OVER (PARTITION BY e.dept_id) AS dept_avg_salary
FROM employees e
JOIN departments d ON e.dept_id = d.dept_id
WHERE e.salary > AVG(e.salary) OVER (PARTITION BY e.dept_id);
```

25. 查询每个部门工资前两名的员工。
```
SELECT dept_id,first_name,last_name,salary,
    RANK() OVER (PARTITION BY dept_id ORDER BY salary DESC) AS salary_rank
FROM employees
ORDER BY dept_id,salary_rank;
```

26. 查询跨部门的项目(参与员工来自不同部门)。
```
SELECT p.project_id,p.project_name,COUNT( DISTINCT e.dept_id ) AS num_departments 
FROMprojects p
JOIN employee_projects ep ON p.project_id = ep.project_id
JOIN employees e ON ep.emp_id = e.emp_id 
GROUP BYp.project_id 
HAVING COUNT( DISTINCT e.dept_id ) > 1;
```

27. 查询每个员工的工作年限,并按工作年限降序排序。
```
SELECT emp_id,first_name,last_name,hire_date,DATEDIFF(CURDATE(), hire_date) / 365 AS years_of_experience
FROM employees
ORDER BY years_of_experience DESC;
```

28. 查询本月过生日的员工(假设hire_date是生日)。
```
SELECT emp_id,first_name,last_name,hire_date
FROM employees
WHERE MONTH(hire_date) = MONTH(CURDATE());
```

29. 查询即将在90天内到期的项目和负责该项目的员工。
```
SELECT p.project_id,p.project_name,p.end_date,e.emp_id,e.first_name,e.last_name
FROM projects p
JOIN employee_projects ep ON p.project_id = ep.project_id
JOIN employees e ON ep.emp_id = e.emp_id
WHERE p.end_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 90 DAY);
```

30. 计算每个项目的持续时间(天数)。
```
SELECT project_id,project_name,DATEDIFF(end_date, start_date) AS duration_days
FROM projects
WHERE end_date IS NOT NULL AND start_date IS NOT NULL;

```

31. 查询没有进行中项目的部门。
```
SELECT d.dept_id, d.dept_name
FROM departments d
WHERE d.dept_id NOT IN (
    SELECT DISTINCT e.dept_id
    FROM employees e
    JOIN employee_projects ep ON e.emp_id = ep.emp_id
    JOIN projects p ON ep.project_id = p.project_id
    WHERE (p.end_date IS NULL OR p.end_date > CURRENT_DATE)
);
```

32. 查询员工数量最多的部门。
```
SELECT d.dept_name,COUNT(e.emp_id) AS num_employees
FROM departments d
JOIN employees e ON d.dept_id = e.dept_id
GROUP BY d.dept_name
ORDER BY num_employees DESC
LIMIT 1;
```

33. 查询参与项目最多的部门。
```
SELECT d.dept_name,COUNT(DISTINCT ep.project_id) AS num_projects
FROM departments d
JOIN employees e ON d.dept_id = e.dept_id
JOIN employee_projects ep ON e.emp_id = ep.emp_id
GROUP BY d.dept_name
ORDER BY num_projects DESC
LIMIT 1;
```

34. 计算每个员工的薪资涨幅(假设每年涨5%)。
```
SELECT emp_id,first_name,last_name,salary,
    salary * POWER(1.05, FLOOR(YEAR(CURDATE()) - YEAR(hire_date))) AS adjusted_salary
FROM employees;
```

35. 查询入职时间最长的3名员工。
```
SELECT emp_id,first_name,last_name,hire_date
FROM employees
ORDER BY hire_date ASC
LIMIT 3;
```

36. 查询名字和姓氏相同的员工。
```
SELECT emp_id,first_name,last_name
FROM employees
WHERE first_name = last_name;
```

37. 查询每个部门薪资最低的员工。
```
SELECT dept_id,first_name,last_name,salary
FROM (
    SELECT e.dept_id,e.first_name,e.last_name,e.salary,
        RANK() OVER (PARTITION BY e.dept_id ORDER BY e.salary ASC) AS salary_rank
    FROM employees e
) ranked_employees
WHERE salary_rank = 1;
```

38. 查询哪些部门的平均工资高于公司的平均工资。
```
SELECT d.dept_name,AVG(e.salary) AS dept_avg_salary
FROM employees e
JOIN departments d ON e.dept_id = d.dept_id
GROUP BY d.dept_name
HAVING dept_avg_salary > (
        SELECT AVG(salary)
        FROM employees
    );
```

39. 查询姓名包含"son"的员工信息。
```
SELECT *
FROM employees
WHERE first_name LIKE '%son%' OR last_name LIKE '%son%';
```

40. 查询所有员工的工资级别(可以自定义工资级别)。
```
SELECT emp_id,first_name,last_name,salary,
    CASE
        WHEN salary < 30000 THEN 'Low'
        WHEN salary BETWEEN 30000 AND 50000 THEN 'Medium'
        WHEN salary BETWEEN 50001 AND 70000 THEN 'High'
        WHEN salary > 70000 THEN 'Very High'
    END AS salary_level
FROM employees;
```

41. 查询每个项目的完成进度(根据当前日期和项目的开始及结束日期)。
```
SELECT p.project_id,p.project_name,p.start_date,p.end_date,
	DATEDIFF( CURDATE(), p.start_date ) AS days_elapsed,
	DATEDIFF( p.end_date, p.start_date ) AS total_days,
CASE
		WHEN CURDATE() > p.end_date THEN 100 
		ELSE ROUND( DATEDIFF( CURDATE(), p.start_date ) / DATEDIFF( p.end_date, p.start_date ) * 100, 2 ) 
END AS completion_percentage 
FROM projects p 
WHERE p.end_date IS NOT NULL AND p.start_date IS NOT NULL;
```

42. 查询每个经理(假设job_title包含'Manager'的都是经理)管理的员工数量。
```
SELECT m.emp_id AS manager_id,
    CONCAT(m.first_name, ' ', m.last_name) AS manager_name,
    COUNT(e.emp_id) AS num_managed_employees
FROM employees m
JOIN employees e ON m.dept_id = e.dept_id AND m.emp_id <> e.emp_id
WHERE m.job_title LIKE '%Manager%'
GROUP BY m.emp_id
ORDER BY num_managed_employees DESC;
```

43. 查询工作岗位名称里包含"Manager"但不在管理岗位(salary<70000)的员工。
```
SELECT e.emp_id,CONCAT(e.first_name, ' ', e.last_name) AS full_name,e.job_title,e.salary
FROM employees e
WHERE e.job_title LIKE '%Manager%'AND e.salary < 70000;
```

44. 计算每个部门的男女比例(假设以名字首字母A-M为女性,N-Z为男性)。
```
SELECT d.dept_name,
    COUNT(CASE WHEN LEFT(e.first_name, 1) BETWEEN 'A' AND 'M' THEN e.emp_id END) AS num_females,
    COUNT(CASE WHEN LEFT(e.first_name, 1) BETWEEN 'N' AND 'Z' THEN e.emp_id END) AS num_males,
    CONCAT(ROUND(COUNT(CASE WHEN LEFT(e.first_name, 1) BETWEEN 'A' AND 'M' THEN e.emp_id END) / COUNT(e.emp_id) * 100, 2), '%') AS female_percentage,
    CONCAT(ROUND(COUNT(CASE WHEN LEFT(e.first_name, 1) BETWEEN 'N' AND 'Z' THEN e.emp_id END) / COUNT(e.emp_id) * 100, 2), '%') AS male_percentage
FROM employees e
JOIN departments d ON e.dept_id = d.dept_id
GROUP BY d.dept_name;
```

45. 查询每个部门年龄最大和最小的员工(假设hire_date反应了年龄)。
```
SELECT 
		e1.dept_id,
    e1.emp_id AS oldest_emp_id,
    e1.first_name AS oldest_first_name,
    e1.last_name AS oldest_last_name,
    e2.emp_id AS youngest_emp_id,
    e2.first_name AS youngest_first_name,
    e2.last_name AS youngest_last_name
FROM 
    (SELECT dept_id, MAX(hire_date) AS hire_date 
     FROM employees 
     GROUP BY dept_id) oldest
JOIN employees e1 ON oldest.dept_id = e1.dept_id AND oldest.hire_date = e1.hire_date
JOIN 
		(SELECT dept_id, MIN(hire_date) AS hire_date 
     FROM employees 
     GROUP BY dept_id) youngest
ON e1.dept_id = youngest.dept_id
JOIN employees e2 ON e1.dept_id = e2.dept_id AND youngest.hire_date = e2.hire_date
ORDER BY e1.dept_id;
```

46. 查询连续3天都有员工入职的日期。
```
SELECT e1.hire_date
FROM employees e1
WHERE 
    EXISTS (
        SELECT 1
        FROM employees e2
        WHERE e2.hire_date = DATE_SUB(e1.hire_date, INTERVAL 1 DAY)
    )
    AND EXISTS (
        SELECT 1
        FROM employees e3
        WHERE e3.hire_date = DATE_SUB(e1.hire_date, INTERVAL 2 DAY)
    )
GROUP BY e1.hire_date;
```

47. 查询员工姓名和他参与的项目数量。
```
SELECT CONCAT(e.first_name, ' ', e.last_name) AS full_name,
       COUNT(ep.project_id) AS num_projects
FROM employees e
LEFT JOIN employee_projects ep ON e.emp_id = ep.emp_id
GROUP BY e.emp_id
ORDER BY full_name;
```

48. 查询每个部门工资最高的3名员工。
```
SELECT dept_id,first_name,last_name,salary
FROM (
    SELECT 
        e.dept_id,
        e.first_name,
        e.last_name,
        e.salary,
        DENSE_RANK() OVER (PARTITION BY e.dept_id ORDER BY e.salary DESC) AS salary_rank
    FROM employees e
) ranked_employees
WHERE salary_rank <= 3;
```

49. 计算每个员工的工资与其所在部门平均工资的差值。
```
SELECT e.emp_id, e.first_name, e.last_name, e.salary, d.avg_salary, e.salary - d.avg_salary AS salary_diff
FROM employees e
JOIN (
    SELECT dept_id, AVG(salary) AS avg_salary
    FROM employees
    GROUP BY dept_id
) d ON e.dept_id = d.dept_id;
```

50. 查询所有项目的信息,包括项目名称、负责人姓名(假设工资最高的为负责人)、开始日期和结束日期。
```
SELECT p.project_id, p.project_name, p.start_date, p.end_date,
    (SELECT CONCAT(e.first_name, ' ', e.last_name) 
     FROM employees e
     WHERE e.emp_id = (
         SELECT MAX(sub_e.emp_id)
         FROM employees sub_e
         JOIN employee_projects sub_ep ON sub_e.emp_id = sub_ep.emp_id
         WHERE sub_ep.project_id = p.project_id
     )
    ) AS manager_name
FROM projects p;
```
