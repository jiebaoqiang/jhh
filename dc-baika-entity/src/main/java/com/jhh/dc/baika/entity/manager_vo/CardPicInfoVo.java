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
public class CardPicInfoVo  implements Serializable{
	
	private int id;
	private int perId;
	
	private String imageTypeZ;
	private String imageFormatZ;
	private String imageZ;
	private String imageTypeF;
	private String imageFormatF;
	private String imageF;
	private String imageUrlZ;
	private String imageUrlF;
}

