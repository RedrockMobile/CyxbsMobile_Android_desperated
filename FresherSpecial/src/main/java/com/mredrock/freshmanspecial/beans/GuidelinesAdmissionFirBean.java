package com.mredrock.freshmanspecial.beans;

import java.util.List;

/**
 * Created by glossimar on 2017/8/10.
 */

public class GuidelinesAdmissionFirBean {
    private String FirstTitle;

    public String getFirstTitle() {
        return FirstTitle;
    }

    public void setFirstTitle(String firstTitle) {
        FirstTitle = firstTitle;
    }

    public List<GuidelinesAdmissionSecBean> getGuidelinesAdmissionSecBeanList() {
        return guidelinesAdmissionSecBeanList;
    }

    public void setGuidelinesAdmissionSecBeanList(List<GuidelinesAdmissionSecBean> guidelinesAdmissionSecBeanList) {
        this.guidelinesAdmissionSecBeanList = guidelinesAdmissionSecBeanList;
    }

    private List<GuidelinesAdmissionSecBean> guidelinesAdmissionSecBeanList;

    public static class GuidelinesAdmissionSecBean {
        private String text;


        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
