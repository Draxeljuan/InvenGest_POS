package com.invengest.analytics.usecase;

import com.invengest.analytics.domain.exception.BusinessRuleException;
import com.invengest.analytics.domain.gateway.AlertaStockGateway;
import com.invengest.analytics.domain.model.AlertaStock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultarAlertasStockUseCaseTest {

    @Mock
    private AlertaStockGateway alertaStockGateway;

    private ConsultarAlertasStockUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ConsultarAlertasStockUseCase(alertaStockGateway);
    }

    @Test
    @DisplayName("Debe retornar las alertas filtradas cuando el estado es válido ('Agotado' o 'Bajo Stock')")
    void debeRetornarAlertasPorEstadoValido() {
        AlertaStock alerta = AlertaStock.builder()
                .idAlerta(1)
                .idProducto("PROD-01")
                .nombreProducto("Borrador")
                .stockActual((short) 0)
                .estado("Agotado")
                .fechaAlerta(LocalDateTime.now())
                .build();

        when(alertaStockGateway.findByEstado("Agotado")).thenReturn(List.of(alerta));

        List<AlertaStock> resultado = useCase.obtenerPorEstado("Agotado");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEstado()).isEqualTo("Agotado");
    }

    @Test
    @DisplayName("Debe lanzar BusinessRuleException cuando el estado es inválido")
    void debeLanzarExcepcionConEstadoInvalido() {
        assertThatThrownBy(() -> useCase.obtenerPorEstado("Normal"))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("'Agotado' o 'Bajo Stock'");
    }
}
