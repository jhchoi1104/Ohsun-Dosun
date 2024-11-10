select * from chatbot_history;

INSERT INTO chatbot_history (
    session_id, message_text, sender_type, message_type, timestamp
)
VALUES (
           1,
           "시험용",
           'User',
           '질문',
           NOW()
       );

INSERT INTO chatbot_history (
    session_id, message_text, sender_type, message_type, timestamp
)
VALUES (
           1,
           "시험용",
           'Bot',
           '응답',
           NOW()
       );


select * from chatbot_session;

