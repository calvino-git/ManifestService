/*
 * To searchPortTRB this license header, choose License Headers in Project Properties.
 * To searchPortTRB this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manifestservice;

import asycuda.xml.Awmds;
import static base.Const.BOL_REPORT;
import static base.Const.CARGO_DRIVER;
import static base.Const.CARGO_FULL_URL;
import static base.Const.CARGO_PASSWORD;
import static base.Const.CARGO_USER;
import static base.Const.CTN_REPORT;
import static base.Const.DOSSIER_MANIFEST_ARC;
import static base.Const.DOSSIER_MANIFEST_ERR;
import static base.Const.DOSSIER_MANIFEST_IN;
import static base.Const.GIJC;
import static base.Const.LOG;
import static base.Const.MAIN_REPORT;
import static base.Const.SUB_REPORT;
import base.Function;
import static base.PersistObject.manifestToDB;
import static base.XmlObject.AwmdsToXml;
import dao.DbHandler;
import dao.Escale;
import jpa.beans.GeneralInfo;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.apache.commons.net.ftp.FTPClient;

/**
 *
 * @author calvin0
 */
public class ManifestService {
    //VARIABLES
    private static Connection conn;
    private static String queryString;
    private static String reportFilename;
    private static String mois;
    private static int id_manifest;
    private static boolean isManifest;
    private static HashMap<String, Object> reportParameters = new HashMap<>();
    private static JasperPrint jasperPrint;
    private static Awmds manifest;
    private static JasperReport jasperManiReport;
    private static FileWriter fileWriter = null;
    
    private static FTPClient ftp;
    private static Connection connection;
    private static File jrprint;
    
    //CONSTANTES
    
    
    public static void main(String[] args) {
//        allManifest();
        runAction();
    }

    public JasperPrint getJasperPrint() {
        return jasperPrint;
    }

    public void setJasperPrint(JasperPrint jasperPrint) {
        this.jasperPrint = jasperPrint;
    }

    public static String getReportFilename() {
        return reportFilename;
    }

    public static void setReportFilename(String reportFilename) {
        reportFilename = reportFilename;
    }

    public static HashMap<String, Object> getReportParameters() {
        return reportParameters;
    }

    public void setReportParameters(HashMap<String, Object> reportParameters) {
        this.reportParameters = reportParameters;
    }
    
