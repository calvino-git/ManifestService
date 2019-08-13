/*
 Navicat Premium Data Transfer

 Source Server         : data.db
 Source Server Type    : SQLite
 Source Server Version : 3026000
 Source Schema         : main

 Target Server Type    : SQLite
 Target Server Version : 3026000
 File Encoding         : 65001

 Date: 10/07/2019 11:00:12
*/

PRAGMA foreign_keys = false;

-- ----------------------------
-- Table structure for CTN_TYPE
-- ----------------------------
DROP TABLE IF EXISTS "CTN_TYPE";
CREATE TABLE "CTN_TYPE" (
  "code" TEXT,
  "libelle" TEXT
);

-- ----------------------------
-- Records of CTN_TYPE
-- ----------------------------
BEGIN;
INSERT INTO "CTN_TYPE" VALUES (45, '45 pieds');
INSERT INTO "CTN_TYPE" VALUES (40, '40 pieds');
INSERT INTO "CTN_TYPE" VALUES (30, '30 pieds');
INSERT INTO "CTN_TYPE" VALUES (20, '20 pieds');
INSERT INTO "CTN_TYPE" VALUES (12, '12 pieds');
INSERT INTO "CTN_TYPE" VALUES (10, '10 pieds');
INSERT INTO "CTN_TYPE" VALUES ('08', '08 pieds');
INSERT INTO "CTN_TYPE" VALUES ('06', '06 pieds');
INSERT INTO "CTN_TYPE" VALUES ('00', 'TC VIDE');
COMMIT;

PRAGMA foreign_keys = true;
