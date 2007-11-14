package gz.plugin.ivy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.ui.Messages;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * @author <a href="mailto:zgong@naesasoft.com">zgong</a>
 * @version $Id: MailGroup.java,v 1.2 2005/04/26 03:33:23 zgong Exp $
 */
public class IvyUtil
{
    public static void importLibToModuleLib(String[] newUrls, Module module)
    {
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

        ApplicationManager.getApplication().runWriteAction(new Runnable()
        {
            public void run()
            {
                modModel.commit();
                mrModel.commit();
            }
        });
    }

    public static String[] getLibsUrlFromIvy(String ivyFile, String pattern) throws JDOMException, IOException
    {
        return getLibsFromIvy(ivyFile, pattern, true);
    }

    public static String[] getLibsFromIvy(String ivyFile, String pattern, boolean isUrl) throws JDOMException, IOException
    {
        List<String> jars = new ArrayList<String>();

        SAXBuilder sax = new SAXBuilder();
        org.jdom.Document doc = sax.build(ivyFile);
        org.jdom.Element root = doc.getRootElement();
        org.jdom.Element dependencies = root.getChild("dependencies");
        for (Object o : dependencies.getChildren("dependency")) {
            Element dependency = (Element) o;
            String org = dependency.getAttributeValue("org");
            String moduleName = dependency.getAttributeValue("name");
            String rev = dependency.getAttributeValue("rev");

            List artifacts = dependency.getChildren("artifact");
            if (artifacts != null && artifacts.size() > 0) {
                for (Object artifact1 : artifacts) {
                    Element artifact = (Element) artifact1;
                    String artifactName = artifact.getAttributeValue("name");
                    String artifactType = artifact.getAttributeValue("type");
                    String ext = artifact.getAttributeValue("ext");
                    String artifactUrl;
                    if (isUrl) {
                        artifactUrl = getArtifactUrl(pattern, org, moduleName, rev,
                                                      artifactName, artifactType, ext);
                    }
                    else {
                        artifactUrl = getArtifactFile(pattern, org, moduleName, rev,
                                                     artifactName, artifactType, ext);
                    }
                    jars.add(artifactUrl);
                }
            }
            else {
                String artifactUrl = getArtifactFile(pattern, org, moduleName, rev,
                                                     moduleName, "jar", "jar");
                jars.add(artifactUrl);
            }
        }

        return jars.toArray(new String[jars.size()]);
    }

    public static String getArtifactFile(String pattern, String org, String moduleName, String revision, String artifact,
                                         String type, String ext)
    {
        org = org.replace('.', '/');
        return IvyPatternHelper.substitute(pattern, org, moduleName, revision, artifact, type, ext);
    }

    public static String getArtifactUrl(String pattern, String org, String moduleName, String revision, String artifact,
                                        String type, String ext)
    {
        org = org.replace('.', '/');
        String s = IvyPatternHelper.substitute(pattern, org, moduleName, revision, artifact, type, ext);
        return "jar://" + s + "!/";
    }

    public static void exportLibToDirectory(String[] libFiles, File dir) throws IOException
    {
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

    public static void copyFile(File src, File dest) throws IOException
    {
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

    public static void error(Throwable e)
    {
        Messages.showMessageDialog(e.getMessage(), "Error", Messages.getErrorIcon());
    }
}
