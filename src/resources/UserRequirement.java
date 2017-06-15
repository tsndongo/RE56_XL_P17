package resources;

/**
 * Created by Elias on 6/13/2017.
 */
public class UserRequirement {
    private int data;
    private int voice;
    private int video;

    public UserRequirement() {
        this.data = 0;
        this.voice = 0;
        this.video = 0;
    }

    public int getTotalRequirements(){
        return (this.data + this.video + this.voice);
    }

    public UserRequirement(int data, int voice, int video) {
        this.data = data;
        this.video = video;
        this.voice = voice;
    }

    public int getData() {
        return this.data;
    }

    public int getVoice() {
        return this.voice;
    }

    public int getVideo(){
        return this.video;
    }

    public void setData(int data){
        this.data = data;
    }

    public void setVideo (int video){
        this.video = video;
    }

    public void setVoice (int voice){
        this.voice = voice;
    }

    public boolean containsNegativeValues() {
        return (this.data < 0 || this.voice < 0 || this.video < 0);
    }

    public boolean isEmpty(){
        return this.data <= 0 && this.voice <= 0 && this.video <=0;
    }
}
