
package services.dpa.common.deposit.v01.types;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for DocTypeListType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DocTypeListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="doctype" type="{http://common.dpa.services/deposit/v01/types}DocTypeType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocTypeListType", propOrder = {
        "doctype"
})
public class DocTypeListType {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected List<DocTypeType> doctype;

    /**
     * Gets the value of the doctype property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the doctype property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDoctype().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DocTypeType }
     */
    public List<DocTypeType> getDoctype() {
        if (doctype == null) {
            doctype = new ArrayList<DocTypeType>();
        }
        return this.doctype;
    }

}
