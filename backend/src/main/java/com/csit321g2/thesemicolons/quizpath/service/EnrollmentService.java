
package com.csit321g2.thesemicolons.quizpath.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.csit321g2.thesemicolons.quizpath.dto.EnrollmentRequest;
import com.csit321g2.thesemicolons.quizpath.dto.EnrollmentResponse;
import com.csit321g2.thesemicolons.quizpath.entity.CourseEntity;
import com.csit321g2.thesemicolons.quizpath.entity.EnrollmentEntity;
import com.csit321g2.thesemicolons.quizpath.entity.UserEntity;
import com.csit321g2.thesemicolons.quizpath.entity.UserRole;
import com.csit321g2.thesemicolons.quizpath.repository.CourseRepository;
import com.csit321g2.thesemicolons.quizpath.repository.EnrollmentRepository;
import com.csit321g2.thesemicolons.quizpath.repository.UserRepository;

@Service
@Transactional
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public EnrollmentService(
            EnrollmentRepository enrollmentRepository,
            CourseRepository courseRepository,
            UserRepository userRepository
    ) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public EnrollmentResponse createEnrollment(EnrollmentRequest request) {
        CourseEntity course = findCourse(request.courseId());
        UserEntity student = findStudent(request.studentId());

        validateStudentRole(student);
        validateUniqueEnrollment(request.studentId(), request.courseId(), null);

        EnrollmentEntity enrollment = new EnrollmentEntity();
        enrollment.setCourse(course);
        enrollment.setStudent(student);

        return toResponse(enrollmentRepository.saveAndFlush(enrollment));
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getAllEnrollments() {
        return enrollmentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public EnrollmentResponse getEnrollmentById(Long id) {
        return toResponse(findEnrollment(id));
    }

    public EnrollmentResponse updateEnrollment(Long id, EnrollmentRequest request) {
        EnrollmentEntity enrollment = findEnrollment(id);
        CourseEntity course = findCourse(request.courseId());
        UserEntity student = findStudent(request.studentId());

        validateStudentRole(student);
        validateUniqueEnrollment(request.studentId(), request.courseId(), id);

        enrollment.setCourse(course);
        enrollment.setStudent(student);

        return toResponse(enrollmentRepository.saveAndFlush(enrollment));
    }

    public void deleteEnrollment(Long id) {
        EnrollmentEntity enrollment = findEnrollment(id);
        enrollmentRepository.delete(enrollment);
    }

    private EnrollmentEntity findEnrollment(Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Enrollment not found"
                ));
    }

    private CourseEntity findCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Course not found"
                ));
    }

    private UserEntity findStudent(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Student not found"
                ));
    }

    private void validateStudentRole(UserEntity student) {
        if (student.getRole() != UserRole.STUDENT) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only users with the STUDENT role can enroll"
            );
        }
    }

    private void validateUniqueEnrollment(
            Long studentId,
            Long courseId,
            Long currentEnrollmentId
    ) {
        boolean exists = enrollmentRepository
                .existsByStudentUserIdAndCourseCourseId(studentId, courseId);

        if (!exists) {
            return;
        }

        if (currentEnrollmentId != null) {
            EnrollmentEntity current = findEnrollment(currentEnrollmentId);

            boolean sameStudent = current.getStudent().getUserId().equals(studentId);
            boolean sameCourse = current.getCourse().getCourseId().equals(courseId);

            if (sameStudent && sameCourse) {
                return;
            }
        }

        throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Student is already enrolled in this course"
        );
    }

    private EnrollmentResponse toResponse(EnrollmentEntity enrollment) {
        return new EnrollmentResponse(
                enrollment.getEnrollmentId(),
                enrollment.getCourse().getCourseId(),
                enrollment.getCourse().getCourseName(),
                enrollment.getStudent().getUserId(),
                enrollment.getStudent().getFullName(),
                enrollment.getEnrolledAt()
        );
    }
}
