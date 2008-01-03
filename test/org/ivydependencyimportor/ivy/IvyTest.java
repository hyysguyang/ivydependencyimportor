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

import java.io.File;
import java.util.List;

import org.apache.ivy.util.FileUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:hyysguyang@gmail.com">Numen</a>
 * @version 1.0 2007-11-18 0:33:51
 */
public class IvyTest
{

    String baseDir = IvyTest.class.getResource("/").getFile();
    private String cache;
    private IvyConfig ivyConfig;

    @Before
    public void setUp()
            throws Exception
    {
        cache = IvyTest.class.getResource("/cache").getFile();
        ivyConfig = getIvyConfig();
    }

    @After
    public void tearDown()
            throws Exception
    {
        ivyConfig = null;
        cleanCache();
    }

    private void cleanCache()
    {
        File cacheFile = new File(cache);
        FileUtil.forceDelete(cacheFile);
        cacheFile.mkdir();
    }

    @Test
    public void testGetJarArtifactsFilePathInLocalRepo()
            throws Exception
    {
        ivyConfig.setUseLocalRepository(true);
        List<String> pathinLocalRepo = getIvyHepler(ivyConfig).getJarArtifactsFilePath();
        Assert.assertEquals(2, pathinLocalRepo.size());
        String expected = baseDir.substring(1) + "repo/org1/mod1.1/jars/mod1.1-1.0.jar";
        Assert.assertEquals(expected.replace("/", File.separator), pathinLocalRepo.get(0));
    }

    @Test
    public void testGetJarArtifactsFilePathInLocalCache()
            throws Exception
    {
        ivyConfig.setUseCache(true);

        List<String> pathinLocalRepo = getIvyHepler(ivyConfig).getJarArtifactsFilePath();
        Assert.assertEquals(2, pathinLocalRepo.size());
        String expected = baseDir.substring(1) + "cache/org1/mod1.1/jars/mod1.1-1.0.jar";
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
            throws Exception
    {
        IvyFacade ivyFacade = new IvyFacade();
        ivyFacade.setIvyConfig(setting);
        ivyFacade.resolve();
        return ivyFacade;
    }

    private IvyConfig getIvyConfig()
    {
        IvyConfig setting = new IvyConfig();
        setting.setCacheDir(cache);
        setting.setRepositoryDir(IvyTest.class.getResource("/repo").getFile());
        setting.setIvySettingFile(IvyTest.class.getResource("ivysettings.xml").getFile());
        setting.setIvyFile(IvyTest.class.getResource("ivy.xml").getFile());
        setting.setTransitive(true);
        return setting;
    }


}
