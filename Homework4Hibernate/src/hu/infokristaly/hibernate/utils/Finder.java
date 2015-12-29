/**
 * 
 */
package hu.infokristaly.hibernate.utils;

import hu.infokristaly.hibernate.model.FileSystemDirInfo;
import hu.infokristaly.hibernate.model.FileSystemFileInfo;
import hu.infokristaly.hibernate.model.FileSystemInfo;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @author pzoli
 * 
 */
public class Finder extends SimpleFileVisitor<Path> {

    private static final int COMMIT_COUNT = 20;

    private final List<PathMatcher> matchers = new LinkedList<PathMatcher>();
    private int numMatches = 0;
    private int counter = 0;
    private Session session;
    private FileSystemInfo fileSystemInfo;
    private FileSystemDirInfo lastDir = null;
    private Transaction tx;

    private FileSystemDirInfo getByPath(Path find) {
        Query query = session.getNamedQuery("findFileSystemDirInfo");
        query.setParameter("path", find.toString());
        query.setParameter("fileSystemInfo", fileSystemInfo);
        @SuppressWarnings("unchecked")
        List<FileSystemDirInfo> result = query.list();
        return (result.size() > 0 ? result.get(0) : null);
    }

    private FileSystemDirInfo getLastPath(Path find) {
        if ((lastDir != null) && lastDir.getPath().toString().equals(find.toString())) {
            return lastDir;
        } else {
            return getByPath(find);
        }
    }

    private FileSystemFileInfo generateFileInfo(Path f, BasicFileAttributes a) {
        FileSystemFileInfo fileInfo = new FileSystemFileInfo();
        fileInfo.setFileName(f.getFileName().toString());
        fileInfo.setSize(a.size());
        fileInfo.setLastModified(new Date(a.lastModifiedTime().toMillis()));
        fileInfo.setUploadDate(new Date());
        return fileInfo;
    }

    private FileSystemDirInfo generateDirInfo(Path f, BasicFileAttributes a) {
        FileSystemDirInfo dirInfo = new FileSystemDirInfo();
        dirInfo.setPath(f.toString());
        dirInfo.setLastModified(new Date(a.lastModifiedTime().toMillis()));
        dirInfo.setUploadDate(new Date());
        return dirInfo;
    }

    public Finder(Session session) {
        this.session = session;
    }

    private boolean checkMatchers(Path path) {
        boolean result = true;
        for(PathMatcher matcher: matchers) {
            if (!matcher.matches(path)) {
                result = false;
                break;
            }
        }
        return result;
    }
    
    // Compares the glob pattern against
    // the file or directory name.
    void find(Path file, BasicFileAttributes attrs) {
        if (tx == null) {
            tx = session.beginTransaction();
        }

        Path name = file.getFileName();
        if (name != null && checkMatchers(name)) {
            FileSystemDirInfo currentPath = getLastPath(file.getParent());
            if (attrs.isDirectory()) {
                FileSystemDirInfo dirInfo = generateDirInfo(file, attrs);
                if (currentPath != null) {
                    dirInfo.setParentDirInfo(currentPath);
                }
                dirInfo.setFileSystemInfo(fileSystemInfo);
                session.save(dirInfo);
                lastDir = dirInfo;
            } else {
                FileSystemFileInfo fileInfo = generateFileInfo(file, attrs);
                fileInfo.setDirInfo(currentPath);
                session.save(fileInfo);
            }

            numMatches++;
            counter++;
            if (counter > COMMIT_COUNT) {
                if ((tx != null) && tx.isActive()) {
                    tx.commit();
                    tx = null;
                }
            }
            System.out.println(file);
        }
    }

    // Prints the total number of
    // matches to standard out.
    void done() {
        System.out.println("Matched: " + numMatches);
        if ((tx != null) && (tx.isActive())) {
            tx.commit();
        }
    }

    // Invoke the pattern matching
    // method on each file.
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        find(file, attrs);
        return FileVisitResult.CONTINUE;
    }

    // Invoke the pattern matching
    // method on each directory.
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        find(dir, attrs);
        FileVisitResult result = attrs.isSymbolicLink() ? FileVisitResult.SKIP_SUBTREE : FileVisitResult.CONTINUE; 
        return result;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        System.err.println(exc);
        return FileVisitResult.CONTINUE;
    }

    /**
     * @param string
     */
    public void addMatcher(String pattern) {
        matchers.add(FileSystems.getDefault().getPathMatcher("glob:" + pattern));
    }

    public FileSystemInfo getFileSystemInfo() {
        return fileSystemInfo;
    }

    public void setFileSystemInfo(FileSystemInfo fileSystemInfo) {
        this.fileSystemInfo = fileSystemInfo;
    }

}
