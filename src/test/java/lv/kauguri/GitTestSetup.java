package lv.kauguri;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class GitTestSetup {

    private Path orgitTmpDir;
    private Path gitProject1;
    private Path gitProject2;
    private Path nonGitProject3;
    private Path originDir;
    private Path originProject1;
    private Path originProject2;

    @BeforeEach
    public void setUp() throws IOException, InterruptedException {
        // 1.2.1. Create a tmp directory orgit in system tmp directory
        String systemTmpDir = System.getProperty("java.io.tmpdir");
        orgitTmpDir = Paths.get(systemTmpDir, "orgit");

        // Clean up if exists from previous run
        if (Files.exists(orgitTmpDir)) {
            deleteDirectory(orgitTmpDir.toFile());
        }

        Files.createDirectories(orgitTmpDir);
        System.out.println("Created tmp directory: " + orgitTmpDir);

        // 1.2.2. Create 2 subdirectories, each subdirectory should be a git project
        gitProject1 = orgitTmpDir.resolve("project1");
        gitProject2 = orgitTmpDir.resolve("project2");

        Files.createDirectories(gitProject1);
        Files.createDirectories(gitProject2);

        // Initialize git repositories
        initGitRepo(gitProject1);
        initGitRepo(gitProject2);

        // 1.2.3. In each git project create and commit a dummy text file
        createAndCommitFile(gitProject1, "dummy1.txt", "This is dummy file in project 1");
        createAndCommitFile(gitProject2, "dummy2.txt", "This is dummy file in project 2");

        // 1.2.4. Add 3rd directory, but it should not be a git project. Add a dummy text file to this directory.
        nonGitProject3 = orgitTmpDir.resolve("project3");
        Files.createDirectories(nonGitProject3);
        Path dummyFile3 = nonGitProject3.resolve("dummy3.txt");
        Files.writeString(dummyFile3, "This is dummy file in non-git project 3");

        // Task 2: Create local-origin for each git project and push changes
        setupOrigins();

        System.out.println("Git test environment setup complete:");
        System.out.println("  Project 1: " + gitProject1);
        System.out.println("  Project 2: " + gitProject2);
        System.out.println("  Project 3 (non-git): " + nonGitProject3);
        System.out.println("  Origin 1: " + originProject1);
        System.out.println("  Origin 2: " + originProject2);
    }

    @AfterEach
    public void tearDown() {
        // Clean up test directories
        if (orgitTmpDir != null && Files.exists(orgitTmpDir)) {
            deleteDirectory(orgitTmpDir.toFile());
            System.out.println("Cleaned up test directories");
        }
    }

    private void initGitRepo(Path repoPath) throws IOException, InterruptedException {
        executeCommand(repoPath, "git", "init");
        executeCommand(repoPath, "git", "config", "user.name", "Test User");
        executeCommand(repoPath, "git", "config", "user.email", "test@example.com");
    }

    private void createAndCommitFile(Path repoPath, String fileName, String content)
            throws IOException, InterruptedException {
        Path filePath = repoPath.resolve(fileName);
        Files.writeString(filePath, content);

        executeCommand(repoPath, "git", "add", fileName);
        executeCommand(repoPath, "git", "commit", "-m", "Add " + fileName);
    }

    private void setupOrigins() throws IOException, InterruptedException {
        // Create origin directory structure under orgit/origin
        originDir = orgitTmpDir.resolve("origin");
        Files.createDirectories(originDir);

        // Create bare repositories for origins
        originProject1 = originDir.resolve("project1.git");
        originProject2 = originDir.resolve("project2.git");

        Files.createDirectories(originProject1);
        Files.createDirectories(originProject2);

        // Initialize bare git repositories
        initBareGitRepo(originProject1);
        initBareGitRepo(originProject2);

        // Configure remote origin for each project and push
        setupRemoteAndPush(gitProject1, originProject1, "project1");
        setupRemoteAndPush(gitProject2, originProject2, "project2");
    }

    private void initBareGitRepo(Path repoPath) throws IOException, InterruptedException {
        executeCommand(repoPath, "git", "init", "--bare");
    }

    private void setupRemoteAndPush(Path projectPath, Path originPath, String projectName)
            throws IOException, InterruptedException {
        // Add remote origin
        executeCommand(projectPath, "git", "remote", "add", "origin", originPath.toString());

        // Push to origin
        executeCommand(projectPath, "git", "push", "-u", "origin", "master");

        System.out.println("  Pushed " + projectName + " to origin");
    }

    private void executeCommand(Path workingDir, String... command)
            throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(workingDir.toFile());
        pb.redirectErrorStream(true);

        Process process = pb.start();

        // Read output
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("  " + line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Command failed: " + Arrays.toString(command) +
                    " (exit code: " + exitCode + ")");
        }
    }

    private void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        }
    }

    private String getGitRemoteUrl(Path projectPath) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("git", "remote", "get-url", "origin");
        pb.directory(projectPath.toFile());
        pb.redirectErrorStream(true);

        Process process = pb.start();

        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Failed to get git remote url for " + projectPath);
        }

        return output.toString().trim();
    }

    @Test
    public void testGitDirectoriesCreated() {
        assertTrue(Files.exists(orgitTmpDir), "orgit tmp directory should exist");
        assertTrue(Files.exists(gitProject1), "project1 should exist");
        assertTrue(Files.exists(gitProject2), "project2 should exist");
        assertTrue(Files.exists(nonGitProject3), "project3 should exist");

        assertTrue(Files.exists(gitProject1.resolve(".git")), "project1 should be a git repo");
        assertTrue(Files.exists(gitProject2.resolve(".git")), "project2 should be a git repo");
        assertFalse(Files.exists(nonGitProject3.resolve(".git")), "project3 should NOT be a git repo");

        assertTrue(Files.exists(gitProject1.resolve("dummy1.txt")), "dummy1.txt should exist");
        assertTrue(Files.exists(gitProject2.resolve("dummy2.txt")), "dummy2.txt should exist");
        assertTrue(Files.exists(nonGitProject3.resolve("dummy3.txt")), "dummy3.txt should exist");

        // Task 2: Verify origins are set up
        assertTrue(Files.exists(originDir), "origin directory should exist");
        assertTrue(Files.exists(originProject1), "origin for project1 should exist");
        assertTrue(Files.exists(originProject2), "origin for project2 should exist");

        // Verify bare repositories
        assertTrue(Files.exists(originProject1.resolve("HEAD")), "origin1 should be a bare git repo");
        assertTrue(Files.exists(originProject2.resolve("HEAD")), "origin2 should be a bare git repo");
        assertTrue(Files.exists(originProject1.resolve("refs")), "origin1 should have refs");
        assertTrue(Files.exists(originProject2.resolve("refs")), "origin2 should have refs");

        // Verify git projects have correct origin remotes configured
        try {
            String origin1Url = getGitRemoteUrl(gitProject1);
            String origin2Url = getGitRemoteUrl(gitProject2);

            assertEquals(originProject1.toString(), origin1Url,
                    "project1 should have origin pointing to " + originProject1);
            assertEquals(originProject2.toString(), origin2Url,
                    "project2 should have origin pointing to " + originProject2);

            System.out.println("  Verified project1 origin: " + origin1Url);
            System.out.println("  Verified project2 origin: " + origin2Url);
        } catch (Exception e) {
            fail("Failed to verify git remote origin: " + e.getMessage());
        }

        System.out.println();
        System.out.println(orgitTmpDir.getFileName().toAbsolutePath());
    }

    public Path getOrgitTmpDir() {
        return orgitTmpDir;
    }

    public Path getGitProject1() {
        return gitProject1;
    }

    public Path getGitProject2() {
        return gitProject2;
    }

    public Path getNonGitProject3() {
        return nonGitProject3;
    }

    public Path getOriginDir() {
        return originDir;
    }

    public Path getOriginProject1() {
        return originProject1;
    }

    public Path getOriginProject2() {
        return originProject2;
    }
}