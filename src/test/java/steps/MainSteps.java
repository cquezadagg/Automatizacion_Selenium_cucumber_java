package steps;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.exec.OS;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import locators.MainLocators;
import utils.ExtentReportManager;
import utils.ScreenshotUtil;

public class MainSteps {

    WebDriver driver;
    MainLocators locators;
    public static ExtentReports extent;
    public static ExtentTest feature;
    public static ExtentTest scenarioTest;

    @BeforeAll
    public static void beforeAll() {
        extent = ExtentReportManager.getInstance();
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        scenarioTest = extent.createTest(scenario.getName());

        if (driver == null) {
            if (OS.isName("Windows")) {
                System.setProperty("webdriver.edge.driver", "src/test/resources/driver/msedgedriver.exe");
            } else if (OS.isName("Linux")) {
                System.setProperty("webdriver.edge.driver", "src/test/resources/driver/msedgedriver");
            }
            driver = new EdgeDriver();
            driver.manage().window().maximize();
        }
    }

 @AfterStep
public void afterStep(Scenario scenario) {
    String screenshotName = "step_" + System.currentTimeMillis();
    String screenshotPath = ScreenshotUtil.takeScreenshot(driver, screenshotName);

    String relativePath = "screenshots/" + screenshotName + ".png";

    if (scenario.isFailed()) {
        scenarioTest.fail("Paso fallido").addScreenCaptureFromPath(relativePath);
    } else {
        scenarioTest.pass("Paso correcto").addScreenCaptureFromPath(relativePath);
    }
}



    @After
    public void afterScenario() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    @AfterAll
    public static void afterAll() {
        extent.flush();
    }

    @Given("entro a la url de la tienda")
    public void entroAlaUrlDeGoogle() throws InterruptedException {
        driver.get("http://opencart.abstracta.us/index.php?route=common/home");
        locators = new MainLocators();
        PageFactory.initElements(driver, locators);
        Thread.sleep(2000);
    }

    @When("busco el producto {string} y lo agrego al carrito")
    public void buscoElProductoYLoAgregoAlCarrito(String producto) throws InterruptedException {
        locators.inptSearch.clear();
        locators.inptSearch.sendKeys(producto);
        locators.btnSearch.click();
        Thread.sleep(2000);
        Actions actions = new Actions(driver);
        actions.moveToElement(locators.btnAddToCart).perform();
        locators.btnAddToCart.click();
        Thread.sleep(2000);
    }

