/*
 * Copyright 2013 Integrity Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author Zoltan Papp
 * 
 */
package hu.infokristaly.hibernate.model;

import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;

public class FileSystemFileInfo {

	private Long id;
    
    private String fileName;
	
    private FileSystemDirInfo dirInfo;

    private Long size;
	
    private Date lastModified;

	private Date uploadDate;
	
	private String comment;
			
	public FileSystemFileInfo() {
		super();
	}   

	/**
     * Gets the FileInfo unique identifier.
     * 
     * @return the FileInfo unique identifier
     */
	public Long getId() {
		return this.id;
	}

	/**
     * Sets the FileInfo unique identifier.
     * 
     * @param id
     *            the new FileInfo unique identifier
     */
	public void setId(Long id) {
		this.id = id;
	}   
	
	/**
     * Gets the file name used on file system.
     * 
     * @return the file name used on file system
     */
	public String getFileName() {
		return this.fileName;
	}

	/**
     * Sets the file name used on file system.
     * 
     * @param fileName
     *            the new file name used on file system
     */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}   
	
	/**
     * Gets the file location path.
     * 
     * @return the file location path
     */
	public FileSystemDirInfo getDirInfo() {
		return this.dirInfo;
	}

	/**
     * Sets the file location path.
     * 
     * @param path
     *            the new file location path
     */
	public void setDirInfo(FileSystemDirInfo path) {
		this.dirInfo = path;
	}   
	
    /**
     * Gets the file size.
     * 
     * @return the file size
     */
	public Long getSize() {
		return this.size;
	}

	/**
     * Sets the file size.
     * 
     * @param size
     *            the new file size
     */
	public void setSize(Long size) {
		this.size = size;
	}   
		
	/**
     * Gets the start date of upload method.
     * 
     * @return the start date of upload method
     */
	public Date getLastModified() {
        return lastModified;
    }

    /**
     * Sets the start date of upload method.
     * 
     * @param uploadStartDate
     *            the new start date of upload method
     */
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * Gets the end date of upload method.
     * 
     * @return the end date of upload method
     */
    public Date getUploadDate() {
		return this.uploadDate;
	}

	/**
     * Sets the end date of upload method.
     * 
     * @param uploadDate
     *            the new end date of upload method
     */
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}   
	
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Gets the size for human reader.
     * 
     * @return the size for human reader
     */
    public String getSizeForHumanReader() {
        if (getSize() == null) {
            return null;
        }
        
        BigDecimal resultSize = new BigDecimal(getSize());
        String sizeUnitForHumanReader = "byte";
        if (resultSize.longValue() > (1024)) {
            resultSize = resultSize.divide(BigDecimal.valueOf(1024));
            sizeUnitForHumanReader = "KByte";
            if (resultSize.longValue() > 1024) {
                resultSize = resultSize.divide(BigDecimal.valueOf(1024));
                sizeUnitForHumanReader = "MByte";
                if (resultSize.longValue() > 1024) {
                    resultSize = resultSize.divide(BigDecimal.valueOf(1024));
                    sizeUnitForHumanReader = "GiB";
                }
            }
        }
        return String.format("%.2f %s", resultSize, sizeUnitForHumanReader);
    }
    
}
