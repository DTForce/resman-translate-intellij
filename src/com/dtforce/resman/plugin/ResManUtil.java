package com.dtforce.resman.plugin;

import com.dtforce.resman.plugin.fileType.ResManFileType;
import com.dtforce.resman.plugin.parser.ResManFile;
import com.dtforce.resman.plugin.parser.ResManProperty;
import com.google.common.base.CaseFormat;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.indexing.FileBasedIndex;
import com.jetbrains.php.lang.psi.elements.ClassConstantReference;
import com.jetbrains.php.lang.psi.elements.ClassReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ResManUtil {

    public static List<ResManProperty> findProperties(ClassConstantReference classConstantReference)
    {
        ClassReference classReference = (ClassReference) classConstantReference.getClassReference();

        Project project = classConstantReference.getProject();
        return ResManUtil.findProperties(project, classReference.getName(), classConstantReference.getName());
    }

    public static List<ResManProperty> findProperties(Project project, String className, String key)
    {
        List<ResManProperty> result = null;
        Collection<VirtualFile> virtualFiles =
                FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, ResManFileType.INSTANCE,
                        GlobalSearchScope.allScope(project));
        String fileName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, className);
        for (VirtualFile virtualFile : virtualFiles) {
            if ( ! virtualFile.getNameWithoutExtension().equals(fileName)) {
                continue;
            }
            ResManFile simpleFile = (ResManFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (simpleFile != null) {
                ResManProperty[] properties = PsiTreeUtil.getChildrenOfType(simpleFile, ResManProperty.class);
                if (properties != null) {
                    for (ResManProperty property : properties) {
                        if (key.equals(property.getKey())) {
                            if (result == null) {
                                result = new ArrayList<ResManProperty>();
                            }
                            result.add(property);
                        }
                    }
                }
            }
        }
        return result != null ? result : Collections.<ResManProperty>emptyList();
    }

    public static List<ResManProperty> findProperties(Project project, String className)
    {
        List<ResManProperty> result = new ArrayList<ResManProperty>();
        Collection<VirtualFile> virtualFiles =
                FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, ResManFileType.INSTANCE,
                        GlobalSearchScope.allScope(project));
        String fileName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, className);
        for (VirtualFile virtualFile : virtualFiles) {
            if ( ! virtualFile.getName().equals(fileName)) {
                continue;
            }
            ResManFile simpleFile = (ResManFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (simpleFile != null) {
                ResManProperty[] properties = PsiTreeUtil.getChildrenOfType(simpleFile, ResManProperty.class);
                if (properties != null) {
                    Collections.addAll(result, properties);
                }
            }
        }
        return result;
    }

}
