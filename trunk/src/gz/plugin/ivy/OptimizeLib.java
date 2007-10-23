//package gz.plugin.ivy;
//
//import java.io.IOException;
//import java.util.*;
//import java.util.jar.JarFile;
//import java.util.zip.ZipEntry;
//
//import com.intellij.openapi.actionSystem.AnAction;
//import com.intellij.openapi.actionSystem.AnActionEvent;
//import com.intellij.openapi.actionSystem.DataConstants;
//import com.intellij.openapi.actionSystem.DataContext;
//import com.intellij.openapi.module.Module;
//import com.intellij.openapi.roots.ModuleRootManager;
//import com.intellij.openapi.roots.OrderRootType;
//import com.intellij.openapi.ui.Messages;
//import com.intellij.openapi.vfs.VirtualFile;
//import com.intellij.psi.PsiDirectory;
//import com.intellij.psi.PsiFile;
//import com.intellij.psi.PsiImportList;
//import com.intellij.psi.PsiImportStatement;
//import com.intellij.psi.PsiJavaFile;
//import com.intellij.psi.PsiManager;
//
///**
// * @author <a href="mailto:zgong@naesasoft.com">zgong</a>
// * @version $Id: MailGroup.java,v 1.2 2005/04/26 03:33:23 zgong Exp $
// */
//public class OptimizeLib extends AnAction
//{
//    private Module module;
//
//    private List getClassListFromJar(JarFile jf)
//    {
//        List list = new ArrayList(jf.size());
//        Enumeration enumeration = jf.entries();
//        while (enumeration.hasMoreElements()) {
//            ZipEntry zipEntry = (ZipEntry) enumeration.nextElement();
//            String name = zipEntry.getName().replace('/', '.');
//            if (name.endsWith(".class")) {
//                list.add(name.substring(0, name.length() - 6));
//            }
//        }
//        return list;
//    }
//
//    private List getList(Collection classes, List jars)
//    {
//        List list = new ArrayList(jars.size());
//        for (Iterator jit = jars.iterator(); jit.hasNext();) {
//            JarFile jarFile = (JarFile) jit.next();
//            List cls = getClassListFromJar(jarFile);
//            printCollection(cls);
//            if (!Collections.disjoint(classes, cls)) {
//                list.add(jarFile);
//            }
//        }
//        return list;
//    }
//
//
//    public void actionPerformed(AnActionEvent e)
//    {
//        DataContext dataContext = e.getDataContext();
//        module = (Module) dataContext.getData(DataConstants.MODULE);
//        VirtualFile vf = (VirtualFile) dataContext.getData(DataConstants.VIRTUAL_FILE);
//        PsiManager pm = PsiManager.getInstance(module.getProject());
//        PsiDirectory src = pm.findDirectory(vf);
//        Log.log("=========================");
//        Set imports = getAllJavaFileImports(src);
//        Iterator iterator = imports.iterator();
//        while (iterator.hasNext()) {
//            Object o = iterator.next();
//            Log.log(o);
//        }
//        Log.log("=========================");
//        List list = getList(imports, getJars());
//        StringBuffer sb = new StringBuffer();
//        for (int i = 0; i < list.size(); i++) {
//            JarFile jf = (JarFile) list.get(i);
//            Log.log(jf.getName());
//            sb.append(jf.getName());
//            sb.append("\n");
//        }
//        Messages
//            .showMessageDialog(module.getProject(), sb.toString(), "Information",
//                Messages.getInformationIcon());
//    }
//
//    private List getJars()
//    {
//        List list = new ArrayList();
//        OrderRootType rootType = OrderRootType.CLASSES;
//        ModuleRootManager mrm = ModuleRootManager.getInstance(module);
//        VirtualFile[] files = mrm.getFiles(rootType);
//        for (int i = 0; i < files.length; i++) {
//            try {
//                list.add(new JarFile(files[i].getPresentableUrl()));
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return list;
//    }
//
//    private Set getAllJavaFileImports(PsiDirectory src)
//    {
//        Set set = new TreeSet();
//        PsiFile[] pfs = src.getFiles();
//        for (int i = 0; i < pfs.length; i++) {
//            if (pfs[i] instanceof PsiJavaFile) {
//                PsiJavaFile pjf = (PsiJavaFile) pfs[i];
//                PsiImportList importList = pjf.getImportList();
//                PsiImportStatement[] importStatements = importList.getImportStatements();
//                for (int j = 0; j < importStatements.length; j++) {
//                    String className = importStatements[j].getQualifiedName();
//                    if (!className.startsWith("java")) {
//                        set.add(className);
//                    }
//                }
//            }
//        }
//        PsiDirectory[] pds = src.getSubdirectories();
//        for (int i = 0; i < pds.length; i++) {
//            Set tmp = getAllJavaFileImports(pds[i]);
//            set.addAll(tmp);
//        }
//        return set;
//    }
//
//    private void printCollection(Collection c)
//    {
//        Log.log("[[[[[[[[[[[[[[[[[[[[[");
//        Iterator it = c.iterator();
//        while (it.hasNext()) {
//            Object o = it.next();
//            Log.log(o);
//        }
//        Log.log("]]]]]]]]]]]]]]]]]]]]]]]]");
//    }
//
//    public static void main(String[] args)
//        throws IOException
//    {
//        OptimizeLib ol = new OptimizeLib();
//        JarFile jf = new JarFile("I:\\develop\\IntelliJ IDEA 5.0\\jre\\lib\\rt.jar");
//        List jfList = new ArrayList();
//        jfList.add(jf);
//        List list = new ArrayList();
//        list.add("java.lang.String");
//        List list2 = ol.getList(list, jfList);
//        ol.printCollection(list2);
//    }
//}
