package cn.ljj.order.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ljj.order.core.entity.Order;
import cn.ljj.order.core.mapper.OrderMapper;
import cn.ljj.order.core.service.IOrderService;

@Service
public class OrderServiceImpl implements IOrderService {
	@Autowired
	private OrderMapper orderMapper;
	
	/**   
	 * @Title: 获取订单列表     
	 * @return: List<Order>      
	 * @auth: JiaJie Li
	 * @data: 2018年1月21日 下午8:10:38
	 */ 
	@Override
	public List<Order> getOrderList() {
		return orderMapper.getOrderList();
	}
}
