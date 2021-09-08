
package services.dpa.common.deposit.file.v01.messages;

import services.dpa.common.deposit.file.v01.types.FileDeleteResponseType;

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
 *         &lt;element name="response" type="{http://common.dpa.services/deposit/file/v01/types}FileDeleteResponseType"/>
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
@XmlRootElement(name = "FileDeleteResponse")
public class FileDeleteResponse {

    @XmlElement(required = true)
    protected FileDeleteResponseType response;

    /**
     * Gets the value of the response property.
     *
     * @return possible object is
     * {@link FileDeleteResponseType }
     */
    public FileDeleteResponseType getResponse() {
        return response;
    }

    /**
     * Sets the value of the response property.
     *
     * @param value allowed object is
     *              {@link FileDeleteResponseType }
     */
    public void setResponse(FileDeleteResponseType value) {
        this.response = value;
    }

}
