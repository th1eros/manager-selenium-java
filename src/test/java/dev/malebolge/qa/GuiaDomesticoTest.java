package dev.malebolge.qa;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.List;

public class GuiaDomesticoTest {
    private WebDriver driver;

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
    }

    @Test
    @DisplayName("Escanear Elementos de Login")
    void scanLoginElements() {
        driver.get("https://guiadomestico.com.br/login");
        
        System.out.println("\n=== MAPEAMENTO DE ELEMENTOS (QA SCAN) ===");
        List<WebElement> inputs = driver.findElements(By.tagName("input"));
        for (WebElement input : inputs) {
            System.out.println("Input -> Name: " + input.getAttribute("name") + 
                               " | ID: " + input.getAttribute("id") + 
                               " | Type: " + input.getAttribute("type"));
        }

        List<WebElement> buttons = driver.findElements(By.tagName("button"));
        for (WebElement btn : buttons) {
            System.out.println("Button -> Text: " + btn.getText() + 
                               " | Type: " + btn.getAttribute("type"));
        }
        System.out.println("==========================================\n");
    }

    @Test
    @DisplayName("Executar Login e Enviar Documentação para o Drive")
    void testLoginCompletoComEmail() throws InterruptedException {
        String email = System.getenv("QA_EMAIL");
        String senha = System.getenv("QA_PWD");

        if (email == null || email.isEmpty()) {
            fail("Variável de ambiente QA_EMAIL não definida");
        }
        if (senha == null || senha.isEmpty()) {
            fail("Variável de ambiente QA_PWD não definida");
        }

        String loginUrl = "https://guiadomestico.com.br/publico/usuario/usuario_login.php";
        driver.get(loginUrl);
        
        driver.findElement(By.id("EMAIL_USUARIO")).sendKeys(email);
        driver.findElement(By.id("SENHA_USUARIO")).sendKeys(senha);
        driver.findElement(By.id("SENHA_USUARIO")).submit();
        
        Thread.sleep(4000);
        
        String currentUrl = driver.getCurrentUrl();
        String pageTitle = driver.getTitle();
        System.out.println(">>> URL após login: " + currentUrl);
        System.out.println(">>> Título da página: " + pageTitle);
        
        boolean sucesso = false;
        if (!currentUrl.equals(loginUrl) && !currentUrl.contains("usuario_login.php")) {
            sucesso = true;
        } else {
            List<WebElement> errorMessages = driver.findElements(By.cssSelector(".alert-danger, .error, .mensagem-erro"));
            if (!errorMessages.isEmpty()) {
                System.out.println(">>> Mensagem de erro encontrada: " + errorMessages.get(0).getText());
            }
        }
        
        String corpoEmail = String.format(
            "PROJETO INTEGRADOR 1 - 2026\n" +
            "---------------------------\n" +
            "Sistema: Guia Doméstico\n" +
            "Responsável QA: Malebolge\n" +
            "Status do Teste: %s\n" +
            "URL Final: %s\n" +
            "Data/Hora: %s\n" +
            "---------------------------\n" +
            "Relatório gerado automaticamente via Selenium Headless.",
            sucesso ? "SUCESSO (PASS)" : "FALHA (FAIL)", 
            currentUrl, 
            java.time.LocalDateTime.now()
        );
        
        EmailService.enviarRelatorio(corpoEmail);
        assertTrue(sucesso, "Login falhou: redirecionamento incorreto ou credenciais inválidas. URL final: " + currentUrl);
        System.out.println(">>> CONTEÚDO DO E-MAIL QUE SERIA ENVIADO:\n" + corpoEmail);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}