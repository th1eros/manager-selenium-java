# Argus QA – Automação de Testes E2E para Sistemas Críticos

Engine de automação de testes End‑to‑End focada em **DevSecOps**, **escalabilidade** e **hardening**. Projetada para execução em pipelines CI/CD (GitHub Actions) e também em ambientes isolados (ARM64, sem interface gráfica). Ideal para validar aplicações empresaria
# argus-qa

Engine de automação E2E para o **Guia Doméstico** — cobertura de autenticação, cálculos previdenciários e notificações via pipeline DevSecOps.

---

## Stack

| Camada | Tecnologia | Versão |
|---|---|---|
| Runtime | OpenJDK (ARM64) | 21.0.10 |
| Build | Apache Maven | 3.8.7 |
| Browser | Selenium WebDriver | 4.15.0 |
| Driver | GeckoDriver (aarch64) | 0.33.0 |
| Test Runner | JUnit Jupiter | 5.10.0 |
| SMTP | javax.mail | 1.6.2 |
| CI/CD | GitHub Actions | ubuntu-latest |

---

## Arquitetura

```
argus-qa/
├── .github/workflows/qa-pipeline.yml
├── src/
│   ├── main/java/com/malebolge/sandbox/
│   │   ├── auth/AuthorityChecker.java
│   │   ├── core/GerenciatorPermission.java
│   │   └── core/PrivilegeManager.java
│   └── test/java/dev/malebolge/qa/
│       ├── GuiaDomesticoTest.java
│       └── EmailService.java
└── pom.xml
```

```mermaid
flowchart TD
    A[Push / PR → main] --> B[GitHub Runner]
    B --> C[Setup JDK 21]
    C --> D[GeckoDriver Setup]
    D --> E[mvn test]
    E --> F{Status?}
    F -->|Pass| G[Surefire Reports]
    F -->|Fail| H[Logs + Screenshots]
    G --> I[Upload Artifacts]
    H --> I
    I --> J[EmailService → Relatório]
    F -->|Fail| K[Alert: Falha]
```

---

## Pré-requisitos

- Java 21 (OpenJDK)
- Maven 3.8+
- Firefox (headless) + GeckoDriver `aarch64`
- Variáveis de ambiente configuradas (ver abaixo)

---

## Configuração de Segredos

### Local

```bash
export QA_EMAIL="user@domain.com"
export QA_PWD="password"
export EMAIL_USER="smtp_user@gmail.com"
export EMAIL_PASS="app_password"
export EMAIL_DEST="target@domain.com"
```

### GitHub Actions — Secrets obrigatórios

| Secret | Descrição |
|---|---|
| `QA_EMAIL` | Login da aplicação alvo |
| `QA_PWD` | Senha da aplicação alvo |
| `EMAIL_USER` | Conta SMTP (Gmail/Outlook) |
| `EMAIL_PASS` | App Password |
| `EMAIL_DEST` | Destinatário dos relatórios |

---

## Execução

```bash
mvn test -Dtest=GuiaDomesticoTest
```

> ARM64 sem display: GeckoDriver em modo headless nativo — `xvfb-run` não necessário.

---

## Cobertura de Testes

## Autenticação e Conta
- [x] Login com credenciais válidas (redireciona para área logada)
- [x] Recuperação de senha – solicitação de link
- [x] Login com credenciais inválidas (mensagem de erro)
- [x] Logout (encerramento de sessão)
- [ ] Bloqueio temporário após 3 tentativas falhas – **N/A**: o sistema Guia Doméstico não implementa bloqueio por tentativas.
- [x] Cadastro de novo trabalhador doméstico (fluxo completo) – **OK**: implementado com e-mail único dinâmico.
- [ ] Alteração de e-mail/senha – **N/A**: o sistema redireciona para recuperação de senha (já testada). Não há alteração direta de senha logado.

## Funcionalidades Core
- [x] Cálculo de guia (INSS, FGTS, multa rescisória) – **OK**: testado via `DiagnosticoConfigTest` (configuração de diagnóstico e folha de pagamento).
- [ ] Geração de extrato para impressão – **N/A**: o programa não oferece essa funcionalidade.
- [x] Simulação de férias + 1/3 – **OK**: validado via página estática `diagnostico_ferias13.php` (valores fixos).
- [x] Cálculo de décimo terceiro – **OK**: incluso na mesma página de férias/13º.

## Relatórios e Notificações
- [x] Envio de e-mail com resultado do teste (EmailService)
- [ ] Download de PDF da guia – **N/A**: não há geração de PDF no programa.
- [ ] Envio de cópia para e-mail do usuário – **N/A**: o sistema não envia cópia do diagnóstico por e-mail (apenas link de recuperação).

## Segurança e Robustez
- [x] Elementos sensíveis não expostos no source (headless) – **OK**: os testes rodam em headless, sem interface.
- [x] Timeout e tratamento de elementos ausentes – **OK**: uso de `WebDriverWait` e cliques via JavaScript.
- [x] Suporte a ambiente headless (CI/CD) – **OK**: pipeline GitHub Actions com Chrome headless.

---

## Segurança — DevSecOps

| Controle | Implementação |
|---|---|
| Zero-Footprint | Nenhuma credencial hardcoded |
| Secret Injection | Env vars locais / GitHub Secrets |
| Ephemeral Runners | Runners descartáveis sem persistência |
| Headless-Only | Superfície de ataque reduzida |
| ARM64 Hardening | GeckoDriver nativo — sem Selenium Manager |

---

## Licença

MIT — veja [`LICENSE`](./LICENSE).
