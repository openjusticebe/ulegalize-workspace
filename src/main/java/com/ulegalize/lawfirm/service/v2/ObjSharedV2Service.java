package com.ulegalize.lawfirm.service.v2;

import com.ulegalize.dto.ShareFileDTO;

import java.util.List;

public interface ObjSharedV2Service {
    public void shareObject(ShareFileDTO shareFileDTO);

    public void shareFolder(String obj, String vckey, String username, Long userIdFrom, long size, List<String> emails, String message, int right, String clientFrom);

    ShareFileDTO getShareObject(String path);

    String getShareLink(String path);
}
