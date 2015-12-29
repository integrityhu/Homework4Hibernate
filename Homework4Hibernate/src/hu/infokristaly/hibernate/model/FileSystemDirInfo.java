/**
 * 
 */
package hu.infokristaly.hibernate.model;

import java.util.Date;

/**
 * @author pzoli
 *
 */
public class FileSystemDirInfo {

    private Long id;
    
    private String path;
    
    private Date lastModified;
    
    private Date uploadDate;
    
    private FileSystemInfo fileSystemInfo;
    
    private FileSystemDirInfo parentDirInfo;
    
    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public FileSystemInfo getFileSystemInfo() {
        return fileSystemInfo;
    }

    public void setFileSystemInfo(FileSystemInfo fileSystemInfo) {
        this.fileSystemInfo = fileSystemInfo;
    }

    public FileSystemDirInfo getParentDirInfo() {
        return parentDirInfo;
    }

    public void setParentDirInfo(FileSystemDirInfo parentDirInfo) {
        this.parentDirInfo = parentDirInfo;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
