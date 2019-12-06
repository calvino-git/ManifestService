/*
 * To searchPortTRB this license header, choose License Headers in Project Properties.
 * To searchPortTRB this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import asycuda.awmds.Awmds;
import static util.Const.LOG;
import model.CongoTerminal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import static util.Const.EMF;

/**
 *
 * @author calvin0
 */
public class PortDestination {
    static int test = 0;
    private static final EntityManager EM = EMF.createEntityManager();
    private static List<CongoTerminal> listCtnr;
    private static LocalDate date;
    private static String dateFin;
    private static String dateDeb;
    private static Integer mois;

    public static void searchPortTRB(Awmds manifest) {
        // TODO code application logic here
        TypedQuery<CongoTerminal> query = EM.createQuery("SELECT c FROM CongoTerminal c WHERE c.numCtn = :numCtn and c.mois=:mois", CongoTerminal.class);

        LOG.info("######### VOYAGE N° " + manifest.getGeneralSegment().getGeneralSegmentId().getVoyageNumber() + "###########");

        referenceTable ref = new referenceTable();
        int count = 0;
        for (Awmds.BolSegment bol : manifest.getBolSegment()
                .stream().filter(bl -> (bl.getBolId().getBolNature().equalsIgnoreCase("28")&& bl.getLoadUnloadPlace().getPlaceOfUnloadingCode().equalsIgnoreCase("CGPNR") ) 
                        || (bl.getBolId().getBolNature().equalsIgnoreCase("29")&& bl.getLoadUnloadPlace().getPlaceOfLoadingCode().equalsIgnoreCase("CGPNR") ))
                .collect(Collectors.toList())) {
            test = 0;
            LOG.info("##############################################################");
            LOG.info("BOL N°" + ++count + " => REF : " + bol.getBolId().getBolReference());
            LOG.info("Nature : " + ref.nature.get(bol.getBolId().getBolNature()).toUpperCase());
            
            date = LocalDate.parse(bol.getBolId().getBolNature().equals("28")
                    ? manifest.getGeneralSegment().getGeneralSegmentId().getDateOfArrival().substring(0, 10)
                    : manifest.getGeneralSegment().getGeneralSegmentId().getDateOfDeparture().substring(0, 10));

            dateDeb = String.valueOf(date.minusDays(5)).replaceAll("-", "");
            dateFin = String.valueOf(date.plusDays(5)).replaceAll("-", "");
            mois = Integer.valueOf(date.format(DateTimeFormatter.BASIC_ISO_DATE).substring(0, 6));
            test = 0;
            LOG.info("BL AVANT : POL / POD = " + bol.getBolId().getBolReference()
                                    + " : " + bol.getLoadUnloadPlace().getPlaceOfLoadingCode()
                                    + "/" + bol.getLoadUnloadPlace().getPlaceOfUnloadingCode());
            for (Awmds.BolSegment.CtnSegment ctn : bol.getCtnSegment()) {
                if (query != null) {
                    listCtnr = query.setParameter("numCtn", ctn.getCtnReference()).setParameter("mois", mois).getResultList();
                    LOG.info("Resultat de " + ctn.getCtnReference() + " : " + listCtnr.size());
                    if (!listCtnr.isEmpty()) {
                        boolean isFound = false;
                        for (CongoTerminal ctnr : listCtnr) {
                            if (bol.getLoadUnloadPlace().getPlaceOfLoadingCode().equals("CGPNR") && ctnr.getTrafic().equals("T") && ctnr.getPol() != null) {
                                if ((!ctnr.getPol().equals("CGPNR"))) {
                                    if ((!ctnr.getPol().equals(bol.getLoadUnloadPlace().getPlaceOfUnloadingCode()))) {
                                        if (ctnr.getPol() != null && (!ctnr.getPol().isEmpty())) {
                                            bol.getLoadUnloadPlace().setPlaceOfLoadingCode(ctnr.getPol());
                                            isFound = true;
                                        }
                                    }
                                }
                            } else if (bol.getLoadUnloadPlace().getPlaceOfUnloadingCode().equals("CGPNR") && ctnr.getTrafic().equals("T") && ctnr.getPol() != null) {

                                if ((!ctnr.getPod().equals("CGPNR"))) {
                                    if ((!ctnr.getPod().equals(bol.getLoadUnloadPlace().getPlaceOfLoadingCode()))) {
                                        if (ctnr.getPod() != null && (!ctnr.getPod().isEmpty())) {
                                            bol.getLoadUnloadPlace().setPlaceOfUnloadingCode(ctnr.getPod());
                                            isFound = true;
                                        }
                                    }

                                }
                            } else {
                                LOG.info("BL  N° " + ctnr.getNumCtn() + " CORRECT");
                                isFound = true;
                            }
                            if (isFound == true) {
                                test = 1;
                                break;
                            }
                        }
                    }
                }
                if (test == 1) {
                    LOG.info("BL APRES : POL / POD = " + bol.getBolId().getBolReference() + ": "
                            + bol.getLoadUnloadPlace().getPlaceOfLoadingCode() + " => "
                            + bol.getLoadUnloadPlace().getPlaceOfUnloadingCode());
                    break;
                }
            }
        }
    }

}
