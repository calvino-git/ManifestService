/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manifestservice;

import static base.Const.LOG;
import jpa.beans.CongoTerminal;
import jpa.beans.Container;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Admin
 */
public class importCongoTerminal {

    public static int lastLine = 0;

    public static void main(String[] args) {
        File excel = new File("excel");
        XSSFWorkbook workbk;
        CongoTerminal ct = new CongoTerminal();
        Iterator rows;
        XSSFRow row;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ASYCUDAPU");

        EntityManager em = null;
        em = emf.createEntityManager();
        CriteriaQuery<Object> cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(CongoTerminal.class));
        Query q = em.createQuery(cq);
        List<CongoTerminal> cts = q.getResultList();
        LOG.info(""+cts.isEmpty());
        if (!cts.isEmpty()) {
            cts.forEach((CongoTerminal ctn) -> {
                if (lastLine < ctn.getId()) {
                    lastLine = ctn.getId();
                }
            });
        }

        for (File listFile : excel.listFiles()) {
            try {
                if(listFile.getName().startsWith("~")) continue;
                
                LOG.info(listFile.getName());
                workbk = new XSSFWorkbook(listFile);
                rows = workbk.getSheetAt(0).rowIterator();
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

                while (rows.hasNext()) {

                    row = (XSSFRow) rows.next();
                    if (row.getRowNum() == 0) {
                        LOG.info(" LIGNE " + row.getRowNum());
                        continue;
                    }
                    ct.setMois(BigInteger.valueOf(Integer.valueOf(listFile.getName().substring(0, 6))));
                    ct.setNumCtn(row.getCell(0).getStringCellValue());
                    ct.setDate(dateFormat.format(row.getCell(1).getDateCellValue()));
                    ct.setMouvement(row.getCell(2).getStringCellValue());
                    ct.setTrafic(row.getCell(3).getStringCellValue());
                    ct.setVidePlein(row.getCell(4).getStringCellValue());
                    ct.setIso(row.getCell(5).getRawValue());
                    ct.setTare(row.getCell(6).getStringCellValue());
                    ct.setExpCours(row.getCell(7).getStringCellValue());
                    ct.setEscale(row.getCell(8).getStringCellValue());
                    ct.setVoyage(row.getCell(9)==null?"":row.getCell(9).getRawValue());

                        ct.setPol(row.getCell(10) == null?"":row.getCell(10).getStringCellValue());
                        ct.setPod(row.getCell(11) == null?"":row.getCell(11).getStringCellValue());
                    ct.setArmateur(row.getCell(12).getStringCellValue());
                    ct.setPoidsBrut(row.getCell(13).getRawValue());
                    ct.setDateArr(dateFormat.format(row.getCell(14).getDateCellValue()));
                    ct.setDateDep(dateFormat.format(row.getCell(15).getDateCellValue()));
                    
                    try {
                        em = emf.createEntityManager();
                        em.getTransaction().begin();
                        ct.setId(++lastLine);
                        em.persist(ct);
                        em.getTransaction().commit();
                        LOG.info(ct.toString());
                    } catch (Exception ex) {
                        if (em.find(CongoTerminal.class, ct.getId()) != null) {
                            LOG.error("Congo Terminal " + ct.getId() + " already exists.");
                        }
                    } finally {
                        if (em != null) {
                            em.close();
                        }
                    }

                }
                listFile.delete();
                LOG.info("TERMINE");

//                MYWORKS.put(listFile.getName(),workbk);
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(ManifestService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidFormatException ex) {
                java.util.logging.Logger.getLogger(ManifestService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
