import java.util.ArrayList;

public class Player {
    private int scoreMinute;
    private int scoreSeconds;
    private final ArrayList<Card> cards;

    public Player(){
        this.scoreMinute = 0;
        this.scoreSeconds = 0;
        this.cards = new ArrayList<>();
    }

    public void addPenalty(){
        this.scoreMinute += 1;
    }
    public void addCard(Card card){
        this.cards.add(card);
    }
    public void calculateScore(){
        for(Card card : this.cards){
            this.scoreMinute += card.getValueMinutes();
            this.scoreSeconds += card.getValueSeconds();
        }
    }
    public void flushHand(){
        this.cards.clear();
    }
    public Card getCard(int index){
        return this.cards.get(index);
    }
    public int getMinutes(){
        return this.scoreMinute;
    }
    public int getSeconds(){
        return this.scoreSeconds;
    }
    public int getNbCards(){
        return this.cards.size();
    }
    public String getScore(){
        return this.scoreMinute + " minutes, " + this.scoreSeconds + " seconds";
    }
    public void playCard(Card card){
        if(card != null) this.cards.remove(card);
    }
    public void playCards(ArrayList<Card> playedCards){
        if(playedCards != null){
            for(Card card : playedCards) this.cards.remove(card);
        }
    }
    public void showHand(int playerId){
        StringBuilder handDisplay = new StringBuilder("Player " + playerId + ": [");
        StringBuilder indexDisplay = new StringBuilder("          [");
        for(int i=0; i<this.cards.size(); i++){
            Card card = this.cards.get(i);
            handDisplay.append(card.shortString()).append(", ");
            indexDisplay.append(i+1).append(" ");
            if(card.getFigure().equals("10")) indexDisplay.append(" ");
            indexDisplay.append(", ");
        }
        System.out.println(handDisplay + "]\n" + indexDisplay + "]");
    }
}
