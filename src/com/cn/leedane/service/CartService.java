package com.cn.leedane.service;
import com.cn.leedane.model.IDBean;
/**
 * 购物车的Service类
 * @author LeeDane
 * 2016年7月12日 上午11:30:56
 * Version 1.0
 */
public interface CartService<T extends IDBean>{

	public void addCart();
}
