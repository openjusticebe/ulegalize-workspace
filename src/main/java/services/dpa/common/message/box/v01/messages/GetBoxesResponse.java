
package services.dpa.common.message.box.v01.messages;

import services.dpa.common.message.box.v01.types.BoxList;

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
 *         &lt;element name="boxes" type="{http://common.dpa.services/message/box/v01/types}BoxList"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "boxes"
})
@XmlRootElement(name = "getBoxesResponse")
public class GetBoxesResponse {

    @XmlElement(required = true)
    protected BoxList boxes;

    /**
     * Gets the value of the boxes property.
     *
     * @return possible object is
     * {@link BoxList }
     */
    public BoxList getBoxes() {
        return boxes;
    }

    /**
     * Sets the value of the boxes property.
     *
     * @param value allowed object is
     *              {@link BoxList }
     */
    public void setBoxes(BoxList value) {
        this.boxes = value;
    }

}
