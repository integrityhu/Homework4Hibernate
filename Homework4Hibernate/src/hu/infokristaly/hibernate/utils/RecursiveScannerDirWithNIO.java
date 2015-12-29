/**
 * 
 */
package hu.infokristaly.hibernate.utils;

import hu.infokristaly.hibernate.model.FileSystemInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.hibernate.Session;

/**
 * @author pzoli
 *
 */
public class RecursiveScannerDirWithNIO extends Thread {
    private String pattern = "*";
    private Session session;
    private FileSystemInfo fileSystemInfo;
    
    public FileSystemInfo getFileSystemInfo() {
        return fileSystemInfo;
    }

    public void setFileSystemInfo(FileSystemInfo fileSystemInfo) {
        this.fileSystemInfo = fileSystemInfo;
    }

    public RecursiveScannerDirWithNIO(Session session) {
        this.session = session;
    }

    @Override
    public void run() {
        Path rootDirPath = Paths.get(fileSystemInfo.getPath());
        Finder finder = new Finder(session);
        finder.setFileSystemInfo(fileSystemInfo);
        finder.addMatcher(pattern);
        try {
            Date start = new Date();
            Files.walkFileTree(rootDirPath, finder);
            Date end = new Date();
            System.out.println(end.getTime()-start.getTime() + " ms");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            finder.done();
        }
    }

}
