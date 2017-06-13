package resources;

/**
 * Created by Syntiche on 5/16/2017.
 */
public class UserRequirement {

    private double data;
    private double voice;
    private double video;

    UserRequirement(double data, double voice, double video){
        this.data = data;
        this.voice = voice;
        this.video = video;
    }

    UserRequirement(){
        this.data = 0;
        this.voice = 0;
        this.video = 0;
    }

    public void addData(double data){
        this.data += data;
    }

    public void addVoice(double voice){
        this.voice += voice;
    }

    public void addVideo(double video){
        this.video += video;
    }

    public void addRequirement(double data, double voice, double video){
        this.data += data;
        this.voice += voice;
        this.video += video;
    }

    public void removeRequirement (double data, double voice, double video){
        this.data -= data;
        this.voice -= voice;
        this.video -= video;
    }

    public void removeRequirement(double value){
        if (data >0 && voice > 0 && video > 0 ) {
            this.removeData(value / 3);
            this.removeVoice(value / 3);
            this.removeVideo(value / 3);
        }
        else if (data > 0 && voice > 0 && video ==0){
            this.removeData(value / 2);
            this.removeVoice(value / 2);
        }
        else if (data == 0 && voice > 0 && video >0){
            this.removeVoice(value / 2);
            this.removeVideo(value / 2);
        }
        else if (data > 0 && voice == 0 && video >0){
            this.removeData(value / 2);
            this.removeVideo(value / 2);
        }
        else if (data == 0 && voice == 0 && video > 0 ){
            this.removeVideo(value);
        }
        else if (data == 0 && voice > 0 && video == 0 ){
            this.removeVoice(value);
        }
        else if (data > 0 && voice == 0 && video == 0 ){
            this.removeData(value);
        }
    }

    public void removeData(double data){
        this.data -= data;
        if (this.data < 0) {
            this.data = 0;
        }
    }

    public void removeVoice(double voice){
        this.voice -= voice;
        if (this.voice < 0 ){
            this.voice = 0;
        }
    }

    public void removeVideo(double video){
        this.video -= video;
        if (this.video < 0){
            this.video = 0;
        }
    }

    public boolean isEmpty(){
        return data == 0 && voice == 0 && video == 0;
    }


}
