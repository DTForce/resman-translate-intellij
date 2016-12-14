package com.dtforce.resman.plugin.annotation;


import com.dtforce.resman.plugin.latte.ResManLatteUtil;
import com.dtforce.resman.plugin.parser.ResManProperty;
import com.dtforce.resman.plugin.util.PropertyReference;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.jantvrdik.intellij.latte.psi.LatteMacroContent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ResManLatteAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof LatteMacroContent) {
            LatteMacroContent latteMacroContent = (LatteMacroContent) element;
            final List<ResManProperty> properties = ResManLatteUtil.findProperties(latteMacroContent);
            if (properties.size() == 1) {
                TextRange range = ResManLatteUtil.getFoldingRange(latteMacroContent);
                Annotation annotation = holder.createInfoAnnotation(range, null);
                annotation.setTextAttributes(DefaultLanguageHighlighterColors.CONSTANT);
            } else if (properties.size() == 0) {
                PropertyReference propertyReference = ResManLatteUtil.extractPropertyReference(latteMacroContent);
                TextRange range = ResManLatteUtil.getFoldingRange(latteMacroContent);
                holder.createWeakWarningAnnotation(range, "Unresolved property")
                        .registerFix(new QuickFixAction(propertyReference));
            }
        }
    }
}