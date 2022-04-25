public class Card implements Comparable<Card>{
    private int position;
    private int valueMinutes;
    private int valueSeconds;
    private final String color;
    private final String figure;

    public Card(String _color, String _figure){
        this.color = _color;
        this.figure = _figure;

        switch (this.figure) {
            case "Ace":
                this.position = 0;
                this.valueMinutes = 4;
                this.valueSeconds = 0;
                break;
            case "King":
                this.position = 1;
                this.valueMinutes = 3;
                this.valueSeconds = 0;
                break;
            case "Queen":
                this.position = 2;
                this.valueMinutes = 2;
                this.valueSeconds = 0;
                break;
            case "Jack":
                this.position = 3;
                this.valueMinutes = 1;
                this.valueSeconds = 0;
                break;
            case "10":
                this.position = 4;
                this.valueMinutes = 10;
                this.valueSeconds = 0;
                break;
            case "9":
                this.position = 5;
                this.valueMinutes = 0;
                this.valueSeconds = 9;
                break;
            case "8":
                this.position = 6;
                this.valueMinutes = 0;
                this.valueSeconds = 8;
                break;
            case "7":
                this.position = 7;
                this.valueMinutes = 0;
                this.valueSeconds = 7;
                break;
            case "6":
                this.position = 8;
                this.valueMinutes = 0;
                this.valueSeconds = 6;
                break;
            case "5":
                this.position = 9;
                this.valueMinutes = 0;
                this.valueSeconds = 5;
                break;
            case "4":
                this.position = 10;
                this.valueMinutes = 0;
                this.valueSeconds = 4;
                break;
            case "3":
                this.position = 11;
                this.valueMinutes = 0;
                this.valueSeconds = 3;
                break;
            case "2":
                this.position = 12;
                this.valueMinutes = 0;
                this.valueSeconds = 2;
                break;
        }
        this.position *= 4;
        switch (this.color) {
            case "hearts":
                this.position += 1;
                break;
            case "clubs":
                this.position += 2;
                break;
            case "diamonds":
                this.position += 3;
                break;
        }
    }

    public String getColor(){
        return this.color;
    }
    public String getFigure(){
        return this.figure;
    }
    public int getPosition(){
        return this.position;
    }
    public int getScore(){
        int temp = 0;
        switch(this.color){
            case "hearts":
                temp += 100;
                break;
            case "clubs":
                temp += 200;
                break;
            case "diamonds":
                temp += 300;
                break;
        }
        switch (this.figure) {
            case "Ace":
                temp += 12;
                break;
            case "King":
                temp += 11;
                break;
            case "Queen":
                temp += 10;
                break;
            case "Jack":
                temp += 9;
                break;
            case "9":
                temp += 1;
                break;
            case "8":
                temp += 2;
                break;
            case "7":
                temp += 3;
                break;
            case "6":
                temp += 4;
                break;
            case "5":
                temp += 5;
                break;
            case "4":
                temp += 6;
                break;
            case "3":
                temp += 7;
                break;
            case "2":
                temp += 8;
                break;
        }
        return temp;
    }
    public int getValueMinutes(){
        return this.valueMinutes;
    }
    public int getValueSeconds(){
        return this.valueSeconds;
    }
    public String shortString(){
        String shortName = "";
        switch (this.figure) {
            case "Ace":
                shortName += "A";
                break;
            case "King":
                shortName += "K";
                break;
            case "Queen":
                shortName += "Q";
                break;
            case "Jack":
                shortName += "J";
                break;
            default:
                shortName += this.figure;
                break;
        }
        switch (this.color) {
            case "clubs":
                shortName += "c";
                break;
            case "diamonds":
                shortName += "d";
                break;
            case "hearts":
                shortName += "h";
                break;
            case "spades":
                shortName += "s";
                break;
        }
        return shortName;
    }

    @Override
    public String toString(){
        return this.figure + " of " + this.color;
    }

    @Override
    public int compareTo(Card card) {
        return Integer.compare(this.getScore(), card.getScore());
    }
}
