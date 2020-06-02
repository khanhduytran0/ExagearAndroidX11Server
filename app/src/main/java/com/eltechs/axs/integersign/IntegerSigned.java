package com.eltechs.axs.integersign;

public class IntegerSigned extends IntegerSign
{
	public IntegerSigned(int i) {
		super(i);
	}
	
	@Override
	public boolean isSigned() {
		return true;
	}
}
