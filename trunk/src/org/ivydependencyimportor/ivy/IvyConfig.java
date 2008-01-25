/**
 * Copyright(c) 2005 Ceno Techonologies, Ltd.
 *
 * History:
 *   2008-1-3 16:45:25 Created by ygu
 */
package org.ivydependencyimportor.ivy;

import org.apache.ivy.core.resolve.ResolveOptions;
import org.apache.ivy.util.Message;

import java.io.File;
import java.net.URL;

/**
 * Ivy setting config.
 * Those config may be come from IDE.
 *
 * @author <a href="mailto:hyysguyang@gmail.com">Numen</a>
 * @version 1.0 2008-1-3 16:45:25
 */
public class IvyConfig {
    private static final String REPOSITORY_DIR_VARIABLE  = "ivy.repository.dir";
    private static final String ARTIFACT_PATTERN_VARIABLE = "artifact_pattern";
    private static final String IVY_PATTERN_VARIABLE = "ivy_pattern";
    private static final String IVY_CACHE_VARIABLE = "ivy.cache.dir";

    private static final String DEFAULT_ARTIFACT_PATTERN = "[organisation]/[module]/[type]s/[artifact]-[revision].[ext]";
    private static final String DEFAULT_IVY_PATTERN = "[organisation]/[module]/ivys/ivy-[revision].xml";
    private static final String DEFAULT_IVY_SETTING_FILE_NAME = "ivysettings.xml";

    private String ivySettingFile;
    private String ivyFile;
    private boolean useCache = false;
    private boolean transitive;

    //Set default varable value
    {
        setArtifactPattern(null);
        setIvyPattern(null);
    }


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

        if(isEmpty(ivysettings)) {
            Message.debug("Invalid ivy settiong file path: ivy settiong file path must not be empty,use the default ivy settiong file of ivydependdencyimpotor.");
            this.ivySettingFile = getDefaultIvySetting();
            Message.debug("Default ivy pattern: "+ivySettingFile);
        }

        this.ivySettingFile = ivysettings;
    }

    public void setIvyFile(String ivyFile) {
        this.ivyFile = ivyFile;
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

    public void setArtifactPattern(String artifactPattern) {
        if(isEmpty(artifactPattern)) {
            Message.debug("Invalid artifact pattern: artifact pattern must not be empty,use the default artifact pattern");
            Message.debug("Default artifact pattern: "+DEFAULT_ARTIFACT_PATTERN);
            System.setProperty(ARTIFACT_PATTERN_VARIABLE,DEFAULT_ARTIFACT_PATTERN);
        } else{
            System.setProperty(ARTIFACT_PATTERN_VARIABLE,artifactPattern);
        }
    }


    private boolean isEmpty(String string) {
        return string==null||"".equals(string);
    }

    public void setIvyPattern(String ivyPattern) {
        if(isEmpty(ivyPattern)) {
            Message.debug("Invalid ivy pattern: ivy pattern must not be empty,use the default ivy pattern");
            Message.debug("Default ivy pattern: "+DEFAULT_IVY_PATTERN);
            System.setProperty(IVY_PATTERN_VARIABLE,DEFAULT_IVY_PATTERN);
        } else{
            System.setProperty(IVY_PATTERN_VARIABLE,ivyPattern);
        }

    }

    public void setRepositoryDir(String repositoryDir) {
        if(isEmpty(repositoryDir)) {
            throw new RuntimeException("Invalid repository dir:repository path must not be empty.");
        }
        System.setProperty(REPOSITORY_DIR_VARIABLE,repositoryDir);
    }

    public void setCacheDir(String cacheDir) {
        if(isEmpty(cacheDir)) {
            Message.debug("Invalid cache directory: cache path must not be empty,use the default ivy cache");
            return ;
        }
        System.setProperty(IVY_CACHE_VARIABLE,cacheDir);
    }



    public String getDefaultIvySetting() {
        // TODO: give another more elegant solution
        // FIXME:We will put our default ivy setting file in some directory because the ivy setting file may be edit
        //  It's disappoint that we can't put the file in our jar because we using it，so we put the file in the
        //  parent directory of lib that the jar locating. current the directory is as bellow.
        //    basedir/lib/*.jar (include ivydependencyimportor-idea.jar)
        //    basedir/conf/ivysettings.xml
        //  this is a very bad solution,shall we give another more elegant solution?
        URL currentClassFileResource= this.getClass().getResource("org/ivydependencyimportor/ivy/IvyConfig.class");
              if(currentClassFileResource==null)  {
                  throw new RuntimeException("The core jar of the project is not found,please give me the jar to work.");
              }
        String jarFileLocationBaseDir=currentClassFileResource.getPath().substring(0,currentClassFileResource.getPath().indexOf("/lib/"));
        return jarFileLocationBaseDir+ File.separator+"conf"+File.separator+DEFAULT_IVY_SETTING_FILE_NAME;
    }
}
