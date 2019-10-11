/*
 Navicat Premium Data Transfer

 Source Server         : data.db
 Source Server Type    : SQLite
 Source Server Version : 3026000
 Source Schema         : main

 Target Server Type    : SQLite
 Target Server Version : 3026000
 File Encoding         : 65001

 Date: 10/07/2019 11:00:21
*/

PRAGMA foreign_keys = false;

-- ----------------------------
-- Table structure for CUOCOD
-- ----------------------------
DROP TABLE IF EXISTS "CUOCOD";
CREATE TABLE "CUOCOD" (
  "code" TEXT,
  "libelle" TEXT
);

-- ----------------------------
-- Records of CUOCOD
-- ----------------------------
BEGIN;
INSERT INTO "CUOCOD" VALUES ('DD100', 'Direction Départ. de Pointe-Noire');
INSERT INTO "CUOCOD" VALUES ('DD141', 'Bureau Principal Port');
INSERT INTO "CUOCOD" VALUES ('DD142', 'Bureau des PTT');
INSERT INTO "CUOCOD" VALUES ('DD143', 'Bureau Princip. Ext. Aéroport');
INSERT INTO "CUOCOD" VALUES ('DD144', 'Bureau Nzassi');
INSERT INTO "CUOCOD" VALUES ('DD145', 'Bureau Raffinerie');
INSERT INTO "CUOCOD" VALUES ('DD146', 'CERDOC');
INSERT INTO "CUOCOD" VALUES ('DD147', 'Bureau Principal Hydrocarb & Bois');
INSERT INTO "CUOCOD" VALUES ('DD149', 'Bureau Lianzi');
INSERT INTO "CUOCOD" VALUES ('DD200', 'Direction Départ. de Brazzaville');
INSERT INTO "CUOCOD" VALUES ('DD210', 'CERDOC');
INSERT INTO "CUOCOD" VALUES ('DD211', 'Bureau NGABE');
INSERT INTO "CUOCOD" VALUES ('DD230', 'Bureau Principal Beach');
INSERT INTO "CUOCOD" VALUES ('DD231', 'Bureau Principal MAYA-MAYA');
INSERT INTO "CUOCOD" VALUES ('DD232', 'Bureau Poste');
INSERT INTO "CUOCOD" VALUES ('DD233', 'Bureau MINDOULI');
INSERT INTO "CUOCOD" VALUES ('DD234', 'Bureau Mbanzandounga');
INSERT INTO "CUOCOD" VALUES ('DD235', 'Bureau de BOKO');
INSERT INTO "CUOCOD" VALUES ('DD236', 'Bureau MPOUYA');
INSERT INTO "CUOCOD" VALUES ('DD237', 'Bureau de Kinkala');
INSERT INTO "CUOCOD" VALUES ('DD300', 'Direction Départ. Dolisie');
INSERT INTO "CUOCOD" VALUES ('DD361', 'Bureau Principal');
INSERT INTO "CUOCOD" VALUES ('DD362', 'Bureau Nkayi');
INSERT INTO "CUOCOD" VALUES ('DD363', 'Bureau Kimongo');
INSERT INTO "CUOCOD" VALUES ('DD364', 'Bureau Bambama');
INSERT INTO "CUOCOD" VALUES ('DD365', 'Bureau Mbinda');
INSERT INTO "CUOCOD" VALUES ('DD366', 'Bureau Loutété');
INSERT INTO "CUOCOD" VALUES ('DD367', 'Bureau Nyanga');
INSERT INTO "CUOCOD" VALUES ('DD368', 'CERDOC Dolisie');
INSERT INTO "CUOCOD" VALUES ('DD369', 'Bureau Mila-Mila');
INSERT INTO "CUOCOD" VALUES ('DD400', 'Direction Départementale Ouesso');
INSERT INTO "CUOCOD" VALUES ('DD451', 'Bureau Principal de Ouesso');
INSERT INTO "CUOCOD" VALUES ('DD452', 'Bureau Pokola');
INSERT INTO "CUOCOD" VALUES ('DD453', 'Bureau Ngbala');
INSERT INTO "CUOCOD" VALUES ('DD454', 'Bureau Yangandou');
INSERT INTO "CUOCOD" VALUES ('DD455', 'Bureau Ifo');
INSERT INTO "CUOCOD" VALUES ('DD457', 'Bureau Tala-Tala');
INSERT INTO "CUOCOD" VALUES ('DD458', 'Bureau Ngombé');
INSERT INTO "CUOCOD" VALUES ('DD460', 'Bureau Principal Impfondo');
INSERT INTO "CUOCOD" VALUES ('DD461', 'Bureau Enyellé');
INSERT INTO "CUOCOD" VALUES ('DD462', 'Bureau Mossaka');
INSERT INTO "CUOCOD" VALUES ('DD463', 'Bureau Loukoléla');
INSERT INTO "CUOCOD" VALUES ('DD464', 'Bureau Bétou');
INSERT INTO "CUOCOD" VALUES ('DD465', 'Bureau Dongou');
INSERT INTO "CUOCOD" VALUES ('DD466', 'Bureau Cristal');
INSERT INTO "CUOCOD" VALUES ('DD467', 'Bureau Lopola');
INSERT INTO "CUOCOD" VALUES ('DD468', 'Bureau Mokabi');
INSERT INTO "CUOCOD" VALUES ('DD469', 'Bureau Likouala Timber');
INSERT INTO "CUOCOD" VALUES ('DD470', 'CERDOC');
INSERT INTO "CUOCOD" VALUES ('DD471', 'Bureau Okoyo');
INSERT INTO "CUOCOD" VALUES ('DD472', 'Bureau Lékéty');
INSERT INTO "CUOCOD" VALUES ('DD473', 'Bureau Kellé');
INSERT INTO "CUOCOD" VALUES ('DD474', 'Bureau Mbomo');
INSERT INTO "CUOCOD" VALUES ('DD601', 'Bureau Principal de SIBITI');
INSERT INTO "CUOCOD" VALUES ('DG500', 'Direction Générale des Douanes');
INSERT INTO "CUOCOD" VALUES ('DG520', 'Direction Nationale des Enquêtes');
INSERT INTO "CUOCOD" VALUES ('DG521', 'Direction Contrôle des Services');
INSERT INTO "CUOCOD" VALUES ('DG522', 'Direction Législation Contentieux');
INSERT INTO "CUOCOD" VALUES ('DG523', 'Direction Adm. Ress. Humaines');
INSERT INTO "CUOCOD" VALUES ('DG524', 'Direction Etudes Prev. Informatiq.');
INSERT INTO "CUOCOD" VALUES ('DG525', 'Direction Fin. Compta. et Equip.');
INSERT INTO "CUOCOD" VALUES ('OW110', 'Bureau Test');
COMMIT;

PRAGMA foreign_keys = true;
