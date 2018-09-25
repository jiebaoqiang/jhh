package com.jhh.dc.baika.handle;

import com.jhh.dc.baika.entity.app.BorrowList;
import com.jhh.dc.baika.entity.app.BorrowStatusLog;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.beans.BeanUtils;

import java.util.Properties;

/**
 * 2018/8/8.
 */
@Intercepts({@Signature(
        type=Executor.class,
        method="update",
        args={MappedStatement.class, Object.class})})
public class BorrowStatusHandle implements Interceptor {



    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        if (!(ms.getSqlCommandType() != SqlCommandType.INSERT || ms.getSqlCommandType() != SqlCommandType.UPDATE)){
            return invocation.proceed();
        }else {
            Object parameter = invocation.getArgs()[1];
            if (parameter instanceof BorrowList){
                BorrowList borrowList = (BorrowList) parameter;
                //执行真正的方法
                Object result = invocation.proceed();
                //获取insertSqlLog方法
                ms = ms.getConfiguration().getMappedStatement("insertBorrowStatus");
                //替换当前的参数为新的ms
                args[0] = ms;
                BorrowStatusLog log = new BorrowStatusLog();
                BeanUtils.copyProperties(borrowList,log);
                log.setBorrowid(borrowList.getId());
                log.setStatus(borrowList.getBorrStatus());
                log.setId(null);
                args[1] = log;
                //执行insertSqlLog方法
                invocation.proceed();
                //返回真正方法执行的结果
                return result;
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
