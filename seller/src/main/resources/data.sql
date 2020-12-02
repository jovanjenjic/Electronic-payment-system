INSERT IGNORE  INTO roles(name) VALUES('ROLE_PAYPAL');
INSERT IGNORE  INTO roles(name) VALUES('ROLE_BITCOIN');
INSERT IGNORE  INTO roles(name) VALUES('ROLE_BANK');
INSERT IGNORE  INTO roles(name) VALUES('ROLE_ADMIN');

INSERT IGNORE INTO `seller`
(`id`,`created_at`,`email`,`password`,`pib`)
VALUES
(1,"2020-12-01 20:45:01","paypal@paypal.com","$2a$10$XJBbi3IQzy9o6tkWHFX17OmkEJaTQ3LLND4ihyRoS/2AnybJ1xDG2",39949989);

INSERT IGNORE INTO `seller`
(`id`,`created_at`,`email`,`password`,`pib`)
VALUES
(2,"2020-12-01 20:45:01","bank@bank.com","$2a$10$XJBbi3IQzy9o6tkWHFX17OmkEJaTQ3LLND4ihyRoS/2AnybJ1xDG2",79345189);

INSERT IGNORE INTO `seller`
(`id`,`created_at`,`email`,`password`,`pib`)
VALUES
(3,"2020-12-01 20:45:01","bitcoin@bitcoin.com","$2a$10$XJBbi3IQzy9o6tkWHFX17OmkEJaTQ3LLND4ihyRoS/2AnybJ1xDG2",99426989);

INSERT IGNORE INTO `seller`
(`id`,`created_at`,`email`,`password`,`pib`)
VALUES
(4,"2020-12-01 20:45:01","admin@admin.com","$2a$10$XJBbi3IQzy9o6tkWHFX17OmkEJaTQ3LLND4ihyRoS/2AnybJ1xDG2",11111111);

INSERT IGNORE INTO `seller_roles`
(`seller_id`,`role_id`)
VALUES
(1, 1);

INSERT IGNORE INTO `seller_roles`
(`seller_id`,`role_id`)
VALUES
(2, 3);

INSERT IGNORE INTO `seller_roles`
(`seller_id`,`role_id`)
VALUES
(3, 2);


INSERT IGNORE INTO `seller_roles`
(`seller_id`,`role_id`)
VALUES
(4, 4);
