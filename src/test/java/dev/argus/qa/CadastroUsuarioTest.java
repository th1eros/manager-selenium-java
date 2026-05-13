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

public class CadastroUsuarioTest extends BaseTest {

    @Test
    @DisplayName("Cadastro de novo usuário com e-mail único por execução")
    void testCadastroUsuario() {
        // Gera e-mail único por execução; TEST_EMAIL_BASE opcional (padrão: qa@argus.dev)
        String base = System.getenv("TEST_EMAIL_BASE");
        if (base == null || base.isEmpty()) base = "qa@argus.dev";
        int atIdx = base.indexOf('@');
        String email = base.substring(0, atIdx) + "+" + System.currentTimeMillis() + base.substring(atIdx);
        String nome  = "Usuario QA Teste";
        String senha = "Senha@QA2026";

        driver.manage().window().setSize(new Dimension(1280, 1024));
        driver.get("https://guiadomestico.com.br/publico/usuario/usuario_cadastro.php");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("NOME_USUARIO")));

        // Preencher campos
        driver.findElement(By.id("NOME_USUARIO")).sendKeys(nome);
        driver.findElement(By.id("EMAIL_USUARIO")).sendKeys(email);
        driver.findElement(By.id("SENHA_USUARIO")).sendKeys(senha);
        driver.findElement(By.id("SENHA2_USUARIO")).sendKeys(senha);

        // Aceitar termos via JS (evita scroll/interação visual)
        WebElement checkTermos = driver.findElement(By.id("DECLARO_QUE_LI"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkTermos);

        // Submeter via JS (bypassa Bootstrap needs-validation listener)
        WebElement form = driver.findElement(By.id("FUSUARIO_CADASTRO"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].submit();", form);

        // Qualquer redirect fora da página de cadastro indica sucesso (index.php, login, etc.)
        boolean sucesso = false;
        try {
            wait.until(ExpectedConditions.not(
                ExpectedConditions.urlContains("usuario_cadastro.php")
            ));
            sucesso = true;
        } catch (Exception e) {
            sucesso = !driver.getCurrentUrl().contains("usuario_cadastro.php");
        }

        System.out.println("E-mail usado: " + email);
        System.out.println("URL final: " + driver.getCurrentUrl());
        if (!sucesso) {
            System.err.println("Page source (2000 chars):\n" +
                driver.getPageSource().substring(0, Math.min(2000, driver.getPageSource().length())));
        }

        assertTrue(sucesso, "Cadastro não confirmado. URL final: " + driver.getCurrentUrl());
    }
}