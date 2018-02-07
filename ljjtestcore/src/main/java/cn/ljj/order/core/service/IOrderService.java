package cn.ljj.order.core.service;

import java.util.List;

import cn.ljj.order.core.entity.Order;

public interface IOrderService {
	/**   
	 * @Title: 获取订单列表     
	 * @return: List<Order>      
	 * @auth: JiaJie Li
	 * @data: 2018年1月21日 下午8:10:38
	 */ 
	public List<Order> getOrderList();
}
