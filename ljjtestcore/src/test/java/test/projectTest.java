package test;
import java.util.List;

import cn.ljj.test.common.util.RedisUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.ljj.order.core.entity.Order;
import cn.ljj.order.core.service.IOrderService;

public class projectTest extends BaseTest {
	private Logger log = Logger.getLogger(projectTest.class);
	@Autowired
	private IOrderService orderService;
	@Autowired
	private RedisUtils redisUtils;

	@Test
	public void test(){
		List<Order> orders =  orderService.getOrderList();
		log.info(orders.toString());
	}
}
