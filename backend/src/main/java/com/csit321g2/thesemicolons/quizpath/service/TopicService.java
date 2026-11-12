
package com.csit321g2.thesemicolons.quizpath.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.csit321g2.thesemicolons.quizpath.dto.TopicRequest;
import com.csit321g2.thesemicolons.quizpath.dto.TopicResponse;
import com.csit321g2.thesemicolons.quizpath.entity.CourseEntity;
import com.csit321g2.thesemicolons.quizpath.entity.TopicEntity;
import com.csit321g2.thesemicolons.quizpath.repository.CourseRepository;
import com.csit321g2.thesemicolons.quizpath.repository.TopicRepository;

@Service
public class TopicService {

    private final TopicRepository topicRepository;
    private final CourseRepository courseRepository;

    public TopicService(TopicRepository topicRepository, CourseRepository courseRepository) {
        this.topicRepository = topicRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional
    public TopicResponse createTopic(TopicRequest request) {
        CourseEntity course = findCourse(request.courseId());

        TopicEntity topic = new TopicEntity(
                course,
                request.topicName(),
                request.description()
        );

        return toResponse(topicRepository.save(topic));
    }

    @Transactional(readOnly = true)
    public List<TopicResponse> getAllTopics() {
        return topicRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TopicResponse getTopicById(Long id) {
        return toResponse(findTopic(id));
    }

    @Transactional
    public TopicResponse updateTopic(Long id, TopicRequest request) {
        TopicEntity topic = findTopic(id);
        CourseEntity course = findCourse(request.courseId());

        topic.setCourse(course);
        topic.setTopicName(request.topicName());
        topic.setDescription(request.description());

        return toResponse(topicRepository.save(topic));
    }

    @Transactional
    public void deleteTopic(Long id) {
        TopicEntity topic = findTopic(id);
        topicRepository.delete(topic);
    }

    private TopicEntity findTopic(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Topic not found"
                ));
    }

    private CourseEntity findCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Course not found"
                ));
    }

    private TopicResponse toResponse(TopicEntity topic) {
        return new TopicResponse(
                topic.getTopicId(),
                topic.getCourse().getCourseId(),
                topic.getCourse().getCourseName(),
                topic.getTopicName(),
                topic.getDescription()
        );
    }
}
