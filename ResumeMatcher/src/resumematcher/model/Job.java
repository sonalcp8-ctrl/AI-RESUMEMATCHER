package resumematcher.model;

import java.util.LinkedHashSet;
import java.util.Set;

public class Job {
    private final int id;
    private final String title;
    private final String company;
    private final String location;
    private final Set<String> skills = new LinkedHashSet<>();
    private int score;

    public Job(int id, String title, String company, String location) {
        this.id = id;
        this.title = title;
        this.company = company;
        this.location = location;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getCompany() { return company; }
    public String getLocation() { return location; }
    public Set<String> getSkills() { return skills; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
}
