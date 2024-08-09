package org.xiao.cs.sso.box.permission;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.xiao.cs.common.box.exception.CommonException;
import org.xiao.cs.common.box.utils.SpringUtils;
import org.xiao.cs.properties.box.SymbolProperties;
import org.xiao.cs.sso.box.enumerate.VisitorMode;
import org.xiao.cs.sso.box.properties.SSOProperties;
import org.xiao.cs.sso.box.utils.TokenUtils;

import java.util.List;

@Component(AuthorizePermission.NAME)
public class AuthorizePermission {

    public static final String NAME = "aps";

    @Resource
    SymbolProperties symbolProperties;
    @Resource
    SSOProperties ssoProperties;
    @Resource
    TokenUtils tokenUtils;

    public boolean hasVisitR() throws CommonException {
        return hasVisitCompetence(VisitorMode.R);
    }

    public boolean hasVisitW() throws CommonException {
        return hasVisitCompetence(VisitorMode.W);
    }

    public boolean hasVisitRW() throws CommonException {
        return hasVisitCompetence(VisitorMode.RW);
    }

    // 访问能力权限
    public boolean hasVisitCompetence(VisitorMode visitorMode) throws CommonException {
        String applicationName = SpringUtils.getApplicationName();
        Jws<Claims> parser = tokenUtils.parser();
        String issuer = parser.getBody().getIssuer();

        if (applicationName.equals(issuer)) {
            return true;
        }

        List<SSOProperties.VisitCompetenceProperties> visitCompetenceProperties =
                ssoProperties.getVisitCompetence().get(applicationName);

        if (visitCompetenceProperties != null && !visitCompetenceProperties.isEmpty()) {
            return visitCompetenceProperties
                    .stream()
                    .anyMatch(visit -> issuer.equals(visit.getVisitor()) && visitorMode.equals(visit.getMode()));
        }

        return false;
    }

    // 组织权限
    public boolean hasOrganizations(String aimOrganizations) throws CommonException {
        return hasOrganizations(aimOrganizations.split(symbolProperties.getSeparate()));
    }

    public boolean hasOrganizations(String aimOrganizations, String currentOrganizations) throws CommonException {
        return hasOrganizations(aimOrganizations.split(symbolProperties.getSeparate()), currentOrganizations.split(symbolProperties.getSeparate()));
    }

    public boolean hasOrganizations(String[] aimOrganizations) throws CommonException {
        String [] currentOrganizations = tokenUtils.toBean().toOrgArray();
        return hasOrganizations(aimOrganizations, currentOrganizations);
    }

