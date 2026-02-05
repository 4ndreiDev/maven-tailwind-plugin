package com.deey;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class TailwindBinaryManager {
    private final String version;
    private final File downloadDirectory;
    private final String baseUrl;
    private final Log log;

    public TailwindBinaryManager(String version, File downloadDirectory, String baseUrl, Log log) {
        this.version = version;
        this.downloadDirectory = downloadDirectory;
        this.baseUrl = baseUrl;
        this.log = log;
    }

    /**
     * Resolves the binary by detecting the OS and downloading it if necessary.
     */
    public File resolveBinary(boolean forceDownload) throws MojoExecutionException {
        File binaryFile = new File(downloadDirectory, detectOsBinaryName());

        if (shouldDownload(binaryFile, forceDownload)) {
            String downloadUrl = String.format("%s/%s/%s", baseUrl, version, binaryFile.getName());
            download(downloadUrl, binaryFile);
        } else {
            log.info("Using cached Tailwind binary: " + binaryFile.getName());
        }
        ensureExecutable(binaryFile);
        return binaryFile;
    }

    private boolean shouldDownload(File file, boolean force) {
        log.debug("Binary path: " + file.getAbsolutePath());
        log.debug("Force download: " + force);
        log.debug("File exists: " + file.exists());
        if (force && file.exists()) {
            log.info("Forcing re-download of Tailwind binary...");
            return true;
        }
        return !file.exists();
    }

    private void ensureExecutable(File file) {
        if (!file.setExecutable(true)) {
            log.warn("Could not set executable permissions on: " + file.getName());
        }
    }

    private void download(String url, File targetFile) throws MojoExecutionException {
        try {
            File parent = targetFile.getParentFile();
            if (parent != null && !parent.exists() && !parent.mkdirs()) {
                throw new IOException("Failed to create directory: " + parent.getAbsolutePath());
            }

            log.info("Downloading Tailwind CSS " + version + " from: " + url);
            URL downloadSource = new URL(url);
            try (InputStream in = downloadSource.openStream()) {
                Files.copy(in, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            log.info("Download completed successfully.");
        } catch (IOException e) {
            throw new MojoExecutionException("Critical error downloading Tailwind binary from " + url, e);
        }
    }

    private String detectOsBinaryName() throws MojoExecutionException {
        String os = System.getProperty("os.name").toLowerCase();
        String arch = System.getProperty("os.arch").toLowerCase();
        StringBuilder name = new StringBuilder("tailwindcss-");

        if (os.contains("win")) name.append("windows-");
        else if (os.contains("mac")) name.append("macos-");
        else if (os.contains("nux") || os.contains("nix") || os.contains("aix")) name.append("linux-");
        else throw new MojoExecutionException("Unsupported Operating System: " + os);

        if (arch.contains("amd64") || arch.contains("x86_64")) name.append("x64");
        else if (arch.contains("arm64") || arch.contains("aarch64")) name.append("arm64");
        else if (arch.contains("arm")) name.append("armv7");
        else throw new MojoExecutionException("Unsupported Architecture: " + arch);

        if (os.contains("win")) name.append(".exe");
        return name.toString();
    }
}