/*
 Navicat Premium Data Transfer

 Source Server         : data.db
 Source Server Type    : SQLite
 Source Server Version : 3026000
 Source Schema         : main

 Target Server Type    : SQLite
 Target Server Version : 3026000
 File Encoding         : 65001

 Date: 10/07/2019 11:02:06
*/

PRAGMA foreign_keys = false;

-- ----------------------------
-- Table structure for scan_docs
-- ----------------------------
DROP TABLE IF EXISTS "scan_docs";
CREATE TABLE "scan_docs" (
  "code" TEXT,
  "libelle" TEXT
);

-- ----------------------------
-- Records of scan_docs
-- ----------------------------
BEGIN;
INSERT INTO "scan_docs" VALUES ('001', 'LTA (lettre de transport aérien)');
INSERT INTO "scan_docs" VALUES ('002', 'Feuille de route');
INSERT INTO "scan_docs" VALUES ('004', 'Facture commerciale');
INSERT INTO "scan_docs" VALUES ('005', 'Facture assurance');
INSERT INTO "scan_docs" VALUES ('006', 'Facture transport (fret)');
INSERT INTO "scan_docs" VALUES ('008', 'Certificat de circulation');
INSERT INTO "scan_docs" VALUES ('007', 'Copie déclaration régime précedent');
INSERT INTO "scan_docs" VALUES ('009', 'Autorisation franchise(attest.exo)');
INSERT INTO "scan_docs" VALUES ('010', 'Note de détail');
INSERT INTO "scan_docs" VALUES ('011', 'Certificat phytosanitaire');
INSERT INTO "scan_docs" VALUES ('017', 'Liste de colisage');
INSERT INTO "scan_docs" VALUES ('020', 'Autorisation du régime suspensif');
INSERT INTO "scan_docs" VALUES ('024', 'Spécifications');
INSERT INTO "scan_docs" VALUES ('028', 'Connaissement');
INSERT INTO "scan_docs" VALUES ('029', 'Attest. vérification BIVAC');
INSERT INTO "scan_docs" VALUES ('021', 'AV SGS');
INSERT INTO "scan_docs" VALUES ('027', 'Déclaration export pays provenance');
INSERT INTO "scan_docs" VALUES ('025', 'Acquit à caution');
INSERT INTO "scan_docs" VALUES ('032', 'Engagement de change');
INSERT INTO "scan_docs" VALUES ('035', 'Formule domiciliation exportation');
INSERT INTO "scan_docs" VALUES ('018', 'Carte grise /facture(véhicule neuf)');
INSERT INTO "scan_docs" VALUES ('030', 'BORDE ELECTRO DE SUIVI CARGO(BESC)');
INSERT INTO "scan_docs" VALUES ('036', 'Copie Argus');
COMMIT;

PRAGMA foreign_keys = true;
