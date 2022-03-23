package be.aui.table;

import com.atlassian.renderer.v2.components.HtmlEscaper;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

public class Util {
    public static final Pattern PARAGRAPH_PATTERN = Pattern.compile("<p>(.*)</p>", 34);

    public static String getHtmlSafeParameter(Map params, String key) {
        Object value = params.get(key);
        if (value instanceof String)
            return HtmlEscaper.escapeAll((String)value, false);
        return null;
    }

    public static String stripFirstParagraphTags(String xml) {
        String trimmed = StringUtils.trim(xml);
        Matcher matcher = PARAGRAPH_PATTERN.matcher(trimmed);
        if (matcher.find() && matcher.end() == trimmed.length() && matcher.start() == 0 && !matcher.group(1).contains("<p>"))
            return matcher.group(1);
        return xml;
    }

    public static String getParameterValue(Map<String, String> parameters, String defaultValue, String... aliases) {
        for (String name : aliases) {
            String value = parameters.get(name);
            if (StringUtils.isNotBlank(value))
                return value.trim();
        }
        return defaultValue;
    }

    public static String sanitizeString(String str) {
        PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.BLOCKS);
        return policy.sanitize(str);
    }
}

