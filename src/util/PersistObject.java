/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import asycuda.awmds.Awmds;
import static util.Const.*;
import dao.Escale;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import model.BillOfLanding;
import model.Container;
import model.GeneralInfo;
import main.ManifesteService;
import static main.ManifesteService.deleteManifeste;

/**
 *
 * @author Admin
 */
public class PersistObject {

    public static String getPortLibelle(String portCode, Statement stmt) {
        String port = null;
        try {
            ResultSet rst = stmt.executeQuery("select libelle from DSIPAPN.LOCODE where code like '" + portCode + "'");
            if (rst.next()) {
                port = rst.getString("libelle");
                return port;
            }
        } catch (SQLException ex) {
            LOG.error(ex.getSQLState() + " : " + ex.getLocalizedMessage());
            return portCode;
        }
        return portCode;
    }

    public static int manifestToDB(Awmds cargo, Escale escale, String numero_douane, String date_enregistrement_douane) {
        Statement id = null;
        ResultSet resultSet = null;
        int id_gen;
        LOG.info("=======================================");
        LOG.info("========> DEBUT - INSERTION DANS LA BASE DE DONNEE <======");

        try {
            CNX = DbHandler.getDbConnection();
            id = CNX.createStatement();
            Statement stmt = CNX.createStatement();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-HHmm");
            String[] dateheure = format.format(new Date()).split("-");

            try (PreparedStatement insertGen = CNX.prepareStatement(QUERY_SG)) {

//                insertGen.setInt(1, id_gen);
                CNX.setAutoCommit(false);
                insertGen.setString(1, cargo.getGeneralSegment().getTransportInformation().getCarrier().getCarrierAddress());
                insertGen.setString(2, cargo.getGeneralSegment().getTransportInformation().getCarrier().getCarrierCode());
                insertGen.setString(3, cargo.getGeneralSegment().getTransportInformation().getCarrier().getCarrierName());
                insertGen.setString(4, cargo.getGeneralSegment().getGeneralSegmentId().getCustomsOfficeCode());
                insertGen.setString(5, cargo.getGeneralSegment().getGeneralSegmentId().getDateOfArrival().substring(0, 10));
                insertGen.setString(6, cargo.getGeneralSegment().getGeneralSegmentId().getDateOfDeparture().substring(0, 10));
                insertGen.setString(7, cargo.getGeneralSegment().getGeneralSegmentId().getDateOfLastDischarge() != null ? cargo.getGeneralSegment().getGeneralSegmentId().getDateOfLastDischarge().substring(0, 10) : "");
                insertGen.setString(8, date_enregistrement_douane);
                insertGen.setString(9, cargo.getGeneralSegment().getTransportInformation().getIdentityOfTransporter());
                insertGen.setString(10, cargo.getGeneralSegment().getTransportInformation().getMasterInformation());
                insertGen.setString(11, REF.mode_trans.get(cargo.getGeneralSegment().getTransportInformation().getModeOfTransportCode()));
                insertGen.setString(12, cargo.getGeneralSegment().getTransportInformation().getNationalityOfTransporterCode());
                insertGen.setString(13, escale.getNumero());
                insertGen.setString(14, getPortLibelle(cargo.getGeneralSegment().getLoadUnloadPlace().getPlaceOfDepartureCode(), stmt));
                insertGen.setString(15, getPortLibelle(cargo.getGeneralSegment().getLoadUnloadPlace().getPlaceOfDestinationCode(), stmt));

                insertGen.setString(16, cargo.getGeneralSegment().getTransportInformation().getPlaceOfTransporter());
                insertGen.setString(17, cargo.getGeneralSegment().getTransportInformation().getRegistrationNumberOfTransportCode());
                insertGen.setString(18, cargo.getGeneralSegment().getTransportInformation().getShippingAgent() == null ? "" : cargo.getGeneralSegment().getTransportInformation().getShippingAgent().getShippingAgentCode());
                insertGen.setString(19, cargo.getGeneralSegment().getTransportInformation().getShippingAgent() == null ? "" : cargo.getGeneralSegment().getTransportInformation().getShippingAgent().getShippingAgentName());
                insertGen.setString(20, cargo.getGeneralSegment().getGeneralSegmentId().getTimeOfArrival());
                insertGen.setDouble(21, cargo.getGeneralSegment().getTonnage() == null ? 0.0 : cargo.getGeneralSegment().getTonnage().getTonnageGrossWeight());
                insertGen.setDouble(22, cargo.getGeneralSegment().getTonnage() == null ? 0.0 : cargo.getGeneralSegment().getTonnage().getTonnageNetWeight());
                insertGen.setDouble(23, cargo.getGeneralSegment().getTotalsSegment().getTotalNumberOfBols());
                insertGen.setDouble(24, cargo.getGeneralSegment().getTotalsSegment().getTotalGrossMass());
                insertGen.setDouble(25, cargo.getGeneralSegment().getTotalsSegment().getTotalNumberOfContainers());
                insertGen.setDouble(26, cargo.getGeneralSegment().getTotalsSegment().getTotalNumberOfPackages());
                insertGen.setString(27, cargo.getGeneralSegment().getGeneralSegmentId().getVoyageNumber());
                insertGen.setInt(28, escale.getEscleunik());
                insertGen.setString(29, dateheure[0]);
                insertGen.setString(30, dateheure[1]);
                insertGen.setString(31, numero_douane);
                insertGen.setString(32, cargo.getGeneralSegment().getLoadUnloadPlace().getPlaceOfDepartureCode().equalsIgnoreCase("CGPNR") ? "EXP" : "IMP");
                insertGen.setString(33, null);
                insertGen.setString(34, null);
                insertGen.setString(35, null);

                insertGen.executeUpdate();
            }

            int i = 0;
            int j = 0;
            try (PreparedStatement insertBol = CNX.prepareStatement(QUERY_BOL)) {

                for (Awmds.BolSegment bol : cargo.getBolSegment()) {
                    try {
//                        insertBol.setInt(1, ++id_bol);
                        insertBol.setString(1, " ");
                        insertBol.setString(2, REF.nature.get(bol.getBolId().getBolNature()));
                        insertBol.setString(3, bol.getBolId().getBolReference());
                        insertBol.setString(4, bol.getBolId().getBolTypeCode());
                        insertBol.setString(5, bol.getTradersSegment().getConsignee().getConsigneeAddress());
                        insertBol.setString(6, bol.getTradersSegment().getConsignee().getConsigneeCode());
                        insertBol.setString(7, bol.getTradersSegment().getConsignee().getConsigneeName());
                        insertBol.setString(8, "");
                        insertBol.setDouble(9, 0);
                        insertBol.setString(10, bol.getTradersSegment().getExporter().getExporterAddress());
                        insertBol.setString(11, bol.getTradersSegment().getExporter().getExporterCode());
                        insertBol.setString(12, bol.getTradersSegment().getExporter().getExporterName());
                        insertBol.setString(13, "");
                        insertBol.setDouble(14, 0.0);
                        insertBol.setString(15, bol.getGoodsSegment().getGoodsDescription());
                        insertBol.setDouble(16, bol.getGoodsSegment().getGrossMass());
//                        insertBol.setInt(17, id_gen);
                        insertBol.setString(17, bol.getGoodsSegment().getInformation());
                        insertBol.setString(18, "");
                        insertBol.setDouble(19, 0.0);
                        insertBol.setInt(20, bol.getBolId().getLineNumber().getValue());
                        insertBol.setString(21, bol.getLocation().getLocationCode());
                        insertBol.setString(22, bol.getLocation().getLocationInfo());
                        insertBol.setString(23, bol.getBolId().getMasterBolRefNumber());
                        insertBol.setString(24, bol.getTradersSegment().getNotify().getNotifyAddress());
                        insertBol.setString(25, bol.getTradersSegment().getNotify().getNotifyCode());
                        insertBol.setString(26, bol.getTradersSegment().getNotify().getNotifyName());
                        insertBol.setDouble(27, bol.getGoodsSegment().getNumOfCtnForThisBol());
                        insertBol.setDouble(28, bol.getGoodsSegment().getNumberOfPackages());
                        insertBol.setString(29, REF.pkg_table.get(bol.getGoodsSegment().getPackageTypeCode()));
                        insertBol.setString(30, getPortLibelle(bol.getLoadUnloadPlace().getPlaceOfLoadingCode(), stmt));
                        insertBol.setString(31, getPortLibelle(bol.getLoadUnloadPlace().getPlaceOfUnloadingCode(), stmt));
                        insertBol.setString(32, bol.getGoodsSegment().getShippingMarks());
                        insertBol.setString(33, "");
                        insertBol.setDouble(34, 0.0);
                        insertBol.setString(35, bol.getBolId().getUniqueCarrierReference());
                        insertBol.setDouble(36, bol.getGoodsSegment().getVolumeInCubicMeters() == null ? 0.0 : bol.getGoodsSegment().getVolumeInCubicMeters());

                        if (insertBol.executeUpdate() > 0) {
                            i++;
                        }

//                        resultSet = id.executeQuery(SEQ_BOL);
//                        resultSet.next();
//                        int id_bol = resultSet.getInt(1);
//                        LOG.info("===> Bol N° " + i++ + " insere. ID = " + id_bol);
//                        LOG.info("===> NOMBRE DE CONTENEURS " + bol.getGoodsSegment().getNumOfCtnForThisBol());
                        try (PreparedStatement insertCtn = CNX.prepareStatement(QUERY_CTN)) {
                            for (Awmds.BolSegment.CtnSegment ctn : bol.getCtnSegment()) {
                                try {
//                                    insertCtn.setInt(1, ++id_ctn);
                                    insertCtn.setString(1, ctn.getCtnReference());
                                    insertCtn.setString(2, String.valueOf(ctn.getEmptyFull()));
                                    insertCtn.setString(3, String.valueOf(ctn.getEmptyWeight()));
                                    insertCtn.setString(4, ctn.getGoodsWeight() == null ? "" : String.valueOf(ctn.getGoodsWeight()));
                                    insertCtn.setString(5, ctn.getMarks1());
                                    insertCtn.setString(6, ctn.getMarks2());
                                    insertCtn.setString(7, ctn.getMarks3());
                                    insertCtn.setString(8, String.valueOf(ctn.getNumberOfPackages()));
                                    insertCtn.setString(9, ctn.getSealingParty());
                                    String taille = "20";
                                    if (ctn.getTypeOfContainer().length() > 2) {
                                        switch (ctn.getTypeOfContainer().substring(0, 1)) {
                                            case "2":
                                                taille = "20";
                                                break;
                                            case "4":
                                                taille = "40";
                                                break;
                                            case "3":
                                                taille = "30";
                                                break;
                                            default:
                                                taille = "20";
                                        }
                                    } else {
                                        taille = ctn.getTypeOfContainer();
                                    }

                                    insertCtn.setString(10, taille);

                                    if (insertCtn.executeUpdate() > 0) {
                                        j++;
                                    }
//                                    resultSet = id.executeQuery(SEQ_CTN);
//                                    resultSet.next();
//                                    int id_ctn = resultSet.getInt(1);
//                                    LOG.info("=> Conteneur N° " + j++ + " insere. ID = " + id_ctn);
//                                    LOG.info("=> NOMBRE DE COLIS " + ctn.getNumberOfPackages());
                                } catch (SQLException ex) {
//                                    LOG.warn("CONTENEUR N° " + id_ctn);
                                    if (ex instanceof java.sql.SQLSyntaxErrorException) {
                                        if (ex.getMessage().contains("ORA-02289")) {
                                            LOG.error("ORA-02289: " + "SEQ_PAPN_CONTAINER " + "sequence does not exist");
                                            ex.printStackTrace();
                                        }
                                    }
                                    ex.printStackTrace();
                                }
                            }
                        }
                    } catch (SQLException ex) {
//                        LOG.warn("BOL N° " + id_bol);
                        if (ex instanceof java.sql.SQLSyntaxErrorException) {
                            if (ex.getMessage().contains("ORA-02289")) {
                                LOG.error("ORA-02289: " + "SEQ_PAPN_BILLOFLANDING " + "sequence does not exist");
                                ex.printStackTrace();
                            }
                        }
                        ex.printStackTrace();
                    }
                }
            }
            resultSet = id.executeQuery(SEQ_SG);
            resultSet.next();
            id_gen = resultSet.getInt(1);
            LOG.info("===> SEGMENT GENERAL insere avec succes id = " + id_gen);
            LOG.info("===> NOMBRE DE BOLs : " + i + "/" + cargo.getGeneralSegment().getTotalsSegment().getTotalNumberOfBols());
            LOG.info("===> NOMBRE DE CONTENEURS : " + j + "/" + cargo.getGeneralSegment().getTotalsSegment().getTotalNumberOfContainers());

            CNX.commit();
            LOG.info("========> FIN - INSERTION DANS LA BASE DE DONNEE <======");
            LOG.info("=======================================");

            return id_gen;
        } catch (SQLException ex) {
////            LOG.warn("MANIFESTE " + id_gen);
            if (ex instanceof java.sql.SQLSyntaxErrorException) {
                if (ex.getMessage().contains("ORA-02289")) {
                    LOG.error("ORA-02289: " + "SEQ_PAPN_GENERAL_INFO " + "sequence does not exist");
                    ex.printStackTrace();
                } else {
                    LOG.error("Integration du manifeste a recontre un probleme [" + ex.getSQLState() + "] " + ex.getMessage());
                }
            }
            ex.printStackTrace();
        }
        return 0;
    }

