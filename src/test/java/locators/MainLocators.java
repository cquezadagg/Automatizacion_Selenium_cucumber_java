package locators;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MainLocators {

    @FindBy(name = "search")
    public WebElement inptSearch;

    @FindBy(xpath = "//button[@class='btn btn-default btn-lg']")
    public WebElement btnSearch;

    @FindBy(xpath = "//*[@id='content']/div[3]/div/div/div[2]/div[2]/button[1]")
    public WebElement btnAddToCart;

    @FindBy(id= "cart-total")
    public WebElement btnShoppingCart;

    @FindBy(xpath = "//strong[contains(text(),' Checkout')]")
    public WebElement btnCheckout;

    @FindBy(id="button-account")
    public WebElement btnRegisterAccount;

    @FindBy(id= "input-payment-firstname")
    public WebElement inptFirstName;

    @FindBy(id= "input-payment-lastname")
    public WebElement inptLastName;

    @FindBy(id= "input-payment-email")
    public WebElement inptEmail;

    @FindBy(id= "input-payment-telephone")
    public WebElement inptTelephone;

    @FindBy(id="input-payment-address-1")
    public WebElement inptAddress1;

    @FindBy(id="input-payment-city")
    public WebElement inptCity;

    @FindBy(id="input-payment-postcode")
    public WebElement inptPostcode;

    @FindBy(id="input-payment-country")
    public WebElement inptCountry;  

    @FindBy(id="input-payment-zone")
    public WebElement inptZone;

    @FindBy(id = "input-payment-password")
    public WebElement inptPassword;

    @FindBy(id = "input-payment-confirm")
    public WebElement inptConfirmPassword;

    @FindBy(id = "button-register")
    public WebElement btnContinueRegister;

    @FindBy(name= "agree")
    public WebElement chkTerms;

    @FindBy(id="button-shipping-address")
    public WebElement btnContinueShippingAddress;

    @FindBy(id="button-shipping-method")
    public WebElement btnContinueShippingMethod;

    @FindBy(id="button-payment-method")
    public WebElement btnContinuePaymentMethod;

    @FindBy(id="button-confirm")
    public WebElement btnConfirmOrder;

    @FindBy(xpath="//a[@title='My Account']")
    public WebElement btnMyAccount;

    @FindBy(xpath="/html/body/nav/div/div[2]/ul/li[2]/ul/li[2]/a")
    public WebElement btnOrderHistory;

    @FindBy(xpath="//a[@data-original-title='View']")
    public WebElement btnViewOrder;

    @FindBy(xpath="/html/body/div[2]/div/div/table[2]/tbody/tr/td[1]")
    public WebElement txtPaymentAdress;

    @FindBy(xpath="/html/body/nav/div/div[2]/ul/li[2]/ul/li[5]/a")
    public WebElement btnLogout;

    // Locators para iniciar sesion
    @FindBy(id="input-email")
    public WebElement inptEmailLogin;

    @FindBy(id="input-password")
    public WebElement inptPasswordLogin;

    @FindBy(id="button-login")
    public WebElement btnLogin;

    @FindBy(id="button-payment-address")
    public WebElement btnContinuePaymentAddress;

    //Locators para la seccion de extras
    @FindBy(xpath="//button[@data-original-title='Compare this Product']")
    public WebElement btnAddToCompare;

    @FindBy(xpath="//a[contains(text(),'product comparison')]")
    public WebElement btnProductComparison;

    @FindBy(xpath="//strong[contains(text(),'Apple Cinema 30')]")
    public WebElement txtProductComparison;

    @FindBy(xpath="//strong[contains(text(),'Samsung SyncMaster 941BW')]")
    public WebElement txtProductComparison2;

    @FindBy(id="input-option225")
    public WebElement inptDeliveryDate;

    @FindBy(id="input-quantity")
    public WebElement inptQuantity;

    @FindBy(id="button-cart")
    public WebElement btnAddToCart2;

    @FindBy(xpath="//a[contains(text(),'Specification')]")
    public WebElement btnSpecification;

    @FindBy(xpath="//a[contains(text(),'Reviews')]")
    public WebElement btnReviews;

    @FindBy(id="input-name")
    public WebElement inptNameReview;

    @FindBy(id="input-review")
    public WebElement inptReview;

    @FindBy(xpath="//div[contains(text(),'Warning')]")
    public WebElement txtWarning;

    @FindBy(xpath="//input[@name='rating'and@value='3']")
    public WebElement rbtnRatingNeutral;

    @FindBy(id="button-review")
    public WebElement btnContinueReview;

    @FindBy(xpath="//div[contains(text(),'Thank you')]")
    public WebElement txtReviewSuccess;

}
