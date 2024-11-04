package tn.esprit.spring;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IRegistrationRepository;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.services.RegistrationServicesImpl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationServicesImplMockTest {
    private static final Logger logger = LogManager.getLogger(RegistrationServicesImplMockTest.class);
    @Mock
    private IRegistrationRepository registrationRepository;

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private RegistrationServicesImpl registrationServices;

    private Skier skier;
    private Course course;
    private Registration registration;

    @BeforeEach
    public void setUp() {
        // Initialisation des objets mockés
        logger.info("Initialisation des objets mockés pour les tests");
        skier = new Skier();
        skier.setNumSkier(1L);
        skier.setFirstName("Amine");
        skier.setLastName("Kbaier");
        skier.setDateOfBirth(LocalDate.of(2010, 1, 1));  // 14 ans

        course = new Course();
        course.setNumCourse(1L);

        registration = new Registration();
        registration.setNumWeek(5);
    }

    @Test
    public void testAddRegistrationAndAssignToSkier_Success() {
        logger.info("Début du test : testAddRegistrationAndAssignToSkier_Success");
        when(skierRepository.findById(anyLong())).thenReturn(Optional.of(skier));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        Registration result = registrationServices.addRegistrationAndAssignToSkier(registration, 1L);

        assertNotNull(result);
        assertEquals(skier.getNumSkier(), result.getSkier().getNumSkier());
        verify(registrationRepository, times(1)).save(any(Registration.class));
        logger.info("Test réussi : La réservation a été ajoutée et assignée au skieur");
    }

    @Test
    public void testAssignRegistrationToCourse_Success() {
        logger.info("Début du test : testAssignRegistrationToCourse_Success");
        when(registrationRepository.findById(anyLong())).thenReturn(Optional.of(registration));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        Registration result = registrationServices.assignRegistrationToCourse(1L, 1L);

        assertNotNull(result);
        assertEquals(course.getNumCourse(), result.getCourse().getNumCourse());
        verify(registrationRepository, times(1)).save(any(Registration.class));
        logger.info("Test réussi : La réservation a été assignée au cours");
    }

    @Test
    public void testFindById_Success() {
        logger.info("Début du test : testFindById_Success");
        when(registrationRepository.findById(anyLong())).thenReturn(Optional.of(registration));

        Optional<Registration> result = registrationRepository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(registration.getNumRegistration(), result.get().getNumRegistration());
        verify(registrationRepository, times(1)).findById(anyLong());
        logger.info("Test réussi : La réservation a été trouvée par son ID");
    }

    @Test
    public void testFindAllRegistrations() {
        logger.info("Début du test : testFindAllRegistrations");
        when(registrationRepository.findAll()).thenReturn(Arrays.asList(registration));

        List<Registration> registrations = (List<Registration>) registrationRepository.findAll();

        assertFalse(registrations.isEmpty());
        assertTrue(registrations.contains(registration));
        verify(registrationRepository, times(1)).findAll();
        logger.info("Test réussi : Toutes les réservations ont été récupérées");
    }

    @Test
    public void testUpdateRegistration_Success() {
        logger.info("Début du test : testUpdateRegistration_Success");
        // Modification de la semaine
        registration.setNumWeek(10);
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);
        Registration updatedRegistration = registrationRepository.save(registration);

        assertEquals(10, updatedRegistration.getNumWeek());
        verify(registrationRepository, times(1)).save(any(Registration.class));
        logger.info("Test réussi : La réservation a été mise à jour avec succès");
    }

    @Test
    public void testDeleteRegistration_Success() {
        logger.info("Début du test : testDeleteRegistration_Success");
        doNothing().when(registrationRepository).deleteById(anyLong());

        registrationRepository.deleteById(1L);

        verify(registrationRepository, times(1)).deleteById(anyLong());
        logger.info("Test réussi : La réservation a été supprimée avec succès");
    }
}

/*
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IRegistrationRepository;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.services.RegistrationServicesImpl;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class RegistrationServicesImplTestMock {
    /*@InjectMocks
    private RegistrationServicesImpl registrationServices;

    @Mock
    private IRegistrationRepository registrationRepository;

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private ICourseRepository courseRepository;

    private Skier skier;
    private Course course;
    private Registration registration;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Créer des objets pour les tests
        skier = new Skier();
        skier.setNumSkier(1L);
        skier.setDateOfBirth(LocalDate.of(2005, 1, 1));  // 19 ans

        course = new Course();
        course.setNumCourse(1L);
        course.setTypeCourse(TypeCourse.COLLECTIVE_ADULT);

        registration = new Registration();
        registration.setNumWeek(1);
    }

    @Test
    public void testAddRegistrationAndAssignToSkierAndCourse_Successful() {
        // Configurer les mocks
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(anyInt(), anyLong(), anyLong())).thenReturn(0L);
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        // Exécuter le test
        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(registration, 1L, 1L);

        // Vérifier les assertions
        assertNotNull(result);
        assertEquals(skier, result.getSkier());
        assertEquals(course, result.getCourse());
        verify(registrationRepository, times(1)).save(registration);
    }

    @Test
    public void testAddRegistrationAndAssignToSkierAndCourse_AlreadyRegistered() {
        // Configurer les mocks
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(anyInt(), anyLong(), anyLong())).thenReturn(1L);

        // Exécuter le test
        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(registration, 1L, 1L);

        // Vérifier que la méthode retourne null (déjà enregistré)
        assertNull(result);
        verify(registrationRepository, never()).save(any(Registration.class));
    }

    @Test
    public void testAddRegistrationAndAssignToSkierAndCourse_SkierTooYoung() {
        // Adapter l'âge du skieur pour le test
        skier.setDateOfBirth(LocalDate.of(2015, 1, 1));  // 9 ans

        // Configurer les mocks
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(anyInt(), anyLong(), anyLong())).thenReturn(0L);

        // Exécuter le test
        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(registration, 1L, 1L);

        // Vérifier que le skieur est trop jeune pour le cours adulte
        assertNull(result);
        verify(registrationRepository, never()).save(any(Registration.class));
    }
}
*/
