package com.example.fioni.bakingapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by fioni on 10/14/2017.
 */

public class BakingContract {

    public static final String AUTHORITY =
            "com.example.fioni.bakingapp.provider";

    public static final Uri BASE_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY);

    public static final String PATH_RECIPES = "recipes";
    public static final String PATH_INGREDIENTS = "ingredients";
    public static final String PATH_STEPS = "steps";

    public static final class Recipes implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES).build();

        public static final String TABLE_NAME_RECIPES = "recipes";

        public static final String COL_R_ID = "recipe_id";
        public static final String COL_R_NAME = "recipe_name";
        public static final String COL_R_SERVINGS = "recipe_servings";
        public static final String COL_R_IMAGE = "recipe_image";

    }

    public static final class Ingredients implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENTS).build();

        public static final String TABLE_NAME_INGREDIENTS = "ingredients";

        public static final String COL_I_RID = "i_recipe_id";
        public static final String COL_I_QUANTITY = "i_quantity";
        public static final String COL_I_MEASURE = "i_measure";
        public static final String COL_I_NAME = "i_name";

    }

    public static final class Steps implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STEPS).build();

        public static final String TABLE_NAME_STEPS = "steps";

        public static final String COL_S_RID = "s_recipe_id";
        public static final String COL_S_ID = "s_id";
        public static final String COL_S_SHORT_DESC = "s_short_desc";
        public static final String COL_S_DESC = "s_desc";
        public static final String COL_S_VIDEO_URL = "s_video_url";
        public static final String COL_S_THUMB_URL = "s_thumb_url";


    }

}
