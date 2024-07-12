package org.xiao.cs.sso.box.condition;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.lang.NonNull;
import org.xiao.cs.sso.box.constant.ProcessingConstant;

public class ProcessingConfigServiceCondition implements Condition {

    @Override
    public boolean matches(@NonNull ConditionContext context, @NonNull AnnotatedTypeMetadata metadata) {
        String issuanceCenter = context.getEnvironment().getProperty(ProcessingConstant.PROPERTY_ISSUANCE_CENTER_KEY);
        return StringUtils.isNotBlank(issuanceCenter) && (issuanceCenter.equals(Boolean.TRUE.toString()));
    }
}