    public static int existsManifeste(Awmds awmds) {
        CNX = DbHandler.getDbConnection();
        try {
            String depart = awmds.getGeneralSegment().getGeneralSegmentId().getDateOfDeparture();
            LOG.info("select id,escleunik from MANIFESTE_SEGMENT_GENERAL where "
                    + "CUSTOMS_OFFICE_CODE like '"
                    + awmds.getGeneralSegment().getGeneralSegmentId().getCustomsOfficeCode()
                    + "' and VOYAGE_NUMBER like '"
                    + awmds.getGeneralSegment().getGeneralSegmentId().getVoyageNumber()
                    + "' and DATE_DEPARTURE like '"
                    + (depart.length() > 10 ? depart.substring(0, 10) : depart)
                    + "' order by id desc");
            ResultSet rst = CNX.createStatement().executeQuery("select id,escleunik from MANIFESTE_SEGMENT_GENERAL where "
                    + "CUSTOMS_OFFICE_CODE like '"
                    + awmds.getGeneralSegment().getGeneralSegmentId().getCustomsOfficeCode()
                    + "' and VOYAGE_NUMBER like '"
                    + awmds.getGeneralSegment().getGeneralSegmentId().getVoyageNumber()
                    + "' and DATE_DEPARTURE like '"
                    + (depart.length() > 10 ? depart.substring(0, 10) : depart)
                    + "' order by id desc");
            if (rst.next()) {
                int id = rst.getInt("id");
                LOG.info("MANIFESTE TROUVE: " + id);
                return id;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManifesteService.class.getName()).log(Level.SEVERE, null, ex);
        }
        LOG.info("MANIFESTE NON EXISTANT");
        return 0;
    }

