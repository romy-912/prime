package com.romy.prime.common.interceptor;

import com.romy.prime.common.Log;
import com.romy.prime.common.dvo.AuditDvo;
import com.romy.prime.common.utils.SessionUtils;
import com.romy.prime.organization.dvo.OrgUserDvo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

/**
 * packageName    : com.romy.prime.common.interceptor
 * fileName       : MyBatisInterceptor
 * author         : 김새롬이
 * date           : 2024-10-18
 * description    : MyBatis Interceptor
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-18        김새롬이       최초 생성
 */
@Slf4j
@Intercepts({@Signature
        (type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature
                (type = Executor.class, method = "query"
                        , args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class MyBatisInterceptor implements Interceptor {

    private static final String UPDATE = "update";

    @Override
    public Object intercept(Invocation invocation) {
        String methodName = invocation.getMethod().getName();

        Object[] args = invocation.getArgs();
        Object param = args.length > 1 ? args[1] : null;

        // INSERT, UPDATE, DELETE 의 경우 Audit Column 셋팅
        if (UPDATE.equals(methodName)) {
            this.setAuditing(param);
        }

        // Log 출력
        MappedStatement statement = (MappedStatement) invocation.getArgs()[0];
        String sqlId = statement.getId();
        BoundSql boundSql = statement.getBoundSql(param);
        Configuration configuration = statement.getConfiguration();

        Object returnValue = null;
        log.info("/*---------------Mapper Map ID: {} [begin]---------------*/", sqlId);

        String sql = this.generateSql(configuration, boundSql);
        log.info("==> sql: /* {} */", sqlId);
        log.info("{}", sql);

        long start = System.currentTimeMillis();

        try {
            returnValue = invocation.proceed();
        } catch (InvocationTargetException | IllegalAccessException e) {
            Log.Error(e.getMessage());
        }

        long end = System.currentTimeMillis();

        log.info("<== sql END：{} ms", end - start);

        return returnValue;
    }

    @Override
    public Object plugin(Object target) {
        return Interceptor.super.plugin(target);
    }

    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }

    /**
     * 로그 생성 용 SQL
     */
    private String generateSql(Configuration configuration, BoundSql boundSql) {
        // 매핑 파라미터 정보
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

        // 쿼리문
        String sql = boundSql.getSql();
        if (CollectionUtils.isNotEmpty(parameterMappings) && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    }
                }
            }
        }

        return sql;
    }

    /**
     * 파라미터 값 추출
     */
    private String getParameterValue(Object param) {

        if (param instanceof LocalDate obj) {
            // yyyy-MM-dd
            return "'" + obj.format(DateTimeFormatter.ISO_DATE) + "'";
        } else if (param instanceof LocalDateTime obj) {
            // yyyy-MM-ddTHH24:mi:ss
            return "'" + obj.format(DateTimeFormatter.ISO_DATE_TIME) + "'";
        } else if (param instanceof Integer || param instanceof Byte || param instanceof Short) {
            return String.valueOf(param);
        }

        return "'" + param + "'";
    }

    /**
     * Audit Column 셋팅
     */
    private void setAuditing(Object param) {
        Log.Info("[MybatisExecuteInterceptor]");
        OrgUserDvo dvo = SessionUtils.getUserInfo();

        if (dvo != null && param instanceof AuditDvo auditDvo) {
            String empNo = dvo.getEmpNo();
            LocalDateTime now = LocalDateTime.now();

            auditDvo.setCrtDthr(now);
            auditDvo.setCrtUserId(empNo);
            auditDvo.setUpdDthr(now);
            auditDvo.setUpdUserId(empNo);
        }
    }
}
