package com.grouporder;

import com.grouporder.domain.model.EstadoPedido;
import com.grouporder.domain.model.PedidoGrupal;
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

}