    static void runAction() {
        //        importXml();
        String[] destXmlManifest = new String[2];
        String[] destPdfManifest = new String[2];
        File listManifest = new File(DOSSIER_MANIFEST_IN);
        File[] destXmlManifestFile = new File[2], destPdfManifestFile = new File[2];
//        while (true) 
        {
//            if (connect(false)) {
//
//                //Recuperation des manifestes s'ils existent
//                listFTPFiles(Config.PROPERTIES.getString("ftp.comingManifest"), Config.PROPERTIES.getString("dossier.manifest.in"), Config.PROPERTIES.getString("ftp.archiveManifeste"));
//
//                // deconnexion au FTP DOUANE
//                disconnect();
//            }
//            else {
//                LOG.info("Impossible de se connecer.");
//            }

            if (listManifest.listFiles() != null && listManifest.listFiles().length > 0) {
                for (File manifestFile : listManifest.listFiles()) {
                    
                    //Validation du fichier Manifest: renvoie 
                    manifest = Function.validationManifest(manifestFile,isManifest,DOSSIER_MANIFEST_ERR);

                    if (manifest != null) {
                        destXmlManifest = Function.renameManifest(manifest, manifestFile.getName());
                        
                        LOG.info("Formatage du nom terminé.");

                        if (destXmlManifest != null){
                            LOG.info(manifestFile.getName() + "/" + destXmlManifest[0]);
                            destPdfManifest[0] = destXmlManifest[0].substring(0, destXmlManifest[0].length() - 4) + ".pdf";
                            destPdfManifest[1] = destXmlManifest[1].substring(0, destXmlManifest[1].length() - 4) + ".pdf";
                            destXmlManifestFile[0] = new File(destXmlManifest[0]);
                            destPdfManifestFile[0] = new File(destPdfManifest[0]);
                            destXmlManifestFile[1] = new File(destXmlManifest[1]);
                            destPdfManifestFile[1] = new File(destPdfManifest[1]);
                            
                            LOG.info("============ DEBUT TRATITEMENT  ============");

//                            LOG.info("Nouveau ");
//                            Function.AwmdsToXml(manifest, destXmlManifestFile[0]);
//                            LOG.info(" Manifest classé : " + destXmlManifest[0]);
//                    try {
//                        fileWriter = new FileWriter(destXmlManifestFile);
//                        fileWriter.flush();
//                        fileWriter.close();
//                    } catch (IOException ex) {
//                        LOG.error(ex.getLocalizedMessage());
//                    }
                            try {
                                Class.forName(CARGO_DRIVER);
                            } catch (ClassNotFoundException ex) {
                                LOG.error(ex.getMessage());
                            }
                            try {
                                connection = DriverManager.getConnection(CARGO_FULL_URL, CARGO_USER, CARGO_PASSWORD);
                                LOG.info("===> CONNECTION :" + !connection.isClosed());
                            } catch (SQLException ex) {
                                LOG.error(ex.getMessage());
                            }

                            try {
                                persistenceXML(manifest, destXmlManifestFile[0]);
                            } catch (SQLException ex) {
                                LOG.error("Quelque chose a mal tourne du cote de la DB");
//                                manifestFile.renameTo(new File(Config.PROPERTIES.getString("dossier.manifest.err") + manifestFile.getName()));
//                                manifestFile.delete();
                                continue;
                            }
                            try {
                                generationPDF(id_manifest, destXmlManifestFile[0], destPdfManifestFile[0]);
                            } catch (JRException | SQLException | IOException ex) {
//                                manifestFile.renameTo(new File(Config.PROPERTIES.getString("dossier.manifest.err") + manifestFile.getName()));
//                                manifestFile.delete();
                                continue;
                            }

                            if (true) {
                                continue;
                            }
                            LOG.info(destXmlManifestFile[0].getAbsolutePath());
                            LOG.info(destXmlManifestFile[1].getAbsolutePath());
                            try {
                                if (destXmlManifestFile[1].exists()) {
                                    destXmlManifestFile[1].delete();
                                }
                                Files.copy(destXmlManifestFile[0].toPath(), destXmlManifestFile[1].toPath());
                                LOG.info("=======================================");
                                LOG.info("=============> DEBUT COPIE <===========");
                                LOG.info("===> Copie " + destXmlManifestFile[0].getAbsolutePath() + " vers " + destXmlManifestFile[1].getAbsolutePath());
                                if (destPdfManifestFile[1].exists()) {
                                    destPdfManifestFile[1].delete();
                                }
                                Files.copy(destPdfManifestFile[0].toPath(), destPdfManifestFile[1].toPath());
                                LOG.info("===> Copie " + destPdfManifestFile[0].getAbsolutePath() + " vers " + destPdfManifestFile[1].getAbsolutePath());
                                LOG.info("==============> FIN COPIE <============");
                                LOG.info("=======================================");
                                LOG.info("=======================================");
                                LOG.info("==========> DEBUT DEPLACEMENT <========");
                                LOG.info("===> Deplacement du contenu du dossier " + DOSSIER_MANIFEST_IN + " vers " + DOSSIER_MANIFEST_ARC);
                                File target = new File(DOSSIER_MANIFEST_ARC + manifestFile.getName());
                                if (target.exists()) {
                                    target.delete();
                                    LOG.info(target.getAbsolutePath() + " supprimé.");
                                }
                                if (manifestFile.exists()) {
                                    manifestFile.delete();
                                }
                                LOG.info("===========> FIN DEPLACEMENT <=========");
                                LOG.info("=======================================");
//                                if (Files.move(manifestFile.toPath(), target.toPath()).toFile().exists()) {
//                                    if (manifestFile.exists()) {
//                                        manifestFile.delete();
//                                    }
//                                    LOG.info("===> " + manifestFile.getAbsolutePath() + " archivé vers " + target.getAbsolutePath());
//                                }
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }

//                if (!manifestFile.getName().equals(destXmlManifest)) {
//                    if (manifestFile.delete()) {
//                        LOG.info(manifestFile.getName() + " supprimé ");
//                    }
//                }
                            LOG.info(destXmlManifest[1]);
                        } else {
                            continue;
                        }
                    }
                }
            }
//            else {
//                LOG.info("===> Dossier " + Config.PROPERTIES.getString("dossier.manifest.in") + " vide.");
//                LOG.info("===> En attente de nouveaux manifestes.");
//                System.out.print("Relance dans 60 secondes : ");
//                for (int i = 0; i < 60; i++) {
//
//                    try {
//                        System.out.print("*");
//                        Thread.sleep(1000);
//                    } catch (InterruptedException ex) {
//                        LOG.trace(ex.getMessage());
//                    }
//                }
//                LOG.info("");
//            }

        }
    }

