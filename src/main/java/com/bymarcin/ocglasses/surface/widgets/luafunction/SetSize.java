package com.bymarcin.ocglasses.surface.widgets.luafunction;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;

import com.bymarcin.ocglasses.lua.LuaFunction;
import com.bymarcin.ocglasses.surface.IWidget;
import com.bymarcin.ocglasses.surface.widgets.atribute.IResizable;

public class SetSize extends LuaFunction{

	@Override
	public Object[] call(Context context, Arguments arguments) {
		IWidget widget = getSelf().getWidget(); 
		if(widget instanceof IResizable){
			((IResizable) widget).setSize(arguments.checkDouble(0), arguments.checkDouble(1));
			getSelf().getTerminal().updateWidget(getSelf().getWidgetRef());	
			return null;
		}
		throw new RuntimeException("Component does not exists!");
	}

}
