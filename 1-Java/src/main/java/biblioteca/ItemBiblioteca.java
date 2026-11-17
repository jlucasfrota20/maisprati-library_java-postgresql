package biblioteca;

import java.math.BigDecimal;

public abstract class ItemBiblioteca {
    private final String codigo;
    private final String titulo;
    private boolean disponivel = true;

    protected ItemBiblioteca(String codigo, String titulo) {
        this.codigo = validarTexto(codigo, "codigo");
        this.titulo = validarTexto(titulo, "titulo");
    }

    public final String getCodigo() {
        return codigo;
    }

    public final String getTitulo() {
        return titulo;
    }

    public final boolean estaDisponivel() {
        return disponivel;
    }

    public abstract int prazoDevolucaoDias();

    public abstract BigDecimal multaPorDia();

    final void marcarEmprestado() {
        disponivel = false;
    }

    final void marcarDevolvido() {
        disponivel = true;
    }

    private static String validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " nao pode ser vazio");
        }
        return valor;
    }
}
