package biblioteca;

import java.math.BigDecimal;

public final class Revista extends ItemBiblioteca {
    private static final BigDecimal MULTA_POR_DIA = new BigDecimal("1.00");

    public Revista(String codigo, String titulo) {
        super(codigo, titulo);
    }

    @Override
    public int prazoDevolucaoDias() {
        return 7;
    }

    @Override
    public BigDecimal multaPorDia() {
        return MULTA_POR_DIA;
    }
}
