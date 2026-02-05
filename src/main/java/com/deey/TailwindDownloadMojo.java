package com.deey;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "download", defaultPhase = LifecyclePhase.INITIALIZE)
public class TailwindDownloadMojo extends AbstractTailwindMojo {
    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("Starting Tailwind binary download process...");
        getBinaryManager().resolveBinary(forceDownload);
    }
}