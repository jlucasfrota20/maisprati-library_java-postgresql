package biblioteca;

import java.time.LocalDate;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        Biblioteca biblioteca = new Biblioteca(10, 5, 3);
        biblioteca.cadastrarItem(new Livro("LIV-001", "Dom Casmurro"));
        biblioteca.cadastrarItem(new Revista("REV-001", "Ciencia Hoje"));
        biblioteca.cadastrarItem(new Livro("LIV-002", "O Cortico"));
        biblioteca.cadastrarItem(new Revista("REV-002", "National Geographic"));

        Aluno aluno = new Aluno("Ana Souza");
        biblioteca.cadastrarUsuario(aluno);
        biblioteca.cadastrarUsuario(new Professor("Carlos Lima"));

        for (String codigo : new String[]{"LIV-001", "REV-001", "LIV-002"}) {
            System.out.printf("Emprestimo de %s: %s%n", codigo, biblioteca.emprestar(codigo, aluno));
        }

        Biblioteca.ResultadoEmprestimo resultado = biblioteca.emprestar("REV-002", aluno);
        System.out.printf("Quarto emprestimo: %s%n", resultado);
        if (resultado == Biblioteca.ResultadoEmprestimo.LIMITE_ATINGIDO) {
            System.out.println("Emprestimo recusado: limite de itens do aluno atingido.");
        }

        System.out.printf("Multa na devolucao de LIV-001: R$ %.2f%n",
                biblioteca.devolver("LIV-001", LocalDate.now().plusDays(16)));
        System.out.printf("Novo emprestimo apos devolucao: %s%n",
                biblioteca.emprestar("LIV-001", aluno));
        biblioteca.listarAcervo();
    }
}
