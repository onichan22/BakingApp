package com.example.fioni.bakingapp.utilities;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by fioni on 9/2/2017.
 */

public final class BakingJSonUtil {

    public static ArrayList<Recipe> getRecipeFromJson(Context context, String bakingStr)
            throws JSONException{

        final String RECIPE_ID = "id";
        final String RECIPE_NAME = "name";
        final String RECIPE_SERVINGS = "servings";

        JSONArray recipesArray = new JSONArray(bakingStr);
        ArrayList<Recipe> parsedRecipeList = new ArrayList<Recipe>();

        for (int i = 0; i < recipesArray.length(); i++) {

            JSONObject aRecipe = recipesArray.getJSONObject(i);
            parsedRecipeList.add(new Recipe(aRecipe.getString(RECIPE_ID),
                    aRecipe.getString(RECIPE_NAME), aRecipe.getString(RECIPE_SERVINGS)));

        }
        return parsedRecipeList;
    }

    public static ArrayList<Ingredients> getIngredientsFromJson(Context context, String bakingStr, int recipe)
            throws JSONException{

        final String RECIPE_ID = "id";
        final String ING_QUANTITY = "quantity";
        final String ING_MSR = "measure";
        final String ING_NAME = "ingredient";
        final String ING_TITLE = "ingredients";

        JSONArray recipesArray = new JSONArray(bakingStr);
        JSONArray ingredientsArray = recipesArray.getJSONObject(recipe).getJSONArray(ING_TITLE);
        ArrayList<Ingredients> parsedIngredientList = new ArrayList<Ingredients>();

        for (int i = 0; i < ingredientsArray.length(); i++) {

            JSONObject aIngredient = ingredientsArray.getJSONObject(i);
            parsedIngredientList.add(new Ingredients(String.valueOf(recipe), aIngredient.getString(ING_QUANTITY),
                    aIngredient.getString(ING_MSR), aIngredient.getString(ING_NAME)));

        }
        return parsedIngredientList;
    }

    public static ArrayList<Step> getStepsFromJson(Context context, String bakingStr, int position)
            throws JSONException{

        final String STEP_OBJ = "steps";
        final String STEP_ID = "id";
        final String STEP_SDESC = "shortDescription";
        final String STEP_DESC = "description";
        final String STEP_VIDEO = "videoURL";
        final String STEP_THUMB = "thumbnailURL";

        JSONArray recipesArray = new JSONArray(bakingStr);
        JSONArray stepsArray = recipesArray.getJSONObject(position).getJSONArray(STEP_OBJ);
        ArrayList<Step> parsedStepList = new ArrayList<Step>();

        for (int i = 0; i < stepsArray.length(); i++) {

            JSONObject aStep = stepsArray.getJSONObject(i);
            parsedStepList.add(new Step(
                    Integer.toString(position),
                    aStep.getString(STEP_ID),
                    aStep.getString(STEP_SDESC),
                    aStep.getString(STEP_DESC),
                    aStep.getString(STEP_VIDEO),
                    aStep.getString(STEP_THUMB)));

        }
        return parsedStepList;
    }

    public static Step getAStepFromJson(Context context, String bakingStr, int position, int i)
            throws JSONException{
        final String STEP_OBJ = "steps";
        final String STEP_ID = "id";
        final String STEP_SDESC = "shortDescription";
        final String STEP_DESC = "description";
        final String STEP_VIDEO = "videoURL";
        final String STEP_THUMB = "thumbnailURL";

        JSONArray recipesArray = new JSONArray(bakingStr);
        JSONArray stepsArray = recipesArray.getJSONObject(position).getJSONArray(STEP_OBJ);
        JSONObject aStep = stepsArray.getJSONObject(i);

        Step newStep = new Step(
                Integer.toString(position),
                aStep.getString(STEP_ID),
                aStep.getString(STEP_SDESC),
                aStep.getString(STEP_DESC),
                aStep.getString(STEP_VIDEO),
                aStep.getString(STEP_THUMB));

        return newStep;
    }

    public static ArrayList<Ingredients> getAllIngredientsFromJson(Context context, String bakingStr)
            throws JSONException {

        final String RECIPE_ID = "id";
        final String ING_QUANTITY = "quantity";
        final String ING_MSR = "measure";
        final String ING_NAME = "ingredient";
        final String ING_TITLE = "ingredients";

        JSONArray recipesArray = new JSONArray(bakingStr);
        int recipesArraySize = recipesArray.length();
        ArrayList<Ingredients> allIngredients = new ArrayList<Ingredients>();

        for (int i = 0; i < recipesArraySize; i++) {
            JSONArray ingredientsArray = recipesArray.getJSONObject(i).getJSONArray(ING_TITLE);
            for (int n = 0; n < ingredientsArray.length(); n++) {
                JSONObject aIngredient = ingredientsArray.getJSONObject(n);
                allIngredients.add(new Ingredients(String.valueOf(i), aIngredient.getString(ING_QUANTITY),
                        aIngredient.getString(ING_MSR), aIngredient.getString(ING_NAME)));

            }
        }

        return allIngredients;

    }

    public static ArrayList<Step> getAllStepsFromJson(Context context, String bakingStr)
            throws JSONException {

        final String STEP_OBJ = "steps";
        final String STEP_ID = "id";
        final String STEP_SDESC = "shortDescription";
        final String STEP_DESC = "description";
        final String STEP_VIDEO = "videoURL";
        final String STEP_THUMB = "thumbnailURL";

        JSONArray recipesArray = new JSONArray(bakingStr);
        int recipesArraySize = recipesArray.length();
        ArrayList<Step> allSteps = new ArrayList<Step>();

        for (int i = 0; i < recipesArraySize; i++) {
            JSONArray stepsArray = recipesArray.getJSONObject(i).getJSONArray(STEP_OBJ);
            for (int n = 0; n < stepsArray.length(); n++) {
                JSONObject aStep = stepsArray.getJSONObject(n);
                allSteps.add(new Step(
                        Integer.toString(i),
                        aStep.getString(STEP_ID),
                        aStep.getString(STEP_SDESC),
                        aStep.getString(STEP_DESC),
                        aStep.getString(STEP_VIDEO),
                        aStep.getString(STEP_THUMB)));

            }
        }

        return allSteps;
    }
}
