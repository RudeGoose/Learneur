package edu.whu.learneur.neo4j.dao;

import edu.whu.learneur.neo4j.domain.Knowledge;
import edu.whu.learneur.neo4j.domain.Relation;
import org.neo4j.driver.Record;
import org.neo4j.driver.internal.InternalNode;
import org.neo4j.driver.internal.InternalRelationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class KnowledgeRepo implements KnowledgeRepoInterface {

    @Autowired
    Neo4jClient neo4jClient;

    public Optional<List<Relation>> updateRelationById(Long relationId, String type, String description) {
        List<Relation> update = neo4jClient
                .query(String.format("MATCH (n:knowledge)-[r]->(m:knowledge) WHERE id(r) = %d MERGE (n)-[r2:%s {type:'%s',description:'%s'}]->(m) delete r RETURN r2",relationId,type,type,description))
                .fetch().all().stream().map(record -> {
                    Relation relation = new Relation();
                    InternalRelationship internalRelationship = (InternalRelationship) record.get("r2");
                    relation.setId(internalRelationship.id());
                    relation.setType(internalRelationship.type());
                    relation.setDescription(internalRelationship.get("description").asString());
                    relation.setStart(internalRelationship.startNodeId());
                    relation.setEnd(internalRelationship.endNodeId());
                    return relation;
                }).collect(Collectors.toList());
        return Optional.of(update);
        //@Query("MATCH (n:knowledge)-[r]->(m:knowledge) WHERE id(r) = $relationId MERGE (n)-[r2:`:#{literal(#type)}` {type:$type,description:$description}]->(m) delete r RETURN id(startNode(r2)) as start,id(endNode(r2)) as end,id(r2) as id,r2.type as type,r2.description as description")
    }

    public Optional<Relation> addRelation(Long knowledgeId, Long relatedId, String type, String description) {
      Relation relation = neo4jClient
                .query(String.format("MATCH (n:knowledge),(m:knowledge) WHERE id(n) = %d AND id(m)=%d MERGE (n)-[r:%s {type:'%s',description:'%s'}]->(m) RETURN r", knowledgeId,relatedId,type,type,description))
                .fetch().one().map(record -> {
                    Relation relation1 = new Relation();
                    InternalRelationship internalRelationship = (InternalRelationship) record.get("r");
                    relation1.setId(internalRelationship.id());
                    relation1.setType(internalRelationship.type());
                    relation1.setDescription(internalRelationship.get("description").asString());
                    relation1.setStart(internalRelationship.startNodeId());
                    relation1.setEnd(internalRelationship.endNodeId());
                    return relation1;
                }).orElse(null);
        return Optional.ofNullable(relation);
    };

    public Optional<Knowledge> updateTagById(Knowledge knowledge, Long id) {
        Knowledge knowledget = neo4jClient
                .query(String.format("MATCH (n:knowledge) WHERE id(n) = %d SET n.name = '%s',n.description = '%s' RETURN n",id,knowledge.getName(),knowledge.getDescription()))
                .fetch().one().map(record -> {
                    Knowledge knowledge1 = new Knowledge();
                    InternalNode internalNode = (InternalNode) record.get("n");
                    knowledge1.setId(internalNode.id());
                    knowledge1.setName(internalNode.get("name").asString());
                    knowledge1.setDescription(internalNode.get("description").asString());
                    return knowledge1;
                }).orElse(null);
        return Optional.ofNullable(knowledget);
    }

    public Optional<Knowledge> deleteById(Long id) {
        Knowledge knowledget = neo4jClient
                .query(String.format("MATCH (n:knowledge) WHERE id(n) = %d DETACH DELETE n RETURN n",id))
                .fetch().one().map(record -> {
                    Knowledge knowledge1 = new Knowledge();
                    InternalNode internalNode = (InternalNode) record.get("n");
                    knowledge1.setId(internalNode.id());
                    knowledge1.setName(internalNode.get("name").asString());
                    knowledge1.setDescription(internalNode.get("description").asString());
                    return knowledge1;
                }).orElse(null);
        return Optional.ofNullable(knowledget);
    }

    @Override
    public Optional<Relation> deleteRelationById(Long id) {
        Relation relation = neo4jClient
                .query(String.format("MATCH (n:knowledge)-[r]->(m:knowledge) WHERE id(r) = %d DELETE r RETURN r",id))
                .fetch().one().map(record -> {
                    Relation relation1 = new Relation();
                    InternalRelationship internalRelationship = (InternalRelationship) record.get("r");
                    relation1.setId(internalRelationship.id());
                    relation1.setType(internalRelationship.type());
                    relation1.setDescription(internalRelationship.get("description").asString());
                    relation1.setStart(internalRelationship.startNodeId());
                    relation1.setEnd(internalRelationship.endNodeId());
                    return relation1;
                }).orElse(null);
        return Optional.ofNullable(relation);
    }

    @Override
    public Optional<List<Relation>> findRelationByNameAndType(String name, String type) {
        List<Relation> relations = neo4jClient
                .query(String.format("MATCH (n:knowledge)-[r:%s]->(m:knowledge) WHERE n.name = '%s' RETURN r",type,name))
                .fetch().all().stream().map(record -> {
                    Relation relation = new Relation();
                    InternalRelationship internalRelationship = (InternalRelationship) record.get("r");
                    relation.setId(internalRelationship.id());
                    relation.setType(internalRelationship.type());
                    relation.setDescription(internalRelationship.get("description").asString());
                    relation.setStart(internalRelationship.startNodeId());
                    relation.setEnd(internalRelationship.endNodeId());
                    return relation;
                }).collect(Collectors.toList());
        return Optional.of(relations);
    }
}