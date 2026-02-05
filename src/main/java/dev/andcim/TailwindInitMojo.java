package dev.andcim;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Maven Mojo to initialize the Tailwind CSS input file.
 * Creates src/main/resources/static/css/input.css with Tailwind CSS imports.
 * Usage: mvn tailwind:init
 */
@Mojo(name = "init", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class TailwindInitMojo extends AbstractTailwindMojo {

    @Parameter(property = "tailwind.inputFile", defaultValue = "${project.basedir}/src/main/resources/static/css/input.css", required = true)
    private File inputFile;

    private static final String TAILWIND_CSS_CONTENT = "@import \"tailwindcss\";\n";

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Initializing Tailwind CSS configuration...");

        Path inputPath = inputFile.toPath();

        // 1. Check if file already exists (Defensive check)
        if (Files.exists(inputPath)) {
            getLog().warn("Tailwind input file already exists: " + inputPath.toAbsolutePath());
            getLog().warn("Skipping initialization to avoid overwriting existing content.");
            return;
        }

        try {
            if (inputPath.getParent() != null) {
                Files.createDirectories(inputPath.getParent());
            }

            Files.writeString(
                    inputPath,
                    TAILWIND_CSS_CONTENT,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

            getLog().info("Tailwind CSS input file created successfully: " + inputPath.toAbsolutePath());

        } catch (IOException e) {
            throw new MojoExecutionException("Failed to initialize Tailwind CSS input file at: " + inputPath, e);
        }
    }
}