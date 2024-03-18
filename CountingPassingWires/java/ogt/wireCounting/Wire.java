package ogt.wireCounting;

public record Wire(int number, String from, String to) {
    @Override
    public String toString() {
        return String.format("Wire-number=%3d, from=%3s, to=%3s", number, from, to);
    }
}
