package cn.ljj.order.core.entity;

import java.io.Serializable;

public class Order implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private Long sysOrderSn;
	
	private Integer state;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSysOrderSn() {
		return sysOrderSn;
	}

	public void setSysOrderSn(Long sysOrderSn) {
		this.sysOrderSn = sysOrderSn;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", sysOrderSn=" + sysOrderSn + ", state=" + state + "]";
	}
	
}
