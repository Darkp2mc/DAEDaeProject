package dtos;

public class DocumentDTO {
    private int id;
    private String filepath;
    private String filename;

    public DocumentDTO(int id, String filepath, String filename) {
        this.id = id;
        this.filepath = filepath;
        this.filename = filename;
    }
}
