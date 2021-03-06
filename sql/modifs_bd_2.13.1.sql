-- -- 2.13.1
-- -- Ajout taux de réduction annuel formule
ALTER TABLE module ADD taux_annu numeric;
-- -- Renommer colonne taux_mensuel
ALTER TABLE module RENAME taux_mensuel TO taux_mens;

-- SELECT * from siteweb where url like 'http://https://%';
UPDATE siteweb SET url = right(url, -7) WHERE url LIKE 'http://https://%';

-- Droits
ALTER TABLE menu2 ADD constraint menu2_pk PRIMARY KEY(id);
ALTER TABLE menu2 ADD constraint menu2_label_unique UNIQUE(label);

-- modifs_bd_2.9.4.10.sql
--ALTER TABLE menuprofil ADD CONSTRAINT menuprofil_pk PRIMARY KEY(idmenu,profil);

-- modifs_bd_2.9.4.10.sql
-- suppression doublons menuaccess
-- DELETE FROM menuaccess where oid IN (SELECT m2.oid from menuaccess m1, menuaccess m2 where m1.idper = m2.idper and m1.idmenu = m2.idmenu and m2.oid > m1.oid);
-- ALTER TABLE menuaccess ADD CONSTRAINT menuaccess_pk PRIMARY KEY(idper,idmenu);

INSERT INTO menu2 VALUES(73,'Room.modification.auth');
INSERT INTO menu2 VALUES(74,'Course.reading.auth');
INSERT INTO menu2 VALUES(75,'Schedule.modification.auth');

INSERT INTO menuprofil VALUES(73,0,false);
INSERT INTO menuprofil VALUES(73,1,true);
INSERT INTO menuprofil VALUES(73,2,false);
INSERT INTO menuprofil VALUES(73,3,false);
INSERT INTO menuprofil VALUES(73,4,true);
INSERT INTO menuprofil VALUES(73,10,false);
INSERT INTO menuprofil VALUES(73,11,false);

INSERT INTO menuprofil VALUES(74,0,false);
INSERT INTO menuprofil VALUES(74,1,true);
INSERT INTO menuprofil VALUES(74,2,false);
INSERT INTO menuprofil VALUES(74,3,false);
INSERT INTO menuprofil VALUES(74,4,true);
INSERT INTO menuprofil VALUES(74,10,false);
INSERT INTO menuprofil VALUES(74,11,false);

INSERT INTO menuprofil VALUES(75,0,false);
INSERT INTO menuprofil VALUES(75,1,true);
INSERT INTO menuprofil VALUES(75,2,false);
INSERT INTO menuprofil VALUES(75,3,false);
INSERT INTO menuprofil VALUES(75,4,true);
INSERT INTO menuprofil VALUES(75,10,false);
INSERT INTO menuprofil VALUES(75,11,false);

INSERT INTO menuaccess (select idper, 73, false from login where profil = 0);
INSERT INTO menuaccess (select idper, 73, true from login where profil = 1);
INSERT INTO menuaccess (select idper, 73, false from login where profil = 2);
INSERT INTO menuaccess (select idper, 73, false from login where profil = 3);
INSERT INTO menuaccess (select idper, 73, true from login where profil = 4);
INSERT INTO menuaccess (select idper, 73, false from login where profil = 10);
INSERT INTO menuaccess (select idper, 73, false from login where profil = 11);

INSERT INTO menuaccess (select idper, 74, false from login where profil = 0);
INSERT INTO menuaccess (select idper, 74, true from login where profil = 1);
INSERT INTO menuaccess (select idper, 74, false from login where profil = 2);
INSERT INTO menuaccess (select idper, 74, false from login where profil = 3);
INSERT INTO menuaccess (select idper, 74, true from login where profil = 4);
INSERT INTO menuaccess (select idper, 74, false from login where profil = 10);
INSERT INTO menuaccess (select idper, 74, false from login where profil = 11);

