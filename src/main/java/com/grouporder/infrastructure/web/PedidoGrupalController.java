package com.grouporder.infrastructure.web;

import com.grouporder.application.usecase.CancelarPedidoGrupal;
import com.grouporder.application.usecase.CerrarPedidoGrupal;
import com.grouporder.application.usecase.ConfirmarPedidoGrupal;
import com.grouporder.application.usecase.CrearPedidoGrupal;
import com.grouporder.application.usecase.EntregarPedidoGrupal;
import com.grouporder.domain.model.PedidoGrupal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoGrupalController {

    private final Map<Long, PedidoGrupal> pedidos = new ConcurrentHashMap<>();
    private final CrearPedidoGrupal crearPedidoGrupal = new CrearPedidoGrupal();
    private final CerrarPedidoGrupal cerrarPedidoGrupal = new CerrarPedidoGrupal();
    private final ConfirmarPedidoGrupal confirmarPedidoGrupal = new ConfirmarPedidoGrupal();
    private final CancelarPedidoGrupal cancelarPedidoGrupal = new CancelarPedidoGrupal();
    private final EntregarPedidoGrupal entregarPedidoGrupal = new EntregarPedidoGrupal();

    @PostMapping
    public ResponseEntity<PedidoGrupal> crear(@RequestBody CrearPedidoRequest solicitud) {
        PedidoGrupal pedido = crearPedidoGrupal.ejecutar(
                solicitud.id(), solicitud.codigo(), solicitud.creador()
        );

        if (pedidos.putIfAbsent(pedido.getId(), pedido) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un pedido con ese id");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    @GetMapping("/{id}")
    public PedidoGrupal obtener(@PathVariable Long id) {
        return buscarPedido(id);
    }

    @PostMapping("/{id}/cerrar")
    public PedidoGrupal cerrar(@PathVariable Long id) {
        return actualizar(id, cerrarPedidoGrupal::ejecutar);
    }

    @PostMapping("/{id}/confirmar")
    public PedidoGrupal confirmar(@PathVariable Long id) {
        return actualizar(id, confirmarPedidoGrupal::ejecutar);
    }

    @PostMapping("/{id}/cancelar")
    public PedidoGrupal cancelar(@PathVariable Long id) {
        return actualizar(id, cancelarPedidoGrupal::ejecutar);
    }

    @PostMapping("/{id}/entregar")
    public PedidoGrupal entregar(@PathVariable Long id) {
        return actualizar(id, entregarPedidoGrupal::ejecutar);
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Map<String, String>> manejarReglaDeNegocio(RuntimeException exception) {
        return ResponseEntity.badRequest().body(Map.of("error", exception.getMessage()));
    }

    private PedidoGrupal actualizar(Long id, Consumer<PedidoGrupal> accion) {
        PedidoGrupal pedido = buscarPedido(id);
        accion.accept(pedido);
        return pedido;
    }

    private PedidoGrupal buscarPedido(Long id) {
        PedidoGrupal pedido = pedidos.get(id);
        if (pedido == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado");
        }
        return pedido;
    }
}
