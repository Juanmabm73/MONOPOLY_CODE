public abstract class Terminal implements Serializable{
    public Terminal() {
        
    }

    public abstract void show(String s, Object...args);

    public abstract int read();
}
