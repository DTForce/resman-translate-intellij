package com.dtforce.resman.plugin.parser;

import com.dtforce.resman.plugin.ResManIcons;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ResManPsiImplUtil {
    public static String getKey(ResManProperty element) {
        ASTNode keyNode = element.getNode().findChildByType(ResManTypes.KEY);
        if (keyNode != null) {
            return keyNode.getText();
        } else {
            return null;
        }
    }

    public static String getValue(ResManProperty element) {
        ASTNode[] valueNodes = element.getNode().getChildren(TokenSet.create(ResManTypes.VALUE, ResManTypes.PARAM));
        if (valueNodes.length == 0) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (ASTNode node : valueNodes) {
            stringBuilder.append(node.getText());
        }
        return stringBuilder.toString();
    }

    public static String getName(ResManProperty element) {
        return getKey(element);
    }

    public static PsiElement setName(ResManProperty element, String newName) {
        ASTNode keyNode = element.getNode().findChildByType(ResManTypes.KEY);
        if (keyNode != null) {
            ResManProperty property = ResManElementFactory.createProperty(element.getProject(), newName);
            ASTNode newKeyNode = property.getFirstChild().getNode();
            element.getNode().replaceChild(keyNode, newKeyNode);
        }
        return element;
    }

    public static PsiElement getNameIdentifier(ResManProperty element) {
        ASTNode keyNode = element.getNode().findChildByType(ResManTypes.KEY);
        if (keyNode != null) {
            return keyNode.getPsi();
        } else {
            return null;
        }
    }

    public static ItemPresentation getPresentation(final ResManProperty element) {
        return new ItemPresentation() {
            @Nullable
            @Override
            public String getPresentableText() {
                return element.getKey();
            }

            @Nullable
            @Override
            public String getLocationString() {
                PsiFile containingFile = element.getContainingFile();
                return containingFile == null ? null : containingFile.getName();
            }

            @Nullable
            @Override
            public Icon getIcon(boolean unused) {
                return ResManIcons.FILE;
            }
        };
    }
}
