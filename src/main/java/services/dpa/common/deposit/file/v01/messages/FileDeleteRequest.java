
package services.dpa.common.deposit.file.v01.messages;

import services.dpa.common.deposit.file.v01.types.FileDeleteRequestType;

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
 *         &lt;element name="fileUpload" type="{http://common.dpa.services/deposit/file/v01/types}FileDeleteRequestType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "fileUpload"
})
@XmlRootElement(name = "FileDeleteRequest")
public class FileDeleteRequest {

    @XmlElement(required = true)
    protected FileDeleteRequestType fileUpload;

    /**
     * Gets the value of the fileUpload property.
     *
     * @return possible object is
     * {@link FileDeleteRequestType }
     */
    public FileDeleteRequestType getFileUpload() {
        return fileUpload;
    }

    /**
     * Sets the value of the fileUpload property.
     *
     * @param value allowed object is
     *              {@link FileDeleteRequestType }
     */
    public void setFileUpload(FileDeleteRequestType value) {
        this.fileUpload = value;
    }

}
