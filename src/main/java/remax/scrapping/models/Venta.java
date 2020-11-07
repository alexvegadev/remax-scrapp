package remax.scrapping.models;

public class Venta {
	private String venta_id;
	private String lugar;
	private String tipo;
	private double precio;
	private String divisa;
	private int ambientes;
	private int dormitorios;
	private int banios;
	private double supCubierta;
	private double supTotal;
	
	public Venta(String venta_id, String lugar, String tipo, double precio, String divisa, int ambientes,
			int dormitorios, int banios, double supCubierta, double supTotal) {
		this.venta_id = venta_id;
		this.lugar = lugar;
		this.tipo = tipo;
		this.precio = precio;
		this.divisa = divisa;
		this.ambientes = ambientes;
		this.dormitorios = dormitorios;
		this.banios = banios;
		this.supCubierta = supCubierta;
		this.supTotal = supTotal;
	}
	
	public String getVentaId() {
		return venta_id;
	}
	public void setVentaId(String venta_id) {
		this.venta_id = venta_id;
	}
	public String getLugar() {
		return lugar;
	}
	public void setLugar(String lugar) {
		this.lugar = lugar;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public double getPrecio() {
		return precio;
	}
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	public String getDivisa() {
		return divisa;
	}
	public void setDivisa(String divisa) {
		this.divisa = divisa;
	}
	public int getAmbientes() {
		return ambientes;
	}
	public void setAmbientes(int ambientes) {
		this.ambientes = ambientes;
	}
	public int getDormitorios() {
		return dormitorios;
	}
	public void setDormitorios(int dormitorios) {
		this.dormitorios = dormitorios;
	}
	public double getSupCubierta() {
		return supCubierta;
	}
	public void setSupCubierta(double supCubierta) {
		this.supCubierta = supCubierta;
	}
	public double getSupTotal() {
		return supTotal;
	}
	public void setSupTotal(double supTotal) {
		this.supTotal = supTotal;
	}
	
	public int getBanios() {
		return banios;
	}
	public void setBanios(int banios) {
		this.banios = banios;
	}
	
	
}
