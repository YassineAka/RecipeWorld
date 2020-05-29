 package com.example.recipeworld.ui.addRecipe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.recipeworld.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

 public class AddRecipe extends Fragment {

    private AddRecipeViewModel addRecipeViewModel;
    private EditText mAddRecipeName;
    private EditText mAddRecipeIngredients;
    private Button mSave;
    private Button mTakePicture;
    private ImageView mPicture;
    private Uri picture;
    private Bitmap pictureForImageView;
    private Uri filePath;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        addRecipeViewModel =ViewModelProviders.of(this).get(AddRecipeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_addrecipe, container, false);

        mAddRecipeName = root.findViewById(R.id.addrecipename);
        mAddRecipeIngredients = root.findViewById(R.id.addrecipeingredients);
        mPicture = root.findViewById(R.id.addrecipepicture);
        mTakePicture = root.findViewById(R.id.addrecipetakepicture);
        mTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }
        });
        mSave = root.findViewById(R.id.addrecipesave);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecipe();
            }
        });
        if (savedInstanceState != null){
            this.pictureForImageView = savedInstanceState.getParcelable("bitmap");
            this.picture = savedInstanceState.getParcelable("uri");
            mAddRecipeName.setText(savedInstanceState.getString("nameRecipe"));
            mAddRecipeIngredients.setText(savedInstanceState.getString("ingredientsRecipe"));
            this.mPicture.setImageBitmap(this.pictureForImageView);

        }
        return root;
    }
    private void addRecipe(){
        String name =mAddRecipeName.getText().toString().intern();
        String ingredients = mAddRecipeIngredients.getText().toString().intern();
        Map<String, Object> recipe = new HashMap<>();
        recipe.put("name", name);
        recipe.put("ingredients",ingredients);


        //save in database the recipe
        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(ingredients)) {
            db.collection("recipes")
                    .add(recipe)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.i("YAAAAAAAAASSINE", "Recipe added successfully");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("YAAAAAAAAASSINE", "Error adding recipe", e);
                        }
                    });
        }

        //Upload image
        if(this.picture != null){
            final ProgressDialog progressDialog = new ProgressDialog(this.getContext());
            progressDialog.setTitle("Uploading ...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ name);
            ref.putFile(this.picture)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0* taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploading ..." +"     "+ (int)progress+"%");
                }
            });

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.picture = data.getData();
        Log.i("YASSIIIIIINE",this.picture +"-------------------------"+this.picture.toString());
        this.pictureForImageView = ((Bitmap)data.getExtras().get("data"));
        this.mPicture.setImageBitmap(this.pictureForImageView);

        //Save the picture in the database

    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("nameRecipe",mAddRecipeName.getText().toString());
        outState.putString("ingredientsRecipe",mAddRecipeIngredients.getText().toString());
        outState.putParcelable("bitmap", this.pictureForImageView);
        outState.putParcelable("uri", this.picture);
    }

}
