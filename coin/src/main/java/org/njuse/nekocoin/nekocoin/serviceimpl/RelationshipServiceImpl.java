package org.njuse.nekocoin.nekocoin.serviceimpl;

import org.njuse.nekocoin.nekocoin.dao.RelationshipDao;

import org.njuse.nekocoin.nekocoin.entity.Relation;
import org.njuse.nekocoin.nekocoin.service.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelationshipServiceImpl implements RelationshipService {
    private final
    RelationshipDao relationshipDao;

    @Autowired
    public RelationshipServiceImpl(RelationshipDao relationshipDao) {
        this.relationshipDao = relationshipDao;
    }

    @Override
    public void buildRelation(String source, String target, String value, String graphId){
        relationshipDao.buildRelation(source,target, value, graphId);
    }
    @Override
    public void buildRelationIncValue(String source, String target, String value, String graphId){
        relationshipDao.buildRelationIncValue(source,target,value, graphId);
    }
    @Override
    public void deleteRelationByValue(String value, String graphId){
        relationshipDao.deleteRelationByValue(value, graphId);
    }
    @Override
    public void deleteRelationByNodes(String source, String target, String graphId){
        relationshipDao.deleteRelationByNodes(source, target, graphId);
    }
    @Override
    public boolean updateRelation(Relation newRelation, String source, String target, String graphId){
        return  relationshipDao.updateRelation(newRelation, source, target, graphId);
    }
    @Override
    public boolean findRelation(String source, String target, String relationName, String graphId){
        return relationshipDao.findRelation(source,target,relationName, graphId);
    }
    @Override
    public Relation getRelation(String source, String target, String relationName, String graphId){
        return relationshipDao.getRelation(source,target,relationName, graphId);
    }
    @Override
    public List<Relation> getAllRelations(String graphId){
        return relationshipDao.retrieveAllRelations(graphId);
    }

}
