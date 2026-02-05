package com.deey;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

@Mojo(name = "compile", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class TailwindCompileMojo extends AbstractMojo {

    @Parameter(property = "tailwind.inputFile", defaultValue = "${project.basedir}/src/main/resources/input.css", required = true)
    private File inputFile;

    @Parameter(property = "tailwind.outputFile", defaultValue = "${project.build.outputDirectory}/static/tailwind.css", required = true)
    private File outputFile;

    @Parameter(property = "tailwind.downloadDirectory", defaultValue = "${project.build.directory}/tailwind-bin", required = true)
    private File downloadDirectory;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Starting tailwindcss compilation...");

        if (!inputFile.exists()) {
            getLog().error("Tailwind input file not found: " + inputFile.getAbsolutePath());
            throw new MojoFailureException("Tailwind input file doesn't exist: " + inputFile.getAbsolutePath());
        }

        String binaryName = TailwindBinaryResolver.detectOsBinaryName();
        File binaryFile = new File(downloadDirectory, binaryName);
        if (!binaryFile.exists()) {
            getLog().error("Tailwind binary not found. Please run the download goal first: " + binaryFile.getAbsolutePath());
            throw new MojoFailureException("Tailwind binary not found. Please run the download goal first: " + binaryFile.getAbsolutePath());
        }
        runTailwind(binaryFile);
    }


    private void runTailwind(File binary) throws MojoExecutionException {
        File parentDir = outputFile.getParentFile();
        if (parentDir != null && !parentDir.exists() && !parentDir.mkdirs()) {
            getLog().error("Failed to create output directory: " + parentDir.getAbsolutePath());
            throw new MojoExecutionException("Failed to create output directory: " + parentDir.getAbsolutePath());
        }

        try {
            ProcessBuilder pb = new ProcessBuilder(
                    binary.getAbsolutePath(),
                    "-i", inputFile.getAbsolutePath(),
                    "-o", outputFile.getAbsolutePath(),
                    "--minify"
            );

            pb.redirectErrorStream(true);
            Process process = pb.start();

            // Stream process output
            try (var reader = new BufferedReader(new java.io.InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    getLog().info("[Tailwind] " + line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new MojoExecutionException("Tailwind exited with error code: " + exitCode);
            }

            getLog().info("CSS compiled successfully to: " + outputFile.getAbsolutePath());

        } catch (IOException | InterruptedException e) {
            getLog().error("Error running Tailwind process", e);
            throw new MojoExecutionException("Error running Tailwind process", e);
        }
    }
}
