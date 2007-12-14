package gz.plugin.ivy;

import java.io.File;

import org.apache.ivy.core.resolve.ResolveOptions;
import org.apache.ivy.util.FileUtil;
import org.jdom.Element;

/**
 * @author <a href="mailto:zgong@naesasoft.com">zgong</a>
 * @version $Id: MailGroup.java,v 1.2 2005/04/26 03:33:23 zgong Exp $
 */
public class Setting {
    private String distPath = "";
    private String artifactPattern = "[organisation]/[module]/[revision]/[artifact].[ext]";
    private static Setting setting;

    
    private ResolveOptions resolveOptions;
    private String ivySettingFile;
    private String ivyFile;
    private String cacheDir;
    private String repositoryDir;
    private boolean useCache=false;
    private boolean includeSources=false;


    private Setting() {
    }

    public String getDistPath() {
        return distPath;
    }

    public void setDistPath(String distPath) {
        this.distPath = distPath;
    }

    public String getArtifactPattern() {
        return artifactPattern;
    }

    public void setArtifactPattern(String artifactPattern) {
        this.artifactPattern = artifactPattern;
    }

    public static Setting getSetting() {
        if (setting == null) {
            setting = new Setting();
        }
        return setting;
    }

    public void read(Element element) {
        Element elMark = element.getChild("ivy_dependency_importor");
        if (elMark != null) {
            distPath = elMark.getAttributeValue("dist_lib_path");
            artifactPattern = elMark.getAttributeValue("artifact_pattern");
        }
    }

    public void write(Element element) {

        Element elMark = new Element("ivy_dependency_importor");
        if (distPath != null) {
            elMark.setAttribute("dist_lib_path", distPath);
        }
        if (artifactPattern != null) {
            elMark.setAttribute("artifact_pattern", artifactPattern);
        }
        element.addContent(elMark);
    }

    public String getIvySettingFile() {
        return ivySettingFile;
    }

    public String getIvyFile() {
        return ivyFile;
    }

    public ResolveOptions getResolveOptions() {
        resolveOptions = new ResolveOptions();
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
     if(cacheDir==null||!new File(cacheDir).exists()){
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

    private boolean transitive;

    public boolean isTransitive() {
        return transitive;
    }

    public void setIncludeSources(boolean includeSources)
    {
        this.includeSources = includeSources;
    }
}


