package org.xiao.cs.common.box.domain;

import java.util.List;

public class ArgsState<By, To> {
    private By by;
    private To to;

    public ArgsState() {}

    public ArgsState(By by, To to) {
        this.by = by;
        this.to = to;
    }

    public By getBy() {
        return by;
    }

    public void setBy(By by) {
        this.by = by;
    }

    public To getTo() {
        return to;
    }

    public void setTo(To to) {
        this.to = to;
    }
    
    public static class Many<By, To> extends ArgsState<List<By>, To> {
        public Many() {
            super();
        }

        public Many(List<By> bys, To to) {
            super(bys, to);
        }
    }
}
