package com.example;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.List;


@Mojo(name = "custom-clean", defaultPhase = LifecyclePhase.CLEAN)
public class CustomCleanMojo extends AbstractMojo {

    @Parameter(property = "directoriesToClean", defaultValue = "")
    private String directoriesToClean;

    public void execute() throws MojoExecutionException {
        if (directoriesToClean.trim().isEmpty()) {
            getLog().info("No directories to clean");
        } else {
            deleteDirectories(directoriesToClean.split(","));
        }
    }

    private void deleteDirectories(final String[] directoriesToClean) {
        for (String directory : directoriesToClean) {
            File file = new File(directory);

            if (file.exists()) {
                deleteDirectory(file, List.of(directoriesToClean));
                getLog().info("Deleted " + directory);
            } else {
                getLog().info("Directory " + directory + " does not exist");
            }
        }
    }

    private void deleteDirectory(final File directory, List<String> directoriesToClean) {
        final var files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file, directoriesToClean);
                } else {
                    file.delete();
                }
            }
        }

        if (!directoriesToClean.contains(directory.getName())) {
            directory.delete();
        }
    }
}
