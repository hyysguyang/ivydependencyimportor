package org.ivydependencyimportor.ivy.idea.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.ivydependencyimportor.ivy.IvyConfig;
import org.ivydependencyimportor.ivy.IvyFacade;
import org.ivydependencyimportor.ivy.Log;
import org.ivydependencyimportor.ivy.idea.IdeaUtil;
import org.ivydependencyimportor.ivy.idea.setting.SettingHelper;
import org.ivydependencyimportor.ivy.idea.setting.Setting;

import java.util.List;

/**
 * @author <a href="mailto:gzkaneg@gmail.com">gzkaneg</a>
 */
public class ImportAction extends AnAction {
    private String DEFAULT_IVY_FILE_NAME = "ivy.xml";

    public void actionPerformed(AnActionEvent e) {
        DataContext dataContext = e.getDataContext();
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
        String fileName = virtualFile.getName();
        String ivyFilePath = virtualFile.getPath();
        if (fileName.equals(DEFAULT_IVY_FILE_NAME)) {
            try {
                List<String> libs = getLibs(ivyFilePath);
                Log.log("import libs:" + libs);
                IdeaUtil.importLibToModuleLib(libs, module);
                Messages.showMessageDialog(module.getProject(), "Import libs successfully",
                        "Information", Messages.getInformationIcon());
            } catch (Exception e1) {
                Log.log(e1);
                Messages.showMessageDialog(module.getProject(), "Import libs failed",
                        "Error", Messages.getInformationIcon());
            }

        } else {
            Messages.showMessageDialog(module.getProject(), "Only ivy.xml will be accepted",
                    "Wanning", Messages.getWarningIcon());
        }
    }

    public void update(AnActionEvent e) {
        DataContext dataContext = e.getDataContext();
        VirtualFile virtualFile = (VirtualFile) dataContext.getData(DataConstants.VIRTUAL_FILE);
        if (virtualFile == null) {
            e.getPresentation().setEnabled(false);
            return;
        }
        String filename = virtualFile.getName();
        e.getPresentation().setEnabled(DEFAULT_IVY_FILE_NAME.equals(filename));
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
        Setting ivyUISetting = SettingHelper.getInstance().getSetting();
        setting.setRepositoryDir(ivyUISetting.getDistPath());
        setting.setArtifactPattern(ivyUISetting.getArtifactPattern());
        setting.setTransitive(ivyUISetting.isTransitive());
//        setting.setCacheDir(null);
//        setting.setIvySettingFile(null);
        return setting;
    }

    private List<String> getLibs(String ivyFile) throws Exception {
        IvyConfig ivyConfig = getIvyConfig();
        ivyConfig.setIvyFile(ivyFile);
        return getIvyFacade(ivyConfig).getJarArtifactsFilePath();
    }
}