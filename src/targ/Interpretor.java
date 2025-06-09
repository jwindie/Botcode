package targ;

public interface Interpretor {
    public abstract void activate();
    public abstract void charge();

    public abstract void forward();
    public abstract void reverse();
    public abstract void move(Literal dir);

    public abstract void left();
    public abstract void right();
    public abstract void turn(Literal dir);

    public abstract void link(Literal target);
    public abstract void swap (Literal target);
    public abstract void unlink (Literal target);

    public abstract void runFunc (String pointer);
}
