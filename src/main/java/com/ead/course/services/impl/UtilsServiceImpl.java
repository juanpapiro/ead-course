package com.ead.course.services.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.ead.course.services.UtilsService;


@Service
public class UtilsServiceImpl implements UtilsService {
	
	@Value("${ead.api.url.authuser}")
	private String urlAuthUser;
	
	public String createUrlGetAllUsersByCourse(UUID courseId, Pageable pageable) {
		return urlAuthUser + "/users?courseId=" + courseId + "&page=" + pageable.getPageNumber() + "&size="
				+ pageable.getPageSize() + "&sort=" + pageable.getSort().toString().replaceAll(": ", ",");
	}
	
	public String createUrlGetOneUserById(UUID userId) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlAuthUser)
				.pathSegment("users")
				.pathSegment(userId.toString());
		return builder.toUriString();
	}

	@Override
	public String createUrlPostSubscriptionUserInCourse(UUID userId) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlAuthUser)
				.pathSegment("users")
				.pathSegment(userId.toString())
				.pathSegment("courses")
				.pathSegment("subscription");
		return builder.toUriString();
	}

	@Override
	public String createUrlDelteCourseInAuthUser(UUID courseId) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlAuthUser)
				.path("/users/courses")
				.pathSegment(courseId.toString());
		return builder.toString();
	}
	

}
