# manager-selenium-java

Engine de automação de testes E2E (End-to-End) focado em **DevSecOps** e **escalabilidade**. Projetado para execução em ambientes de alta segurança (Hard Mode) e arquiteturas isoladas.

## 🛠 Tech Stack
- **Java** (Maven)
- **Selenium WebDriver** (Headless Chromium)
- **JUnit 5**
- **SMTP/javax.mail** (Reporting Service)

## 🛡 Segurança e Hardening
O projeto utiliza **Zero-Footprint Personalization**, garantindo que nenhuma credencial seja armazenada no código-fonte. O gerenciamento de segredos é feito via variáveis de ambiente do sistema.

## 🚀 Como Executar

### 1. Definir Variáveis (OCI / Local)
Exporte as chaves necessárias antes da execução:
```bash
export QA_EMAIL="usuario"
export QA_PWD="senha"
export EMAIL_USER="seu-gmail@gmail.com"
export EMAIL_PASS="sua-senha-de-app"
export EMAIL_DEST="destino1@com,destino2@com"
# manager-selenium-java
