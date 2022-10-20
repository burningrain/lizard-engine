package com.github.burningrain.lizard.editor.ui.utils;

public final class FileUtils {

    private FileUtils() {
    }

    public static String getExtensions(String fileName) {
        if (fileName == null || !fileName.contains(".")) return null;
        return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    }


}
