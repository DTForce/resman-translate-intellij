package com.dtforce.resman.plugin.annotation;

import com.dtforce.resman.plugin.ResManUtil;
import com.dtforce.resman.plugin.parser.ResManProperty;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.ClassConstantReference;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class ResManLineMarker extends RelatedItemLineMarkerProvider {
    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element,
                                            Collection<? super RelatedItemLineMarkerInfo> result) {
        if (element instanceof ClassConstantReference) {
            ClassConstantReference classConstantReference = (ClassConstantReference) element;
            final List<ResManProperty> properties = ResManUtil.findProperties(classConstantReference);
            if (properties.size() > 0) {
                NavigationGutterIconBuilder<PsiElement> builder =
                        NavigationGutterIconBuilder.create(AllIcons.FileTypes.Properties).
                                setTargets(properties).
                                setTooltipText("Navigate to resource");
                result.add(builder.createLineMarkerInfo(element));
            }
        }
    }
}