    @And("verifico que el carrito tenga {string} y {string}")
    public void verificoQueElCarritoTenga(String prod1, String prod2) throws InterruptedException {
        locators.btnShoppingCart.click();
        Thread.sleep(2000);
        WebElement tabla = driver.findElement(By.tagName("table"));
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

    @Then("procedo al checkout")
    public void procedoAlCheckout() throws InterruptedException {
        locators.btnCheckout.click();
        Thread.sleep(2000);
    }

    @Then("selecciono la opcion de registarme como usuario")
    public void seleccionoLaOpcionDeRegistrarmeComoUsuario() throws InterruptedException {
        locators.btnRegisterAccount.click();
        Thread.sleep(2000);
    }

    @And("me registro como usuario con los siguientes datos:")
    public void meRegistroComoUsuarioConLosSiguientesDatos(io.cucumber.datatable.DataTable dataTable) throws InterruptedException, IOException {
        List<Map<String, String>> datos = dataTable.asMaps(String.class, String.class);
        Map<String, String> fila = datos.get(0);
        Select pais = new Select(locators.inptCountry);
        Properties properties = new Properties();
        try {
            FileInputStream input = new FileInputStream("src/test/resources/data/credentials.properties"); 
            properties.load(input);
            input.close();
            if(properties.getProperty("email").equals(fila.get("Email"))){
                scenarioTest.info("  El email ya existe en una cuenta registrada, no se puede continuar con el registro");
            } else {
                scenarioTest.info("  El email es nuevo, se guarda y puede seguir el flujo");
            }
            properties.setProperty("email", fila.get("Email"));
            properties.setProperty("firstName", fila.get("Nombre"));
            properties.setProperty("lastName", fila.get("Apellido"));
            properties.setProperty("telefono", fila.get("Telefono"));
            properties.setProperty("direccion", fila.get("Direccion"));
            properties.setProperty("ciudad", fila.get("Ciudad"));
            properties.setProperty("codigoPostal", fila.get("CodigoPostal"));
            properties.setProperty("pais", fila.get("Pais"));
            properties.setProperty("region", fila.get("Region"));
            FileOutputStream output = new FileOutputStream("src/test/resources/data/credentials.properties");
            properties.store(output, null);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        locators.inptFirstName.sendKeys(fila.get("Nombre"));
        locators.inptLastName.sendKeys(fila.get("Apellido"));
        locators.inptEmail.sendKeys(fila.get("Email"));
        locators.inptTelephone.sendKeys(fila.get("Telefono"));
        locators.inptAddress1.sendKeys(fila.get("Direccion"));
        locators.inptCity.sendKeys(fila.get("Ciudad"));
        locators.inptPostcode.sendKeys(fila.get("CodigoPostal"));
        pais.selectByVisibleText(fila.get("Pais"));
        Thread.sleep(2000);
        Select region = new Select(locators.inptZone);
        region.selectByVisibleText(fila.get("Region"));

        Thread.sleep(2000);
    }

    @And("ingreso mi contrasena y la confirmo {string}")
    public void ingresoMiContrasenaYLaConfirmo(String Contrasena) throws InterruptedException {
        Properties properties = new Properties();
        try {
            FileInputStream input = new FileInputStream("src/test/resources/data/credentials.properties"); 
            properties.load(input);
            input.close();
            properties.setProperty("pass", Contrasena);
            FileOutputStream output = new FileOutputStream("src/test/resources/data/credentials.properties");
            properties.store(output, null);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        locators.inptPassword.sendKeys(Contrasena);
        locators.inptConfirmPassword.sendKeys(Contrasena);
        Thread.sleep(2000);
    }

    @Then("acepto los terminos y condiciones")
    public void aceptoLosTerminosYCondiciones() throws InterruptedException {
        locators.chkTerms.click();
        Thread.sleep(2000);
    }

    @Then("confirmo el registro")
    public void confirmoElRegistro() throws InterruptedException {
        locators.btnContinueRegister.click();
        Thread.sleep(2000);
    }

    @Then("continuo con los datos de despacho ingresados en cuenta")
    public void continuoConLosDatosDeDespachoIngresadosEnCuenta() throws InterruptedException {
        locators.btnContinueShippingAddress.click();
        Thread.sleep(2000);
    }

    @And("valido que el despacho y costo sea Flat Shipping Rate - $5.00 y continuo")
    public void validoQueElDespachoYCostoSea() throws InterruptedException {
        WebElement costoEnvio = driver.findElement(By.xpath("//label[contains(normalize-space(string(.)), '" + "Flat Shipping Rate - $5.00"+ "')]"));
        if (costoEnvio.isDisplayed()) {
            scenarioTest.pass("  El costo de envío es esta bien");
        } else {
            scenarioTest.warning("  El costo de envío no es correcto");
        }
        locators.btnContinueShippingMethod.click();
        Thread.sleep(2000);
    }

    @Then("acepto los terminos y condiciones de compra y continuo a confirmar orden")
    public void aceptoLosTerminosYCondicionesDeCompraYContinuoAConfirmarOrden() throws InterruptedException {
        locators.chkTerms.click();
        locators.btnContinuePaymentMethod.click();
        Thread.sleep(2000);
    }

    @And("confirmo la orden de compra")
    public void confirmoLaOrdenDeCompra() throws InterruptedException {
        locators.btnConfirmOrder.click();
        Thread.sleep(2000);
    }

    @Then("valido que el mensaje de confirmacion sea Your order has been placed!")
    public void validoQueElMensajeDeConfirmacionSea() throws InterruptedException {
        WebElement mensajeConfirmacion = driver.findElement(By.xpath("//h1[contains(text(),'" + "Your order has been placed!" + "')]"));
        if (mensajeConfirmacion.isDisplayed()) {
            scenarioTest.pass("  Mensaje de confirmación correcto ");  
        } else {
            scenarioTest.warning("  Mensaje de confirmación incorrecto ");
        }
        Thread.sleep(2000);
    }
    @And("voy al apartado de My Account y entro en Order History")
    public void voyAlApartadoDeMyAccountYEntroEnOrderHistory() throws InterruptedException {
        locators.btnMyAccount.click();
        Thread.sleep(500);
        locators.btnOrderHistory.click();
        Thread.sleep(2000);
    }

    @Then("valido que el pedido este en estado Pending")
    public void validoQueElPedidoEsteEnEstado() throws InterruptedException {
        Thread.sleep(2000);
       WebElement tabla = driver.findElement(By.tagName("table"));
       WebElement cuerpoTabla = tabla.findElement(By.tagName("tbody"));
        List<WebElement> filas = cuerpoTabla.findElements(By.tagName("tr")); 
        for (WebElement fila : filas) {
            List<WebElement> columnas = fila.findElements(By.tagName("td"));
            String nombreProducto = columnas.get(3).getText();
            scenarioTest.info("Verificando estado del pedido: " + nombreProducto);
            if(nombreProducto.equals("Pending")) {
                scenarioTest.pass(" El estado del pedido es correcto");
            } else {
                scenarioTest.warning("El estado no es el correcto ");
                scenarioTest.warning("Estado actual del pedido: " + nombreProducto);
            }

        } 
        Thread.sleep(2000);
    }

    @And("valido los datos de pago vs los ingresados en mi cuenta")
    public void validoLosDatosDePagoVsLosIngresadosEnMiCuenta() throws InterruptedException {
        locators.btnViewOrder.click();
        Thread.sleep(2000);
        String txtComplete = locators.txtPaymentAdress.getText();
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
            if(datos[0].equals(nombreApellido) && 
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
    @Then("cierro la sesion")
    public void cierroLaSesion() throws InterruptedException {
        locators.btnMyAccount.click();
        Thread.sleep(500);
        locators.btnLogout.click();
        Thread.sleep(2000);
    }

    // Estos flujos son para usuario con cuenta
    @And("ingreso mis credenciales de usuario desde el archivo properties")
    public void ingresoMisCredencialesDeUsuarioDesdeElArchivoDeProperties() throws IOException, InterruptedException {
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
        locators.btnLogin.click();
        Thread.sleep(2000);
    }

    @And("continuo con los datos de pago ingresados en cuenta")
    public void continuoConLosDatosDePagoIngresadosEnCuenta() throws InterruptedException {
        locators.btnContinuePaymentAddress.click();
        Thread.sleep(2000);
    }

    // Estos flujos corresponden a la seccion de puntos extras
    @When("busco el producto {string} y lo agrego a comparacion")
    public void buscoElProductoYLoAgregoAComparacion(String producto) throws InterruptedException {
        locators.inptSearch.clear();
        locators.inptSearch.sendKeys(producto);
        locators.btnSearch.click();
        Thread.sleep(2000);
        Actions actions = new Actions(driver);
        actions.moveToElement(locators.btnAddToCompare).perform();
        locators.btnAddToCompare.click();
        Thread.sleep(2000);
    }

    @Then("entro al apartado de Product Comparison")
    public void entroAlApartadoDeProductComparison() throws InterruptedException {
        locators.btnProductComparison.click();
        Thread.sleep(2000);
    }

    @Then("valido que los productos comparados sean Apple Cinema y Samsung SyncMaster 941BW")
   public void valido_que_los_productos_comparados_sean_apple_cinema_y_samsung_sync_master_941bw() throws InterruptedException {
        WebElement tabla = driver.findElement(By.tagName("table"));
        WebElement fila = tabla.findElement(By.cssSelector("tbody > tr"));
        List<WebElement> columnas = fila.findElements(By.tagName("td"));
        String nombreProducto1 = columnas.get(1).getText();
        String nombreProducto2 = columnas.get(2).getText();
        if(nombreProducto1.equals("Apple Cinema 30\"") && nombreProducto2.equals("Samsung SyncMaster 941BW")) {
            scenarioTest.pass("  Productos comparados correctamente: ");
        } else {
            scenarioTest.warning("  Productos comparados incorrectamente: " + nombreProducto1 + " y " + nombreProducto2);
        }
    }

    @Then("selecciono el delivery date para manana")
    public void seleccionoElDeliveryDateParaManana() throws InterruptedException {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        String formattedDate = tomorrow.format(java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        locators.inptDeliveryDate.clear();
        locators.inptDeliveryDate.sendKeys(formattedDate);
        Thread.sleep(2000);
    }

    @And("coloco la cantidad 2 y lo agrego al carrito")
    public void colocoLaCantidad2YLoAgregoAlCarrito() throws InterruptedException {
        locators.inptQuantity.clear();
        locators.inptQuantity.sendKeys("2");
        locators.btnAddToCart2.click();
        Thread.sleep(2000);
    }

    @Then("valido que la memoria del equipo sea 16GB")
    public void validoQueLaMemoriaDelEquipoSea() throws InterruptedException {
        locators.btnSpecification.click();
        Thread.sleep(2000);
        WebElement especificacion = driver.findElement(By.xpath("//td[contains(text(),'" + "16GB" + "')]"));
        if (especificacion.isDisplayed()) {
            scenarioTest.pass("  La memoria del equipo es correcta");
        } else {
            scenarioTest.warning("  La memoria del equipo no es correcta: " );
        }
        Thread.sleep(2000);
    }

    @Then("ingreso a la seccion reviews e ingreso un mensaje {string}")
    public void ingresoAlaSeccionReviewsEIngresoUnMensaje(String mensaje) throws InterruptedException {
        locators.btnReviews.click();
        switch(mensaje){
            case "incorrecto":
                locators.inptReview.sendKeys("Mensaje incorrecto");
                locators.btnContinueReview.click();
                Thread.sleep(2000);
                WebElement txtWarning = locators.txtWarning;
                if (txtWarning.isDisplayed()) {
                    scenarioTest.pass("  Mensaje de review no enviado: " + txtWarning.getText());
                } else {
                    scenarioTest.warning("  Mensaje de review enviado correctamente");
                }
                break;
            case "correcto":
                locators.inptNameReview.sendKeys("Usuario de prueba");
                locators.inptReview.clear();
                locators.inptReview.sendKeys("Este mensaje de prueba es correcto");
                locators.rbtnRatingNeutral.click();
                locators.btnContinueReview.click();
                Thread.sleep(2000);
                WebElement textReviewSucces = locators.txtReviewSuccess;
                if (textReviewSucces.isDisplayed()) {
                    scenarioTest.pass("  Mensaje de review enviado correctamente: " + textReviewSucces.getText());
                } else {
                    scenarioTest.warning("  Mensaje de review no enviado correctamente");
                }
                Thread.sleep(2000);
                break;
        }
    }

}
