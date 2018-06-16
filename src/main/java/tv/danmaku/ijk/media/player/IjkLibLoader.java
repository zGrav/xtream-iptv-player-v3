package tv.danmaku.ijk.media.player;

public interface IjkLibLoader {
    void loadLibrary(String str) throws UnsatisfiedLinkError, SecurityException;
}
