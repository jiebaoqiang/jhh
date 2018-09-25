/**
*描述：
*@author: wanyan
*@date： 日期：2016年12月12日 时间：下午4:52:27
*@version 1.0
*/

package com.jhh.dc.baika.entity.manager_vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 *描述：
 *@author: Wanyan
 *@date： 日期：2016年12月12日 时间：下午4:52:27
 *@version 1.0
 */
@Setter
@Getter
public class BalckListVo implements Serializable{

	private String type;
	private String reason;
	private String source;
	private String operator;
	private String createTime;
}

