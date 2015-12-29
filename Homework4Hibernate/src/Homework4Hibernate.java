import java.io.File;
import java.util.Date;
import java.util.List;

import hu.infokristaly.hibernate.model.FileSystemDirInfo;
import hu.infokristaly.hibernate.model.FileSystemInfo;
import hu.infokristaly.hibernate.model.LocationInfo;
import hu.infokristaly.hibernate.model.MediaInfo;
import hu.infokristaly.hibernate.tasks.ScanDir;
import hu.infokristaly.hibernate.utils.HibernateUtils;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

/**
 * 
 */

/**
 * @author pzoli
 *         http://www.tutorialspoint.com/hibernate/hibernate_criteria_queries
 *         .htm
 * 
 */
public class Homework4Hibernate {

    private static Session session;

    @SuppressWarnings("static-access")
    public static Options getOprions() {
        Options options = new Options();
        options.addOption(OptionBuilder.isRequired(true).withArgName("target").withLongOpt("target").hasArg(true).withDescription("directory for indexing").create());
        options.addOption(OptionBuilder.isRequired(true).withArgName("name").withLongOpt("name").hasArg(true).withDescription("name of target").create());
        options.addOption(OptionBuilder.isRequired(true).withArgName("media").withLongOpt("media").hasArg(true).withDescription("media type like DVD/HDD/CD").create());
        options.addOption(OptionBuilder.isRequired(true).withArgName("location").withLongOpt("location").hasArg(true).withDescription("location of media").create());
        return options;
    }

    public static void main(String[] args) {
        Options options = getOprions();
        CommandLine commandLine = null;
        Parser parser = new GnuParser();
        try {
            commandLine = parser.parse(options, args);
            String target = (String) commandLine.getOptionValue("target");
            String name = (String) commandLine.getOptionValue("name");
            String media = (String) commandLine.getOptionValue("media");
            String location = (String) commandLine.getOptionValue("location");
                    
            Configuration config = HibernateUtils.getHibernateConfig(new File("hibernate.cfg.xml"));
            SessionFactory sessionFactory = HibernateUtils.getSessionFactory(config);
            session = sessionFactory.openSession();
            ScanDir scanDir = new ScanDir(session);

            session.beginTransaction();
            
            MediaInfo mediaInfo = findMedia(media);
            session.save(mediaInfo);
            
            LocationInfo locationInfo = findLocation(location);
            session.save(locationInfo);
                    
            FileSystemInfo fileSystemInfo = new FileSystemInfo();
            fileSystemInfo.setMediaInfo(mediaInfo);
            fileSystemInfo.setLocationInfo(locationInfo);
            fileSystemInfo.setPath(target);
            fileSystemInfo.setName(name);
            session.save(fileSystemInfo);
            
            FileSystemDirInfo rootDirInfo = new FileSystemDirInfo();
            rootDirInfo.setFileSystemInfo(fileSystemInfo);
            rootDirInfo.setPath(fileSystemInfo.getPath());
            rootDirInfo.setLastModified(fileSystemInfo.getLastModified());
            rootDirInfo.setUploadDate(new Date());
            session.save(rootDirInfo);
            
            session.getTransaction().commit();
            scanDir.hibernateWithNIO(fileSystemInfo, session);
            session.close();
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar homework4hibernate.jar", options);
        }
    }

    /**
     * @param mediaName
     * @return
     */
    private static MediaInfo findMedia(String mediaName) {
        MediaInfo result = null;
        Criteria criteria = session.createCriteria(MediaInfo.class);
        criteria.add(Restrictions.eq("name", mediaName));
        @SuppressWarnings("rawtypes")
        List mediaList = criteria.list();
        if (mediaList.size() > 0) {
            result = (MediaInfo) mediaList.get(0);
        } else {
            result = new MediaInfo(mediaName);
        }
        return result;
    }

    /**
     * @param mediaName
     * @return
     */
    private static LocationInfo findLocation(String place) {
        LocationInfo result = null;
        Criteria criteria = session.createCriteria(LocationInfo.class);
        criteria.add(Restrictions.eq("place", place));
        @SuppressWarnings("rawtypes")
        List locationList = criteria.list();
        if (locationList.size() > 0) {
            result = (LocationInfo) locationList.get(0);
        } else {
            result = new LocationInfo(place);
        }
        return result;
    }

}
