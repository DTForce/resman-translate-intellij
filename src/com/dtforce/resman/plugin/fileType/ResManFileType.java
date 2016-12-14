package com.dtforce.resman.plugin.fileType;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.*;

import javax.swing.*;

public class ResManFileType extends LanguageFileType {
    public static final ResManFileType INSTANCE = new ResManFileType();

    private ResManFileType() {
        super(ResManLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "ResMan file";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "ResMan language file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "res";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return AllIcons.FileTypes.Properties;
    }

}