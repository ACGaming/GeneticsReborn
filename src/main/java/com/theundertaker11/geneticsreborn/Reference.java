package com.theundertaker11.geneticsreborn;

public class Reference {

	public static final String MODID = Tags.MOD_ID;
	public static final String VERSION = Tags.VERSION;
	public static final String NAME = "Genetics Reborn";
	public static final String ACCEPTED_MINECRAFT = "[1.12,1.12.2]";
	public static final String CLIENTPROXY = "com.theundertaker11.geneticsreborn.proxy.ClientProxy";
	public static final String SERVERPROXY = "com.theundertaker11.geneticsreborn.proxy.CommonProxy";
	public static final String GUIFACTORY = "com.theundertaker11.geneticsreborn.util.GuiFactory";

	private Reference() {
		throw new IllegalAccessError("Ref class");
	}
}
