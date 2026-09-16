package com.sportsmatch;

import com.sportsmatch.application.usecase.AbrirInscripciones;
import com.sportsmatch.application.usecase.CerrarInscripciones;
import com.sportsmatch.application.usecase.ConfirmarEncuentro;
import com.sportsmatch.application.usecase.FinalizarEncuentro;
import com.sportsmatch.application.usecase.IniciarEncuentro;
import com.sportsmatch.domain.model.EncuentroDeportivo;
import com.sportsmatch.domain.model.EstadoEncuentro;
import com.sportsmatch.infrastructure.web.CrearEncuentroRequest;
import com.sportsmatch.infrastructure.web.EncuentroDeportivoController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class SportsMatchApplicationTests {

    private static final LocalDateTime FECHA_ENCUENTRO = LocalDateTime.of(2026, 10, 20, 18, 0);

    @Test
    void contextLoads() {
    }

    @Test
    void creaEncuentroEnBorradorConDatosValidos() {
        EncuentroDeportivo encuentro = crearEncuentro(1L);

        assertEquals("Fútbol", encuentro.getDeporte());
        assertEquals("Cancha Central", encuentro.getCancha());
        assertEquals(EstadoEncuentro.BORRADOR, encuentro.getEstado());
    }

    @Test
    void rechazaCuposInvalidos() {
        assertThrows(IllegalArgumentException.class,
                () -> new EncuentroDeportivo(1L, "Fútbol", FECHA_ENCUENTRO, "Cancha Central", 12, 10, "Ana"));
    }

    @Test
    void completaElCicloDeVidaDelEncuentro() {
        EncuentroDeportivo encuentro = crearEncuentro(1L);

        new AbrirInscripciones().ejecutar(encuentro);
        new CerrarInscripciones().ejecutar(encuentro);
        new ConfirmarEncuentro().ejecutar(encuentro);
        new IniciarEncuentro().ejecutar(encuentro);
        new FinalizarEncuentro().ejecutar(encuentro);

        assertEquals(EstadoEncuentro.FINALIZADO, encuentro.getEstado());
    }

    @Test
    void noPermiteConfirmarUnEncuentroEnBorrador() {
        EncuentroDeportivo encuentro = crearEncuentro(3L);

        assertThrows(IllegalStateException.class, () -> new ConfirmarEncuentro().ejecutar(encuentro));
    }

    @Test
    void creaYActualizaUnEncuentroMedianteLaApi() {
        EncuentroDeportivoController controller = new EncuentroDeportivoController();

        ResponseEntity<EncuentroDeportivo> respuesta = controller.crear(
                new CrearEncuentroRequest(4L, "Vóley", FECHA_ENCUENTRO, "Cancha Norte", 8, 12, "Carla")
        );
        controller.abrirInscripciones(4L);
        controller.cerrarInscripciones(4L);
        controller.confirmar(4L);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertEquals(EstadoEncuentro.CONFIRMADO, controller.obtener(4L).getEstado());
    }

    private EncuentroDeportivo crearEncuentro(Long id) {
        return new EncuentroDeportivo(id, "Fútbol", FECHA_ENCUENTRO, "Cancha Central", 10, 14, "Ana");
    }
}
