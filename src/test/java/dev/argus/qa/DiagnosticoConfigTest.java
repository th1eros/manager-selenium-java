package dev.argus.qa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DiagnosticoConfigTest extends BaseTest {

    @Test
    @DisplayName("Configurar diagnóstico e validar cálculo da folha de pagamento")
    void testDiagnosticoFolhaPagamento() throws InterruptedException {
        String email = System.getenv("QA_EMAIL");
        String senha = System.getenv("QA_PWD");
        if (email == null || senha == null) {
            throw new IllegalStateException("QA_EMAIL e QA_PWD devem estar definidas");
        }

        String cargoValue = "1";
        String salarioBruto = "3000";
        String dataInicio = LocalDate.now().minusMonths(6).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String horasDia = "8";
        String horasSabado = "4";
        String diasSemana = "5";

        driver.manage().window().setSize(new Dimension(1280, 1024));

        // 1. Login
        driver.get("https://guiadomestico.com.br/publico/usuario/usuario_login.php");
        driver.findElement(By.id("EMAIL_USUARIO")).sendKeys(email);
        driver.findElement(By.id("SENHA_USUARIO")).sendKeys(senha);
        driver.findElement(By.id("SENHA_USUARIO")).submit();
        Thread.sleep(3000);

        // 2. Acessar página de configuração do diagnóstico
        driver.get("https://guiadomestico.com.br/publico/diagnostico/diagnostico_config.php");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("CHAVE_CARGO")));

        // 3. Preencher formulário
        new Select(driver.findElement(By.id("CHAVE_CARGO"))).selectByValue(cargoValue);
        driver.findElement(By.id("SALARIO_USUARIO")).clear();
        driver.findElement(By.id("SALARIO_USUARIO")).sendKeys(salarioBruto);
        driver.findElement(By.id("DTI_USUARIO")).clear();
        driver.findElement(By.id("DTI_USUARIO")).sendKeys(dataInicio);
        driver.findElement(By.id("HRDIA_USUARIO")).clear();
        driver.findElement(By.id("HRDIA_USUARIO")).sendKeys(horasDia);
        driver.findElement(By.id("HRDIASAB_USUARIO")).clear();
        driver.findElement(By.id("HRDIASAB_USUARIO")).sendKeys(horasSabado);
        driver.findElement(By.id("DIASEMANA_USUARIO")).clear();
        driver.findElement(By.id("DIASEMANA_USUARIO")).sendKeys(diasSemana);

        // 4. Submeter formulário via JavaScript
        WebElement btnSubmit = driver.findElement(By.id("BTN_SUBMETER"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btnSubmit);
        Thread.sleep(500);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnSubmit);

        // 5. Aguardar redirecionamento
        Thread.sleep(3000);
        String currentUrl = driver.getCurrentUrl();
        System.out.println("URL após submit: " + currentUrl);

        if (currentUrl.contains("diagnostico_config.php")) {
            try {
                WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(),'salvo com sucesso') or contains(text(),'configuração salva')]")
                ));
                System.out.println("Mensagem de sucesso: " + successMsg.getText());
            } catch (Exception e) {
                System.out.println("Nenhuma mensagem de sucesso visível, mas prosseguindo...");
            }
        }

        // 6. Acessar página de folha de pagamento
        driver.get("https://guiadomestico.com.br/publico/diagnostico/diagnostico_folhapagto.php");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'Salário Bruto')]")));

        // 7. Extrair salário bruto — div com 'rounded' e 'border' contendo o texto alvo
        WebElement brutoCard = driver.findElement(By.xpath(
            "//div[contains(@class,'rounded') and contains(@class,'border')][.//span[contains(text(),'Salário Bruto')]]"));
        WebElement brutoValue = brutoCard.findElement(By.xpath(".//span[contains(@class,'h2')]"));
        String brutoText = brutoValue.getText();

        // 8. Extrair salário líquido — intersecção 'rounded'+'shadow' exclui o card container externo
        WebElement liquidoCard = driver.findElement(By.xpath(
            "//div[contains(@class,'rounded') and contains(@class,'shadow')][.//span[contains(text(),'Salário líquido')]]"));
        WebElement liquidoValue = liquidoCard.findElement(By.xpath(".//span[contains(@class,'h2')]"));
        String liquidoText = liquidoValue.getText();

        double bruto = Double.parseDouble(brutoText.replace("R$", "").replace(".", "").replace(",", ".").trim());
        double liquido = Double.parseDouble(liquidoText.replace("R$", "").replace(".", "").replace(",", ".").trim());

        System.out.println("Salário bruto: R$ " + bruto);
        System.out.println("Salário líquido: R$ " + liquido);

        assertTrue(bruto > 0, "Salário bruto deve ser maior que zero");
        assertTrue(liquido > 0 && liquido < bruto,
            String.format("Salário líquido (%.2f) deve ser positivo e menor que o bruto (%.2f)", liquido, bruto));

        double desconto = bruto - liquido;
        double percentual = (desconto / bruto) * 100;
        assertTrue(percentual >= 7.5 && percentual <= 14,
            String.format("Percentual de desconto %.2f%% fora da faixa esperada (7.5%% a 14%%)", percentual));
    }
}