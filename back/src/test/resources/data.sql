INSERT INTO TEACHERS (first_name, last_name)
VALUES ('Margot', 'DELAHAYE'),
       ('Hélène', 'THIERCELIN');


INSERT INTO USERS (first_name, last_name, admin, email, password)
VALUES ('Yoga', 'TestAdmin', true, 'yogaTEST2@studio.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq');

INSERT INTO SESSIONS (name, description, teacher_id, date)
VALUES ('Initiation au Yoga', 'Description', 1 /*Margot DELAHAYE*/, NOW()),
       ('Cours novice de Yoga', 'Description2', 2 /*Hélène THIERCELIN*/, NOW());

INSERT INTO PARTICIPATE (session_id, user_id)
VALUES (2, 1)