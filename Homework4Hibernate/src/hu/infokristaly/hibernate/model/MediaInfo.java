/**
 * 
 */
package hu.infokristaly.hibernate.model;

/**
 * @author pzoli
 *
 */
public class MediaInfo {

    private Long id;
    
    private String name;
        
    private String comment;

    public MediaInfo() {
    }

    public MediaInfo(String name) {
        this.name = name;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
