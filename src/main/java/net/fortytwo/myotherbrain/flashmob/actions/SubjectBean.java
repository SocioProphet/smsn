package net.fortytwo.myotherbrain.flashmob.actions;

/**
 * Author: josh
 * Date: Jul 11, 2009
 * Time: 2:11:27 PM
 */
public abstract class SubjectBean extends ActionBean {
    protected String subject;
       
    public String getSubject() {
        return subject;
    }

    public void setSubject(final String subject) {
        this.subject = subject;
    }
}
