
package services.dpa.common.message.box.v01.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for CaseDocumentList complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="CaseDocumentList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="caseDocument" type="{http://common.dpa.services/message/box/v01/types}CaseDocument" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CaseDocumentList", propOrder = {
        "caseDocument"
})
public class CaseDocumentList {

    protected List<CaseDocument> caseDocument;

    /**
     * Gets the value of the caseDocument property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the caseDocument property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCaseDocument().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CaseDocument }
     */
    public List<CaseDocument> getCaseDocument() {
        if (caseDocument == null) {
            caseDocument = new ArrayList<CaseDocument>();
        }
        return this.caseDocument;
    }

}
