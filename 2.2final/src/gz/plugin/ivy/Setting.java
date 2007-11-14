package gz.plugin.ivy;

import org.jdom.Element;

/**
 * @author <a href="mailto:zgong@naesasoft.com">zgong</a>
 * @version $Id: MailGroup.java,v 1.2 2005/04/26 03:33:23 zgong Exp $
 */public class Setting
{
    private String distPath = "";
    private String artifactPattern = "[organisation]/[module]/[revision]/[artifact].[ext]";
    private static Setting setting;

    private Setting()
    {
    }

    public String getDistPath()
    {
        return distPath;
    }

    public void setDistPath(String distPath)
    {
        this.distPath = distPath;
    }

    public String getArtifactPattern()
    {
        return artifactPattern;
    }

    public void setArtifactPattern(String artifactPattern)
    {
        this.artifactPattern = artifactPattern;
    }

    public static Setting getSetting()
    {
        if (setting == null) {
            setting = new Setting();
        }
        return setting;
    }

    public void read(Element element)
    {
        Element elMark = element.getChild("ivy_dependency_importor");
        if (elMark != null) {
            distPath = elMark.getAttributeValue("dist_lib_path");
            artifactPattern = elMark.getAttributeValue("artifact_pattern");
        }
    }

    public void write(Element element)
    {

        Element elMark = new Element("ivy_dependency_importor");
        if (distPath != null) {
            elMark.setAttribute("dist_lib_path", distPath);
        }
        if (artifactPattern != null) {
            elMark.setAttribute("artifact_pattern", artifactPattern);
        }
        element.addContent(elMark);
    }
}
