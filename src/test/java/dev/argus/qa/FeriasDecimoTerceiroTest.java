package dev.argus.qa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FeriasDecimoTerceiroTest extends BaseTest {

    @Test
    @DisplayName("Validar valores na página de Férias e 13º Salário")
    void testValidarValoresFerias13() throws InterruptedException {
        String email = System.getenv("QA_EMAIL");
        String senha = System.getenv("QA_PWD");
        if (email == null || email.isEmpty()) throw new IllegalStateException("QA_EMAIL não definida");
        if (senha == null || senha.isEmpty()) throw new IllegalStateException("QA_PWD não definida");

        driver.get("https://guiadomestico.com.br/publico/usuario/usuario_login.php");
        driver.findElement(By.id("EMAIL_USUARIO")).sendKeys(email);
        driver.findElement(By.id("SENHA_USUARIO")).sendKeys(senha);
        driver.findElement(By.id("SENHA_USUARIO")).submit();
        Thread.sleep(3000);

        driver.get("https://guiadomestico.com.br/publico/diagnostico/diagnostico_ferias13.php");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.titleContains("Férias e 13º Salário"));

        boolean primeiraParcela = driver.getPageSource().contains("R$ 1.250,00");
        boolean segundaParcela = driver.getPageSource().contains("R$ 1.250,00");
        boolean totalLiquido13 = driver.getPageSource().contains("R$ 2.299,32");

        boolean salarioBaseFerias = driver.getPageSource().contains("R$ 2.500,00");
        boolean umTerco = driver.getPageSource().contains("R$ 833,34");
        boolean totalLiquidoFerias = driver.getPageSource().contains("R$ 3.044,74");

        assertTrue(primeiraParcela && segundaParcela && totalLiquido13,
            "Valores do 13º salário não conferem");
        assertTrue(salarioBaseFerias && umTerco && totalLiquidoFerias,
            "Valores das férias não conferem");
    }
}