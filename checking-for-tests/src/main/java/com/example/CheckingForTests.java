package com.example;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FileNotFoundException;

@Mojo(name = "checking", defaultPhase = LifecyclePhase.VALIDATE)
public class CheckingForTests extends AbstractMojo {

    @Parameter(property = "checkingForTests", defaultValue = "")
    private String checkingForTests;

    @Parameter(property = "subModulePath", defaultValue = "rental-store")
    private String subModulePath;

    public void execute() throws MojoExecutionException {
        if (checkingForTests.isEmpty()) {
            throw new MojoExecutionException("Файл не указан");
        }

        String[] listCheckingForTests = checkingForTests.split(",");
        for (String testFile : listCheckingForTests) {
            testFile = testFile.trim();
            File file = new File(subModulePath + "/src/test/java", testFile.replace('.', '/') + ".java");

            if (!file.exists()) {
                throw new MojoExecutionException("Файл не существует", new FileNotFoundException("Файл не найден: "
                        + file.getAbsolutePath()));
            } else {
                getLog().info("Файл найден: " + file.getAbsolutePath());
            }
        }
    }
}
