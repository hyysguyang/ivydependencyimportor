package org.ivydependencyimportor.ivy.idea.setting;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.IconLoader;
import org.ivydependencyimportor.ivy.Log;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Setting GUI
 * @author <a href="mailto:gzkaneg@gmail.com">gzkaneg</a>
 */
public class SettingUI implements Configurable, ApplicationComponent {
    SettingPanel uiForm;

    public String getDisplayName() {
        return "Ivy Importor";
    }

    public Icon getIcon() {
        return IconLoader.getIcon("/org/ivydependencyimportor/ivy/idea/res/logo.png");
    }

    public String getHelpTopic() {
        return null;
    }

    public JComponent createComponent() {
        Log.log("create componet in SettingUI");
        return uiForm.getPanel();
    }

    public void apply() throws ConfigurationException {
        Log.log("apply in SettingUI");
        uiForm.saveSettings();
    }

    public void reset() {
        Log.log("reset in SettingUI");
        uiForm.readSettings();
    }

    public void disposeUIResources() {
        Log.log("dispose UI Resources");
    }

    @NotNull
    public String getComponentName() {
        return "importor";
    }

    public void initComponent() {
        Log.log("init SettingUI");
        uiForm = new SettingPanel();
    }

    public void disposeComponent() {
        Log.log("dispose SettingUI");
    }

    public boolean isModified() {
        return uiForm.isChanged();
    }
}