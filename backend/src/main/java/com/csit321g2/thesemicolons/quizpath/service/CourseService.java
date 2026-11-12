
package com.csit321g2.thesemicolons.quizpath.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.csit321g2.thesemicolons.quizpath.dto.CourseRequest;
import com.csit321g2.thesemicolons.quizpath.dto.CourseResponse;
import com.csit321g2.thesemicolons.quizpath.entity.CourseEntity;
import com.csit321g2.thesemicolons.quizpath.entity.UserEntity;
import com.csit321g2.thesemicolons.quizpath.entity.UserRole;
import com.csit321g2.thesemicolons.quizpath.repository.CourseRepository;
import com.csit321g2.thesemicolons.quizpath.repository.UserRepository;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseService(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public CourseResponse createCourse(CourseRequest request) {
        UserEntity teacher = findTeacher(request.teacherId());

        if (courseRepository.existsByCourseCode(request.courseCode())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Course code already exists");
        }

        CourseEntity course = new CourseEntity(
                teacher,
                request.courseCode(),
                request.courseName(),
                request.description()
        );

        return toResponse(courseRepository.save(course));
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CourseResponse getCourseById(Long id) {
        CourseEntity course = findCourse(id);
        return toResponse(course);
    }

    @Transactional
    public CourseResponse updateCourse(Long id, CourseRequest request) {
        CourseEntity course = findCourse(id);
        UserEntity teacher = findTeacher(request.teacherId());

        if (!course.getCourseCode().equals(request.courseCode())
                && courseRepository.existsByCourseCode(request.courseCode())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Course code already exists");
        }

        course.setTeacher(teacher);
        course.setCourseCode(request.courseCode());
        course.setCourseName(request.courseName());
        course.setDescription(request.description());

        return toResponse(courseRepository.save(course));
    }

    @Transactional
    public void deleteCourse(Long id) {
        CourseEntity course = findCourse(id);
        courseRepository.delete(course);
    }

    private CourseEntity findCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Course not found"
                ));
    }

    private UserEntity findTeacher(Long teacherId) {
        UserEntity teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Teacher not found"
                ));

        if (teacher.getRole() != UserRole.TEACHER) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Selected user must have the TEACHER role"
            );
        }

        return teacher;
    }

    private CourseResponse toResponse(CourseEntity course) {
        return new CourseResponse(
                course.getCourseId(),
                course.getTeacher().getUserId(),
                course.getTeacher().getFullName(),
                course.getCourseCode(),
                course.getCourseName(),
                course.getDescription()
        );
    }
}
