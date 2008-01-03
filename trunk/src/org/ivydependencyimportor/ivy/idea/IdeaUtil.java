package org.ivydependencyimportor.ivy.idea;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.ui.Messages;

import java.io.*;
import java.util.List;

import org.ivydependencyimportor.ivy.Log;

/**
 * @author <a href="mailto:gzkaneg@gmail.com">gzkaneg</a>
 */
public class IdeaUtil {
    public static void importLibToModuleLib(List<String> newUrls, Module module) {
        OrderRootType rootType = OrderRootType.CLASSES;
        ModuleRootManager mrm = ModuleRootManager.getInstance(module);
        final ModifiableRootModel mrModel = mrm.getModifiableModel();
        LibraryTable table = mrModel.getModuleLibraryTable();

        final LibraryTable.ModifiableModel modModel = table.getModifiableModel();

        Library[] oldLibs = table.getLibraries();
        for (Library oldLib : oldLibs) {
            modModel.removeLibrary(oldLib);
        }

        for (String newUrl : newUrls) {
            Library newLib = table.createLibrary();
            final Library.ModifiableModel libModel = newLib.getModifiableModel();
            libModel.addRoot(newUrl, rootType);
            libModel.commit();
        }

        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            public void run() {
                modModel.commit();
                mrModel.commit();
            }
        });
    }

    public static void exportLibToDirectory(String[] libFiles, File dir) throws IOException {
        Log.log("Start to export lib to dir:" + dir.getAbsolutePath());
        for (String lib : libFiles) {
            File libFile = new File(lib);
            Log.log("from:" + libFile.getAbsolutePath());
            if (libFile.isFile()) {
                File newFile = new File(dir, libFile.getName());
                copyFile(libFile, newFile);
            }
        }
    }

    public static void copyFile(File src, File dest) throws IOException {
        Log.log("Copy file, from:" + src.getAbsolutePath() + ",to:" + dest.getAbsolutePath());
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(src));
            bos = new BufferedOutputStream(new FileOutputStream(dest));
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
        }
        finally {
            if (bis != null) {
                bis.close();
            }

            if (bos != null) {
                bos.close();
            }
        }
    }

    public static void error(Throwable e) {
        Messages.showMessageDialog(e.getMessage(), "Error", Messages.getErrorIcon());
    }
}
