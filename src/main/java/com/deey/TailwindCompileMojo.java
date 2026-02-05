package com.deey;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Mojo(name = "compile", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class TailwindCompileMojo extends AbstractTailwindMojo {

    @Parameter(property = "tailwind.inputFile", defaultValue = "${project.basedir}/src/main/resources/input.css", required = true)
    private File inputFile;

    @Parameter(property = "tailwind.outputFile", defaultValue = "${project.build.outputDirectory}/static/tailwind.css", required = true)
    private File outputFile;

    @Parameter(property = "tailwind.minify", defaultValue = "true")
    private boolean minify;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Starting Tailwind CSS compilation...");

        if (!inputFile.exists()) {
            throw new MojoFailureException("Tailwind input file not found: " + inputFile.getAbsolutePath());
        }

        File binary = getBinaryManager().resolveBinary(forceDownload);

        runTailwind(binary);
    }

    private void runTailwind(File binary) throws MojoExecutionException {
        File parentDir = outputFile.getParentFile();
        if (parentDir != null && !parentDir.exists() && !parentDir.mkdirs()) {
            throw new MojoExecutionException("Failed to create output directory: " + parentDir.getAbsolutePath());
        }

        List<String> command = new ArrayList<>();
        command.add(binary.getAbsolutePath());
        command.add("-i");
        command.add(inputFile.getAbsolutePath());
        command.add("-o");
        command.add(outputFile.getAbsolutePath());
        if (minify) command.add("--minify");

        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    getLog().info("[Tailwind] " + line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new MojoExecutionException("Tailwind process failed with exit code: " + exitCode);
            }

            getLog().info("CSS compiled successfully to: " + outputFile.getAbsolutePath());
        } catch (Exception e) {
            throw new MojoExecutionException("Error executing Tailwind process", e);
        }
    }
}