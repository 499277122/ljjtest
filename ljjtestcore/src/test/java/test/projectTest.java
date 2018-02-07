package test;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.ljj.order.core.entity.Order;
import cn.ljj.order.core.service.IOrderService;

public class projectTest extends BaseTest {
	@Autowired
	private IOrderService orderService;
	
	@Test
	public void test(){
		List<Order> orders =  orderService.getOrderList();
		System.out.println(orders.toString());
	}
}
