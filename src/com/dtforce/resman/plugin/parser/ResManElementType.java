package com.dtforce.resman.plugin.parser;

import com.dtforce.resman.plugin.fileType.ResManLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class ResManElementType extends IElementType {
    public ResManElementType(@NotNull @NonNls String debugName) {
        super(debugName, ResManLanguage.INSTANCE);
    }
}
