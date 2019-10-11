
  CREATE TABLE "PPNCARGO"."CONGO_TERMINAL" 
   (	"ID" NUMBER(*,0), 
	"MOIS" NUMBER(*,0) NOT NULL ENABLE, 
	"NUM_CTN" VARCHAR2(50 BYTE) DEFAULT NULL, 
	"DAT" VARCHAR2(50 BYTE) DEFAULT NULL, 
	"MOUVEMENT" VARCHAR2(50 BYTE) DEFAULT NULL, 
	"TRAFIC" VARCHAR2(50 BYTE) DEFAULT NULL, 
	"VIDE_PLEIN" VARCHAR2(50 BYTE) DEFAULT NULL, 
	"ISO" VARCHAR2(50 BYTE) DEFAULT NULL, 
	"TARE" VARCHAR2(50 BYTE) DEFAULT NULL, 
	"EXP_COURS" VARCHAR2(50 BYTE) DEFAULT NULL, 
	"ESCALE" VARCHAR2(50 BYTE) DEFAULT NULL, 
	"VOYAGE" VARCHAR2(50 BYTE) DEFAULT NULL, 
	"POL" VARCHAR2(50 BYTE) DEFAULT NULL, 
	"POD" VARCHAR2(50 BYTE) DEFAULT NULL, 
	"ARMATEUR" VARCHAR2(50 BYTE) DEFAULT NULL, 
	"POIDS_BRUT" VARCHAR2(50 BYTE) DEFAULT NULL, 
	"DATE_ARR" VARCHAR2(50 BYTE) DEFAULT NULL, 
	"DATE_DEP" VARCHAR2(50 BYTE) DEFAULT NULL, 
	 PRIMARY KEY ("ID") DISABLE
   ) PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "TS_CARGO_I_1" ;
 