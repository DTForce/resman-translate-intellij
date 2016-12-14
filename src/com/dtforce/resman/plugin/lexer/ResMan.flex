package com.dtforce.resman.plugin.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.dtforce.resman.plugin.parser.ResManTypes;
import com.intellij.psi.TokenType;

%%

%class ResManLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

CRLF=\R
WHITE_SPACE=[\ \n\t\f]
FIRST_VALUE_CHARACTER=[^\n\f\\{}] | "\\"{CRLF} | "\\".
VALUE_CHARACTER=[^\n\f\\{}] | "\\"{CRLF} | "\\".
END_OF_LINE_COMMENT=("#")[^\r\n]*
SEPARATOR=[,]
KEY_CHARACTER=[^,#\r\n] | "\\ "
PARAM=("{{")[^}]*("}}")

%state WAITING_VALUE

%%

<YYINITIAL> {END_OF_LINE_COMMENT}                           { yybegin(YYINITIAL); return ResManTypes.COMMENT; }

<YYINITIAL> {KEY_CHARACTER}+                                { yybegin(YYINITIAL); return ResManTypes.KEY; }

<YYINITIAL> {SEPARATOR}                                     { yybegin(WAITING_VALUE); return ResManTypes.SEPARATOR; }

<WAITING_VALUE> {CRLF}+                                     { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }

<WAITING_VALUE> {FIRST_VALUE_CHARACTER}{VALUE_CHARACTER}*   { yybegin(WAITING_VALUE); return ResManTypes.VALUE; }
<WAITING_VALUE> {PARAM}                                     { yybegin(WAITING_VALUE); return ResManTypes.PARAM; }

({CRLF}|{WHITE_SPACE})+                                     { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }

.                                                           { return TokenType.BAD_CHARACTER; }