    public static void allManifest() {
        try {
            conn = DbHandler.getDbConnection();
            String query = "select id from general_info";
            Statement stmt = conn.createStatement();
            stmt.execute(query);
            ResultSet list = stmt.getResultSet();
            while (list.next()) {
                LOG.info("" + list.getInt(1));
                GeneralInfo awmds = GIJC.findGeneralInfo(list.getInt(1));

            }
        } catch (SQLException ex) {
            LOG.info("");
        }

    }

    public static void generationPDF(int id, File xmlFile, File savefile) throws SQLException, IOException, JRException {
        setReportFilename(xmlFile.getName().substring(1, xmlFile.getName().length() - 4));
        String code = xmlFile.getName().substring(24, 34);
        String papn = xmlFile.getName();
        getReportParameters().put("id", id);
        getReportParameters().put("papn", papn);
        getReportParameters().put("code", code);
        reportParameters.put("SUBREPORT_DIR", SUB_REPORT);
        try {
            LOG.info("=======================================");
            LOG.info("==>DEBUT COMPILATION JRXML to JASPER<==");
            JasperCompileManager.compileReportToFile(CTN_REPORT + ".jrxml", CTN_REPORT + ".jasper");
            JasperCompileManager.compileReportToFile(BOL_REPORT + ".jrxml", BOL_REPORT + ".jasper");
            JasperCompileManager.compileReportToFile(MAIN_REPORT + ".jrxml", MAIN_REPORT +  ".jasper");
            jasperManiReport = JasperCompileManager.compileReport(MAIN_REPORT);
            LOG.info("===>FIN COMPILATION JRXML to JASPER<===");
            LOG.info("=======================================");
        } catch (JRException ex) {
            LOG.error("===> La compilation JRXML to JASPER a rencontré un problème [" + ex.getMessageKey() + "]: " + ex.getMessage());
            throw ex;
        }

        // Load the JDBC driver
        if (connection == null) {
            try {
                LOG.info("=======================================");
                LOG.info("========> CONNEXION A CARGO DB <=======");
                connection = DriverManager.getConnection(CARGO_FULL_URL, CARGO_USER, CARGO_PASSWORD);
                LOG.info("=========> CONNEXION ETABLIE <=========");
                LOG.info("=======================================");
            } catch (SQLException ex) {
                LOG.error("===> Probleme de connexion a la base de donnée " + ex.getSQLState() + "\n" + ex.getMessage());
                throw ex;
            }
        }

        try {
            LOG.info("=======================================");
            LOG.info("====>DEBUT CREATION DU FICHIER PDF<====");
            fileWriter = new FileWriter(savefile);
            fileWriter.flush();
            fileWriter.close();
            LOG.info("===> Création du fichier " + savefile.getName() + " avec succès!");
            LOG.info("=====>FIN CREATION DU FICHIER PDF<=====");
            LOG.info("=======================================");
        } catch (IOException ex) {
            LOG.error("Probleme d'ecriture du fichier " + savefile.getAbsolutePath() + "\n" + ex.getMessage());
            throw ex;
        }

        setReportFilename(savefile.getAbsolutePath());

        LOG.info("=======================================");
        LOG.info("====>DEBUT RENDU JASPER to JRPRINT<====");
        jrprint = new File(getReportFilename() + ".jrprint");
        try {
            JasperFillManager.fillReportToFile(jasperManiReport, jrprint.getAbsolutePath(), reportParameters, connection);
            LOG.info("=====>FIN RENDU JASPER to JRPRINT<=====");
            LOG.info("=======================================");
        } catch (JRException ex) {
            LOG.error("Probleme de conversion JASPER to JRPRINT [" + ex.getMessageKey() + "] : " + ex.getMessage());
            throw ex;
        }
        try {
            LOG.info("=======================================");
            LOG.info("=====>PHASE FINAL DU FICHIER PDF<======");
            LOG.info("===> Fichier PDF : " + savefile.getAbsolutePath());
            JasperExportManager.exportReportToPdfFile(jrprint.getAbsolutePath(), savefile.getAbsolutePath());
            LOG.info("===============> TERMINE <=============");
            LOG.info("=======================================");
            jrprint.delete();
        } catch (JRException e) {
            if (e.getMessage().contains("Permission denied")) {
                LOG.error("Vous n'avez pas le droit d'écrire ce repertoire");
            }
            throw e;
        }
        connection.close();
        if (connection.isClosed()) {
            LOG.info("DB deconnecté.");
        }
    }

