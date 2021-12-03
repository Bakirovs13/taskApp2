package kg.geektech.taskapp36.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;

import java.io.Serializable;

@Entity
public class Task implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private  long id;
    private  String docId;
   private String text;
    private Long createdAt;
    private String ImgUrl;


    public Task() {
    }


    public Task(String text, Long createdAt) {
        this.text = text;
        this.createdAt = createdAt;
    }


    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }


}
