package com.example.classicjeans.util;

import static com.example.classicjeans.util.RegexPatterns.URL_PATTERN;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;

public class MarkdownRenderer {

    public static String convertMarkdownToHtml(String markdownText) {
        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(parser.parse(markdownText));
    }

    // 출처 없이 출력하는 Markdown 언어
    public static String convertMarkdownToHtmlWithoutSource(String htmlText) {
        htmlText = htmlText.replace(URL_PATTERN, "");
        return convertMarkdownToHtml(htmlText);
    }
}
