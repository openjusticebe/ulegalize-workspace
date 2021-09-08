package com.ulegalize.lawfirm.rest.impl;

import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MultipartInputStreamFileResource implements MultipartFile {
    private final String originalFilename;
    private final InputStream inputStream;
    private final byte[] bytes;

    public MultipartInputStreamFileResource(InputStream inputStream, byte[] bytes, String originalFilename) {
        this.inputStream = inputStream;
        this.bytes = bytes;
        this.originalFilename = originalFilename;
    }

    @Override
    public String getName() {
        // TODO - implementation depends on your requirements
        return originalFilename;
    }

    @Override
    public String getOriginalFilename() {
        // TODO - implementation depends on your requirements
        return originalFilename;
    }

    @Override
    public String getContentType() {
        // TODO - implementation depends on your requirements
        return null;
    }

    @Override
    public boolean isEmpty() {
        return inputStream == null;
    }

    @Override
    public long getSize() {
        return bytes.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return bytes;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return inputStream;
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {

    }

    /**
     * This implementation compares the underlying InputStream.
     */
    @Override
    public boolean equals(@Nullable Object other) {
        return (this == other || (other instanceof MultipartInputStreamFileResource &&
                ((MultipartInputStreamFileResource) other).inputStream.equals(this.inputStream)));
    }

    /**
     * This implementation returns the hash code of the underlying InputStream.
     */
    @Override
    public int hashCode() {
        return this.inputStream.hashCode();
    }

}