drop table if exists user ;

CREATE TABLE `user` (
                        `user_id` int NOT NULL AUTO_INCREMENT,
                        `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                        `username` varchar(50) NOT NULL,
                        `password` varchar(255) NOT NULL,
                        `age` int DEFAULT NULL,
                        `gender` enum('M','F','O') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                        `ssn` varchar(14) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                        PRIMARY KEY (`user_id`),
                        UNIQUE KEY `username` (`username`),
                        UNIQUE KEY `ssn` (`ssn`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO ohsundosun.`user` (name,username,password,age,gender,ssn) VALUES
                                                                          ('한민국','mink123@naver.com','password123',30,'M','123456-1234567'),
                                                                          ('홍길동','hong123','password456',25,'F','234567-2345678'),
                                                                          ('이철수','lee789@naver.com','password789',28,'M','345678-3456789'),
                                                                          ('박수진','park012@naver.com','password012',35,'F','456789-4567890'),
                                                                          ('김영화','movie1234@naver.com','password345',40,'M','567890-5678901'),
                                                                          (NULL,'1234@naver.com','$2a$10$RrJIByqeuK0gqwJ55qcxK.VoGkWYDpiptSHzQp0bArILmL4ipzjCW',NULL,NULL,NULL);

select * from chatbot_history;