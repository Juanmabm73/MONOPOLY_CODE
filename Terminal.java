public abstract class Terminal {
    public Terminal() {
        
    }

    public abstract void show(String s, Object...args);

    public abstract int read();
}
