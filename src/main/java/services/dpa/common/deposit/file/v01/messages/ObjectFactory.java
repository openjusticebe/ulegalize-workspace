
package services.dpa.common.deposit.file.v01.messages;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the services.dpa.common.deposit.file.v01.messages package.
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: services.dpa.common.deposit.file.v01.messages
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FileUploadRequest }
     */
    public FileUploadRequest createFileUploadRequest() {
        return new FileUploadRequest();
    }

    /**
     * Create an instance of {@link FileUpload1Fault }
     */
    public FileUpload1Fault createFileUpload1Fault() {
        return new FileUpload1Fault();
    }

    /**
     * Create an instance of {@link FileDeleteResponse }
     */
    public FileDeleteResponse createFileDeleteResponse() {
        return new FileDeleteResponse();
    }

    /**
     * Create an instance of {@link FileUploadResponse }
     */
    public FileUploadResponse createFileUploadResponse() {
        return new FileUploadResponse();
    }

    /**
     * Create an instance of {@link FileDelete1Fault }
     */
    public FileDelete1Fault createFileDelete1Fault() {
        return new FileDelete1Fault();
    }

    /**
     * Create an instance of {@link FileDeleteRequest }
     */
    public FileDeleteRequest createFileDeleteRequest() {
        return new FileDeleteRequest();
    }

}
