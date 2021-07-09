package org.njuse.nekocoin.nekocoin.service;

import org.njuse.nekocoin.nekocoin.entity.Relation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RelationshipService {
    public void buildRelation(String source, String target, String value, String graphId);
    public void buildRelationIncValue(String source, String target, String value, String graphId);
    public void deleteRelationByValue(String value, String graphId);
    public void deleteRelationByNodes(String source, String target, String graphId);
    public boolean updateRelation(Relation newRelation, String source, String target, String graphId);
    public boolean findRelation(String source, String target, String relationName, String graphId);
    public Relation getRelation(String source, String target, String relationName, String graphId);
    public List<Relation> getAllRelations(String graphId);
}
