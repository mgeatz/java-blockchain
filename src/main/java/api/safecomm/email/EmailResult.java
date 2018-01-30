package api.safecomm.email;

public class EmailResult {

    private String result;
    private long id;

    public EmailResult(long id, String result) {
        this.id = id;
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public long getId() {
        return id;
    }

}
