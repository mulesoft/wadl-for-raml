package org.mulesoft.web.app.model;

public class AbstractElement {

    private DocumentationModel doc;
    
    private String id;

    public AbstractElement() {
        super();
    }

    public DocumentationModel getDoc() {
        return doc;
    }

    public void setDoc(DocumentationModel doc) {
        this.doc = doc;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}