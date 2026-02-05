package com.deey;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Mojo(name = "download", defaultPhase = LifecyclePhase.INITIALIZE)
public class TailwindDownloadMojo extends AbstractMojo {

    @Parameter(property = "tailwind.version", defaultValue = "v4.1.18", required = true)
    private String tailwindVersion;

    @Parameter(property = "tailwind.downloadDirectory", defaultValue = "${project.build.directory}/tailwind-bin", required = true)
    private File downloadDirectory;

    @Parameter(property = "tailwind.baseUrl", defaultValue = "https://github.com/tailwindlabs/tailwindcss/releases/download", readonly = true)
    private String baseUrl;

    @Parameter(property = "tailwind.forceDownload", defaultValue = "false")
    private boolean forceDownload;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Preparing Tailwind CSS download...");

        String binaryName = TailwindBinaryResolver.detectOsBinaryName();
        String downloadUrl = String.format("%s/%s/%s", baseUrl, tailwindVersion, binaryName);
        File binaryFile = new File(downloadDirectory, binaryName);

        try {
            downloadBinary(downloadUrl, binaryFile, forceDownload);
        } catch (IOException e) {
            getLog().error("Failed to download Tailwind binary: " + e.getMessage());
            throw new MojoExecutionException("Failed to download Tailwind binary", e);
        }
    }

    private void downloadBinary(String url, File targetFile, boolean force) throws IOException {
        if (targetFile.exists() && !force) {
            getLog().info("Binary found in cache: " + targetFile.getAbsolutePath());
            return;
        }

        if (targetFile.exists() && force) {
            getLog().info("Forcing re-download of binary: " + targetFile.getAbsolutePath());
        }

        File parent = targetFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        getLog().info("Downloading Tailwind CSS from: " + url);

        URL downloadSource = new URL(url);
        try (InputStream in = downloadSource.openStream()) {
            Files.copy(in, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        if (!targetFile.setExecutable(true)) {
            getLog().warn("Could not set executable permissions on the binary.");
        }

        getLog().info("Download completed.");
    }
}
