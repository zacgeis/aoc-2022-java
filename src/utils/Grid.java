package utils;

public class Grid<T> {
    public record Loc(int x, int y) {}
    // TODO: Add a dup function that generates another grid of another type.
    //       Can be used for visited tracking.
}
