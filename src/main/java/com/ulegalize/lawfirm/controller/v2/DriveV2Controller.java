package com.ulegalize.lawfirm.controller.v2;

import com.ulegalize.dto.*;
import com.ulegalize.enumeration.DriveType;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.rest.DriveFactory;
import com.ulegalize.lawfirm.rest.v2.DriveApi;
import com.ulegalize.lawfirm.service.InvoiceService;
import com.ulegalize.lawfirm.service.v2.ObjSharedV2Service;
import com.ulegalize.lawfirm.utils.DriveUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v2/drive")
@Slf4j
public class DriveV2Controller {
    @Autowired
    private DriveFactory driveFactory;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private ObjSharedV2Service objSharedV2Service;

    @PostMapping("/inbox")
    @ApiIgnore
    public InboxDTO uploadFileInbox(@RequestParam("files") MultipartFile file, @RequestParam("userId") String userId, @RequestParam(value = "dossier", required = false) String dossier) throws IOException {
        log.debug("Entering uploadFileInbox()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("uploadFileInbox by (vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        DriveApi driveApi = driveFactory.getDriveImpl(lawfirmToken.getDriveType());
        driveApi.uploadFile(lawfirmToken, file.getBytes(), file.getOriginalFilename(), DriveUtils.POSTIN_PATH + userId + "/");

        return new InboxDTO(file.getOriginalFilename(), file.getOriginalFilename(), new Date());
    }

    @PostMapping("/template")
    @ApiIgnore
    public InboxDTO uploadTemplate(@RequestParam("files") MultipartFile file) throws IOException {
        log.debug("Entering uploadTemplate()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("uploadTemplate by (vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        // extension must be docx
        DriveApi driveApi = driveFactory.getDriveImpl(lawfirmToken.getDriveType());

        driveApi.uploadFile(lawfirmToken, file.getBytes(), file.getOriginalFilename(), DriveUtils.TEMPLATE_PATH);

        return new InboxDTO(file.getOriginalFilename(), file.getOriginalFilename(), new Date());
    }

    @GetMapping("/inbox")
    @ApiIgnore
    public List<ItemStringDto> getPostingDossierList(@RequestParam String userId, @RequestParam(required = false) String path) {
        log.debug("Entering getDossierList()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("getDossierList by (vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());
        String pathFolders = path != null ? DriveUtils.DOSSIER_PATH + path : DriveUtils.DOSSIER_PATH;
        DriveApi driveApi = driveFactory.getDriveImpl(lawfirmToken.getDriveType());

        List<ObjectResponseDTO> dossierList = driveApi.getDossierList(lawfirmToken, pathFolders);
        return dossierList.stream().map(dossier -> {
            // return folder only
            // and check split "/"  => dossiers/2020/001 so 3 and display it
            if (dossier.getSize() == null
                    && dossier.getName().split("/").length == 3) {
                return new ItemStringDto(dossier.getName(), dossier.getName());
            } else {
                return null;
            }
        })
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(ItemStringDto::getValue).reversed())
                .collect(Collectors.toList());
    }

    @GetMapping("")
    @ApiIgnore
    public List<ObjectResponseDTO> getDossierList(@RequestParam(required = false) String path) {
        log.debug("Entering getDossierList({})", path);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("getDossierList by (vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());


        String pathFolders = path != null && !path.isEmpty() ? path : "/";
        DriveApi driveApi = driveFactory.getDriveImpl(lawfirmToken.getDriveType());

        List<ObjectResponseDTO> dossierList = driveApi.getObjects(lawfirmToken, pathFolders);
        return dossierList.stream().map(dossier -> {
            // remove the path and postin
            if (dossier.getName() != null
                    && !dossier.getName().equalsIgnoreCase(DriveUtils.POSTIN_PATH)
                    && !dossier.getName().equalsIgnoreCase(pathFolders)) {
                return dossier;
            } else {
                return null;
            }
        })
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(ObjectResponseDTO::getName).reversed())
                .collect(Collectors.toList());
    }

    @GetMapping("/inbox/dispatching")
    @ApiIgnore
    public List<InboxDTO> getDispatchingFiles(@RequestParam String userId) {
        log.debug("Entering getDispatchingFiles()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("getDispatchingFiles by (vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());
        DriveApi driveApi = driveFactory.getDriveImpl(lawfirmToken.getDriveType());

        String path = DriveUtils.POSTIN_PATH + userId + "/";
        List<ObjectResponseDTO> dossierList = driveApi.getDispatchingFilesList(lawfirmToken, path);
        return dossierList.stream().map(dossier -> {
            String name = dossier.getName().substring(path.length());
            return new InboxDTO(dossier.getId(), name, dossier.getLastModified());
        }).collect(Collectors.toList());
    }

    @DeleteMapping("/inbox")
    @ApiIgnore
    public String deletingFileInbox(@RequestParam String userId, @RequestParam String filename) {
        log.debug("Entering deletingFileInbox({} and {})", userId, filename);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("deletingFileInbox by (vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());
        String path = DriveUtils.POSTIN_PATH + userId + "/" + filename;
        DriveApi driveApi = driveFactory.getDriveImpl(lawfirmToken.getDriveType());

        return driveApi.deletingFile(lawfirmToken, path);
    }

    @PostMapping("/inbox/move")
    @ApiIgnore
    public String moveFile(@RequestParam String userId, @RequestParam String filename, @RequestParam String pathTo) {
        log.debug("Entering moveFile( to {})", pathTo);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("moveFile by (vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());
        String path = DriveUtils.POSTIN_PATH + userId + "/" + filename;
        DriveApi driveApi = driveFactory.getDriveImpl(lawfirmToken.getDriveType());

        return driveApi.moveFile(lawfirmToken, filename, path, pathTo);
    }

    @GetMapping("/inbox/file")
    @ApiIgnore
    public ResponseEntity<Resource> downloadFileInbox(@RequestParam String userId, @RequestParam String filename) {
        log.debug("Entering downloadFile( user {} to filename {})", userId, filename);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("moveFile by (vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());
        String path = "/" + DriveUtils.POSTIN_PATH + userId + "/" + filename;
        DriveApi driveApi = driveFactory.getDriveImpl(lawfirmToken.getDriveType());

        ByteArrayResource fileResponse = driveApi.downloadFile(lawfirmToken, path);
        log.debug(" Document {} RECEIVED ", fileResponse.getFilename());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=" + fileResponse.getFilename());

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body((fileResponse))
                ;
    }

    @GetMapping("/template/file")
    @ApiIgnore
    public ResponseEntity<Resource> downloadTemplateFile(@RequestParam String filename) {
        log.debug("Entering downloadFile( to filename {})", filename);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("moveFile by (vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());
        String path = "/" + DriveUtils.TEMPLATE_PATH + filename;
        DriveApi driveApi = driveFactory.getDriveImpl(lawfirmToken.getDriveType());

        ByteArrayResource fileResponse = driveApi.downloadFile(lawfirmToken, path);
        log.debug(" Document {} RECEIVED ", fileResponse.getFilename());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=" + fileResponse.getFilename());

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body((fileResponse))
                ;
    }

    @GetMapping("/file")
    @ApiIgnore
    public ResponseEntity<Resource> downloadFile(@RequestParam String path) {
        log.debug("Entering downloadFile( to path {})", path);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("moveFile by (vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());
        DriveApi driveApi = driveFactory.getDriveImpl(lawfirmToken.getDriveType());

        ByteArrayResource fileResponse = driveApi.downloadFile(lawfirmToken, URLDecoder.decode(path));
        log.debug(" Document {} RECEIVED ", fileResponse.getFilename());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=" + fileResponse.getFilename());

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body((fileResponse))
                ;
    }

    @PostMapping("/folder/rename")
    @ApiIgnore
    public String renameFolder(@RequestParam String pathFrom, @RequestParam String pathTo) {
        log.debug("Entering renameFolder(from {} to {})", pathFrom, pathTo);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("renameFolder by (vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());
        String from = pathFrom != null && !pathFrom.isEmpty() ? URLDecoder.decode(pathFrom) : "";
        String to = pathTo != null && !pathTo.isEmpty() ? URLDecoder.decode(pathTo) : "";
        log.debug("Path from {}", from);
        log.debug("Path to {}", to);
        DriveApi driveApi = driveFactory.getDriveImpl(lawfirmToken.getDriveType());

        return driveApi.moveFolders(lawfirmToken, from, to);
    }


    @PostMapping("/file/rename")
    @ApiIgnore
    public String renameFile(@RequestParam String newfilename, @RequestParam String pathFrom, @RequestParam String pathTo) {
        log.debug("Entering renameFile( to {})", pathTo);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("renameFile by (vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());
        String from = pathFrom != null && !pathFrom.isEmpty() ? URLDecoder.decode(pathFrom) : "";
        String to = pathTo != null && !pathTo.isEmpty() ? URLDecoder.decode(pathTo) : "";
        log.debug("Path from {}", from);
        log.debug("Path to {}", to);

        DriveApi driveApi = driveFactory.getDriveImpl(lawfirmToken.getDriveType());

        return driveApi.moveFile(lawfirmToken, newfilename, from, to);
    }

    @PostMapping("/file")
    @ApiIgnore
    public InboxDTO uploadFile(@RequestParam("files") MultipartFile file, @RequestParam("path") String path) throws IOException {
        log.debug("Entering uploadFile({})", path);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("uploadTemplate by (vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        String pathFolder = URLDecoder.decode(path);
        // extension must be docx
        DriveApi driveApi = driveFactory.getDriveImpl(lawfirmToken.getDriveType());

        driveApi.uploadFile(lawfirmToken, file.getBytes(), file.getOriginalFilename(), pathFolder);

        return new InboxDTO(file.getOriginalFilename(), file.getOriginalFilename(), new Date());
    }

    @DeleteMapping("/file")
    @ApiIgnore
    public String deletingFile(@RequestParam String path) {
        log.debug("Entering deletingFile({})", path);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("deletingFile by (vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());
        String pathFile = URLDecoder.decode(path);
        DriveApi driveApi = driveFactory.getDriveImpl(lawfirmToken.getDriveType());

        return driveApi.deletingFile(lawfirmToken, pathFile);
    }

    @DeleteMapping("/folder")
    @ApiIgnore
    public String deletingFolder(@RequestParam String path) {
        log.debug("Entering deletingFolder({})", path);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("deletingFile by (vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());
        String pathFile = URLDecoder.decode(path);
        DriveApi driveApi = driveFactory.getDriveImpl(lawfirmToken.getDriveType());

        return driveApi.deletingFolder(lawfirmToken, pathFile);
    }

    @PostMapping("/folder")
    @ApiIgnore
    public String createFolder(@RequestParam String fullpath) {
        log.debug("Entering createFolder({})", fullpath);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("createFolder by (vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());
        String pathFile = URLDecoder.decode(fullpath);
        DriveApi driveApi = driveFactory.getDriveImpl(lawfirmToken.getDriveType());

        driveApi.createFolders(lawfirmToken, lawfirmToken.getVcKey(), List.of(pathFile));

        return "ok";
    }

    @PostMapping("/share")
    @ApiIgnore
    public String shareObject(@RequestBody ShareFileDTO shareFileDTO) {
        log.debug("Entering shareObject({})", shareFileDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("shareObject by (vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());
        if (CollectionUtils.isEmpty(shareFileDTO.getUsersItem())) {
            log.warn("user item is empty");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user item is empty");
        }


        if (lawfirmToken.getDriveType().equals(DriveType.openstack)) {
            objSharedV2Service.shareObject(shareFileDTO);
        } else {
            DriveApi driveApi = driveFactory.getDriveImpl(lawfirmToken.getDriveType());

            driveApi.shareObject(lawfirmToken, shareFileDTO);
        }
        return "ok";
    }

    @GetMapping("/share")
    @ApiIgnore
    public ShareFileDTO getShareObject(@RequestParam String path) {
        log.debug("Entering getShareObject({})", path);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("getShareObject by (vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        return objSharedV2Service.getShareObject(path);
    }

    @GetMapping("/share/link")
    @ApiIgnore
    public String getShareLink(@RequestParam String path) {
        log.debug("Entering getShareLink({})", path);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("getShareObject by (vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        return objSharedV2Service.getShareLink(path);
    }

    @RequestMapping(path = "/invoice/{id}", method = RequestMethod.GET)
    public ResponseEntity<Resource> getInvoice(@PathVariable Long id) throws IOException {
        log.debug("Entering getInvoice({})", id);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("getInvoice by (vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        ByteArrayResource fileResponse = invoiceService.downloadInvoice(id);
        InvoiceDTO invoiceDTO = invoiceService.getInvoiceById(id, lawfirmToken.getVcKey());


        log.debug(" Document {} RECEIVED ", invoiceDTO.getReference());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=" + invoiceDTO.getReference());

        return ResponseEntity
                .ok()
                .headers(headers)
//                .contentType(MediaType.APPLICATION_PDF)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body((fileResponse))
                ;
    }
}
