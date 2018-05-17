package com.test;

import org.apache.log4j.Logger;

import java.io.File;

/**
 * The main entry point of the program.
 */
public class Main {
    private static final Logger LOGGER = Logger.getLogger(ElementFinder.class);

    public static void main(String[] args) {
        if (args.length != 3) {
            LOGGER.error("Exactly 3 parameters must be specified: " +
                    "<origin_file_path> <other_sample_file_path> <target_element_id>");
            return;
        }

        String originalPath = args[0];
        String diffCasePath = args[1];
        String targetElementId = args[2];

        ElementFinder elementFinder = new ElementFinder();
        elementFinder.findAndPrintSimilarElements(new File(originalPath), new File(diffCasePath), targetElementId);
    }
}
