
package services.dpa.common.message.box.v01.messages;

import javax.activation.DataHandler;
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
 *         &lt;element name="fileContent" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "fileContent"
})
@XmlRootElement(name = "getDocumentResponse")
public class GetDocumentResponse {

    @XmlElement(required = true)
    @XmlMimeType("application/octet-stream")
    protected DataHandler fileContent;

    /**
     * Gets the value of the fileContent property.
     *
     * @return possible object is
     * {@link DataHandler }
     */
    public DataHandler getFileContent() {
        return fileContent;
    }

    /**
     * Sets the value of the fileContent property.
     *
     * @param value allowed object is
     *              {@link DataHandler }
     */
    public void setFileContent(DataHandler value) {
        this.fileContent = value;
    }

}
