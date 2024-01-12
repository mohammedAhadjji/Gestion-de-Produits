package org.example.services;

import org.example.entities.Produit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.Map;

import static org.junit.Assert.*;

public class ProduitServiceTest {
    ProduitService service;
    private static final String FILE_PATH = "produits.json";
    @Before
    public void setUp() throws Exception {
        service = new ProduitService();
    }
    @After
    public void tearDown() throws Exception {
        try {
            Files.deleteIfExists(Paths.get(FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testAjouterProduitValid() {
        Produit produit = new Produit(1L, "Produit1", 20.0, 5);
        service.ajouterProduit(produit);
        assertEquals(produit, service.obtenirProduit(1L));
    }

    @Test
    public void testAjouterProduitExistant() {
        service.ajouterProduit(new Produit(2L, "Produit2", 15.0, 10));
        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.ajouterProduit(new Produit(2L, "Produit2", 20.0, 5));
        });
        assertTrue(exception.getMessage().contains("existe déjà"));
    }

    @Test
    public void testSupprimerProduitExistant() {
        Produit produit = new Produit(9L, "Produit9", 50.0, 2);
        service.ajouterProduit(produit);
        service.supprimerProduit(9L);
        assertNull(service.obtenirProduit(9));
    }

    @Test
    public void testSupprimerProduitNonExistant() {
        try {
            service.supprimerProduit(999L);
            fail("Une RuntimeException aurait dû être levée");
        } catch (RuntimeException e) {
            assertEquals("Produit non trouvé", e.getMessage());
        }
    }

    @Test
    public void testGetProduitsVide() {
        assertTrue(service.getProduits().isEmpty());
    }

    @Test
    public void testGetProduitsAvecDonnees() {
        service.ajouterProduit(new Produit(1L, "Produit1", 20.0, 5));
        service.ajouterProduit(new Produit(2L, "Produit2", 30.0, 3));
        Map<Long, Produit> produits = service.getProduits();

        assertEquals(2, produits.size());
        assertNotNull(produits.get(1L));
        assertNotNull(produits.get(2L));
    }

    @Test
    public void testGetProduitsApresSuppression() {
        Produit produit = new Produit(3L, "Produit3", 40.0, 2);
        service.ajouterProduit(produit);
        service.supprimerProduit(3L);

        assertFalse(service.getProduits().containsKey(3L));
    }



}
