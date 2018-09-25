package com.jhh.dc.baika.service.message;

import com.alibaba.dubbo.config.annotation.Service;
import com.jhh.dc.baika.mapper.manager.MsgMapper;
import com.jhh.dc.baika.mapper.manager.MsgTemplateMapper;
import com.jhh.dc.baika.api.constant.StateCode;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.api.message.MessageService;
import com.jhh.dc.baika.entity.manager.Msg;
import com.jhh.dc.baika.entity.manager.MsgTemplate;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by chenchao on 2018/1/16.
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MsgMapper msgMapper;

    @Autowired
    private MsgTemplateMapper msgTemplateMapper;

    @Override
    public ResponseDo<String> setMessage(String userId, String templateId, String params) {
        ResponseDo obj = new ResponseDo();
        try {
            String[] cc = params.split(",");
            // 定义最后的消息内容
            String dd = "";
            // 获取消息模版
            MsgTemplate msgTemplate = msgTemplateMapper
                    .selectByPrimaryKey(Integer.parseInt(templateId));
            if (null != msgTemplate) {

                if ("1".equals(msgTemplate.getStatus())) {
                    // 获取模版的内容
                    String ll = msgTemplate.getContent();
                    // 获取模版的标题
                    String title = msgTemplate.getTitle();
                    // 分割模版内容
                    String[] aa = ll.split("\\{");
                    // 将模版内容和参数拼接成最后的消息内容
                    for (int i = 0; i < aa.length; i++) {
                        String[] bb = aa[i].split("}");
                        if (bb.length > 1) {
                            dd += cc[i - 1] + bb[1];
                        } else {
                            dd += aa[i];
                        }
                    }
                    Msg msg = new Msg();
                    msg.setContent(dd);
                    msg.setTitle(title);
                    msg.setPerId(Integer.parseInt(userId));
                    msg.setStatus("n");
                    msg.setType(1);
                    msg.setCreateTime(new Date());
                    msgMapper.insertSelective(msg);

                    obj.setCode(StateCode.SUCCESS_CODE);
                    obj.setInfo(StateCode.SUCCESS_MSG);
                    obj.setData(dd);
                } else {
                    obj.setCode(StateCode.MSG_FAILURE_CODE);
                    obj.setInfo(StateCode.MSG_FAILURE_MSG);
                }
            } else {
                obj.setCode(StateCode.MSG_EMPTY_CODE);
                obj.setInfo(StateCode.MSG_EMPTY_MSG);
            }
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(StateCode.SYSTEM_CODE);
            obj.setInfo(StateCode.SYSTEM_MSG);
        }
        return obj;
    }
}
