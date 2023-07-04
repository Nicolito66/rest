package classes;

public class VerificationInfos {

    private String id;

    private String code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public VerificationInfos() {
    }

    public VerificationInfos(String id, String code) {
        this.id = id;
        this.code = code;
    }
}
