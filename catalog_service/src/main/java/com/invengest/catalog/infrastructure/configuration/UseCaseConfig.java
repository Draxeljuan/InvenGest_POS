package com.invengest.catalog.infrastructure.configuration;

import com.invengest.catalog.domain.gateway.*;
import com.invengest.catalog.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CategoriaUseCase categoriaUseCase(CategoriaGateway categoriaGateway) {
        return new CategoriaUseCase(categoriaGateway);
    }

    @Bean
    public ProductoUseCase productoUseCase(ProductoGateway productoGateway, EstadoProductoGateway estadoProductoGateway, CategoriaGateway categoriaGateway) {
        return new ProductoUseCase(productoGateway, estadoProductoGateway, categoriaGateway);
    }

    @Bean
    public ProveedorUseCase proveedorUseCase(ProveedorGateway proveedorGateway, EstadoProveedorGateway estadoProveedorGateway) {
        return new ProveedorUseCase(proveedorGateway, estadoProveedorGateway);
    }

    @Bean
    public ServicioUseCase servicioUseCase(ServicioGateway servicioGateway, CategoriaServicioGateway categoriaServicioGateway) {
        return new ServicioUseCase(servicioGateway, categoriaServicioGateway);
    }

    @Bean
    public CategoriaServicioUseCase categoriaServicioUseCase(CategoriaServicioGateway categoriaServicioGateway) {
        return new CategoriaServicioUseCase(categoriaServicioGateway);
    }

    @Bean
    public ProductoProveedorUseCase productoProveedorUseCase(ProductoProveedorGateway productoProveedorGateway) {
        return new ProductoProveedorUseCase(productoProveedorGateway);
    }

    @Bean
    public EstadoProductoUseCase estadoProductoUseCase(EstadoProductoGateway estadoProductoGateway) {
        return new EstadoProductoUseCase(estadoProductoGateway);
    }

    @Bean
    public EstadoProveedorUseCase estadoProveedorUseCase(EstadoProveedorGateway estadoProveedorGateway) {
        return new EstadoProveedorUseCase(estadoProveedorGateway);
    }
}
