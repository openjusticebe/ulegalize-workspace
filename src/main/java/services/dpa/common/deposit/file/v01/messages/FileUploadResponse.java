
package services.dpa.common.deposit.file.v01.messages;

import services.dpa.common.deposit.file.v01.types.FileUploadResponseType;

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
 *         &lt;element name="response" type="{http://common.dpa.services/deposit/file/v01/types}FileUploadResponseType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "response"
})
@XmlRootElement(name = "FileUploadResponse")
public class FileUploadResponse {

    @XmlElement(required = true)
    protected FileUploadResponseType response;

    /**
     * Gets the value of the response property.
     *
     * @return possible object is
     * {@link FileUploadResponseType }
     */
    public FileUploadResponseType getResponse() {
        return response;
    }

    /**
     * Sets the value of the response property.
     *
     * @param value allowed object is
     *              {@link FileUploadResponseType }
     */
    public void setResponse(FileUploadResponseType value) {
        this.response = value;
    }

}
