package com.dtforce.resman.plugin.folding;

import com.dtforce.resman.plugin.parser.ResManProperty;
import com.dtforce.resman.plugin.php.ResManPhpUtil;
import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.FoldingGroup;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.psi.elements.ClassConstantReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ResManFoldingBuilder extends FoldingBuilderEx {
    @NotNull
    @Override
    public FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
        FoldingGroup group = FoldingGroup.newGroup("ResMan");

        List<FoldingDescriptor> descriptors = new ArrayList<FoldingDescriptor>();
        Collection<ClassConstantReference> classConstantReferences =
                PsiTreeUtil.findChildrenOfType(root, ClassConstantReference.class);
        for (final ClassConstantReference classConstantReference : classConstantReferences) {
            final List<ResManProperty> properties = ResManPhpUtil.findProperties(classConstantReference);
            if (properties.size() == 1) {
                descriptors.add(new FoldingDescriptor(classConstantReference.getNode(),
                        new TextRange(classConstantReference.getTextRange().getStartOffset(),
                                classConstantReference.getTextRange().getEndOffset()),
                        group) {
                    @Nullable
                    @Override
                    public String getPlaceholderText() {
                        // IMPORTANT: keys can come with no values, so a test for null is needed
                        String valueOf = properties.get(0).getValue();
                        return valueOf == null ? "" : valueOf;
                    }
                });
            }
        }
        return descriptors.toArray(new FoldingDescriptor[descriptors.size()]);
    }

    @Nullable
    @Override
    public String getPlaceholderText(@NotNull ASTNode node) {
        return "...";
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        return true;
    }
}
