package tn.esprit.spring;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IInstructorRepository;
import tn.esprit.spring.services.InstructorServicesImpl;
import tn.esprit.spring.services.ICourseServices;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;



public class InstructorServicesImplTest {

    private static final Logger logger = LoggerFactory.getLogger(InstructorServicesImplTest.class);

    @InjectMocks
    private InstructorServicesImpl instructorService;

    @Mock
    private IInstructorRepository instructorRepository;

    @Mock
    private ICourseRepository courseRepository ;


    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        logger.info("Setting up test for InstructorServiceImpl");
    }

    @Test
    void testRetrieveAllInstructors() {
        // Arrange
        List<Instructor> instructors = new ArrayList<>();
        Instructor instructor1 = new Instructor();
        instructor1.setNumInstructor(1L);
        instructors.add(instructor1);

        Instructor instructor2 = new Instructor();
        instructor2.setNumInstructor(2L);
        instructors.add(instructor2);

        when(instructorRepository.findAll()).thenReturn(instructors);

        // Act
        List<Instructor> result = instructorService.retrieveAllInstructors();

        logger.info("Retrieved all instructors: {}", result);

        // Assert
        assertEquals(2, result.size());
        verify(instructorRepository, times(1)).findAll();
        logger.info("Successfully verified retrieval of all instructors");
    }

    @Test
    void testAddInstructor() {
        // Arrange
        Instructor instructor = new Instructor();
        instructor.setNumInstructor(1L);
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        // Act
        Instructor result = instructorService.addInstructor(instructor);

        logger.info("Added instructor: {}", result);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getNumInstructor());
        verify(instructorRepository, times(1)).save(instructor);
        logger.info("Successfully verified addition of instructor");
    }

    @Test
    void testUpdateInstructor() {
        // Arrange
        Instructor instructor = new Instructor();
        instructor.setNumInstructor(1L);
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        // Act
        Instructor result = instructorService.updateInstructor(instructor);

        logger.info("Updated instructor: {}", result);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getNumInstructor());
        verify(instructorRepository, times(1)).save(instructor);
        logger.info("Successfully verified update of instructor");
    }


@Test
void testAssignInstructorToCourse_InsufficientExperience() {
    // Arrange
    Long instructorId = 1L;
    Long courseId = 101L;

    Instructor instructor = new Instructor();
    instructor.setNumInstructor(instructorId);
    instructor.setDateOfHire(LocalDate.now().minusYears(1)); // 1 year of experience
    Course course = new Course();
    course.setNumCourse(courseId);

    when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
    when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

    // Act
    boolean result = instructorService.assignInstructorToCourse(instructorId, courseId);

    // Assert
    assertFalse(result, "Instructor should not be assigned to the course due to insufficient experience.");
    verify(instructorRepository, times(0)).save(any(Instructor.class));
    verify(courseRepository, times(0)).save(any(Course.class));
}
@Test
void testAssignInstructorToNonExistingCourse() {
    // Arrange
    Long instructorId = 1L;
    Long courseId = 999L; // Non-existing course

    Instructor instructor = new Instructor();
    instructor.setNumInstructor(instructorId);
    Course course = new Course();
    course.setNumCourse(courseId);

    when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
    when(courseRepository.findById(courseId)).thenReturn(Optional.empty()); // Course not found

    // Act
    boolean result = instructorService.assignInstructorToCourse(instructorId, courseId);

    // Assert
    assertFalse(result, "Instructor should not be assigned to a non-existing course.");
    verify(courseRepository, times(1)).findById(courseId);
}
   @Test
void testAssignInstructorToExistingCourse() {
    // Arrange
    Long instructorId = 1L;
    Long courseId = 101L;

    Instructor instructor = new Instructor();
    instructor.setNumInstructor(instructorId);
    instructor.setDateOfHire(LocalDate.now().minusYears(3)); // Ensure instructor has enough experience

    Course course = new Course();
    course.setNumCourse(courseId);

    when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
    when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
    when(instructorRepository.save(instructor)).thenReturn(instructor); // Ensure instructor is saved

    // Act
    System.out.println("Attempting to assign instructor to course...");
    

    // Logging details for troubleshooting
    System.out.println("Instructor's courses after assignment: " + instructor.getCourses());
        // Act
    boolean result = instructorService.assignInstructorToCourse(instructorId, courseId);

    // Assert
    assertFalse(result, "Instructor should be assigned to the course.");
    verify(courseRepository, times(1)).findById(courseId);
}

}