INSERT INTO menuaccess (select idper, 75, false from login where profil = 0);
INSERT INTO menuaccess (select idper, 75, true from login where profil = 1);
INSERT INTO menuaccess (select idper, 75, false from login where profil = 2);
INSERT INTO menuaccess (select idper, 75, false from login where profil = 3);
INSERT INTO menuaccess (select idper, 75, true from login where profil = 4);
INSERT INTO menuaccess (select idper, 75, false from login where profil = 10);
INSERT INTO menuaccess (select idper, 75, false from login where profil = 11);


-- menus manquants profils 10/11
INSERT INTO menuprofil select distinct idmenu,10,false from menuprofil where idmenu not in (SELECT distinct idmenu from menuprofil where profil = 10);
INSERT INTO menuprofil select distinct idmenu,11,false from menuprofil where idmenu not in (SELECT distinct idmenu from menuprofil where profil = 11);

-- acces menus manquants
INSERT INTO menuaccess SELECT idper,1,false FROM login WHERE profil = 11 AND idper NOT IN (SELECT idper FROM menuaccess WHERE idmenu = 1);
insert into menuaccess select idper,2,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 2);
insert into menuaccess select idper,3,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 3);
insert into menuaccess select idper,11,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 11);
insert into menuaccess select idper,12,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 12);
insert into menuaccess select idper,13,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 13);
insert into menuaccess select idper,14,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 14);
insert into menuaccess select idper,15,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 15);
insert into menuaccess select idper,21,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 21);
insert into menuaccess select idper,22,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 22);
insert into menuaccess select idper,23,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 23);
insert into menuaccess select idper,31,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 31);
insert into menuaccess select idper,32,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 32);
insert into menuaccess select idper,33,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 33);
insert into menuaccess select idper,34,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 34);
insert into menuaccess select idper,41,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 41);
insert into menuaccess select idper,42,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 42);
insert into menuaccess select idper,43,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 43);
insert into menuaccess select idper,44,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 44);
insert into menuaccess select idper,51,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 51);
insert into menuaccess select idper,52,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 52);
insert into menuaccess select idper,53,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 53);
insert into menuaccess select idper,61,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 61);
insert into menuaccess select idper,62,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 62);
insert into menuaccess select idper,63,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 63);
insert into menuaccess select idper,71,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 71);
insert into menuaccess select idper,72,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 72);
insert into menuaccess select idper,73,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 73);
insert into menuaccess select idper,74,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 74);
insert into menuaccess select idper,75,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 75);
insert into menuaccess select idper,81,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 81);
insert into menuaccess select idper,82,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 82);
insert into menuaccess select idper,101,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 101);
insert into menuaccess select idper,102,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 102);
insert into menuaccess select idper,105,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 105);
insert into menuaccess select idper,106,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 106);
insert into menuaccess select idper,107,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 107);
insert into menuaccess select idper,108,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 108);
insert into menuaccess select idper,111,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 111);
insert into menuaccess select idper,112,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 112);
insert into menuaccess select idper,113,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 113);
insert into menuaccess select idper,114,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 114);
insert into menuaccess select idper,115,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 115);
insert into menuaccess select idper,116,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 116);
insert into menuaccess select idper,117,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 117);
insert into menuaccess select idper,122,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 122);
insert into menuaccess select idper,123,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 123);
insert into menuaccess select idper,124,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 124);
insert into menuaccess select idper,125,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 125);
insert into menuaccess select idper,126,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 126);
insert into menuaccess select idper,127,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 127);
insert into menuaccess select idper,128,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 128);
insert into menuaccess select idper,129,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 129);
insert into menuaccess select idper,130,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 130);
insert into menuaccess select idper,133,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 133);
insert into menuaccess select idper,134,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 134);
insert into menuaccess select idper,135,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 135);
insert into menuaccess select idper,136,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 136);
insert into menuaccess select idper,137,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 137);
insert into menuaccess select idper,138,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 138);
insert into menuaccess select idper,139,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 139);
insert into menuaccess select idper,140,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 140);
insert into menuaccess select idper,141,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 141);
insert into menuaccess select idper,131,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 131);
insert into menuaccess select idper,132,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 132);
insert into menuaccess select idper,142,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 142);
insert into menuaccess select idper,143,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 143);
insert into menuaccess select idper,144,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 144);
insert into menuaccess select idper,145,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 145);
insert into menuaccess select idper,146,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 146);
insert into menuaccess select idper,147,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 147);
insert into menuaccess select idper,149,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 149);
insert into menuaccess select idper,150,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 150);
insert into menuaccess select idper,148,false from login where profil = 11 and idper not in (select idper from menuaccess where idmenu = 148);

