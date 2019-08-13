/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base;

import asycuda.xml.Awmds;
import static base.Const.ESCJC;
import static base.Const.GIJC;
import static base.Const.LOG;
import static base.Function.*;
import static base.PersistObject.getBol;
import static base.PersistObject.getGeneral;
import dao.Escale;
import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import jpa.beans.BillOfLanding;
import jpa.beans.GeneralInfo;
import jpa.exceptions.IllegalOrphanException;
import jpa.exceptions.NonexistentEntityException;
import jpa.exceptions.PreexistingEntityException;

/**
 *
 * @author Admin
 */
public class XmlObject {

    private static JAXBContext jc;
    private static Unmarshaller unmarshaller;
    private static Marshaller marshaller;
    private static GeneralInfo segmentGeneral;
    
    public static Awmds xmlToAwmds(File xmlFichier) {
        LOG.info("Chargement du fichier XML en objet AWMDS");
        try {
            jc = JAXBContext.newInstance(Awmds.class);
            unmarshaller = jc.createUnmarshaller();
            return (Awmds) unmarshaller.unmarshal(xmlFichier);
        } catch (JAXBException ue) {
            LOG.error("Fichier XML non valide \n" + ue.getMessage());
        }
        return null;
    }
    public static void AwmdsToXml(Awmds awmds, File file) {
        try {
            jc = JAXBContext.newInstance(Awmds.class);
            marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Marshalling and saving XML to the file.
            marshaller.marshal(awmds, file);

        } catch (JAXBException e) { // catches ANY exception
            LOG.info("JAXBException error code : " + e.getErrorCode() + " " + e.getMessage());
        }
    }
    public static int AwmdsToDB(Awmds cargo, Escale escale) throws NonexistentEntityException, IllegalOrphanException, PreexistingEntityException, Exception {
        segmentGeneral = getGeneral(cargo);
        //
        segmentGeneral.setId(GIJC.findLastId());
        segmentGeneral.setIdEscale(ESCJC.findEscale(BigDecimal.valueOf(escale.getEscleunik())));
        segmentGeneral.setNumeroEscale(escale.getNumero());
        //
        GIJC.create(segmentGeneral);
        //
        List<BillOfLanding> bols = getBol(cargo, segmentGeneral);
        segmentGeneral.setBillOfLandingCollection(bols);
        //
        return gen.getId();
    }
}
