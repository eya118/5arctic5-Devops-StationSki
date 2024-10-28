package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.apache.log4j.Logger;
class SkierServicesImplTest {
    @InjectMocks
    private SkierServicesImpl skierServices;

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private IPisteRepository pisteRepository;

    @Mock
    private ICourseRepository courseRepository;

    @Mock
    private IRegistrationRepository registrationRepository;

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    private static final Logger logger = Logger.getLogger(SkierServicesImplTest.class);

    // This method ensures the mocks are initialized before each test
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // Initializes the @Mock and @InjectMocks annotations
    }
    // Test for retrieveAllSkiers()

/*    @Test
    void  testRetrieveAllSkiers() {

        List<Skier> mockSkiers = Arrays.asList(new Skier(), new Skier());
        when(skierRepository.findAll()).thenReturn(mockSkiers);
        // When
        List<Skier> result = skierServices.retrieveAllSkiers();

        // Then
        assertEquals(20, result.size());
       // verify(skierRepository, times(1)).findAll();
    }*/

/*
    @Test
    // Test for addSkier()
    public void testAddSkier() {
        // Given
        Skier skier = new Skier();
        Subscription subscription = new Subscription();
        subscription.setStartDate(LocalDate.now());
        subscription.setTypeSub(TypeSubscription.ANNUAL);
        skier.setSubscription(subscription);

        when(skierRepository.save(any(Skier.class))).thenReturn(skier);

        // When
        Skier savedSkier = skierServices.addSkier(skier);

        // Then
        assertNotNull(savedSkier);
        assertEquals(subscription.getEndDate(), LocalDate.now().plusYears(1));
        verify(skierRepository, times(1)).save(skier);
    }
*/


    // Test for assignSkierToSubscription()
    @Test
    public void testAssignSkierToSubscription() {
        logger.info("Starting testAssignSkierToSubscription");

        // Given
        Skier skier = new Skier();
        Subscription subscription = new Subscription();

        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(subscriptionRepository.findById(2L)).thenReturn(Optional.of(subscription));
        when(skierRepository.save(any(Skier.class))).thenReturn(skier);

        // When
        Skier result = skierServices.assignSkierToSubscription(1L, 2L);

        // Then
        assertEquals(subscription, skier.getSubscription());
        verify(skierRepository, times(1)).findById(1L);
        verify(subscriptionRepository, times(1)).findById(2L);
        verify(skierRepository, times(1)).save(skier);
        logger.info("testAssignSkierToSubscription completed successfully");

    }

    // Test for assignSkierToPiste()
    @Test
    public void testAssignSkierToPiste() {
        logger.info("Starting testAssignSkierToPiste");

        // Given
        Skier skier = new Skier();
        Piste piste = new Piste();
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(pisteRepository.findById(2L)).thenReturn(Optional.of(piste));
        when(skierRepository.save(any(Skier.class))).thenReturn(skier);

        // When
        Skier result = skierServices.assignSkierToPiste(1L, 2L);

        // Then
        assertNotNull(skier.getPistes());
        assertTrue(skier.getPistes().contains(piste));
        verify(skierRepository, times(1)).findById(1L);
        verify(pisteRepository, times(1)).findById(2L);
        verify(skierRepository, times(1)).save(skier);
        logger.info("testAssignSkierToPiste completed successfully");

    }

    // Test for addSkierAndAssignToCourse()
    @Test
    public void testAddSkierAndAssignToCourse() {
        logger.info("Starting testAddSkierAndAssignToCourse");

        // Given
        Skier skier = new Skier();
        Registration registration = new Registration();
        skier.setRegistrations(new HashSet<>(Arrays.asList(registration)));
        Course course = new Course();

        when(courseRepository.getById(1L)).thenReturn(course);
        when(skierRepository.save(any(Skier.class))).thenReturn(skier);
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        // When
        Skier result = skierServices.addSkierAndAssignToCourse(skier, 1L);

        // Then
        assertEquals(1, skier.getRegistrations().size());
        verify(registrationRepository, times(1)).save(any(Registration.class));
        logger.info("testAddSkierAndAssignToCourse completed successfully");

    }

    // Test for retrieveSkiersBySubscriptionType()
    @Test
    public void testRetrieveSkiersBySubscriptionType() {
        logger.info("Starting testRetrieveSkiersBySubscriptionType");

        // Given
        List<Skier> skiers = Arrays.asList(new Skier(), new Skier());
        when(skierRepository.findBySubscription_TypeSub(TypeSubscription.ANNUAL)).thenReturn(skiers);

        // When
        List<Skier> result = skierServices.retrieveSkiersBySubscriptionType(TypeSubscription.ANNUAL);

        // Then
        assertEquals(2, result.size());
        verify(skierRepository, times(1)).findBySubscription_TypeSub(TypeSubscription.ANNUAL);
        logger.info(" testRetrieveSkiersBySubscriptionType completed successfully");

    }


    // junit tests //




  /*  @Test
    void addSkiertest() {
        logger.info("Starting addSkiertest");

        // Create a skier and assign the previously saved subscription
        Skier skier = new Skier();
        skier.setNumSkier(9L);
        skier.setFirstName("Test Skier");
      //  System.out.println("tttttttttttt"+skier);
        // Add the skier
        Skier savedSkier = skierRepository.save(skier); // Persist the skier

        // Ensure the skier was added successfully
        Skier retrievedSkier = skierServices.retrieveSkier(savedSkier.getNumSkier());
        assertNotNull(retrievedSkier, "The skier should be successfully added to the database.");
        assertEquals("Test Skier", retrievedSkier.getFirstName(), "The first name should match.");
        logger.info(" addSkiertest completed successfully");

    }*/

}