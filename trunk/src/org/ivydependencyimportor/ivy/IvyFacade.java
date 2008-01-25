/**
 *
 */
package org.ivydependencyimportor.ivy;

import org.apache.ivy.Ivy;
import org.apache.ivy.core.cache.CacheManager;
import org.apache.ivy.core.module.descriptor.Artifact;
import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.core.report.ResolveReport;
import org.apache.ivy.core.resolve.ResolveOptions;
import org.apache.ivy.plugins.parser.ModuleDescriptorParser;
import org.apache.ivy.plugins.parser.ModuleDescriptorParserRegistry;
import org.apache.ivy.plugins.repository.url.URLResource;
import org.apache.ivy.util.Message;

import java.io.File;
import java.net.URL;
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
        CacheManager cacheManager = ivy.getCacheManager(ivy.getSettings().getDefaultCache());
        for (Artifact artifact : arti) {
            list.add(cacheManager.getArchiveFileInCache(artifact).getAbsolutePath());
        }
        return list;
    }

    private List<Artifact> getArtifacts() {
        return (List<Artifact>) resolveReport.getArtifacts();
    }

    public void setIvyConfig(IvyConfig ivyConfig) {
        this.ivyConfig = ivyConfig;
    }


    public void resolve() throws Exception {
        ivy = Ivy.newInstance();
        ivy.configure(new File(ivyConfig.getIvySettingFile()));
        URL ivySource = new File(ivyConfig.getIvyFile()).toURL();
        ResolveOptions options = ivyConfig.getResolveOptions();
        ModuleDescriptor md = parseModuleDescriptor(ivySource, options);
        resolveReport = ivy.resolve(md, options);
    }

    private ModuleDescriptor parseModuleDescriptor(URL ivySource, ResolveOptions options)
            throws Exception {
        URLResource res = new URLResource(ivySource);
        ModuleDescriptorParser parser = ModuleDescriptorParserRegistry.getInstance().getParser(res);
        Message.verbose("using " + parser + " to parse " + ivySource);
        ModuleDescriptor md = parser.parseDescriptor(ivy.getSettings(), ivySource, options.isValidate());
        String revision = options.getRevision();
        if (revision == null &&
                md.getResolvedModuleRevisionId().getRevision() == null) {
            revision = Ivy.getWorkingRevision();
        }
        if (revision != null) {
            md.setResolvedModuleRevisionId(ModuleRevisionId.newInstance(md.getModuleRevisionId(),revision));
        }
        return md;
    }


}
