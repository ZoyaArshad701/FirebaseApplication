package com.example.firebaseapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private static final String COLLECTION_NAME = "PakistanCities";
    private FirebaseFirestore objectFirebaseFirestore;
    private Dialog objectDialog;

    private EditText documentIDET, cityDetailsET, noOfUniET;
    private TextView downloadedDataTV;

    private DocumentReference objectDocumentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        objectFirebaseFirestore = FirebaseFirestore.getInstance();
        objectDialog = new Dialog(this);

        objectDialog.setContentView(R.layout.wait_dialog);
        objectDialog.setCancelable(false);

        documentIDET = findViewById(R.id.documentIDET);
        cityDetailsET = findViewById(R.id.cityDetailET);

        noOfUniET = findViewById(R.id.noOfUniET);
        downloadedDataTV = findViewById(R.id.downloadedDataTV);

    }

    public void addValuesToFirebaseFireStore(View v) {
        try {

            if (!documentIDET.getText().toString().isEmpty() && !cityDetailsET.getText().toString().isEmpty()
                    && !noOfUniET.getText().toString().isEmpty()) {
                objectDialog.show();
                final Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("citydetails", cityDetailsET.getText().toString());
                objectMap.put("noofuniversities", noOfUniET.getText().toString());

                objectFirebaseFirestore.collection(COLLECTION_NAME)
                        .document(documentIDET.getText().toString())
                        .set(objectMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this, "City Details added successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Fails to add City Details", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            objectDialog.dismiss();
            Toast.makeText(this, "addValuesToFirebaseFireStore:" +
                    e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getValuesFromFirebaseFirestore(View v) {
        try {
            if (documentIDET.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter valid document id", Toast.LENGTH_SHORT).show();
            } else {
                objectDialog.show();
                objectDocumentReference = objectFirebaseFirestore.collection(COLLECTION_NAME)
                        .document(documentIDET.getText().toString());

                objectDocumentReference.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                objectDialog.dismiss();
                                if (documentSnapshot.exists()) {
                                    downloadedDataTV.setText("");
                                    documentIDET.setText("");

                                    String documentID = documentSnapshot.getId();
                                    documentIDET.requestFocus();
                                    String cityDetails = documentSnapshot.getString("citydetails");
                                    String noOfUnis = documentSnapshot.getString("noofuniversities");

                                    downloadedDataTV.setText("City Name:" + documentID + "\n" + "City Details:" + cityDetails + "\n" +
                                            "No of Unis:" + noOfUnis);
                                } else {
                                    Toast.makeText(MainActivity.this, "No Document Retrieved", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        objectDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Fails to retrieve data:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception e) {
            objectDialog.dismiss();
            Toast.makeText(this, "getValuesFromFirebaseFirstore:" +
                    e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void updateDocumentFieldValue(View view) {
        try {
            if (documentIDET.getText().toString().isEmpty() && cityDetailsET.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                objectDialog.show();
                objectDocumentReference = objectFirebaseFirestore.collection(COLLECTION_NAME)
                        .document(documentIDET.getText().toString());

                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("citydetails", cityDetailsET.getText().toString());
                objectDocumentReference.update(objectMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Fails to update data:"
                                        + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }

        } catch (Exception e) {
            objectDialog.dismiss();
            Toast.makeText(this, "updateDocumentFieldValue:" +
                    e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteFieldFromCollectionDocument(View view) {
        try {
            if (documentIDET.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter the document id", Toast.LENGTH_SHORT).show();
            } else {
                objectDialog.show();
                objectDocumentReference = objectFirebaseFirestore.collection(COLLECTION_NAME)
                        .document(documentIDET.getText().toString());

                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("citydetails", FieldValue.delete());
                objectDocumentReference.update(objectMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Data Field deleted Successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Fails to delete filed data:"
                                        + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }

        } catch (Exception e) {
            objectDialog.dismiss();
            Toast.makeText(this, "deleteFieldFromCollectionDocument:" +
                    e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void DeleteCollection(View v) {
        try {
            if (documentIDET.getText().toString().isEmpty()) {
                objectDocumentReference = objectFirebaseFirestore.collection(COLLECTION_NAME)
                        .document(documentIDET.getText().toString());

                objectDocumentReference.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Id Deleted Successfully", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Deletion Failed", Toast.LENGTH_LONG).show();
                            }
                        });
            } else {
                Toast.makeText(this, "Fails to delete the Document", Toast.LENGTH_LONG);
            }

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
