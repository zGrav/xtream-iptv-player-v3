package z.xtreamiptv.playerv3.model;

public class EpgChannelModel {
    String next = "";
    String nowPlaying = "";

    public String getNowPlaying() {
        return this.nowPlaying;
    }

    public void setNowPlaying(String nowPlaying) {
        this.nowPlaying = nowPlaying;
    }

    public String getNext() {
        return this.next;
    }

    public void setNext(String next) {
        this.next = next;
    }
}
