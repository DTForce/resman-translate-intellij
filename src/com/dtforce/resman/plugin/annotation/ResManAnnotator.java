package com.dtforce.resman.plugin.annotation;


import com.dtforce.resman.plugin.util.PropertyReference;
import com.dtforce.resman.plugin.util.ResManUtil;
import com.dtforce.resman.plugin.parser.ResManProperty;
import com.dtforce.resman.plugin.php.ResManPhpUtil;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.ClassConstantReference;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class ResManAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof ClassConstantReference) {
            ClassConstantReference constantReference = (ClassConstantReference) element;
            final List<ResManProperty> properties = ResManPhpUtil.findProperties(constantReference);
            if (properties.size() == 1) {
                TextRange range = new TextRange(element.getTextRange().getStartOffset(),
                        element.getTextRange().getEndOffset());
                Annotation annotation = holder.createInfoAnnotation(range, null);
                annotation.setTextAttributes(DefaultLanguageHighlighterColors.CONSTANT);
            } else if (properties.size() == 0) {
                PropertyReference propertyReference = ResManPhpUtil.extractPropertyReference(constantReference);
                Collection<VirtualFile> virtualFiles = ResManUtil.findFilesForClass(constantReference.getProject(), propertyReference.getClassName());
                if (virtualFiles.size() > 0) {
                    TextRange range = new TextRange(element.getTextRange().getStartOffset(),
                            element.getTextRange().getEndOffset());
                    holder.createWeakWarningAnnotation(range, "Unresolved property")
                            .registerFix(new QuickFixAction(propertyReference));
                }
            }
        }
    }
}