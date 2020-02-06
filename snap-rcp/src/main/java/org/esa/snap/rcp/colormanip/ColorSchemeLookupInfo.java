package org.esa.snap.rcp.colormanip;

import org.esa.snap.core.datamodel.ColorSchemeInfo;
import org.esa.snap.ui.product.ProductSceneView;

import java.util.ArrayList;


/**
 * Contains all info for a color scheme lookup
 *
 * @author Daniel Knowles (NASA)
 * @date Jan 2020
 */

public class ColorSchemeLookupInfo {

    private String regex;
    private String[] regexArray; // implement later
    private String scheme_id;
    private ColorSchemeInfo colorSchemeInfo;
    private String description;


    public ColorSchemeLookupInfo(String regex, String scheme_id, String description, ColorSchemeInfo colorSchemeInfo) {

        if (regex != null && scheme_id != null) {
            setRegex(regex);
            setScheme_id(scheme_id);
            setDescription(description);
            setColorSchemeInfo(colorSchemeInfo);
            setRegexArray(null);
        }
    }


    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String[] getRegexArray() {
        return regexArray;
    }

    public void setRegexArray(String[] regexArray) {
        this.regexArray = regexArray;
    }

    public String getScheme_id() {
        return scheme_id;
    }

    public void setScheme_id(String scheme_id) {
        this.scheme_id = scheme_id;
    }

    public ColorSchemeInfo getColorSchemeInfo() {
        return colorSchemeInfo;
    }

    public void setColorSchemeInfo(ColorSchemeInfo colorSchemeInfo) {
        this.colorSchemeInfo = colorSchemeInfo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public boolean isMatch(String bandName) {

        boolean match = false;

        if (bandName == null || bandName.length() == 0) { return false; }

        final String WILDCARD = new String("*");

        String regex = getRegex().trim();

        if (bandName.equals(regex)) {
            match = true;
        } else if (regex.contains(WILDCARD)) {
            if (!regex.startsWith(WILDCARD) && regex.endsWith(WILDCARD)) {
                String basename = regex.substring(0, regex.length() - 1);
                if (bandName.startsWith(basename)) {
                    match = true;
                }
            } else if (regex.startsWith(WILDCARD) && !regex.endsWith(WILDCARD)) {
                String basename = regex.substring(1, regex.length());
                if (bandName.endsWith(basename)) {
                    match = true;
                }
            } else if (regex.startsWith(WILDCARD) && regex.endsWith(WILDCARD)) {
                String basename = regex.substring(1, regex.length() - 1);
                if (bandName.contains(basename)) {
                    match = true;
                }
            } else {
                String basename = regex;
                String wildcard = "\\*";
                String basenameSplit[] = basename.split(wildcard);
                if (basenameSplit.length == 2 && basenameSplit[0].length() > 0 && basenameSplit[1].length() > 0) {
                    if (bandName.startsWith(basenameSplit[0]) && bandName.endsWith(basenameSplit[1])) {
                        match = true;
                    }
                }
            }
        }

        return match;
    }
}

