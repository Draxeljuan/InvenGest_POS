package com.invengest.transaction.infrastructure.driver_adapter.client.adapter;

import com.invengest.transaction.domain.gateway.CatalogGateway;
import com.invengest.transaction.domain.model.ProductoCatalogo;
import com.invengest.transaction.domain.model.ServicioCatalogo;
import com.invengest.transaction.infrastructure.driver_adapter.client.dto.CatalogProductoDto;
import com.invengest.transaction.infrastructure.driver_adapter.client.dto.CatalogServicioDto;
import com.invengest.transaction.infrastructure.driver_adapter.client.feign.CatalogFeignClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CatalogAdapter implements CatalogGateway {

    private final CatalogFeignClient catalogFeignClient;

    @Override
    public Optional<ProductoCatalogo> obtenerProductoPorId(String idProducto) {
        try {
            CatalogProductoDto dto = catalogFeignClient.obtenerProductoPorId(idProducto);
            if (dto == null) {
                return Optional.empty();
            }
            return Optional.of(new ProductoCatalogo(
                    dto.idProducto(),
                    dto.nombre(),
                    dto.precioVenta(),
                    dto.stock(),
                    dto.stockMinimo()
            ));
        } catch (FeignException.NotFound e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<ServicioCatalogo> obtenerServicioPorId(Integer idServicio) {
        try {
            CatalogServicioDto dto = catalogFeignClient.obtenerServicioPorId(idServicio);
            if (dto == null) {
                return Optional.empty();
            }
            return Optional.of(new ServicioCatalogo(
                    dto.idServicio(),
                    dto.nombre(),
                    dto.descripcion(),
                    dto.precioSugerido()
            ));
        } catch (FeignException.NotFound e) {
            return Optional.empty();
        }
    }

    @Override
    public void descontarStock(String idProducto, Integer cantidad) {
        catalogFeignClient.descontarStock(idProducto, cantidad);
    }
}
