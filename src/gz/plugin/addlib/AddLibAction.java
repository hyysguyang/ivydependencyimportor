package gz.plugin.addlib;

import java.util.ArrayList;
import java.util.List;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * @author <a href="mailto:zgong@naesasoft.com">zgong</a>
 * @version $Id: MailGroup.java,v 1.2 2005/04/26 03:33:23 zgong Exp $
 */
public class AddLibAction extends AnAction
{

    public void actionPerformed(AnActionEvent e)
    {
        DataContext dataContext = e.getDataContext();
        Module module = (Module) dataContext.getData(DataConstants.MODULE);
        if (module == null) {
            return;
        }
        VirtualFile[] vfs = (VirtualFile[]) dataContext.getData(DataConstants.VIRTUAL_FILE_ARRAY);
        if (vfs == null || vfs.length < 1) {
            return;
        }
        List<String> list = new ArrayList<String>();
        for (VirtualFile vf : vfs) {
            String type = vf.getFileType().getName();
            if (type.equals("ARCHIVE")) {
                String name = "jar://" + vf.getPath() + "!/";
                list.add(name);
            }
        }
        addToModuleLib(module, list.toArray(new String[0]));
    }

    public void update(AnActionEvent e)
    {
        DataContext dataContext = e.getDataContext();
        VirtualFile[] vfs = (VirtualFile[]) dataContext.getData(DataConstants.VIRTUAL_FILE_ARRAY);
        if (vfs != null && vfs.length == 1) {
            e.getPresentation().setEnabled(true);
        }
        else {
            e.getPresentation().setEnabled(false);
        }
    }

    private void addToModuleLib(Module module, String[] newUrls)
    {
        OrderRootType rootType = OrderRootType.CLASSES;
        ModuleRootManager mrm = ModuleRootManager.getInstance(module);
        final ModifiableRootModel mrModel = mrm.getModifiableModel();
        LibraryTable table = mrModel.getModuleLibraryTable();

        final LibraryTable.ModifiableModel modModel = table.getModifiableModel();
//
        Library[] oldLibs = table.getLibraries();

//        for (int i = 0; i < oldLibs.length; i++) {
//            modModel.removeLibrary(oldLibs[i]);
//        }

        for (String newUrl : newUrls) {
            if (contains(oldLibs, newUrl)) {
                continue;
            }
            Library newLib = table.createLibrary();
            final Library.ModifiableModel libModel = newLib.getModifiableModel();
            libModel.addRoot(newUrl, rootType);
            libModel.commit();
        }

        ApplicationManager.getApplication().runWriteAction(new Runnable()
        {
            public void run()
            {
                modModel.commit();
                mrModel.commit();
            }
        });
    }

    private boolean contains(Library[] libs, String newUrl)
    {
        for (Library lib : libs) {
            String[] urls = lib.getUrls(OrderRootType.CLASSES);
            for (String url : urls) {
                if (url.equalsIgnoreCase(newUrl)) {
                    return true;
                }
            }
        }
        return false;
    }
}
