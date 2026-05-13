# Checklist de Testes Empresariais - Argus QA

## Autenticação e Conta
- [x] Login com credenciais válidas (redireciona para área logada)
- [x] Recuperação de senha – solicitação de link
- [x] Login com credenciais inválidas (mensagem de erro)
- [ ] Logout (encerramento de sessão)
- [ ] Bloqueio temporário após 3 tentativas falhas
- [ ] Cadastro de novo trabalhador doméstico (fluxo completo)
- [ ] Alteração de e-mail/senha

## Funcionalidades Core
- [ ] Cálculo de guia (INSS, FGTS, multa rescisória)
- [ ] Geração de extrato para impressão
- [ ] Simulação de férias + 1/3
- [ ] Cálculo de décimo terceiro

## Relatórios e Notificações
- [x] Envio de e-mail com resultado do teste (EmailService)
- [ ] Download de PDF da guia
- [ ] Envio de cópia para e-mail do usuário

## Segurança e Robustez
- [ ] Elementos sensíveis não expostos no source (headless)
- [ ] Timeout e tratamento de elementos ausentes
- [ ] Suporte a ambiente headless (CI/CD)