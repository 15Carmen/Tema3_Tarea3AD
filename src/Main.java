import java.sql.*;
import java.util.Scanner;

public class Main {

    private static String servidor = "jdbc:mysql://dns11036.phdns11.es";
    private static Connection connection;
    private static Statement st = null;

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        try {
            crearConexion();
            transaccion();
        }catch (SQLException e){
            System.out.println(e.getSQLState());
        }


    }

    private static void crearConexion() {
        connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(servidor + "/ad2223_cmartin", "ad2223_cmartin", "Marnu");

            //mostrarTablas();
            transaccion();

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void mostrarTablas() throws SQLException {
        String sql = "SHOW TABLES";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getString(1));
        }
    }


    public static void transaccion() throws SQLException {
        connection.setAutoCommit(false);	// Se desactiva el AutoCommit para poder realizar la transacción
        Statement st = connection.createStatement();
        String sql;
        sql="drop table if exists cliente;";
        st.execute(sql);	// Se elimina la tabla si existiera
        System.out.println("Elimina la tabla");
        sql="CREATE TABLE cliente(id int primary key, nombre varchar(45));";
        st.execute(sql);	// Se crea la tabla
        System.out.println("Crea la tabla");
        sql="insert into cliente VALUES (1,'UNO');";
        st.executeUpdate(sql);	// Inserta el registro 1
        System.out.println("Inserta registro 1");
        try{
            connection.commit();		// Comienza la transacción
            sql="insert into cliente VALUES (2,'DOS');";
            st.executeUpdate(sql);	// Inserta el registro 2
            System.out.println("Inserta registro 2");
            sql="insert into cliente VALUES (3,'TRES');";
            st.executeUpdate(sql);	// Inserta el registro 3
            System.out.println("Inserta registro 3");
            sql="insert into cliente VALUES (4,'CUATRO');";
            st.executeUpdate(sql);	// Intenta insertar el registro 3 en vez de 4
            System.out.println("No inserta registro al exister el ID 3");
        }catch(SQLException e) {
            connection.rollback();		// Deshace las dos últimas inserciones (2 y 3) ya que la última lanzó el error
        }
        connection.setAutoCommit(true);	// Se vuelve a activar el AutoCommit
    }


}