package test;
import java.util.List;

import cn.ljj.test.common.util.RedisUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.ljj.order.core.entity.Order;
import cn.ljj.order.core.service.IOrderService;

public class projectTest extends BaseTest {
	@Autowired
	private IOrderService orderService;
	@Autowired
	private RedisUtils redisUtils;

	@Test
	public void test(){
		List<Order> orders =  orderService.getOrderList();
		System.out.println(orders.toString());
		redisUtils.set("name","xiaoming");
	}
}
