package dev.malebolge.qa;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;

public class GuiaDomesticoTest {
    private WebDriver driver;

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        
        if (System.getenv("CI") != null) {
            options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");
            options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36");
            options.setBinary("/usr/bin/google-chrome");
        }
        
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
    void testLoginCompletoComEmail() {
        String email = System.getenv("QA_EMAIL");
        String senha = System.getenv("QA_PWD");

        driver.get("https://guiadomestico.com.br/publico/usuario/usuario_login.php");
        
        driver.findElement(By.id("EMAIL_USUARIO")).sendKeys(email);
        driver.findElement(By.id("SENHA_USUARIO")).sendKeys(senha);
        driver.findElement(By.id("SENHA_USUARIO")).submit();
        
        boolean sucesso = driver.getCurrentUrl().contains("painel") || driver.getCurrentUrl().contains("home");
        
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
            driver.getCurrentUrl(), 
            java.time.LocalDateTime.now()
        );

        EmailService.enviarRelatorio(corpoEmail);
        assertTrue(sucesso, "Login falhou: redirecionamento incorreto.");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}