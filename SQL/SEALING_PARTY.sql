/*
 Navicat Premium Data Transfer

 Source Server         : data.db
 Source Server Type    : SQLite
 Source Server Version : 3026000
 Source Schema         : main

 Target Server Type    : SQLite
 Target Server Version : 3026000
 File Encoding         : 65001

 Date: 10/07/2019 11:01:59
*/

PRAGMA foreign_keys = false;

-- ----------------------------
-- Table structure for SEALING_PARTY
-- ----------------------------
DROP TABLE IF EXISTS "SEALING_PARTY";
CREATE TABLE "SEALING_PARTY" (
  "code" TEXT,
  "libelle" TEXT
);

-- ----------------------------
-- Records of SEALING_PARTY
-- ----------------------------
BEGIN;
INSERT INTO "SEALING_PARTY" VALUES ('TOP', 'Opérateur terminal');
INSERT INTO "SEALING_PARTY" VALUES ('DOU', 'Douane');
INSERT INTO "SEALING_PARTY" VALUES ('TRT', 'Transitaire');
COMMIT;

PRAGMA foreign_keys = true;
