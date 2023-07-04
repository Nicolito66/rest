package classes;

public class Response {
    private Object object;
    private int status;
    private String message;

    public Response(Object object, int status, String message) {
        this.object = object;
        this.status = status;
        this.message = message;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
