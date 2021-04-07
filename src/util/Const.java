/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import controller.BillOfLandingJpaController;
import controller.ContainerJpaController;
import controller.EscaleJpaController;
import controller.GeneralInfoJpaController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Admin
 */
public class Const {

    public static Connection CNX = null;
    public static ResourceBundle PROPERTIES = ResourceBundle.getBundle("config.dev_config");
    public static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    public static final String PRE_MAN_EXP = "Manifest";
    public static final String PRE_MAN_IMP = "Manifest";
    public static final String PRE_MAN = "";
    public static final String SUB_REPORT = "./data/";
    public static final String MAIN_REPORT = "./data/general";
    public static final String BOL_REPORT = "./data/bols";
    public static final String CTN_REPORT = "./data/ctn";
    public static  String CARGO_DRIVER = PROPERTIES.getString("cargo.driver");
    public static  String CARGO_FULL_URL = PROPERTIES.getString("cargo.fullurl");
    public static  String CARGO_USER = PROPERTIES.getString("cargo.user");
    public static  String CARGO_PASSWORD = PROPERTIES.getString("cargo.password");
    public static  String DOSSIER_MANIFEST_ERR = PROPERTIES.getString("dossier.manifest.err");
    public static  String DOSSIER_MANIFEST_IN = PROPERTIES.getString("dossier.manifest.in");
    public static  String DOSSIER_MANIFEST_ARC = PROPERTIES.getString("dossier.manifest.arc");
    public static  String DOSSIER_MANIFEST_OUT = PROPERTIES.getString("dossier.manifest.out");
    public static  String DOSSIER_FTP_IN = PROPERTIES.getString("ftp.comingManifest");
    public static  String DOSSIER_FTP_ARC = PROPERTIES.getString("ftp.archiveManifeste");
    public static  String DOSSIER_DC = PROPERTIES.getString("dossier.dc");
    public static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("ASYCUDAPU");
    public static final GeneralInfoJpaController GIJC = new GeneralInfoJpaController(EMF);
    public static final BillOfLandingJpaController BOLJC = new BillOfLandingJpaController(EMF);
    public static final ContainerJpaController CJC = new ContainerJpaController(EMF);
    public static final EscaleJpaController ESCJC = new EscaleJpaController(EMF);
    public static final Logger LOG = LoggerFactory.getLogger("LOG_RECORD");
    public static final referenceTable REF = new referenceTable();
    public static final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-HHmm");

    public static final String QUERY_SG = "INSERT INTO MANIFESTE_SEGMENT_GENERAL VALUES (SEQ_MANIFESTE_SEGMENT_GENERAL.nextval"
            + ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    public static final String SEQ_SG = "select SEQ_MANIFESTE_SEGMENT_GENERAL.currval from dual ";

    public static final String QUERY_BOL = "INSERT INTO MANIFESTE_BL VALUES (SEQ_MANIFESTE_BL.nextval"
            + ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SEQ_MANIFESTE_SEGMENT_GENERAL.currval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    public static final String SEQ_BOL = "select SEQ_MANIFESTE_BL.currval from dual ";

    public static final String QUERY_CTN = "INSERT INTO MANIFESTE_CONTENEUR VALUES (SEQ_MANIFESTE_CONTENEUR.nextval"
            + ",?,?,?,?,SEQ_MANIFESTE_BL.currval,?,?,?,?,?,?)";
    public static final String SEQ_CTN = "select SEQ_MANIFESTE_CONTENEUR.currval from dual ";

    public static final String QUERY_CT = "INSERT INTO CONGO_TERMINAL_CONTENEUR VALUES (SEQ_CONGO_TERMINAL_CONTENEUR.nextval"
                + ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    
    public static final String SEQ_CT = "select SEQ_CONGO_TERMINAL_CONTENEUR from dual ";

    public static final String QUERY_UPDATE_BOL = "UPDATE MANIFESTE_BL SET PLACE_OF_LOADING_CODE=?,PLACE_OF_UNLOADING_CODE=? WHERE BOL_REFERENCE=? AND ID_GENERAL=?";

}
