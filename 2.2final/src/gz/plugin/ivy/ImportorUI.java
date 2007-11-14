package gz.plugin.ivy;

import javax.swing.*;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.NamedJDOMExternalizable;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;

/**
 * @author <a href="mailto:zgong@naesasoft.com">zgong</a>
 * @version $Id: MailGroup.java,v 1.2 2005/04/26 03:33:23 zgong Exp $
 */
public class ImportorUI implements Configurable, NamedJDOMExternalizable, ApplicationComponent
{
    OptionPanel uiForm;
    String distLib;

    public String getDisplayName()
    {
        return "Ivy Importor";
    }

    public Icon getIcon()
    {
        return IconLoader.getIcon("/gz/plugin/ivy/ZoomIn.png");
    }

    public String getHelpTopic()
    {
        return null;
    }

    public JComponent createComponent()
    {
        Log.log("createcomponet");
        return uiForm.getPanel();
    }

    public void apply() throws ConfigurationException
    {
        Log.log("apply");
        uiForm.saveSettings();
    }

    public void reset()
    {
        Log.log("reset");
        uiForm.readSettings();
    }

    public void disposeUIResources()
    {
        Log.log("dis");
    }

    public String getExternalFileName()
    {
        return "ivy_importor_conf";
    }

    public void readExternal(Element element) throws InvalidDataException
    {
        Log.log("read");
        Setting.getSetting().read(element);
    }

    public void writeExternal(Element element) throws WriteExternalException
    {
        Log.log("write");
        Setting.getSetting().write(element);
    }

    public String getComponentName()
    {
        return "importxxxx";
    }

    public void initComponent()
    {
        Log.log("init");
        uiForm = new OptionPanel();
    }

    public void disposeComponent()
    {
        Log.log("dispose");
    }

    public boolean isModified()
    {
        return uiForm.isChanged();
    }
}
