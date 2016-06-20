package org.bscl.common.web.html;


import com.google.common.base.Charsets;
import com.google.common.base.Strings;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for HTML escaping. Escapes and unescapes
 * based on the W3C HTML 4.01 recommendation, handling
 * character entity references.
 * <p/>
 * <p>Reference:
 * <a href="http://www.w3.org/TR/html4/charset.html">http://www.w3.org/TR/html4/charset.html</a>
 * <p/>
 * <p>For a comprehensive set of String escaping utilities,
 * consider Jakarta Commons Lang and its StringEscapeUtils class.
 * We are not using that class here to avoid a runtime dependency
 * on Commons Lang just for HTML escaping. Furthermore, Spring's
 * HTML escaping is more flexible and 100% HTML 4.0 compliant.
 *
 * @author Juergen Hoeller
 * @author Martin Kersten
 * @see org.apache.commons.lang.StringEscapeUtils
 * @since 01.03.2003
 */
public class HtmlUtils {

    private static final String REGULAR_HTML_TAG = "<([^>]*)>";
    private static final String REGULAR_SRCATR_VALUE = "<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";

    private HtmlUtils() {
    }

    /**
     * Shared instance of pre-parsed HTML character entity references.
     */
    private static final HtmlCharacterEntityReferences characterEntityReferences = new HtmlCharacterEntityReferences();


    /**
     * Turn special characters into HTML character references.
     * Handles complete character set defined in HTML 4.01 recommendation.
     * <p>Escapes all special characters to their corresponding
     * entity reference (e.g. {@code &lt;}).
     * <p>Reference:
     * <a href="http://www.w3.org/TR/html4/sgml/entities.html">
     * http://www.w3.org/TR/html4/sgml/entities.html
     * </a>
     *
     * @param input the (unescaped) input string
     * @return the escaped string
     */
    public static String htmlEscape(String input) {
        if (input == null) {
            return null;
        }
        StringBuilder escaped = new StringBuilder(input.length() * 2);
        for (int i = 0; i < input.length(); i++) {
            char character = input.charAt(i);
            String reference = characterEntityReferences.convertToReference(character);
            if (reference != null) {
                escaped.append(reference);
            } else {
                escaped.append(character);
            }
        }
        return escaped.toString();
    }

    /**
     * Turn special characters into HTML character references.
     * Handles complete character set defined in HTML 4.01 recommendation.
     * <p>Escapes all special characters to their corresponding numeric
     * reference in decimal format (&#<i>Decimal</i>;).
     * <p>Reference:
     * <a href="http://www.w3.org/TR/html4/sgml/entities.html">
     * http://www.w3.org/TR/html4/sgml/entities.html
     * </a>
     *
     * @param input the (unescaped) input string
     * @return the escaped string
     */
    public static String htmlEscapeDecimal(String input) {
        if (input == null) {
            return null;
        }
        StringBuilder escaped = new StringBuilder(input.length() * 2);
        for (int i = 0; i < input.length(); i++) {
            char character = input.charAt(i);
            if (characterEntityReferences.isMappedToReference(character)) {
                escaped.append(HtmlCharacterEntityReferences.DECIMAL_REFERENCE_START);
                escaped.append((int) character);
                escaped.append(HtmlCharacterEntityReferences.REFERENCE_END);
            } else {
                escaped.append(character);
            }
        }
        return escaped.toString();
    }

    /**
     * Turn special characters into HTML character references.
     * Handles complete character set defined in HTML 4.01 recommendation.
     * <p>Escapes all special characters to their corresponding numeric
     * reference in hex format (&#x<i>Hex</i>;).
     * <p>Reference:
     * <a href="http://www.w3.org/TR/html4/sgml/entities.html">
     * http://www.w3.org/TR/html4/sgml/entities.html
     * </a>
     *
     * @param input the (unescaped) input string
     * @return the escaped string
     */
    public static String htmlEscapeHex(String input) {
        if (input == null) {
            return null;
        }
        StringBuilder escaped = new StringBuilder(input.length() * 2);
        for (int i = 0; i < input.length(); i++) {
            char character = input.charAt(i);
            if (characterEntityReferences.isMappedToReference(character)) {
                escaped.append(HtmlCharacterEntityReferences.HEX_REFERENCE_START);
                escaped.append(Integer.toString(character, 16));
                escaped.append(HtmlCharacterEntityReferences.REFERENCE_END);
            } else {
                escaped.append(character);
            }
        }
        return escaped.toString();
    }

