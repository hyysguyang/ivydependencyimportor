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

import org.apache.ivy.util.FileUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * @author <a href="mailto:hyysguyang@gmail.com">Numen</a>
 * @version 1.0 2007-11-18 0:33:51
 */
public class IvyTest {
    private String ivyUserDir=IvyTest.class.getResource("/ivy-user-dir").getFile();
    private IvyConfig ivyConfig;

    @Before
    public void setUp()
            throws Exception {
        this.ivyConfig = getIvyConfig();
    }

    private String getCache() {
        return ivyUserDir+ File.separator+"cache";
    }

    @After
    public void tearDown()
            throws Exception {
        this.ivyConfig = null;
        cleanCache();
    }

    private void cleanCache() {
        File cacheFile = new File(getCache());
        FileUtil.forceDelete(cacheFile);
        cacheFile.mkdir();
    }

    @Test
    public void testGetJarArtifactsFilePathInLocalRepo()
            throws Exception {
        this.ivyConfig.setUseLocalRepository(true);
        List<String> pathinLocalRepo = getIvyHepler(this.ivyConfig).getJarArtifactsFilePath();
        Assert.assertEquals(2, pathinLocalRepo.size());
        String expected = IvyTest.class.getResource("/repo/org1/mod1.1/jars/mod1.1-1.0.jar").getFile().substring(1);
        Assert.assertEquals(expected.replace("/", File.separator), pathinLocalRepo.get(0));
    }

    @Test
    public void testGetJarArtifactsFilePathInLocalCache()
            throws Exception {
        doTestWithCache();
    }


    @Test
    public void testDefaultVariableInSettingFile()
            throws Exception {
        ivyConfig.setIvySettingFile(IvyTest.class.getResource("ivysettings_default_variable_test.xml").getFile());

        doTestWithCache();
    }

    @Test
    public void testDefaultIvySettingsFilesResolveArtifactFromRemoteMaven2Repositry()
            throws Exception {
        ivyConfig.setIvySettingFile(null);
        ivyConfig.setIvyFile(IvyTest.class.getResource("ivy_default_ivysettings_file.xml").getFile());
        System.setProperty("ivy.default.ivy.user.dir",this.ivyUserDir);

        this.ivyConfig.setUseCache(true);
        List<String> pathinLocalRepo = getIvyHepler(this.ivyConfig).getJarArtifactsFilePath();
        Assert.assertEquals(1, pathinLocalRepo.size());
        String expected = IvyTest.class.getResource("/ivy-user-dir/cache/junit/junit/jars/junit-3.8.2.jar").getFile().substring(1);
        Assert.assertEquals(expected.replace("/", File.separator), pathinLocalRepo.get(0));
    }


    private void doTestWithCache() throws Exception {
        this.ivyConfig.setUseCache(true);
        List<String> pathinLocalRepo = getIvyHepler(this.ivyConfig).getJarArtifactsFilePath();
        Assert.assertEquals(2, pathinLocalRepo.size());
        String expected = IvyTest.class.getResource("/ivy-user-dir/cache/org1/mod1.1/jars/mod1.1-1.0.jar").getFile().substring(1);
        Assert.assertEquals(expected.replace("/", File.separator), pathinLocalRepo.get(0));
    }

//    @Test
//    public void testGetArtifactsIncludeSource()
//            throws Exception
//    {
//        ivyConfig.setIncludeSources(true);
//        List<Artifact> artifacts = getIvyHepler(ivyConfig).getArtifacts();
//        Assert.assertEquals(3, artifacts.size());
//        Assert.assertEquals("mod1.1", artifacts.get(0).getName());
//        Assert.assertEquals("mod1.1", artifacts.get(1).getName());
//        Assert.assertEquals("source", artifacts.get(1).getType());
//        Assert.assertEquals("mod1.2", artifacts.get(2).getName());
//    }


    private IvyFacade getIvyHepler(IvyConfig setting)
            throws Exception {
        IvyFacade ivyFacade = new IvyFacade();
        ivyFacade.setIvyConfig(setting);
        ivyFacade.resolve();
        return ivyFacade;
    }

    private IvyConfig getIvyConfig() {
        IvyConfig ivyConfig = new IvyConfig();
        ivyConfig.setCacheDir(getCache());
        ivyConfig.setRepositoryDir(IvyTest.class.getResource("/repo").getFile());
        ivyConfig.setIvySettingFile(IvyTest.class.getResource("ivysettings.xml").getFile());
        ivyConfig.setIvyFile(IvyTest.class.getResource("ivy.xml").getFile());
        ivyConfig.setTransitive(true);
        return ivyConfig;
    }


}
