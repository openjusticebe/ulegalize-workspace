
package services.dpa.common.message.box.v01.types;

import services.dpa.common.message.base.v01.codes.TransactionState;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for DeliveryChannel complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DeliveryChannel">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="transactionID" type="{http://common.dpa.services/global/v01/types}UUID"/>
 *         &lt;element name="state" type="{http://common.dpa.services/message/base/v01/codes}TransactionState"/>
 *         &lt;element name="comment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="price" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="docs" type="{http://common.dpa.services/message/box/v01/types}DocList"/>
 *       &lt;/sequence>
 *       &lt;attribute name="channelID" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DeliveryChannel", propOrder = {
        "transactionID",
        "state",
        "comment",
        "price",
        "docs"
})
@XmlSeeAlso({
        Email.class,
        Edeposit.class,
        Letter.class,
        DPAbox.class,
        Fax.class
})
public abstract class DeliveryChannel {

    @XmlElement(required = true)
    protected String transactionID;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected TransactionState state;
    protected String comment;
    protected double price;
    @XmlElement(required = true)
    protected DocList docs;
    @XmlAttribute(name = "channelID", required = true)
    protected int channelID;

    /**
     * Gets the value of the transactionID property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTransactionID() {
        return transactionID;
    }

    /**
     * Sets the value of the transactionID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTransactionID(String value) {
        this.transactionID = value;
    }

    /**
     * Gets the value of the state property.
     *
     * @return possible object is
     * {@link TransactionState }
     */
    public TransactionState getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     *
     * @param value allowed object is
     *              {@link TransactionState }
     */
    public void setState(TransactionState value) {
        this.state = value;
    }

    /**
     * Gets the value of the comment property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the value of the comment property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setComment(String value) {
        this.comment = value;
    }

    /**
     * Gets the value of the price property.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the value of the price property.
     */
    public void setPrice(double value) {
        this.price = value;
    }

    /**
     * Gets the value of the docs property.
     *
     * @return possible object is
     * {@link DocList }
     */
    public DocList getDocs() {
        return docs;
    }

    /**
     * Sets the value of the docs property.
     *
     * @param value allowed object is
     *              {@link DocList }
     */
    public void setDocs(DocList value) {
        this.docs = value;
    }

    /**
     * Gets the value of the channelID property.
     */
    public int getChannelID() {
        return channelID;
    }

    /**
     * Sets the value of the channelID property.
     */
    public void setChannelID(int value) {
        this.channelID = value;
    }

}
