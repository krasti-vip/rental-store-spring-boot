package com.example;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import java.io.File;
import java.io.FileNotFoundException;

@Mojo(name = "required", defaultPhase = LifecyclePhase.VALIDATE)
public class RequiredFiles extends AbstractMojo {

    @Parameter(property = "requiredFilesToCheck", defaultValue = "")
    private String requiredFilesToCheck;

    public void execute() throws MojoExecutionException {
        if (requiredFilesToCheck.isEmpty()) {
            throw new MojoExecutionException("Файл не указан");
        } else {
            String[] listRequredFiles = requiredFilesToCheck.split(",");
            for (String requredFile : listRequredFiles) {
                File file = new File(requredFile);
                if (!file.exists()) {
                    throw new MojoExecutionException("Файл не существует", new FileNotFoundException("Файл не существует "
                            + file.getName()));
                } else {
                    getLog().info("Файл найден " + file.getName());
                }
            }
        }
    }
}
