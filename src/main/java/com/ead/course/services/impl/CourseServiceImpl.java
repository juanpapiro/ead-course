package com.ead.course.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ead.course.dtos.NotificationCommandDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.models.UserModel;
import com.ead.course.publisher.NotificationCommandPublisher;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.repositories.UserRepository;
import com.ead.course.services.CourseService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class CourseServiceImpl implements CourseService {
	
	@Autowired
	private CourseRepository courseRepository;	
	
	@Autowired
	private ModuleRepository moduleRepository;
	
	@Autowired
	private LessonRepository lessonRepository;

	@Autowired
	private UserRepository userRepository;	
	
	@Autowired
	private NotificationCommandPublisher notificationCommandPublisher;
	
	@Transactional
	@Override
	public void delete(CourseModel courseModel) {
		List<ModuleModel> modules = moduleRepository.findAllModulesIntoCourse(courseModel.getCourseId());
		if(!modules.isEmpty()) {
			modules.forEach(module -> {
				List<LessonModel> lessons = lessonRepository.findAllLessonsIntoModule(module.getModuleId());
				if(!lessons.isEmpty()) {
					lessonRepository.deleteAll(lessons);
				}
			});
			moduleRepository.deleteAll(modules);
		}
		courseRepository.deleteCourseUserByCourse(courseModel.getCourseId());
		courseRepository.delete(courseModel);
	}

	@Override
	public CourseModel save(CourseModel courseModel) {
		return courseRepository.save(courseModel);
	}

	@Override
	public Optional<CourseModel> findById(UUID courseId) {
		return courseRepository.findById(courseId);
	}

	@Override
	public Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable) {
		return courseRepository.findAll(spec, pageable);
	}

	@Override
	public boolean existsByCourseAndUser(UUID courseId, UUID userId) {
		return courseRepository.existsByCourseAndUser(courseId, userId);
	}

	@Transactional
	@Override
	public void saveSubscriptionUserService(UUID courseId, UUID userId) {
		courseRepository.saveCourseUser(courseId, userId);
	}
	
	@Transactional
	@Override
	public void saveSubscriptionUserInCourseAndSendNotification(CourseModel courseModel, UserModel userModel) {
		courseRepository.saveCourseUser(courseModel.getCourseId(), userModel.getUserId());
		try {
			NotificationCommandDto notificationCommandDto = new NotificationCommandDto();
			notificationCommandDto.setTitle(String.format("Bem-vindo ao curso %s", courseModel.getName()));
			notificationCommandDto.setMessage(String.format("%s, sua inscri????o foi relizada com sucesso!", userModel.getFullName()));
			notificationCommandDto.setUserId(userModel.getUserId());
			notificationCommandPublisher.publishNotficationCommand(notificationCommandDto);
		} catch(Exception e) {
			log.warn("Error send notification");
		}
	}
	

}
