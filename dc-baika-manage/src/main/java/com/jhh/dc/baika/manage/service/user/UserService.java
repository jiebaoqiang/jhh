package com.jhh.dc.baika.manage.service.user;


import com.jhh.dc.baika.entity.app.NoteResult;
import com.jhh.dc.baika.manage.entity.Response;
import com.jhh.dc.baika.manage.entity.Result;

import java.util.List;
import java.util.Map;

public interface UserService {

  /**
   * 获取用户详细信息
   * @param perId
   * @return
   */
  Response getUserInfo(Integer perId);

  /**
   * 获取身份证
   * @param perId
   * @return
   */
  Response getIdentityCard(Integer perId);

  /**
   * 获取姓名
   * @param personId
   * @return
   */
  String getNameByPersonId(Integer personId);

  /**
   * 拉黑洗白用户
   * @param personId
   * @param operatorNum
   * @param operator
   * @param type
   * @return
   */
  Response userBlockWhite(Integer personId, String operatorNum, String operator, String reason, Integer type) throws Exception;

  /**
   * 黑名单列表
   * @param personId
   * @return
   */
  Response getBlackList(Integer personId);
  /**
   * 银行卡列表
   * @param personId
   * @return
   */
  Response getBankList(Integer personId);

  Response getValidBankList(Integer personId);

  /**
   * 流水列表
   * @param personId
   * @return
   * @throws Exception
   */
  Response getOrderList(Integer personId);


  /**
   * 节点列表
   * @param personId
   * @return
   * @throws Exception
   */
  Response getNodeList(Integer personId);

  /**
   * 节点详情列表
   * @param personId
   * @return
   * @throws Exception
   */
  Response getNodeDetailList(Integer personId);
  /**
   * 查询用户列表
   * */
  Response getUsers(Map<String, String[]> args);
  /**
   * 渠道用户查询自己渠道的用户列表
   * */
  Response getChannelUsers(Map<String, String[]> args);
  /**
   * 查询注册来源
  * */
  List getSource(String code);

  Result addOfflineUser(String phone);

  String riskNodeProductId();

  /**
   * 获取风控节点名称和节点状态
   */
  Map getRiskNodeAndStatus();
}
