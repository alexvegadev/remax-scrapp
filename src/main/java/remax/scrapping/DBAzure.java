package remax.scrapping;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import remax.scrapping.models.Alquiler;
import remax.scrapping.models.Venta;

public class DBAzure {
	private static final String INSERT_VENTA = "INSERT INTO dbo.remax_venta(venta_id, lugar, tipo, precio, divisa, ambientes, dormitorios, banios, sup_cubierta, sup_total) VALUES (?,?,?,?,?,?,?,?,?,?)";
	private static final String INSERT_ALQUILER = "INSERT INTO dbo.remax_alquiler(venta_id, lugar, tipo, periodo_pago, precio, divisa, ambientes, dormitorios, banios, sup_cubierta, sup_total) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
	
	
	public static Connection get() {
		Connection con =  null;
        try {
			con = DriverManager.getConnection("jdbc:sqlserver://hackaton-magios.database.windows.net:1433;database=bi-magios;user=losmagiospractia@hackaton-magios;password={Magiospractia_};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return con;		
	}
	
	public static boolean insertVenta(Venta data) {
		try(Connection con = get(); PreparedStatement ps = con.prepareStatement(INSERT_VENTA)){
			ps.setString(1, data.getVentaId());
			ps.setString(2, data.getLugar());
			ps.setString(3, data.getTipo());
			ps.setDouble(4, data.getPrecio());
			ps.setString(5, data.getDivisa());
			ps.setInt(6, data.getAmbientes());
			ps.setInt(7, data.getDormitorios());
			ps.setInt(8, data.getBanios());
			ps.setDouble(9, data.getSupCubierta());
			ps.setDouble(10, data.getSupTotal());
			return ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean insertAlquiler(Alquiler data) {
		try(Connection con = get(); PreparedStatement ps = con.prepareStatement(INSERT_ALQUILER)){
			ps.setString(1, data.getVentaId());
			ps.setString(2, data.getLugar());
			ps.setString(3, data.getTipo());
			ps.setString(4, data.getPeriodo());
			ps.setDouble(5, data.getPrecio());
			ps.setString(6, data.getDivisa());
			ps.setInt(7, data.getAmbientes());
			ps.setInt(8, data.getDormitorios());
			ps.setInt(9, data.getBanios());
			ps.setDouble(10, data.getSupCubierta());
			ps.setDouble(11, data.getSupTotal());
			return ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
