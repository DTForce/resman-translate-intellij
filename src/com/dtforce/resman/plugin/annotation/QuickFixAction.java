package com.dtforce.resman.plugin.annotation;


import com.dtforce.resman.plugin.util.PropertyReference;
import com.dtforce.resman.plugin.util.ResManUtil;
import com.dtforce.resman.plugin.fileType.ResManFileType;
import com.dtforce.resman.plugin.parser.ResManElementFactory;
import com.dtforce.resman.plugin.parser.ResManFile;
import com.dtforce.resman.plugin.parser.ResManProperty;
import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

class QuickFixAction extends BaseIntentionAction {
    private PropertyReference propertyReference;

    public QuickFixAction(PropertyReference propertyReference) {
        this.propertyReference = propertyReference;
    }

    @NotNull
    @Override
    public String getText() {
        return "Create property";
    }

    @NotNull
    @Override
    public String getFamilyName() {
        return "ResMan properties";
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
        return true;
    }

    @Override
    public void invoke(@NotNull final Project project, final Editor editor, PsiFile file) throws
            IncorrectOperationException {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                Collection<VirtualFile> virtualFiles = ResManUtil.findFilesForClass(project, propertyReference.getClassName());
                if (virtualFiles.size() == 1) {
                    createProperty(project, virtualFiles.iterator().next());
                } else {
                    final FileChooserDescriptor descriptor =
                            FileChooserDescriptorFactory.createSingleFileDescriptor(ResManFileType.INSTANCE);
                    descriptor.setRoots(project.getBaseDir());
                    final VirtualFile file = FileChooser.chooseFile(descriptor, project, null);
                    if (file != null) {
                        createProperty(project, file);
                    }
                }
            }
        });
    }

    private void createProperty(final Project project, final VirtualFile file) {
        new WriteCommandAction.Simple(project) {
            @Override
            public void run() {
                ResManFile simpleFile = (ResManFile) PsiManager.getInstance(project).findFile(file);
                ASTNode lastChildNode = simpleFile.getNode().getLastChildNode();
                // TODO: Add another check for CRLF
                if (lastChildNode != null/* && !lastChildNode.getElementType().equals(SimpleTypes.CRLF)*/) {
                    simpleFile.getNode().addChild(ResManElementFactory.createCRLF(project).getNode());
                }
                // IMPORTANT: change spaces to escaped spaces or the new node will only have the first word for the key
                ResManProperty property = ResManElementFactory.createProperty(project, propertyReference.getKey(), "");
                simpleFile.getNode().addChild(property.getNode());
                ((Navigatable) property.getLastChild().getNavigationElement()).navigate(true);
                FileEditorManager.getInstance(project).getSelectedTextEditor()
                        .getCaretModel()
                        .moveCaretRelatively(2, 0, false, false, false);
            }
        }.execute();
    }

}
