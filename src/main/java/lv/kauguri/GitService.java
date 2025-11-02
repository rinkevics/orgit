package lv.kauguri;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class GitService {

    public List<Path> getGitDirs(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        List<Path> gitDirs = new ArrayList<>();
        Path dirPath = Paths.get(path);

        if (!Files.exists(dirPath) || !Files.isDirectory(dirPath)) {
            return gitDirs;
        }

        try (Stream<Path> paths = Files.list(dirPath)) {
            paths.filter(Files::isDirectory)
                    .filter(this::isGitRepository)
                    .forEach(gitDirs::add);
        } catch (IOException e) {
            throw new RuntimeException("Error reading directory: " + path, e);
        }

        return gitDirs;
    }

    private boolean isGitRepository(Path directory) {
        Path gitDir = directory.resolve(".git");
        return Files.exists(gitDir) && Files.isDirectory(gitDir);
    }

    //public boolean
}
