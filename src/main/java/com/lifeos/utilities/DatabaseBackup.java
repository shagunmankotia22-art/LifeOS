
package com.lifeos.utilities;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatabaseBackup {

    public boolean createBackup() {

        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");

        if (dbUser == null || dbPassword == null) {
            System.out.println("Database credentials are missing.");
            return false;
        }

        try {
            // Create backup folder
            Path backupFolder = Path.of("backups");
            Files.createDirectories(backupFolder);

            // Generate unique backup filename
            String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
            );

            Path backupFile = backupFolder.resolve(
                "lifeos_backup_" + timestamp + ".sql"
            ).toAbsolutePath();

            // Prepare MySQL backup command
            ProcessBuilder pb = new ProcessBuilder(
                "/usr/local/mysql/bin/mysqldump",
                "--single-transaction",
                "--set-gtid-purged=OFF",
                "-u", dbUser,
                "lifeos_db",
                "--result-file=" + backupFile
            );

            // Pass password securely through child process environment
            pb.environment().put("MYSQL_PWD", dbPassword);

            pb.redirectError(ProcessBuilder.Redirect.INHERIT);

            Process process = pb.start();
            int result = process.waitFor();

            if (result == 0 && Files.exists(backupFile)
                    && Files.size(backupFile) > 0) {

                System.out.println("Database backup created successfully!");
                System.out.println("Backup file: " + backupFile);
                return true;
            }

            System.out.println("Database backup failed.");
            return false;

        } catch (Exception e) {
            System.out.println("Backup error: " + e.getMessage());
            return false;
        }
    }
}
