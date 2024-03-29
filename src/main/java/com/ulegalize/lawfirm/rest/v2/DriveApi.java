package com.ulegalize.lawfirm.rest.v2;

import com.ulegalize.dto.ObjectResponseDTO;
import com.ulegalize.dto.ShareFileDTO;
import com.ulegalize.lawfirm.model.LawfirmToken;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface DriveApi {
    void createContainer(LawfirmToken lawfirmToken, String containerName, List<String> paths) throws ResponseStatusException;

    void createFolders(LawfirmToken lawfirmToken, String vcKey, List<String> paths, String parentId);

    void uploadFile(LawfirmToken lawfirmToken, byte[] file, String originalFilename, String path);

    List<ObjectResponseDTO> getDossierList(LawfirmToken lawfirmToken, String path);

    List<ObjectResponseDTO> getObjects(LawfirmToken lawfirmToken, String path);

    List<ObjectResponseDTO> getDispatchingFilesList(LawfirmToken lawfirmToken, String path);

    String moveFile(LawfirmToken lawfirmToken, String filename, String fromPath, String pathTo);

    /**
     * @param token
     * @param vcKey
     * @param fromPath start with "" and finish with "/"
     * @param pathTo   start with "" and finish with "/"
     * @return
     */
    String moveFolders(String token, String vcKey, String fromPath, String pathTo);

    String deletingFile(LawfirmToken lawfirmToken, String path);

    String deletingFolder(LawfirmToken lawfirmToken, String path);

    ByteArrayResource downloadFile(LawfirmToken lawfirmToken, String path);

    String getShareLink(LawfirmToken lawfirmToken, String path);

    Boolean shareObject(LawfirmToken lawfirmToken, ShareFileDTO shareFileDTO);

    Integer getSizeContainer(LawfirmToken lawfirmToken, String vcKey);
}
