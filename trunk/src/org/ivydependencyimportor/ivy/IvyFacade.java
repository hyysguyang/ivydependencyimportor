/**
 *
 */
package org.ivydependencyimportor.ivy;

import org.apache.ivy.Ivy;
import org.apache.ivy.core.cache.CacheManager;
import org.apache.ivy.core.module.descriptor.Artifact;
import org.apache.ivy.core.report.ResolveReport;
import org.apache.ivy.core.settings.IvySettings;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Ivy process facade for the ivy tools
 *
 * @author <a href="mailto:hyysguyang@gmail.com">Numen</a>
 * @version 1.0 2008-1-3 16:45:25
 */
public class IvyFacade {
    private IvyConfig ivyConfig;

    private Ivy ivy = null;
    private ResolveReport resolveReport;
    private static final String IVY_REPO_DIR = "ivy.repo.dir";

    /**
     * Get the full file path of jar artifacts
     * If <code>useCache</code> of IvyConfig is set to <code>true</code>
     * then,return the file path that in cache,else return the file path than in local repository
     *
     * @return All jar artifacts file path
     */
    public List<String> getJarArtifactsFilePath() {
        List<Artifact> arti = getArtifacts();
        List<String> list = new ArrayList<String>();
        CacheManager cacheManager = ivy.getCacheManager(new File(ivyConfig.getCacheDir()));
        for (Artifact artifact : arti) {
            list.add(cacheManager.getArchiveFileInCache(artifact).getAbsolutePath());
        }
        return list;
    }

    private List<Artifact> getArtifacts()
    {
        return (List<Artifact>) resolveReport.getArtifacts();
    }

    public void setIvyConfig(IvyConfig ivyConfig) {
        this.ivyConfig = ivyConfig;
    }

    public void resolve() throws Exception {
        ivy = Ivy.newInstance();
        ivy.setVariable(IVY_REPO_DIR, ivyConfig.getRepositoryDir());

        ivy.configure(new File(ivyConfig.getIvySettingFile()));
        IvySettings ivySettings = ivy.getSettings();
        if (ivyConfig.getCache() == null) {
            ivyConfig.setCacheDir(ivySettings.getDefaultCache().getAbsolutePath());
        }
        ivySettings.setDefaultCache(ivyConfig.getCache());
        resolveReport = ivy.resolve(new File(ivyConfig.getIvyFile()).toURI().toURL(), ivyConfig.getResolveOptions());
    }
}
