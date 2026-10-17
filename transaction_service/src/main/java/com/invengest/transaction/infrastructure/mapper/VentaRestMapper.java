package com.invengest.transaction.infrastructure.mapper;

import com.invengest.transaction.domain.model.Cliente;
import com.invengest.transaction.domain.model.MetodoPago;
import com.invengest.transaction.domain.model.Venta;
import com.invengest.transaction.infrastructure.driver_adapter.rest.dto.request.RegistrarVentaRequest;
import com.invengest.transaction.infrastructure.driver_adapter.rest.dto.response.VentaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ClienteRestMapper.class, MetodoPagoRestMapper.class, DetalleVentaRestMapper.class})
public interface VentaRestMapper {

    VentaResponse toResponse(Venta domain);

    @Mapping(target = "idVenta", ignore = true)
    @Mapping(target = "fecha", ignore = true)
    @Mapping(target = "total", ignore = true)
    @Mapping(target = "cliente", expression = "java(mapCliente(request.idCliente()))")
    @Mapping(target = "metodoPago", expression = "java(mapMetodoPago(request.idMetodoPago()))")
    @Mapping(target = "detalles", source = "items")
    Venta toDomain(RegistrarVentaRequest request);

    default Cliente mapCliente(Integer idCliente) {
        if (idCliente == null) {
            return null;
        }
        Cliente c = new Cliente();
        c.setIdCliente(idCliente);
        return c;
    }

    default MetodoPago mapMetodoPago(Integer idMetodoPago) {
        if (idMetodoPago == null) {
            return null;
        }
        MetodoPago m = new MetodoPago();
        m.setIdMetodoPago(idMetodoPago);
        return m;
    }
}