    public static void persistenceXML(Awmds awmds, File xmlFile) throws SQLException {
        LOG.info("=======================================");
        LOG.info("===========>DEBUT PERSISTENCE XML<===========");
        awmds.getBolSegment().forEach(bol -> {
            if (bol.getBolId().getBolNature().equals("28") && awmds.getGeneralSegment().getLoadUnloadPlace().getPlaceOfDepartureCode().equals("CGPNR")) {
                bol.getBolId().setBolNature("29");
            }
        });
        //Determination du port de destination en transbordement 
        if (awmds.getGeneralSegment().getLoadUnloadPlace().getPlaceOfDepartureCode().equals("CGPNR") || awmds.getGeneralSegment().getLoadUnloadPlace().getPlaceOfDepartureCode().equals("CGPNP")) {
            mois = awmds.getGeneralSegment().getGeneralSegmentId().getDateOfDeparture().substring(0, 4) + awmds.getGeneralSegment().getGeneralSegmentId().getDateOfDeparture().substring(5, 7);
            LOG.info("Date de depart:" + awmds.getGeneralSegment().getGeneralSegmentId().getDateOfDeparture() + "\n Mois : " + mois);
        } else if (awmds.getGeneralSegment().getLoadUnloadPlace().getPlaceOfDestinationCode().equals("CGPNR") || awmds.getGeneralSegment().getLoadUnloadPlace().getPlaceOfDestinationCode().equals("CGPNP")) {
            mois = awmds.getGeneralSegment().getGeneralSegmentId().getDateOfArrival().substring(0, 4) + awmds.getGeneralSegment().getGeneralSegmentId().getDateOfArrival().substring(5, 7);
            LOG.info("Date d'arrivée:" + awmds.getGeneralSegment().getGeneralSegmentId().getDateOfArrival() + "\n Mois : " + mois);
        }

        if (!mois.equals(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE).substring(0, 6))) {
            LOG.info("===> Recherche dans le fichier de CONGO TERMINAL : " + mois);
//            PortDestination.searchPortTRB(awmds);
        }
//            MYWORKS.clear();
//            String name = xmlFile.getParent() + "/" + renameManifest(awmds, xmlFile.getName()) + ".xml";
//            LOG.info(name);
//            maniFile = new File(name);
//            maniFile = new File(xmlFile.getAbsolutePath().substring(0, xmlFile.getAbsolutePath().length() - 4) + ".xml");
        LOG.info("==> Enregistrement du nouveau XML traité");
        AwmdsToXml(awmds, xmlFile);
        String[] trim = xmlFile.getName().split("-");
        if (trim[4].isEmpty()) {
            trim[4] = "0";
        }
        Escale escale = new Escale("", "", "", "", trim[4], Integer.valueOf(trim[3]));
        try {
            id_manifest = manifestToDB(manifest, escale);// after stored in db, the pk id of the current manifest is returned
        } catch (SQLException ex) {
            LOG.error("Integration du manifeste a recontre un probleme [" + ex.getSQLState() + "] " + ex.getMessage());
            throw ex;
        }

        LOG.info("===========>FIN PERSISTENCE XML<===========");
        LOG.info("=======================================");
    }

    public static String getETB(String numero) throws SQLException {
        conn = DbHandler.getDbConnection();
        List<Escale> data = new ArrayList();
        queryString = "SELECT la_ligne  "
                + "FROM escales_douanes_papn  "
                + "WHERE substr(nom_fichier,11,10) like ?  ";
        String laligne = "";
        if (conn != null) {
            LOG.info("DB connected");
            PreparedStatement pst = conn.prepareStatement(queryString);
            LOG.info("********************************************");

            pst.setString(1, numero);
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                laligne = rst.getString("la_ligne");
            }
//            LOG.info("*****************ETB************************");
//            LOG.info("********************************************");
//            LOG.info(laligne);
//            LOG.info("********************************************");
            conn.close();
            LOG.info("********************************************");
        }
        return laligne;
    }  
}
