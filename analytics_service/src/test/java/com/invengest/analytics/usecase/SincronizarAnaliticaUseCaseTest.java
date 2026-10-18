package com.invengest.analytics.usecase;

import com.invengest.analytics.domain.gateway.AlertaStockGateway;
import com.invengest.analytics.domain.gateway.ResumenVentaGateway;
import com.invengest.analytics.domain.gateway.TopProductoGateway;
import com.invengest.analytics.domain.model.AlertaStock;
import com.invengest.analytics.domain.model.ItemVentaSincronizacion;
import com.invengest.analytics.domain.model.ResumenVentaDiaria;
import com.invengest.analytics.domain.model.TopProducto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SincronizarAnaliticaUseCaseTest {

    @Mock
    private ResumenVentaGateway resumenVentaGateway;

    @Mock
    private TopProductoGateway topProductoGateway;

    @Mock
    private AlertaStockGateway alertaStockGateway;

    private SincronizarAnaliticaUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new SincronizarAnaliticaUseCase(
                resumenVentaGateway,
                topProductoGateway,
                alertaStockGateway
        );
    }

    @Test
    @DisplayName("Debe acumular ventas diarias, incrementar cantidad de operaciones y actualizar top_productos")
    void debeRegistrarVentaSincronizada() {
        LocalDate fecha = LocalDate.of(2026, 8, 10);
        ResumenVentaDiaria existente = ResumenVentaDiaria.builder()
                .fecha(fecha)
                .totalVentas(new BigDecimal("100000.00"))
                .cantidadOperaciones(5)
                .build();

        when(resumenVentaGateway.findByFecha(fecha)).thenReturn(Optional.of(existente));
        when(resumenVentaGateway.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(topProductoGateway.findByIdProductoAndFecha("PROD-01", fecha)).thenReturn(Optional.empty());
        when(topProductoGateway.save(any())).thenAnswer(inv -> inv.getArgument(0));

        List<ItemVentaSincronizacion> items = List.of(
                new ItemVentaSincronizacion("PROD-01", "Cuaderno", 3)
        );

        ResumenVentaDiaria actualizado = useCase.registrarVentaSincronizada(
                fecha,
                new BigDecimal("45000.00"),
                items
        );

        assertThat(actualizado.getTotalVentas()).isEqualByComparingTo("145000.00");
        assertThat(actualizado.getCantidadOperaciones()).isEqualTo(6);

        ArgumentCaptor<TopProducto> topCaptor = ArgumentCaptor.forClass(TopProducto.class);
        verify(topProductoGateway).save(topCaptor.capture());
        assertThat(topCaptor.getValue().getIdProducto()).isEqualTo("PROD-01");
        assertThat(topCaptor.getValue().getCantidadVendida()).isEqualTo(3);
    }

    @Test
    @DisplayName("Debe clasificar como 'Agotado' cuando el stock es 0 y como 'Bajo Stock' cuando es < 10")
    void debeSincronizarAlertaStockCritico() {
        when(alertaStockGateway.findByIdProducto("PROD-02")).thenReturn(Optional.empty());
        when(alertaStockGateway.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Optional<AlertaStock> alertaAgotado = useCase.sincronizarAlertaStock("PROD-02", "Marcador", (short) 0);
        assertThat(alertaAgotado).isPresent();
        assertThat(alertaAgotado.get().getEstado()).isEqualTo("Agotado");

        Optional<AlertaStock> alertaBajo = useCase.sincronizarAlertaStock("PROD-02", "Marcador", (short) 5);
        assertThat(alertaBajo).isPresent();
        assertThat(alertaBajo.get().getEstado()).isEqualTo("Bajo Stock");
    }

    @Test
    @DisplayName("Debe eliminar la alerta de stock cuando el stock vuelve a ser >= 10")
    void debeEliminarAlertaCuandoStockSeNormaliza() {
        Optional<AlertaStock> resultado = useCase.sincronizarAlertaStock("PROD-02", "Marcador", (short) 15);

        assertThat(resultado).isEmpty();
        verify(alertaStockGateway).deleteByIdProducto("PROD-02");
    }
}
