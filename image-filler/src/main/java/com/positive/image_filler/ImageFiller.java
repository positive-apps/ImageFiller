package com.positive.image_filler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Ori.Mentesh on 06-Dec-16.
 */

public class ImageFiller {

    private final String TAG = getClass().getSimpleName();

    // Directions:
    public static final int FILL_TOP_TO_BOTTOM = 1;
    public static final int FILL_BOTTOM_TO_TOP = 2;
    public static final int FILL_RIGHT_TO_LEFT = 3;
    public static final int FILL_LEFT_TO_RIGHT = 4;

    public static final int DURATION_VERY_SLOW = 1;
    public static final int DURATION_SLOW = 2;
    public static final int DURATION_MEDIUM = 5;
    public static final int DURATION_FAST = 10;
    public static final int DURATION_VERY_FAST = 20;

    // Parameters
    private int mPercentage;
    private Context context;
    private ImageView imageHolder;
    private int toPercentage;
    private int emptyVersionResId;
    private int fullVersionResId;
    private int gradientResId;
    private int gradientPercentage;
    private int direction;
    private boolean animate;
    private int duration = DURATION_MEDIUM;

    // ---------------------------------------------------------------------------------------------

    public ImageFiller with(Context context) {
        this.context = context;
        return this;
    }

    public ImageFiller onHolder(ImageView imageHolder) {
        this.imageHolder = imageHolder;
        return this;
    }

    public ImageFiller toPercentage(int toPercentage) {
        this.toPercentage = toPercentage;
        return this;
    }

    public ImageFiller emptyVersionResId(int emptyVersionResId) {
        this.emptyVersionResId = emptyVersionResId;
        return this;
    }

    public ImageFiller fullVersionResId(int fullVersionResId) {
        this.fullVersionResId = fullVersionResId;
        return this;
    }

    public ImageFiller gradientVersionResId(int gradientResId) {
        this.gradientResId = gradientResId;
        return this;
    }

    public ImageFiller gradientPercentage(int gradientPercentage) {
        this.gradientPercentage = gradientPercentage;
        return this;
    }

    public ImageFiller direction(int direction) {
        this.direction = direction;
        return this;
    }

    public ImageFiller animate(boolean animate) {
        this.animate = animate;
        return this;
    }

    public ImageFiller duration(int duration) {
        if (duration == DURATION_VERY_SLOW
                || duration == DURATION_SLOW
                || duration == DURATION_MEDIUM
                || duration == DURATION_FAST
                || duration == DURATION_VERY_FAST) {
            this.duration = duration;
        } else {
            this.duration = DURATION_MEDIUM;
        }
        return this;
    }

    public void go() {

        if (validateParams()) {

            if (animate){
                // Animate:
                if (gradientResId != 0) {
                    // Animate with gradient:
                    fillGradientWithAnimation(context, toPercentage, imageHolder, emptyVersionResId, fullVersionResId, gradientResId, gradientPercentage, direction);
                } else {
                    // Animate with no gradient:
                    fillWithAnimation(context, toPercentage, imageHolder, emptyVersionResId, fullVersionResId, direction);
                }
            } else {
                // No animation
                if (gradientResId != 0) {
                    // No animation With gradient:
                    fillGradient(context, toPercentage, imageHolder, emptyVersionResId, fullVersionResId, gradientResId, gradientPercentage, direction);
                } else {
                    // No animation No gradient:
                    fill(context, toPercentage, imageHolder, emptyVersionResId, fullVersionResId, direction);
                }
            }

        } else {
            Toast.makeText(context, "Not enough params to do image fill", Toast.LENGTH_SHORT).show();
        }

    }

    // ---------------------------------------------------------------------------------------------

    private boolean validateParams() {

        boolean validated = true;

        // Context:
        if (context == null) {
            validated = false;
            Log.i(TAG, "No Context");
        }

        // Image holder:
        if (imageHolder == null) {
            validated = false;
            Log.i(TAG, "No Image Holder");
        }

        // Percentage:
        if (toPercentage < 0 || toPercentage > 100) {
            validated = false;
            Log.i(TAG, "No Percentage");
        }

        // Empty and Full versions:
        if (emptyVersionResId == 0 || fullVersionResId == 0) {
            validated = false;
            Log.i(TAG, "No Empty and Full versions");
        }

        // Direction:
        if (direction != FILL_TOP_TO_BOTTOM && direction != FILL_BOTTOM_TO_TOP &&
                direction != FILL_RIGHT_TO_LEFT && direction != FILL_LEFT_TO_RIGHT) {
            validated = false;
            Log.i(TAG, "No Direction");
        }

        // Gradient:
        if (gradientPercentage > 0 || gradientPercentage < 100) {
            if (gradientResId == 0) {
                validated = false;
                Log.i(TAG, "No Gradient");
            }
        }

        return validated;
    }

    // ---------------------------------------------------------------------------------------------

