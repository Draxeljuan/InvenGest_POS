package com.invengest.catalog.domain.gateway;

import com.invengest.catalog.domain.model.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoRepository {
    Optional<Producto> findById(String idProducto);
    List<Producto> findAll();
    List<Producto> findBySearchCriteria(String query); // Búsqueda por múltiples criterios
    List<Producto> findLowStockProducts(); // Productos por debajo de su stock_minimo
    Producto save(Producto producto);
    void deleteById(String idProducto);
}