/*
 * To searchPortTRB this license header, choose License Headers in Project Properties.
 * To searchPortTRB this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import asycuda.awmds.Awmds;
import asycuda.awmds.ObjectFactory;
import static util.Const.*;
import util.ManifestMethods;
import static util.PersistObject.manifestToDB;
import util.FtpClient;
import util.PortDestination;
import static util.XmlObject.AwmdsToXml;
import util.DbHandler;
import dao.Escale;
import model.GeneralInfo;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import model.BillOfLanding;
import model.Container;
import controller.EscaleJpaController;
import exception.IllegalOrphanException;
import exception.NonexistentEntityException;
import java.util.ResourceBundle;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.fonts.InvalidFontException;
import org.apache.commons.net.ftp.FTPClient;
import util.Const;
import util.PersistObject;
import static util.PersistObject.existsManifeste;

/**
 *
 * @author calvin0
 */
public class ManifesteService {

    //VARIABLES
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
    private static final FTPClient FTP = new FTPClient();
    private static File jrprint;
    static ObjectFactory obj = new ObjectFactory();

    public static void main(String[] args) {
//        try {
//            //        allManifest();
////            importCongoTerminal.main(args);
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
        String action = "main";
        if (args.length > 0) {
            action = args[0];
            Const.PROPERTIES = ResourceBundle.getBundle("config." + args[0] + "_config");
            Const.CARGO_DRIVER = PROPERTIES.getString("cargo.driver");
            Const.CARGO_FULL_URL = PROPERTIES.getString("cargo.fullurl");
            Const.CARGO_USER = PROPERTIES.getString("cargo.user");
            Const.CARGO_PASSWORD = PROPERTIES.getString("cargo.password");
            Const.DOSSIER_MANIFEST_ERR = PROPERTIES.getString("dossier.manifest.err");
            Const.DOSSIER_MANIFEST_IN = PROPERTIES.getString("dossier.manifest.in");
            Const.DOSSIER_MANIFEST_ARC = PROPERTIES.getString("dossier.manifest.arc");
            Const.DOSSIER_MANIFEST_OUT = PROPERTIES.getString("dossier.manifest.out");
            Const.DOSSIER_FTP_IN = PROPERTIES.getString("ftp.comingManifest");
            Const.DOSSIER_FTP_ARC = PROPERTIES.getString("ftp.archiveManifeste");
            Const.DOSSIER_DC = PROPERTIES.getString("dossier.dc");
        }

        runAction(action);
    }

