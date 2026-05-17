package cn.iocoder.yudao.framework.xss.core.clean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;

/**
 * Implement XSS filtering string based on JSONP
 */
public class JsoupXssCleaner implements XssCleaner {

 private final Safelist safelist;

 /**
     * Used to force conversion to an absolute path when the src attribute uses a relative path. If it is empty, it will not be processed. The value should be the prefix of the absolute path (including the protocol part).
 */
 private final String baseUri;

 /**
     * No parameter construction, by default using the {@link JsoupXssCleaner#buildSafelist} method to build a safe list
 */
 public JsoupXssCleaner() {
 this.safelist = buildSafelist();
 this.baseUri = "";
 }

 /**
     * Build an Xss sanitizing Safelist rule.
     * Based on Safelist#relaxed():
     * 1. The extension supports style and class attributes
     * 2. The a tag additionally supports the target attribute
     * 3. The img tag additionally supports the data protocol to support base64.
 *
 * @return Safelist
 */
 private Safelist buildSafelist() {
        // Use the default provided by jsoup
 Safelist relaxedSafelist = Safelist.relaxed();
        // Some styles are implemented using style when editing rich text.
        // For example, the red font style="color:red;", so you need to add the style attribute to all tags
        // Note: The style attribute has the risk of injection <img STYLE="background-image:url(javascript:alert('XSS'))">
 relaxedSafelist.addAttributes(":all", "style", "class");
        // Keep the target attribute of the a tag
 relaxedSafelist.addAttributes("a", "target");
        // Support img as base64
 relaxedSafelist.addProtocols("img", "src", "data");

        // Retain relative paths. When retaining relative paths, the corresponding baseUri attribute must be provided, otherwise it will still be deleted.
 // WHITELIST.preserveRelativeLinks(false);

        // Remove some protocol restrictions of a tag and img tag, which will cause xss anti-injection to fail, such as <img src=javascript:alert("xss")>
        // Although WhiteList#isSafeAttribute can be rewritten to handle it, there are hidden dangers, so relative paths are not supported for the time being.
 // WHITELIST.removeProtocols("a", "href", "ftp", "http", "https", "mailto");
 // WHITELIST.removeProtocols("img", "src", "http", "https");
 return relaxedSafelist;
 }

 @Override
 public String clean(String html) {
 return Jsoup.clean(html, baseUri, safelist, new Document.OutputSettings().prettyPrint(false));
 }

}