INSERT INTO menuaccess SELECT idper,1,false FROM login WHERE profil = 2 AND idper NOT IN (SELECT idper FROM menuaccess WHERE idmenu = 1);
insert into menuaccess select idper,2,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 2);
insert into menuaccess select idper,3,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 3);
insert into menuaccess select idper,11,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 11);
insert into menuaccess select idper,12,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 12);
insert into menuaccess select idper,13,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 13);
insert into menuaccess select idper,14,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 14);
insert into menuaccess select idper,15,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 15);
insert into menuaccess select idper,21,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 21);
insert into menuaccess select idper,22,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 22);
insert into menuaccess select idper,23,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 23);
insert into menuaccess select idper,31,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 31);
insert into menuaccess select idper,32,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 32);
insert into menuaccess select idper,33,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 33);
insert into menuaccess select idper,34,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 34);
insert into menuaccess select idper,41,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 41);
insert into menuaccess select idper,42,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 42);
insert into menuaccess select idper,43,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 43);
insert into menuaccess select idper,44,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 44);
insert into menuaccess select idper,51,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 51);
insert into menuaccess select idper,52,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 52);
insert into menuaccess select idper,53,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 53);
insert into menuaccess select idper,61,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 61);
insert into menuaccess select idper,62,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 62);
insert into menuaccess select idper,63,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 63);
insert into menuaccess select idper,71,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 71);
insert into menuaccess select idper,72,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 72);
insert into menuaccess select idper,73,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 73);
insert into menuaccess select idper,74,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 74);
insert into menuaccess select idper,75,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 75);
insert into menuaccess select idper,81,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 81);
insert into menuaccess select idper,82,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 82);
insert into menuaccess select idper,101,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 101);
insert into menuaccess select idper,102,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 102);
insert into menuaccess select idper,105,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 105);
insert into menuaccess select idper,106,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 106);
insert into menuaccess select idper,107,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 107);
insert into menuaccess select idper,108,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 108);
insert into menuaccess select idper,111,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 111);
insert into menuaccess select idper,112,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 112);
insert into menuaccess select idper,113,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 113);
insert into menuaccess select idper,114,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 114);
insert into menuaccess select idper,115,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 115);
insert into menuaccess select idper,116,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 116);
insert into menuaccess select idper,117,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 117);
insert into menuaccess select idper,122,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 122);
insert into menuaccess select idper,123,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 123);
insert into menuaccess select idper,124,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 124);
insert into menuaccess select idper,125,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 125);
insert into menuaccess select idper,126,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 126);
insert into menuaccess select idper,127,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 127);
insert into menuaccess select idper,128,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 128);
insert into menuaccess select idper,129,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 129);
insert into menuaccess select idper,130,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 130);
insert into menuaccess select idper,133,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 133);
insert into menuaccess select idper,134,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 134);
insert into menuaccess select idper,135,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 135);
insert into menuaccess select idper,136,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 136);
insert into menuaccess select idper,137,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 137);
insert into menuaccess select idper,138,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 138);
insert into menuaccess select idper,139,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 139);
insert into menuaccess select idper,140,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 140);
insert into menuaccess select idper,141,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 141);
insert into menuaccess select idper,131,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 131);
insert into menuaccess select idper,132,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 132);
insert into menuaccess select idper,142,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 142);
insert into menuaccess select idper,143,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 143);
insert into menuaccess select idper,144,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 144);
insert into menuaccess select idper,145,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 145);
insert into menuaccess select idper,146,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 146);
insert into menuaccess select idper,147,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 147);
insert into menuaccess select idper,149,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 149);
insert into menuaccess select idper,150,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 150);
insert into menuaccess select idper,148,false from login where profil = 2 and idper not in (select idper from menuaccess where idmenu = 148);
