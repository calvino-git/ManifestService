/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base;

import asycuda.Awmds;
import db.CongoTerminal;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import static manifestservice.ManifestService.LOG;
/**
 *
 * @author calvin0
 */
public class ExcelXml {

    /**
     * @param args the command line arguments
     */
    static int test = 0;
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ManifestServicePU");
    private static final EntityManager em = emf.createEntityManager();
    private static List<CongoTerminal> listCtnr;
    private static String dateDebut;
    private static String dateFin;
    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
//    private static List<Integer> sheets = new ArrayList<>() ;
    public static void change(Awmds manifest, int mois) throws IOException {
        // TODO code application logic here
        TypedQuery<CongoTerminal> query = em.createQuery("SELECT c FROM CongoTerminal c WHERE c.numCtn = :numCtn and c.date between :dateDebut and :dateFin", CongoTerminal.class);

        LOG.info("######### VOYAGE N° " + manifest.getGeneralSegment().getGeneralSegmentId().getVoyageNumber() + "###########");
//        int index=0;
////        for(int i = mois-1; i<=mois+1; i++){
////            if(i == mois-1 || i == mois) j = i+1;
////            if(i == mois+1) j = i-2;
////            switch(String.valueOf(j).substring(4,6)){
////                case "00":
////                    j = j - 188;
////                case "13":
////                    j = j + 88;
////                default:
////                    j = j;
////            }
////            myWorks.add(index++,new XSSFWorkbook(j+".xlsx"));
////        }
        referenceTable ref = new referenceTable();
        manifest.getBolSegment().forEach(bol -> {
            test = 0;
            //if(test == 1) break;
//                for(XSSFWorkbook myWork : myWorks) {
//                    rows = myWorks.getSheetAt(0).rowIterator();
            if (bol.getBolId().getBolNature().equals("28") || bol.getBolId().getBolNature().equals("29")) {
                LOG.info("#######################");
                LOG.info("BOL N°" + manifest.getBolSegment().indexOf(bol) + " => REF : " + bol.getBolId().getBolReference());
                LOG.info("Nature : " + ref.nature.get(bol.getBolId().getBolNature()).toUpperCase());
                //LOG.info("##############################################################");
                dateDebut = bol.getBolId().getBolNature().equals("28")?manifest.getGeneralSegment().getGeneralSegmentId().getDateOfArrival():manifest.getGeneralSegment().getGeneralSegmentId().getDateOfDeparture();
                for (Awmds.BolSegment.CtnSegment ctn : bol.getCtnSegment()) {
                    listCtnr = query.setParameter("numCtn", ctn.getCtnReference()).setParameter("dateDebut", dateDebut).setParameter("dateFin", dateFin).getResultList();
                    
                    if (!listCtnr.isEmpty()) {
                        LOG.info("Resultat de " + ctn.getCtnReference() + " : " + listCtnr.size());
                        for (CongoTerminal ctnr : listCtnr) {
//                            row = (XSSFRow) rows.next();
//                            if (row.getRowNum() == 0) {
//                                continue;
//                            }
                            //                            LOG.info("Conteneur EXCEL / MANIFEST = " + row.getCell(0).getStringCellValue() + " / " + ctn.getCtnReference());

                            if (ctnr.getNumCtn().equals(ctn.getCtnReference()) /*&& row.getCell(4).getRawValue().equals(manifest.getGeneralSegment().getGeneralSegmentId().getVoyag                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           