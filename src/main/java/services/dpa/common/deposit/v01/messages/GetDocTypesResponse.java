
package services.dpa.common.deposit.v01.messages;

import services.dpa.common.deposit.v01.types.DocTypeListType;

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
 *         &lt;element name="docTypes" type="{http://common.dpa.services/deposit/v01/types}DocTypeListType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "docTypes"
})
@XmlRootElement(name = "getDocTypesResponse")
public class GetDocTypesResponse {

    @XmlElement(required = true)
    protected DocTypeListType docTypes;

    /**
     * Gets the value of the docTypes property.
     *
     * @return possible object is
     * {@link DocTypeListType }
     */
    public DocTypeListType getDocTypes() {
        return docTypes;
    }

    /**
     * Sets the value of the docTypes property.
     *
     * @param value allowed object is
     *              {@link DocTypeListType }
     */
    public void setDocTypes(DocTypeListType value) {
        this.docTypes = value;
    }

}
