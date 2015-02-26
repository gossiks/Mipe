package com.kazin.mvp.mipeflip;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.Pair;

public class ImageResizer {

	/*
	 * Call this static method to resize an image to a specified width and height.
	 * 
	 * @param targetWidth  The width to resize to.
	 * @param targetHeight The height to resize to.
	 * @returns 		   The resized image as a Bitmap.
	 */
	public static Bitmap resizeImage(byte[] imageData, int targetWidth, int targetHeight) {
		// Use BitmapFactory to decode the image
        BitmapFactory.Options options = new BitmapFactory.Options();
        
        // inSampleSize is used to sample smaller versions of the image
        options.inSampleSize = calculateInSampleSize(options, targetWidth, targetHeight);
        Log.d("ImageResizer.resizeImage","inSampleSize: "+options.inSampleSize);
        if (options.inSampleSize<4){ //TODO get rid of walkaround. Implemented here for compatibility issues crash "bfb1a9055f3124f4b9d659b965aa026a"
            options.inSampleSize = 4;
        }
        // Decode bitmap with inSampleSize and target dimensions set
        options.inJustDecodeBounds = false;	
        
        Bitmap reducedBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length, options);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(reducedBitmap, targetWidth, targetHeight, false);

        return resizedBitmap;        
	}

	public static Bitmap resizeImageMaintainAspectRatio(byte[] imageData, int longerSideTarget) {
        Pair<Integer, Integer> dimensions = getDimensions(imageData);
		
        // Determine the aspect ratio (width/height) of the image
        int imageWidth = dimensions.first;
        int imageHeight = dimensions.second;
        float ratio = (float) dimensions.first / dimensions.second;
        
        int targetWidth;
        int targetHeight;

        // Determine portrait or landscape
        if (imageWidth > imageHeight) {
            // Landscape image. ratio (width/height) is > 1
        	targetWidth = longerSideTarget; 
            targetHeight = Math.round(longerSideTarget / ratio);
        }
        else {
            // Portrait image. ratio (width/height) is < 1
        	targetHeight =  Math.round(longerSideTarget*(3/2));
            targetWidth = Math.round(targetHeight * ratio);
        }
        
		return resizeImage(imageData, targetWidth, targetHeight);
	}
	
	public static Pair<Integer, Integer> getDimensions(byte[] imageData) {
		// Use BitmapFactory to decode the image
        BitmapFactory.Options options = new BitmapFactory.Options();

        // Only decode the bounds of the image, not the whole image, to get the dimensions
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(imageData, 0, imageData.length, options);
        
        return new Pair<Integer, Integer>(options.outWidth, options.outHeight);
	}
	
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	
	    if (height > reqHeight || width > reqWidth) {
	
	        final int halfHeight = height / 2;
	        final int halfWidth = width / 2;
	
	        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
	        // height and width larger than the requested height and width.
	        while ((halfHeight / inSampleSize) > reqHeight
	                && (halfWidth / inSampleSize) > reqWidth) {
	            inSampleSize *= 2;
	        }
	    }
	
	    return inSampleSize;
	}
}
