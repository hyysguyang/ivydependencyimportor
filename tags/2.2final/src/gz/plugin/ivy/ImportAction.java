package gz.plugin.ivy;

import java.io.IOException;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.jdom.JDOMException;

/**
 * @author <a href="mailto:zgong@naesasoft.com">zgong</a>
 * @version $Id: MailGroup.java,v 1.2 2005/04/26 03:33:23 zgong Exp $
 */
public class ImportAction extends AnAction
{
    public void actionPerformed(AnActionEvent e)
    {
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
        String filename = virtualFile.getName();
        String distPath = Setting.getSetting().getDistPath();
        String artifactPattern = Setting.getSetting().getArtifactPattern();
        String fullIvyURL = distPath + artifactPattern;
        if (filename.equals("ivy.xml")) {
            String[] libs = new String[0];
            try {
                libs = IvyUtil.getLibsUrlFromIvy(virtualFile.getPath(), fullIvyURL);
            }
            catch (JDOMException e1) {
                e1.printStackTrace();
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
            IvyUtil.importLibToModuleLib(libs, module);
            Messages.showMessageDialog(module.getProject(), "Import libs successfully",
                                       "Information", Messages.getInformationIcon());
        }
        else {
            Messages.showMessageDialog(module.getProject(), "Only ivy.xml will be accepted",
                                       "Wanning", Messages.getWarningIcon());
        }
    }

    public void update(AnActionEvent e)
    {
        VirtualFile virtualFile = (VirtualFile) e.getDataContext().getData(DataConstants.VIRTUAL_FILE);
        if (virtualFile == null) {
            e.getPresentation().setEnabled(false);
            return;
        }
        String filename = virtualFile.getName();
        e.getPresentation().setEnabled("ivy.xml".equals(filename));
    }


    public static void main(String[] args) throws JDOMException, IOException
    {
        String pattern = "c://[module]/[type]s/[artifact]-[revision].[ext]";
        ImportAction ia = new ImportAction();
        String ivy = "I:\\myDev\\IvyDependencyImportor\\src\\help\\ivy.xml";
        String[] libsFromIvy = IvyUtil.getLibsUrlFromIvy(ivy, pattern);
        Log.log(libsFromIvy.length);
        for (String aLibsFromIvy : libsFromIvy) {
            Log.log(aLibsFromIvy);
        }
    }
}
