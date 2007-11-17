/**
 * Copyright 2004-2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * History:
 *   2007-11-18 0:33:51 Created by Numen
 */
package org.ivydependencyimportor.ivy;

import gz.plugin.ivy.Setting;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:hyysguyang@gmail.com">Numen</a>
 * @version 1.0 2007-11-18 0:33:51
 */
public class IvyHelperTest {

    String baseDir = IvyHelperTest.class.getResource("/").getFile();

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetFilePathinLocalRepo() throws Exception {

        IvyHelper ivyHelper = new IvyHelper();

        Setting setting = Setting.getSetting();
        setting.setCacheDir(IvyHelperTest.class.getResource("/cache").getFile());
        setting.setRepositoryDir(IvyHelperTest.class.getResource("/repo").getFile());
        setting.setIvySettingFile(IvyHelperTest.class.getResource("ivysettings.xml").getFile());
        setting.setIvyFile(IvyHelperTest.class.getResource("ivy.xml").getFile());
        setting.setUseLocalRepository(true);
        setting.setTransitive(true);

        ivyHelper.setSetting(setting);
        ivyHelper.resolve();

        Assert.assertEquals(2, ivyHelper.getFilePathinLocalRepo().size());
        Assert.assertEquals(baseDir.replace('/', '\\').substring(1) + "repo\\org1\\mod1.1\\jars\\mod1.1-1.0.jar", ivyHelper.getFilePathinLocalRepo().get(0));
    }

    /* @Test
    public void testGetFilePathInCahe() throws Exception {

        Assert.assertEquals(baseDir+"\\org\\ivydependencyimportor\\ivy\\ivy.xml", new IvyHelper().getFilePathInCahe());
    }*/

}
