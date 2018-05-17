package com.test;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * Finds elements in a given HTML document that are similar to the target element provided in the original HTML document.
 * <p>
 * An element is treated as similar if one of the following is true:
 * <ul>
 *     <li>The id of the current element exactly matches the id of the target element</li>
 *     <li>The text of the current element contains the text of the target element</li>
 *     <li>The class of the current element exactly matches the class of the target element</li>
 *     <li>The title of the current element exactly matches the title of the target element</li>
 * </ul>
 * <p>
 * The priority of each of the method is in accordance with how it is listed above. If one of the method's conditions are met,
 * the other methods are not checked, so that the method with the highest priority wins.
 */
public class ElementFinder {
    private final Logger logger = Logger.getLogger(getClass());

    private static final String CHARSET_NAME = "utf8";
    private static final String TEXT_ATTRIBUTE = "#text";
    private static final String TITLE_ATTRIBUTE = "title";
    private static final String STYLE_ATTRIBUTE = "style";
    private static final String DISPLAY_NONE_REGEX = ".*display:none.*";

    /**
     * Finds the the similar elements and prints those in the log if found.
     *
     * @param original the original HTML document containing the target element
     * @param diffCase the changed HTML document to find the similar elements in
     * @param targetElementId the ID of the element in the original document for which the similar IDs are to be
     *                        found in the changed document
     */
    public void findAndPrintSimilarElements(File original, File diffCase, String targetElementId) {
        requireNonNull(original, "original cannot be null");
        requireNonNull(diffCase, "diffCase cannot be null");
        requireNonNull(targetElementId, "targetElementId cannot be null");

        Document originalDocument = getDocument(original);
        Document diffCaseDocument = getDocument(diffCase);

        Element originalTargetElement = originalDocument.getElementById(targetElementId);
        if (originalTargetElement == null) {
            throw new RuntimeException("Could not find target element [" + targetElementId + "] in ["
                    + original.getAbsolutePath() + "]");
        }

        List<Element> similarElements = getSimilarElements(originalTargetElement, diffCaseDocument);
        List<Element> filteredElements = similarElements.stream().filter(this::isShown).collect(Collectors.toList());

        if (filteredElements.isEmpty()) {
            logger.warn("No similar elements found.");
        } else {
            logger.info(String.format("Found [%s] similar element(s):", filteredElements.size()));
            filteredElements.forEach(this::printElement);
        }
    }

    private void printElement(Element element) {
        Elements parents = element.parents();
        Collections.reverse(parents);
        String path = parents.stream().map(this::describeElement).collect(Collectors.joining(" > "));
        logger.info(String.format("elementDesc = %s; elementPath = %s", describeElement(element), path));
    }

    private String describeElement(Element element) {
        return "[tag = '" + element.tag() + "'" +
                (element.id().isEmpty() ? "" : "; id = '" + element.id() + "'") +
                (element.className().isEmpty() ? "" : "; class = '" + element.className() + "'") + "]";
    }

    private Elements getSimilarElements(Element targetElement, Document document) {
        Elements elements = document.select("*#" + targetElement.id());
        if (!elements.isEmpty()) {
            return elements;
        }

        elements = document.select("a:contains(" + targetElement.childNodes().get(0).attr(TEXT_ATTRIBUTE).trim() + ")");
        if (!elements.isEmpty()) {
            return elements;
        }

        elements = document.select("a[class=" + targetElement.className() + "]");
        if (!elements.isEmpty()) {
            return elements;
        }

        return document.select("a[title=" + targetElement.attr(TITLE_ATTRIBUTE) + "]");
    }

    private boolean isShown(Element element) {
        return !element.attr(STYLE_ATTRIBUTE).matches(DISPLAY_NONE_REGEX);
    }

    private Document getDocument(File resource) {
        try {
            return Jsoup.parse(
                    resource,
                    CHARSET_NAME,
                    resource.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Error reading [" + resource.getAbsolutePath() + "] file");
        }
    }
}
