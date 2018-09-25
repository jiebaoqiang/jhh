package com.jhh.dc.baika.app.common.util;

import com.jhh.dc.baika.api.constant.StateCode;
import com.jhh.dc.baika.app.common.exception.CommonException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@Component
public class ValidMac {
	private static Log log = LogFactory.getLog(ValidMac.class);

	@Value("${merchantKey}")
	private String merchantKey;

	public String generatorMac(String[] keys, Map<String, String[]> params)
			throws CommonException, UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		for (String key : keys) {
			String param = params.get(key) != null ? params.get(key)[0] : null;
			if (StringUtils.isBlank(param)) {
				throw new CommonException(StateCode.PARAM_EMPTY_CODE,
						String.format(StateCode.PARAM_EMPTY_MSG,
								key));
			}
			if (!key.equals("mac")) {
				sb.append(key).append("=").append(params.get(key)[0])
						.append("&");
			}
		}
		sb.append("merchantKey=").append(merchantKey);
		String md5 = Md5Encrypt.md5(sb.toString());
		log.debug("The md5 string is " + sb.toString() + ";mac=" + md5);
		return md5;
	}

	public void checkMac(String[] keys, Map<String, String[]> params)
			throws CommonException, UnsupportedEncodingException {
		String nowmac = generatorMac(keys, params);
		if (!params.get("mac")[0].equalsIgnoreCase(nowmac)) {
			throw new CommonException(StateCode.CHECK_MAC_ERROR_CODE,
					StateCode.CHECK_MAC_ERROR_MSG);
		}
	}


}
