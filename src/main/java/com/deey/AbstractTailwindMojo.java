package com.deey;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import java.io.File;

public abstract class AbstractTailwindMojo extends AbstractMojo {

    @Parameter(property = "tailwind.version", defaultValue = "v4.1.18")
    protected String tailwindVersion;

    @Parameter(property = "tailwind.downloadDirectory", defaultValue = "${user.home}/.m2/tailwindcss")
    protected File downloadDirectory;

    @Parameter(property = "tailwind.baseUrl", defaultValue = "https://github.com/tailwindlabs/tailwindcss/releases/download")
    protected String baseUrl;

    @Parameter(property = "tailwind.forceDownload", defaultValue = "false")
    protected boolean forceDownload;

    /**
     * Helper to get a pre-configured binary manager.
     */
    protected TailwindBinaryManager getBinaryManager() {
        return new TailwindBinaryManager(tailwindVersion, downloadDirectory, baseUrl, getLog());
    }
}