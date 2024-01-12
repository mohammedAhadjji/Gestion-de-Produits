package org.example.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.entities.Produit;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
public class ProduitService {
    private Map<Long, Produit> produits = new HashMap<>();
    private static final String FILE_PATH = "produits.json";
    private final Gson gson = new Gson();
    public ProduitService() {
        chargerProduits();
    }
    private boolean produitExiste(Long id) {
        return produits.containsKey(id);
    }

    private boolean produitExisteParNom(String nom) {
        return produits.values().stream().anyMatch(p -> p.getNom().equals(nom));
    }

    private void validerProduit(Produit produit) {
        if (produit.getPrix() <= 0 || produit.getQuantite() < 0) {
            throw new IllegalArgumentException("Prix et quantité doivent être positifs.");
        }
    }
    private void sauvegarderProduits() {
        try {
            String json = gson.toJson(produits);
            Files.write(Paths.get(FILE_PATH), json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void chargerProduits() {
        try {
            if (Files.exists(Paths.get(FILE_PATH))) {
                String json = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
                Type type = new TypeToken<Map<Long, Produit>>(){}.getType();
                produits = gson.fromJson(json, type);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ajouterProduit(Produit produit) {
        chargerProduits();
        if (produitExiste(produit.getId()) || produitExisteParNom(produit.getNom())) {
            throw new RuntimeException("Un produit avec cet ID ou nom existe déjà.");
        }
        validerProduit(produit);
        produits.put(produit.getId(), produit);
        sauvegarderProduits();
    }

    public void supprimerProduit(Long id) {
        chargerProduits();
        if (!produits.containsKey(id)) {
            throw new RuntimeException("Produit non trouvé");
        }
        produits.remove(id);
        sauvegarderProduits();
    }
    public Map<Long, Produit> getProduits() {
        chargerProduits();
        return produits;
    }
    public Produit obtenirProduit(long id) {
        return produits.get(id);
    }

}
