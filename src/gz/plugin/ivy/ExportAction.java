package gz.plugin.ivy;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

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
import org.jdom.JDOMException;

/**
 * @author <a href="mailto:zgong@naesasoft.com">zgong</a>
 * @version $Id: MailGroup.java,v 1.2 2005/04/26 03:33:23 zgong Exp $
 */
public class ExportAction extends AnAction
{
    public void actionPerformed(AnActionEvent event)
    {

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
        String distPath = Setting.getSetting().getDistPath();
        String artifactPattern = Setting.getSetting().getArtifactPattern();
        String fullIvyURL = distPath + artifactPattern;
        if (filename.equals("ivy.xml")) {
            String[] libs = new String[0];
            try {
                libs = IvyUtil.getLibsFromIvy(virtualFile.getPath(), fullIvyURL,false);
                Log.log(Arrays.asList(libs));
            }
            catch (JDOMException e) {
                IvyUtil.error(e);
            }
            catch (IOException e) {
                IvyUtil.error(e);
            }

            try {
                IvyUtil.exportLibToDirectory(libs, dir);
                Messages.showMessageDialog(module.getProject(), "export libs successfully",
                                           "Information", Messages.getInformationIcon());
            }
            catch (IOException e) {
                IvyUtil.error(e);
            }
        }
        else {
            Messages.showMessageDialog(module.getProject(), "Only ivy.xml will be accepted",
                                       "Wanning", Messages.getWarningIcon());
        }
    }

    public File selectFile(Module module)
    {
        FileChooserDescriptor fileDescriptor = FileChooserDescriptorFactory
            .createSingleFolderDescriptor();
        fileDescriptor.setTitle("Select lib path");
        fileDescriptor.setDescription("Select external graphics editor");
        VirtualFile[] virtualFiles = FileChooser.chooseFiles(module.getProject(), fileDescriptor);
        if (virtualFiles.length == 1 && virtualFiles[0].isDirectory()) {
            return new File(virtualFiles[0].getPath());
        }
        else {
            return null;
        }
    }
}
