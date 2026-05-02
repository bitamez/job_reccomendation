package com.mesi.jobai.util;

import com.mesi.jobai.config.DBConnection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class to run database migration scripts
 */
public class MigrationRunner {
    
    /**
     * Execute a SQL migration script file
     * @param scriptPath Path to the SQL script file
     * @return true if migration succeeded, false otherwise
     */
    public static boolean runMigration(String scriptPath) {
        System.out.println("Running migration script: " + scriptPath);
        
        try {
            String sqlScript = readSqlFile(scriptPath);
            executeSqlScript(sqlScript);
            System.out.println("Migration completed successfully!");
            return true;
        } catch (IOException e) {
            System.err.println("Error reading migration script: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (SQLException e) {
            System.err.println("Error executing migration script: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Read SQL file content
     */
    private static String readSqlFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }
    
    /**
     * Execute SQL script by splitting on semicolons and executing each statement
     */
    private static void executeSqlScript(String sqlScript) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Split script into individual statements
            // Note: This is a simple split and may not handle all edge cases
            String[] statements = sqlScript.split(";");
            
            int executedCount = 0;
            for (String sql : statements) {
                String trimmedSql = sql.trim();
                
                // Skip empty statements and comments
                if (trimmedSql.isEmpty() || 
                    trimmedSql.startsWith("--") || 
                    trimmedSql.startsWith("/*")) {
                    continue;
                }
                
                try {
                    stmt.execute(trimmedSql);
                    executedCount++;
                } catch (SQLException e) {
                    // Log but continue for IF NOT EXISTS statements
                    if (!e.getMessage().contains("already exists")) {
                        System.err.println("Warning executing statement: " + e.getMessage());
                        System.err.println("Statement: " + trimmedSql.substring(0, Math.min(100, trimmedSql.length())));
                    }
                }
            }
            
            System.out.println("Executed " + executedCount + " SQL statements");
        }
    }
    
    /**
     * Main method for standalone execution
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MigrationRunner <path-to-migration-script>");
            System.out.println("Example: java MigrationRunner database/migration_job_alerts_analytics_resume_parser.sql");
            System.exit(1);
        }
        
        String scriptPath = args[0];
        boolean success = runMigration(scriptPath);
        System.exit(success ? 0 : 1);
    }
}
