
package services.dpa.common.global.v01.messages;

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
 *         &lt;element name="echo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "echo"
})
@XmlRootElement(name = "Pong")
public class Pong {

    @XmlElement(required = true)
    protected String echo;

    /**
     * Gets the value of the echo property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getEcho() {
        return echo;
    }

    /**
     * Sets the value of the echo property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEcho(String value) {
        this.echo = value;
    }

}
