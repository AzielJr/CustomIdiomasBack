import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

public class FixFotoColumn {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/customidiomas?useSSL=false&serverTimezone=UTC";
        String username = "banco";
        String password = "geral";
        
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            
            System.out.println("Conectado ao banco de dados com sucesso!");
            
            // Verificar estrutura atual
            System.out.println("\nEstrutura atual da tabela usuarios:");
            ResultSet rs = statement.executeQuery("DESCRIBE usuarios");
            while (rs.next()) {
                if ("foto".equals(rs.getString("Field"))) {
                    System.out.println("Campo foto: " + rs.getString("Type"));
                }
            }
            rs.close();
            
            // Alterar coluna foto para LONGBLOB
            System.out.println("\nAlterando coluna foto para LONGBLOB...");
            statement.executeUpdate("ALTER TABLE usuarios MODIFY COLUMN foto LONGBLOB");
            System.out.println("Coluna alterada com sucesso!");
            
            // Verificar estrutura após alteração
            System.out.println("\nEstrutura após alteração:");
            rs = statement.executeQuery("DESCRIBE usuarios");
            while (rs.next()) {
                if ("foto".equals(rs.getString("Field"))) {
                    System.out.println("Campo foto: " + rs.getString("Type"));
                }
            }
            rs.close();
            
            statement.close();
            connection.close();
            System.out.println("\nConexão fechada. Alteração concluída!");
            
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
}