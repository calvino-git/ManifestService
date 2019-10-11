/*
 Navicat Premium Data Transfer

 Source Server         : data.db
 Source Server Type    : SQLite
 Source Server Version : 3026000
 Source Schema         : main

 Target Server Type    : SQLite
 Target Server Version : 3026000
 File Encoding         : 65001

 Date: 10/07/2019 11:01:32
*/

PRAGMA foreign_keys = false;

-- ----------------------------
-- Table structure for MODE_TRANS
-- ----------------------------
DROP TABLE IF EXISTS "MODE_TRANS";
CREATE TABLE "MODE_TRANS" (
  "code" TEXT,
  "libelle" TEXT
);

-- ----------------------------
-- Records of MODE_TRANS
-- ----------------------------
BEGIN;
INSERT INTO "MODE_TRANS" VALUES (10, 'TRANSPORT MARITIME');
INSERT INTO "MODE_TRANS" VALUES (12, 'WAGON SUR NAVIRE DE MER');
INSERT INTO "MODE_TRANS" VALUES (16, 'VEHICULE ROUTIER SUR NAVIRE');
INSERT INTO "MODE_TRANS" VALUES (17, 'REMORQUE OU S/R SUR NAVIRE');
INSERT INTO "MODE_TRANS" VALUES (18, 'BATEAU SUR NAVIRE MARITIME');
INSERT INTO "MODE_TRANS" VALUES (20, 'TRANSPORT PAR CHEMIN DE FER');
INSERT INTO "MODE_TRANS" VALUES (23, 'VEHICULE ROUTIER SUR TRAIN');
INSERT INTO "MODE_TRANS" VALUES (30, 'TRANSPORT PAR ROUTE');
INSERT INTO "MODE_TRANS" VALUES (40, 'TRANSPORT PAR AIR');
INSERT INTO "MODE_TRANS" VALUES (50, 'ENVOIS POSTAUX');
INSERT INTO "MODE_TRANS" VALUES (80, 'TRANSPORT PAR NAVIGATION INTERIEURE');
INSERT INTO "MODE_TRANS" VALUES (90, 'PROPULSION PROPRE');
INSERT INTO "MODE_TRANS" VALUES (60, 'PIPE LINE');
COMMIT;

PRAGMA foreign_keys = true;