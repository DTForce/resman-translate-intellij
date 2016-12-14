package com.dtforce.resman.plugin.lexer;

import com.intellij.lexer.FlexAdapter;

import java.io.Reader;

public class ResManLexerAdapter extends FlexAdapter {
    public ResManLexerAdapter() {
        super(new ResManLexer((Reader) null));
    }
}
