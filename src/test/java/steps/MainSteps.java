package steps;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

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
import pages.MainPage;
import utils.ExtentReportManager;
import utils.ScreenshotUtil;

public class MainSteps {

    WebDriver driver;
    MainPage mainPage;
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
ChromeOptions options = new ChromeOptions();
// options.addArguments("--headless=new");
options.addArguments("--no-sandbox");
options.addArguments("--disable-dev-shm-usage");
options.addArguments("--disable-gpu");
options.addArguments("--window-size=1920,1080");
options.addArguments("--remote-allow-origins=*");

driver = new ChromeDriver(options);

        }
        mainPage = new MainPage(driver, scenarioTest);
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
        mainPage.navegarALaTienda();
    }

    @When("busco el producto {string} y lo agrego al carrito")
    public void buscoElProductoYLoAgregoAlCarrito(String producto) throws InterruptedException {
        mainPage.buscarYAgregarProductoAlCarrito(producto);
    }

    @And("verifico que el carrito tenga {string} y {string}")
    public void verificoQueElCarritoTenga(String prod1, String prod2) throws InterruptedException {
        mainPage.verificarContenidoCarrito(prod1, prod2);
    }

    @Then("procedo al checkout")
    public void procedoAlCheckout() throws InterruptedException {
        mainPage.procederAlCheckout();
    }

    @Then("selecciono la opcion de registarme como usuario")
    public void seleccionoLaOpcionDeRegistrarmeComoUsuario() throws InterruptedException {
        mainPage.seleccionarRegistroComoUsuario();
    }

    @And("me registro como usuario con los siguientes datos:")
    public void meRegistroComoUsuarioConLosSiguientesDatos(io.cucumber.datatable.DataTable dataTable) throws InterruptedException, IOException {
        List<Map<String, String>> datos = dataTable.asMaps(String.class, String.class);
        Map<String, String> fila = datos.get(0);
        mainPage.registrarUsuario(fila);
    }

    @And("ingreso mi contrasena y la confirmo {string}")
    public void ingresoMiContrasenaYLaConfirmo(String Contrasena) throws InterruptedException {
        mainPage.ingresarContrasena(Contrasena);
    }

    @Then("acepto los terminos y condiciones")
    public void aceptoLosTerminosYCondiciones() throws InterruptedException {
        mainPage.aceptarTerminosYCondiciones();
    }

    @Then("confirmo el registro")
    public void confirmoElRegistro() throws InterruptedException {
        mainPage.confirmarRegistro();
    }

    @Then("continuo con los datos de despacho ingresados en cuenta")
    public void continuoConLosDatosDeDespachoIngresadosEnCuenta() throws InterruptedException {
        mainPage.continuarConDireccionDeEnvio();
    }

    @And("valido que el despacho y costo sea Flat Shipping Rate - $5.00 y continuo")
    public void validoQueElDespachoYCostoSea() throws InterruptedException {
        mainPage.validarCostoDeEnvioYContinuar();
    }

    @Then("acepto los terminos y condiciones de compra y continuo a confirmar orden")
    public void aceptoLosTerminosYCondicionesDeCompraYContinuoAConfirmarOrden() throws InterruptedException {
        mainPage.aceptarTerminosYCompraYContinuar();
    }

    @And("confirmo la orden de compra")
    public void confirmoLaOrdenDeCompra() throws InterruptedException {
        mainPage.confirmarOrden();
    }

    @Then("valido que el mensaje de confirmacion sea Your order has been placed!")
    public void validoQueElMensajeDeConfirmacionSea() throws InterruptedException {
        mainPage.validarMensajeConfirmacionOrden();
    }

    @And("voy al apartado de My Account y entro en Order History")
    public void voyAlApartadoDeMyAccountYEntroEnOrderHistory() throws InterruptedException {
        mainPage.irAHistorialDeOrdenes();
    }

    @Then("valido que el pedido este en estado Pending")
    public void validoQueElPedidoEsteEnEstado() throws InterruptedException {
        mainPage.validarEstadoOrden();
    }

    @And("valido los datos de pago vs los ingresados en mi cuenta")
    public void validoLosDatosDePagoVsLosIngresadosEnMiCuenta() throws InterruptedException {
        mainPage.validarDatosDePago();
    }

    @Then("cierro la sesion")
    public void cierroLaSesion() throws InterruptedException {
        mainPage.cerrarSesion();
    }

    // Estos flujos son para usuario con cuenta
    @And("ingreso mis credenciales de usuario desde el archivo properties")
    public void ingresoMisCredencialesDeUsuarioDesdeElArchivoDeProperties() throws IOException, InterruptedException {
        mainPage.iniciarSesionConCredenciales();
    }

    @And("continuo con los datos de pago ingresados en cuenta")
    public void continuoConLosDatosDePagoIngresadosEnCuenta() throws InterruptedException {
        mainPage.continuarConDireccionPago();
    }

    // Estos flujos corresponden a la seccion de puntos extras
    @When("busco el producto {string} y lo agrego a comparacion")
    public void buscoElProductoYLoAgregoAComparacion(String producto) throws InterruptedException {
        mainPage.buscarYAgregarProductoAComparacion(producto);
    }

    @Then("entro al apartado de Product Comparison")
    public void entroAlApartadoDeProductComparison() throws InterruptedException {
        mainPage.irAComparacionDeProductos();
    }

    @Then("valido que los productos comparados sean Apple Cinema y Samsung SyncMaster 941BW")
   public void valido_que_los_productos_comparados_sean_apple_cinema_y_samsung_sync_master_941bw() throws InterruptedException {
        mainPage.validarProductosComparados();
    }

    @Then("selecciono el delivery date para manana")
    public void seleccionoElDeliveryDateParaManana() throws InterruptedException {
        mainPage.seleccionarFechaEntregaParaManana();
    }

    @And("coloco la cantidad 2 y lo agrego al carrito")
    public void colocoLaCantidad2YLoAgregoAlCarrito() throws InterruptedException {
        mainPage.agregar2enCantidad();
    }

    @Then("valido que la memoria del equipo sea 16GB")
    public void validoQueLaMemoriaDelEquipoSea() throws InterruptedException {
        mainPage.validarMemoriaDelEquipo();
    }

    @Then("ingreso a la seccion reviews e ingreso un mensaje {string}")
    public void ingresoAlaSeccionReviewsEIngresoUnMensaje(String mensaje) throws InterruptedException {
        mainPage.agregarMensajeDeReview(mensaje);
    }


}
