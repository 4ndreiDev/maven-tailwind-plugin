package com.deey;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Mojo(name = "compile", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class TailwindCompileMojo extends AbstractMojo {

    @Parameter(property = "tailwind.version", defaultValue = "v4.1.18", required = true)
    private String tailwindVersion;

    @Parameter(property = "tailwind.inputFile", defaultValue = "${project.basedir}/src/main/resources/input.css", required = true)
    private File inputFile;

    @Parameter(property = "tailwind.outputFile", defaultValue = "${project.build.outputDirectory}/static/tailwind.css", required = true)
    private File outputFile;

    @Parameter(property = "tailwind.downloadDirectory", defaultValue = "${project.build.directory}/tailwind-bin", required = true)
    private File downloadDirectory;

    @Parameter(property = "tailwind.baseUrl", defaultValue = "https://github.com/tailwindlabs/tailwindcss/releases/download", readonly = true)
    private String baseUrl;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Iniciando compilación Tailwind CSS v4...");

        // 1. Validar entrada
        if (!inputFile.exists()) {
            getLog().error("Archivo de entrada no encontrado: " + inputFile.getAbsolutePath());
            throw new MojoFailureException("El archivo de entrada no existe: " + inputFile.getAbsolutePath());
        }

        // 2. Determinar binario según SO
        String binaryName = detectOsBinaryName();
        String downloadUrl = String.format("%s/%s/%s", baseUrl, tailwindVersion, binaryName);
        File binaryFile = new File(downloadDirectory, binaryName);

        // 3. Descargar binario si no existe
        try {
            downloadBinary(downloadUrl, binaryFile);
        } catch (IOException e) {
            throw new MojoExecutionException("Fallo al descargar Tailwind binary", e);
        }

        // 4. Ejecutar Tailwind
        runTailwind(binaryFile);
    }

    private String detectOsBinaryName() throws MojoExecutionException {
        String os = System.getProperty("os.name").toLowerCase();
        String arch = System.getProperty("os.arch").toLowerCase();

        String binaryName = "tailwindcss-";

        if (os.contains("win")) {
            binaryName += "windows-";
        } else if (os.contains("mac")) {
            binaryName += "macos-";
        } else if (os.contains("nux") || os.contains("nix") || os.contains("aix")) {
            binaryName += "linux-";
        } else {
            throw new MojoExecutionException("Sistema operativo no soportado: " + os);
        }

        if (arch.contains("amd64") || arch.contains("x86_64")) {
            binaryName += "x64";
        } else if (arch.contains("arm64") || arch.contains("aarch64")) {
            binaryName += "arm64";
        } else if (arch.contains("arm")) {
            binaryName += "armv7";
        } else {
            throw new MojoExecutionException("Arquitectura no soportada: " + arch);
        }

        if (os.contains("win")) {
            binaryName += ".exe";
        }

        return binaryName;
    }

    private void downloadBinary(String url, File targetFile) throws IOException {
        if (targetFile.exists()) {
            getLog().info("Binario encontrado en cache: " + targetFile.getAbsolutePath());
            return;
        }

        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }

        getLog().info("Descargando Tailwind CSS desde: " + url);

        URL downloadSource = new URL(url);
        try (InputStream in = downloadSource.openStream()) {
            Files.copy(in, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        if (!targetFile.setExecutable(true)) {
            getLog().warn("No se pudo establecer permisos de ejecución en el binario.");
        }

        getLog().info("Descarga completada.");
    }

    private void runTailwind(File binary) throws MojoExecutionException {
        // Asegurar que el directorio de salida existe
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
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

            // Leer logs del proceso
            try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    getLog().info("[Tailwind] " + line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new MojoExecutionException("Tailwind terminó con código de error: " + exitCode);
            }

            getLog().info("CSS compilado exitosamente en: " + outputFile.getAbsolutePath());

        } catch (IOException | InterruptedException e) {
            throw new MojoExecutionException("Error ejecutando proceso Tailwind", e);
        }
    }
}