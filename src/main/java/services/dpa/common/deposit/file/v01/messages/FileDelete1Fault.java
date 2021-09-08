
package services.dpa.common.deposit.file.v01.messages;

import services.dpa.common.global.v01.messages.GeneralFault;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{http://common.dpa.services/global/v01/messages}GeneralFault">
 *       &lt;sequence>
 *         &lt;element name="docID" type="{http://common.dpa.services/global/v01/types}UUID"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "docID"
})
@XmlRootElement(name = "FileDelete1Fault")
public class FileDelete1Fault
        extends GeneralFault {

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
