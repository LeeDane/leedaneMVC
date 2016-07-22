package com.cn.leedane.model;



/**
 * 记账实体类
 * @author LeeDane
`* 2016年7月22日 上午9:42:15
 * Version 1.0
 */
//@Table(name="T_FINANCIAL")
public class FinancialBean extends RecordTimeBean{
	
	private static final long serialVersionUID = 1L;
	//记账的状态,1：正常，0:禁用，2、删除
	
	/**
	 * 模块，1:收入；2：支出
	 */
	private int model;

	/**
	 * 相关金额
	 */
	private float money; 
	
	/**
	 * 一级分类
	 */
	private String oneLevel;
	
	/**
	 * 二级分类
	 */
	private String twoLevel;
	
	/**
	 * 是否有图片
	 */
	private boolean hasImg;
	
	/**
	 * 位置的展示信息
	 */
	private String location;
	
	/**
	 * 经度
	 */
    private double longitude;
    
    /**
     * 纬度
     */
    private double latitude;
    
    /**
     * 备注信息
     */
    private String financialDesc;

	public int getModel() {
		return model;
	}

	public void setModel(int model) {
		this.model = model;
	}

	public float getMoney() {
		return money;
	}

	public void setMoney(float money) {
		this.money = money;
	}

	public String getOneLevel() {
		return oneLevel;
	}

	public void setOneLevel(String oneLevel) {
		this.oneLevel = oneLevel;
	}

	public String getTwoLevel() {
		return twoLevel;
	}

	public void setTwoLevel(String twoLevel) {
		this.twoLevel = twoLevel;
	}

	public boolean isHasImg() {
		return hasImg;
	}

	public void setHasImg(boolean hasImg) {
		this.hasImg = hasImg;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getFinancialDesc() {
		return financialDesc;
	}

	public void setFinancialDesc(String financialDesc) {
		this.financialDesc = financialDesc;
	}
    
    
}
