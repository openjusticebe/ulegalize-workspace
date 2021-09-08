
package services.dpa.common.deposit.file.v01.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FileDeleteRequestType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="FileDeleteRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="docID" type="{http://common.dpa.services/global/v01/types}UUID"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FileDeleteRequestType", propOrder = {
        "docID"
})
public class FileDeleteRequestType {

    @XmlElement(required = true)
    protected String docID;

    /**
     * Gets the value of the docID property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDocID() {
        return docID;
    }

    /**
     * Sets the value of the docID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDocID(String value) {
        this.docID = value;
    }

}
