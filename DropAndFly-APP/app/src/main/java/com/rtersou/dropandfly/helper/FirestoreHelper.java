package com.rtersou.dropandfly.helper;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class FirestoreHelper {


    FirebaseAuth mUser;


    public static void disconnect() {
        FirebaseAuth.getInstance().signOut();
    }
    /**
     * Ajout d'un document firestore
     * @param db
     * @param collection
     * @param data
     */
    public static void addData(FirebaseFirestore db, String collection, Object data ) {
        db = FirebaseFirestore.getInstance();
        db.collection(collection)
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(Helper.DB_EVENT_ADD, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(Helper.DB_EVENT_ADD, "Error adding document", e);
                    }
                });
    }

    /**
     * Get all documents
     * @param db
     * @param collection
     */
    public static void getAll(FirebaseFirestore db, String collection) {
        db.collection(collection)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(Helper.DB_EVENT_GET, document.getId() + " => " + document.getData());

                                // @TODO : Recuperer les documents (Comment ??)
                            }
                        } else {
                            Log.w(Helper.DB_EVENT_GET, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    /**
     * get a document by his id
     * @param db
     * @param collection
     * @param document
     */
    public static void get(FirebaseFirestore db, String collection, String document) {
        DocumentReference docRef = db.collection(collection).document(document);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(Helper.DB_EVENT_GET, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(Helper.DB_EVENT_GET, "No such document");
                    }
                } else {
                    Log.d(Helper.DB_EVENT_GET, "get failed with ", task.getException());
                }
            }
        });

    }

    /**
     * Suppression d'un document firestore
     * @param db
     * @param collection
     * @param document
     */
    public static void removeData(FirebaseFirestore db, String collection, String document) {
        db.collection(collection).document(document)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(Helper.DB_EVENT_DELETE, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(Helper.DB_EVENT_DELETE, "Error deleting document", e);
                    }
                });
    }
}
