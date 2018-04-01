package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @BindView(R.id.also_known_tv)
    TextView tvAlsoKnownAs;

    @BindView(R.id.origin_tv)
    TextView tvPlaceOfOrigin;

    @BindView(R.id.description_tv)
    TextView tvDescription;

    @BindView(R.id.ingredients_tv)
    TextView tvIngredients;

    @BindView(R.id.image_iv)
    ImageView ivIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);

        if (TextUtils.isEmpty(sandwich.getImage())) {
            Picasso.with(this)
                    .cancelRequest(ivIngredients);

            ivIngredients.setImageResource(R.drawable.image_not_available);
        } else {
            Picasso.with(this)
                    .load(sandwich.getImage())
                    .error(R.drawable.image_error)
                    .placeholder(R.drawable.image_placeholder)
                    .into(ivIngredients);
        }

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        if (sandwich.getAlsoKnownAs().isEmpty()) {
            tvAlsoKnownAs.setText(R.string.no_data_error_message);
        } else {
            tvAlsoKnownAs.setText(TextUtils.join("\n", sandwich.getAlsoKnownAs()));
        }

        if (TextUtils.isEmpty(sandwich.getPlaceOfOrigin())) {
            tvPlaceOfOrigin.setText(R.string.no_data_error_message);
        } else {
            tvPlaceOfOrigin.setText(sandwich.getPlaceOfOrigin());
        }

        if (TextUtils.isEmpty(sandwich.getDescription())) {
            tvDescription.setText(R.string.no_data_error_message);
        } else {
            tvDescription.setText(sandwich.getDescription());
        }

        if (sandwich.getIngredients().isEmpty()) {
            tvIngredients.setText(R.string.no_data_error_message);
        } else {
            tvIngredients.setText(TextUtils.join("\n", sandwich.getIngredients()));
        }
    }

}
