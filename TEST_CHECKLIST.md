# Checklist de Testes Empresariais - Argus QA

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