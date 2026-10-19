package resumematcher.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import resumematcher.db.Database;
import resumematcher.model.Job;
import resumematcher.model.User;
import resumematcher.util.Matcher;
import resumematcher.util.Security;

/** All database work in one place. Every query uses PreparedStatement (prevents SQL injection). */
public class Dao {

    // ---------- users ----------

    /** Returns false if the email is already registered. */
    public boolean register(String name, String email, String password) throws SQLException {
        String salt = Security.newSalt();
        String hash = Security.hash(password, salt);
        String sql = "INSERT IGNORE INTO users (name, email, password_hash, salt) VALUES (?, ?, ?, ?)";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email.toLowerCase());
            ps.setString(3, hash);
            ps.setString(4, salt);
            return ps.executeUpdate() == 1;
        }
    }

    /** Returns the user if email and password are correct, otherwise null. */
    public User login(String email, String password) throws SQLException {
        String sql = "SELECT id, name, email, password_hash, salt, resume_path FROM users WHERE email = ?";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email.toLowerCase());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                String expected = rs.getString("password_hash");
                String actual = Security.hash(password, rs.getString("salt"));
                if (!expected.equals(actual)) return null;
                return new User(rs.getInt("id"), rs.getString("name"),
                        rs.getString("email"), rs.getString("resume_path"));
            }
        }
    }

    public void updateName(int userId, String name) throws SQLException {
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement("UPDATE users SET name = ? WHERE id = ?")) {
            ps.setString(1, name);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

    public void updateResumePath(int userId, String path) throws SQLException {
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement("UPDATE users SET resume_path = ? WHERE id = ?")) {
            ps.setString(1, path);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

    // ---------- user skills ----------

    public Set<String> getUserSkills(int userId) throws SQLException {
        Set<String> skills = new LinkedHashSet<>();
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT skill FROM user_skills WHERE user_id = ? ORDER BY skill")) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) skills.add(rs.getString(1));
            }
        }
        return skills;
    }

    public void addSkill(int userId, String skill) throws SQLException {
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT IGNORE INTO user_skills (user_id, skill) VALUES (?, ?)")) {
            ps.setInt(1, userId);
            ps.setString(2, Matcher.normalize(skill));
            ps.executeUpdate();
        }
    }

    public void removeSkill(int userId, String skill) throws SQLException {
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement(
                     "DELETE FROM user_skills WHERE user_id = ? AND skill = ?")) {
            ps.setInt(1, userId);
            ps.setString(2, skill);
            ps.executeUpdate();
        }
    }

    // ---------- jobs ----------

    /** All jobs with their required skills. */
    public List<Job> getJobs() throws SQLException {
        Map<Integer, Job> jobs = new LinkedHashMap<>();
        try (Connection c = Database.get(); Statement st = c.createStatement()) {
            try (ResultSet rs = st.executeQuery("SELECT id, title, company, location FROM jobs ORDER BY id")) {
                while (rs.next()) {
                    jobs.put(rs.getInt(1), new Job(rs.getInt(1), rs.getString(2),
                            rs.getString(3), rs.getString(4)));
                }
            }
            try (ResultSet rs = st.executeQuery("SELECT job_id, skill FROM job_skills ORDER BY id")) {
                while (rs.next()) {
                    Job j = jobs.get(rs.getInt(1));
                    if (j != null) j.getSkills().add(Matcher.normalize(rs.getString(2)));
                }
            }
        }
        return new ArrayList<>(jobs.values());
    }

    /** Jobs with the match score calculated for this user, best match first. */
    public List<Job> getJobMatches(int userId) throws SQLException {
        Set<String> userSkills = getUserSkills(userId);
        List<Job> jobs = getJobs();
        for (Job j : jobs) {
            j.setScore(Matcher.score(userSkills, j.getSkills()));
        }
        jobs.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
        return jobs;
    }

    // ---------- applications ----------

    /** Returns false if the user already applied to this job. */
    public boolean apply(int userId, int jobId, int score) throws SQLException {
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT IGNORE INTO applications (user_id, job_id, match_score) VALUES (?, ?, ?)")) {
            ps.setInt(1, userId);
            ps.setInt(2, jobId);
            ps.setInt(3, score);
            return ps.executeUpdate() == 1;
        }
    }

    /** Each row: title, company, match score, status, applied date. */
    public List<String[]> getApplications(int userId) throws SQLException {
        List<String[]> rows = new ArrayList<>();
        String sql = "SELECT j.title, j.company, a.match_score, a.status, a.applied_on "
                   + "FROM applications a JOIN jobs j ON a.job_id = j.id "
                   + "WHERE a.user_id = ? ORDER BY a.applied_on DESC";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new String[] { rs.getString(1), rs.getString(2),
                            rs.getInt(3) + "%", rs.getString(4), rs.getString(5) });
                }
            }
        }
        return rows;
    }
}
