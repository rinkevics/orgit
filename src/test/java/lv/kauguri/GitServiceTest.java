package lv.kauguri;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GitServiceTest extends GitTestSetup {

    private GitService gitService;

    @BeforeEach
    @Override
    public void setUp() throws IOException, InterruptedException {
        super.setUp();
        gitService = new GitService();
    }

    @Test
    public void testGetGitDirs_returnsOnlyGitRepositories() {
        // Given: We have 2 git repos and 1 non-git directory in orgit tmp folder
        Path orgitDir = getOrgitTmpDir();

        // When: We call getGitDirs with the orgit tmp directory
        List<Path> gitDirs = gitService.getGitDirs(orgitDir.toString());

        // Then: Should return only the 2 git repositories, not the non-git directory
        assertNotNull(gitDirs, "getGitDirs should not return null");
        assertEquals(2, gitDirs.size(), "Should return exactly 2 git directories");

        // Verify that the list contains project1 and project2
        assertTrue(gitDirs.stream().anyMatch(dir -> dir.toString().contains("project1")),
                "Should contain project1");
        assertTrue(gitDirs.stream().anyMatch(dir -> dir.toString().contains("project2")),
                "Should contain project2");

        // Verify that the list does NOT contain project3 (non-git directory)
        assertFalse(gitDirs.stream().anyMatch(dir -> dir.toString().contains("project3")),
                "Should NOT contain project3 (non-git directory)");

        // Verify that returned paths are absolute full paths
        assertTrue(gitDirs.stream().allMatch(Path::isAbsolute),
                "All returned paths should be absolute");
    }

    @Test
    public void testGetGitDirs_withEmptyDirectory() throws IOException {
        // Given: An empty directory
        Path emptyDir = getOrgitTmpDir().resolve("empty");
        java.nio.file.Files.createDirectories(emptyDir);

        // When: We call getGitDirs with an empty directory
        List<Path> gitDirs = gitService.getGitDirs(emptyDir.toString());

        // Then: Should return an empty list
        assertNotNull(gitDirs, "getGitDirs should not return null");
        assertTrue(gitDirs.isEmpty(), "Should return empty list for directory with no git repos");
    }

    @Test
    public void testGetGitDirs_withNullPath() {
        // When/Then: Calling with null should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            gitService.getGitDirs(null);
        }, "Should throw IllegalArgumentException for null path");
    }
}