    static void runAction(String action) {
        //        importXml();
        String[] destXmlManifest = new String[2];
        String[] destPdfManifest = new String[2];
        File listManifest = new File(DOSSIER_MANIFEST_IN);
        File[] destXmlManifestFile = new File[2], destPdfManifestFile = new File[2];
        while (true) {
//            if (action.equalsIgnoreCase("main")) {
//                if (FtpClient.connect(FTP, false)) {
////                LOG.info("CONNECTED : " + ftp.isConnected()) ;
//                    //Recuperation des manifestes s'ils existent
//                    FtpClient.listFTPFiles(FTP, DOSSIER_FTP_IN, DOSSIER_MANIFEST_IN, DOSSIER_FTP_ARC);
//
//                    // deconnexion au FTP DOUANE
//                    FtpClient.disconnect(FTP);
//                }
//            }
            boolean mois = false;
            if (listManifest.listFiles() != null && listManifest.listFiles().length > 0) {
                for (File manifestFile : listManifest.listFiles()) {
                    LOG.info("FICHIER " + manifestFile.getName());
                    String[] split = manifestFile.getName().split("-");
                    String numero_douane = "";
                    String date_enregistrement_douane = "";
                    if (split.length > 3) {
                        numero_douane = split[2] + "-" + split[3].replace(".xml", "");
                        if (split.length > 4) {
                            date_enregistrement_douane = split[4].replace(".xml", "");
                        }
                    }
                    
                    //Validation du fichier Manifest: renvoie 
                    manifest = ManifestMethods.validationManifest(manifestFile, isManifest, DOSSIER_MANIFEST_ERR);

//                    if (manifest.getGeneralSegment().getLoadUnloadPlace().getPlaceOfDepartureCode().equals("CGPNR") && manifest.getGeneralSegment().getGeneralSegmentId().getDateOfDeparture().startsWith("2019")) {
//                        mois = true;
//                    }
//                    if (manifest.getGeneralSegment().getLoadUnloadPlace().getPlaceOfDestinationCode().equals("CGPNR") && manifest.getGeneralSegment().getGeneralSegmentId().getDateOfArrival().startsWith("2019")) {
//                        mois = true;
//                    }
                    mois = true;
                    if (manifest != null && mois) {
                        destXmlManifest = ManifestMethods.renameManifest(manifest, manifestFile.getName());

                        if (destXmlManifest != null) {
                            LOG.info("======" + destXmlManifest[0] + "=====");
                            destPdfManifest[0] = destXmlManifest[0].substring(0, destXmlManifest[0].length() - 4) + ".pdf";
                            destPdfManifest[1] = destXmlManifest[1].substring(0, destXmlManifest[1].length() - 4) + ".pdf";
                            destXmlManifestFile[0] = new File(destXmlManifest[0]);
                            destPdfManifestFile[0] = new File(destPdfManifest[0]);
                            destXmlManifestFile[1] = new File(destXmlManifest[1]);
                            destPdfManifestFile[1] = new File(destPdfManifest[1]);

                            LOG.info("============ DEBUT TRATITEMENT  ============");

                            id_manifest = persistenceXML(manifest, destXmlManifestFile[0], numero_douane,date_enregistrement_douane);
//                                Awmds manif = getManifeste(id_manifest);
//                                AwmdsToXml(manif, destXmlManifestFile[0]);
                            try {
                                generationPDF(id_manifest, destXmlManifestFile[0], destPdfManifestFile[0]);
                            } catch (JRException | SQLException | IOException ex) {
//                                manifestFile.renameTo(new File(Config.PROPERTIES.getString("dossier.manifest.err") + manifestFile.getName()));
//                                manifestFile.delete();
                                continue;
                            }
                            LOG.info(destXmlManifestFile[0].getAbsolutePath());
                            LOG.info(destXmlManifestFile[1].getAbsolutePath());
                            try {
                                if (destXmlManifestFile[1].exists()) {
                                    destXmlManifestFile[1].delete();
                                }
                                Files.copy(destXmlManifestFile[0].toPath(), destXmlManifestFile[1].toPath(), StandardCopyOption.REPLACE_EXISTING);
                                LOG.info("=======================================");
                                LOG.info("=============> DEBUT COPIE <===========");
                                LOG.info("===> Copie " + destXmlManifestFile[0].getAbsolutePath() + " vers " + destXmlManifestFile[1].getAbsolutePath());
                                if (destPdfManifestFile[1].exists()) {
                                    destPdfManifestFile[1].delete();
                                }
                                Files.copy(destPdfManifestFile[0].toPath(), destPdfManifestFile[1].toPath(), StandardCopyOption.REPLACE_EXISTING);
                                LOG.info("===> Copie " + destPdfManifestFile[0].getAbsolutePath() + " vers " + destPdfManifestFile[1].getAbsolutePath());
                                LOG.info("==============> FIN COPIE <============");
                                LOG.info("=======================================");
                                LOG.info("=======================================");
                                LOG.info("==========> DEBUT DEPLACEMENT <========");
                                LOG.info("===> Deplacement du contenu du dossier " + DOSSIER_MANIFEST_IN + " vers " + DOSSIER_MANIFEST_ARC);
                                File target = null;

                                if (manifest.getGeneralSegment().getLoadUnloadPlace().getPlaceOfDepartureCode().startsWith("CGPN")) {
                                    if (destXmlManifestFile[0].getAbsolutePath().contains("ESCALE_NON_TROUVEE")) {
                                        target = new File(DOSSIER_MANIFEST_ARC + File.separator + "ESCALE_NON_TROUVEE" + File.separator + manifest.getGeneralSegment().getGeneralSegmentId().getDateOfDeparture().substring(0, 7)
                                                + File.separator + manifest.getGeneralSegment().getTransportInformation().getIdentityOfTransporter());
                                    } else {
                                        target = new File(DOSSIER_MANIFEST_ARC + File.separator + manifest.getGeneralSegment().getGeneralSegmentId().getDateOfDeparture().substring(0, 7)
                                                + File.separator + manifest.getGeneralSegment().getTransportInformation().getIdentityOfTransporter());
                                    }
                                }
                                if (manifest.getGeneralSegment().getLoadUnloadPlace().getPlaceOfDestinationCode().startsWith("CGPN")) {
                                    if (destXmlManifestFile[0].getAbsolutePath().contains("ESCALE_NON_TROUVEE")) {
                                        target = new File(DOSSIER_MANIFEST_ARC + File.separator + "ESCALE_NON_TROUVEE" + File.separator + manifest.getGeneralSegment().getGeneralSegmentId().getDateOfArrival().substring(0, 7)
                                                + File.separator + manifest.getGeneralSegment().getTransportInformation().getIdentityOfTransporter());
                                    } else {
                                        target = new File(DOSSIER_MANIFEST_ARC + File.separator + manifest.getGeneralSegment().getGeneralSegmentId().getDateOfArrival().substring(0, 7)
                                                + File.separator + manifest.getGeneralSegment().getTransportInformation().getIdentityOfTransporter());
                                    }
                                }

                                if (target != null) {
                                    if (!target.exists()) {
                                        Files.createDirectories(target.toPath());
                                    }
                                    Files.move(manifestFile.toPath(), target.toPath().resolve(manifestFile.toPath().getFileName()), StandardCopyOption.REPLACE_EXISTING);
                                    LOG.info(target.getAbsolutePath() + "/" + manifestFile.toPath().getFileName() + " crée.");
                                }

                                LOG.info("===========> FIN DEPLACEMENT <=========");
                                LOG.info("=======================================");

                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            LOG.info(destXmlManifest[1]);
                        } else {
                            continue;
                        }
                    }
                }
            }
        }
    }

    public static void allManifest() {
        try {
            CNX = DbHandler.getDbConnection();
            String query = "select id from general_info";
            Statement stmt = CNX.createStatement();
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
        if (id == 0) {
            throw new NullPointerException("id_manifest est egale à 0. verifier la connection avec DB");
        }
        setReportFilename(xmlFile.getName().substring(1, xmlFile.getName().length() - 4));
        EscaleJpaController ejc = new EscaleJpaController(EMF);;
        String code = null;
        try {
            Double id_escale = Double.valueOf(xmlFile.getName().substring(18, 23));
            model.Escale esc = ejc.findEscale(BigDecimal.valueOf(id_escale));
            code = esc.getNumero() + " du " + esc.getArrivee() + " au " + esc.getDepart();
        } catch (NumberFormatException ex) {
            LOG.error("ID ESCALE NON CONFORME : " + xmlFile.getName().substring(18, 23));
            code = "_________________ DU ________________ AU ________________";
        }

        String papn = xmlFile.getName();
        getReportParameters().put("id", id);
        getReportParameters().put("papn", papn);
        getReportParameters().put("trafic", "MANIFEST " + xmlFile.getName().substring(14, 16) + "PORT");
        getReportParameters().put("code", code);
        reportParameters.put("SUBREPORT_DIR", SUB_REPORT);
        try {
            LOG.info("=======================================");
            LOG.info("==>DEBUT COMPILATION JRXML to JASPER<==");
            if (Files.notExists(new File(CTN_REPORT + ".jasper").toPath())) {
                JasperCompileManager.compileReportToFile(CTN_REPORT + ".jrxml", CTN_REPORT + ".jasper");
            }
            if (Files.notExists(new File(BOL_REPORT + ".jasper").toPath())) {
                JasperCompileManager.compileReportToFile(BOL_REPORT + ".jrxml", BOL_REPORT + ".jasper");
            }
//            JasperCompileManager.compileReportToFile(MAIN_REPORT + ".jrxml", MAIN_REPORT +  ".jasper");
            jasperManiReport = JasperCompileManager.compileReport(MAIN_REPORT + ".jrxml");
            LOG.info("===>FIN COMPILATION JRXML to JASPER<===");
            LOG.info("=======================================");
        } catch (JRException ex) {
            ex.printStackTrace();
            LOG.error("===> La compilation JRXML to JASPER a rencontré un problème [" + ex.getMessageKey() + "]: " + ex.getMessage());
            throw ex;
        }

        // Load the JDBC driver
        if (CNX == null) {
            try {
                LOG.info("=======================================");
                LOG.info("========> CONNEXION A CARGO DB <=======");
                CNX = DriverManager.getConnection(CARGO_FULL_URL, CARGO_USER, CARGO_PASSWORD);
                LOG.info("=========> CONNEXION ETABLIE <=========");
                LOG.info("=======================================");
            } catch (SQLException ex) {
                LOG.error("===> Probleme de connection a la base de donnée " + ex.getSQLState() + "\n" + ex.getMessage());
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
            JasperFillManager.fillReportToFile(jasperManiReport, jrprint.getAbsolutePath(), reportParameters, CNX);
            LOG.info("=====>FIN RENDU JASPER to JRPRINT<=====");
            LOG.info("=======================================");
        } catch (JRException ex) {
            LOG.error("Probleme de conversion JASPER to JRPRINT [" + ex.getMessageKey() + "] : " + ex.getMessage());
            throw ex;
        } catch (InvalidFontException ex) {
            LOG.warn(" Error loading font family ");
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
        CNX.close();
        if (CNX.isClosed()) {
            LOG.info("DB deconnecté.");
        }
    }

    public static void deleteGI(int id) {
        try {
            GIJC.destroy(id);
        } catch (IllegalOrphanException | NonexistentEntityException ex) {
            LOG.error("GENERAL-INFO NON EXISTANT" + id);
        }

    }

    public static void deleteBL(int id) {
        try {
            BOLJC.destroy(id);
        } catch (IllegalOrphanException | NonexistentEntityException ex) {
            LOG.error("BOL NON EXISTANT" + id);
        }
    }

    public static void deleteCT(int id) {
        try {
            CJC.destroy(id);
        } catch (NonexistentEntityException ex) {
            LOG.error("CONTNEUR NON EXISTANT" + id);
        }
    }

    public static void deleteManifeste(int id) {
        GeneralInfo gi = GIJC.findGeneralInfo(id);
        gi.getBillOfLandingCollection().forEach(bl -> {
            bl.getContainerCollection().forEach(ct -> {
                deleteCT(ct.getIdCtn());

            });
            deleteBL(bl.getIdBol());
            System.out.print("=");
        });
        deleteGI(id);
        Logger.getLogger(ManifesteService.class.getName()).log(Level.SEVERE, "SUPPRESSION DU MANIFESTE DOUBLON ID : {0}", id);
    }

    public static int persistenceXML(Awmds awmds, File xmlFile, String numero_douane,String date_enregistrement_douane) {
        int id = 0;
        LOG.info("=======================================");
        LOG.info("===========>DEBUT PERSISTENCE XML<===========");

        awmds.getBolSegment().forEach(bol -> {
            if (bol.getLoadUnloadPlace().getPlaceOfLoadingCode().equalsIgnoreCase("CGPNP")) {
                bol.getLoadUnloadPlace().setPlaceOfLoadingCode("CGPNR");
            }
            if (bol.getLoadUnloadPlace().getPlaceOfUnloadingCode().equalsIgnoreCase("CGPNP")) {
                bol.getLoadUnloadPlace().setPlaceOfUnloadingCode("CGPNR");
            }
            try {
                bol.getLoadUnloadPlace().setPlaceOfLoadingCode(bol.getLoadUnloadPlace().getPlaceOfLoadingCode() == null ? "CGPNR" : bol.getLoadUnloadPlace().getPlaceOfLoadingCode());
                bol.getLoadUnloadPlace().setPlaceOfUnloadingCode(bol.getLoadUnloadPlace().getPlaceOfUnloadingCode() == null ? "CGPNR" : bol.getLoadUnloadPlace().getPlaceOfUnloadingCode());

                if (bol.getBolId().getBolNature().equals("28") && awmds.getGeneralSegment().getLoadUnloadPlace().getPlaceOfDepartureCode().equals("CGPNR")) {
                    bol.getBolId().setBolNature("29");
                }
            } catch (NullPointerException ex) {
                LOG.error("Un element null : " + ex.getLocalizedMessage());
//                }
            }
        });
        //Determination du mois du manifeste 
        if (awmds.getGeneralSegment().getLoadUnloadPlace().getPlaceOfDepartureCode().equals("CGPNR") || awmds.getGeneralSegment().getLoadUnloadPlace().getPlaceOfDepartureCode().equals("CGPNP") || awmds.getGeneralSegment().getLoadUnloadPlace().getPlaceOfDepartureCode().equals("CGHTM")) {
            mois = awmds.getGeneralSegment().getGeneralSegmentId().getDateOfDeparture().substring(0, 4) + awmds.getGeneralSegment().getGeneralSegmentId().getDateOfDeparture().substring(5, 7);
            LOG.info("Date de depart:" + awmds.getGeneralSegment().getGeneralSegmentId().getDateOfDeparture() + "\n Mois : " + mois);
        } else if (awmds.getGeneralSegment().getLoadUnloadPlace().getPlaceOfDestinationCode().equals("CGPNR") || awmds.getGeneralSegment().getLoadUnloadPlace().getPlaceOfDestinationCode().equals("CGPNP") || awmds.getGeneralSegment().getLoadUnloadPlace().getPlaceOfDestinationCode().equals("CGHTM")) {
            mois = awmds.getGeneralSegment().getGeneralSegmentId().getDateOfArrival().substring(0, 4) + awmds.getGeneralSegment().getGeneralSegmentId().getDateOfArrival().substring(5, 7);
            LOG.info("Date d'arrivée:" + awmds.getGeneralSegment().getGeneralSegmentId().getDateOfArrival() + "\n Mois : " + mois);
        }
        String[] trim = xmlFile.getName().split("-");
        if (trim[4].isEmpty()) {
            trim[4] = "0";
        }
        Escale escale = new Escale("", "", "", "", trim[4], Integer.valueOf(trim[3]), "");
        //Existence du manifeste dans la base de donnée
        LOG.info("VERIFICATION DE L'EXISTENCE DU MANIFESTES DANS LA BASE");
        id = existsManifeste(awmds);
        
        if (Integer.valueOf(mois) < Integer.valueOf(LocalDate.now().format(DateTimeFormatter.ofPattern("uuuuMM")))) {
            LOG.info("===> RECHERCHE DES PORTS EN TRANSBO DANS LA TABLE CONGO-TERMINAL DU MOIS : " + mois);
            PortDestination.searchPortTRB(awmds);
        }
        if(id != 0){
            LOG.info("VERIFICATION DE L'EXISTENCE DES DOUBLONS DANS LA BASE");
            int[] r = supprimerManifesteDoublon(awmds); //Renvoie le nombre de manifestes existants en double r[0] et le id du manifeste retenu r[1].
            id = r[1];
            int nbrManifestesSupprims = r[0];
            if(nbrManifestesSupprims>0){
                LOG.info("MANIFESTE(S) SUPPRIME(S) EN DOUBLE : " + nbrManifestesSupprims);
                LOG.info("MANIFESTE RETENU : " + id);
            }else{
                LOG.info("AUCUN DOUBLON");
            }
            
            LOG.info("===> MIS A JOUR DES PORTS EN TRANSBO POUR LE MANIFESTE  " + id);
            PersistObject.updateBolPort(awmds, escale, id,numero_douane,date_enregistrement_douane);
        }else{
            LOG.info("===> ENREGISTREMENT DU NOUVEAU MANIFESTE ");
            id = manifestToDB(awmds, escale, numero_douane,date_enregistrement_douane);
        }
     
        LOG.info("===> Enregistrement du nouveau XML traité");
        AwmdsToXml(awmds, xmlFile);
//        
        LOG.info("===========>FIN PERSISTENCE XML<===========");
        LOG.info("===========================================");
        return id;
    }

    public static String getETB(String numero) throws SQLException {
        CNX = DbHandler.getDbConnection();
        List<Escale> data = new ArrayList();
        queryString = "SELECT la_ligne  "
                + "FROM escales_douanes_papn  "
                + "WHERE substr(nom_fichier,11,10) like ?  ";
        String laligne = "";
        if (CNX != null) {
            LOG.info("DB connected");
            PreparedStatement pst = CNX.prepareStatement(queryString);
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
            CNX.close();
            LOG.info("********************************************");
        }
        return laligne;
    }

    private static Awmds getManifeste(int id_manifest) {
        Awmds man = new Awmds();
        CNX = DbHandler.getDbConnection();
        try {
            Statement stmt = CNX.createStatement();
            GeneralInfo gen = GIJC.findGeneralInfo(id_manifest);
            Awmds.GeneralSegment sg = xtractSegmentGeneral(gen, stmt);
            man.setGeneralSegment(sg);
            gen.getBillOfLandingCollection().stream().map((bol) -> {
                Awmds.BolSegment bl = xtractBol(bol, stmt);
                bol.getContainerCollection().stream().map((ctn) -> xtractctn(ctn)).forEachOrdered((ctnr) -> {
                    bl.getCtnSegment().add(ctnr);
                });
                return bl;
            }).forEachOrdered((bl) -> {
                man.getBolSegment().add(bl);
            });
            return man;
        } catch (SQLException ex) {
            LOG.error("Erreur de connexion " + ex.getSQLState() + " " + ex.getMessage());
            return null;
        }
    }

    private static int[] supprimerManifesteDoublon(Awmds awmds) {
        int[] r = new int[2];
        CNX = DbHandler.getDbConnection();
        String query = "select id from DSIPAPN.MANIFESTE_SEGMENT_GENERAL where "
                + "CUSTOMS_OFFICE_CODE like '"
                + awmds.getGeneralSegment().getGeneralSegmentId().getCustomsOfficeCode()
                + "' and VOYAGE_NUMBER like '"
                + awmds.getGeneralSegment().getGeneralSegmentId().getVoyageNumber()
                + "' and DATE_DEPARTURE like '"
                + awmds.getGeneralSegment().getGeneralSegmentId().getDateOfDeparture().substring(0, 10)
                + "' order by id desc";
        int i = 0;
        int id_ok = 0;
        int id_to_del = 0;
        try (Statement stmt = CNX.createStatement()) {
            ResultSet rst = stmt.executeQuery(query);
            while (rst.next()) {
                if (i == 0) {
                    id_ok = rst.getInt("id");
                    Logger.getLogger(ManifesteService.class.getName()).log(Level.SEVERE, "MANIFESTE ID : {0}", id_to_del);
                } else {
                    id_to_del = rst.getInt("id");
                    deleteManifeste(id_to_del);
                    i++;
                }
                
            }
            System.out.println(i);
        } catch (SQLException ex) {
            Logger.getLogger(ManifesteService.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        r[0] = i; //Nombre de manifestes suprimés
        r[1] = id_ok; //Manifeste à conserver
        return r;
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

    public static Awmds.GeneralSegment xtractSegmentGeneral(GeneralInfo sgDb, Statement stmt) {
        Awmds.GeneralSegment sgXml = obj.createAwmdsGeneralSegment();
        sgXml.setGeneralSegmentId(obj.createAwmdsGeneralSegmentGeneralSegmentId());
        sgXml.getGeneralSegmentId().setCustomsOfficeCode(sgDb.getCustomsOfficeCode());
        sgXml.getGeneralSegmentId().setVoyageNumber(sgDb.getVoyageNumber());
        sgXml.getGeneralSegmentId().setDateOfDeparture(sgDb.getDateDeparture());
        sgXml.getGeneralSegmentId().setDateOfArrival(sgDb.getDateArrival());
        sgXml.getGeneralSegmentId().setTimeOfArrival(sgDb.getTimeArrival());

        sgXml.setTransportInformation(obj.createAwmdsGeneralSegmentTransportInformation());
        sgXml.getTransportInformation().setCarrier(obj.createAwmdsGeneralSegmentTransportInformationCarrier());
        sgXml.getTransportInformation().getCarrier().setCarrierCode(sgDb.getCarrierCode());
        sgXml.getTransportInformation().getCarrier().setCarrierName(sgDb.getCarrierName());
        sgXml.getTransportInformation().getCarrier().setCarrierAddress(sgDb.getCarrierAddress());

        sgXml.getTransportInformation().setIdentityOfTransporter(sgDb.getIdentityTransporter());
        sgXml.getTransportInformation().setMasterInformation(sgDb.getMasterInformation());
        sgXml.getTransportInformation().setModeOfTransportCode(sgDb.getModeTransport());

        sgXml.getTransportInformation().setNationalityOfTransporterCode(sgDb.getNationalityTransporter());
        sgXml.getTransportInformation().setPlaceOfTransporter(sgDb.getPlaceOfTransporter());

        sgXml.setLoadUnloadPlace(obj.createAwmdsGeneralSegmentLoadUnloadPlace());
        try {
            ResultSet rst = stmt.executeQuery("select code from papn_locode where libelle like '" + sgDb.getPlaceOfDepartureCode() + "'");
            if (rst.next()) {
                sgXml.getLoadUnloadPlace().setPlaceOfDepartureCode(rst.getString("code"));
            }
        } catch (SQLException ex) {
            LOG.error(ex.getSQLState() + " : " + ex.getLocalizedMessage());
            LOG.error("SG ID : " + sgDb.getId());
        }
        try {
            ResultSet rst = stmt.executeQuery("select code from papn_locode where libelle like '" + sgDb.getPlaceOfDestinationCode() + "'");
            if (rst.next()) {
                sgXml.getLoadUnloadPlace().setPlaceOfDestinationCode(rst.getString("code"));
            }
        } catch (SQLException ex) {
            LOG.error(ex.getSQLState() + " : " + ex.getLocalizedMessage());
            LOG.error("SG ID : " + sgDb.getId());
        }
        sgXml.setTotalsSegment(obj.createAwmdsGeneralSegmentTotalsSegment());
        sgXml.getTotalsSegment().setTotalGrossMass(sgDb.getTotalGrossMass());
        sgXml.getTotalsSegment().setTotalNumberOfBols(sgDb.getTotalNumberOfBols());
        sgXml.getTotalsSegment().setTotalNumberOfContainers(sgDb.getTotalNumberOfContainers());
        sgXml.getTotalsSegment().setTotalNumberOfPackages(sgDb.getTotalNumberOfPackages());

        sgXml.setTonnage(obj.createAwmdsGeneralSegmentTonnage());

        sgXml.getTonnage().setTonnageGrossWeight(sgDb.getTonnageGrossWeight());
        sgXml.getTonnage().setTonnageNetWeight(sgDb.getTonnageNetWeight());

//        afficher(sgDb.toString());
        return sgXml;
    }

    public static Awmds.BolSegment xtractBol(BillOfLanding bol, Statement stmt) {
        Awmds.BolSegment ab = obj.createAwmdsBolSegment();
        ab.setBolId(obj.createAwmdsBolSegmentBolId());
        REF.nature.entrySet().stream().filter((entry) -> (entry.getValue().equalsIgnoreCase(bol.getBolNature()))).forEachOrdered((entry) -> {
            ab.getBolId().setBolNature(entry.getKey());
        });
        ab.getBolId().setBolReference(bol.getBolReference());
        ab.getBolId().setBolTypeCode(bol.getBolTypeCode());
        ab.getBolId().setLineNumber(obj.createAwmdsBolSegmentBolIdLineNumber());
        ab.getBolId().getLineNumber().setValue(bol.getLineNumber());

        ab.setLoadUnloadPlace(obj.createAwmdsBolSegmentLoadUnloadPlace());
        try {
            ResultSet rst = stmt.executeQuery("select code from papn_locode where libelle like '" + bol.getPlaceOfLoadingCode() + "'");
            if (rst.next()) {
                ab.getLoadUnloadPlace().setPlaceOfLoadingCode(rst.getString("code"));
            }
        } catch (SQLException ex) {
            LOG.error(ex.getSQLState() + " : " + ex.getLocalizedMessage());
            LOG.error("BOL ID : " + bol.getIdBol());
        }
        try {
            ResultSet rst = stmt.executeQuery("select code from papn_locode where libelle like '" + bol.getPlaceOfUnloadingCode() + "'");
            if (rst.next()) {
                ab.getLoadUnloadPlace().setPlaceOfUnloadingCode(rst.getString("code"));
            }
        } catch (SQLException ex) {
            LOG.error(ex.getSQLState() + " : " + ex.getLocalizedMessage());
            LOG.error("BOL ID : " + bol.getIdBol());
        }
        ab.setLocation(obj.createAwmdsBolSegmentLocation());
        ab.getLocation().setLocationCode(bol.getLocationCode());
        ab.getLocation().setLocationInfo(bol.getLocationInfo());

        ab.setTradersSegment(obj.createAwmdsBolSegmentTradersSegment());
        ab.getTradersSegment().setConsignee(obj.createAwmdsBolSegmentTradersSegmentConsignee());
        ab.getTradersSegment().getConsignee().setConsigneeName(bol.getConsigneeName());
        ab.getTradersSegment().getConsignee().setConsigneeAddress(bol.getConsigneeAddress());
        ab.getTradersSegment().setExporter(obj.createAwmdsBolSegmentTradersSegmentExporter());
        ab.getTradersSegment().getExporter().setExporterName(bol.getExporterName());
        ab.getTradersSegment().getExporter().setExporterAddress(bol.getExporterAddress());
        ab.getTradersSegment().setNotify(obj.createAwmdsBolSegmentTradersSegmentNotify());
        ab.getTradersSegment().getNotify().setNotifyName(bol.getNotifyName());
        ab.getTradersSegment().getNotify().setNotifyAddress(bol.getNotifyAddress());

        ab.setGoodsSegment(obj.createAwmdsBolSegmentGoodsSegment());
        ab.getGoodsSegment().setGoodsDescription(bol.getGoodsDescription());
        ab.getGoodsSegment().setGrossMass(bol.getGrossMass());
        ab.getGoodsSegment().setNumOfCtnForThisBol((int) bol.getNumOfCtnForThisBol());
        ab.getGoodsSegment().setNumberOfPackages((int) bol.getNumberOfPackages());

        REF.pkg_table.entrySet().stream().filter((entry) -> (entry.getValue().equalsIgnoreCase(bol.getPackageTypeCode()))).forEachOrdered((entry) -> {
            ab.getGoodsSegment().setPackageTypeCode(entry.getKey());
        });
        ab.getGoodsSegment().setShippingMarks(bol.getShippingMarks());
        ab.getGoodsSegment().setVolumeInCubicMeters(bol.getVolumeInCubicMeters());
        ab.setValueSegment(obj.createAwmdsBolSegmentValueSegment());
//        afficher(bol.toString());
        return ab;
    }

    public static Awmds.BolSegment.CtnSegment xtractctn(Container ctn) {
        Awmds.BolSegment.CtnSegment abc = obj.createAwmdsBolSegmentCtnSegment();

        abc.setCtnReference(ctn.getCtnReference());
        abc.setTypeOfContainer(ctn.getTypeOfContainer());
        abc.setNumberOfPackages(Integer.valueOf(ctn.getNumberOfPackages()));
        abc.setEmptyFull(ctn.getEmptyFull());
        abc.setEmptyWeight(ctn.getEmptyWeight().isEmpty() || ctn.getEmptyWeight().equals("null") ? 0.0 : Double.valueOf(ctn.getEmptyWeight()));
        abc.setGoodsWeight(ctn.getGoodsWeight().isEmpty() || ctn.getEmptyWeight().equals("null") ? 0.0 : Double.valueOf(ctn.getGoodsWeight()));
        abc.setMarks1(ctn.getMarks1());
        abc.setMarks2(ctn.getMarks2());
        abc.setMarks3(ctn.getMarks3());
        abc.setSealingParty(ctn.getSealingParty());
//        afficher(ctn.toString());
        return abc;
    }
}
