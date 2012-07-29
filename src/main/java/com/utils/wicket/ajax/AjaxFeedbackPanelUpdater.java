package com.utils.wicket.ajax;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import java.util.Map;

/**
 * This class add to AjaxRequestTarget.addComponent() all feddback panels that should be repaint
 *
 * Based on Apache Wicket Cookbook
 */
public class AjaxFeedbackPanelUpdater implements AjaxRequestTarget.IListener{
    @Override
    public void onBeforeRespond(Map<String, Component> map, final AjaxRequestTarget target) {
        target.getPage().visitChildren(FeedbackPanel.class, new IVisitor<FeedbackPanel, Object>() {
            @Override
            public void component(FeedbackPanel object, IVisit<Object> visit) {
                if (object.getOutputMarkupId()) {
                    //maybe check methdos hasErrorMessages and hasFeedbackMessages on feedback panel?
                    target.add(object);
                }

            }
        });
    }

    @Override
    public void onAfterRespond(Map<String, Component> map, AjaxRequestTarget.IJavaScriptResponse response) {

    }
}
