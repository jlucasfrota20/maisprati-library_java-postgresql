package biblioteca;

import java.math.BigDecimal;

public final class Livro extends ItemBiblioteca {
    private static final BigDecimal MULTA_POR_DIA = new BigDecimal("0.50");

    public Livro(String codigo, String titulo) {
        super(codigo, titulo);
    }

    @Override
    public int prazoDevolucaoDias() {
        return 14;
    }

    @Override
    public BigDecimal multaPorDia() {
        return MULTA_POR_DIA;
    }
}
