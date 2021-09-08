
package services.dpa.common.deposit.v01.messages;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the services.dpa.common.deposit.v01.messages package.
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: services.dpa.common.deposit.v01.messages
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Deposit1Fault }
     */
    public Deposit1Fault createDeposit1Fault() {
        return new Deposit1Fault();
    }

    /**
     * Create an instance of {@link GetDocTypesRequest }
     */
    public GetDocTypesRequest createGetDocTypesRequest() {
        return new GetDocTypesRequest();
    }

    /**
     * Create an instance of {@link DepositRequest }
     */
    public DepositRequest createDepositRequest() {
        return new DepositRequest();
    }

    /**
     * Create an instance of {@link DepositResponse }
     */
    public DepositResponse createDepositResponse() {
        return new DepositResponse();
    }

    /**
     * Create an instance of {@link GetDocTypesResponse }
     */
    public GetDocTypesResponse createGetDocTypesResponse() {
        return new GetDocTypesResponse();
    }

}