    /**
     * Turn HTML character references into their plain text UNICODE equivalent.
     * <p>Handles complete character set defined in HTML 4.01 recommendation
     * and all reference types (decimal, hex, and entity).
     * <p>Correctly converts the following formats:
     * <blockquote>
     * &amp;#<i>Entity</i>; - <i>(Example: &amp;amp;) case sensitive</i>
     * &amp;#<i>Decimal</i>; - <i>(Example: &amp;#68;)</i><br>
     * &amp;#x<i>Hex</i>; - <i>(Example: &amp;#xE5;) case insensitive</i><br>
     * </blockquote>
     * Gracefully handles malformed character references by copying original
     * characters as is when encountered.<p>
     * <p>Reference:
     * <a href="http://www.w3.org/TR/html4/sgml/entities.html">
     * http://www.w3.org/TR/html4/sgml/entities.html
     * </a>
     *
     * @param input the (escaped) input string
     * @return the unescaped string
     */
    public static String htmlUnescape(String input) {
        if (input == null) {
            return null;
        }
        return new HtmlCharacterEntityDecoder(characterEntityReferences, input).decode();
    }


    public static String removeAllHtmlTag(String str) {
        Pattern pattern = Pattern.compile(REGULAR_HTML_TAG);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result1 = matcher.find();
        while (result1) {
            matcher.appendReplacement(sb, "");
            result1 = matcher.find();
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static String filterHtmlTag(String str, String tag) {
        String regxp = "<\\s*" + tag + "\\s*+([^>]*)\\s*>|</" + tag + ">";
        Pattern pattern = Pattern.compile(regxp);
        Matcher matcher = pattern.matcher(str);

        return matcher.replaceAll("");
    }

    public static String filterHtmlTag(String str, String[] tags) {
        String ret = str;
        for (String tag : tags) {
            ret = filterHtmlTag(ret, tag);
        }
        return ret;
    }

    public static String replaceHtmlTag(String str, String beforeTag, String tagAttrib, String startTag, String endTag) {
        String regxpForTag = "<\\s*" + beforeTag + "\\s+([^>]*)\\s*>";
        String regxpForTagAttrib = tagAttrib + "=\"([^\"]+)\"";
        Pattern patternForTag = Pattern.compile(regxpForTag);
        Pattern patternForAttrib = Pattern.compile(regxpForTagAttrib);
        Matcher matcherForTag = patternForTag.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result = matcherForTag.find();
        while (result) {
            StringBuffer sbreplace = new StringBuffer();
            Matcher matcherForAttrib = patternForAttrib.matcher(matcherForTag.group(1));
            if (matcherForAttrib.find()) {
                matcherForAttrib.appendReplacement(sbreplace, startTag + matcherForAttrib.group(1) + endTag);
            }
            matcherForTag.appendReplacement(sb, sbreplace.toString());
            result = matcherForTag.find();
        }
        matcherForTag.appendTail(sb);
        return sb.toString();
    }

    public static String getImgSrcAtrVal(String input) {
        Pattern p = Pattern.compile(REGULAR_SRCATR_VALUE);
        Matcher matcher = p.matcher(input);
        String retVal = null;
        boolean result = matcher.find();
        while (result) {
            retVal = matcher.group(1);
            result = matcher.find();
        }
        return retVal;
    }

    public static List<String> getImgSrcAtrVals(String str) {
        Pattern p = Pattern.compile(REGULAR_SRCATR_VALUE);
        Matcher matcher = p.matcher(str);
        List<String> list = new ArrayList<String>();
        boolean flag = matcher.find();

        while (flag) {
            list.add(matcher.group(1));
            flag = matcher.find();
        }
        return list;
    }

    public static String interceptLen(String str, int len) {
        str = str.trim();
        int byteLen = 0;
        if (Strings.isNullOrEmpty(str))
            return "";

        byteLen = str.getBytes().length;
        if (byteLen < len)
            return str;

        for (int i = 0; i < len; i++) {
            String tmpStr = str.substring(i, i + 1);
            if (tmpStr.getBytes(Charsets.UTF_8).length >= 2)
                len = len - 1;
        }
        return str.substring(0, len);
    }

    public static String removeStyleTag(String str) {
        if (Strings.isNullOrEmpty(str)) {
            throw new InvalidParameterException("parameter is invalid");
        }
        String rex = "style=\"\\s*\\S*\"";
        String ret = "";
        Pattern p = Pattern.compile(rex);
        Matcher m = p.matcher(str);
        ret = m.replaceAll("");
        return ret;
    }
}