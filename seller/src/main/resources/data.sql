INSERT IGNORE  INTO roles(id, name) VALUES(1, 'ROLE_ADMIN');
INSERT IGNORE  INTO roles(id, name) VALUES(2, 'ROLE_USER');
-- ______________________________________________________________________________________________________________________________________________________
INSERT IGNORE INTO `seller`
(`id`,`created_at`,`email`,`password`,`pib`)
VALUES
(1,"2020-12-01 20:45:01","admin@admin.com","$2a$10$XJBbi3IQzy9o6tkWHFX17OmkEJaTQ3LLND4ihyRoS/2AnybJ1xDG2",1);

INSERT IGNORE INTO `seller`
(`id`,`created_at`,`email`,`password`,`pib`)
VALUES
(2,"2020-12-01 20:45:01","seller@seller.com","$2a$10$XJBbi3IQzy9o6tkWHFX17OmkEJaTQ3LLND4ihyRoS/2AnybJ1xDG2",79345189);

INSERT IGNORE INTO `seller_roles`
(`seller_id`,`role_id`)
VALUES
(1, 1);


INSERT IGNORE INTO `seller_roles`
(`seller_id`,`role_id`)
VALUES
(2, 2);
