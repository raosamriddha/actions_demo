package org.lattice.spectrum_backend_final.dao.util;

public final class GraphType {

	private GraphType() {
	}

	public static final byte FLOW_RATE_VS_TIME = 0;
	public static final byte PRESSURE_VS_TIME = 1;
	public static final byte FLUX_VS_TIME = 2;
	public static final byte PRESSURE_VS_CF_VS_TIME = 3;
	public static final byte FLUX_VS_CF = 4;
	public static final byte FLUX_VS_TMP = 5;
	public static final byte PRESSURE_VS_CF_VS_VT = 6;
	public static final byte DV_VS_CONDUCTIVTY_VS_TIME = 7;
	public static final byte KCH_1_VS_TEMP_VS_TIME = 8;
	public static final byte KCH_2_VS_TEMP_VS_TIME = 9;
	public static final byte FLUX_VS_CF_TIME = 10;
	public static final byte TEMP_VS_CONDUCTIVTY_VS_TIME = 11;
	public static final byte RPM_VS_TIME = 12;
}
