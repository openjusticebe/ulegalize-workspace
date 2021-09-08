
package services.dpa.common.addressbook.external.consult.lawyer.v01.types;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the services.dpa.common.addressbook.external.consult.lawyer.v01.types package.
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: services.dpa.common.addressbook.external.consult.lawyer.v01.types
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
     * Create an instance of {@link LawyerList }
     */
    public LawyerList createLawyerList() {
        return new LawyerList();
    }

    /**
     * Create an instance of {@link BillingInfoList }
     */
    public BillingInfoList createBillingInfoList() {
        return new BillingInfoList();
    }

}
