package com.jhh.dc.baika.entity.enums;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnumTypeHandler  extends BaseTypeHandler<String> {

   private Class<CodeBaseEnum> clazz;

   public EnumTypeHandler(Class<CodeBaseEnum> enumType) {
       if (enumType == null)
           throw new IllegalArgumentException("Type argument cannot be null");

       this.clazz = enumType;
   }
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter);
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return codeOf(clazz, rs.getString(columnName));
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return codeOf(clazz, rs.getString(columnIndex));
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return codeOf(clazz, cs.getString(columnIndex));
    }

    public static  String codeOf(Class<CodeBaseEnum> enumClass, String code) {
        CodeBaseEnum[] enumConstants =  enumClass.getEnumConstants();
        for (CodeBaseEnum e : enumConstants) {
            if (e.getCode().equals(code))
                return e.description();
        }
        return null;
    }
}