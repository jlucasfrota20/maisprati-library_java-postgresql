package biblioteca;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

final class Emprestimo {
    private final ItemBiblioteca item;
    private final Usuario usuario;
    private final LocalDate dataRetirada;
    private final LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucao;
    private BigDecimal valorMulta;

    Emprestimo(ItemBiblioteca item, Usuario usuario, LocalDate dataRetirada) {
        this.item = Objects.requireNonNull(item);
        this.usuario = Objects.requireNonNull(usuario);
        this.dataRetirada = Objects.requireNonNull(dataRetirada);
        this.dataDevolucaoPrevista = dataRetirada.plusDays(item.prazoDevolucaoDias());
    }

    ItemBiblioteca getItem() {
        return item;
    }

    Usuario getUsuario() {
        return usuario;
    }

    boolean estaAberto() {
        return dataDevolucao == null;
    }

    BigDecimal finalizar(LocalDate dataDevolucao) {
        Objects.requireNonNull(dataDevolucao);
        if (!estaAberto()) {
            throw new IllegalStateException("emprestimo ja foi devolvido");
        }
        if (dataDevolucao.isBefore(dataRetirada)) {
            throw new IllegalArgumentException("devolucao nao pode anteceder a retirada");
        }
        long diasAtraso = Math.max(0, ChronoUnit.DAYS.between(dataDevolucaoPrevista, dataDevolucao));
        this.dataDevolucao = dataDevolucao;
        this.valorMulta = item.multaPorDia()
                .multiply(BigDecimal.valueOf(diasAtraso))
                .setScale(2, RoundingMode.HALF_UP);
        return valorMulta;
    }
}
