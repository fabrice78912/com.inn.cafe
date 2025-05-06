package com.inn.cafe.serviceImpl;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class InMemoryMultipartFile implements MultipartFile {
    private final String name;
    private final String originalFilename;
    private final String contentType;
    private final byte[] content;

    public InMemoryMultipartFile(String name, String originalFilename, String contentType, byte[] content) {
        this.name = name;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.content = content;
    }

    // Implement required methods from MultipartFile interface
    // (getters, etc.) based on your specific needs
    // ...

    /**
     * Return the name of the parameter in the multipart form.
     *
     * @return the name of the parameter (never {@code null} or empty)
     */
    @Override
    public String getName() {
        return null;
    }

    /**
     * Return the original filename in the client's filesystem.
     * <p>This may contain path information depending on the browser used,
     * but it typically will not with any other than Opera.
     * <p><strong>Note:</strong> Please keep in mind this filename is supplied
     * by the client and should not be used blindly. In addition to not using
     * the directory portion, the file name could also contain characters such
     * as ".." and others that can be used maliciously. It is recommended to not
     * use this filename directly. Preferably generate a unique one and save
     * this one somewhere for reference, if necessary.
     *
     * @return the original filename, or the empty String if no file has been chosen
     * in the multipart form, or {@code null} if not defined or not available
     * @see org.apache.commons.fileupload.FileItem#getName()
     * @see CommonsMultipartFile#setPreserveFilename
     * @see <a href="https://tools.ietf.org/html/rfc7578#section-4.2">RFC 7578, Section 4.2</a>
     * @see <a href="https://owasp.org/www-community/vulnerabilities/Unrestricted_File_Upload">Unrestricted File Upload</a>
     */
    @Override
    public String getOriginalFilename() {
        return null;
    }

    /**
     * Return the content type of the file.
     *
     * @return the content type, or {@code null} if not defined
     * (or no file has been chosen in the multipart form)
     */
    @Override
    public String getContentType() {
        return null;
    }

    /**
     * Return whether the uploaded file is empty, that is, either no file has
     * been chosen in the multipart form or the chosen file has no content.
     */
    @Override
    public boolean isEmpty() {
        return false;
    }

    /**
     * Return the size of the file in bytes.
     *
     * @return the size of the file, or 0 if empty
     */
    @Override
    public long getSize() {
        return 0;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return content;
    }

    /**
     * Return an InputStream to read the contents of the file from.
     * <p>The user is responsible for closing the returned stream.
     *
     * @return the contents of the file as stream, or an empty stream if empty
     * @throws IOException in case of access errors (if the temporary store fails)
     */
    @Override
    public InputStream getInputStream() throws IOException {
        return null;
    }

    /**
     * Transfer the received file to the given destination file.
     * <p>This may either move the file in the filesystem, copy the file in the
     * filesystem, or save memory-held contents to the destination file. If the
     * destination file already exists, it will be deleted first.
     * <p>If the target file has been moved in the filesystem, this operation
     * cannot be invoked again afterwards. Therefore, call this method just once
     * in order to work with any storage mechanism.
     * <p><b>NOTE:</b> Depending on the underlying provider, temporary storage
     * may be container-dependent, including the base directory for relative
     * destinations specified here (e.g. with Servlet 3.0 multipart handling).
     * For absolute destinations, the target file may get renamed/moved from its
     * temporary location or newly copied, even if a temporary copy already exists.
     *
     * @param dest the destination file (typically absolute)
     * @throws IOException           in case of reading or writing errors
     * @throws IllegalStateException if the file has already been moved
     *                               in the filesystem and is not available anymore for another transfer
     * @see org.apache.commons.fileupload.FileItem#write(File)
     * @see Part#write(String)
     */
    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {

    }
}