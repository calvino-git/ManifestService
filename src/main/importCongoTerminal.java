/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import static util.Const.LOG;
import static util.Const.QUERY_CT;
import util.DbHandler;
import model.CongoTerminal;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Admin
 */
public class importCongoTerminal {

    public static int lastLine = 0;

    public static void main(String[] args) throws SQLException, InterruptedException {
        File excel = new File("excel");
        XSSFWorkbook workbk;

        Connection con = DbHandler.getDbConnection();
        PreparedStatement stmt = con.prepareStatement(QUERY_CT);

//        CongoTerminal ct = new CongoTerminal();
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ASYCUDAPU");
//        EntityManager em = null;
//        em = emf.createEntityManager();
//        CriteriaQuery<Object> cq = em.getCriteriaBuilder().createQuery();
//        cq.select(cq.from(CongoTerminal.class));
//        Query q = em.createQuery(cq);
//        List<CongoTerminal> cts = q.getResultList();
//        
//        LOG.info("" + cts.isEmpty());
//        if (!cts.isEmpty()) {
//            cts.forEach((CongoTerminal ctn) -> {
//                if (lastLine < ctn.getId()) {
//                    lastLine = ctn.getId();
//                }
//            });
//        }
        for (File listFile : excel.listFiles()) {
            try {
                if (listFile.getName().startsWith("~")) {
                    continue;
                }
                LOG.info(listFile.getName());
                workbk = new XSSFWorkbook(listFile);
                XSSFSheet sheet = workbk.getSheetAt(0);
                Iterator rows = sheet.rowIterator();
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                int i = 0;
                while (rows.hasNext()) {
                    XSSFRow row = (XSSFRow) rows.next();

                    if (row.getRowNum() == 0) {
                        LOG.info(" LIGNE " + row.getRowNum());
                        continue;
                    }
                    stmt.setInt(1, Integer.valueOf(listFile.getName().substring(0, 6))); //MOIS                
                    stmt.setString(2, row.getCell(0).toString()); //NUM_CTN
                    stmt.setString(3, row.getCell(11) == null ? "" : row.getCell(11).toString()); //DAT
                    stmt.setString(4, row.getCell(1) == null ? "" : row.getCell(1).toString()); //MVNT
                    stmt.setString(5, row.getCell(10) == null ? "" : row.getCell(10).toString()); //TRAFIC
                    stmt.setString(6, row.getCell(9) == null ? "" : row.getCell(9).toString()); //P V
                    stmt.setString(7, row.getCell(2) == null ? "" : row.getCell(2).toString());//ISO
                    stmt.setString(8, row.getCell(3) == null ? "" : row.getCell(3).toString());//TARE
                    stmt.setString(9, row.getCell(6) == null ? "" : row.getCell(6).toString());//EXPLOITANT
                    stmt.setString(10, row.getCell(14) == null ? "" : row.getCell(14).toString());//NAVIRE
                    stmt.setString(11, row.getCell(15) == null ? "" : row.getCell(15).toString()); //VOYAGE
                    stmt.setString(12, row.getCell(23) == null ? "" : row.getCell(23).toString());//POL
                    stmt.setString(13, row.getCell(24) == null ? "" : row.getCell(24).toString());//POD
                    stmt.setString(14, row.getCell(8) == null ? "" : row.getCell(8).toString());//OWNER
                    stmt.setString(15, row.getCell(5) == null ? "" : row.getCell(5).toString());//POIDS
                    stmt.setString(16, row.getCell(17) == null ? "" : row.getCell(17).toString());//DATE ATA
                    stmt.setString(17, row.getCell(18) == null ? "" : row.getCell(18).toString());//DATE ATD
                    stmt.setString(18, row.getCell(3) == null ? "" : row.getCell(3).toString()); //TYPE
                    stmt.setString(19, row.getCell(4) == null ? "" : row.getCell(4).toString()); //EVP
                    stmt.setString(20, row.getCell(12) == null ? "" : row.getCell(12).toString()); //PARC
                    stmt.setString(21, row.getCell(12) == null ? "" : row.getCell(12).toString());//TRUCK VESSEL
                    stmt.setString(22, row.getCell(16) == null ? "" : row.getCell(16).toString());//TYPE NAVIRE
                    stmt.setString(23, row.getCell(19) == null ? "" : row.getCell(19).toString());//DGX
                    stmt.setString(24, row.getCell(20) == null ? "" : row.getCell(20).toString());//REFF
                    stmt.setString(25, row.getCell(21) == null ? "" : row.getCell(21).toString()); //OOG
                    stmt.setString(26, row.getCell(22) == null ? "" : row.getCell(22).toString()); //OPL
                    stmt.setString(27, row.getCell(25) == null ? "" : row.getCell(25).toString()); //PDESF
                    stmt.setString(28, row.getCell(26) == null ? "" : row.getCell(26).toString());//PRESENCE
                    if (i % 1000 == 0) {
                        System.out.print("=");
                    }
                    stmt.executeUpdate();
                    i++;
                }
                Thread.sleep(1000);
                if(Files.deleteIfExists(listFile.toPath())){
                    LOG.info("FICHIER supprimé");
                };
                LOG.info("TERMINE : " + i + " enregistrement.");

//                MYWORKS.put(listFile.getName(),workbk);
            } catch (IOException | InvalidFormatException ex) {
                ex.printStackTrace();
            }
        }
    }
}
