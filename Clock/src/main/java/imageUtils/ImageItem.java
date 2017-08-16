package imageUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.Serializable;

public class ImageItem implements Serializable {
	public boolean isEditArtcle;
	public String imageId;
	public String thumbnailPath;
	public String imagePath;
    public long imageDate;
    public String longtitude;
    public String latitude;
	private Bitmap bitmap;
	public boolean isSelected = false;
	public boolean isError;

	public int height;
	public int width;
	
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public String getThumbnailPath() {
		return thumbnailPath;
	}
	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

    public long getImageDate() {
        return imageDate;
    }

    public void setImageDate(long imageDate) {
        this.imageDate = imageDate;
    }

    public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public Bitmap getBitmap() {
		if(bitmap == null){
            try {
                bitmap = Bimp.revitionImageSize(imagePath);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	public int[] getBitmapwh(String path){

		BitmapFactory.Options options = new BitmapFactory.Options();

		/**
		 * 最关键在此，把options.inJustDecodeBounds = true;
		 * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
		 */
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
		/**
		 *options.outHeight为原始图片的高
		 */
		int[] wh = {options.outWidth,options.outHeight};
		return wh;
	}
	
}
