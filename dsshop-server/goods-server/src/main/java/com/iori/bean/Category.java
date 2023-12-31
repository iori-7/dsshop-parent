package com.iori.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

/**
 * @ClassName TbCategoryModel
 * @Description 商品类目模型对象
 * @Author zj
 * @Date 2023/07/04 09:47
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("tb_category")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private Integer goodsNum;

    private String isShow;

    private String isMenu;

    private Integer seq;

    private Integer parentId;

    private Integer templateId;


}
