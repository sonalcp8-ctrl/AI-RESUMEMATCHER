package resumematcher.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Set;

/**
 * Match score = (required skills the user has / total required skills) x 100.
 * Example: job needs 10 skills, user has 6 of them -> 60%.
 *
 * If the optional C++ program (cpp/matcher or cpp/matcher.exe) is compiled, it is used for the
 * calculation. If it is not there, the same formula runs in plain Java, so the app always works.
 *
 * Current limitation: exact keyword matching only ("JS" does not match "JavaScript").
 * Future improvement: NLP (TF-IDF / cosine similarity) for similar words.
 */
public class Matcher {
    private static Boolean cppAvailable = null;
    private static File cppExe = null;

    public static String normalize(String skill) {
        return skill == null ? "" : skill.trim().toLowerCase();
    }

    public static int score(Set<String> userSkills, Set<String> jobSkills) {
        if (jobSkills.isEmpty()) return 0;
        Integer fromCpp = scoreWithCpp(userSkills, jobSkills);
        return fromCpp != null ? fromCpp : scoreInJava(userSkills, jobSkills);
    }

    public static int scoreInJava(Set<String> userSkills, Set<String> jobSkills) {
        if (jobSkills.isEmpty()) return 0;
        int matched = 0;
        for (String skill : jobSkills) {
            if (userSkills.contains(skill)) matched++;
        }
        return (int) Math.round(100.0 * matched / jobSkills.size());
    }

    private static Integer scoreWithCpp(Set<String> userSkills, Set<String> jobSkills) {
        if (cppAvailable == null) detectCpp();
        if (!cppAvailable) return null;
        try {
            ProcessBuilder pb = new ProcessBuilder(cppExe.getPath(), join(userSkills), join(jobSkills));
            pb.redirectErrorStream(true);
            Process p = pb.start();
            try (BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line = r.readLine();
                p.waitFor();
                if (line != null) return Integer.parseInt(line.trim());
            }
        } catch (Exception e) {
            cppAvailable = false;   // fall back to Java from now on
        }
        return null;
    }

    private static void detectCpp() {
        File[] candidates = { new File("cpp/matcher.exe"), new File("cpp/matcher") };
        cppAvailable = false;
        for (File f : candidates) {
            if (f.isFile()) {
                cppExe = f;
                cppAvailable = true;
                break;
            }
        }
    }

    private static String join(Set<String> skills) {
        StringBuilder sb = new StringBuilder();
        for (String s : skills) {
            if (sb.length() > 0) sb.append(',');
            sb.append(s.replace(",", " "));
        }
        return sb.toString();
    }
}
