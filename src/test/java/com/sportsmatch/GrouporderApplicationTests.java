package com.grouporder;

import com.grouporder.application.usecase.CancelarPedidoGrupal;
import com.grouporder.application.usecase.CerrarPedidoGrupal;
import com.grouporder.application.usecase.ConfirmarPedidoGrupal;
import com.grouporder.application.usecase.EntregarPedidoGrupal;
import com.grouporder.domain.model.EstadoPedido;
import com.grouporder.domain.model.PedidoGrupal;
import com.grouporder.infrastructure.web.CrearPedidoRequest;
import com.grouporder.infrastructure.web.PedidoGrupalController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class GrouporderApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void creaPedidoConDatosValidos() {
		PedidoGrupal pedido = new PedidoGrupal(1L, " PED-001 ", " Ana ");

		assertEquals("PED-001", pedido.getCodigo());
		assertEquals("Ana", pedido.getCreador());
		assertEquals(EstadoPedido.ABIERTO, pedido.getEstado());
	}

	@Test
	void rechazaPedidoSinCodigo() {
		assertThrows(IllegalArgumentException.class,
				() -> new PedidoGrupal(1L, "   ", "Ana"));
	}

	@Test
	void completaElCicloDeVidaDelPedido() {
		PedidoGrupal pedido = new PedidoGrupal(1L, "PED-001", "Ana");

		new CerrarPedidoGrupal().ejecutar(pedido);
		new ConfirmarPedidoGrupal().ejecutar(pedido);
		new EntregarPedidoGrupal().ejecutar(pedido);

		assertEquals(EstadoPedido.ENTREGADO, pedido.getEstado());
	}

	@Test
	void cancelaUnPedidoAbierto() {
		PedidoGrupal pedido = new PedidoGrupal(2L, "PED-002", "Luis");

		new CancelarPedidoGrupal().ejecutar(pedido);

		assertEquals(EstadoPedido.CANCELADO, pedido.getEstado());
	}

	@Test
	void noPermiteConfirmarUnPedidoAbierto() {
		PedidoGrupal pedido = new PedidoGrupal(3L, "PED-003", "Marta");

		assertThrows(IllegalStateException.class,
				() -> new ConfirmarPedidoGrupal().ejecutar(pedido));
	}

	@Test
	void creaYActualizaUnPedidoMedianteLaApi() {
		PedidoGrupalController controller = new PedidoGrupalController();

		ResponseEntity<PedidoGrupal> respuesta = controller.crear(
				new CrearPedidoRequest(4L, "PED-004", "Carla")
		);
		controller.cerrar(4L);
		controller.confirmar(4L);

		assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
		assertEquals(EstadoPedido.CONFIRMADO, controller.obtener(4L).getEstado());
	}

}
