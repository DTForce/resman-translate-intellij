package com.dtforce.resman.plugin.syntax;

import com.dtforce.resman.plugin.ResManIcons;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.*;
import org.jetbrains.annotations.*;

import javax.swing.*;
import java.util.Map;

public class ResManColorSettingsPage implements ColorSettingsPage {
    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
            new AttributesDescriptor("Key", ResManSyntaxHighlighter.KEY),
            new AttributesDescriptor("Separator", ResManSyntaxHighlighter.SEPARATOR),
            new AttributesDescriptor("Value", ResManSyntaxHighlighter.VALUE),
            new AttributesDescriptor("Param", ResManSyntaxHighlighter.PARAM),
    };

    @Nullable
    @Override
    public Icon getIcon() {
        return ResManIcons.FILE;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new ResManSyntaxHighlighter();
    }

    @NotNull
    @Override
    public String getDemoText() {
        return "# You are reading the \".properties\" entry.\n" +
                "website,http://en.wikipedia.org/\n" +
                "# Using parameter\n" +
                "hello,Hello {{name}} how are you?";
    }

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @NotNull
    @Override
    public AttributesDescriptor[] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    @NotNull
    @Override
    public ColorDescriptor[] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "ResMan";
    }

}
