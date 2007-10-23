package gz.plugin.ivy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;

/**
 * @author <a href="mailto:zgong@naesasoft.com">zgong</a>
 * @version $Id: MailGroup.java,v 1.2 2005/04/26 03:33:23 zgong Exp $
 */
public class IvyGenerator extends AnAction
{
    Module module;

    public void actionPerformed(AnActionEvent e)
    {
        DataContext dataContext = e.getDataContext();
        module = (Module) dataContext.getData(DataConstants.MODULE);
        String ivyPattern = null;
        String[] allLibs = getAllLibs();
        for (int i = 0; i < allLibs.length; i++) {
            String url = formatUrl(allLibs[i]);
            Map map = getMap(ivyPattern, url);
        }
    }

    private String[] getAllLibs()
    {
        OrderRootType rootType = OrderRootType.CLASSES;
        ArrayList list = new ArrayList();
        ModuleRootManager mrm = ModuleRootManager.getInstance(module);
        final ModifiableRootModel mrModel = mrm.getModifiableModel();
        LibraryTable table = mrModel.getModuleLibraryTable();


        Library[] libs = table.getLibraries();
        for (int i = 0; i < libs.length; i++) {
            String[] urls = libs[i].getUrls(rootType);
            for (int j = 0; j < urls.length; j++) {
                list.add(urls[j]);
                Log.log(urls[j]);
            }
        }
        return (String[]) list.toArray(new String[0]);
    }

    private String formatUrl(String url)
    {
        return url.substring("jar://".length(), url.length() - 2);
    }

    private static final Pattern TOKEN_PATTERN = Pattern.compile("\\[(.*?)\\]");
    public static final String TYPE_KEY = "type";
    public static final String EXT_KEY = "ext";
    public static final String ARTIFACT_KEY = "artifact";
    public static final String REVISION_KEY = "revision";
    public static final String MODULE_KEY = "module";
    public static final String ORGANISATION_KEY = "organisation";

    public static void main(String[] args)
    {

        String pattern =
            "D:/Program Files/JetBrains/IntelliJ IDEA 5.1/bin/[organisation]/[module]/[artifact]-[revision].[ext]";
        StringBuffer patternBuf = new StringBuffer(pattern);
        int pos = pattern.lastIndexOf('.');
        patternBuf.insert(pos, '\\');
        Matcher m = TOKEN_PATTERN.matcher(patternBuf);

        StringBuffer sb = new StringBuffer();
        ArrayList list1 = new ArrayList();
        ArrayList list2 = new ArrayList();
        while (m.find()) {
            list1.add(m.group(1));
            m.appendReplacement(sb, "(.+)");
        }
        m.appendTail(sb);
        String p = sb.toString();
        Log.log(p + "}");
        Pattern newP = Pattern.compile(p);
        Matcher newM = newP.matcher("D:/Program Files/JetBrains/IntelliJ IDEA 5.1/bin/ceno/umail/umail_base-1.0.jar");
        if (newM.find()) {
            Log.log(newM.groupCount());
        }
        for (int i = 0; i < newM.groupCount(); i++) {
            String var = newM.group(i + 1);
            Log.log(var);
            list2.add(var);
        }
    }

    private Map getMap(String ivyPattern, String url)
    {
        List[] lists = new List[2];
        Map map = new HashMap();
        String pattern = "[organisation]/[module]/[artifact]-[revision].[ext]";
        StringBuffer patternBuf = new StringBuffer(pattern);
        int pos = pattern.lastIndexOf('.');
        patternBuf.insert(pos, '\\');
        Matcher m = TOKEN_PATTERN.matcher(patternBuf);

        StringBuffer sb = new StringBuffer();
        ArrayList list1 = new ArrayList();
        ArrayList list2 = new ArrayList();
        while (m.find()) {
            list1.add(m.group(1));
            m.appendReplacement(sb, "(.+)");
        }
        m.appendTail(sb);
        String p = sb.toString();
        Pattern newP = Pattern.compile(p);
        Matcher newM = newP.matcher("ceno/umail/umail_base-1.0.jar");
        if (newM.find()) {
            Log.log(newM.groupCount());
        }
        for (int i = 0; i < newM.groupCount(); i++) {
            String var = newM.group(i + 1);
            Log.log(var);
            list2.add(var);
        }
        lists[0] = list1;
        lists[1] = list2;
        if (list1.size() == list2.size()) {
            for (int i = 0; i < list1.size(); i++) {
                map.put(list1.get(i), list2.get(i));
            }
        }
        return map;
    }
}
