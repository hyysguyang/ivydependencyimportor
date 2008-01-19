package org.ivydependencyimportor.ivy.idea.setting;

/**
 * Settings will be saved.
 *
 * @author <a href="mailto:gzkaneg@gmail.com">gzkaneg</a>
 */
public class Setting {
    public String distPath = "";
    public String artifactPattern = "[organisation]/[module]/[revision]/[artifact].[ext]";
    public boolean isTransitive;

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

    public String toString() {
        return "{distPath=" + distPath + ",artifactPattern=" + artifactPattern + "}";
    }

    public boolean isTransitive() {
        return isTransitive;
    }

    public void setTransitive(boolean transitive) {
        isTransitive = transitive;
    }
}