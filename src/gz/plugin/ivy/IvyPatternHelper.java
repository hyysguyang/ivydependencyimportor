/*
 * This file is subject to the license found in LICENCE.TXT in the root directory of the project.
 *
 * version 1.1
 */
package gz.plugin.ivy;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author x.hanin
 */
public class IvyPatternHelper
{
    public static final String CONF_KEY = "conf";
    public static final String TYPE_KEY = "type";
    public static final String EXT_KEY = "ext";
    public static final String ARTIFACT_KEY = "artifact";
    public static final String REVISION_KEY = "revision";
    public static final String MODULE_KEY = "module";
    public static final String ORGANISATION_KEY = "organisation";

    private static final Pattern VAR_PATTERN = Pattern.compile("\\$\\{(.*?)\\}");
    private static final Pattern TOKEN_PATTERN = Pattern.compile("\\[(.*?)\\]");


    public static String substitute(String pattern, String org, String module, String revision, String artifact,
                                    String type, String ext)
    {
        return substitute(pattern, org, module, revision, artifact, type, ext, null);
    }

    public static String substitute(String pattern, String org, String module, String revision, String artifact,
                                    String type, String ext, String conf)
    {
        Map<String, String> tokens = new HashMap<String, String>();
        tokens.put(ORGANISATION_KEY, org == null ? "" : org);
        tokens.put(MODULE_KEY, module == null ? "" : module);
        tokens.put(REVISION_KEY, revision == null ? "" : revision);
        tokens.put(ARTIFACT_KEY, artifact == null ? module : artifact);
        tokens.put(TYPE_KEY, type == null ? "jar" : type);
        tokens.put(EXT_KEY, ext == null ? "jar" : ext);
        tokens.put(CONF_KEY, conf == null ? "default" : conf);
        return substituteTokens(pattern, tokens);
    }

    public static String substitute(String pattern, Map<String, String> variables, Map<String, String> tokens)
    {
        return substituteTokens(substituteVariables(pattern, variables), tokens);
    }

    public static String substituteVariables(String pattern, Map<String, String> variables)
    {
        // if you supply null, null is what you get
        if (pattern == null) {
            return null;
        }

        Matcher m = VAR_PATTERN.matcher(pattern);

        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String var = m.group(1);
            String val = variables.get(var);
            if (val != null) {
                val = substituteVariables(val, variables);
            }
            else {
                val = m.group();
            }
            m.appendReplacement(sb, val.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$"));
        }
        m.appendTail(sb);

        return sb.toString();
    }

    public static String substituteTokens(String pattern, Map<String, String> tokens)
    {
        Matcher m = TOKEN_PATTERN.matcher(pattern);

        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String var = m.group(1);
            String val = tokens.get(var);
            if (val == null) {
                val = m.group();
            }
            m.appendReplacement(sb, val.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$"));
        }
        m.appendTail(sb);

        return sb.toString();
    }

    public static String substituteVariable(String pattern, String variable, String value)
    {
        StringBuffer buf = new StringBuffer(pattern);
        substituteVariable(buf, variable, value);
        return buf.toString();
    }

    public static void substituteVariable(StringBuffer buf, String variable, String value)
    {
        String from = "${" + variable + "}";
        int fromLength = from.length();
        for (int index = buf.indexOf(from); index != -1; index = buf.indexOf(from, index)) {
            buf.replace(index, index + fromLength, value);
        }
    }

    public static String substituteToken(String pattern, String token, String value)
    {
        StringBuffer buf = new StringBuffer(pattern);
        substituteToken(buf, token, value);
        return buf.toString();
    }

    public static void substituteToken(StringBuffer buf, String token, String value)
    {
        String from = getTokenString(token);
        int fromLength = from.length();
        for (int index = buf.indexOf(from); index != -1; index = buf.indexOf(from, index)) {
            buf.replace(index, index + fromLength, value);
        }
    }

    public static String getTokenString(String token)
    {
        return "[" + token + "]";
    }

    public static void main(String[] args)
    {
//        String pattern = "[organisation]/[module]/build/archives/[type]s/[artifact]-[revision].[ext]";
        String pattern = "d:/[organisation]/[module]/[revision]-[artifact].[ext]";
        Log.log("pattern= " + pattern);
        Log.log("resolved= " + substitute(pattern, "jayasoft", "Test", "1.0", "test", "jar", "jar"));

        Map<String, String> variables = new HashMap<String, String>();
        variables.put("test", "mytest");
        variables.put("test2", "${test}2");
        pattern = "${test} ${test2} ${nothing}";
        Log.log("pattern= " + pattern);
        Log.log("resolved= " + substituteVariables(pattern, variables));

        Log.log("=============================================");
        variables.put("dist.libs", "d:/libs");

    }

}
