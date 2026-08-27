package com.WebExcersise.config;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class UploadConfig {
    public static final Path UPLOAD_DIR = Paths.get(System.getProperty("user.home"), "WebExcerciseUploads");

    private UploadConfig() {
    }
}
