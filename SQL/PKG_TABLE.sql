/*
 Navicat Premium Data Transfer

 Source Server         : data.db
 Source Server Type    : SQLite
 Source Server Version : 3026000
 Source Schema         : main

 Target Server Type    : SQLite
 Target Server Version : 3026000
 File Encoding         : 65001

 Date: 10/07/2019 11:01:49
*/

PRAGMA foreign_keys = false;

-- ----------------------------
-- Table structure for PKG_TABLE
-- ----------------------------
DROP TABLE IF EXISTS "PKG_TABLE";
CREATE TABLE "PKG_TABLE" (
  "code" TEXT,
  "libelle" TEXT
);

-- ----------------------------
-- Records of PKG_TABLE
-- ----------------------------
BEGIN;
INSERT INTO "PKG_TABLE" VALUES (13, 'Rouleaux');
INSERT INTO "PKG_TABLE" VALUES (14, 'Colis à nu');
INSERT INTO "PKG_TABLE" VALUES (15, 'Vrac');
INSERT INTO "PKG_TABLE" VALUES (16, 'Bache');
INSERT INTO "PKG_TABLE" VALUES (17, 'Palette');
INSERT INTO "PKG_TABLE" VALUES (18, 'Support ( bois ou metal )');
INSERT INTO "PKG_TABLE" VALUES (19, 'Caisses en bois');
INSERT INTO "PKG_TABLE" VALUES (20, 'Caisses metallique');
INSERT INTO "PKG_TABLE" VALUES (21, 'Cartons');
INSERT INTO "PKG_TABLE" VALUES (22, 'Emballage en matiere plastique');
INSERT INTO "PKG_TABLE" VALUES (23, 'Sac en polypropylène ou en tissu');
INSERT INTO "PKG_TABLE" VALUES (24, 'Sac en papier');
INSERT INTO "PKG_TABLE" VALUES (25, 'Sac en plastique');
INSERT INTO "PKG_TABLE" VALUES (26, 'Balles');
INSERT INTO "PKG_TABLE" VALUES (27, 'Touque');
INSERT INTO "PKG_TABLE" VALUES (28, 'Bouteille');
INSERT INTO "PKG_TABLE" VALUES (29, 'Flacon');
INSERT INTO "PKG_TABLE" VALUES (30, 'Ampoule');
INSERT INTO "PKG_TABLE" VALUES (31, 'Enveloppe');
INSERT INTO "PKG_TABLE" VALUES (32, 'Sachet');
INSERT INTO "PKG_TABLE" VALUES (33, 'Valise');
INSERT INTO "PKG_TABLE" VALUES (34, 'Conteneur');
INSERT INTO "PKG_TABLE" VALUES (35, 'Bidon en matière plastique');
INSERT INTO "PKG_TABLE" VALUES (36, 'Bidon métallique');
INSERT INTO "PKG_TABLE" VALUES (37, 'Futs');
INSERT INTO "PKG_TABLE" VALUES (38, 'Citerne');
INSERT INTO "PKG_TABLE" VALUES (39, 'Tonneaux');
INSERT INTO "PKG_TABLE" VALUES (40, 'Tambours');
INSERT INTO "PKG_TABLE" VALUES (41, 'Casier à bouteilles');
INSERT INTO "PKG_TABLE" VALUES (42, 'Contenant isotherme');
INSERT INTO "PKG_TABLE" VALUES (43, 'Récipients métal pour gaz comprm.');
INSERT INTO "PKG_TABLE" VALUES (44, 'Emballage spec. explosif-dang.');
INSERT INTO "PKG_TABLE" VALUES (45, 'Cercueil');
INSERT INTO "PKG_TABLE" VALUES (46, 'Urne funéraire');
INSERT INTO "PKG_TABLE" VALUES (47, 'Conditionnés pour vente détail');
INSERT INTO "PKG_TABLE" VALUES (48, 'Non conditionnés vente détail');
INSERT INTO "PKG_TABLE" VALUES (49, 'Véhicule');
INSERT INTO "PKG_TABLE" VALUES (50, 'Parties de colis');
INSERT INTO "PKG_TABLE" VALUES (51, 'Bille de bois');
INSERT INTO "PKG_TABLE" VALUES (52, 'Fardeau');
INSERT INTO "PKG_TABLE" VALUES (53, 'Divers emballages');
COMMIT;

PRAGMA foreign_keys = true;
