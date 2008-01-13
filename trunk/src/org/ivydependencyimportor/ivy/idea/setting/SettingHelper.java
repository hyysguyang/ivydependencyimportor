package org.ivydependencyimportor.ivy.idea.setting;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.util.DefaultJDOMExternalizer;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.NamedJDOMExternalizable;
import com.intellij.openapi.util.WriteExternalException;
import org.ivydependencyimportor.ivy.Log;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Save settings to disk.
 * @author <a href="mailto:gzkaneg@gmail.com">gzkaneg</a>
 */
public class SettingHelper implements NamedJDOMExternalizable, ApplicationComponent {
    private static SettingHelper helper = new SettingHelper();
    private static Setting setting = new Setting();

    private SettingHelper() {
    }

    public static SettingHelper getInstance() {
        return helper;
    }

    public Setting getSetting() {
        return setting;
    }

    public void readExternal(Element element) throws InvalidDataException {
        Log.log("read element in helper:" + setting);
        DefaultJDOMExternalizer.readExternal(setting, element);
    }

    public void writeExternal(Element element) throws WriteExternalException {
        Log.log("Write element in helper:" + setting);
        DefaultJDOMExternalizer.writeExternal(setting, element);
    }

    @NonNls
    public String getExternalFileName() {
        return "IvySettings";
    }

    @NonNls
    @NotNull
    public String getComponentName() {
        return "SettingHelper";
    }

    public void initComponent() {
        Log.log("Init ivySettingHelper");
    }

    public void disposeComponent() {
        Log.log("dispose ivySettingHelper");
    }
}
