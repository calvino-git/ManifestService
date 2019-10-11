/*
 Navicat Premium Data Transfer

 Source Server         : data.db
 Source Server Type    : SQLite
 Source Server Version : 3026000
 Source Schema         : main

 Target Server Type    : SQLite
 Target Server Version : 3026000
 File Encoding         : 65001

 Date: 10/07/2019 11:01:40
*/

PRAGMA foreign_keys = false;

-- ----------------------------
-- Table structure for NATURE
-- ----------------------------
DROP TABLE IF EXISTS "NATURE";
CREATE TABLE "NATURE" (
  "code" TEXT,
  "libelle" TEXT
);

-- ----------------------------
-- Records of NATURE
-- ----------------------------
BEGIN;
INSERT INTO "NATURE" VALUES (22, 'Exportation');
INSERT INTO "NATURE" VALUES (23, 'Importation');
INSERT INTO "NATURE" VALUES (24, 'Transit');
INSERT INTO "NATURE" VALUES (28, 'Transbordement');
COMMIT;

PRAGMA foreign_keys = true;
