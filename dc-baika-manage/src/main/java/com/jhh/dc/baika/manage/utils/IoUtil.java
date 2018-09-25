package com.jhh.dc.baika.manage.utils;

import java.io.IOException;
import java.io.InputStream;

public abstract class IoUtil {

	public static String readStringFromStream(InputStream in) throws IOException {
		int length = in.available();
		byte[] bytes = new byte[length];
		in.read(bytes);
		in.close();
		return new String(bytes);
	}
}
