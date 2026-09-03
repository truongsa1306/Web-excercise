package com.WebExcersise.service;

import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Base64;

public class EmailService {
    private static final Path DEV_MAIL_LOG = Path.of("target", "otp-mail.log");

    public void sendOtp(String to, String subject, String otp) {
        String host = configured("app.mail.host", "APP_MAIL_HOST");
        if (host == null || host.isBlank()) {
            writeDevMail(to, subject, otp);
            return;
        }

        try {
            sendSmtp(host, to, subject, otp);
        } catch (IOException exception) {
            writeDevMail(to, subject + " (SMTP failed: " + exception.getMessage() + ")", otp);
        }
    }

    private void sendSmtp(String host, String to, String subject, String otp) throws IOException {
        int port = Integer.parseInt(defaultValue(configured("app.mail.port", "APP_MAIL_PORT"), "465"));
        String username = configured("app.mail.username", "APP_MAIL_USERNAME");
        String password = configured("app.mail.password", "APP_MAIL_PASSWORD");
        String from = defaultValue(configured("app.mail.from", "APP_MAIL_FROM"), username);
        boolean ssl = Boolean.parseBoolean(defaultValue(configured("app.mail.ssl", "APP_MAIL_SSL"), "true"));

        try (Socket socket = ssl
                ? SSLSocketFactory.getDefault().createSocket(host, port)
                : new Socket(host, port)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

            expect(reader, 220);
            command(reader, writer, "EHLO localhost", 250);
            if (username != null && !username.isBlank()) {
                command(reader, writer, "AUTH LOGIN", 334);
                command(reader, writer, Base64.getEncoder().encodeToString(username.getBytes(StandardCharsets.UTF_8)), 334);
                command(reader, writer, Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8)), 235);
            }
            command(reader, writer, "MAIL FROM:<" + from + ">", 250);
            command(reader, writer, "RCPT TO:<" + to + ">", 250);
            command(reader, writer, "DATA", 354);
            writer.write("From: " + from + "\r\n");
            writer.write("To: " + to + "\r\n");
            writer.write("Subject: " + subject + "\r\n");
            writer.write("Content-Type: text/plain; charset=UTF-8\r\n\r\n");
            writer.write("Ma OTP cua ban la: " + otp + "\r\n");
            writer.write("Ma co hieu luc trong 5 phut.\r\n.\r\n");
            writer.flush();
            expect(reader, 250);
            command(reader, writer, "QUIT", 221);
        }
    }

    private void command(BufferedReader reader, BufferedWriter writer, String command, int expectedCode) throws IOException {
        writer.write(command + "\r\n");
        writer.flush();
        expect(reader, expectedCode);
    }

    private void expect(BufferedReader reader, int expectedCode) throws IOException {
        String line = reader.readLine();
        if (line == null || !line.startsWith(String.valueOf(expectedCode))) {
            throw new IOException("SMTP expected " + expectedCode + " but got " + line);
        }
        while (line.length() > 3 && line.charAt(3) == '-') {
            line = reader.readLine();
            if (line == null) {
                break;
            }
        }
    }

    private void writeDevMail(String to, String subject, String otp) {
        try {
            Files.createDirectories(DEV_MAIL_LOG.getParent());
            String message = LocalDateTime.now() + " | to=" + to + " | " + subject + " | OTP=" + otp + System.lineSeparator();
            Files.writeString(DEV_MAIL_LOG, message, StandardCharsets.UTF_8,
                    Files.exists(DEV_MAIL_LOG) ? java.nio.file.StandardOpenOption.APPEND : java.nio.file.StandardOpenOption.CREATE);
            System.out.println(message);
        } catch (IOException exception) {
            System.out.println("OTP for " + to + ": " + otp);
        }
    }

    private String configured(String systemKey, String envKey) {
        String value = System.getProperty(systemKey);
        if (value == null || value.isBlank()) {
            value = System.getenv(envKey);
        }
        return value;
    }

    private String defaultValue(String value, String defaultValue) {
        return value == null || value.isBlank() ? defaultValue : value;
    }
}
