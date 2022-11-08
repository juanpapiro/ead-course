package com.ead.course.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ead.course.models.ModuleModel;

public interface ModuleRepository extends JpaRepository<ModuleModel, UUID>, JpaSpecificationExecutor<ModuleModel> {

//	@EntityGraph(attributePaths = {"course"})
//	ModuleModel findByTitle(String title);
	
	@Query(value = "select * from TB_MODULES where course_course_id = :courseId", nativeQuery = true)
	List<ModuleModel> findAllModulesIntoCourse(@Param("courseId") UUID courseId);

	@Query(value = "select * from TB_MODULES where module_id = :moduleId and course_course_id = :courseId", nativeQuery = true)
	Optional<ModuleModel> findModuleIntoCourse(@Param("courseId") UUID courseId, @Param("moduleId") UUID moduleId);

	
}
