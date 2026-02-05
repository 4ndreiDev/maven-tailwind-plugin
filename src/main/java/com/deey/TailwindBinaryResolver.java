package com.deey;

import org.apache.maven.plugin.MojoExecutionException;

final class TailwindBinaryResolver {
    private TailwindBinaryResolver() {
    }

    static String detectOsBinaryName() throws MojoExecutionException {
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
}
