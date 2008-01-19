/**
 * Copyright(c) 2005 Ceno Techonologies, Ltd.
 *
 * History:
 *   2008-1-3 16:45:25 Created by ygu
 */
package org.ivydependencyimportor.ivy;

import org.apache.ivy.core.resolve.ResolveOptions;

import java.io.File;

/**
 * Ivy setting config.
 * Those config may be come from IDE.
 *
 * @author <a href="mailto:hyysguyang@gmail.com">Numen</a>
 * @version 1.0 2008-1-3 16:45:25
 */
public class IvyConfig {
    private String ivySettingFile;
    private String ivyFile;
    private String cacheDir;
    private String repositoryDir;
    private boolean useCache = false;
    private boolean transitive;


    public String getIvySettingFile() {
        return ivySettingFile;
    }

    public String getIvyFile() {
        return ivyFile;
    }

    public ResolveOptions getResolveOptions() {
        ResolveOptions resolveOptions = new ResolveOptions();
        resolveOptions = resolveOptions.setUseOrigin(isUseLocalRepository());
        resolveOptions = resolveOptions.setTransitive(isTransitive());
        return resolveOptions;
    }

    public void setIvySettingFile(String ivysettings) {

        this.ivySettingFile = ivysettings;
    }

    public void setIvyFile(String ivyFile) {
        this.ivyFile = ivyFile;
    }

    public String getCacheDir() {
        return cacheDir;
    }

    public File getCache() {
        if (cacheDir == null || !new File(cacheDir).exists()) {
            return null;
        }
        return new File(cacheDir);

    }

    public void setCacheDir(String cacheDir) {
        this.cacheDir = cacheDir;
    }


    public void setRepositoryDir(String repositoryDir) {
        this.repositoryDir = repositoryDir;
    }

    public String getRepositoryDir() {
        return repositoryDir;
    }

    public void setUseLocalRepository(boolean useLocalRepository) {
        setUseCache(!useLocalRepository);
    }


    public boolean isUseCache() {
        return useCache;
    }

    public void setUseCache(boolean useCache) {
        this.useCache = useCache;
    }

    public boolean isUseLocalRepository() {
        return !isUseCache();
    }

    public void setTransitive(boolean transitive) {
        this.transitive = transitive;
    }

    public boolean isTransitive() {
        return transitive;
    }

    public String getArtifactPattern() {
        throw new UnsupportedOperationException("The method is not supported yet");
    }

    public void setArtifactPattern(String artifactPattern) {
        throw new UnsupportedOperationException("The method is not supported yet");
    }
}
