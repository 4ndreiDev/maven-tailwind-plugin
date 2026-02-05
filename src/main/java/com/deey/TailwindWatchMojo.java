package com.deey;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.List;

/**
 * Goal to compile Tailwind CSS in watch mode for development.
 * Usage: mvn tailwind:watch
 */
@Mojo(name = "watch")
public class TailwindWatchMojo extends AbstractTailwindMojo {

    @Parameter(property = "tailwind.inputFile", defaultValue = "${project.basedir}/src/main/resources/static/css/input.css", required = true)
    private File inputFile;

    @Parameter(property = "tailwind.outputFile", defaultValue = "${project.build.outputDirectory}/static/tailwind.css", required = true)
    private File outputFile;

    @Parameter(property = "tailwind.minify", defaultValue = "false")
    private boolean minify;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("========================================");
        getLog().info("Starting Tailwind CSS in WATCH mode...");
        getLog().info("Press Ctrl+C to stop");
        getLog().info("========================================");

        if (!inputFile.exists()) {
            throw new MojoFailureException("Tailwind input file not found: " + inputFile.getAbsolutePath());
        }

        File binary = getBinaryManager().resolveBinary(forceDownload);
        runTailwindWatch(binary);
    }

    private void runTailwindWatch(File binary) throws MojoExecutionException {
        File parentDir = outputFile.getParentFile();
        if (parentDir != null && !parentDir.exists() && !parentDir.mkdirs()) {
            throw new MojoExecutionException("Failed to create output directory: " + parentDir.getAbsolutePath());
        }

        List<String> command = List.of(
                binary.getAbsolutePath(),
                "-i", inputFile.getAbsolutePath(),
                "-o", outputFile.getAbsolutePath(),
                "--watch",
                minify ? "--minify" : ""
        );

        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            pb.inheritIO(); // Inherit parent process I/O to handle Ctrl+C properly
            Process process = pb.start();
            int exitCode = process.waitFor();
            getLog().info("Tailwind watch mode stopped with exit code: " + exitCode);
        } catch (InterruptedException e) {
            getLog().info("Tailwind watch mode interrupted");
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            throw new MojoExecutionException("Error executing Tailwind watch process", e);
        }
    }
}
