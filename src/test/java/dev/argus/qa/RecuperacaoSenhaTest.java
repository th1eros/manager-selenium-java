package dev.argus.qa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class RecuperacaoSenhaTest extends BaseTest {

    @Test
    @DisplayName("Recuperar Senha - Solicitar link de redefinição")
    void testRecuperarSenha() throws InterruptedException {
        String email = System.getenv("QA_EMAIL");
        if (email == null || email.isEmpty()) {
            fail("Variável de ambiente QA_EMAIL não definida");
        }

        driver.manage().window().setSize(new Dimension(1280, 1024));
        driver.get("https://guiadomestico.com.br/publico/usuario/usuario_lembrar.php");

        Thread.sleep(1000);
        try {
            WebElement cookieBanner = driver.findElement(By.xpath("//button[contains(text(), 'Aceitar') or contains(text(), 'Fechar')]"));
            cookieBanner.click();
            Thread.sleep(500);
        } catch (Exception ignored) {}

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("EMAIL_USUARIO")));
        emailField.sendKeys(email);

        WebElement submitButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("BTN_SUBMETER")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", submitButton);
        Thread.sleep(500);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(), 'Por favor, verifique sua caixa de mensagens')]")
        ));

        assertTrue(driver.getPageSource().contains("Por favor, verifique sua caixa de mensagens"));
    }
}