    private void fill(Context context, int toPercentage, ImageView imageHolder, int emptyVersionResId, int fullVersionResId, int direction){

        // Set the image to the empty version image:
        imageHolder.setImageResource(emptyVersionResId);

        if (toPercentage > 0 && toPercentage <= 100) {

            switch (direction) {
                case FILL_TOP_TO_BOTTOM:
                    imageHolder.setImageBitmap(fillTopToBottom(context, toPercentage, emptyVersionResId, fullVersionResId));
                    break;
                case FILL_BOTTOM_TO_TOP:
                    imageHolder.setImageBitmap(fillBottomToTop(context, toPercentage, emptyVersionResId, fullVersionResId));
                    break;
                case FILL_RIGHT_TO_LEFT:
                    imageHolder.setImageBitmap(fillRightToLeft(context, toPercentage, emptyVersionResId, fullVersionResId));
                    break;
                case FILL_LEFT_TO_RIGHT:
                    imageHolder.setImageBitmap(fillLeftToRight(context, toPercentage, emptyVersionResId, fullVersionResId));
                    break;
            }
        }
    }

    private void fillWithAnimation(final Context context, final int toPercentage, final ImageView imageHolder, final int emptyVersionResId, final int fullVersionResId, final int direction){

        final Handler handler = new Handler();

        mPercentage = 0;

        imageHolder.setImageResource(emptyVersionResId);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPercentage += duration;

                switch (direction) {
                    case FILL_TOP_TO_BOTTOM:
                        imageHolder.setImageBitmap(fillTopToBottom(context, mPercentage, emptyVersionResId, fullVersionResId));
                        break;
                    case FILL_BOTTOM_TO_TOP:
                        imageHolder.setImageBitmap(fillBottomToTop(context, mPercentage, emptyVersionResId, fullVersionResId));
                        break;
                    case FILL_RIGHT_TO_LEFT:
                        imageHolder.setImageBitmap(fillRightToLeft(context, mPercentage, emptyVersionResId, fullVersionResId));
                        break;
                    case FILL_LEFT_TO_RIGHT:
                        imageHolder.setImageBitmap(fillLeftToRight(context, mPercentage, emptyVersionResId, fullVersionResId));
                        break;
                }

                if (mPercentage < toPercentage)
                    handler.postDelayed(this, 10);
            }
        }, 10);
    }

    private void fillGradient(Context context, int toPercentage, ImageView imageHolder, int emptyVersionResId, int fullVersionResId, int gradientResId, int gradientPercentage, int direction){

        // Set the image to the empty version image:
        imageHolder.setImageResource(emptyVersionResId);

        if (toPercentage > 0 && toPercentage <= 100) {

            switch (direction) {
                case FILL_TOP_TO_BOTTOM:
                    if (toPercentage <= gradientPercentage) {
                        imageHolder.setImageBitmap(fillTopToBottom(context, toPercentage, emptyVersionResId, gradientResId));
                    } else {
                        imageHolder.setImageBitmap(fillTopToBottom(context, toPercentage, emptyVersionResId, fullVersionResId));
                    }
                    break;
                case FILL_BOTTOM_TO_TOP:
                    if (toPercentage <= gradientPercentage) {
                        imageHolder.setImageBitmap(fillBottomToTop(context, toPercentage, emptyVersionResId, gradientResId));
                    } else {
                        imageHolder.setImageBitmap(fillBottomToTop(context, toPercentage, emptyVersionResId, fullVersionResId));
                    }
                    break;
                case FILL_RIGHT_TO_LEFT:
                    if (toPercentage <= gradientPercentage) {
                        imageHolder.setImageBitmap(fillRightToLeft(context, toPercentage, emptyVersionResId, gradientResId));
                    } else {
                        imageHolder.setImageBitmap(fillRightToLeft(context, toPercentage, emptyVersionResId, fullVersionResId));
                    }
                    break;
                case FILL_LEFT_TO_RIGHT:
                    if (toPercentage <= gradientPercentage) {
                        imageHolder.setImageBitmap(fillLeftToRight(context, toPercentage, emptyVersionResId, gradientResId));
                    } else {
                        imageHolder.setImageBitmap(fillLeftToRight(context, toPercentage, emptyVersionResId, fullVersionResId));
                    }
                    break;
            }
        }
    }

    private void fillGradientWithAnimation(final Context context, final int toPercentage, final ImageView imageHolder, final int emptyVersionResId, final int fullVersionResId, final int gradientResId, final int gradientPercentage, final int direction){

        final Handler handler = new Handler();

        mPercentage = 0;

        imageHolder.setImageResource(emptyVersionResId);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPercentage += duration;

                switch (direction) {
                    case FILL_TOP_TO_BOTTOM:
                        if (toPercentage <= gradientPercentage) {
                            imageHolder.setImageBitmap(fillTopToBottom(context, mPercentage, emptyVersionResId, gradientResId));
                        } else {
                            imageHolder.setImageBitmap(fillTopToBottom(context, mPercentage, emptyVersionResId, fullVersionResId));
                        }
                        break;
                    case FILL_BOTTOM_TO_TOP:
                        if (toPercentage <= gradientPercentage) {
                            imageHolder.setImageBitmap(fillBottomToTop(context, mPercentage, emptyVersionResId, gradientResId));
                        } else {
                            imageHolder.setImageBitmap(fillBottomToTop(context, mPercentage, emptyVersionResId, fullVersionResId));
                        }
                        break;
                    case FILL_RIGHT_TO_LEFT:
                        if (toPercentage <= gradientPercentage) {
                            imageHolder.setImageBitmap(fillRightToLeft(context, mPercentage, emptyVersionResId, gradientResId));
                        } else {
                            imageHolder.setImageBitmap(fillRightToLeft(context, mPercentage, emptyVersionResId, fullVersionResId));
                        }
                        break;
                    case FILL_LEFT_TO_RIGHT:
                        if (toPercentage <= gradientPercentage) {
                            imageHolder.setImageBitmap(fillLeftToRight(context, mPercentage, emptyVersionResId, gradientResId));
                        } else {
                            imageHolder.setImageBitmap(fillLeftToRight(context, mPercentage, emptyVersionResId, fullVersionResId));
                        }
                        break;
                }

                if (mPercentage < toPercentage)
                    handler.postDelayed(this, 10);
            }
        }, 10);
    }

    // ---------------------------------------------------------------------------------------------

    // Top -> Bottom
    private Bitmap fillTopToBottom(Context context, int percentage, int emptyVersionResId, int fullVersionResId) {
        Bitmap bitmapOriginal = BitmapFactory.decodeResource(context.getResources(), emptyVersionResId);
        Bitmap bitmapTarget = BitmapFactory.decodeResource(context.getResources(), fullVersionResId);

        int heightToCrop = bitmapTarget.getHeight() * percentage / 100;

        Bitmap croppedBitmap = Bitmap.createBitmap(bitmapTarget, 0, 0, bitmapTarget.getWidth(), heightToCrop);

        Bitmap bmOverlay = Bitmap.createBitmap(bitmapOriginal.getWidth(), bitmapOriginal.getHeight(), bitmapOriginal.getConfig());

        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bitmapOriginal, new Matrix(), null);
        canvas.drawBitmap(croppedBitmap, new Matrix(), null);

        return bmOverlay;
    }

    // Bottom -> Top
    private Bitmap fillBottomToTop(Context context, int percentage, int emptyVersionResId, int fullVersionResId) {
        Bitmap bitmapOriginal = BitmapFactory.decodeResource(context.getResources(), emptyVersionResId);
        Bitmap bitmapTarget = BitmapFactory.decodeResource(context.getResources(), fullVersionResId);

        int heightToCrop = bitmapTarget.getHeight() * (100 - percentage) / 100;

        Bitmap croppedBitmap = Bitmap.createBitmap(bitmapTarget, 0, heightToCrop, bitmapTarget.getWidth(), bitmapTarget.getHeight() - heightToCrop);

        Bitmap bmOverlay = Bitmap.createBitmap(bitmapOriginal.getWidth(), bitmapOriginal.getHeight(), bitmapOriginal.getConfig());

        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bitmapOriginal, new Matrix(), null);
        canvas.drawBitmap(croppedBitmap,
                canvas.getWidth() - croppedBitmap.getWidth(),
                canvas.getHeight() - croppedBitmap.getHeight(), null);

        return bmOverlay;
    }

    // Right -> Left
    private Bitmap fillRightToLeft(Context context, int percentage, int emptyVersionResId, int fullVersionResId) {
        Bitmap bitmapOriginal = BitmapFactory.decodeResource(context.getResources(), emptyVersionResId);
        Bitmap bitmapTarget = BitmapFactory.decodeResource(context.getResources(), fullVersionResId);

        int widthToCrop = bitmapTarget.getWidth() * (100 - percentage) / 100;

        Bitmap croppedBitmap = Bitmap.createBitmap(bitmapTarget, widthToCrop, 0, bitmapTarget.getWidth() - widthToCrop, bitmapTarget.getHeight());

        Bitmap bmOverlay = Bitmap.createBitmap(bitmapOriginal.getWidth(), bitmapOriginal.getHeight(), bitmapOriginal.getConfig());

        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bitmapOriginal, new Matrix(), null);
        canvas.drawBitmap(croppedBitmap,
                canvas.getWidth() - croppedBitmap.getWidth(),
                canvas.getHeight() - croppedBitmap.getHeight(), null);

        return bmOverlay;
    }

    // Left -> Right
    private Bitmap fillLeftToRight(Context context, int percentage, int emptyVersionResId, int fullVersionResId) {
        Bitmap bitmapOriginal = BitmapFactory.decodeResource(context.getResources(), emptyVersionResId);
        Bitmap bitmapTarget = BitmapFactory.decodeResource(context.getResources(), fullVersionResId);

        int widthToCrop = bitmapTarget.getWidth() * percentage / 100;

        Bitmap croppedBitmap = Bitmap.createBitmap(bitmapTarget, 0, 0, widthToCrop, bitmapTarget.getHeight());

        Bitmap bmOverlay = Bitmap.createBitmap(bitmapOriginal.getWidth(), bitmapOriginal.getHeight(), bitmapOriginal.getConfig());

        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bitmapOriginal, new Matrix(), null);
        canvas.drawBitmap(croppedBitmap, new Matrix(), null);

        return bmOverlay;
    }

}
