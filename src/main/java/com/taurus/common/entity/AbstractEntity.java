package com.taurus.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

/**
 * 实体/表 基类
 *
 * @author 郑楷山
 **/

@Accessors(chain = true)
@Getter
@Setter
@MappedSuperclass
public abstract class AbstractEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long fdId;

	/**
	 * 1:启用 0:禁用
	 */
	@Column(columnDefinition = "int(2)", nullable = false)
	private Boolean fdEnabled;

	/**
	 * 是否已删除
	 */
	@Column(columnDefinition = "int(2)", nullable = false)
	@JsonIgnore
	private Boolean fdDeleted;

	@Column
	private String fdRemark;

	@Column
	private String fdCreateBy;

	@Column
	private Date fdCreateTime;

	@Column
	private String fdUpdateBy;

	@Column
	private Date fdUpdateTime;

}