    public static void updateBolPort(Awmds awmds, Escale escale, int id, String numero_douane, String date_enregistrement_douane) {
        LOG.info("=======================================");
        LOG.info("========> DEBUT - MIS A JOUR DES PORTS DANS LA BASE DE DONNEE <======");
        int i = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-HHmm");
        String[] dateheure = format.format(new Date()).split("-");
        if (id != 0) {
            try {
                CNX = DbHandler.getDbConnection();
                Statement stmt = CNX.createStatement();
                String query1 = "update MANIFESTE_SEGMENT_GENERAL set ESCLEUNIK =?, NUMERO_ESCALE=?,DATE_UPDATE=?,HEURE_UPDATE=?,NUMERO_DOUANE='" + numero_douane + "',DATE_OF_REGISTRATION=?,TRAFIC=? where id=?";
                String query2 = "update MANIFESTE_SEGMENT_GENERAL set ESCLEUNIK =?, NUMERO_ESCALE=?,DATE_UPDATE=?,HEURE_UPDATE=?,DATE_OF_REGISTRATION=?,TRAFIC=? where id=?";
                String query = numero_douane.isEmpty() ? query2 : query1;
                try (PreparedStatement updateGi = CNX.prepareStatement(query)) {
                    updateGi.setInt(1, escale.getEscleunik());
                    updateGi.setString(2, escale.getNumero());
                    updateGi.setString(3, dateheure[0]);
                    updateGi.setString(4, dateheure[1]);
                    updateGi.setString(5, date_enregistrement_douane);
                    updateGi.setString(6, awmds.getGeneralSegment().getLoadUnloadPlace().getPlaceOfDepartureCode().equalsIgnoreCase("CGPNR") ? "EXP" : "IMP");
                    updateGi.setInt(7, id);

                    if (updateGi.executeUpdate() == 1) {
                        LOG.info("Escale du manifeste id : " + id + " mis à jour avec numero d'escale " + escale.getNumero());
                    };
                }

//                insertGen.setInt(1, id_gen);
                
                List<Awmds.BolSegment> list = awmds.getBolSegment()
                        .stream().filter(
                                bl -> (has(bl.getBolId().getBolNature()) && has(bl.getLoadUnloadPlace().getPlaceOfUnloadingCode()) && has(bl.getBolId().getBolNature()) && bl.getBolId().getBolNature().equalsIgnoreCase("28") && !bl.getLoadUnloadPlace().getPlaceOfUnloadingCode().equalsIgnoreCase("CGPNR"))
                                || (has(bl.getBolId().getBolNature()) && has(bl.getLoadUnloadPlace().getPlaceOfLoadingCode()) && has(bl.getBolId().getBolNature()) && bl.getBolId().getBolNature().equalsIgnoreCase("29") && !bl.getLoadUnloadPlace().getPlaceOfLoadingCode().equalsIgnoreCase("CGPNR"))
                        ).collect(Collectors.toList());
                Connection connexion = DbHandler.getDbConnection();
                connexion.setAutoCommit(true);
                Statement updateBol = connexion.createStatement();
                for (Awmds.BolSegment bol : list) {
//                    try (PreparedStatement updateBol = CNX.prepareStatement(QUERY_UPDATE_BOL)) {
//                        updateBol.setString(1, getPortLibelle(bol.getLoadUnloadPlace().getPlaceOfLoadingCode(), stmt));
//                        updateBol.setString(2, getPortLibelle(bol.getLoadUnloadPlace().getPlaceOfUnloadingCode(), stmt));
//                        updateBol.setString(3, bol.getBolId().getBolReference());
//                        updateBol.setInt(4, id);
                    String queryBol = "UPDATE MANIFESTE_BL SET PLACE_OF_LOADING_CODE='" + getPortLibelle(bol.getLoadUnloadPlace().getPlaceOfLoadingCode(), stmt)
                            + "',PLACE_OF_UNLOADING_CODE='" + getPortLibelle(bol.getLoadUnloadPlace().getPlaceOfUnloadingCode(), stmt)
                            + "' WHERE BOL_REFERENCE= '" + bol.getBolId().getBolReference()
                            + "' AND ID_GENERAL=" + id;

                    updateBol.addBatch(queryBol);
                    LOG.info("==>" + queryBol + "<==");
                    i++;
//                    }
                }
                int[] test = updateBol.executeBatch();
//                connexion.commit();
                int succes = 0;
                int echec = 0;
                int rien = 0;
                for (int j = 0; j < test.length; j++) {
                    switch (j) {
                        case 1:
                            succes++;
                            break;
                        case 0:
                            echec++;
                            break;
                        default:
                            rien++;
                            break;
                    }
                }
                
                LOG.info("===>MIS A JOUR BL " + succes + " SUCCES.");
                LOG.info("===>MIS A JOUR BL" + echec + " ECHOUE.");
                LOG.info("===>MIS A JOUR BL" + rien + " NULL.");
                
                LOG.info("========> FIN- MIS A JOUR DES PORTS DANS LA BASE DE DONNEE AVEC BOLs MIS A JOUR : " + i + " <======");
            } catch (SQLException ex) {
//                        LOG.warn("BOL N° " + id_bol);
                if (ex instanceof java.sql.SQLSyntaxErrorException) {
                    if (ex.getMessage().contains("ORA-02289")) {
                        LOG.error("ORA-02289: " + "SEQ_PAPN_BILLOFLANDING " + "sequence does not exist");
                        ex.printStackTrace();
                    }
                }
                ex.printStackTrace();
            }
        }
    }

