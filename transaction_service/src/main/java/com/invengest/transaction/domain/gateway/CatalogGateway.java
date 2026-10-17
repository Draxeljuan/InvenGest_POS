package com.invengest.transaction.domain.gateway;

import com.invengest.transaction.domain.model.ProductoCatalogo;
import com.invengest.transaction.domain.model.ServicioCatalogo;

import java.util.Optional;

public interface CatalogGateway {
    Optional<ProductoCatalogo> obtenerProductoPorId(String idProducto);
    Optional<ServicioCatalogo> obtenerServicioPorId(Integer idServicio);
    void descontarStock(String idProducto, Integer cantidad);
}
