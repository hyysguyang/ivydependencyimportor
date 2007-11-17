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

    Ivy ivy = null;
    ResolveReport resolveReport;


    public String getFilePathInCahe() {
        return filePathInCahe;
    }

    public List<String> getFilePathinLocalRepo() {
        List<Artifact> arti = resolveReport.getArtifacts();


        List<String> list = new ArrayList<String>();

        CacheManager cacheManager = ivy.getCacheManager(new File(setting.getCacheDir()));
        for (Artifact artifact : arti) {
            list.add(cacheManager.getArchiveFileInCache(artifact).getAbsolutePath());
        }
        return list;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    public void resolve() throws Exception {
        ivy = Ivy.newInstance();
        ivy.setVariable("ivy.repo.dir", setting.getRepositoryDir());

        ivy.configure(new File(setting.getIvySettingFile()));
        IvySettings ivySettings = ivy.getSettings();
        if (setting.getCacheDir() == null) {
            setting.setCacheDir(ivySettings.getDefaultCache().getAbsolutePath());
        }
        ivySettings.setDefaultCache(new File(setting.getCacheDir()));
        resolveReport = ivy.resolve(new File(setting.getIvyFile()).toURL(), setting.getResolveOptions());

    }
}
