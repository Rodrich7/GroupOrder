package com.sportsmatch.infrastructure.web;

import com.sportsmatch.application.usecase.AbrirInscripciones;
import com.sportsmatch.application.usecase.CancelarEncuentro;
import com.sportsmatch.application.usecase.CerrarInscripciones;
import com.sportsmatch.application.usecase.ConfirmarEncuentro;
import com.sportsmatch.application.usecase.CrearEncuentroDeportivo;
import com.sportsmatch.application.usecase.FinalizarEncuentro;
import com.sportsmatch.application.usecase.IniciarEncuentro;
import com.sportsmatch.domain.model.EncuentroDeportivo;
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
@RequestMapping("/api/encuentros")
public class EncuentroDeportivoController {

    private final Map<Long, EncuentroDeportivo> encuentros = new ConcurrentHashMap<>();
    private final CrearEncuentroDeportivo crearEncuentroDeportivo = new CrearEncuentroDeportivo();
    private final AbrirInscripciones abrirInscripciones = new AbrirInscripciones();
    private final CerrarInscripciones cerrarInscripciones = new CerrarInscripciones();
    private final ConfirmarEncuentro confirmarEncuentro = new ConfirmarEncuentro();
    private final IniciarEncuentro iniciarEncuentro = new IniciarEncuentro();
    private final FinalizarEncuentro finalizarEncuentro = new FinalizarEncuentro();
    private final CancelarEncuentro cancelarEncuentro = new CancelarEncuentro();

    @PostMapping
    public ResponseEntity<EncuentroDeportivo> crear(@RequestBody CrearEncuentroRequest solicitud) {
        EncuentroDeportivo encuentro = crearEncuentroDeportivo.ejecutar(
                solicitud.id(), solicitud.deporte(), solicitud.fechaHora(), solicitud.cancha(),
                solicitud.cupoMinimo(), solicitud.cupoMaximo(), solicitud.organizador()
        );

        if (encuentros.putIfAbsent(encuentro.getId(), encuentro) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un encuentro con ese id");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(encuentro);
    }

    @GetMapping("/{id}")
    public EncuentroDeportivo obtener(@PathVariable Long id) {
        return buscarEncuentro(id);
    }

    @PostMapping("/{id}/abrir-inscripciones")
    public EncuentroDeportivo abrirInscripciones(@PathVariable Long id) {
        return actualizar(id, abrirInscripciones::ejecutar);
    }

    @PostMapping("/{id}/cerrar-inscripciones")
    public EncuentroDeportivo cerrarInscripciones(@PathVariable Long id) {
        return actualizar(id, cerrarInscripciones::ejecutar);
    }

    @PostMapping("/{id}/confirmar")
    public EncuentroDeportivo confirmar(@PathVariable Long id) {
        return actualizar(id, confirmarEncuentro::ejecutar);
    }

    @PostMapping("/{id}/iniciar")
    public EncuentroDeportivo iniciar(@PathVariable Long id) {
        return actualizar(id, iniciarEncuentro::ejecutar);
    }

    @PostMapping("/{id}/finalizar")
    public EncuentroDeportivo finalizar(@PathVariable Long id) {
        return actualizar(id, finalizarEncuentro::ejecutar);
    }

    @PostMapping("/{id}/cancelar")
    public EncuentroDeportivo cancelar(@PathVariable Long id) {
        return actualizar(id, cancelarEncuentro::ejecutar);
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Map<String, String>> manejarReglaDeNegocio(RuntimeException exception) {
        return ResponseEntity.badRequest().body(Map.of("error", exception.getMessage()));
    }

    private EncuentroDeportivo actualizar(Long id, Consumer<EncuentroDeportivo> accion) {
        EncuentroDeportivo encuentro = buscarEncuentro(id);
        accion.accept(encuentro);
        return encuentro;
    }

    private EncuentroDeportivo buscarEncuentro(Long id) {
        EncuentroDeportivo encuentro = encuentros.get(id);
        if (encuentro == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Encuentro no encontrado");
        }
        return encuentro;
    }
}
