package org.ivydependencyimportor.ivy.idea;

import com.intellij.testFramework.IdeaTestCase;
import org.ivydependencyimportor.ivy.idea.setting.SettingHelper;
import org.ivydependencyimportor.ivy.idea.setting.Setting;

/**
 * Created by IntelliJ IDEA.
 * User: new
 * Date: 2008-1-13
 * Time: 18:54:45
 * To change this template use File | Settings | File Templates.
 */
public class Demo extends IdeaTestCase {
    protected void setUp() throws Exception {
        super.setUp();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void testA() throws Exception {
        Setting setting = SettingHelper.getInstance().getSetting();
        System.out.println(setting);
        setting.setArtifactPattern("lsdjflsdfj");
        Thread.sleep(5000);
    }
}
