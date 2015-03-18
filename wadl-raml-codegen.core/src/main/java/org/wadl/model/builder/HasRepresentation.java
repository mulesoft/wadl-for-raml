package org.wadl.model.builder;

import java.util.ArrayList;
import java.util.List;

import org.mulesoft.web.app.model.AbstractElement;
import org.mulesoft.web.app.model.RepresentationModel;

public class HasRepresentation extends AbstractElement {
    
    
    private List<RepresentationModel> representations = new ArrayList<RepresentationModel>();


    public List<RepresentationModel> getRepresentations() {
        return representations;
    }

    public void addRepresentation(RepresentationModel representation) {
        this.representations.add(representation);
    }

}
