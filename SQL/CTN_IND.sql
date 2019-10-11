/*
 Navicat Premium Data Transfer

 Source Server         : data.db
 Source Server Type    : SQLite
 Source Server Version : 3026000
 Source Schema         : main

 Target Server Type    : SQLite
 Target Server Version : 3026000
 File Encoding         : 65001

 Date: 10/07/2019 11:00:01
*/

PRAGMA foreign_keys = false;

-- ----------------------------
-- Table structure for CTN_IND
-- ----------------------------
DROP TABLE IF EXISTS "CTN_IND";
CREATE TABLE "CTN_IND" (
  "code" TEXT,
  "libelle" TEXT
);

-- ----------------------------
-- Records of CTN_IND
-- ----------------------------
BEGIN;
INSERT INTO "CTN_IND" VALUES ('1/1', 'Plein complet');
INSERT INTO "CTN_IND" VALUES ('1/2', 'Demi plein');
INSERT INTO "CTN_IND" VALUES ('1/4', 'Quart plein');
INSERT INTO "CTN_IND" VALUES ('1/3', 'Tiers du plein');
INSERT INTO "CTN_IND" VALUES ('0/0', 'TC VIDE');
COMMIT;

PRAGMA foreign_keys = true;
