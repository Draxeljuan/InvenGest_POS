package com.invengest.catalog.infrastructure.driver_adapter.rest.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ProductoProveedorRequest(
        @NotBlank(message = "El id de producto es obligatorio") String idProducto,
        @NotNull(message = "El id de proveedor es obligatorio") Integer idProveedor,
        @NotNull @DecimalMin("0.0") BigDecimal precioCompraProveedor
) {}
