package edu.whu.learneur.neo4j.dao;

import cn.hutool.core.lang.Singleton;
import edu.whu.learneur.neo4j.domain.Knowledge;
import edu.whu.learneur.neo4j.domain.Relation;
import org.apache.http.annotation.Obsolete;
import org.neo4j.driver.Record;
import org.neo4j.driver.internal.InternalNode;
import org.neo4j.driver.internal.InternalRelationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * <p>
 *     知识图谱节点数据访问层
 *     用于处理数据库的操作
 *     与KnowledgeService配合使用
 * </p>
 * @author Geraltigas
 * @since 2022-12-10
 * @version 1.0
 */
@Repository
public class KnowledgeRepo implements KnowledgeRepoInterface {

    @Autowired
    Neo4jClient neo4jClient;


    /**
     * @param relationId 关系id
     * @param type 关系类型
     * @param description 关系描述
     * @return Optional<List<Relation>> 更新的关系列表
     */
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

    /**
     * @param knowledgeId 节点id
     * @param relatedId 相关节点id
     * @param type 关系类型
     * @param description 关系描述
     * @return Optional<Relation> 新增的关系
     */
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

    /**
     * @param knowledge 节点
     * @param id 节点id
     * @return Optional<Knowledge> 更新的节点
     */
    public Optional<Knowledge> updateTagById(Knowledge knowledge, Long id) {
        Knowledge knowledget = neo4jClient
                .query(String.format("MATCH (n:knowledge) WHERE id(n) = %d SET n.name = '%s',n.description = '%s' RETURN n",id,knowledge.getName(),knowledge.getDescription()))
                .fetch().one().map(record -> {
                    Knowledge knowledge1 = new Knowledge();
                    InternalNode internalNode = (InternalNode) record.get("n");
                    knowledge1.setId(internalNode.id());
                    knowledge1.setName(internalNode.get("name").asString());
                    knowledge1.setDescription(internalNode.get("description").asString());
                    knowledge1.setForeignId(internalNode.get("foreign_id").asLong());
                    return knowledge1;
                }).orElse(null);
        return Optional.ofNullable(knowledget);
    }

    /**
     * @param id 节点id
     * @return Optional<Knowledge> 删除的节点
     */
    public Optional<Knowledge> deleteById(Long id) {
        Knowledge knowledget = neo4jClient
                .query(String.format("MATCH (n:knowledge) WHERE id(n) = %d DETACH DELETE n RETURN n",id))
                .fetch().one().map(record -> {
                    Knowledge knowledge1 = new Knowledge();
                    InternalNode internalNode = (InternalNode) record.get("n");
                    knowledge1.setId(internalNode.id());
                    knowledge1.setName(internalNode.get("name").asString());
                    knowledge1.setDescription(internalNode.get("description").asString());
                    knowledge1.setForeignId(null);
                    return knowledge1;
                }).orElse(null);
        return Optional.ofNullable(knowledget);
    }

    /**
     * @param start 起始节点id
     * @param end 结束节点id
     * @return Optional<Relation> 删除的关系
     */
    @Override
    public Optional<Relation> deleteRelationById(Long start, Long end) {
        Relation relation = neo4jClient
                .query(String.format("MATCH (n:knowledge)-[r]->(m:knowledge) WHERE id(n) = %d AND id(m) = %d DELETE r RETURN r",start,end))
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

    /**
     * @param name 节点名称
     * @param type 关系类型
     * @return Optional<List<Relation>>> 关系列表
     */
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

    /**
     * @param name 节点名称
     * @param depth 深度
     * @return Optional<List<Relation>>> 关系列表
     */
    @Override
    public Optional<List<Relation>> findRelationByName(String name, Integer depth) {
        List<Relation> relations = neo4jClient
                .query(String.format("MATCH (n:knowledge)-[r*1..%d]->(m:knowledge) WHERE n.name = '%s' RETURN DISTINCT r",depth,name))
                .fetch().all().stream().map(record -> {
                    Relation relation = new Relation();
                    List<InternalRelationship> collections = (List<InternalRelationship>)record.get("r");
                    relation.setId(collections.get(collections.size()-1).id());
                    relation.setType(collections.get(collections.size()-1).type());
                    relation.setDescription(collections.get(collections.size()-1).get("description").asString());
                    relation.setType(collections.get(collections.size()-1).get("type").asString());
                    relation.setStart(collections.get(collections.size()-1).startNodeId());
                    relation.setEnd(collections.get(collections.size()-1).endNodeId());
                    return relation;
                }).collect(Collectors.toList());
        return Optional.of(relations);
    }

    /**
     * @param name 节点名称
     * @return Optional<List<Knowledge>>> 节点列表
     */
    @Override
    public Optional<List<Knowledge>> findAllRelated(String name) {
        List<Knowledge> knowledges = neo4jClient
                .query(String.format("MATCH (n:knowledge)-[r*1..3]->(m:knowledge) WHERE n.name = '%s' RETURN DISTINCT m",name))
                .fetch().all().stream().map(record -> {
                    Knowledge knowledge = new Knowledge();
                    InternalNode internalNode = (InternalNode) record.get("m");
                    knowledge.setId(internalNode.id());
                    knowledge.setName(internalNode.get("name").asString());
                    knowledge.setDescription(internalNode.get("description").asString());
                    knowledge.setForeignId(internalNode.get("foreign_id").asLong());
                    return knowledge;
                }).collect(Collectors.toList());
        return Optional.of(knowledges);
    }

    /**
     * @param name1 起始节点名称
     * @param name2 结束节点名称
     * @param description  关系描述
     * @param type 关系类型
     * @return Optional<Relation> 新增关系
     */
    @Override
    public Optional<Relation> addRelationByNames(String name1, String name2, String type, String description) {
        Relation relation = neo4jClient
                .query(String.format("MATCH (n:knowledge),(m:knowledge) WHERE n.name = '%s' AND m.name = '%s' CREATE (n)-[r:%s {type:'%s' ,description:'%s'}]->(m) RETURN r",name1,name2,type,type,description))
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
}
