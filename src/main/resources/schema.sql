-- DROP VIEW APP_USER_DETAILS_VIEW IF EXISTS;
-- DROP TABLE APP_USER_ROLES IF EXISTS;
-- DROP TABLE APP_USERS IF EXISTS;

CREATE TABLE IF NOT EXISTS APP_USERS
(
    id     BIGINT AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_active BOOLEAN,
    PRIMARY KEY (id),
    UNIQUE KEY app_users_username_unique(username)
);

CREATE TABLE IF NOT EXISTS APP_USER_ROLES
(
    id     BIGINT AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    role_id INT  NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES APP_USERS(id),
    UNIQUE KEY app_user_roles_unique(user_id, role_id)
);

CREATE OR REPLACE VIEW APP_USER_DETAILS_VIEW as
select u.id as ID,
       u.username as USERNAME,
       u.password as PASSWORD,
       u.is_active as IS_ACTIVE,
       --sub-select is better, if using where user_id = ?
       IFNULL(LISTAGG( r.role_id, CHAR(13)  || CHAR(10)),'') as AUTHORITIES
from APP_USERS u
         left join APP_USER_ROLES r on r.user_id = u.id
group by u.ID,
         u.USERNAME,
         u.PASSWORD,
         u.IS_ACTIVE;