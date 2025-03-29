# Andre Goulart teste NG Billing

## Para rodar o projeto
Por padrão está configurado pra rodar com o banco em memória H2.

Ao abrir o projeto na IDE e rodar o main() da classe AdredevtesAppllication.

### Caso deseja testar/rodar o projeto com o PostgreSQL
- Necessário ter PostgreSQL instalado localmente
- Copiar o conteudo que tem no arquivo application_yml_postgres.txt e colar dentro do application.yml
- Conteúdo dentro do application.yml deve ser substituído pelo conteúdo que tem no arquivo application_yml_postgres.txt
- Descomentar o código dentro da classe DatabaseConfiguration.java