    public boolean hasOrganizations(String[] aimOrganizations, String[] currentOrganizations) throws CommonException {
        if (StringUtils.isAnyBlank(aimOrganizations)) {
            return false;
        }

        if (StringUtils.isNoneBlank(currentOrganizations)) {
            for (String aimOrganization : aimOrganizations) {
                if (StringUtils.isNotBlank(aimOrganization) && StringUtils.startsWithAny(aimOrganization, currentOrganizations)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean hasOrganization(String aimOrganizations) throws CommonException {
        String [] currentOrganizations = tokenUtils.toBean().toOrgArray();
        return hasOrganization(aimOrganizations, currentOrganizations);
    }

    public boolean hasOrganization(String aimOrganizations, String currentOrganizations) throws CommonException {
        return hasOrganization(aimOrganizations, currentOrganizations.split(symbolProperties.getSeparate()));
    }

    public boolean hasOrganization(String aimOrganizations, String[] currentOrganizations) throws CommonException {
        if (StringUtils.isBlank(aimOrganizations)) {
            return false;
        }

        if (StringUtils.isNoneBlank(currentOrganizations)) {
            return StringUtils.startsWithAny(aimOrganizations, currentOrganizations);
        }

        return false;
    }

    // 角色权限
    public boolean hasRoles(String aimRoles) throws CommonException {
        return hasRoles(aimRoles.split(symbolProperties.getSeparate()));
    }

    public boolean hasRoles(String aimRoles, String currentRoles) throws CommonException {
        return hasRoles(aimRoles.split(symbolProperties.getSeparate()), currentRoles.split(symbolProperties.getSeparate()));
    }

    public boolean hasRoles(String[] aimRoles) throws CommonException {
        String [] currentRoles = tokenUtils.toBean().toRoleArray();
        return hasRoles(aimRoles, currentRoles);
    }

    public boolean hasRoles(String[] aimRoles, String[] currentRoles) throws CommonException {
        if (StringUtils.isAnyBlank(aimRoles)) {
            return false;
        }

        if (StringUtils.isNoneBlank(currentRoles)) {
            for (String aimRole : aimRoles) {
                if (StringUtils.isNotBlank(aimRole) && StringUtils.containsAny(aimRole, currentRoles)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean hasRole(String aimRole) throws CommonException {
        String [] currentRoles = tokenUtils.toBean().toRoleArray();
        return hasRole(aimRole, currentRoles);
    }

    public boolean hasRole(String aimRole, String currentRoles) throws CommonException {
        return hasRole(aimRole, currentRoles.split(symbolProperties.getSeparate()));
    }

    public boolean hasRole(String aimRole, String[] currentRoles) throws CommonException {
        if (StringUtils.isBlank(aimRole)) {
            return false;
        }

        if (StringUtils.isNoneBlank(currentRoles)) {
            return StringUtils.containsAny(aimRole, currentRoles);
        }

        return false;
    }

    // 标识符权限
    public boolean hasPermissions(String aimPermissions) throws CommonException {
        return hasPermissions(aimPermissions.split(symbolProperties.getSeparate()));
    }

    public boolean hasPermissions(String aimPermissions, String currentPermissions) throws CommonException {
        return hasPermissions(aimPermissions.split(symbolProperties.getSeparate()), currentPermissions.split(symbolProperties.getSeparate()));
    }

    public boolean hasPermissions(String[] aimPermissions) throws CommonException {
        String [] currentPermissions = tokenUtils.toBean().toPermissionArray();
        return hasPermissions(aimPermissions, currentPermissions);
    }

    public boolean hasPermissions(String[] aimPermissions, String[] currentPermissions) throws CommonException {
        if (StringUtils.isAnyBlank(aimPermissions)) {
            return false;
        }

        if (StringUtils.isNoneBlank(currentPermissions)) {
            for (String aimPermission : aimPermissions) {
                if (StringUtils.isNotBlank(aimPermission) && hasPermission(aimPermission, currentPermissions)) {
                    return true;
                }
            }

        }

        return false;
    }

    private boolean hasPermission(String aimPermission) {
        String [] currentPermissions = tokenUtils.toBean().toPermissionArray();
        return hasPermission(aimPermission, currentPermissions);
    }

    private boolean hasPermission(String aimPermission, String currentPermissions) {
        return hasPermission(aimPermission, currentPermissions.split(symbolProperties.getSeparate()));
    }

    private boolean hasPermission(String aimPermission, String [] currentPermissions) {

        String topPermissions = symbolProperties.getTopPermissions();
        String universal = symbolProperties.getUniversal();
        String separate = symbolProperties.getSeparate();
        String rank = symbolProperties.getRank();

        if (StringUtils.containsAny(topPermissions, currentPermissions)) {
            return true;
        }

        if (StringUtils.containsAny(aimPermission, currentPermissions)) {
            return true;
        }


        for (String currentPermission : currentPermissions) {
            if (currentPermission.contains(universal)) {

                int count = 0;
                String [] currentUnits = currentPermission.split(rank);
                String [] aimUnits = aimPermission.split(rank);

                if (currentUnits.length > aimUnits.length || (currentUnits.length == 1 && currentUnits[0].equals(universal))) {
                    continue;
                }

                for (String unit : currentUnits) {

                    if (universal.equals(unit)) {
                        aimUnits[count] = universal;
                    }

                    count++;
                }

                if (StringUtils.join(aimUnits, separate).startsWith(StringUtils.join(currentUnits, separate))) {
                    return true;
                }
            }
        }

        return false;
    }
}
