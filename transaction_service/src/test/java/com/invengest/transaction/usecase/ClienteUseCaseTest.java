package com.invengest.transaction.usecase;

import com.invengest.transaction.domain.exception.DuplicateEntityException;
import com.invengest.transaction.domain.exception.EntityNotFoundException;
import com.invengest.transaction.domain.gateway.ClienteGateway;
import com.invengest.transaction.domain.gateway.TipoDocumentoGateway;
import com.invengest.transaction.domain.model.Cliente;
import com.invengest.transaction.domain.model.TipoDocumento;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteUseCaseTest {

    @Mock
    private ClienteGateway clienteGateway;

    @Mock
    private TipoDocumentoGateway tipoDocumentoGateway;

    @InjectMocks
    private ClienteUseCase clienteUseCase;

    @Test
    @DisplayName("Registrar cliente exitosamente")
    void registrar_Exitoso() {
        Cliente cliente = new Cliente();
        cliente.setPrimerNombre("Ana");
        cliente.setPrimerApellido("Gomez");
        cliente.setDocumentoIdentidad("12345678");
        cliente.setTipoDocumento(new TipoDocumento(1, null));

        when(clienteGateway.findByDocumentoIdentidad("12345678")).thenReturn(Optional.empty());
        when(tipoDocumentoGateway.findById(1)).thenReturn(Optional.of(new TipoDocumento(1, "CC")));
        when(clienteGateway.save(any())).thenAnswer(i -> i.getArgument(0));

        Cliente registrado = clienteUseCase.registrar(cliente);
        assertNotNull(registrado);
        assertEquals("Ana", registrado.getPrimerNombre());
        assertEquals("Ana Gomez", registrado.getNombreCompleto());
    }

    @Test
    @DisplayName("Registrar cliente con documento duplicado lanza DuplicateEntityException")
    void registrar_DocumentoDuplicado_LanzaException() {
        Cliente cliente = new Cliente();
        cliente.setPrimerNombre("Ana");
        cliente.setPrimerApellido("Gomez");
        cliente.setDocumentoIdentidad("12345678");

        when(clienteGateway.findByDocumentoIdentidad("12345678")).thenReturn(Optional.of(new Cliente()));

        assertThrows(DuplicateEntityException.class, () -> clienteUseCase.registrar(cliente));
        verify(clienteGateway, never()).save(any());
    }

    @Test
    @DisplayName("Obtener cliente por documento exitoso")
    void obtenerPorDocumento_Exitoso() {
        Cliente cliente = new Cliente();
        cliente.setDocumentoIdentidad("12345678");
        when(clienteGateway.findByDocumentoIdentidad("12345678")).thenReturn(Optional.of(cliente));

        Cliente res = clienteUseCase.obtenerPorDocumento("12345678");
        assertEquals("12345678", res.getDocumentoIdentidad());
    }

    @Test
    @DisplayName("Obtener cliente por documento no existente lanza EntityNotFoundException")
    void obtenerPorDocumento_NoExiste_LanzaException() {
        when(clienteGateway.findByDocumentoIdentidad("999")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> clienteUseCase.obtenerPorDocumento("999"));
    }
}