    /**
     * Recuperation des informations generales du manifest La methode prend en
     * parametre un manifest complet et retourne un objet contenant les info
     * generales
     */
    public static GeneralInfo getGeneral(Awmds awmds) throws IOException, FileNotFoundException, InterruptedException {
        GeneralInfo general = new GeneralInfo();
        int j = 0;
        general.setCustomsOfficeCode(awmds.getGeneralSegment().getGeneralSegmentId().getCustomsOfficeCode());
        general.setVoyageNumber(awmds.getGeneralSegment().getGeneralSegmentId().getVoyageNumber());
        general.setDateDeparture(awmds.getGeneralSegment().getGeneralSegmentId().getDateOfDeparture());
        general.setDateArrival(awmds.getGeneralSegment().getGeneralSegmentId().getDateOfArrival());
        general.setDateLastDischarge(awmds.getGeneralSegment().getGeneralSegmentId().getDateOfLastDischarge());
        general.setTimeArrival(awmds.getGeneralSegment().getGeneralSegmentId().getTimeOfArrival());

        //recuperer les donnees du segment <TotalsSegment>
        general.setTotalNumberOfPackages(awmds.getGeneralSegment().getTotalsSegment().getTotalNumberOfPackages());
        general.setTotalNumberOfContainers(awmds.getGeneralSegment().getTotalsSegment().getTotalNumberOfContainers());
        general.setTotalGrossMass(awmds.getGeneralSegment().getTotalsSegment().getTotalGrossMass());
        general.setTotalNumberOfBols(awmds.getGeneralSegment().getTotalsSegment().getTotalNumberOfBols());

        //recuperation des donnes du segment <Transport_informations>
        //segment <Carrier>
        general.setCarrierName(awmds.getGeneralSegment().getTransportInformation().getCarrier().getCarrierName());
        general.setCarrierCode(awmds.getGeneralSegment().getTransportInformation().getCarrier().getCarrierCode());
        general.setCarrierAddress(awmds.getGeneralSegment().getTransportInformation().getCarrier().getCarrierAddress());

        general.setModeTransport(awmds.getGeneralSegment().getTransportInformation().getModeOfTransportCode());
        general.setIdentityTransporter(awmds.getGeneralSegment().getTransportInformation().getIdentityOfTransporter());
        general.setNationalityTransporter(awmds.getGeneralSegment().getTransportInformation().getNationalityOfTransporterCode());

        if (awmds.getGeneralSegment().getTransportInformation().getMasterInformation() == null) {
            general.setMasterInformation("DEFAULT");
        }

        if (awmds.getGeneralSegment().getTransportInformation().getPlaceOfTransporter() != null) {
            general.setPlaceOfTransporter(awmds.getGeneralSegment().getTransportInformation().getPlaceOfTransporter());
        }

        general.setRegistrationNumber(awmds.getGeneralSegment().getTransportInformation().getRegistrationNumberOfTransportCode());
        general.setDateOfRegistration(awmds.getGeneralSegment().getTransportInformation().getDateOfRegistration());

        //segment <Shipping agent>
        if (awmds.getGeneralSegment().getTransportInformation().getShippingAgent() != null) {
            general.setShippingAgentName(awmds.getGeneralSegment().getTransportInformation().getShippingAgent().getShippingAgentName());
            general.setShippingAgentCode(awmds.getGeneralSegment().getTransportInformation().getShippingAgent().getShippingAgentCode());
        }
        general.setPlaceOfDepartureCode(awmds.getGeneralSegment().getLoadUnloadPlace().getPlaceOfDepartureCode());
        general.setPlaceOfDestinationCode(awmds.getGeneralSegment().getLoadUnloadPlace().getPlaceOfDestinationCode());

        if (awmds.getGeneralSegment().getTonnage() != null) {
            general.setTonnageNetWeight(awmds.getGeneralSegment().getTonnage().getTonnageNetWeight());
            general.setTonnageGrossWeight(awmds.getGeneralSegment().getTonnage().getTonnageGrossWeight());
        }
        return general;
    }

