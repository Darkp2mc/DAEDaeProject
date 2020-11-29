package dtos;

public class EmailDTO {
    
    private String subject;
    private String message;

    public EmailDTO(String subject, String message) {
        this.subject = subject;
        this.message = message;
    }

    public EmailDTO() {
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
