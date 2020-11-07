package remax.scrapping;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import remax.scrapping.models.Alquiler;
import remax.scrapping.models.Venta;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import java.time.Duration;
import java.util.List;

public class ScrapperMaster {
	static int skipped = 0;
	static int mapped = 0;
	
	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriver\\chromedriver.exe");
		
		final int untilPage = 40; // coud be extract 24 rents per page
        try {
        	mapAlquiler(untilPage);
        	mapVentas(untilPage);
            for(int currentPage = 1; currentPage <= untilPage; currentPage++) {
            	mapVentas(currentPage);
            	System.out.println("Página "+ currentPage + " mapeada.");
            }
        	System.out.println("Datos cargados: " + mapped);
        	System.out.println("Denegados: " + skipped);
        	mapped = 0;
        	skipped = 0;
        	for(int currentPage = 1; currentPage <= untilPage; currentPage++) {
	        	mapAlquiler(currentPage);
	        	System.out.println("Página "+ currentPage + " mapeada.");
        	}
        	System.out.println("Datos cargados: " + mapped);
        	System.out.println("Denegados: " + skipped);
        } finally {
        	
        }
	}
	
	static void mapVentas(int page) {
		WebDriver driver = new ChromeDriver();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20).getSeconds());
		try {
			String url = page == 1 ? "https://www.remax.com.ar/publiclistinglist.aspx?SelectedCountryID=42&TransactionType=For%20Sale&IsQuickSearch=1" : navVentas(page);
	    	driver.get(url);
	    	
	        WebElement firstResult = driver.findElement(By.id("MainContent"));
	        firstResult.findElement(By.id("ll-content-container"));
	        WebElement gallery = wait.until(presenceOfElementLocated(By.className("gallery-container")));
	        List<WebElement> items = gallery.findElements(By.className("gallery-item-container"));
	        for(WebElement item : items) {
	        	String venta_id = item.getAttribute("id");
	        	String loc = item.findElement(By.className("gallery-title")).getText();
	        	String tipo = item.findElement(By.className("gallery-transtype")).getText();
	        	String pre = item.findElement(By.className("gallery-price")).getText();
	        	String divisa = "";
	        	if(pre.endsWith("ARS")) {
	        		divisa = "ARS";
	        	} else if(pre.endsWith("USD")) {
	        		divisa = "USD";
	        	} else if(pre.contains("Solicitar entrevista")) {// no se necesita ventas que no contengan precio.
	        		skipped++;
	        		continue;
	        	}
	        	
	        	pre = pre.substring(0, pre.length() -3).trim();
	        	double precio = Double.parseDouble(pre.replaceFirst(",", ""));
	        	
	        	int ambientes = 0;
	        	int num_dormitorios = 0;
	        	int banios = 0;
	        	double sup_cubierta = 0.0;
	        	double sup_total = 0.0;
	        	WebElement listed = item.findElement(By.className("gallery-icons"));
	        	List<WebElement> infos = listed.findElements(By.tagName("img"));
	        	for(WebElement info : infos) {
	        		String val = info.getAttribute("data-original-title");
	        		String[] splitted = val.split(" ");
	        		String lat = splitted[splitted.length - 1]; // get the latest val of the splitted string.
	        		lat = lat.replaceFirst(",", "");
	        		if(val.startsWith("Total de Ambientes")) {
	        			ambientes = Integer.parseInt(lat);
	        		} else if(val.startsWith("Baños:")) {
	        			 banios = Integer.parseInt(lat);
	        		} else if(val.startsWith("Num.")) {
	        			num_dormitorios = Integer.parseInt(lat);
	        		} else if(val.startsWith("Sup. Total")) {
	        			sup_total = Double.parseDouble(lat);
	        		} else if(val.startsWith("Sup. Cubierta")) {
	        			sup_cubierta = Double.parseDouble(lat);
	        		}
	        	}
	        	DBAzure.insertVenta(new Venta(venta_id, loc, tipo, precio, divisa, ambientes, num_dormitorios, banios, sup_cubierta, sup_total));
	        	mapped++;
	        }
		}
		finally {
			driver.quit();
		}
	}
	
	static void mapAlquiler(int page) {
		WebDriver driver = new ChromeDriver();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20).getSeconds());
		try {
			String url = page == 1 ? "https://www.remax.com.ar/publiclistinglist.aspx?SelectedCountryID=42&TransactionType=For%20Rent%2FLease&IsQuickSearch=1" : navAlquiler(page);
	    	driver.get(url);
	    	
	        WebElement firstResult = driver.findElement(By.id("MainContent"));
	        firstResult.findElement(By.id("ll-content-container"));
	        WebElement gallery = wait.until(presenceOfElementLocated(By.className("gallery-container")));
	        List<WebElement> items = gallery.findElements(By.className("gallery-item-container"));
	        for(WebElement item : items) {
	        	String venta_id = item.getAttribute("id");
	        	String loc = item.findElement(By.className("gallery-title")).getText();
	        	String tipo = item.findElement(By.className("gallery-transtype")).getText();
	        	String pre = item.findElement(By.className("gallery-price")).getText();
	        	String divisa = "";
	        	if(pre.endsWith("ARS")) {
	        		divisa = "ARS";
	        	} else if(pre.endsWith("USD")) {
	        		divisa = "USD";
	        	} else if(pre.contains("Solicitar entrevista")) {// no se necesita ventas que no contengan precio.
	        		skipped++;
	        		continue;
	        	}
	        	int bracketOpen = pre.indexOf("[");
	        	if(bracketOpen < 0) {
	        		skipped++;
	        		continue;
	        	}
	        	int bracketClose = pre.indexOf("]");
	        	String periodo_pago = pre.substring(bracketOpen + 1, bracketClose);
	        	pre = pre.substring(0, bracketOpen-3).trim();
	        	double precio = Double.parseDouble(pre.replaceFirst(",", ""));
	        	
	        	int ambientes = 0;
	        	int num_dormitorios = 0;
	        	int banios = 0;
	        	double sup_cubierta = 0.0;
	        	double sup_total = 0.0;
	        	WebElement listed = item.findElement(By.className("gallery-icons"));
	        	List<WebElement> infos = listed.findElements(By.tagName("img"));
	        	for(WebElement info : infos) {
	        		String val = info.getAttribute("data-original-title");
	        		String[] splitted = val.split(" ");
	        		String lat = splitted[splitted.length - 1]; // get the latest val of the splitted string.
	        		lat = lat.replaceFirst(",", "");
	        		if(val.startsWith("Total de Ambientes")) {
	        			ambientes = Integer.parseInt(lat);
	        		} else if(val.startsWith("Baños:")) {
	        			 banios = Integer.parseInt(lat);
	        		} else if(val.startsWith("Num.")) {
	        			num_dormitorios = Integer.parseInt(lat);
	        		} else if(val.startsWith("Sup. Total")) {
	        			sup_total = Double.parseDouble(lat);
	        		} else if(val.startsWith("Sup. Cubierta")) {
	        			sup_cubierta = Double.parseDouble(lat);
	        		}
	        	}
	        	DBAzure.insertAlquiler(new Alquiler(venta_id, loc, tipo, periodo_pago, precio, divisa, ambientes, num_dormitorios, banios, sup_cubierta, sup_total));
	        	mapped++;
	        }
		}
		finally {
			driver.quit();
		}
	}
	
	static String navVentas(int page) {
		return "https://www.remax.com.ar/publiclistinglist.aspx?SelectedCountryID=42&TransactionType=For%20Sale&IsQuickSearch=1#mode=gallery&tt=261&cur=ARS&sb=PriceIncreasing&page="+page;
	}
	
	static String navAlquiler(int page) {
		return "https://www.remax.com.ar/publiclistinglist.aspx?SelectedCountryID=42&TransactionType=For%20Rent%2FLease&IsQuickSearch=1#mode=gallery&tt=260&cur=ARS&sb=PriceIncreasing&page="+page;
	}
	

}
