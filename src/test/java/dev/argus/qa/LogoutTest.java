package dev.argus.qa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogoutTest extends BaseTest {

    @Test
    @DisplayName("Logout - encerrar sessão e retornar à página pública")
    void testLogout() throws InterruptedException {

        String email = System.getenv("QA_EMAIL");
        String senha = System.getenv("QA_PWD");
        if (email == null || email.isEmpty()) {
            throw new IllegalStateException("QA_EMAIL não definida");
        }
        if (senha == null || senha.isEmpty()) {
            throw new IllegalStateException("QA_PWD não definida");
        }

        // Faz login
        driver.get("https://guiadomestico.com.br/publico/usuario/usuario_login.php");
        driver.findElement(By.id("EMAIL_USUARIO")).sendKeys(email);
        driver.findElement(By.id("SENHA_USUARIO")).sendKeys(senha);
        driver.findElement(By.id("SENHA_USUARIO")).submit();

        Thread.sleep(3000); // aguarda login

        // Acessa URL de logout
        driver.get("https://guiadomestico.com.br/acesso/logout.php");

        Thread.sleep(2000);
        String currentUrl = driver.getCurrentUrl();
        boolean sucesso = currentUrl.contains("login") ||
                          currentUrl.contains("usuario_login") ||
                          currentUrl.equals("https://guiadomestico.com.br/index.php");

        assertTrue(sucesso, "Logout não redirecionou para página pública. URL final: " + currentUrl);
    }
}