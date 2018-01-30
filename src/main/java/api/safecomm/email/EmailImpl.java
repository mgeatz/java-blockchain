package api.safecomm.email;

/**
 * Created by geatz on 7/8/17.
 * Description: resource representation class
 */
public class EmailImpl {

    private String emailAddress;
    private String emailSubject;
    private String emailContent;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getEmailContent() {
        return emailContent;
    }

    public void setEmailContent(String emailContent) {
        this.emailContent = emailContent;
    }

}
