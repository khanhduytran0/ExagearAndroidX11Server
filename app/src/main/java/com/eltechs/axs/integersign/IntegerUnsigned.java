package com.eltechs.axs.integersign;

public class IntegerUnsigned extends IntegerSign
{
	public IntegerUnsigned(int i) {
		super(i);
	}

	@Override
	public boolean isSigned() {
		return false;
	}
}
