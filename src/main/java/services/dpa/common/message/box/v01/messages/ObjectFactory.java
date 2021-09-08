
package services.dpa.common.message.box.v01.messages;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the services.dpa.common.message.box.v01.messages package.
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: services.dpa.common.message.box.v01.messages
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetCasesRequest }
     */
    public GetCasesRequest createGetCasesRequest() {
        return new GetCasesRequest();
    }

    /**
     * Create an instance of {@link GetMessageSummariesRequest }
     */
    public GetMessageSummariesRequest createGetMessageSummariesRequest() {
        return new GetMessageSummariesRequest();
    }

    /**
     * Create an instance of {@link GetDocumentRequest }
     */
    public GetDocumentRequest createGetDocumentRequest() {
        return new GetDocumentRequest();
    }

    /**
     * Create an instance of {@link GetBoxesRequest }
     */
    public GetBoxesRequest createGetBoxesRequest() {
        return new GetBoxesRequest();
    }

    /**
     * Create an instance of {@link GetMessageByIdRequest }
     */
    public GetMessageByIdRequest createGetMessageByIdRequest() {
        return new GetMessageByIdRequest();
    }

    /**
     * Create an instance of {@link GetMessageByIdResponse }
     */
    public GetMessageByIdResponse createGetMessageByIdResponse() {
        return new GetMessageByIdResponse();
    }

    /**
     * Create an instance of {@link GetCaseDocumentsRequest }
     */
    public GetCaseDocumentsRequest createGetCaseDocumentsRequest() {
        return new GetCaseDocumentsRequest();
    }

    /**
     * Create an instance of {@link GetMessageSummariesResponse }
     */
    public GetMessageSummariesResponse createGetMessageSummariesResponse() {
        return new GetMessageSummariesResponse();
    }

    /**
     * Create an instance of {@link GetCasesRequest.UpdatedBetween }
     */
    public GetCasesRequest.UpdatedBetween createGetCasesRequestUpdatedBetween() {
        return new GetCasesRequest.UpdatedBetween();
    }

    /**
     * Create an instance of {@link GetCasesRequest.OpenedBetween }
     */
    public GetCasesRequest.OpenedBetween createGetCasesRequestOpenedBetween() {
        return new GetCasesRequest.OpenedBetween();
    }

    /**
     * Create an instance of {@link GetMessageSummariesRequest.UpdatedBetween }
     */
    public GetMessageSummariesRequest.UpdatedBetween createGetMessageSummariesRequestUpdatedBetween() {
        return new GetMessageSummariesRequest.UpdatedBetween();
    }

    /**
     * Create an instance of {@link GetMessageSummariesRequest.CaseReference }
     */
    public GetMessageSummariesRequest.CaseReference createGetMessageSummariesRequestCaseReference() {
        return new GetMessageSummariesRequest.CaseReference();
    }

    /**
     * Create an instance of {@link GetCasesResponse }
     */
    public GetCasesResponse createGetCasesResponse() {
        return new GetCasesResponse();
    }

    /**
     * Create an instance of {@link GetCaseDocumentsResponse }
     */
    public GetCaseDocumentsResponse createGetCaseDocumentsResponse() {
        return new GetCaseDocumentsResponse();
    }

    /**
     * Create an instance of {@link GetBoxesResponse }
     */
    public GetBoxesResponse createGetBoxesResponse() {
        return new GetBoxesResponse();
    }

    /**
     * Create an instance of {@link GetDocumentResponse }
     */
    public GetDocumentResponse createGetDocumentResponse() {
        return new GetDocumentResponse();
    }

}
