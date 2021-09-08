
package services.dpa.common.global.v01.messages;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the services.dpa.common.global.v01.messages package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: services.dpa.common.global.v01.messages
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Ping }
     */
    public Ping createPing() {
        return new Ping();
    }

    /**
     * Create an instance of {@link Pong }
     */
    public Pong createPong() {
        return new Pong();
    }

    /**
     * Create an instance of {@link BasicFault }
     */
    public BasicFault createBasicFault() {
        return new BasicFault();
    }

    /**
     * Create an instance of {@link GeneralFault }
     */
    public GeneralFault createGeneralFault() {
        return new GeneralFault();
    }

}
