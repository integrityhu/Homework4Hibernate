/**
 * 
 */
package hu.infokristaly.hibernate.tasks;

import hu.infokristaly.hibernate.model.FileInfo;
import hu.infokristaly.hibernate.model.FileSystemInfo;

import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

import org.hibernate.Session;
import org.hibernate.Transaction;

import hu.infokristaly.hibernate.utils.RecursiveScannerDirWithNIO;

/**
 * @author pzoli
 *
 */
public class ScanDir {
    
    private static final int COMMIT_COUNT = 20;
    private Queue<File> dirs = new LinkedList<File>();
    private Session session;

    public ScanDir(Session session) {
        this.session = session;
    }
    
    private static FileInfo generateFileInfo(File f) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName(f.getName());
        fileInfo.setSize(f.length());
        fileInfo.setLastModified(new Date(f.lastModified()));
        fileInfo.setUploadDate(new Date());
        fileInfo.setDir(f.isDirectory());
        return fileInfo;
    }

    public void hibernate(String dir) {
        File file = new File(dir);
        dirs.add(file);
        int cnt = 0;

        while (!dirs.isEmpty()) {
            Transaction tx = null;
            for (File f : dirs.poll().listFiles()) {
                if (tx == null) {
                    tx = session.beginTransaction();
                }
                FileInfo fileInfo = generateFileInfo(f);
                if (f.isDirectory()) {
                    dirs.add(f);
                    fileInfo.setPath(f.getAbsolutePath());
                    System.out.println(f.getAbsolutePath() + " {dir}");
                } else if (f.isFile()) {
                    fileInfo.setPath(f.getAbsolutePath().substring(0,f.getAbsolutePath().length() - f.getName().length()));
                    System.out.println(f.getAbsolutePath() + " {fileName:" + f.getName() + ";length:" + file.length() + "}");
                }
                cnt++;
                session.save(fileInfo);
                if ((cnt > COMMIT_COUNT) && tx.isActive()) {
                    tx.commit();
                    tx = null;
                    cnt = 0;
                }
            }
            if ((tx != null) && tx.isActive()) {
                tx.commit();
            }

        }
    }
    
    public void hibernateWithNIO(FileSystemInfo fileSystemInfo, Session session) {
        RecursiveScannerDirWithNIO fsScanner = new RecursiveScannerDirWithNIO(session);
        fsScanner.setFileSystemInfo(fileSystemInfo);
        fsScanner.start();
        synchronized (this) {
            try {
                fsScanner.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }  
        } 
    }
}
