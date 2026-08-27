package com.WebExcersise.util;

import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;

public final class FileUploadUtil {
    private static final Set<String> IMAGE_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "webp");

    private FileUploadUtil() {
    }

    public static String saveImage(Part part, Path uploadDir) throws IOException {
        if (part == null || part.getSize() == 0 || part.getSubmittedFileName() == null || part.getSubmittedFileName().isBlank()) {
            return null;
        }

        String submittedFileName = Path.of(part.getSubmittedFileName()).getFileName().toString();
        String extension = getExtension(submittedFileName);
        if (!IMAGE_EXTENSIONS.contains(extension)) {
            throw new IOException("Chi chap nhan file anh: jpg, jpeg, png, gif, webp");
        }

        Files.createDirectories(uploadDir);
        String fileName = System.currentTimeMillis() + "." + extension;
        try (InputStream inputStream = part.getInputStream()) {
            Files.copy(inputStream, uploadDir.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        }
        return fileName;
    }

    public static void deleteLocalImage(String imageName, Path uploadDir) throws IOException {
        if (imageName == null || imageName.isBlank() || isRemoteImage(imageName)) {
            return;
        }

        Path filePath = uploadDir.resolve(imageName).normalize();
        if (filePath.startsWith(uploadDir.normalize()) && Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }

    public static boolean isRemoteImage(String imageName) {
        String value = imageName == null ? "" : imageName.toLowerCase(Locale.ROOT);
        return value.startsWith("http://") || value.startsWith("https://");
    }

    private static String getExtension(String fileName) throws IOException {
        int index = fileName.lastIndexOf('.');
        if (index < 0 || index == fileName.length() - 1) {
            throw new IOException("File anh phai co phan mo rong");
        }
        return fileName.substring(index + 1).toLowerCase(Locale.ROOT);
    }
}
