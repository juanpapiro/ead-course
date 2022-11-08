package com.ead.course.services;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

public interface UtilsService {
	
	String createUrlGetAllUsersByCourse(UUID userId, Pageable pageable);
	String createUrlGetOneUserById(UUID userId);
	String createUrlPostSubscriptionUserInCourse(UUID userId);
	String createUrlDelteCourseInAuthUser(UUID courseId);

}
