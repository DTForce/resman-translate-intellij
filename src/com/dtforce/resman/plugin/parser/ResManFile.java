package com.dtforce.resman.plugin.parser;

import com.dtforce.resman.plugin.fileType.ResManFileType;
import com.dtforce.resman.plugin.fileType.ResManLanguage;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

public class ResManFile extends PsiFileBase {
    public ResManFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, ResManLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return ResManFileType.INSTANCE;
    }

    @NotNull
    @Override
    public String toString() {
        return "ResMan File";
    }

}
