package biblioteca;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public final class Biblioteca {
    public enum ResultadoEmprestimo {
        SUCESSO,
        ITEM_NAO_ENCONTRADO,
        ITEM_INDISPONIVEL,
        USUARIO_NAO_CADASTRADO,
        LIMITE_ATINGIDO,
        LIMITE_EMPRESTIMOS_ATIVOS_ATINGIDO
    }

    private final ItemBiblioteca[] acervo;
    private final Usuario[] usuarios;
    private final Emprestimo[] emprestimos;

    public Biblioteca(int capacidadeAcervo, int capacidadeUsuarios, int capacidadeEmprestimos) {
        if (capacidadeAcervo <= 0 || capacidadeUsuarios <= 0 || capacidadeEmprestimos <= 0) {
            throw new IllegalArgumentException("capacidades devem ser maiores que zero");
        }
        acervo = new ItemBiblioteca[capacidadeAcervo];
        usuarios = new Usuario[capacidadeUsuarios];
        emprestimos = new Emprestimo[capacidadeEmprestimos];
    }

    public void cadastrarItem(ItemBiblioteca item) {
        Objects.requireNonNull(item);
        if (buscarItem(item.getCodigo()) != null) {
            throw new IllegalArgumentException("codigo de item ja cadastrado: " + item.getCodigo());
        }
        int posicao = primeiraPosicaoVazia(acervo);
        if (posicao < 0) {
            throw new IllegalStateException("capacidade do acervo atingida");
        }
        acervo[posicao] = item;
    }

    public void cadastrarUsuario(Usuario usuario) {
        Objects.requireNonNull(usuario);
        for (Usuario cadastrado : usuarios) {
            if (cadastrado == usuario) {
                throw new IllegalArgumentException("usuario ja cadastrado: " + usuario.getNome());
            }
        }
        int posicao = primeiraPosicaoVazia(usuarios);
        if (posicao < 0) {
            throw new IllegalStateException("capacidade de usuarios atingida");
        }
        usuarios[posicao] = usuario;
    }

    public ResultadoEmprestimo emprestar(String codigo, Usuario usuario) {
        Objects.requireNonNull(codigo);
        Objects.requireNonNull(usuario);
        ItemBiblioteca item = buscarItem(codigo);
        if (item == null) {
            return ResultadoEmprestimo.ITEM_NAO_ENCONTRADO;
        }
        if (!usuarioCadastrado(usuario)) {
            return ResultadoEmprestimo.USUARIO_NAO_CADASTRADO;
        }
        if (!item.estaDisponivel()) {
            return ResultadoEmprestimo.ITEM_INDISPONIVEL;
        }
        if (!usuario.podeEmprestar()) {
            return ResultadoEmprestimo.LIMITE_ATINGIDO;
        }
        int posicaoEmprestimo = primeiraPosicaoDisponivelEmprestimo();
        if (posicaoEmprestimo < 0) {
            return ResultadoEmprestimo.LIMITE_EMPRESTIMOS_ATIVOS_ATINGIDO;
        }

        emprestimos[posicaoEmprestimo] = new Emprestimo(item, usuario, LocalDate.now());
        item.marcarEmprestado();
        usuario.registrarEmprestimo();
        return ResultadoEmprestimo.SUCESSO;
    }

    public BigDecimal devolver(String codigo) {
        return devolver(codigo, LocalDate.now());
    }

    public BigDecimal devolver(String codigo, LocalDate dataDevolucao) {
        Objects.requireNonNull(codigo);
        Objects.requireNonNull(dataDevolucao);
        ItemBiblioteca item = buscarItem(codigo);
        if (item == null) {
            throw new IllegalArgumentException("item nao encontrado: " + codigo);
        }
        Emprestimo emprestimo = buscarEmprestimoAberto(item);
        if (emprestimo == null) {
            throw new IllegalStateException("item nao possui emprestimo em aberto: " + codigo);
        }

        BigDecimal multa = emprestimo.finalizar(dataDevolucao);
        item.marcarDevolvido();
        emprestimo.getUsuario().registrarDevolucao();
        return multa;
    }

    public void listarAcervo() {
        for (ItemBiblioteca item : acervo) {
            if (item != null) {
                String disponibilidade = item.estaDisponivel() ? "disponivel" : "emprestado";
                System.out.printf("%s | %s | %s%n", item.getCodigo(), item.getTitulo(), disponibilidade);
            }
        }
    }

    private ItemBiblioteca buscarItem(String codigo) {
        for (ItemBiblioteca item : acervo) {
            if (item != null && item.getCodigo().equals(codigo)) {
                return item;
            }
        }
        return null;
    }

    private Emprestimo buscarEmprestimoAberto(ItemBiblioteca item) {
        for (Emprestimo emprestimo : emprestimos) {
            if (emprestimo != null && emprestimo.estaAberto() && emprestimo.getItem() == item) {
                return emprestimo;
            }
        }
        return null;
    }

    private boolean usuarioCadastrado(Usuario usuario) {
        for (Usuario cadastrado : usuarios) {
            if (cadastrado == usuario) {
                return true;
            }
        }
        return false;
    }

    private int primeiraPosicaoDisponivelEmprestimo() {
        for (int i = 0; i < emprestimos.length; i++) {
            if (emprestimos[i] == null || !emprestimos[i].estaAberto()) {
                return i;
            }
        }
        return -1;
    }

    private static int primeiraPosicaoVazia(Object[] elementos) {
        for (int i = 0; i < elementos.length; i++) {
            if (elementos[i] == null) {
                return i;
            }
        }
        return -1;
    }
}
