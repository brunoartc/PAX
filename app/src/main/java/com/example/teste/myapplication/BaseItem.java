package com.example.teste.myapplication;

import java.util.concurrent.atomic.AtomicLong;

public abstract class BaseItem {
	private static final AtomicLong ids = new AtomicLong(0);
	public final long id;
	
	public BaseItem() {
		this.id = ids.getAndIncrement();
	}
}
