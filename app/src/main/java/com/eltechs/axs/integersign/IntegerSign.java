package com.eltechs.axs.integersign;

public abstract class IntegerSign
{
	public int value;
	public IntegerSign(int integer) {
		value = integer;
	}
	
	public abstract boolean isSigned();
}
