package com.dtforce.resman.plugin.util;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ResManUtil {

    public static List<VirtualFile> findFilesForClass(Project project, String className)
    {
        Collection<VirtualFile> virtualFiles =
                FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, ResManFileType.INSTANCE,
                        GlobalSearchScope.allScope(project));
        List<VirtualFile> virtualFilesFiltered = new ArrayList<>();
        String fileName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, className);
        for (VirtualFile virtualFile : virtualFiles) {
            if (!virtualFile.getNameWithoutExtension().equals(fileName)) {
                continue;
            }
            virtualFilesFiltered.add(virtualFile);
        }
        return virtualFilesFiltered;
    }


    public static List<ResManProperty> findProperties(Project project, PropertyReference propertyReference)
    {
        List<ResManProperty> result = null;
        Collection<VirtualFile> virtualFiles = findFilesForClass(project, propertyReference.getClassName());
        for (VirtualFile virtualFile : virtualFiles) {
            ResManFile simpleFile = (ResManFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (simpleFile != null) {
                ResManProperty[] properties = simpleFile.getProperties();
                if (properties != null) {
                    for (ResManProperty property : properties) {
                        if (propertyReference.getKey().equals(property.getKey())) {
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
        Collection<VirtualFile> virtualFiles = findFilesForClass(project, className);
        for (VirtualFile virtualFile : virtualFiles) {
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
