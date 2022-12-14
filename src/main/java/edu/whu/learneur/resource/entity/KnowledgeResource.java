package edu.whu.learneur.resource.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 知识点与资源关联表
 * </p>
 *
 * @author Learneur
 * @since 2022-12-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("knowledge_resource")
public class KnowledgeResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 资源id
     */
    @TableField(value = "id_resource")
    private Long idResource;

    /**
     * 知识点id
     */
    @TableField(value = "id_knowledge")
    private Long idKnowledge;


    private int type;

}