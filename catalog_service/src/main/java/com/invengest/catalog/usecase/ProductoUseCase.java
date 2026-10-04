package com.invengest.catalog.usecase;

import com.invengest.catalog.domain.exception.DuplicateEntityException;
import com.invengest.catalog.domain.exception.EntityNotFoundException;
import com.invengest.catalog.domain.gateway.ProductoRepository;
import com.invengest.catalog.domain.model.Producto;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ProductoUseCase {

    private final ProductoRepository productoRepository;

    public Producto registrar(Producto producto) {
        if (productoRepository.findById(producto.getIdProducto()).isPresent()) {
            throw new DuplicateEntityException("El producto con código " + producto.getIdProducto() + " ya existe.");
        }
        return productoRepository.save(producto);
    }

    public Producto actualizar(Producto producto) {
        if (productoRepository.findById(producto.getIdProducto()).isEmpty()) {
            throw new EntityNotFoundException("El producto con código " + producto.getIdProducto() + " no existe.");
        }
        return productoRepository.save(producto);
    }

    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    public Producto obtenerPorId(String idProducto) {
        return productoRepository.findById(idProducto)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con el código: " + idProducto));
    }

    public List<Producto> buscarPorCriterio(String query) {
        return productoRepository.findBySearchCriteria(query);
    }

    public List<Producto> obtenerProductosStockBajo() {
        return productoRepository.findLowStockProducts();
    }

    public Producto descontarStock(String idProducto, Integer cantidad) {
        Producto producto = obtenerPorId(idProducto);
        if (producto.getStock() < cantidad) {
            throw new IllegalStateException("Stock insuficiente para el producto: " + producto.getNombre());
        }
        producto.setStock(producto.getStock() - cantidad);
        if (producto.getStock() == 0) {
            producto.setEstado("Agotado");
        }
        return productoRepository.save(producto);
    }

    public void eliminarPorId(String idProducto) {
        if (productoRepository.findById(idProducto).isEmpty()) {
            throw new EntityNotFoundException("El producto no existe.");
        }
        productoRepository.deleteById(idProducto);
    }
}