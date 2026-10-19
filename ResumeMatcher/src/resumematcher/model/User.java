package resumematcher.model;

public class User {
    private final int id;
    private String name;
    private final String email;
    private String resumePath;

    public User(int id, String name, String email, String resumePath) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.resumePath = resumePath;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public String getResumePath() { return resumePath; }
    public void setResumePath(String resumePath) { this.resumePath = resumePath; }
}
