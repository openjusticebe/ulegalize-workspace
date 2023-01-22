package com.ulegalize.lawfirm.rest.v2.impl;

import com.ulegalize.dto.ItemStringDto;
import com.ulegalize.dto.ObjectResponseDTO;
import com.ulegalize.dto.RequestDropbox;
import com.ulegalize.dto.ShareFileDTO;
import com.ulegalize.lawfirm.exception.RestTemplateResponseErrorHandler;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.rest.impl.MultipartInputStreamFileResource;
import com.ulegalize.lawfirm.rest.v2.DriveApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component(value = "driveOnedrive")
@Slf4j
@Transactional
public class DriveOnedriveImpl implements DriveApi {
    @Value("${app.lawfirm-drive.url}")
    String DRIVE_ONEDRIVE_URL;
    @Value("${spring.profiles.active}")
    private String activeProfile;

    private final RestTemplate restTemplate;

    public DriveOnedriveImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
    }

    @Override
    public void createContainer(LawfirmToken lawfirmToken, String containerName, List<String> paths) throws ResponseStatusException {
        log.info("Entering No DropBox createContainer with container : {} ", containerName);

    }

    @Override
    public void createFolders(LawfirmToken lawfirmToken, String dropbox_token, List<String> paths, String parentId) {
        log.info("Entering createFolder with container : {} and paths {}", dropbox_token, paths);
        if (!activeProfile.equalsIgnoreCase("integrationtest")
//                && !activeProfile.equalsIgnoreCase("dev")
                && !activeProfile.equalsIgnoreCase("devDocker")) {

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + lawfirmToken.getToken());

//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("paths", paths);
//            jsonObject.put("parentId", parentId);

            HttpEntity<List> request = new HttpEntity<>(paths, headers);
            ResponseEntity<String> result = restTemplate.exchange(DRIVE_ONEDRIVE_URL + "v2/onedrive/folders?parentId=" + parentId, HttpMethod.POST, request, String.class);
        }
        log.info("Leaving createFolder");
    }

    @Override
    public void uploadFile(LawfirmToken lawfirmToken, byte[] file, String originalFilename, String path) {
        log.info("Entering uploadFileInbox with container : {} and paths {}", lawfirmToken.getVcKey(), path);
        MultipartFile multipartFile;
        InputStreamResource resource;
        if (!activeProfile.equalsIgnoreCase("integrationtest")
                && !activeProfile.equalsIgnoreCase("devDocker")) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.add("Authorization", "Bearer " + lawfirmToken.getToken());

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(file);
            log.debug("Original Filename {}", originalFilename);
            multipartFile = new MultipartInputStreamFileResource(byteArrayInputStream, file, originalFilename);
            LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            body.add("files", multipartFile.getResource());
            path = path != null && path.isEmpty() ? "/" : path;
            body.add("folderPath", URLEncoder.encode(path).replace("+", "%20"));

            HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity
                    = new HttpEntity<>(body, headers);

            ResponseEntity<String> result = restTemplate.exchange(DRIVE_ONEDRIVE_URL + "v2/onedrive/file", HttpMethod.POST, requestEntity, String.class);

            if (result.getBody() != null) {
                log.info("Leaving uploadFileInbox with success");
            } else {
                log.info("Leaving uploadFileInbox EMPTY NO RESULT");

            }
        }
        log.info("Leaving uploadFileInbox");
    }

    @Override
    public List<ObjectResponseDTO> getDossierList(LawfirmToken lawfirmToken, String path) {
        log.info("Entering getDossierList with container : {} and paths {}", lawfirmToken.getVcKey(), path);
        if (!activeProfile.equalsIgnoreCase("integrationtest")
                && !activeProfile.equalsIgnoreCase("devDocker")) {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + lawfirmToken.getToken());

            LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("folderPath", URLEncoder.encode(path).replace("+", "%20"));

            HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity
                    = new HttpEntity<>(body, headers);

            ResponseEntity<ObjectResponseDTO[]> result = restTemplate.exchange(DRIVE_ONEDRIVE_URL + "v2/onedrive/folder/" + URLEncoder.encode(path).replace("+", "%20"), HttpMethod.GET, requestEntity, ObjectResponseDTO[].class);

            if (result.getBody() != null) {
                log.info("Leaving getDossierList with success");
                return Arrays.stream(result.getBody()).map(folder -> {
                            ObjectResponseDTO response = folder;

                            return response.getSize() == null ? response : null;
                        }).filter(Objects::nonNull)
                        .collect(Collectors.toList());

            } else {
                log.info("Leaving getDossierList EMPTY NO RESULT");

                return new ArrayList<>();
            }
        }
        log.info("Leaving getDossierList EMPTY");
        return new ArrayList<>();
    }

    @Override
    public List<ObjectResponseDTO> getObjects(LawfirmToken lawfirmToken, String path) {
        log.info("Entering getObjects with container : {} and paths {}", lawfirmToken.getVcKey(), path);
        if (!activeProfile.equalsIgnoreCase("integrationtest")
                && !activeProfile.equalsIgnoreCase("devDocker")) {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + lawfirmToken.getToken());

            LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity
                    = new HttpEntity<>(body, headers);

            ResponseEntity<ObjectResponseDTO[]> result = restTemplate.exchange(DRIVE_ONEDRIVE_URL + "v2/onedrive/folder/" + URLEncoder.encode(path).replace("+", "%20"), HttpMethod.GET, requestEntity, ObjectResponseDTO[].class);

            if (result.getBody() != null) {
                log.info("Leaving getObjects with success");
                return Arrays.asList(result.getBody());
            } else {
                log.info("Leaving getObjects EMPTY NO RESULT");

                return new ArrayList<>();
            }
        }
        log.info("Leaving getObjects EMPTY");
        return new ArrayList<>();
    }

    @Override
    public List<ObjectResponseDTO> getDispatchingFilesList(LawfirmToken lawfirmToken, String path) {
        log.info("Entering getDispatchingFilesList with container : {} and paths {}", lawfirmToken.getVcKey(), path);
        if (!activeProfile.equalsIgnoreCase("integrationtest")
                && !activeProfile.equalsIgnoreCase("devDocker")) {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + lawfirmToken.getToken());

            LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("folderPath", URLEncoder.encode(path).replace("+", "%20"));

            HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity
                    = new HttpEntity<>(body, headers);

            ResponseEntity<ObjectResponseDTO[]> result = restTemplate.exchange(DRIVE_ONEDRIVE_URL + "v2/onedrive/folder/" + URLEncoder.encode(path).replace("+", "%20"), HttpMethod.GET, requestEntity, ObjectResponseDTO[].class);

            if (result.getBody() != null) {
                log.info("Leaving getDispatchingFilesList with success");
                return Arrays.asList(result.getBody());
            } else {
                log.info("Leaving getDispatchingFilesList EMPTY NO RESULT");

                return new ArrayList<>();
            }
        }
        log.info("Leaving getDispatchingFilesList EMPTY");
        return new ArrayList<>();
    }

    @Override
    public String deletingFile(LawfirmToken lawfirmToken, String path) {
        log.info("Entering deletingFile with container : {} , path {}", lawfirmToken.getVcKey(), path);
        if (!activeProfile.equalsIgnoreCase("integrationtest")
                && !activeProfile.equalsIgnoreCase("devDocker")) {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.TEXT_PLAIN));
            headers.setContentType(MediaType.TEXT_PLAIN);
            headers.add("Authorization", "Bearer " + lawfirmToken.getToken());

            HttpEntity<String> requestEntity
                    = new HttpEntity<>(URLEncoder.encode(path).replace("+", "%20"), headers);

            ResponseEntity<String> result = restTemplate.exchange(DRIVE_ONEDRIVE_URL + "v2/onedrive/file", HttpMethod.DELETE, requestEntity, String.class);

            if (result.getBody() != null && !result.getBody().isEmpty()) {
                log.info("Leaving deletingFile  ok");

                return "ok";
            }

        }
        log.info("Leaving deletingFile not ok");
        return "nok";
    }

    @Override
    public String deletingFolder(LawfirmToken lawfirmToken, String path) {
        log.info("Entering deletingFolder with container : {} , path {}", lawfirmToken.getVcKey(), path);
        if (!activeProfile.equalsIgnoreCase("integrationtest")
                && !activeProfile.equalsIgnoreCase("devDocker")) {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.TEXT_PLAIN));
            headers.setContentType(MediaType.TEXT_PLAIN);
            headers.add("Authorization", "Bearer " + lawfirmToken.getToken());

            HttpEntity<String> requestEntity
                    = new HttpEntity<>(URLEncoder.encode(path).replace("+", "%20"), headers);

            ResponseEntity<String> result = restTemplate.exchange(DRIVE_ONEDRIVE_URL + "v2/onedrive/folder", HttpMethod.DELETE, requestEntity, String.class);

            if (result.getBody() != null && !result.getBody().isEmpty()) {
                log.info("Leaving deletingFile  ok");

                return "ok";
            }

        }
        log.info("Leaving deletingFile not ok");
        return "nok";
    }

    @Override
    public ByteArrayResource downloadFile(LawfirmToken lawfirmToken, String path) {
        log.info("Entering downloadFile with container : {} and path {}", lawfirmToken.getVcKey(), path);
        if (!activeProfile.equalsIgnoreCase("integrationtest")
                && !activeProfile.equalsIgnoreCase("devDocker")) {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + lawfirmToken.getToken());

            LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity
                    = new HttpEntity<>(body, headers);

            ResponseEntity<byte[]> result = restTemplate.exchange(DRIVE_ONEDRIVE_URL + "v2/onedrive/file/" + URLEncoder.encode("/" + path).replace("+", "%20"), HttpMethod.GET, requestEntity, byte[].class);

            if (result.getBody() != null) {
                log.info("Leaving downloadFile  ok");

                return new ByteArrayResource(result.getBody());
            }

        }
        log.info("Leaving downloadFile not ok");
        byte[] emptyArray = new byte[0];
        return new ByteArrayResource(emptyArray);
    }

    @Override
    public String getShareLink(LawfirmToken lawfirmToken, String path) {
        log.info("Entering getShareLink with container : {} and paths {}", lawfirmToken.getVcKey(), path);
        if (!activeProfile.equalsIgnoreCase("integrationtest")
                && !activeProfile.equalsIgnoreCase("devDocker")) {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + lawfirmToken.getToken());

            LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity
                    = new HttpEntity<>(body, headers);


            ResponseEntity<String> result = restTemplate.exchange(DRIVE_ONEDRIVE_URL + "v2/onedrive/share/link/" + URLEncoder.encode(path).replace("+", "%20"), HttpMethod.GET, requestEntity, String.class);

            if (result.getBody() != null) {
                log.info("Leaving getShareLink with success");
                return result.getBody();
            } else {
                log.info("Leaving getShareLink EMPTY NO RESULT");

                return "";
            }
        }
        log.info("Leaving getShareLink EMPTY");
        return "";
    }

    @Override
    public Boolean shareObject(LawfirmToken lawfirmToken, ShareFileDTO shareFileDTO) {
        log.info("Entering shareObject with container : {} and paths {}", lawfirmToken.getVcKey(), shareFileDTO);
        if (!activeProfile.equalsIgnoreCase("integrationtest")
                && !activeProfile.equalsIgnoreCase("devDocker")) {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + lawfirmToken.getToken());

            shareFileDTO.setShared_with(shareFileDTO.getUsersItem().stream().map(ItemStringDto::getValue).collect(Collectors.toList()));
            HttpEntity<ShareFileDTO> requestEntity = new HttpEntity<>(shareFileDTO, headers);

            ResponseEntity<Boolean> result = restTemplate.exchange(DRIVE_ONEDRIVE_URL + "v2/onedrive/share", HttpMethod.POST, requestEntity, Boolean.class);

            if (result.getBody() != null) {
                log.info("Leaving shareObject with success");
                return result.getBody();
            } else {
                log.info("Leaving shareObject EMPTY NO RESULT");

                return false;
            }
        }
        log.info("Leaving getShareLink EMPTY");
        return false;
    }

    @Override
    public Integer getSizeContainer(LawfirmToken lawfirmToken, String vcKey) {
        log.info("Entering getSizeContainer with container : {} ", lawfirmToken.getVcKey());
        if (!activeProfile.equalsIgnoreCase("integrationtest")
                && !activeProfile.equalsIgnoreCase("devDocker")) {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + lawfirmToken.getToken());

            LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity
                    = new HttpEntity<>(body, headers);


            ResponseEntity<Integer> result = restTemplate.exchange(DRIVE_ONEDRIVE_URL + "v2/onedrive/containers/" + URLEncoder.encode(lawfirmToken.getVcKey()).replace("+", "%20") + "/size", HttpMethod.GET, requestEntity, Integer.class);

            if (result.getBody() != null) {
                log.info("Leaving getSizeContainer with success");
                return result.getBody();
            } else {
                log.info("Leaving getSizeContainer EMPTY NO RESULT");

                return 0;
            }
        }
        log.info("Leaving getSizeContainer EMPTY");
        return 0;
    }

    @Override
    public String moveFile(LawfirmToken lawfirmToken, String filename, String fromPath, String pathTo) {
        log.info("Entering moveFile with container : {} and fromPath {}, pathTo {}", lawfirmToken.getVcKey(), fromPath, pathTo);
        if (!activeProfile.equalsIgnoreCase("integrationtest")
                && !activeProfile.equalsIgnoreCase("devDocker")) {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + lawfirmToken.getToken());
            RequestDropbox requestDropbox = new RequestDropbox();
            requestDropbox.setPath(URLEncoder.encode(fromPath).replace("+", "%20"));
            requestDropbox.setNewPath(URLEncoder.encode(pathTo + filename).replace("+", "%20"));

            HttpEntity<RequestDropbox> requestEntity = new HttpEntity<>(requestDropbox, headers);

            ResponseEntity<String> result = restTemplate.exchange(DRIVE_ONEDRIVE_URL + "v2/onedrive/move", HttpMethod.POST, requestEntity, String.class);

            if (result.getBody() != null && !result.getBody().isEmpty()) {
                log.info("Leaving moveFile  ok");

                return "ok";
            }

        }
        log.info("Leaving moveFile not ok");
        return "nok";
    }

    @Override
    public String moveFolders(String token, String vcKey, String fromPath, String pathTo) {
        log.info("Entering moveFolders with container : {} and fromPath {}, pathTo {}", vcKey, fromPath, pathTo);
        if (!activeProfile.equalsIgnoreCase("integrationtest")
                && !activeProfile.equalsIgnoreCase("devDocker")) {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + token);
            RequestDropbox requestDropbox = new RequestDropbox();
            requestDropbox.setPath(URLEncoder.encode(fromPath).replace("+", "%20"));
            requestDropbox.setNewPath(URLEncoder.encode(pathTo).replace("+", "%20"));

            HttpEntity<RequestDropbox> requestEntity = new HttpEntity<>(requestDropbox, headers);

            ResponseEntity<String> result = restTemplate.exchange(DRIVE_ONEDRIVE_URL + "v2/onedrive/move", HttpMethod.POST, requestEntity, String.class);

            if (result.getBody() != null && !result.getBody().isEmpty()) {
                log.info("Leaving moveFolders  ok");

                return "ok";
            }

        }
        log.info("Leaving moveFolders not ok");
        return "nok";
    }

}
