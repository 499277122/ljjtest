package cn.ljj.order.core.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.ljj.order.core.entity.Order;

/**
 * Order 表数据库控制层接口
 */
@Repository
public interface OrderMapper {
	
	public List<Order> getOrderList();
}