package org.xiao.cs.sso.box.domain;

public class TokenResponseBody {
    private String routine;
    private String hibernation;
    private boolean remember = false;

    public TokenResponseBody() {}
    public TokenResponseBody(String routine, String hibernation, boolean remember) {
        this.routine = routine;
        this.hibernation = hibernation;
        this.remember = remember;
    }

    public String getRoutine() {
        return routine;
    }

    public void setRoutine(String routine) {
        this.routine = routine;
    }

    public String getHibernation() {
        return hibernation;
    }

    public void setHibernation(String hibernation) {
        this.hibernation = hibernation;
    }

    public boolean isRemember() {
        return remember;
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
    }
}
