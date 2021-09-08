
package services.dpa.common.deposit.v01.types;

import services.dpa.common.deposit.price.v01.types.PriceRequestType;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for DepositRequestType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DepositRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sender" type="{http://common.dpa.services/deposit/v01/types}SenderType"/>
 *         &lt;element name="case" type="{http://common.dpa.services/deposit/v01/types}CaseType"/>
 *         &lt;element name="recipients" type="{http://common.dpa.services/deposit/v01/types}RecipientsType"/>
 *         &lt;element name="contents" type="{http://common.dpa.services/deposit/v01/types}ContentsType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DepositRequestType", propOrder = {
        "sender",
        "_case",
        "recipients",
        "contents"
})
@XmlSeeAlso({
        PriceRequestType.class
})
public class DepositRequestType {

    @XmlElement(required = true)
    protected SenderType sender;
    @XmlElement(name = "case", required = true)
    protected CaseType _case;
    @XmlElement(required = true)
    protected RecipientsType recipients;
    @XmlElement(required = true)
    protected ContentsType contents;

    /**
     * Gets the value of the sender property.
     *
     * @return possible object is
     * {@link SenderType }
     */
    public SenderType getSender() {
        return sender;
    }

    /**
     * Sets the value of the sender property.
     *
     * @param value allowed object is
     *              {@link SenderType }
     */
    public void setSender(SenderType value) {
        this.sender = value;
    }

    /**
     * Gets the value of the case property.
     *
     * @return possible object is
     * {@link CaseType }
     */
    public CaseType getCase() {
        return _case;
    }

    /**
     * Sets the value of the case property.
     *
     * @param value allowed object is
     *              {@link CaseType }
     */
    public void setCase(CaseType value) {
        this._case = value;
    }

    /**
     * Gets the value of the recipients property.
     *
     * @return possible object is
     * {@link RecipientsType }
     */
    public RecipientsType getRecipients() {
        return recipients;
    }

    /**
     * Sets the value of the recipients property.
     *
     * @param value allowed object is
     *              {@link RecipientsType }
     */
    public void setRecipients(RecipientsType value) {
        this.recipients = value;
    }

    /**
     * Gets the value of the contents property.
     *
     * @return possible object is
     * {@link ContentsType }
     */
    public ContentsType getContents() {
        return contents;
    }

    /**
     * Sets the value of the contents property.
     *
     * @param value allowed object is
     *              {@link ContentsType }
     */
    public void setContents(ContentsType value) {
        this.contents = value;
    }

}
