package org.mulesoft.web.app.model;


public class ApplicationModel extends ResourceOwner{
    
    private String title;
    
    private String baseUri;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }
    
}
