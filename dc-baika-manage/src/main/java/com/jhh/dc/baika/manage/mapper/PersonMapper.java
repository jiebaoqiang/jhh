package com.jhh.dc.baika.manage.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import com.jhh.dc.baika.entity.app.Person;
import com.jhh.dc.baika.entity.manager_vo.CardPicInfoVo;
import com.jhh.dc.baika.entity.manager_vo.PrivateVo;

public interface PersonMapper extends Mapper<Person>{

    PrivateVo queryUserInfo(Integer perId);

    CardPicInfoVo getCardPicById(Integer perId);

    List getBlackList(Integer perId);

    /**
     * 根据电话  身份证  姓名查询个人ID
     * @param map
     * @return
     */
    List<Integer> selectPersonId(Map<String,Object> map);

    /**
     * 根据用户ID获取节点列表
     * @param idCard
     * @return
     */
    List getNodeByPerId(String perId);
    /**
     * 根据用户ID获取节点详情列表
     * @param map
     * @return
     */
    List getNodeDetailByPerId(Map map);

    List getBankByPerId(Integer perId);
    /**
     * 获取用户列表
     * */
    List getUsers(Map map);

    List getChannelUsers(Map getargs);

    /**
     * 查询注册来源
     * */
    List getRegisterSource(String code);

    /**
     * 根据合同号获取身份证号
     * @return
     */
    Map<String,String> getCardNumAndPhoneByBorrId(Integer borrId);

    Person getPersonByPhone(String phone);
}