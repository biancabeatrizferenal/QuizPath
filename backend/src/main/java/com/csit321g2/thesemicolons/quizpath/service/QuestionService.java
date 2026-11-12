
package com.csit321g2.thesemicolons.quizpath.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.csit321g2.thesemicolons.quizpath.dto.QuestionRequest;
import com.csit321g2.thesemicolons.quizpath.dto.QuestionResponse;
import com.csit321g2.thesemicolons.quizpath.entity.CourseEntity;
import com.csit321g2.thesemicolons.quizpath.entity.QuestionEntity;
import com.csit321g2.thesemicolons.quizpath.entity.TopicEntity;
import com.csit321g2.thesemicolons.quizpath.repository.CourseRepository;
import com.csit321g2.thesemicolons.quizpath.repository.QuestionRepository;
import com.csit321g2.thesemicolons.quizpath.repository.TopicRepository;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final CourseRepository courseRepository;
    private final TopicRepository topicRepository;

    public QuestionService(
            QuestionRepository questionRepository,
            CourseRepository courseRepository,
            TopicRepository topicRepository
    ) {
        this.questionRepository = questionRepository;
        this.courseRepository = courseRepository;
        this.topicRepository = topicRepository;
    }

    @Transactional
    public QuestionResponse createQuestion(QuestionRequest request) {
        CourseEntity course = findCourse(request.courseId());
        TopicEntity topic = findTopic(request.topicId());

        validateTopicCourse(topic, course);

        QuestionEntity question = new QuestionEntity(
                course,
                topic,
                request.questionText(),
                request.questionType(),
                request.points()
        );

        return toResponse(questionRepository.save(question));
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> getAllQuestions() {
        return questionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public QuestionResponse getQuestionById(Long id) {
        return toResponse(findQuestion(id));
    }

    @Transactional
    public QuestionResponse updateQuestion(Long id, QuestionRequest request) {
        QuestionEntity question = findQuestion(id);
        CourseEntity course = findCourse(request.courseId());
        TopicEntity topic = findTopic(request.topicId());

        validateTopicCourse(topic, course);

        question.setCourse(course);
        question.setTopic(topic);
        question.setQuestionText(request.questionText());
        question.setQuestionType(request.questionType());
        question.setPoints(request.points());

        return toResponse(questionRepository.save(question));
    }

    @Transactional
    public void deleteQuestion(Long id) {
        QuestionEntity question = findQuestion(id);
        questionRepository.delete(question);
    }

    private QuestionEntity findQuestion(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Question not found"
                ));
    }

    private CourseEntity findCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Course not found"
                ));
    }

    private TopicEntity findTopic(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Topic not found"
                ));
    }

    private void validateTopicCourse(TopicEntity topic, CourseEntity course) {
        if (!topic.getCourse().getCourseId().equals(course.getCourseId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Topic does not belong to the selected course"
            );
        }
    }

    private QuestionResponse toResponse(QuestionEntity question) {
        return new QuestionResponse(
                question.getQuestionId(),
                question.getCourse().getCourseId(),
                question.getCourse().getCourseName(),
                question.getTopic().getTopicId(),
                question.getTopic().getTopicName(),
                question.getQuestionText(),
                question.getQuestionType(),
                question.getPoints()
        );
    }
}
