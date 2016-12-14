package com.dtforce.resman.plugin.latte;

import com.dtforce.resman.plugin.util.PropertyReference;
import com.dtforce.resman.plugin.util.ResManUtil;
import com.dtforce.resman.plugin.parser.ResManProperty;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.jantvrdik.intellij.latte.psi.LatteMacroContent;
import com.jantvrdik.intellij.latte.psi.LatteTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResManLatteUtil {

    private static class MatchingResult {
        private static final String CLASS_CONSTANT_REFERENCE_REGEX =
                "(?<className>[a-zA-Z]+)::(?<constant>[a-zA-Z_]+)(?<rest>,.*)?";

        private String constant;
        private String className;
        private String rest;

        private MatchingResult() {}

        @Nullable
        static MatchingResult fromString(String latteClassContantRef)
        {
            Pattern pattern = Pattern.compile(CLASS_CONSTANT_REFERENCE_REGEX);
            Matcher matcher = pattern.matcher(latteClassContantRef);
            if ( ! matcher.matches()) {
                return null;
            }
            MatchingResult matchingResult = new MatchingResult();
            matchingResult.className = matcher.group("className");
            matchingResult.constant = matcher.group("constant");
            matchingResult.rest = matcher.group("rest");
            return matchingResult;
        }

        String getConstant() {
            return constant;
        }

        String getClassName() {
            return className;
        }

        String getRest() {
            return rest;
        }
    }

    public static PropertyReference extractPropertyReference(LatteMacroContent macroContent)
    {
        ASTNode firstArg = macroContent.getNode().findChildByType(LatteTypes.T_MACRO_ARGS);
        if (firstArg == null) {
            return null;
        }

        MatchingResult matchingResult = MatchingResult.fromString(firstArg.getText());
        if (matchingResult == null) {
            return null;
        }
        return new PropertyReference(
                matchingResult.getClassName(),
                matchingResult.getConstant()
        );
    }

    @NotNull
    public static List<ResManProperty> findProperties(LatteMacroContent macroContent)
    {
        PropertyReference propertyReference = extractPropertyReference(macroContent);
        if (propertyReference == null) {
            return Collections.emptyList();
        }
        return ResManUtil.findProperties(
                macroContent.getProject(),
                propertyReference
        );
    }

    @NotNull
    public static TextRange getFoldingRange(LatteMacroContent macroContent)
    {
        ASTNode firstArg = macroContent.getNode().findChildByType(LatteTypes.T_MACRO_ARGS);
        if (firstArg == null) {
            throw new RuntimeException("This function can only be called if findProperties returned non empty list.");
        }

        MatchingResult matchingResult = MatchingResult.fromString(firstArg.getText());
        if (matchingResult == null) {
            throw new RuntimeException("This function can only be called if findProperties returned non empty list.");
        }

        if (matchingResult.getRest() == null) {
            return new TextRange(firstArg.getTextRange().getStartOffset(), firstArg.getTextRange().getEndOffset());
        } else {
            return new TextRange(
                    firstArg.getTextRange().getStartOffset(),
                    firstArg.getTextRange().getEndOffset() - matchingResult.getRest().length()
            );
        }
    }

}
