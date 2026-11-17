# Sistema de Biblioteca

As solucoes estao separadas por exercicio:

- `1-Java`: classes Java organizadas no pacote `biblioteca`.
- `2-Modelagem`: criacao das tabelas e insercao dos dados de teste no PostgreSQL.
- `3-Consulta`: as quatro consultas SQL solicitadas.

Para compilar e executar o exemplo Java no PowerShell:

```powershell
$sources = Get-ChildItem .\1-Java\src\main\java -Recurse -Filter *.java
javac -d .\1-Java\out $sources.FullName
java -cp .\1-Java\out biblioteca.Main
```

Execute primeiro `2-Modelagem/01-schema-e-dados.sql` no PostgreSQL e depois `3-Consulta/01-consultas.sql`.
