
package services.dpa.common.addressbook.base.v01.types;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the services.dpa.common.addressbook.base.v01.types package.
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: services.dpa.common.addressbook.base.v01.types
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BillingOrganisation }
     */
    public BillingOrganisation createBillingOrganisation() {
        return new BillingOrganisation();
    }

    /**
     * Create an instance of {@link Address }
     */
    public Address createAddress() {
        return new Address();
    }

    /**
     * Create an instance of {@link LawyerOffice }
     */
    public LawyerOffice createLawyerOffice() {
        return new LawyerOffice();
    }

    /**
     * Create an instance of {@link CommunicationMedium }
     */
    public CommunicationMedium createCommunicationMedium() {
        return new CommunicationMedium();
    }

    /**
     * Create an instance of {@link CommunicationMediumList }
     */
    public CommunicationMediumList createCommunicationMediumList() {
        return new CommunicationMediumList();
    }

    /**
     * Create an instance of {@link LawyerInfo }
     */
    public LawyerInfo createLawyerInfo() {
        return new LawyerInfo();
    }

}
