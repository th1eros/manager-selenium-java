package dev.argus.qa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginInvalidoTest extends BaseTest {

    @Test
    @DisplayName("Login com credenciais inválidas deve exibir mensagem de erro")
    void testLoginInvalido() throws InterruptedException {
        driver.get("https://guiadomestico.com.br/publico/usuario/usuario_login.php");
        
        driver.findElement(By.id("EMAIL_USUARIO")).sendKeys("invalido@exemplo.com");
        driver.findElement(By.id("SENHA_USUARIO")).sendKeys("senhaerrada");
        driver.findElement(By.id("SENHA_USUARIO")).submit();
        
        Thread.sleep(3000);
        
        String pageText = driver.findElement(By.tagName("body")).getText();
        System.out.println(">>> Texto da página após login inválido:\n" + pageText);
        
        boolean mensagemErro = pageText.contains("Usuário ou senha inválida");
        
        assertTrue(mensagemErro, "Mensagem de erro não encontrada. Texto: " + pageText);
        
        assertTrue(driver.getCurrentUrl().contains("usuario_login.php"), 
                "Redirecionamento indevido para área interna");
    }   
}