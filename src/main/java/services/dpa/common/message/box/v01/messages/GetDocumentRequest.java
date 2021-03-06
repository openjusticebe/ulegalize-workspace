
package services.dpa.common.message.box.v01.messages;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="documentId" type="{http://common.dpa.services/global/v01/types}UUID"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "documentId"
})
@XmlRootElement(name = "getDocumentRequest")
public class GetDocumentRequest {

    @XmlElement(required = true)
    protected String documentId;

    /**
     * Gets the value of the documentId property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDocumentId() {
        return documentId;
    }

    /**
     * Sets the value of the documentId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDocumentId(String value) {
        this.documentId = value;
    }

}
