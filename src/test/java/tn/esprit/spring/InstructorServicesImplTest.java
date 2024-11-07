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
        instructor1.setId(1L);
        instructors.add(instructor1);

        Instructor instructor2 = new Instructor();
        instructor2.setId(2L);
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
        instructor.setId(1L);
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        // Act
        Instructor result = instructorService.addInstructor(instructor);

        logger.info("Added instructor: {}", result);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(instructorRepository, times(1)).save(instructor);
        logger.info("Successfully verified addition of instructor");
    }

    @Test
    void testUpdateInstructor() {
        // Arrange
        Instructor instructor = new Instructor();
        instructor.setId(1L);
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        // Act
        Instructor result = instructorService.updateInstructor(instructor);

        logger.info("Updated instructor: {}", result);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(instructorRepository, times(1)).save(instructor);
        logger.info("Successfully verified update of instructor");
    }

    @Test
    void testRetrieveInstructorFound() {
        // Arrange
        Long instructorId = 1L;
        Instructor instructor = new Instructor();
        instructor.setId(instructorId);
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));

        // Act
        Instructor result = instructorService.retrieveInstructor(instructorId);

        logger.info("Retrieved instructor: {}", result);

        // Assert
        assertNotNull(result);
        assertEquals(instructorId, result.getId());
        verify(instructorRepository, times(1)).findById(instructorId);
        logger.info("Successfully verified retrieval of instructor found");
    }

    @Test
    void testRetrieveInstructorNotFound() {
        // Arrange
        Long instructorId = 1L;
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.empty());

        // Act
        Instructor result = instructorService.retrieveInstructor(instructorId);

        logger.info("Attempted to retrieve instructor with ID: {} - Result: {}", instructorId, result);

        // Assert
        assertNull(result);
        verify(instructorRepository, times(1)).findById(instructorId);
        logger.info("Successfully verified retrieval of instructor not found");
    }}
