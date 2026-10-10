package com.invengest.catalog.infrastructure.driver_adapter.jpa_repository.interfaces;

import com.invengest.catalog.infrastructure.driver_adapter.entity.ProductoData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoJpaRepository extends JpaRepository<ProductoData, String> {

    boolean existsByCategoria_IdCategoria(Integer idCategoriaId);

    @Query("SELECT p FROM ProductoData p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.idProducto) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<ProductoData> searchByCriteria(@Param("query") String query);

    @Query("SELECT p FROM ProductoData p WHERE p.stock <= p.stockMinimo")
    List<ProductoData> findLowStockProducts();
}