package com.hetongxue.cloud.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户
 *
 * @author hy
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Comment("系统用户表")
@TableName("sys_user")
@Entity(name = "sys_user")
public class User implements Serializable {

    @Id
    @Comment("ID")
    @Column(nullable = false, length = 20)
    private Long id;

    @Comment("用户名")
    @Column(nullable = false, length = 200)
    private String username;

    @Comment("用户密码")
    @Column(nullable = false, length = 300)
    private String password;

    @Comment("用户描述")
    @Column(columnDefinition = "longtext")
    private String description;

    @Comment("创建人")
    @Column(nullable = false)
    private Long creator;

    @Comment("创建时间")
    @Column(nullable = false)
    private LocalDateTime createTime;

    @Comment("更新人")
    @Column(nullable = false)
    private Long updater;

    @Comment("更新时间")
    @Column(nullable = false)
    private LocalDateTime updateTime;

    @Comment("删除标记")
    @Column(nullable = false, columnDefinition = "default 0")
    @TableLogic
    private Integer deleted;

}
