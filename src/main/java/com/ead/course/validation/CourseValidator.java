package com.ead.course.validation;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ead.course.configs.security.AuthenticationCurrentUserService;
import com.ead.course.dtos.CourseDto;
import com.ead.course.enums.UserType;
import com.ead.course.models.UserModel;
import com.ead.course.services.UserService;

@Component
public class CourseValidator implements Validator {
	
	@Autowired
	@Qualifier("defaultValidator")
	private Validator validator;

	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationCurrentUserService authenticationCurrentUserService;

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate(Object target, Errors errors) {
		CourseDto courseDto = (CourseDto) target;
		validator.validate(courseDto, errors);
		if(!errors.hasErrors()) {
			validateUserInstructor(courseDto.getUserInstructor(), errors);
		}
	}
	
	private void validateUserInstructor(UUID userInstructor, Errors errors) {
		UUID currentUserId = authenticationCurrentUserService.getCurrentUser().getUserId();
		if(!currentUserId.equals(userInstructor)) {
			throw new AccessDeniedException(HttpStatus.FORBIDDEN.getReasonPhrase());
		}
		Optional<UserModel> userModelOptional = userService.findById(userInstructor);
		if(!userModelOptional.isPresent()) {
			errors.rejectValue("userInstructor", "userInstructorError", "Instructor not found.");
		}
		if(userModelOptional.get().getUserType().equals(UserType.STUDENT.toString())) {
			errors.rejectValue("userInstructor", "userInstructorError", "User must be INSTRUCTOR or ADMIN.");			
		}
	}


}
