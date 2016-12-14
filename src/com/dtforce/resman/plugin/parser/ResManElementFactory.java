package com.dtforce.resman.plugin.parser;

import com.dtforce.resman.plugin.fileType.ResManFileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;

public class ResManElementFactory {

    public static ResManProperty createProperty(Project project, String name, String value) {
        final ResManFile file = createFile(project, name + "," + value);
        return (ResManProperty) file.getFirstChild();
    }

    public static ResManProperty createProperty(Project project, String name) {
        final ResManFile file = createFile(project, name);
        return (ResManProperty) file.getFirstChild();
    }

    public static PsiElement createCRLF(Project project) {
        final ResManFile file = createFile(project, "\n");
        return file.getFirstChild();
    }

    public static ResManFile createFile(Project project, String text) {
        String name = "dummy.res";
        return (ResManFile) PsiFileFactory.getInstance(project).
                createFileFromText(name, ResManFileType.INSTANCE, text);
    }

}