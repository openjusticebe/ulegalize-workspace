
package services.dpa.common.deposit.consult.v01.types;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the services.dpa.common.deposit.consult.v01.types package.
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: services.dpa.common.deposit.consult.v01.types
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Lawyer }
     */
    public Lawyer createLawyer() {
        return new Lawyer();
    }

    /**
     * Create an instance of {@link Court }
     */
    public Court createCourt() {
        return new Court();
    }

    /**
     * Create an instance of {@link ChannelInfoListType }
     */
    public ChannelInfoListType createChannelInfoListType() {
        return new ChannelInfoListType();
    }

    /**
     * Create an instance of {@link DocTypeInfoListType }
     */
    public DocTypeInfoListType createDocTypeInfoListType() {
        return new DocTypeInfoListType();
    }

    /**
     * Create an instance of {@link DocTypeInfoType }
     */
    public DocTypeInfoType createDocTypeInfoType() {
        return new DocTypeInfoType();
    }

    /**
     * Create an instance of {@link ThirdParty }
     */
    public ThirdParty createThirdParty() {
        return new ThirdParty();
    }

    /**
     * Create an instance of {@link ChannelInfoType }
     */
    public ChannelInfoType createChannelInfoType() {
        return new ChannelInfoType();
    }

}
