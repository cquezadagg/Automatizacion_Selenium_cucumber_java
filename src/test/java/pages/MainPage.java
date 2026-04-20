package pages;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentTest;

import locators.MainLocators;

public class MainPage {

    final WebDriver driver;
    final MainLocators locators;
    final ExtentTest scenarioTest;
    final WebDriverWait wait;

    public MainPage(WebDriver driver, ExtentTest scenarioTest) {
        this.driver = driver;
        this.scenarioTest = scenarioTest;
        this.locators = new MainLocators();
        PageFactory.initElements(driver, this.locators);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navegarALaTienda() throws InterruptedException {
        driver.get("http://opencart.abstracta.us/index.php?route=common/home");
    }

    public void buscarYAgregarProductoAlCarrito(String producto) throws InterruptedException {
        locators.inptSearch.clear();
        locators.inptSearch.sendKeys(producto);
        locators.btnSearch.click();
        Actions actions = new Actions(driver);
        wait.until(ExpectedConditions.visibilityOf(locators.btnAddToCart));
        actions.moveToElement(locators.btnAddToCart).perform();
        locators.btnAddToCart.click();
    }

    public void verificarContenidoCarrito(String prod1, String prod2) throws InterruptedException {
        locators.btnShoppingCart.click();
        WebElement tabla = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.tagName("table"))));
        List<WebElement> filas = tabla.findElements(By.tagName("tbody tr"));
        for (WebElement fila : filas) {
            List<WebElement> columnas = fila.findElements(By.tagName("td"));
            String nombreProducto = columnas.get(1).getText();
            if (nombreProducto.equals(prod1) || nombreProducto.equals(prod2)) {
                scenarioTest.pass("  Producto encontrado en el carrito: " + nombreProducto);
            } else {
                scenarioTest.warning(" Producto no encontrado en el carrito: " + nombreProducto);
            }
        }
    }

    public void procederAlCheckout() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOf(locators.btnCheckout));
        locators.btnCheckout.click();
    }

    public void seleccionarRegistroComoUsuario() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOf(locators.btnRegisterAccount));
        locators.btnRegisterAccount.click();
    }

    public void registrarUsuario(Map<String, String> userData) throws InterruptedException, IOException {
        Properties properties = new Properties();
        try {
            FileInputStream input = new FileInputStream("src/test/resources/data/credentials.properties");
            properties.load(input);
            input.close();
            if (properties.getProperty("email").equals(userData.get("Email"))) {
                scenarioTest.info("  El email ya existe en una cuenta registrada, no se puede continuar con el registro");
            } else {
                scenarioTest.info("  El email es nuevo, se guarda y puede seguir el flujo");
            }
            properties.setProperty("email", userData.get("Email"));
            properties.setProperty("firstName", userData.get("Nombre"));
            properties.setProperty("lastName", userData.get("Apellido"));
            properties.setProperty("telefono", userData.get("Telefono"));
            properties.setProperty("direccion", userData.get("Direccion"));
            properties.setProperty("ciudad", userData.get("Ciudad"));
            properties.setProperty("codigoPostal", userData.get("CodigoPostal"));
            properties.setProperty("pais", userData.get("Pais"));
            properties.setProperty("region", userData.get("Region"));
            FileOutputStream output = new FileOutputStream("src/test/resources/data/credentials.properties");
            properties.store(output, null);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        locators.inptFirstName.sendKeys(userData.get("Nombre"));
        locators.inptLastName.sendKeys(userData.get("Apellido"));
        locators.inptEmail.sendKeys(userData.get("Email"));
        locators.inptTelephone.sendKeys(userData.get("Telefono"));
        locators.inptAddress1.sendKeys(userData.get("Direccion"));
        locators.inptCity.sendKeys(userData.get("Ciudad"));
        locators.inptPostcode.sendKeys(userData.get("CodigoPostal"));
        Select pais = new Select(locators.inptCountry);
        pais.selectByVisibleText(userData.get("Pais"));
        wait.until(ExpectedConditions.visibilityOf(locators.inptZone));
        Select region = new Select(locators.inptZone);
        region.selectByVisibleText(userData.get("Region"));
        wait.until(ExpectedConditions.visibilityOf(locators.inptFirstName));
    }

    public void ingresarContrasena(String password) throws InterruptedException {
        Properties properties = new Properties();
        try {
            FileInputStream input = new FileInputStream("src/test/resources/data/credentials.properties");
            properties.load(input);
            input.close();
            properties.setProperty("pass", password);
            FileOutputStream output = new FileOutputStream("src/test/resources/data/credentials.properties");
            properties.store(output, null);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        wait.until(ExpectedConditions.visibilityOf(locators.inptPassword));
        locators.inptPassword.sendKeys(password);
        wait.until(ExpectedConditions.visibilityOf(locators.inptConfirmPassword));
        locators.inptConfirmPassword.sendKeys(password);
    }

    public void aceptarTerminosYCondiciones() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOf(locators.chkTerms));
        locators.chkTerms.click();
    }

    public void confirmarRegistro() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOf(locators.btnContinueRegister));
        locators.btnContinueRegister.click();
    }

    public void continuarConDireccionDeEnvio() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOf(locators.btnContinueShippingAddress));
        locators.btnContinueShippingAddress.click();
    }

    public void validarCostoDeEnvioYContinuar() throws InterruptedException {
        WebElement costoEnvio = driver.findElement(By.xpath("//label[contains(normalize-space(string(.)), '" + "Flat Shipping Rate - $5.00" + "')]"));
        wait.until(ExpectedConditions.visibilityOf(costoEnvio));
        if (costoEnvio.isDisplayed()) {
            scenarioTest.pass("  El costo de envío esta bien");
        } else {
            scenarioTest.warning("  El costo de envío no es correcto");
        }
        wait.until(ExpectedConditions.visibilityOf(locators.btnContinueShippingMethod));
        locators.btnContinueShippingMethod.click();
    }

    public void aceptarTerminosYCompraYContinuar() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOf(locators.chkTerms));
        locators.chkTerms.click();
        wait.until(ExpectedConditions.visibilityOf(locators.btnContinuePaymentMethod));
        locators.btnContinuePaymentMethod.click();
    }

    public void confirmarOrden() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOf(locators.btnConfirmOrder));
        locators.btnConfirmOrder.click();
    }

    public void validarMensajeConfirmacionOrden() throws InterruptedException {
        WebElement mensajeConfirmacion = driver.findElement(By.xpath("//h1[contains(text(),'" + "Your order has been placed!" + "')]"));
        wait.until(ExpectedConditions.visibilityOf(mensajeConfirmacion));
        if (mensajeConfirmacion.isDisplayed()) {
            scenarioTest.pass("  Mensaje de confirmación correcto ");
        } else {
            scenarioTest.warning("  Mensaje de confirmación incorrecto ");
        }
    }

    public void irAHistorialDeOrdenes() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOf(locators.btnMyAccount));
        locators.btnMyAccount.click();
        wait.until(ExpectedConditions.visibilityOf(locators.btnOrderHistory));
        locators.btnOrderHistory.click();
        
    }

    public void validarEstadoOrden() throws InterruptedException {
        WebElement tabla = driver.findElement(By.tagName("table"));
        wait.until(ExpectedConditions.visibilityOf(tabla));
        WebElement cuerpoTabla = tabla.findElement(By.tagName("tbody"));
        List<WebElement> filas = cuerpoTabla.findElements(By.tagName("tr"));
        for (WebElement fila : filas) {
            List<WebElement> columnas = fila.findElements(By.tagName("td"));
            String nombreProducto = columnas.get(3).getText();
            scenarioTest.info("Verificando estado del pedido: " + nombreProducto);
            if (nombreProducto.equals("Pending")) {
                scenarioTest.pass(" El estado del pedido es correcto");
            } else {
                scenarioTest.warning("El estado no es el correcto ");
                scenarioTest.warning("Estado actual del pedido: " + nombreProducto);
            }
        }
    }

    public void validarDatosDePago() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOf(locators.btnViewOrder));
        locators.btnViewOrder.click();
        String txtComplete = locators.txtPaymentAdress.getText();
        wait.until(ExpectedConditions.visibilityOf(locators.txtPaymentAdress));
        String[] datos = txtComplete.split("\n");
        Properties properties = new Properties();
        try {
            FileInputStream input = new FileInputStream("src/test/resources/data/credentials.properties");
            properties.load(input);
            input.close();
            String nombreApellido = properties.getProperty("firstName") + " " + properties.getProperty("lastName");
            String direccion = properties.getProperty("direccion");
            String ciudadCodPostal = properties.getProperty("ciudad") + " " + properties.getProperty("codigoPostal");
            String pais = properties.getProperty("pais");
            String region = properties.getProperty("region");
            if (datos[0].equals(nombreApellido) &&
                datos[1].equals(direccion) &&
                datos[2].equals(ciudadCodPostal) &&
                datos[3].equals(region) &&
                datos[4].equals(pais)) {
                scenarioTest.pass("  Los datos de pago corresponden a los ingresados");
            } else {
                scenarioTest.warning("  Los datos de pago no coinciden a los ingresados");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cerrarSesion() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOf(locators.btnMyAccount));
        locators.btnMyAccount.click();
        wait.until(ExpectedConditions.visibilityOf(locators.btnLogout));
        locators.btnLogout.click();
    }

    public void iniciarSesionConCredenciales() throws IOException, InterruptedException {
        Properties properties = new Properties();
        try {
            FileInputStream input = new FileInputStream("src/test/resources/data/credentials.properties");
            properties.load(input);
            input.close();
            locators.inptEmailLogin.sendKeys(properties.getProperty("email"));
            locators.inptPasswordLogin.sendKeys(properties.getProperty("pass"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        wait.until(ExpectedConditions.visibilityOf(locators.btnLogin));
        locators.btnLogin.click();
    }

    public void continuarConDireccionPago() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOf(locators.btnContinuePaymentAddress));
        locators.btnContinuePaymentAddress.click();
    }

    public void buscarYAgregarProductoAComparacion(String producto) throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOf(locators.inptSearch));
        locators.inptSearch.clear();
        locators.inptSearch.sendKeys(producto);
        wait.until(ExpectedConditions.visibilityOf(locators.btnSearch));
        locators.btnSearch.click();
        wait.until(ExpectedConditions.visibilityOf(locators.btnAddToCompare));
        Actions actions = new Actions(driver);
        wait.until(ExpectedConditions.visibilityOf(locators.btnAddToCompare));
        actions.moveToElement(locators.btnAddToCompare).perform();
        locators.btnAddToCompare.click();
    }

    public void irAComparacionDeProductos() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOf(locators.btnProductComparison));
        locators.btnProductComparison.click();
    }

    public void validarProductosComparados() throws InterruptedException {
        WebElement tabla = driver.findElement(By.tagName("table"));
        wait.until(ExpectedConditions.visibilityOf(tabla));
        WebElement fila = tabla.findElement(By.cssSelector("tbody > tr"));
        List<WebElement> columnas = fila.findElements(By.tagName("td"));
        String nombreProducto1 = columnas.get(1).getText();
        String nombreProducto2 = columnas.get(2).getText();
        if (nombreProducto1.equals("Apple Cinema 30\"") && nombreProducto2.equals("Samsung SyncMaster 941BW")) {
            scenarioTest.pass("  Productos comparados correctamente: ");
        } else {
            scenarioTest.warning("  Productos comparados incorrectamente: " + nombreProducto1 + " y " + nombreProducto2);
        }
    }

    public void seleccionarFechaEntregaParaManana() throws InterruptedException {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        String formattedDate = tomorrow.format(java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        wait.until(ExpectedConditions.visibilityOf(locators.inptDeliveryDate));
        locators.inptDeliveryDate.clear();
        locators.inptDeliveryDate.sendKeys(formattedDate);
    }
    public void agregar2enCantidad() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOf(locators.inptQuantity));
        locators.inptQuantity.clear();
        locators.inptQuantity.sendKeys("2");
    }
    public void validarMemoriaDelEquipo() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOf(locators.btnSpecification));
        locators.btnSpecification.click();
        WebElement especificacion = driver.findElement(By.xpath("//td[contains(text(),'" + "16GB" + "')]"));
        wait.until(ExpectedConditions.visibilityOf(especificacion));
        if (especificacion.isDisplayed()) {
            scenarioTest.pass("  La memoria del equipo es correcta");
        } else {
            scenarioTest.warning("  La memoria del equipo no es correcta: " );
        }
    }
    public void agregarMensajeDeReview(String mensaje) throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOf(locators.btnReviews));
        locators.btnReviews.click();
        switch(mensaje){
            case "incorrecto":
                wait.until(ExpectedConditions.visibilityOf(locators.inptNameReview));
                locators.inptReview.sendKeys("Mensaje incorrecto");
                locators.btnContinueReview.click();
                WebElement txtWarning = locators.txtWarning;
                wait.until(ExpectedConditions.visibilityOf(txtWarning));
                if (txtWarning.isDisplayed()) {
                    scenarioTest.pass("  Mensaje de review no enviado: " + txtWarning.getText());
                } else {
                    scenarioTest.warning("  Mensaje de review enviado correctamente");
                }
                break;
            case "correcto":
                wait.until(ExpectedConditions.visibilityOf(locators.inptNameReview));
                locators.inptNameReview.sendKeys("Usuario de prueba");
                wait.until(ExpectedConditions.visibilityOf(locators.inptReview));
                locators.inptReview.clear();
                locators.inptReview.sendKeys("Este mensaje de prueba es correcto");
                locators.rbtnRatingNeutral.click();
                locators.btnContinueReview.click();
                WebElement textReviewSucces = locators.txtReviewSuccess;
                wait.until(ExpectedConditions.visibilityOf(textReviewSucces));
                if (textReviewSucces.isDisplayed()) {
                    scenarioTest.pass("  Mensaje de review enviado correctamente: " + textReviewSucces.getText());
                } else {
                    scenarioTest.warning("  Mensaje de review no enviado correctamente");
                }
                break;
        }
    }
}