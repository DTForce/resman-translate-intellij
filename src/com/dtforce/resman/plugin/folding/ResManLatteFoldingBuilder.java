package com.dtforce.resman.plugin.folding;

import com.dtforce.resman.plugin.latte.ResManLatteUtil;
import com.dtforce.resman.plugin.parser.ResManProperty;
import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.FoldingGroup;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.jantvrdik.intellij.latte.psi.LatteMacroContent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ResManLatteFoldingBuilder extends FoldingBuilderEx {
	@NotNull
	@Override
	public FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
		FoldingGroup group = FoldingGroup.newGroup("ResMan");

		List<FoldingDescriptor> descriptors = new ArrayList<>();
		Collection<LatteMacroContent> latteMacroContents = PsiTreeUtil.findChildrenOfType(root, LatteMacroContent.class);
		for (final LatteMacroContent macroContent : latteMacroContents) {
			final List<ResManProperty> properties = ResManLatteUtil.findProperties(macroContent);
			if (properties.size() == 1) {
				descriptors.add(new FoldingDescriptor(macroContent.getNode(), ResManLatteUtil.getFoldingRange(macroContent), group) {
					@Nullable
					@Override
					public String getPlaceholderText() {
						// keys can come with no values, so a test for null is needed
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
