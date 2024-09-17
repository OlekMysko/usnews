INSERT INTO news (news_id, title, content, location_id)
VALUES (UUID_TO_BIN('10000000-0000-0000-0000-000000000001'), 'Breaking News in New York',
        'Something important happened in New York.', UUID_TO_BIN('00000000-0000-0000-0000-000000000001')),
       (UUID_TO_BIN('10000000-0000-0000-0000-000000000002'), 'Ashburn Local Event',
        'An event has taken place in Ashburn.', UUID_TO_BIN('00000000-0000-0000-0000-000000000002')),
       (UUID_TO_BIN('10000000-0000-0000-0000-000000000003'), 'Hemingford Highlights',
        'Small town news from Hemingford, NE.', UUID_TO_BIN('00000000-0000-0000-0000-000000000003'));

