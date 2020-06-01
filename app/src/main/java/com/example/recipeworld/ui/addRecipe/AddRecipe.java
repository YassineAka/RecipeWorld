 package com.example.recipeworld.ui.addRecipe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

 public class AddRecipe extends Fragment {

    private AddRecipeViewModel addRecipeViewModel;
    private EditText mAddRecipeName;
    private EditText mAddRecipeIngredients;
    private EditText mAddRecipeSteps;
    private Button mSave;
    private Button mTakePictureCamera;
    private Button mTakePictureFile;
    private ImageView mPicture;
    private Uri picture;
    private Bitmap pictureForImageView;
    private Uri filePath;
    public final int REQUEST_CAMERA = 100;
    public final int REQUEST_FILE = 101;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        addRecipeViewModel =ViewModelProviders.of(this).get(AddRecipeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_addrecipe, container, false);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mAddRecipeName = root.findViewById(R.id.addrecipename);
        mAddRecipeIngredients = root.findViewById(R.id.addrecipeingredients);
        mAddRecipeSteps = root.findViewById(R.id.addrecipesteps);
        mPicture = root.findViewById(R.id.addrecipepicture);
        mTakePictureCamera = root.findViewById(R.id.addrecipe_take_picture_camera);
        mTakePictureCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,REQUEST_CAMERA);
            }
        });
        mTakePictureFile = root.findViewById(R.id.addrecipe_take_picture_file);
        mTakePictureFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent,REQUEST_FILE);
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
            mAddRecipeSteps.setText(savedInstanceState.getString("stepsRecipe"));
            this.mPicture.setImageBitmap(this.pictureForImageView);

        }
        return root;
    }
    private void addRecipe(){
        String name =mAddRecipeName.getText().toString().intern();
        String ingredients = mAddRecipeIngredients.getText().toString().intern();
        String steps = mAddRecipeSteps.getText().toString().intern();
        Map<String, Object> recipe = new HashMap<>();
        recipe.put("name", name);
        recipe.put("ingredients",ingredients);
        recipe.put("steps",steps);


        //save in database the recipe
        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(ingredients) && !TextUtils.isEmpty(steps)) {
            db.collection(mAuth.getCurrentUser().getUid())
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

            StorageReference ref = storageReference.child(mAuth.getCurrentUser().getUid()+"/"+ name);
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
        if(resultCode == RESULT_OK) {
            Log.i("Pick-picture","Ok je suis rentré");
            if(requestCode == REQUEST_FILE){
                    this.picture = data.getData();
                    Picasso.get().load(this.picture).fit().centerCrop().into(this.mPicture);
            }
            this.picture = data.getData();
            this.pictureForImageView = ((Bitmap) data.getExtras().get("data"));
            this.mPicture.setImageBitmap(this.pictureForImageView);
        }

    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("nameRecipe",mAddRecipeName.getText().toString());
        outState.putString("ingredientsRecipe",mAddRecipeIngredients.getText().toString());
        outState.putString("stepsRecipe",mAddRecipeSteps.getText().toString());
        outState.putParcelable("bitmap", this.pictureForImageView);
        outState.putParcelable("uri", this.picture);
    }

}
