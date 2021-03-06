package com.fajar.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fajar.annotation.Dto;
import com.fajar.annotation.FormField;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Dto
@Entity
@Table(name="user_role")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRole extends BaseEntity  implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -725487831020131248L;
	@Column(unique = true)
	@FormField
	private String name;
	@Column
	@FormField
	private String access;
	 
	
}
