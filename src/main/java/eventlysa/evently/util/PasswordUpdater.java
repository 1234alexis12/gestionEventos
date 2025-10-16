package eventlysa.evently.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PasswordUpdater {
    
    public static void main(String[] args) {
        // Configuración de la base de datos
        String url = "jdbc:mysql://localhost:3306/evently_db";
        String username = "root";
        String password = ""; // Tu contraseña de MySQL
        
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String newPassword = "password123";
        String hashedPassword = encoder.encode(newPassword);
        
        System.out.println("Actualizando contraseñas en la base de datos...");
        System.out.println("Nueva contraseña: " + newPassword);
        System.out.println("Hash BCrypt: " + hashedPassword);
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            // Conectar a la base de datos
            conn = DriverManager.getConnection(url, username, password);
            
            // Obtener todos los usuarios
            String selectSQL = "SELECT id, email FROM usuario";
            pstmt = conn.prepareStatement(selectSQL);
            rs = pstmt.executeQuery();
            
            // Actualizar cada usuario
            String updateSQL = "UPDATE usuario SET password_hash = ? WHERE id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateSQL);
            
            int updatedCount = 0;
            while (rs.next()) {
                Long userId = rs.getLong("id");
                String userEmail = rs.getString("email");
                
                updateStmt.setString(1, hashedPassword);
                updateStmt.setLong(2, userId);
                updateStmt.executeUpdate();
                
                System.out.println("✓ Actualizado: " + userEmail);
                updatedCount++;
            }
            
            System.out.println("\n✅ Se actualizaron " + updatedCount + " usuarios");
            System.out.println("Todos los usuarios ahora tienen la contraseña: " + newPassword);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}