    /**
     * La methode prend en parametre un objet manifest complet et un objet info
     * general puis retourne une liste de bill of landing
     *
     * @param awmds
     * @param general
     * @return
     */
    protected static List<BillOfLanding> getBol(Awmds awmds, GeneralInfo general) throws NullPointerException {
        ArrayList bols = new ArrayList();
        BillOfLanding bl = new BillOfLanding();

        awmds.getBolSegment().stream().forEach((bol) -> {
            try {
                bl.setBolNature(bol.getBolId().getBolNature());
                bl.setBolReference(bol.getBolId().getBolReference());
                bl.setBolTypeCode(bol.getBolId().getBolTypeCode());

                if (bol.getTradersSegment().getConsignee().getConsigneeAddress() != null) {
                    bl.setConsigneeAddress(bol.getTradersSegment().getConsignee().getConsigneeAddress());
                } else {
                    bl.setConsigneeAddress(" ");
                }

                bl.setConsigneeCode(bol.getTradersSegment().getConsignee().getConsigneeCode());
                bl.setConsigneeName(bol.getTradersSegment().getConsignee().getConsigneeName());

                if (!(bol.getValueSegment().getCustomsSegment() == null)) {
                    bl.setCustomsCurrency(bol.getValueSegment().getCustomsSegment().getCustomsCurrency());
                    bl.setCustomsValue(bol.getValueSegment().getCustomsSegment().getCustomsValue());
                }
                bl.setExporterAddress(bol.getTradersSegment().getExporter().getExporterAddress());
                bl.setExporterCode(bol.getTradersSegment().getExporter().getExporterCode());
                bl.setExporterName(bol.getTradersSegment().getExporter().getExporterName());
                bl.setGoodsDescription(bol.getGoodsSegment().getGoodsDescription());
                bl.setGrossMass(bol.getGoodsSegment().getGrossMass());
                bl.setInformation(bol.getGoodsSegment().getInformation());
                if (!(bol.getValueSegment().getInsuranceSegment() == null)) {
                    bl.setInsuranceCurrency(bol.getValueSegment().getInsuranceSegment().getInsuranceCurrency());
                    bl.setInsuranceValue(bol.getValueSegment().getInsuranceSegment().getInsuranceValue());
                }
                bl.setLineNumber(bol.getBolId().getLineNumber().getValue());
                bl.setLocationCode(bol.getLocation().getLocationCode());
                bl.setLocationInfo(bol.getLocation().getLocationInfo());
                bl.setMasterBolRefNumber(bol.getBolId().getMasterBolRefNumber());
                bl.setNotifyAddress(bol.getTradersSegment().getNotify().getNotifyAddress());
                bl.setNotifyCode(bol.getTradersSegment().getNotify().getNotifyCode());
                bl.setNotifyName(bol.getTradersSegment().getNotify().getNotifyName());
                bl.setNumberOfPackages((int) bol.getGoodsSegment().getNumberOfPackages());
                bl.setNumOfCtnForThisBol(bol.getGoodsSegment().getNumOfCtnForThisBol());
                bl.setPackageTypeCode(bol.getGoodsSegment().getPackageTypeCode());

                bl.setPlaceOfLoadingCode(bol.getLoadUnloadPlace().getPlaceOfLoadingCode());
                bl.setPlaceOfUnloadingCode(bol.getLoadUnloadPlace().getPlaceOfUnloadingCode());

                bl.setShippingMarks(bol.getGoodsSegment().getShippingMarks());
                if (!(bol.getValueSegment().getTransportSegment() == null)) {
                    bl.setTransportCurrency(bol.getValueSegment().getTransportSegment().getTransportCurrency());
                    bl.setTransportValue(bol.getValueSegment().getTransportSegment().getTransportValue());
                }
                if (!(bol.getValueSegment().getFreightSegment() == null)) {
                    // bl.setPCindicator(bol.getValueSegment().getFreightSegment().getPCIndicator());
                    bl.setFreightCurrency(bol.getValueSegment().getFreightSegment().getFreightCurrency());
                    bl.setFreightValue(bol.getValueSegment().getFreightSegment().getFreightValue());
                }
                bl.setUniqueCarrierReference(bol.getBolId().getUniqueCarrierReference());
                if (bol.getGoodsSegment().getVolumeInCubicMeters() == null) {
                    bol.getGoodsSegment().setVolumeInCubicMeters(0.0);
                }
                bl.setVolumeInCubicMeters(bol.getGoodsSegment().getVolumeInCubicMeters());
                bl.setIdGeneral(general);
                ;
                bl.setIdBol(BOLJC.findLastId());
                BOLJC.create(bl);
                ////
                bl.setContainerCollection(getContainer(bol, bl));
                LOG.info(" Bol " + bl.getLineNumber() + "/" + awmds.getBolSegment().size() + " du Manifest " + bl.getIdGeneral().getId() + " a " + bl.getContainerCollection().size() + " Conteneurs");
                bols.add(bl);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        return bols;
    }

    /**
     * @Description @param bolsegment est un B/L donné dans le manifest
     * @param bol une instance contenant les donnees de bolsegment afin d etre
     * enregistrer dans la base de donnee
     * @return une liste de conteneurs que contient un B/L
     */
    protected static List<Container> getContainer(Awmds.BolSegment bolsegment, BillOfLanding bol) {
        ArrayList ctnrs = new ArrayList();
        Container newctn = new Container();
        /**
         * recuperation des donnees concernant les conteneurs contenus dans le
         * B/L bol depuis une instance de la classe JAXB Awmds.bolsegment qui
         * contient les donnees du xml manifest
         */
        bolsegment.getCtnSegment().stream().forEach((ctn) -> {
            try {

                newctn.setCtnReference(ctn.getCtnReference());
                newctn.setEmptyFull(String.valueOf(ctn.getEmptyFull()));
                newctn.setEmptyWeight(String.valueOf(ctn.getEmptyWeight()));
                if (ctn.getGoodsWeight() == null) {
                    ctn.setGoodsWeight(0.0);
                }
                newctn.setGoodsWeight(String.valueOf(ctn.getGoodsWeight()));
                newctn.setIdBol(bol);
                newctn.setMarks1(ctn.getMarks1());
                newctn.setMarks2(ctn.getMarks2());
                newctn.setMarks3(ctn.getMarks3());
                newctn.setNumberOfPackages(String.valueOf(ctn.getNumberOfPackages()));
                newctn.setSealingParty(ctn.getSealingParty());
                newctn.setTypeOfContainer(ctn.getTypeOfContainer());
                newctn.setIdCtn(CJC.findLastId());
                CJC.create(newctn);
                ctnrs.add(newctn); // 
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        return ctnrs;
    }

    private static boolean has(String bolNature) {
        return bolNature != null && !bolNature.isEmpty();
    }

}
