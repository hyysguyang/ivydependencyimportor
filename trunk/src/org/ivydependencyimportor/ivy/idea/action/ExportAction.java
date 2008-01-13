package org.ivydependencyimportor.ivy.idea.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.ivydependencyimportor.ivy.IvyConfig;
import org.ivydependencyimportor.ivy.IvyFacade;
import org.ivydependencyimportor.ivy.Log;
import org.ivydependencyimportor.ivy.idea.IdeaUtil;
import org.ivydependencyimportor.ivy.idea.setting.SettingHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:gzkaneg@gmail.com">gzkaneg</a>
 */
public class ExportAction extends AnAction {
    public void actionPerformed(AnActionEvent event) {

        DataContext dataContext = event.getDataContext();
        Module module = (Module) dataContext.getData(DataConstants.MODULE);
        if (module == null) {
            Messages.showMessageDialog("The module is null!", "Error", Messages.getErrorIcon());
            return;
        }
        VirtualFile virtualFile = (VirtualFile) dataContext.getData(DataConstants.VIRTUAL_FILE);
        if (virtualFile == null) {
            Messages.showMessageDialog("The file is null!", "Error", Messages.getErrorIcon());
            return;
        }
        File dir = selectFile(module);
        if (dir == null) {
            Messages.showMessageDialog("No file was selected!",
                    "Error", Messages.getErrorIcon());
            return;
        }
        Log.log("Selected file:" + dir.getAbsolutePath());
        String filename = virtualFile.getName();
        if (filename.equals("ivy.xml")) {
            List<String> libs = new ArrayList<String>();
            try {
                libs = getLibs(virtualFile.getPath());
                IdeaUtil.importLibToModuleLib(libs, module);
                Log.log(Arrays.asList(libs));
            } catch (Exception e) {
                IdeaUtil.error(e);
            }

            try {
                IdeaUtil.exportLibToDirectory(libs, dir);
                Messages.showMessageDialog(module.getProject(), "export libs successfully",
                        "Information", Messages.getInformationIcon());
            }
            catch (IOException e) {
                IdeaUtil.error(e);
            }
        } else {
            Messages.showMessageDialog(module.getProject(), "Only ivy.xml will be accepted",
                    "Wanning", Messages.getWarningIcon());
        }
    }

    public void update(AnActionEvent e) {
        VirtualFile virtualFile = (VirtualFile) e.getDataContext().getData(DataConstants.VIRTUAL_FILE);
        if (virtualFile == null) {
            e.getPresentation().setEnabled(false);
            return;
        }
        String filename = virtualFile.getName();
        e.getPresentation().setEnabled("ivy.xml".equals(filename));
    }

    public File selectFile(Module module) {
        FileChooserDescriptor fileDescriptor = FileChooserDescriptorFactory
                .createSingleFolderDescriptor();
        fileDescriptor.setTitle("Select lib path");
        fileDescriptor.setDescription("Select external graphics editor");
        VirtualFile[] virtualFiles = FileChooser.chooseFiles(module.getProject(), fileDescriptor);
        if (virtualFiles.length == 1 && virtualFiles[0].isDirectory()) {
            return new File(virtualFiles[0].getPath());
        } else {
            return null;
        }
    }

    private IvyFacade getIvyFacade(IvyConfig setting)
            throws Exception {
        IvyFacade ivyFacade = new IvyFacade();
        ivyFacade.setIvyConfig(setting);
        ivyFacade.resolve();
        return ivyFacade;
    }

    private IvyConfig getIvyConfig() {
        IvyConfig setting = new IvyConfig();
//        setting.setCacheDir(null);
        setting.setRepositoryDir(SettingHelper.getInstance().getSetting().getDistPath());
//        setting.setIvySettingFile(null);
        setting.setTransitive(true);
        return setting;
    }

    private List<String> getLibs(String ivyFile) throws Exception {
        IvyConfig ivyConfig = getIvyConfig();
        ivyConfig.setIvyFile(ivyFile);
        return getIvyFacade(ivyConfig).getJarArtifactsFilePath();
    }
}
