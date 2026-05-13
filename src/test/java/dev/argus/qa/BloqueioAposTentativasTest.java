package dev.argus.qa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BloqueioAposTentativasTest extends BaseTest {

    @Test
    @DisplayName("Bloqueio após 3 tentativas de login inválidas")
    void testBloqueioAposTentativas() throws InterruptedException {
        String email = System.getenv("QA_EMAIL"); 
        if (email == null || email.isEmpty()) {
            throw new IllegalStateException("QA_EMAIL não definida");
        }

        driver.get("https://guiadomestico.com.br/publico/usuario/usuario_login.php");

        for (int tentativa = 1; tentativa <= 3; tentativa++) {
            driver.findElement(By.id("EMAIL_USUARIO")).clear();
            driver.findElement(By.id("EMAIL_USUARIO")).sendKeys(email);
            driver.findElement(By.id("SENHA_USUARIO")).clear();
            driver.findElement(By.id("SENHA_USUARIO")).sendKeys("senhaerradissima" + tentativa);
            driver.findElement(By.id("SENHA_USUARIO")).submit();
            Thread.sleep(1500);
        }

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        boolean bloqueado = false;
        try {
            bloqueado = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(), 'tentativas') or contains(text(), 'bloqueado')]")
            )).isDisplayed();
        } catch (Exception e) {
            bloqueado = !driver.findElement(By.id("SENHA_USUARIO")).isEnabled();
        }

        assertTrue(bloqueado, "Sistema não bloqueou após 3 tentativas falhas");
    }
}