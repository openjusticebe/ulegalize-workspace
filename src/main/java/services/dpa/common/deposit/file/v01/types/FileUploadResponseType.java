
package services.dpa.common.deposit.file.v01.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigInteger;


/**
 * <p>Java class for FileUploadResponseType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="FileUploadResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="docID" type="{http://common.dpa.services/global/v01/types}UUID"/>
 *         &lt;element name="fileName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fileHash" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mimeType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numberOfPages" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FileUploadResponseType", propOrder = {
        "docID",
        "fileName",
        "fileHash",
        "mimeType",
        "numberOfPages"
})
public class FileUploadResponseType {

    @XmlElement(required = true)
    protected String docID;
    @XmlElement(required = true)
    protected String fileName;
    @XmlElement(required = true)
    protected String fileHash;
    @XmlElement(required = true)
    protected String mimeType;
    @XmlElement(required = true)
    protected BigInteger numberOfPages;

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

    /**
     * Gets the value of the fileName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the value of the fileName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setFileName(String value) {
        this.fileName = value;
    }

    /**
     * Gets the value of the fileHash property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getFileHash() {
        return fileHash;
    }

    /**
     * Sets the value of the fileHash property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setFileHash(String value) {
        this.fileHash = value;
    }

    /**
     * Gets the value of the mimeType property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * Sets the value of the mimeType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMimeType(String value) {
        this.mimeType = value;
    }

    /**
     * Gets the value of the numberOfPages property.
     *
     * @return possible object is
     * {@link BigInteger }
     */
    public BigInteger getNumberOfPages() {
        return numberOfPages;
    }

    /**
     * Sets the value of the numberOfPages property.
     *
     * @param value allowed object is
     *              {@link BigInteger }
     */
    public void setNumberOfPages(BigInteger value) {
        this.numberOfPages = value;
    }

}
