package com.ead.course.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TB_COURSES")
public class CourseModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID courseId;
	@Column(nullable = false, length = 150)
	private String name;
	@Column(nullable = false, length = 250)
	private String description;
	@Column
	private String imageUrl;
	@Column(nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	private LocalDateTime creationDate;
	@Column(nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	private LocalDateTime lastUpdateDate;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private CourseStatus courseStatus;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private CourseLevel courseLevel;
	@Column(nullable = false)
	private UUID userInstructor;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
	@Fetch(FetchMode.SUBSELECT)
	private Set<ModuleModel> modules;
//	cascade = CascadeType.ALL faz um comando de delete para cada item de Module vinculado ao curso
//	orphanRemoval = true caso haja algum módulo sem vinculação com curso faz a exclusão
//	@OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//	delega ao banco de dados a responsabilidade de exclusão dos registros de Module
//	esta anotação com action = CASCADE faz com que seja executado um comando de delete para o Curso
//	e um comando de delete para dotos os módulos associados
//	@OnDelete(action = OnDeleteAction.CASCADE) 
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "TB_COURSES_USERS",
			   joinColumns = @JoinColumn(name = "course_id"),
			   inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<UserModel> users;
	
}