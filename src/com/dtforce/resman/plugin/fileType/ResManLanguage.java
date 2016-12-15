package com.dtforce.resman.plugin.fileType;

import com.intellij.lang.Language;

public class ResManLanguage extends Language{
    public static final ResManLanguage INSTANCE = new ResManLanguage();

    private ResManLanguage() {
        super("ResMan");
    }
}
