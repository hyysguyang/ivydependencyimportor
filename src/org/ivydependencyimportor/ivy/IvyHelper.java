/**
 *
 */
package org.ivydependencyimportor.ivy;

import gz.plugin.ivy.Setting;
import org.apache.ivy.Ivy;
import org.apache.ivy.core.cache.CacheManager;
import org.apache.ivy.core.module.descriptor.Artifact;
import org.apache.ivy.core.report.ResolveReport;
import org.apache.ivy.core.settings.IvySettings;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author numen
 */
public class IvyHelper {
    private String filePathInCahe;
    private Setting setting;

    private Ivy ivy = null;
    private ResolveReport resolveReport;
    private static final String IVY_REPO_DIR = "ivy.repo.dir";


    public String getFilePathInCahe() {
        return filePathInCahe;
    }

    public List<String> getJarArtifactsFilePath() {
        List<Artifact> arti = getArtifacts();
        List<String> list = new ArrayList<String>();
        CacheManager cacheManager = ivy.getCacheManager(new File(setting.getCacheDir()));
        for (Artifact artifact : arti) {
            list.add(cacheManager.getArchiveFileInCache(artifact).getAbsolutePath());
        }
        return list;
    }

    public List<Artifact> getArtifacts()
    {
        return (List<Artifact>) resolveReport.getArtifacts();
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    public void resolve() throws Exception {
        ivy = Ivy.newInstance();
        ivy.setVariable(IVY_REPO_DIR, setting.getRepositoryDir());

        ivy.configure(new File(setting.getIvySettingFile()));
        IvySettings ivySettings = ivy.getSettings();
        if (setting.getCache() == null) {
            setting.setCacheDir(ivySettings.getDefaultCache().getAbsolutePath());
        }
        ivySettings.setDefaultCache(setting.getCache());
        resolveReport = ivy.resolve(new File(setting.getIvyFile()).toURL(), setting.getResolveOptions());
    }
}
