
package services.dpa.common.addressbook.external.consult.lawyer.v01.messages;

import services.dpa.common.addressbook.external.consult.lawyer.v01.types.LawyerList;

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
 *         &lt;element name="lawyers" type="{http://common.dpa.services/addressbook/external/consult/lawyer/v01/types}LawyerList"/>
 *         &lt;element name="limit" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="offset" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="size" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "lawyers",
        "limit",
        "offset",
        "size"
})
@XmlRootElement(name = "getLawyersResponse")
public class GetLawyersResponse {

    @XmlElement(required = true)
    protected LawyerList lawyers;
    protected Integer limit;
    protected Integer offset;
    protected Integer size;

    /**
     * Gets the value of the lawyers property.
     *
     * @return possible object is
     * {@link LawyerList }
     */
    public LawyerList getLawyers() {
        return lawyers;
    }

    /**
     * Sets the value of the lawyers property.
     *
     * @param value allowed object is
     *              {@link LawyerList }
     */
    public void setLawyers(LawyerList value) {
        this.lawyers = value;
    }

    /**
     * Gets the value of the limit property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * Sets the value of the limit property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setLimit(Integer value) {
        this.limit = value;
    }

    /**
     * Gets the value of the offset property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getOffset() {
        return offset;
    }

    /**
     * Sets the value of the offset property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setOffset(Integer value) {
        this.offset = value;
    }

    /**
     * Gets the value of the size property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getSize() {
        return size;
    }

    /**
     * Sets the value of the size property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setSize(Integer value) {
        this.size = value;
    }

}
