package com.ead.course.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ead.course.dtos.LessonDto;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.LessonService;
import com.ead.course.services.ModuleService;
import com.ead.course.specifications.SpecificationTemplate;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class LessonController {
	
	@Autowired
	private ModuleService moduleService;
	
	@Autowired
	private LessonService lessonService;
	
	@PreAuthorize("hasAnyRole('INSTRUCTOR')")
	@PostMapping("/modules/{moduleId}/lessons")
	public ResponseEntity<Object> saveLesson(
			@PathVariable(value = "moduleId") UUID moduleId,
			@RequestBody @Valid LessonDto lessonDto) {
		
		log.debug("POST saveLesson lessonDto received {} ", lessonDto.toString());
		
		Optional<ModuleModel> moduleModelOptional = moduleService.fidById(moduleId);
		
		if(!moduleModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found.");
		}
		
		var lessonModel = new LessonModel();
		BeanUtils.copyProperties(lessonDto, lessonModel);
		lessonModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
		lessonModel.setModule(moduleModelOptional.get());
		log.debug("POST saveLesson lessonId saved {} ", lessonModel.getLessonId());
        log.info("Lesson saved successfully lessonId {} ", lessonModel.getLessonId());
		return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.save(lessonModel));
	}
	
	@PreAuthorize("hasAnyRole('INSTRUCTOR')")
	@DeleteMapping("/modules/{moduleId}/lessons/{lessonId}")
	public ResponseEntity<Object> deleteLesson(
			@PathVariable(value = "moduleId") UUID moduleId,
			@PathVariable(value = "lessonId") UUID lessonId) {
		
		log.debug("DELETE deleteLesson lessonId received {} ", lessonId);
		
		Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId, lessonId);
		
		if(!lessonModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson Not Found.");
		}
		
		lessonService.delete(lessonModelOptional.get());
		
		log.debug("DELETE deleteLesson lessonId deleted {} ", lessonId);
        log.info("Lesson deleted successfully lessonId {} ", lessonId);
		
		return ResponseEntity.ok("Lesson deleted successfuly.");
	}
	
	@PreAuthorize("hasAnyRole('INSTRUCTOR')")
	@PutMapping("/modules/{moduleId}/lessons/{lessonId}")
	public ResponseEntity<Object> deleteLesson(
			@PathVariable(value = "moduleId") UUID moduleId,
			@PathVariable(value = "lessonId") UUID lessonId,
			@RequestBody @Valid LessonDto lessonDto) {
		
		log.debug("PUT updateLesson lessonDto received {} ", lessonDto.toString());
		
		Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId, lessonId);
		
		if(!lessonModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson Not Found.");
		}
		
		var lessonModel = lessonModelOptional.get();
		BeanUtils.copyProperties(lessonDto, lessonModel);
		
		log.debug("PUT updateLesson lessonId saved {} ", lessonModel.getLessonId());
        log.info("Lesson updated successfully lessonId {} ", lessonModel.getLessonId());
		
		return ResponseEntity.ok(lessonService.save(lessonModel));
		
	}
	
	@PreAuthorize("hasAnyRole('STUDENT')")
	@GetMapping("/modules/{moduleId}/lessons")
	public ResponseEntity<Page<LessonModel>> getAllLessonsIntoModule(
			@PathVariable(value = "moduleId") UUID moduleId,
			SpecificationTemplate.LessonSpec spec,
			@PageableDefault(page = 0, size = 10, sort = "lessonId", direction = Sort.Direction.ASC) Pageable pageable) {

		return ResponseEntity.ok(lessonService.findAllLessonsIntoModule(
				SpecificationTemplate.lessonModuleId(moduleId).and(spec), pageable));
		
	}
	
	@PreAuthorize("hasAnyRole('STUDENT')")
	@GetMapping("/modules/{moduleId}/lessons/{lessonId}")
	public ResponseEntity<Object> getOneLesson(
			@PathVariable(value = "moduleId") UUID moduleId,
			@PathVariable(value = "lessonId") UUID lessonId) {
		
		Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId, lessonId);

		if(!lessonModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson Not Found.");
		}
		
		return ResponseEntity.ok(lessonModelOptional.get());
		
	}

}
