package com.freelycar.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table
public class CashBackRate {

	@Id
    @GenericGenerator(name="generator",strategy="native")
    @GeneratedValue(generator="generator")
	private int id;
	
	
	//这个是记录返现的返现率  1%; rate=1    1.95% rate=1.95
	private float rate;
	
	public float getRate() {
		return rate;
	}
	
	
	public void setRate(float rate) {
		this.rate = rate;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
}
