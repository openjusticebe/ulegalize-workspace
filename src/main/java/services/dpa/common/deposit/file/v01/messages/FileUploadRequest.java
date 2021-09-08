
package services.dpa.common.deposit.file.v01.messages;

import services.dpa.common.deposit.file.v01.types.FileUploadRequestType;

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
 *         &lt;element name="fileUpload" type="{http://common.dpa.services/deposit/file/v01/types}FileUploadRequestType"/>
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
@XmlRootElement(name = "FileUploadRequest")
public class FileUploadRequest {

    @XmlElement(required = true)
    protected FileUploadRequestType fileUpload;

    /**
     * Gets the value of the fileUpload property.
     *
     * @return possible object is
     * {@link FileUploadRequestType }
     */
    public FileUploadRequestType getFileUpload() {
        return fileUpload;
    }

    /**
     * Sets the value of the fileUpload property.
     *
     * @param value allowed object is
     *              {@link FileUploadRequestType }
     */
    public void setFileUpload(FileUploadRequestType value) {
        this.fileUpload = value;
    }

}
