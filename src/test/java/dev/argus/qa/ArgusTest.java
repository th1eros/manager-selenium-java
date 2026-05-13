package dev.argus.qa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ArgusTest extends BaseTest {

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
            "Responsável QA: Argus\n" +
            "Status do Teste: %s\n" +
            "URL Final: %s\n" +
            "Data/Hora: %s\n" +
            "---------------------------\n" +
            "Relatório gerado automaticamente via Selenium Headless.",
            sucesso ? "SUCESSO (PASS)" : "FALHA (FAIL)", 
            currentUrl, 
            java.time.LocalDateTime.now()
        );

        System.out.println(">>> CONTEÚDO DO E-MAIL QUE SERIA ENVIADO:\n" + corpoEmail);
        EmailService.enviarRelatorio(corpoEmail);
        assertTrue(sucesso, "Login falhou: redirecionamento incorreto ou credenciais inválidas. URL final: " + currentUrl);
    }
}