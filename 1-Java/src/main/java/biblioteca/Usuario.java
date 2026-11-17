package biblioteca;

public abstract class Usuario {
    private final String nome;
    private int quantidadeEmprestada;

    protected Usuario(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("nome nao pode ser vazio");
        }
        this.nome = nome;
    }

    public final String getNome() {
        return nome;
    }

    public final int getQuantidadeEmprestada() {
        return quantidadeEmprestada;
    }

    public abstract int limiteItens();

    public final boolean podeEmprestar() {
        return quantidadeEmprestada < limiteItens();
    }

    final void registrarEmprestimo() {
        if (!podeEmprestar()) {
            throw new IllegalStateException("limite de emprestimos atingido");
        }
        quantidadeEmprestada++;
    }

    final void registrarDevolucao() {
        if (quantidadeEmprestada == 0) {
            throw new IllegalStateException("usuario nao possui emprestimos ativos");
        }
        quantidadeEmprestada--;
    }
}
