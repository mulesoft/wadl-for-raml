package org.mulesoft.web.app.model;

import java.util.LinkedHashMap;

public class ResourceOwner extends AbstractElement {

    LinkedHashMap<String,ResourceModel> resources = new LinkedHashMap<String,ResourceModel>();

    public ResourceOwner() {
        super();
    }

    public LinkedHashMap<String,ResourceModel> getResources() {
        return resources;
    }

    public void addResource(ResourceModel res) {
        this.resources.put(res.getPath(), res);
    }

}