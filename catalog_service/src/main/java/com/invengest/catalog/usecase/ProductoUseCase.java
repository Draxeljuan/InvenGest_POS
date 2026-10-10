package com.invengest.catalog.usecase;

import com.invengest.catalog.domain.exception.EntityNotFoundException;
import com.invengest.catalog.domain.gateway.CategoriaGateway;
import com.invengest.catalog.domain.gateway.EstadoProductoGateway;
import com.invengest.catalog.domain.gateway.ProductoGateway;
import com.invengest.catalog.domain.model.EstadoProducto;
import com.invengest.catalog.domain.model.Producto;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
public class ProductoUseCase {

    private final ProductoGateway productoGateway;
    private final EstadoProductoGateway estadoProductoGateway;
    private final CategoriaGateway categoriaGateway;

    private final Random random = new Random();

    public Producto registrar(Producto producto) {
        validarInvariantes(producto);
        
        producto.setIdProducto(generarIdProducto());
        
        return productoGateway.save(producto);
    }

    public Producto actualizar(Producto producto) {
        if (!productoGateway.existsById(producto.getIdProducto())) {
            throw new EntityNotFoundException("El producto con código " + producto.getIdProducto() + " no existe.");
        }
        validarInvariantes(producto);
        return productoGateway.save(producto);
    }

    private void validarInvariantes(Producto producto) {
        if (producto.getCategoria() == null || producto.getCategoria().getIdCategoria() == null ||
            categoriaGateway.findById(producto.getCategoria().getIdCategoria()).isEmpty()) {
            throw new EntityNotFoundException("La categoría asignada no existe.");
        }
        if (producto.getPrecioVenta() != null && producto.getCostoCompra() != null && 
            producto.getPrecioVenta().compareTo(producto.getCostoCompra()) < 0) {
            throw new IllegalArgumentException("El precio de venta debe ser mayor o igual al costo de compra.");
        }
        if (producto.getStock() != null && producto.getStock() < 0) {
            throw new IllegalArgumentException("El stock actual no puede ser negativo.");
        }
        if (producto.getStockMinimo() != null && producto.getStockMinimo() < 0) {
            throw new IllegalArgumentException("El stock mínimo no puede ser negativo.");
        }
    }

    private String generarIdProducto() {
        String id;
        do {
            int code = random.nextInt(10000);
            id = String.format("PROD-%04d", code);
        } while (productoGateway.existsById(id));
        return id;
    }

    public List<Producto> obtenerTodos() {
        return productoGateway.findAll();
    }

    public Producto obtenerPorId(String idProducto) {
        return productoGateway.findById(idProducto)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con el código: " + idProducto));
    }

    public List<Producto> buscarPorCriterio(String query) {
        return productoGateway.findBySearchCriteria(query);
    }

    public List<Producto> obtenerProductosStockBajo() {
        return productoGateway.findLowStockProducts();
    }

    public Producto descontarStock(String idProducto, Integer cantidad) {
        Producto producto = obtenerPorId(idProducto);

        if (producto.getStock() < cantidad) {
            throw new IllegalArgumentException("Stock insuficiente para el producto: " + producto.getNombre());
        }

        producto.setStock(producto.getStock() - cantidad);

        if (producto.getStock() == 0) {
            EstadoProducto estadoAgotado = estadoProductoGateway.findById(3)
                    .orElseThrow(() -> new IllegalStateException("El estado 'Agotado' (ID 3) no está configurado en el sistema."));
            producto.setEstado(estadoAgotado);
        }

        return productoGateway.save(producto);
    }

    public void eliminacionLogica(String idProducto) {
        Producto productoADescontinuar = productoGateway.findById(idProducto).
            orElseThrow(() -> new EntityNotFoundException("El producto no existe."));

        EstadoProducto estadoDescontinuado = estadoProductoGateway.findById(2)
                        .orElseThrow(() -> new IllegalStateException("El estado 'Descontinuado (ID 2) no está configurado en el sistema"));

        productoADescontinuar.setEstado(estadoDescontinuado);
        productoGateway.save(productoADescontinuar);
    }
}