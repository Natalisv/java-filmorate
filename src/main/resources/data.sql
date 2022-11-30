/*private final JdbcTemplate jdbcTemplate;

@AfterEach
void tearDown() {
    jdbcTemplate.update("DELETE FROM LIKES");
    jdbcTemplate.update("DELETE FROM FILM_GENRE");
    jdbcTemplate.update("DELETE FROM FRIENDSHIP");
    jdbcTemplate.update("DELETE FROM USERS");
    jdbcTemplate.update("DELETE FROM FILMS");
    jdbcTemplate.update("ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1");
    jdbcTemplate.update("ALTER TABLE FILMS ALTER COLUMN ID RESTART WITH 1");
}*/

DELETE FROM LIKES;
DELETE FROM FILM_GENRE;
DELETE FROM FRIENDSHIP;
DELETE FROM USERS;
DELETE FROM FILMS;
ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE FILMS ALTER COLUMN ID RESTART WITH 1;
MERGE INTO MPA KEY(ID)
    VALUES (1, 'G'),
    (2, 'PG'),
    (3, 'PG-13'),
    (4, 'R'),
    (5, 'NC-17');
MERGE INTO GENRES KEY(ID)
    VALUES (1, 'Комедия'),
    (2, 'Драма'),
    (3, 'Мультфильм'),
    (4, 'Триллер'),
    (5, 'Документальный'),
    (6, 'Боевик');